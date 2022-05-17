package de.mknblch.enclog.service

import de.mknblch.enclog.Origin
import de.mknblch.enclog.common.EventMapper
import org.springframework.stereotype.Service
import java.time.Instant

// Your Mesmerize spell has worn off.
data class MezOnEvent(val time: Instant, val target: String, val spell: String)

@Service
class MezOnMapper : EventMapper<MezOnEvent>("Your (Mesmerize|Enthrall|Entrance|Dazzle|Mesmerization|Fascination|Glamour of Kintaz) spell has worn off.", Origin.EQLOG) {

    override fun map(
        origin: Origin,
        time: Instant,
        timeText: String,
        text: String,
        matchResult: MatchResult
    ): MezOnEvent {
        return MezOnEvent(time, matchResult.groupValues[1], matchResult.groupValues[2])
    }
}