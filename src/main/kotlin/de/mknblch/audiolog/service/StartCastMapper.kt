package de.mknblch.audiolog.service

import de.mknblch.audiolog.EqEvent
import de.mknblch.audiolog.GameEvent
import de.mknblch.audiolog.Origin
import de.mknblch.audiolog.common.mapper
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