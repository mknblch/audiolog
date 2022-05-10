package de.mknblch.enclog.service

import de.mknblch.enclog.Origin
import de.mknblch.enclog.common.EventMapper
import org.springframework.stereotype.Service
import java.time.Instant

// event when changing zone
data class ChangeZoneEvent(val time: Instant, val zone: String)

@Service
class ZoneChangeMapper : EventMapper<ChangeZoneEvent>("World initialized: (\\w+)", Origin.DBG) {

    override fun map(
        origin: Origin,
        time: Instant,
        timeText: String,
        text: String,
        matchResult: MatchResult
    ): ChangeZoneEvent {
        return ChangeZoneEvent(time, matchResult.groupValues[1])
    }
}