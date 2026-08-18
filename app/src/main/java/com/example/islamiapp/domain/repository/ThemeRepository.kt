package com.example.islamiapp.domain.repository

import com.example.islamiapp.domain.model.ThemeMode

interface ThemeRepository {
    val themeMode: ThemeMode
    fun setThemeMode(mode: ThemeMode)
}
