package com.example.islamiapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.islamiapp.di.AppContainer
import com.example.islamiapp.di.DefaultAppContainer
import com.example.islamiapp.ui.theme.toNightMode

class IslamiApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        AppCompatDelegate.setDefaultNightMode(
            container.themeRepository.themeMode.toNightMode()
        )
    }
}
