package de.mknblch.enclog.effects

import de.mknblch.enclog.common.AudioPlayer
import de.mknblch.enclog.service.CastInterruptEvent
import de.mknblch.enclog.service.MezOffEvent
import de.mknblch.enclog.service.SelfInterruptEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct


@Service
class CastInterruptEffect {

    @Value("\${cast_interrupt_effect:sounds/Alert1.wav}")
    private lateinit var resource: Resource

    private lateinit var player: AudioPlayer

    @PostConstruct
    fun init() {
        player = AudioPlayer(resource)
    }

    @EventListener
    fun onMezOff(event: CastInterruptEvent) {
        player.play()
    }
}