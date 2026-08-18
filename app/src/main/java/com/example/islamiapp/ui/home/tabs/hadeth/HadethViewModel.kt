package com.example.islamiapp.ui.home.tabs.hadeth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.islamiapp.domain.model.Hadeth
import com.example.islamiapp.domain.repository.HadethRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HadethUiState(
    val isLoading: Boolean = true,
    val hadeths: List<Hadeth> = emptyList(),
    val errorMessage: String? = null
)

class HadethViewModel(
    private val repository: HadethRepository
) : ViewModel() {
    private val mutableState = MutableStateFlow(HadethUiState())
    val state: StateFlow<HadethUiState> = mutableState.asStateFlow()

    init {
        loadHadeths()
    }

    fun loadHadeths() {
        viewModelScope.launch {
            mutableState.value = HadethUiState(isLoading = true)
            mutableState.value = runCatching { repository.getHadeths() }
                .fold(
                    onSuccess = { HadethUiState(isLoading = false, hadeths = it) },
                    onFailure = {
                        HadethUiState(
                            isLoading = false,
                            errorMessage = it.localizedMessage ?: "Unable to load Hadeth"
                        )
                    }
                )
        }
    }
}
