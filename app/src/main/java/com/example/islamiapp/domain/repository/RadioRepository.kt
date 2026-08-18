package com.example.islamiapp.domain.repository

import com.example.islamiapp.domain.model.RadioStation

interface RadioRepository {
    suspend fun getStations(): List<RadioStation>
}
