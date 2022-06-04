package de.mknblch.audiolog.config

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.io.BufferedInputStream
import java.lang.IllegalArgumentException
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl
import javax.sound.sampled.LineEvent
import kotlin.concurrent.scheduleAtFixedRate

data class AudioTask(
    val resource: Resource,
    val playTime: Instant,
    val volume: Int, // 0 - 100
    val id: String = "none",
    var played: Boolean = false
)

@Service
class AudioQueue : AutoCloseable {

    private var timer: Timer = Timer()
    private lateinit var timerTask: TimerTask

    private val queue = ConcurrentLinkedQueue<AudioTask>()

    init {
        logger.info("Starting Audio Queue")
        timerTask = timer.scheduleAtFixedRate(0, 10) {
            val iterator = queue.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                when {
                    // remove already played
                    next.played -> {
                        iterator.remove()
                        logger.trace("removing played Task $next")
                    }

                    next.playTime <= Instant.now() -> {
                        iterator.remove()
                        logger.trace("playing $next")
                        GlobalScope.launch(Dispatchers.IO) { play(next) }
                    }
                }
            }
        }
    }

    fun enqueue(resource: Resource, delay: Int = 0, volume: Int = 100, id: String = "none"): AudioTask {

        if (!resource.exists()) throw IllegalArgumentException("resource $resource doesn't exist")

        logger.debug("enqueuing ${resource.filename} (v:$volume) in ${delay}ms")

        return AudioTask(
            resource = resource,
            playTime = Instant.now().plusMillis(delay.toLong()),
            volume = volume,
            id = id
        ).also(queue::add)
    }

    fun dequeue(audioTask: AudioTask): Boolean {
        audioTask.played = true
        return queue.remove(audioTask)
    }

    fun dequeue(id: String) {
        queue.filter { it.id == id }.forEach {
            dequeue(it)
        }
    }

    private fun play(audioTask: AudioTask) {
        val clip = AudioSystem.getClip() // TODO try use {}
        AudioSystem.getAudioInputStream(BufferedInputStream(audioTask.resource.inputStream)).use { stream ->
            clip.addLineListener { e: LineEvent ->
                if (e.type === LineEvent.Type.STOP) clip.close()
            }
            clip.open(stream)
            val gainControl = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
            val range = gainControl.maximum - gainControl.minimum;
            gainControl.value = (range * (audioTask.volume / 100.0) + gainControl.minimum).toFloat()
            audioTask.played = true
            clip.start()
        }
    }

    override fun close() {
        timerTask.cancel()
        timer.cancel()
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AudioQueue::class.java)
    }

}