package com.example.islamiapp.data.local

import com.example.islamiapp.domain.model.QuranVerse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AssetQuranRepositoryTest {
    @Test
    fun `catalog and chapter content are exposed through the repository`() = runTest {
        val repository = AssetQuranRepository(
            assetReader = FakeAssetReader(mapOf("1.txt" to "الآية الأولى\n\nالآية الثانية  "))
        )

        val chapters = repository.getChapters()
        val verses = repository.getVerses(1)

        assertEquals(114, chapters.size)
        assertEquals("الفاتحة", chapters.first().name)
        assertEquals(7, chapters.first().verseCount)
        assertEquals(6, chapters.last().verseCount)
        assertEquals(
            listOf(
                QuranVerse(number = 1, text = "الآية الأولى"),
                QuranVerse(number = 2, text = "الآية الثانية")
            ),
            verses
        )
    }

    @Test
    fun `verse search ignores Arabic diacritics and returns chapter location`() = runTest {
        val repository = AssetQuranRepository(
            assetReader = FakeAssetReader(
                mapOf(
                    "1.txt" to "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ\nالْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
                    "108.txt" to "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ"
                )
            )
        )

        val results = repository.searchVerses(query = "انا اعطيناك الكوثر", limit = 10)

        assertEquals(1, results.size)
        assertEquals("الكوثر", results.single().chapter.name)
        assertEquals(1, results.single().verse.number)
    }

    private class FakeAssetReader(
        private val assets: Map<String, String>
    ) : AssetReader {
        override suspend fun readText(fileName: String): String = assets[fileName].orEmpty()
    }
}
