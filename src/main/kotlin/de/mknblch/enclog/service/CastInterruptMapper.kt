package de.mknblch.enclog.service

import de.mknblch.enclog.Origin
import de.mknblch.enclog.common.EventMapper
import org.springframework.stereotype.Service
import java.time.Instant


open class CastInterruptEvent(open val time: Instant, val caster: String)

data class SelfInterruptEvent(override val time: Instant) : CastInterruptEvent(time, "Your")

@Service
open class CastInterruptMapper : EventMapper<CastInterruptEvent>("(.+) casting is interrupted!", Origin.EQLOG) {

    override fun map(
        origin: Origin,
        time: Instant,
        timeText: String,
        text: String,
        matchResult: MatchResult
    ): CastInterruptEvent? {
        return if (matchResult.groupValues[1] == "Your")
            SelfInterruptEvent(time)
        else
            CastInterruptEvent(
                time,
                matchResult.groupValues[1]
            )
    }
}