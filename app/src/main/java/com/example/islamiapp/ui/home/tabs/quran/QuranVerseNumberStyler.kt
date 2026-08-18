package com.example.islamiapp.ui.home.tabs.quran

import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import androidx.annotation.ColorInt

object QuranVerseNumberStyler {
    fun style(
        formattedText: FormattedQuranText,
        @ColorInt numberColor: Int,
        highlightedVerseNumber: Int? = null,
        @ColorInt highlightColor: Int? = null
    ): CharSequence =
        SpannableString(formattedText.text).apply {
            formattedText.verseNumberRanges.forEach { range ->
                setSpan(
                    ForegroundColorSpan(numberColor),
                    range.first,
                    range.last + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            val highlightedRange = highlightedVerseNumber?.let(formattedText.verseRanges::get)
            if (highlightedRange != null && highlightColor != null) {
                setSpan(
                    BackgroundColorSpan(highlightColor),
                    highlightedRange.first,
                    highlightedRange.last + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
}
