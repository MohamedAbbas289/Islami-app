package com.example.islamiapp.ui.common

import android.content.Context
import com.example.islamiapp.IslamiApplication
import com.example.islamiapp.di.AppContainer

val Context.appContainer: AppContainer
    get() = (applicationContext as IslamiApplication).container
