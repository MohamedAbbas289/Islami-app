package com.example.islamiapp.domain.repository

import com.example.islamiapp.domain.model.QuranChapter
import com.example.islamiapp.domain.model.QuranSearchResult
import com.example.islamiapp.domain.model.QuranVerse

interface QuranRepository {
    suspend fun getChapters(): List<QuranChapter>
    suspend fun getVerses(chapterNumber: Int): List<QuranVerse>
    suspend fun searchVerses(query: String, limit: Int): List<QuranSearchResult>
}
