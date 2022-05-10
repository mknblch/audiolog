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
import java.nio.file.Path
import java.time.Instant

class EqlogEmitter(val applicationEventPublisher: ApplicationEventPublisher, path: Path, val character: String, val server: String) : LogParser(path.toFile()) {

    override fun emit(value: String) {
        val matchResult: MatchResult = lineRegex.matchEntire(value) ?: return
        applicationEventPublisher.publishEvent(
            EqEvent(
                origin = Origin.EQLOG,
                time = Instant.now(),
                timeText = matchResult.groupValues[1],
                text = matchResult.groupValues[2],
                character = character,
                server = server)
        )
    }

    companion object {
        private val lineRegex = Regex("\\[[a-zA-Z]+ [a-zA-Z]+ \\d+ ([^ ]+) \\d+\\] (.+)")
    }
}