package de.mknblch.enclog.service

import de.mknblch.enclog.Origin
import de.mknblch.enclog.common.EventMapper
import org.springframework.stereotype.Service
import java.time.Instant

// Your charm spell has worn off.
data class CharmOffEvent(val time: Instant)

@Service
class CharmOffMapper : EventMapper<CharmOffEvent>("Your charm spell has worn off.", Origin.EQLOG) {

    override fun map(
        origin: Origin,
        time: Instant,
        timeText: String,
        text: String,
        matchResult: MatchResult
    ): CharmOffEvent {
        return CharmOffEvent(time)
    }
}