package com.example.islamiapp.domain.player

import com.example.islamiapp.domain.model.RadioPlaybackState
import com.example.islamiapp.domain.model.RadioStation
import kotlinx.coroutines.flow.StateFlow

interface RadioPlayer {
    val state: StateFlow<RadioPlaybackState>

    fun play(station: RadioStation)
    fun togglePlayback()
    fun stop()
    fun release()
}
