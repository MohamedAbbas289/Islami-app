package com.example.islamiapp.ui.home.tabs.quran

import com.example.islamiapp.domain.model.QuranChapter
import com.example.islamiapp.domain.model.QuranSearchResult
import com.example.islamiapp.domain.model.QuranVerse
import com.example.islamiapp.domain.repository.QuranRepository
import com.example.islamiapp.domain.usecase.SearchQuranVersesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuranViewModelTest {
    @Test
    fun `valid query searches after debounce and exposes verse locations`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val chapter = QuranChapter(number = 108, name = "الكوثر", verseCount = 3)
            val result = QuranSearchResult(
                chapter = chapter,
                verse = QuranVerse(number = 1, text = "إنا أعطيناك الكوثر")
            )
            val repository =
                FakeQuranRepository(chapters = listOf(chapter), results = listOf(result))
            val viewModel = QuranViewModel(
                repository = repository,
                searchQuranVerses = SearchQuranVersesUseCase(repository)
            )
            advanceUntilIdle()

            viewModel.updateSearchQuery("انا اعطيناك")
            advanceTimeBy(301)
            advanceUntilIdle()

            assertTrue(viewModel.state.value.hasCompletedSearch)
            assertEquals(listOf(result), viewModel.state.value.searchResults)
            assertEquals(listOf("انا اعطيناك"), repository.searchQueries)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `one letter query does not start a Quran search`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val repository = FakeQuranRepository()
            val viewModel = QuranViewModel(repository, SearchQuranVersesUseCase(repository))
            advanceUntilIdle()

            viewModel.updateSearchQuery("ا")
            advanceUntilIdle()

            assertFalse(viewModel.state.value.isSearchQueryValid)
            assertTrue(repository.searchQueries.isEmpty())
        } finally {
            Dispatchers.resetMain()
        }
    }

    private class FakeQuranRepository(
        private val chapters: List<QuranChapter> = emptyList(),
        private val results: List<QuranSearchResult> = emptyList()
    ) : QuranRepository {
        val searchQueries = mutableListOf<String>()

        override suspend fun getChapters(): List<QuranChapter> = chapters

        override suspend fun getVerses(chapterNumber: Int): List<QuranVerse> = emptyList()

        override suspend fun searchVerses(query: String, limit: Int): List<QuranSearchResult> {
            searchQueries += query
            return results.take(limit)
        }
    }
}
