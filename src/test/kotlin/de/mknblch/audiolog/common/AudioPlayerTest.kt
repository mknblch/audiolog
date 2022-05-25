package de.mknblch.audiolog.common

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource

@SpringBootApplication
class TestApp {

}

@SpringBootTest(classes = [TestApp::class])
internal class AudioPlayerTest {

    @Value("sounds/Alert5.wav")
    private lateinit var resource: Resource

    @Test
    fun testSound() {

        for (i in 10..100 step 10) {
            AudioPlayer(
                resource, mapOf(
                    "volume" to i.toString(),
                    "delay" to "100"
                )
            ).play()
            Thread.sleep(500)
        }

    }



}