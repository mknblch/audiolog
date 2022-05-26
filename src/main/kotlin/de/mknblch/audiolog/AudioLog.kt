package de.mknblch.audiolog

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.event.EventListener

@SpringBootApplication
class AudioLog : CommandLineRunner {

    override fun run(vararg args: String?) {
        logger.info("Waiting for data, press Ctrl+C to shutdown application")
        Thread.currentThread().join()
    }

    @EventListener
    fun onGameEvent(event: GameEvent) {
        logger.debug(event.toString())
    }

    companion object {

        private val logger: Logger = LoggerFactory.getLogger(AudioLog::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(AudioLog::class.java, *args)
        }
    }
}