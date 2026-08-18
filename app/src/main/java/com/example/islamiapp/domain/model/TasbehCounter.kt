package com.example.islamiapp.domain.model

data class TasbehCounter(
    val count: Int = 0,
    val phraseIndex: Int = 0
) {
    fun increment(phraseCount: Int): TasbehCounter {
        require(phraseCount > 0)
        return if (count >= MAX_COUNT) {
            copy(count = 0, phraseIndex = (phraseIndex + 1) % phraseCount)
        } else {
            copy(count = count + 1)
        }
    }

    private companion object {
        const val MAX_COUNT = 33
    }
}
