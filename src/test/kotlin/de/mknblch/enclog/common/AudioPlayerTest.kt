package de.mknblch.enclog.common

import kotlinx.coroutines.runBlocking
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
        val audioPlayer = AudioPlayer(resource)

        audioPlayer.playRepeatedly(1, 520)
    }
}