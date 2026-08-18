package com.example.islamiapp.domain.model

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK;

    companion object {
        fun fromStorage(value: String?): ThemeMode =
            values().firstOrNull { it.name == value } ?: SYSTEM
    }
}
