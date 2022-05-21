package de.mknblch.enclog.service

import de.mknblch.enclog.EqEvent
import de.mknblch.enclog.GameEvent
import de.mknblch.enclog.Origin
import de.mknblch.enclog.common.mapper
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import java.time.Instant

// Your Mesmerize spell has worn off.
data class SpellOffEvent(val time: Instant, val spell: String) : GameEvent

@Service
class SpellOffMapper {

    val filter = mapper(Origin.EQLOG, "Your (.+?) spell has worn off.") { e, r ->
        SpellOffEvent(e.time, r.groupValues[1])
    }

    @EventListener
    fun map(event: EqEvent): SpellOffEvent? {
        return filter(event)
    }
}