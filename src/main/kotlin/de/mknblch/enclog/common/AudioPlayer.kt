package de.mknblch.enclog.common

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.core.io.Resource
import java.lang.IllegalArgumentException
import java.util.concurrent.CountDownLatch
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineEvent


class AudioPlayer(private val resource: Resource) {

    init {
        if (!resource.exists()) {
            throw IllegalArgumentException("resource not found $resource")
        }
    }

    fun playBlocking() {
        val clip = AudioSystem.getClip()
        val syncLatch = CountDownLatch(1)
        AudioSystem.getAudioInputStream(resource.inputStream).use { stream ->
            clip.addLineListener { e: LineEvent ->
                if (e.type === LineEvent.Type.STOP) {
                    syncLatch.countDown()
                }
            }
            clip.open(stream)
            clip.start()
        }
        syncLatch.await()
        clip.close()
    }

    fun playRepeatedly(count: Int, delay: Long) {
        runBlocking {
            repeat(count - 1) {
                play()
                delay(delay)
            }
            play()
        }
    }

    fun play() {
        val clip = AudioSystem.getClip()
        AudioSystem.getAudioInputStream(resource.inputStream).use { stream ->
            clip.addLineListener { e: LineEvent ->
                if (e.type === LineEvent.Type.STOP) {
                    clip.close()
                }
            }
            clip.open(stream)
            clip.start()
        }
    }
}