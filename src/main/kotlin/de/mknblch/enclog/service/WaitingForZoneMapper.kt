package de.mknblch.enclog.service

import de.mknblch.enclog.Origin
import de.mknblch.enclog.common.EventMapper
import org.springframework.stereotype.Service
import java.time.Instant

data class WaitingForZone(val time: Instant, val character: String)

@Service
class WaitingForZoneMapper : EventMapper<WaitingForZone>("Waiting to connect to zone\\.\\.\\. Character (.+)", Origin.DBG) {

    override fun map(
        origin: Origin,
        time: Instant,
        timeText: String,
        text: String,
        matchResult: MatchResult
    ): WaitingForZone {
        return WaitingForZone(time, matchResult.groupValues[1])
    }
}