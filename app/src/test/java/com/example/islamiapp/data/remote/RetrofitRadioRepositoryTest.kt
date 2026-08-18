package com.example.islamiapp.data.remote

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RetrofitRadioRepositoryTest {
    @Test
    fun `remote dto is mapped to a trimmed domain station and blank urls are removed`() = runTest {
        val service = object : RadioApiService {
            override suspend fun getRadios() = RadioResponseDto(
                radios = listOf(
                    RadioDto(1, "  إذاعة القرآن  ", "https://example.com/live", "2026-01-01"),
                    RadioDto(2, "Invalid", "", null)
                )
            )
        }

        val result = RetrofitRadioRepository(service).getStations()

        assertEquals(1, result.size)
        assertEquals(1, result.single().id)
        assertEquals("إذاعة القرآن", result.single().name)
        assertEquals("https://example.com/live", result.single().streamUrl)
        assertEquals("2026-01-01", result.single().updatedAt)
    }
}
