package com.example.islamiapp.ui.home.tabs.hadeth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.islamiapp.domain.model.Hadeth
import com.example.islamiapp.domain.repository.HadethRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HadethDetailsUiState(
    val isLoading: Boolean = true,
    val hadeth: Hadeth? = null,
    val errorMessage: String? = null
)

class HadethDetailsViewModel(
    private val hadethId: Int,
    private val repository: HadethRepository
) : ViewModel() {
    private val mutableState = MutableStateFlow(HadethDetailsUiState())
    val state: StateFlow<HadethDetailsUiState> = mutableState.asStateFlow()

    init {
        loadHadeth()
    }

    fun loadHadeth() {
        viewModelScope.launch {
            mutableState.value = HadethDetailsUiState(isLoading = true)
            mutableState.value = runCatching { repository.getHadeth(hadethId) }
                .fold(
                    onSuccess = { hadeth ->
                        if (hadeth == null) {
                            HadethDetailsUiState(
                                isLoading = false,
                                errorMessage = "Hadeth not found"
                            )
                        } else {
                            HadethDetailsUiState(isLoading = false, hadeth = hadeth)
                        }
                    },
                    onFailure = {
                        HadethDetailsUiState(
                            isLoading = false,
                            errorMessage = it.localizedMessage ?: "Unable to load Hadeth"
                        )
                    }
                )
        }
    }
}
