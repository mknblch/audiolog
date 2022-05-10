package de.mknblch.enclog.service

import de.mknblch.enclog.EqEvent
import de.mknblch.enclog.Origin
import de.mknblch.enclog.common.LogParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.io.File
import java.time.Instant


@Scope("singleton")
@Service
class EqlogEmitter(
    @Value("\${eq_directory}") val eqDirectory: String,
    @Value("\${server_name}") val serverName: String,
    @Value("\${character_name}") val characterName: String
) : LogParser(File("$eqDirectory/Logs/eqlog_${characterName}_$serverName.txt")) {

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    override fun emit(value: String) {
        val matchResult: MatchResult = lineRegex.matchEntire(value) ?: return
        applicationEventPublisher.publishEvent(
            EqEvent(
                Origin.EQLOG,
                Instant.now(),
                matchResult.groupValues[1],
                matchResult.groupValues[2]
            )
        )
    }

    companion object {
        private val lineRegex = Regex("\\[[a-zA-Z]+ [a-zA-Z]+ \\d+ ([^ ]+) \\d+\\] (.+)")
    }

}