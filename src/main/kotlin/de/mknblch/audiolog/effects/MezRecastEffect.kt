package de.mknblch.audiolog.effects

import de.mknblch.audiolog.config.AudioQueue
import de.mknblch.audiolog.service.MezOnEvent
import de.mknblch.audiolog.service.SpellOffEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class MezRecastEffect(
    @Autowired private val queue: AudioQueue,
    @Value("\${mez_recast_effect_start_volume:50}") private val startVolume: Int,
    @Value("\${mez_recast_effect_end_volume:100}") private val endVolume: Int,
    @Value("\${mez_recast_effect_reminder_start:4000}") private val reminderStart: Int,
    @Value("\${mez_recast_effect_reminder_ticks:3}") private val reminderTicks: Int,
    @Value("\${mez_recast_effect_reminder_delay:1000}") private val reminderDelay: Int,
) {

    @Value("\${mez_recast_effect:sounds/Alert5.wav}")
    private lateinit var resource: Resource

    private val tracking = ConcurrentHashMap<String, Int>()

    @EventListener
    fun onMezEvent(event: MezOnEvent) {
        logger.debug("enqueuing reminder for ${event.spell.spellName} in ${event.spell.ticks} tícks")
        val volumeIncPerStep = (endVolume - startVolume) / (reminderTicks - 1)
        val wearOffTime = event.spell.ticks * 6_000
        for (t in 0 until reminderTicks) {
            val volume = startVolume + t * volumeIncPerStep
            val delay = reminderDelay * t
            queue.enqueue(resource,  wearOffTime - reminderStart + delay, volume, event.spell.spellName)
        }
        val tracks = tracking.compute(event.spell.spellName) { _, c -> c?.plus(1) ?: 1 }
        logger.debug("counting $tracks active targets under ${event.spell.spellName}")
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
        if (activeMezEffects == 0) {
            logger.debug("cancel all remaining reminders for ${event.spell}")
            queue.dequeue(event.spell)
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MezRecastEffect::class.java)
    }
}