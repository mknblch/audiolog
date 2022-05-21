package de.mknblch.enclog

import java.time.Instant

enum class Origin {
    ANY, DBG, EQLOG
}

// raw event class, emitted by LogParser
data class EqEvent(
    val origin: Origin,
    val time: Instant,
    val timeText: String,
    val text: String,
    val character: String? = null,
    val server: String? = null)


/*
    A fire beetle has been mesmerized.
    A fire beetle has been entranced.
    A fire beetle has been mesmerized by the Glamour of Kintaz.
    A fire beetle has been enthralled.
    A decaying skeleton has been fascinated.
    A fire beetle swoons in raptured bliss.
 */

enum class MezSpell(val ticks: Int, val spellName: String) {
    Rapture(4, "Rapture"),
    Mesmerize(4, "Mesmerize"),
    Mesmerization(4, "Mesmerization"),
    GlamourOfKintaz(5, "Glamour of Kintaz"),
    Fascination(6, "Fascination"),
    Enthrall(8, "Enthrall"),
    Entrance(12, "Entrance"),
    Dazzle(16, "Dazzle")
}

interface GameEvent