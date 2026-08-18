package com.example.islamiapp.domain.search

import java.text.Normalizer
import java.util.Locale

object ArabicTextNormalizer {
    fun normalize(value: String): String = Normalizer.normalize(value, Normalizer.Form.NFD)
        .asSequence()
        .filterNot(::isArabicDecoration)
        .map(::normalizeArabicLetter)
        .joinToString(separator = "")
        .lowercase(Locale.ROOT)
        .replace(NON_LETTER_OR_DIGIT, " ")
        .trim()
        .replace(MULTIPLE_SPACES, " ")

    private fun isArabicDecoration(character: Char): Boolean =
        character == ARABIC_TATWEEL || when (Character.getType(character)) {
            Character.NON_SPACING_MARK.toInt(),
            Character.COMBINING_SPACING_MARK.toInt(),
            Character.ENCLOSING_MARK.toInt() -> true

            else -> false
        }

    private fun normalizeArabicLetter(character: Char): Char = when (character) {
        'أ', 'إ', 'آ', 'ٱ' -> 'ا'
        'ى' -> 'ي'
        'ة' -> 'ه'
        'ؤ' -> 'و'
        'ئ' -> 'ي'
        else -> character
    }

    private const val ARABIC_TATWEEL = 'ـ'
    private val NON_LETTER_OR_DIGIT = Regex("[^\\p{L}\\p{N}]+")
    private val MULTIPLE_SPACES = Regex("\\s+")
}
