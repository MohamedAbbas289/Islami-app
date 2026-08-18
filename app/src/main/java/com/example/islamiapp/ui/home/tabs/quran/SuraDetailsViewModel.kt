package com.example.islamiapp.ui.home.tabs.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.islamiapp.domain.model.QuranVerse
import com.example.islamiapp.domain.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SuraDetailsUiState(
    val isLoading: Boolean = true,
    val verses: List<QuranVerse> = emptyList(),
    val errorMessage: String? = null
)

class SuraDetailsViewModel(
    private val chapterNumber: Int,
    private val repository: QuranRepository
) : ViewModel() {
    private val mutableState = MutableStateFlow(SuraDetailsUiState())
    val state: StateFlow<SuraDetailsUiState> = mutableState.asStateFlow()

    init {
        loadVerses()
    }

    fun loadVerses() {
        viewModelScope.launch {
            mutableState.value = SuraDetailsUiState(isLoading = true)
            mutableState.value = runCatching { repository.getVerses(chapterNumber) }
                .fold(
                    onSuccess = { SuraDetailsUiState(isLoading = false, verses = it) },
                    onFailure = {
                        SuraDetailsUiState(
                            isLoading = false,
                            errorMessage = it.localizedMessage ?: "Unable to load this sura"
                        )
                    }
                )
        }
    }
}
