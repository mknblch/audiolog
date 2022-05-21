package de.mknblch.enclog.service

import de.mknblch.enclog.EqEvent
import de.mknblch.enclog.GameEvent
import de.mknblch.enclog.MezSpell
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import java.time.Instant


data class MezOnEvent(val time: Instant, val target: String, val spell: MezSpell) : GameEvent

@Service
class MezOnMapper {

    private val glamour5 = Regex("(.+?) has been mesmerized by the Glamour of Kintaz\\.")
    private val entrance12 = Regex("(.+?) has been entranced\\.")
    private val enthrall8 = Regex("(.+?) has been enthralled\\.")
    private val fascination6 = Regex("(.+?) has been fascinated\\.")
    private val rapture4 = Regex("(.+?) swoons in raptured bliss\\.")
    private val mezDaz = Regex("(.+?) has been mesmerized\\.")

    private var spell: String = "null"

    @EventListener
    fun onStartCast(event: StartCastEvent) {
        spell = event.spell
    }

    @EventListener
    fun map(event: EqEvent): MezOnEvent? {
        return evalGlamour(event)
            ?: evalEntrance(event)
            ?: evalEnthrall(event)
            ?: evalFascination(event)
            ?: evalGlamour(event)
            ?: evalRapture(event)
            ?: evalMesmerize(event)
    }

    private fun evalRapture(event: EqEvent): MezOnEvent? {
        if (spell != "Rapture") return null
        return rapture4.matchEntire(event.text)?.let {
            MezOnEvent(event.time, it.groupValues[1], MezSpell.Rapture)
        }
    }

    private fun evalGlamour(event: EqEvent): MezOnEvent? {
        if (spell != "Glamour of Kintaz") return null
        return glamour5.matchEntire(event.text)?.let {
            MezOnEvent(event.time, it.groupValues[1], MezSpell.GlamourOfKintaz)
        }
    }

    private fun evalEntrance(event: EqEvent): MezOnEvent? {
        if (spell != "Entrance") return null
        return entrance12.matchEntire(event.text)?.let {
            MezOnEvent(event.time, it.groupValues[1], MezSpell.Entrance)
        }
    }

    private fun evalEnthrall(event: EqEvent): MezOnEvent? {
        if (spell != "Enthrall") return null
        return enthrall8.matchEntire(event.text)?.let {
            MezOnEvent(event.time, it.groupValues[1], MezSpell.Enthrall)
        }
    }

    private fun evalFascination(event: EqEvent): MezOnEvent? {
        if (spell != "Fascination") return null
        return fascination6.matchEntire(event.text)?.let {
            MezOnEvent(event.time, it.groupValues[1], MezSpell.Fascination)
        }
    }

    private fun evalMesmerize(event: EqEvent): MezOnEvent? {
        return mezDaz.matchEntire(event.text)?.let {
            // Dazzle, Mesmerize and Mesmerization ues common sentence
            when (spell) {
                // check which spell has been cast before
                "Dazzle" -> MezOnEvent(event.time, it.groupValues[1], MezSpell.Dazzle)
                "Mesmerize" -> MezOnEvent(event.time, it.groupValues[1], MezSpell.Mesmerize)
                "Mesmerization" -> MezOnEvent(event.time, it.groupValues[1], MezSpell.Mesmerization)
                else -> null
            }

        }
    }
}