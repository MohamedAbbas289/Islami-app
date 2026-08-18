package com.example.islamiapp.data.local

import com.example.islamiapp.domain.model.QuranChapter
import com.example.islamiapp.domain.model.QuranVerse
import com.example.islamiapp.domain.repository.QuranRepository

class AssetQuranRepository(
    private val assetReader: AssetReader
) : QuranRepository {
    override suspend fun getChapters(): List<QuranChapter> = QuranCatalog.chapters

    override suspend fun getVerses(chapterNumber: Int): List<QuranVerse> {
        require(chapterNumber in 1..QuranCatalog.chapters.size) {
            "Unknown Quran chapter: $chapterNumber"
        }
        return assetReader.readText("$chapterNumber.txt")
            .lineSequence()
            .map(String::trim)
            .filter(String::isNotBlank)
            .mapIndexed { index, text ->
                QuranVerse(number = index + 1, text = text)
            }
            .toList()
    }
}
