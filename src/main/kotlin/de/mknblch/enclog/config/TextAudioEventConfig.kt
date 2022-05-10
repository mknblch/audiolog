package de.mknblch.enclog.config

import de.mknblch.enclog.EqEvent
import de.mknblch.enclog.common.LogParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.core.io.Resource
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.Reader
import java.lang.IllegalStateException
import javax.annotation.PostConstruct

@Configuration
class TextAudioEventConfig() {

    @Value("\${audio_config_file:audio_config.cfg}")
    private lateinit var resource: Resource

    @PostConstruct
    fun init() {
        resource.inputStream.reader(Charsets.UTF_8).forEachLine {
            logger.trace("parsing: $it")

        }
    }

    @EventListener
    fun onEvent(event: EqEvent) {
        logger.trace("$event")

    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TextAudioEventConfig::class.java)
    }
}