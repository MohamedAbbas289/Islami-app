package com.example.islamiapp.ui.home.tabs.quran

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.annotation.ColorInt

object QuranVerseNumberStyler {
    fun style(formattedText: FormattedQuranText, @ColorInt color: Int): CharSequence =
        SpannableString(formattedText.text).apply {
            formattedText.verseNumberRanges.forEach { range ->
                setSpan(
                    ForegroundColorSpan(color),
                    range.first,
                    range.last + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
}
