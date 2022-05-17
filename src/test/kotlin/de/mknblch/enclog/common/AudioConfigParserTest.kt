package de.mknblch.enclog.common

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class AudioConfigParserTest {

    @Test
    fun testSimpleLine() {
        val configLine = AudioConfigParser.parseLine("sounds/Alert9.wav:(.+) casting is interrupted!")
        assertEquals("(.+) casting is interrupted!", configLine?.regex?.pattern)
        assertEquals("sounds/Alert9.wav", configLine?.soundFile)
//        assertEquals(0, configLine?.delay)
    }

    @Test
    fun testEmptyLine() {
        val configLine = AudioConfigParser.parseLine("NONE:(.+) casting is interrupted!")
        assertEquals("(.+) casting is interrupted!", configLine?.regex?.pattern)
        assertNull(configLine?.soundFile)
//        assertEquals(0, configLine?.delay)
    }

    @Test
    fun testComplexLine() {
        val configLine = AudioConfigParser.parseLine("sounds/Alert.wav[delay=100, continue]:You are as quiet as a herd of running elephants\\.")
        assertEquals("sounds/Alert.wav", configLine?.soundFile)
        assertEquals("You are as quiet as a herd of running elephants\\.", configLine?.regex?.pattern)
        println(configLine)
//        assertEquals(100, configLine?.delay)
    }

    @Test
    fun testComment() {
        val configLine = AudioConfigParser.parseLine("# Just a comment")
        assertNull(configLine)
    }
}