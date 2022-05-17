package de.mknblch.enclog.common

import de.mknblch.enclog.EncLog
import de.mknblch.enclog.EqEvent
import de.mknblch.enclog.Origin
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import java.time.Instant

abstract class EventMapper<T: Any>(regexString: String, private val origin: Origin) {

    private val regex = Regex(regexString)

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    init {
        logger.info("initializing ${this::class.simpleName}($regexString, $origin)")
    }

    @EventListener
    protected fun map(event: EqEvent): T? {
        if (event.origin != origin) return null
        val matchResult = regex.matchEntire(event.text) ?: return null
        val transformed = map(event.origin, event.time, event.timeText, event.text, matchResult) ?: return null
//        applicationEventPublisher.publishEvent(transformed)
        return transformed
    }

    abstract fun map(origin: Origin, time: Instant, timeText: String, text: String, matchResult: MatchResult): T?

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EncLog::class.java)
    }
}