package de.mknblch.enclog.config

import de.mknblch.enclog.EqEvent
import de.mknblch.enclog.Origin
import de.mknblch.enclog.common.AudioConfigParser
import de.mknblch.enclog.common.AudioPlayer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Configuration
class AudioEventConfig() {

    @Value("\${audio_config_file:audio_config.cfg}")
    private lateinit var audioConf: Resource

    private val resourceLoader: ResourceLoader = DefaultResourceLoader()

    private lateinit var audioTrigger: List<Pair<Regex, AudioPlayer?>>

    @PostConstruct
    fun init() {
        audioTrigger = audioConf.inputStream.reader(Charsets.UTF_8).readLines().mapNotNull { line ->
            val configLine = AudioConfigParser.parseLine(line) ?: return@mapNotNull null
            Pair(
                configLine.regex,
                configLine.soundFile?.let { AudioPlayer(resourceLoader.getResource(it), configLine.args) }
            )
        }
    }

    @EventListener
    fun onEvent(event: EqEvent) {
        if (event.origin != Origin.EQLOG) return
        audioTrigger.forEach { pair ->
            if(!pair.first.matches(event.text)) return@forEach
            pair.second?.run(AudioPlayer::play)
            return
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AudioEventConfig::class.java)
    }
}