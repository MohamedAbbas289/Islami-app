package com.example.islamiapp.ui.home.tabs.radio

import com.example.islamiapp.domain.model.RadioPlaybackState
import com.example.islamiapp.domain.model.RadioStation
import com.example.islamiapp.domain.player.RadioPlayer
import com.example.islamiapp.domain.repository.RadioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RadioViewModelTest {
    @Test
    fun `station picker selection updates the station and preserves active playback`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val stations = listOf(
                station(id = 1, name = "المحطة الأولى"),
                station(id = 2, name = "المحطة الثانية"),
                station(id = 3, name = "المحطة الثالثة")
            )
            val player = FakeRadioPlayer()
            val viewModel = RadioViewModel(
                repository = object : RadioRepository {
                    override suspend fun getStations(): List<RadioStation> = stations
                },
                player = player
            )
            advanceUntilIdle()

            viewModel.selectStation(index = 2)
            assertEquals(stations[2], viewModel.state.value.currentStation)
            assertTrue(player.playedStations.isEmpty())

            player.setPlaying(stations[2])
            advanceUntilIdle()
            viewModel.selectStation(index = 1)
            advanceUntilIdle()

            assertEquals(stations[1], viewModel.state.value.currentStation)
            assertEquals(stations[1], player.playedStations.single())
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun station(id: Int, name: String) = RadioStation(
        id = id,
        name = name,
        streamUrl = "https://example.com/$id",
        updatedAt = null
    )

    private class FakeRadioPlayer : RadioPlayer {
        private val mutableState = MutableStateFlow(RadioPlaybackState())
        override val state: StateFlow<RadioPlaybackState> = mutableState
        val playedStations = mutableListOf<RadioStation>()

        fun setPlaying(station: RadioStation) {
            mutableState.value = RadioPlaybackState(
                stationId = station.id,
                isPlaying = true
            )
        }

        override fun play(station: RadioStation) {
            playedStations += station
            mutableState.value = RadioPlaybackState(
                stationId = station.id,
                isBuffering = true
            )
        }

        override fun togglePlayback() = Unit

        override fun stop() = Unit

        override fun release() = Unit
    }
}
