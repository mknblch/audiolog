//package de.mknblch.audiolog.common
//
//import org.junit.jupiter.api.Test
//import java.util.*
//import javax.speech.Central
//import javax.speech.synthesis.Synthesizer
//import javax.speech.synthesis.SynthesizerModeDesc
//
//
//class SpeechTest {
//
//    @Test
//    fun test() {
//        try {
//            // Set property as Kevin Dictionary
//            System.setProperty(
//                "freetts.voices", "com.sun.speech.freetts.en.us"
//                        + ".cmu_us_kal.KevinVoiceDirectory"
//            )
//
//            // Register Engine
//            Central.registerEngineCentral(
//                "com.sun.speech.freetts"
//                        + ".jsapi.FreeTTSEngineCentral"
//            )
//
//            // Create a Synthesizer
//            val synthesizer = Central.createSynthesizer(
//                SynthesizerModeDesc(Locale.US)
//            )
//
//            // Allocate synthesizer
//            synthesizer.allocate()
//
//            // Resume Synthesizer
//            synthesizer.resume()
//
//            // Speaks the given text
//            // until the queue is empty.
//            synthesizer.speakPlainText(
//                "Velketor the Sorcerer's spell interrupted", null
//            )
//            synthesizer.waitEngineState(
//                Synthesizer.QUEUE_EMPTY
//            )
//
//            // Deallocate the Synthesizer.
//            synthesizer.deallocate()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//}