package com.example.islamiapp.ui.home.tabs.radio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.islamiapp.domain.model.RadioStation
import com.example.islamiapp.domain.player.RadioPlayer
import com.example.islamiapp.domain.repository.RadioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RadioUiState(
    val isLoading: Boolean = true,
    val stations: List<RadioStation> = emptyList(),
    val currentIndex: Int = 0,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val loadError: String? = null,
    val playbackError: String? = null
) {
    val currentStation: RadioStation?
        get() = stations.getOrNull(currentIndex)
}

class RadioViewModel(
    private val repository: RadioRepository,
    private val player: RadioPlayer
) : ViewModel() {
    private val mutableState = MutableStateFlow(RadioUiState())
    val state: StateFlow<RadioUiState> = mutableState.asStateFlow()

    init {
        observePlayback()
        loadStations()
    }

    fun loadStations() {
        viewModelScope.launch {
            mutableState.value = mutableState.value.copy(isLoading = true, loadError = null)
            runCatching { repository.getStations() }
                .onSuccess { stations ->
                    if (stations.isEmpty()) {
                        mutableState.value = mutableState.value.copy(
                            isLoading = false,
                            stations = emptyList(),
                            loadError = "No radio stations are currently available"
                        )
                    } else {
                        val activeStationId = player.state.value.stationId
                        val activeIndex = stations.indexOfFirst { it.id == activeStationId }
                            .takeIf { it >= 0 } ?: 0
                        mutableState.value = mutableState.value.copy(
                            isLoading = false,
                            stations = stations,
                            currentIndex = activeIndex,
                            loadError = null
                        )
                    }
                }
                .onFailure { error ->
                    mutableState.value = mutableState.value.copy(
                        isLoading = false,
                        loadError = error.localizedMessage ?: "Unable to load radio stations"
                    )
                }
        }
    }

    fun togglePlayback() {
        val station = mutableState.value.currentStation ?: return
        val playbackState = player.state.value
        if (playbackState.stationId == station.id && playbackState.errorMessage == null) {
            player.togglePlayback()
        } else {
            player.play(station)
        }
    }

    fun nextStation() = moveStation(offset = 1)

    fun previousStation() = moveStation(offset = -1)

    fun selectStation(index: Int) {
        val current = mutableState.value
        if (index !in current.stations.indices || index == current.currentIndex) return

        val shouldContinuePlaying = current.isPlaying || current.isBuffering
        mutableState.value = current.copy(currentIndex = index, playbackError = null)
        if (shouldContinuePlaying) {
            player.play(current.stations[index])
        }
    }

    fun retryPlayback() {
        mutableState.value.currentStation?.let(player::play)
    }

    private fun moveStation(offset: Int) {
        val current = mutableState.value
        if (current.stations.isEmpty()) return
        val newIndex =
            (current.currentIndex + offset + current.stations.size) % current.stations.size
        selectStation(newIndex)
    }

    private fun observePlayback() {
        viewModelScope.launch {
            player.state.collect { playback ->
                val current = mutableState.value
                val activeIndex = current.stations
                    .indexOfFirst { it.id == playback.stationId }
                    .takeIf { it >= 0 }
                    ?: current.currentIndex
                mutableState.value = current.copy(
                    currentIndex = activeIndex,
                    isPlaying = playback.isPlaying,
                    isBuffering = playback.isBuffering,
                    playbackError = playback.errorMessage
                )
            }
        }
    }
}
