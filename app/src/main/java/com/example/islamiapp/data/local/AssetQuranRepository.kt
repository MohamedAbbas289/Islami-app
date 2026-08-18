package com.example.islamiapp.data.local

import com.example.islamiapp.domain.model.QuranChapter
import com.example.islamiapp.domain.model.QuranSearchResult
import com.example.islamiapp.domain.model.QuranVerse
import com.example.islamiapp.domain.repository.QuranRepository
import com.example.islamiapp.domain.search.ArabicTextNormalizer
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AssetQuranRepository(
    private val assetReader: AssetReader
) : QuranRepository {
    private val verseCache = mutableMapOf<Int, List<QuranVerse>>()
    private val verseCacheMutex = Mutex()
    private val searchIndexMutex = Mutex()

    @Volatile
    private var cachedSearchIndex: List<SearchIndexEntry>? = null

    override suspend fun getChapters(): List<QuranChapter> = QuranCatalog.chapters

    override suspend fun getVerses(chapterNumber: Int): List<QuranVerse> {
        require(chapterNumber in 1..QuranCatalog.chapters.size) {
            "Unknown Quran chapter: $chapterNumber"
        }
        return verseCacheMutex.withLock {
            verseCache[chapterNumber] ?: readVerses(chapterNumber).also {
                verseCache[chapterNumber] = it
            }
        }
    }

    override suspend fun searchVerses(query: String, limit: Int): List<QuranSearchResult> {
        require(limit > 0) { "Search result limit must be positive" }
        val normalizedQuery = ArabicTextNormalizer.normalize(query)
        if (normalizedQuery.isBlank()) return emptyList()

        val compactQuery = normalizedQuery.replace(" ", "")
        return getSearchIndex()
            .asSequence()
            .filter { entry ->
                entry.normalizedText.contains(normalizedQuery) ||
                        entry.compactText.contains(compactQuery)
            }
            .take(limit)
            .map(SearchIndexEntry::result)
            .toList()
    }

    private suspend fun getSearchIndex(): List<SearchIndexEntry> {
        cachedSearchIndex?.let { return it }
        return searchIndexMutex.withLock {
            cachedSearchIndex ?: QuranCatalog.chapters.flatMap { chapter ->
                getVerses(chapter.number).map { verse ->
                    val result = QuranSearchResult(chapter = chapter, verse = verse)
                    SearchIndexEntry(
                        result = result,
                        normalizedText = ArabicTextNormalizer.normalize(verse.text)
                    )
                }
            }.also { cachedSearchIndex = it }
        }
    }

    private suspend fun readVerses(chapterNumber: Int): List<QuranVerse> =
        assetReader.readText("$chapterNumber.txt")
            .lineSequence()
            .map(String::trim)
            .filter(String::isNotBlank)
            .mapIndexed { index, text ->
                QuranVerse(number = index + 1, text = text)
            }
            .toList()

    private data class SearchIndexEntry(
        val result: QuranSearchResult,
        val normalizedText: String
    ) {
        val compactText: String = normalizedText.replace(" ", "")
    }
}
