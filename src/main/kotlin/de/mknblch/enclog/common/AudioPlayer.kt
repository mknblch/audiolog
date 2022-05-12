package de.mknblch.enclog.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.springframework.core.io.Resource
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl
import javax.sound.sampled.LineEvent
import kotlin.math.ln


class AudioPlayer(
    private val resource: Resource,
    private val arguments: Map<String, String> = emptyMap()
) {

    init {
        if (!resource.exists()) {
            throw IllegalArgumentException("resource not found $resource")
        }
    }

    fun play() {
        Companion.play(
            resource,
            arguments.getOrDefault("delay", "0").toLong(),
            arguments.getOrDefault("volume", "1.0").toDouble()
        )
    }

    companion object {

        fun play(resource: Resource, delay: Long = 0L, volume: Double = 1.0) {
            val clip = AudioSystem.getClip()
            GlobalScope.async(Dispatchers.IO) {
                AudioSystem.getAudioInputStream(resource.inputStream).use { stream ->
                    clip.addLineListener { e: LineEvent ->
                        if (e.type === LineEvent.Type.STOP) {
                            clip.close()
                        }
                    }
                    clip.open(stream)
                    val gainControl = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
                    val dB = (ln(volume) * 20.0).toFloat()
                    gainControl.value = dB
                    if (delay > 0) delay(delay)
                    clip.start()
                }
            }
        }
    }
}