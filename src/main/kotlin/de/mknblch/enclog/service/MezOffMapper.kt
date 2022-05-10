package de.mknblch.enclog.service

import de.mknblch.enclog.Origin
import de.mknblch.enclog.common.EventMapper
import org.springframework.stereotype.Service
import java.time.Instant

// Your Mesmerize spell has worn off.
data class MezOffEvent(val time: Instant, val spell: String)

@Service
class MezOffMapper : EventMapper<MezOffEvent>("Your (Mesmerize|Mesmerization|Fascination|Dazzle) spell has worn off.", Origin.EQLOG) {

    override fun map(
        origin: Origin,
        time: Instant,
        timeText: String,
        text: String,
        matchResult: MatchResult
    ): MezOffEvent {
        return MezOffEvent(time, matchResult.groupValues[1])
    }
}