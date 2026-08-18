package com.example.islamiapp.data.remote

import com.example.islamiapp.domain.model.RadioStation
import com.example.islamiapp.domain.repository.RadioRepository

class RetrofitRadioRepository(
    private val apiService: RadioApiService
) : RadioRepository {
    override suspend fun getStations(): List<RadioStation> = apiService
        .getRadios()
        .radios
        .filter { it.url.isNotBlank() }
        .map { dto ->
            RadioStation(
                id = dto.id,
                name = dto.name.trim(),
                streamUrl = dto.url,
                updatedAt = dto.recentDate
            )
        }
}
