package com.example.islamiapp.data.player

import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.islamiapp.R
import com.example.islamiapp.domain.model.RadioPlaybackState
import com.example.islamiapp.domain.model.RadioStation
import com.example.islamiapp.domain.player.RadioPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Media3RadioPlayer(context: Context) : RadioPlayer, Player.Listener {
    private val appContext = context.applicationContext
    private val mutableState = MutableStateFlow(RadioPlaybackState())
    private val controllerFuture = MediaController.Builder(
        appContext,
        SessionToken(appContext, ComponentName(appContext, RadioPlaybackService::class.java))
    ).buildAsync()
    private var controller: MediaController? = null
    private var pendingStation: RadioStation? = null

    override val state: StateFlow<RadioPlaybackState> = mutableState.asStateFlow()

    init {
        controllerFuture.addListener(
            {
                runCatching { controllerFuture.get() }
                    .onSuccess(::onControllerConnected)
                    .onFailure { error ->
                        mutableState.value = mutableState.value.copy(
                            isPlaying = false,
                            isBuffering = false,
                            errorMessage = error.localizedMessage
                                ?: "Unable to connect to radio playback"
                        )
                    }
            },
            ContextCompat.getMainExecutor(appContext)
        )
    }

    override fun play(station: RadioStation) {
        mutableState.value = RadioPlaybackState(
            stationId = station.id,
            isBuffering = true
        )
        controller?.let { playStation(it, station) } ?: run {
            pendingStation = station
        }
    }

    override fun togglePlayback() {
        controller?.let { player ->
            if (player.isPlaying || player.playWhenReady) {
                player.pause()
            } else if (player.mediaItemCount > 0) {
                player.play()
            }
        }
    }

    override fun stop() {
        pendingStation = null
        controller?.run {
            stop()
            clearMediaItems()
        }
        mutableState.value = RadioPlaybackState()
    }

    override fun release() {
        controller?.removeListener(this)
        MediaController.releaseFuture(controllerFuture)
        controller = null
        pendingStation = null
    }

    override fun onEvents(player: Player, events: Player.Events) {
        updateState(player)
    }

    override fun onPlayerError(error: PlaybackException) {
        mutableState.value = mutableState.value.copy(
            isPlaying = false,
            isBuffering = false,
            errorMessage = error.localizedMessage ?: "Unable to play this station"
        )
    }

    private fun onControllerConnected(mediaController: MediaController) {
        controller = mediaController
        mediaController.addListener(this)
        updateState(mediaController)
        pendingStation?.let { station ->
            pendingStation = null
            playStation(mediaController, station)
        }
    }

    private fun playStation(player: Player, station: RadioStation) {
        val artworkUri =
            "android.resource://${appContext.packageName}/${R.drawable.launcher_icon_art}"
        val mediaItem = MediaItem.Builder()
            .setMediaId(station.id.toString())
            .setUri(station.streamUrl)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(station.name)
                    .setArtist(appContext.getString(R.string.live_quran_radio))
                    .setArtworkUri(artworkUri.toUri())
                    .setIsPlayable(true)
                    .build()
            )
            .build()

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    private fun updateState(player: Player) {
        val current = mutableState.value
        val stationId = player.currentMediaItem?.mediaId?.toIntOrNull() ?: current.stationId
        mutableState.value = mutableState.value.copy(
            stationId = stationId,
            isPlaying = player.isPlaying,
            isBuffering = player.playbackState == Player.STATE_BUFFERING
        )
    }
}
