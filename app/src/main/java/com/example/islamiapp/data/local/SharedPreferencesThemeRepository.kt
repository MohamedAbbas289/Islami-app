package com.example.islamiapp.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.islamiapp.domain.model.ThemeMode
import com.example.islamiapp.domain.repository.ThemeRepository

class SharedPreferencesThemeRepository(
    private val preferences: SharedPreferences
) : ThemeRepository {
    override val themeMode: ThemeMode
        get() = ThemeMode.fromStorage(preferences.getString(KEY_THEME_MODE, null))

    override fun setThemeMode(mode: ThemeMode) {
        preferences.edit { putString(KEY_THEME_MODE, mode.name) }
    }

    private companion object {
        const val KEY_THEME_MODE = "theme_mode"
    }
}
