package com.example.islamiapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.islamiapp.domain.model.ThemeMode
import com.example.islamiapp.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val themeRepository: ThemeRepository
) : ViewModel() {
    private val mutableThemeMode = MutableStateFlow(themeRepository.themeMode)
    val themeMode: StateFlow<ThemeMode> = mutableThemeMode.asStateFlow()

    fun selectTheme(mode: ThemeMode) {
        if (mode == mutableThemeMode.value) return
        themeRepository.setThemeMode(mode)
        mutableThemeMode.value = mode
    }
}
