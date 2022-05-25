package de.mknblch.audiolog.common

import de.mknblch.audiolog.EqEvent
import de.mknblch.audiolog.Origin

fun <T : Any> mapper(origin: Origin, regex: String, lambda: (EqEvent, MatchResult) -> T?): (EqEvent) -> T? {
    val compiled = Regex(regex)
    return Ret@{ event ->
        if (event.origin != origin) return@Ret null
        val matchResult = compiled.matchEntire(event.text) ?: return@Ret null
        lambda(event, matchResult)
    }
}


//abstract class EventMapper<T: Any>(regexString: String, private val origin: Origin) {
//
//    private val regex = Regex(regexString)
//
//    @Autowired
//    private lateinit var applicationEventPublisher: ApplicationEventPublisher
//
//    init {
//        logger.info("initializing ${this::class.simpleName}($regexString, $origin)")
//    }
//
//    @EventListener
//    protected fun map(event: EqEvent): T? {
//        if (event.origin != origin) return null
//        val matchResult = regex.matchEntire(event.text) ?: return null
//        val transformed = map(event.origin, event.time, event.timeText, event.text, matchResult) ?: return null
////        applicationEventPublisher.publishEvent(transformed)
//        return transformed
//    }
//
//    abstract fun map(origin: Origin, time: Instant, timeText: String, text: String, matchResult: MatchResult): T?
//
//    companion object {
//        private val logger: Logger = LoggerFactory.getLogger(EncLog::class.java)
//    }
//}