package com.example.islamiapp.domain.usecase

import com.example.islamiapp.domain.model.QuranSearchResult
import com.example.islamiapp.domain.repository.QuranRepository
import com.example.islamiapp.domain.search.ArabicTextNormalizer

class SearchQuranVersesUseCase(
    private val repository: QuranRepository
) {
    fun isValidQuery(query: String): Boolean =
        ArabicTextNormalizer.normalize(query).length >= MIN_QUERY_LENGTH

    suspend operator fun invoke(query: String): List<QuranSearchResult> {
        if (!isValidQuery(query)) return emptyList()
        return repository.searchVerses(query = query, limit = MAX_RESULTS)
    }

    private companion object {
        const val MIN_QUERY_LENGTH = 2
        const val MAX_RESULTS = 50
    }
}
