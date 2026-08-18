package com.example.islamiapp.ui.home.tabs.quran

import com.example.islamiapp.domain.model.QuranVerse
import org.junit.Assert.assertEquals
import org.junit.Test

class QuranTextFormatterTest {
    @Test
    fun `verses are joined continuously with Arabic Indic verse numbers`() {
        val verses = listOf(
            QuranVerse(number = 1, text = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ"),
            QuranVerse(number = 12, text = "إِنَّا نَحْنُ نُحْيِي الْمَوْتَى")
        )

        val result = QuranTextFormatter.format(verses)

        assertEquals(
            "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ﴿١﴾ إِنَّا نَحْنُ نُحْيِي الْمَوْتَى ﴿١٢﴾",
            result
        )
    }

    @Test
    fun `verse number ranges include the decorated number only`() {
        val result = QuranTextFormatter.formatWithVerseNumberRanges(
            listOf(
                QuranVerse(number = 1, text = "الآية الأولى"),
                QuranVerse(number = 12, text = "الآية الثانية")
            )
        )

        assertEquals(
            listOf("﴿١﴾", "﴿١٢﴾"),
            result.verseNumberRanges.map { result.text.substring(it) }
        )
        assertEquals(
            "الآية الثانية ﴿١٢﴾",
            result.text.substring(requireNotNull(result.verseRanges[12]))
        )
    }
}
