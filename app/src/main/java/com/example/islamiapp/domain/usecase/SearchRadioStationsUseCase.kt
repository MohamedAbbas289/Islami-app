package com.example.islamiapp.domain.usecase

import com.example.islamiapp.domain.model.RadioStation
import com.example.islamiapp.domain.search.ArabicTextNormalizer

class SearchRadioStationsUseCase {
    operator fun invoke(stations: List<RadioStation>, query: String): List<RadioStation> {
        val normalizedQuery = ArabicTextNormalizer.normalize(query)
        if (normalizedQuery.isBlank()) return stations

        val queryWords = normalizedQuery.split(' ')
        return stations.filter { station ->
            val normalizedName = ArabicTextNormalizer.normalize(station.name)
            val compactName = normalizedName.replace(" ", "")
            queryWords.all { word -> normalizedName.contains(word) || compactName.contains(word) }
        }
    }
}
