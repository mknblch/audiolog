package de.mknblch.audiolog.service

import de.mknblch.audiolog.EqEvent
import de.mknblch.audiolog.GameEvent
import de.mknblch.audiolog.Origin
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.Instant

/*
    [Mon Apr 18 17:22:45 2022] Players on EverQuest:
    [Mon Apr 18 17:22:45 2022] ---------------------------
    [Mon Apr 18 17:22:45 2022] [ANONYMOUS] Grimgop  <Prowl>
    [Mon Apr 18 17:22:45 2022] [ANONYMOUS] Ryancohen  <Prowl>
    [Mon Apr 18 17:22:45 2022] [ANONYMOUS] Kasima  <Prowl>
    [Mon Apr 18 17:22:45 2022] There are 3 players in Lower Guk.

    [Mon Apr 18 14:23:30 2022] Players on EverQuest:
    [Mon Apr 18 14:23:30 2022] ---------------------------
    [Mon Apr 18 14:23:30 2022] [ANONYMOUS] Kasima  <Prowl>
    [Mon Apr 18 14:23:30 2022] There is 1 player in Lower Guk.
 */

// /who received
data class WhoEvent(val time: Instant, val zone: String, val players: Set<String>) : GameEvent

@Service
class WhoEventMapper {

    private val players = HashSet<String>()
    private var active = false

    @Async
    @EventListener
    fun map(event: EqEvent): WhoEvent? {
        if (event.origin != Origin.EQLOG) return null
        if (event.text == "---------------------------") return null
        // start who
        if (event.text == "Players on EverQuest:") {
            players.clear()
            active = true
            return null
        }
        if (!active) return null
        // end who
        END_REGEX.matchEntire(event.text)?.also {
            active = false
            return WhoEvent(event.time, it.groupValues[2], players)
        }
        players.add(event.text.trim())
        return null
    }

    companion object {
        private val END_REGEX = Regex("There (?:is|are) (\\d+) players? in (.+).")
    }
}