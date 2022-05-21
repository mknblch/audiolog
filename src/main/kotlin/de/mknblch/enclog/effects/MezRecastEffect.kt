package de.mknblch.enclog.effects

import de.mknblch.enclog.config.AudioQueue
import de.mknblch.enclog.service.MezOnEvent
import de.mknblch.enclog.service.SpellOffEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class MezRecastEffect(@Autowired private val queue: AudioQueue) {

    @Value("\${mez_recast_effect:sounds/Alert5.wav}")
    private lateinit var resource: Resource

    private val tracking = ConcurrentHashMap<String, Int>()

    @EventListener
    fun onMezEvent(event: MezOnEvent) {
        logger.debug("enqueuing reminder for ${event.spell.spellName} in ${event.spell.ticks} tícks")
        queue.enqueue(resource, event.spell.ticks * 6_000 - 4_000, 50, event.spell.spellName)
        queue.enqueue(resource, event.spell.ticks * 6_000 - 3_000, 70, event.spell.spellName)
        queue.enqueue(resource, event.spell.ticks * 6_000 - 2_000, 100, event.spell.spellName)
        val tracks = tracking.compute(event.spell.spellName) { _, c -> c?.plus(1) ?: 1 }
        logger.debug("counting $tracks active targets for ${event.spell.spellName}")
    }

    /*
        we don't know on which target the mez broke, but we can keep
        track of the count of mesmerized targets. If the count decreases
        to 0, due to timeout or mez-break we can also remove all AudioTask's
        having that spell-name as their ID from the AudioQueue
     */
    @EventListener
    fun onSpellOff(event: SpellOffEvent) {
        val activeMezEffects: Int? = tracking.computeIfPresent(event.spell) { _, c -> c - 1 }
        if (activeMezEffects == 0) queue.dequeue(event.spell)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MezRecastEffect::class.java)
    }
}