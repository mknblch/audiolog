package de.mknblch.enclog

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.event.EventListener

@SpringBootApplication
class EncLog : CommandLineRunner {

    override fun run(vararg args: String?) {
        logger.info("Joining thread, press Ctrl+C to shutdown application")
        Thread.currentThread().join()
    }

    @EventListener
    fun onEqlogEvent(event: Any) {
        // omit DBG log
        if (event is EqEvent && event.origin == Origin.DBG) return

        logger.trace(event.toString())
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(EncLog::class.java, *args)
        }

        private val logger: Logger = LoggerFactory.getLogger(EncLog::class.java)
    }
}