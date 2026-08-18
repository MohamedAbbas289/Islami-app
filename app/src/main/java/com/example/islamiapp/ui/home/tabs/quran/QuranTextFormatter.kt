package com.example.islamiapp.ui.home.tabs.quran

import com.example.islamiapp.domain.model.QuranVerse

data class FormattedQuranText(
    val text: String,
    val verseNumberRanges: List<IntRange>,
    val verseRanges: Map<Int, IntRange>
)

object QuranTextFormatter {
    fun format(verses: List<QuranVerse>): String = formatWithVerseNumberRanges(verses).text

    fun formatWithVerseNumberRanges(verses: List<QuranVerse>): FormattedQuranText {
        val text = StringBuilder()
        val verseNumberRanges = mutableListOf<IntRange>()
        val verseRanges = mutableMapOf<Int, IntRange>()

        verses.forEachIndexed { index, verse ->
            if (index > 0) text.append(' ')
            val verseStart = text.length
            text.append(verse.text.trim())
            text.append(' ')

            val numberStart = text.length
            text.append('﴿')
            text.append(verse.number.toArabicIndicDigits())
            text.append('﴾')
            verseNumberRanges += numberStart until text.length
            verseRanges[verse.number] = verseStart until text.length
        }

        return FormattedQuranText(
            text = text.toString(),
            verseNumberRanges = verseNumberRanges,
            verseRanges = verseRanges
        )
    }

    private fun Int.toArabicIndicDigits(): String = toString()
        .map { digit -> ARABIC_INDIC_DIGITS[digit.digitToInt()] }
        .joinToString(separator = "")

    private const val ARABIC_INDIC_DIGITS = "٠١٢٣٤٥٦٧٨٩"
}
