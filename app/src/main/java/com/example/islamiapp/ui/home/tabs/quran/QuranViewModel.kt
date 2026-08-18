package com.example.islamiapp.ui.home.tabs.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.islamiapp.domain.model.QuranChapter
import com.example.islamiapp.domain.model.QuranSearchResult
import com.example.islamiapp.domain.repository.QuranRepository
import com.example.islamiapp.domain.usecase.SearchQuranVersesUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class QuranUiState(
    val isLoading: Boolean = true,
    val chapters: List<QuranChapter> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val searchResults: List<QuranSearchResult> = emptyList(),
    val isSearching: Boolean = false,
    val isSearchQueryValid: Boolean = false,
    val hasCompletedSearch: Boolean = false,
    val searchError: String? = null
) {
    val isSearchMode: Boolean
        get() = searchQuery.isNotBlank()
}

class QuranViewModel(
    private val repository: QuranRepository,
    private val searchQuranVerses: SearchQuranVersesUseCase
) : ViewModel() {
    private val mutableState = MutableStateFlow(QuranUiState())
    val state: StateFlow<QuranUiState> = mutableState.asStateFlow()
    private var searchJob: Job? = null

    init {
        loadChapters()
    }

    fun loadChapters() {
        viewModelScope.launch {
            mutableState.value = mutableState.value.copy(isLoading = true, errorMessage = null)
            runCatching { repository.getChapters() }
                .onSuccess { chapters ->
                    mutableState.value = mutableState.value.copy(
                        isLoading = false,
                        chapters = chapters,
                        errorMessage = null
                    )
                }
                .onFailure { error ->
                    mutableState.value = mutableState.value.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Unable to load Quran"
                    )
                }
        }
    }

    fun updateSearchQuery(query: String) {
        searchJob?.cancel()
        val isValidQuery = searchQuranVerses.isValidQuery(query)
        mutableState.value = mutableState.value.copy(
            searchQuery = query,
            searchResults = emptyList(),
            isSearching = false,
            isSearchQueryValid = isValidQuery,
            hasCompletedSearch = false,
            searchError = null
        )
        if (!isValidQuery) return

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MILLIS)
            mutableState.value = mutableState.value.copy(isSearching = true)
            try {
                val results = searchQuranVerses(query)
                mutableState.value = mutableState.value.copy(
                    searchResults = results,
                    isSearching = false,
                    hasCompletedSearch = true,
                    searchError = null
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: Throwable) {
                mutableState.value = mutableState.value.copy(
                    searchResults = emptyList(),
                    isSearching = false,
                    hasCompletedSearch = true,
                    searchError = error.localizedMessage ?: "Unable to search Quran"
                )
            }
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MILLIS = 300L
    }
}
