package de.mknblch.audiolog.common

import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl
import javax.sound.sampled.LineEvent


class AudioPlayer(
    private val resource: Resource,
    val arguments: Map<String, String> = emptyMap()
) {

    init {
        if (!resource.exists()) {
            throw IllegalArgumentException("resource not found $resource")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun play() {
        GlobalScope.launch(Dispatchers.IO) {
            Companion.play(
                resource = resource,
                delay = arguments.getOrDefault("delay", "0").toLong(),
                volume = arguments.getOrDefault("volume", "100").toDouble()
            )
        }
    }


    companion object {

        private val logger: Logger = LoggerFactory.getLogger(AudioPlayer::class.java)

        fun playAsync(
            resource: Resource, delay: Number = 0L, volume: Number = 100.0, lambda: () -> Unit = {
                logger.trace("${resource.file}[delay=$delay, volume=$volume] stopped")
            }
        ) {
            GlobalScope.launch(Dispatchers.IO) {
                play(resource, delay, volume, lambda)
            }
        }

        suspend fun play(
            resource: Resource, delay: Number = 0L, volume: Number = 100.0, lambda: () -> Unit = {
                logger.trace("${resource.file}[delay=$delay, volume=$volume] stopped")
            }
        ) {
            val clip = AudioSystem.getClip()
            AudioSystem.getAudioInputStream(resource.inputStream).use { stream ->
                clip.addLineListener { e: LineEvent ->
                    if (e.type === LineEvent.Type.STOP) {
                        clip.close()
                        lambda()
                    }
                }
                clip.open(stream)
                val gainControl = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
                val range = gainControl.maximum - gainControl.minimum;
                gainControl.value = (range * (volume.toDouble() / 100.0) + gainControl.minimum).toFloat()
                if (delay.toLong() > 0) delay(delay.toLong())
                clip.start()
            }
        }
    }
}