package de.mknblch.enclog.service

import de.mknblch.enclog.Origin
import de.mknblch.enclog.common.AudioPlayer
import de.mknblch.enclog.common.EventMapper
import org.springframework.stereotype.Service
import java.time.Instant

// Your Mesmerize spell has worn off.
data class SpellOffEvent(val time: Instant, val spell: String)

@Service
class SpellOffMapper : EventMapper<SpellOffEvent>("Your (.+?) spell has worn off.", Origin.EQLOG) {

    override fun map(
        origin: Origin,
        time: Instant,
        timeText: String,
        text: String,
        matchResult: MatchResult
    ): SpellOffEvent {
        return SpellOffEvent(time, matchResult.groupValues[1])
    }
}