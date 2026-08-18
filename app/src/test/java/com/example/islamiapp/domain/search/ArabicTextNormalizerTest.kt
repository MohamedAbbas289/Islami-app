package com.example.islamiapp.domain.search

import org.junit.Assert.assertEquals
import org.junit.Test

class ArabicTextNormalizerTest {
    @Test
    fun `Arabic search ignores diacritics tatweel and alef variants`() {
        assertEquals(
            "انا اعطيناك الكوثر",
            ArabicTextNormalizer.normalize("إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ")
        )
        assertEquals(
            "الشيخ الحصري",
            ArabicTextNormalizer.normalize("الـشَّيْخُ   الحُصَرِى")
        )
        assertEquals("الرحمه", ArabicTextNormalizer.normalize("الرَّحْمَة"))
    }
}
