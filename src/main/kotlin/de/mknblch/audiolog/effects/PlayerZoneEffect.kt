package de.mknblch.audiolog.effects

import de.mknblch.audiolog.common.AudioPlayer
import de.mknblch.audiolog.service.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct


@Service
class PlayerZoneEffect(@Value("\${player_zone_effect_volume:80}") val volume: Double) {

    @Value("\${player_zone_effect:sounds/Alert.wav}")
    private lateinit var resource: Resource
    private lateinit var player: AudioPlayer
    private var zone: String = ""
    private val playerCharacters = HashSet<String>()

    @PostConstruct
    fun init() {
        player = AudioPlayer(resource, mapOf("volume" to volume.toString()))
    }

    @EventListener
    fun onWho(event: WhoEvent) {
        if (zone != event.zone) {
            playerCharacters.clear()
            playerCharacters.addAll(event.players)
            zone = event.zone
            return
        }
        val newPlayersInZone = event.players - playerCharacters
        if (newPlayersInZone.isEmpty()) return
        playerCharacters.addAll(newPlayersInZone)
        player.play()
    }
}