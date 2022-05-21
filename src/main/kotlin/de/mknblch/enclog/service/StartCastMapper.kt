package de.mknblch.enclog.service

import de.mknblch.enclog.EqEvent
import de.mknblch.enclog.GameEvent
import de.mknblch.enclog.Origin
import de.mknblch.enclog.common.mapper
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import java.time.Instant


data class StartCastEvent(val time: Instant, val spell: String) : GameEvent


@Service
class StartCastMapper {

    val filter = mapper(Origin.EQLOG, "You begin casting (.+?)\\.") { e, r ->
        StartCastEvent(e.time, r.groupValues[1])
    }

    @EventListener
    fun map(event: EqEvent): StartCastEvent? {
        return filter(event)
    }
}