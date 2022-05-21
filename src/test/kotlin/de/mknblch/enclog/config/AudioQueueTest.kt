package de.mknblch.enclog.config

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.core.io.DefaultResourceLoader

internal class AudioQueueTest {

    @Test
    fun test() {

        val resource = DefaultResourceLoader().getResource("sounds/Alert.wav")
        val resource2 = DefaultResourceLoader().getResource("sounds/Alert5.wav")
        assertNotNull(resource2)

        AudioQueue().use {
            it.enqueue(resource, 0, 100)
            it.enqueue(resource2, 0, 100)
            it.enqueue(resource, 3000, 100)
            it.enqueue(resource2, 3000, 100)
            Thread.sleep(5000)
        }

    }

}