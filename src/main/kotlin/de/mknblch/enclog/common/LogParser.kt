package de.mknblch.enclog.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.util.concurrent.atomic.AtomicBoolean

abstract class LogParser(val logfile: File, private val maxFileSize: Long = 1024 * 256) : AutoCloseable {

    private val active = AtomicBoolean(true)
    private var randomAccessFile: RandomAccessFile = RandomAccessFile(logfile, "rw")

    init {

        logger.debug("reset $logfile(${randomAccessFile.length()}) to 0")

        randomAccessFile.setLength(0)
        randomAccessFile.seek(0)

        GlobalScope.async(Dispatchers.IO) {
            logger.info("starting parser $logfile")
            readLoop()
        }
    }

    private suspend fun readLoop() {
        var chr: Int
        var cr = false
        val builder = StringBuffer()

        GlobalScope.async(Dispatchers.IO) {

            Loop@while (active.get()) {
                chr = randomAccessFile.read()
                when {
                    // wait for input
                    chr == -1 -> {
                        delay(1)
                        if (randomAccessFile.length() > maxFileSize) {
                            logger.debug("reset $logfile(${randomAccessFile.length()}) to 0")
                            randomAccessFile.setLength(0)
                            randomAccessFile.seek(0)
                        }
                        continue@Loop
                    }
                    // emit complete sentence
                    chr == NL && cr -> {
                        emit(builder.toString())
                        builder.setLength(0)
                        cr = false
                    }
                    // set CR
                    chr == CR -> cr = true
                    // append
                    else -> {
                        builder.append(chr.toChar())
                        cr = false
                    }
                }
            }
            logger.info("shutting down")
            randomAccessFile.close()
        }

    }

    abstract fun emit(value: String)

    override fun close() {
        logger.info("closing $logfile")
        active.set(false)
    }

    companion object {
        private const val NL = '\n'.code
        private const val CR = '\r'.code
        private val logger: Logger = LoggerFactory.getLogger(LogParser::class.java)
    }
}