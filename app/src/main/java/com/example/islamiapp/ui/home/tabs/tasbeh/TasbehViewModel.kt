package com.example.islamiapp.ui.home.tabs.tasbeh

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.islamiapp.domain.model.TasbehCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TasbehUiState(
    val counter: TasbehCounter = TasbehCounter(),
    val rotationDegrees: Float = 0f
)

class TasbehViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val mutableState = MutableStateFlow(
        TasbehUiState(
            counter = TasbehCounter(
                count = savedStateHandle[KEY_COUNT] ?: 0,
                phraseIndex = savedStateHandle[KEY_PHRASE_INDEX] ?: 0
            ),
            rotationDegrees = savedStateHandle[KEY_ROTATION] ?: 0f
        )
    )
    val state: StateFlow<TasbehUiState> = mutableState.asStateFlow()

    fun increment() {
        val current = mutableState.value
        val updated = current.copy(
            counter = current.counter.increment(PHRASE_COUNT),
            rotationDegrees = (current.rotationDegrees + ROTATION_STEP) % FULL_ROTATION
        )
        mutableState.value = updated
        savedStateHandle[KEY_COUNT] = updated.counter.count
        savedStateHandle[KEY_PHRASE_INDEX] = updated.counter.phraseIndex
        savedStateHandle[KEY_ROTATION] = updated.rotationDegrees
    }

    private companion object {
        const val PHRASE_COUNT = 3
        const val ROTATION_STEP = 30f
        const val FULL_ROTATION = 360f
        const val KEY_COUNT = "tasbeh_count"
        const val KEY_PHRASE_INDEX = "tasbeh_phrase_index"
        const val KEY_ROTATION = "tasbeh_rotation"
    }
}
