package de.mknblch.enclog.service

import de.mknblch.enclog.Origin
import de.mknblch.enclog.common.EventMapper
import org.springframework.stereotype.Service
import java.time.Instant

data class LoginEvent(val time: Instant, val character: String, val server: String)

@Service
class LoginMapper : EventMapper<LoginEvent>("INI file \\.\\\\UI_([^_]+?)_([^\\.]+)\\.ini loaded\\.", Origin.DBG) {

    override fun map(
        origin: Origin,
        time: Instant,
        timeText: String,
        text: String,
        matchResult: MatchResult
    ): LoginEvent {
        return LoginEvent(time, matchResult.groupValues[1], matchResult.groupValues[2])
    }
}