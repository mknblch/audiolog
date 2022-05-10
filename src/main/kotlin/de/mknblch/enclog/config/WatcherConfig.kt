package de.mknblch.enclog.config

import de.mknblch.enclog.EqEvent
import de.mknblch.enclog.Origin
import de.mknblch.enclog.common.KWatchChannel
import de.mknblch.enclog.common.LogParser
import de.mknblch.enclog.common.asWatchChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Configuration
import java.io.File
import java.nio.file.*
import java.time.Instant
import javax.annotation.PostConstruct

//@Configuration
//class WatcherConfig(@Value("\${eq_directory}") val eqDirectory: String) : AutoCloseable {
//
//    @Autowired
//    private lateinit var applicationEventPublisher: ApplicationEventPublisher
//
//    private var parser: LogParser? = null
//    private var watchKey: WatchKey? = null
//    private val watchService = FileSystems.getDefault().newWatchService()
//    private var running = true;
//
//    init {
//        watchKey = Paths.get("$eqDirectory/Logs/").register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)
//        GlobalScope.launch(Dispatchers.IO) {
//            logger.info("launching watch service on $eqDirectory/Logs")
//            var file: String? = null
//            while (running) {
//
//                watchKey?.pollEvents()?.forEach {
//                    val filename: String = it.context().toString()
//                    logger.debug("check $filename")
//                    if (!eqLogFileRegex.matches(filename)) {
//                        return@launch
//                    }
//
//                    if (filename != file) {
//                        launchParser(File("${eqDirectory}/Logs/$filename"))
//                        file = filename
//                    }
//
//                    logger.debug("run loop done")
//                }
//            }
//        }
//    }
//
//    private fun checkAndSend(value: String) {
//        val matchResult: MatchResult = lineRegex.matchEntire(value) ?: return
//        applicationEventPublisher.publishEvent(
//            EqEvent(
//                Origin.EQLOG,
//                Instant.now(),
//                matchResult.groupValues[1],
//                matchResult.groupValues[2]
//            )
//        )
//    }
//
//    private fun launchParser(file: File) {
//        logger.debug("launching parser for $file")
//        parser?.close()
//        parser = object : LogParser(file) {
//            override fun emit(value: String) = checkAndSend(value)
//        }
//    }
//
//    override fun close() {
//        logger.debug("closing")
//        running = false
//    }
//
//    companion object {
//        private val eqLogFileRegex = Regex("eqlog_([^_]+)_([^\\.]+)\\.txt")
//        private val lineRegex = Regex("\\[[a-zA-Z]+ [a-zA-Z]+ \\d+ ([^ ]+) \\d+\\] (.+)")
//        private val logger: Logger = LoggerFactory.getLogger(LogParser::class.java)
//    }
//}