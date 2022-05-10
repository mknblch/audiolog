package de.mknblch.enclog

import java.time.Instant

enum class Origin {
    ANY, DBG, EQLOG
}

// event class
data class EqEvent(
    val origin: Origin,
    val time: Instant,
    val timeText: String,
    val text: String,
    val character: String? = null,
    val server: String? = null)

