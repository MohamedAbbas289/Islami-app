package com.example.islamiapp.domain.usecase

import com.example.islamiapp.domain.model.RadioStation
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchRadioStationsUseCaseTest {
    private val search = SearchRadioStationsUseCase()
    private val stations = listOf(
        station(1, "إذاعة الشيخ محمود خليل الحصري"),
        station(2, "إذاعة الشيخ عبد الباسط عبد الصمد"),
        station(3, "إذاعة القرآن الكريم")
    )

    @Test
    fun `search matches reader name without requiring diacritics or word order`() {
        assertEquals(
            listOf(stations[0]),
            search(stations, "الحُصَرِى محمود")
        )
    }

    @Test
    fun `blank query returns every station`() {
        assertEquals(stations, search(stations, "  "))
    }

    private fun station(id: Int, name: String) = RadioStation(
        id = id,
        name = name,
        streamUrl = "https://example.com/$id",
        updatedAt = null
    )
}
