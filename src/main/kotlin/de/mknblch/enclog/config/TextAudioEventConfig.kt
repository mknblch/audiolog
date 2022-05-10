package de.mknblch.enclog.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class TextAudioEventConfig(@Value("\${audio_config_file:audio_config.cfg}") val audioConfigFile: String) {


}