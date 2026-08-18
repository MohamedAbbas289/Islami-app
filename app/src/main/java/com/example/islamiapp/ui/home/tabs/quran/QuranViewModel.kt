package com.example.islamiapp.ui.home.tabs.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.islamiapp.domain.model.QuranChapter
import com.example.islamiapp.domain.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class QuranUiState(
    val isLoading: Boolean = true,
    val chapters: List<QuranChapter> = emptyList(),
    val errorMessage: String? = null
)

class QuranViewModel(
    private val repository: QuranRepository
) : ViewModel() {
    private val mutableState = MutableStateFlow(QuranUiState())
    val state: StateFlow<QuranUiState> = mutableState.asStateFlow()

    init {
        loadChapters()
    }

    fun loadChapters() {
        viewModelScope.launch {
            mutableState.value = QuranUiState(isLoading = true)
            mutableState.value = runCatching { repository.getChapters() }
                .fold(
                    onSuccess = { QuranUiState(isLoading = false, chapters = it) },
                    onFailure = {
                        QuranUiState(
                            isLoading = false,
                            errorMessage = it.localizedMessage ?: "Unable to load Quran"
                        )
                    }
                )
        }
    }
}
