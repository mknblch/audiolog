package de.mknblch.audiolog

import de.mknblch.audiolog.config.AudioQueue
import kotlinx.coroutines.delay
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.event.EventListener
import org.springframework.core.io.Resource

@SpringBootApplication
class AudioLog : CommandLineRunner {

    @Value("\${open_sound:sounds/mail2.wav}")
    private lateinit var resource: Resource

    @Autowired
    private lateinit var audioQueue: AudioQueue

    override fun run(vararg args: String?) {
        logger.info("Waiting for data, press Ctrl+C to shutdown application")
        audioQueue.enqueue(resource, delay = 500)
        Thread.currentThread().join()
    }

    @EventListener
    fun onGameEvent(event: GameEvent) {
        logger.debug(event.toString())
    }

    @EventListener
    fun onEqEvent(event: EqEvent) {
        logger.trace(event.toString())
    }

    companion object {

        private val logger: Logger = LoggerFactory.getLogger(AudioLog::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(AudioLog::class.java, *args)
        }
    }
}