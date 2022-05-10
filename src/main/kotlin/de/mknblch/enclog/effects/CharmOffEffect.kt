package de.mknblch.enclog.effects

import de.mknblch.enclog.common.AudioPlayer
import de.mknblch.enclog.service.CharmOffEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct


@Service
class CharmOffEffect {

    @Value("\${charm_off_effect:sounds/Alert1.wav}")
    private lateinit var resource: Resource

    private lateinit var player: AudioPlayer

    @PostConstruct
    fun init() {
        player = AudioPlayer(resource)
    }

    @EventListener
    fun onCharmOff(charmOffEvent: CharmOffEvent) {
        player.play()
    }
}