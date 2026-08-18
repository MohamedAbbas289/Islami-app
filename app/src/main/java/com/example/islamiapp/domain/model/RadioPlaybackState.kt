package com.example.islamiapp.domain.model

data class RadioPlaybackState(
    val stationId: Int? = null,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val errorMessage: String? = null
)
