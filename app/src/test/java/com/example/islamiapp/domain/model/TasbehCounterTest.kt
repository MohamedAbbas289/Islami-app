package com.example.islamiapp.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class TasbehCounterTest {
    @Test
    fun `counter advances phrase after thirty three counts`() {
        var state = TasbehCounter()

        repeat(33) { state = state.increment(phraseCount = 3) }
        assertEquals(TasbehCounter(count = 33, phraseIndex = 0), state)

        state = state.increment(phraseCount = 3)
        assertEquals(TasbehCounter(count = 0, phraseIndex = 1), state)
    }

    @Test
    fun `phrase index wraps after the final phrase`() {
        var state = TasbehCounter(count = 33, phraseIndex = 2)

        state = state.increment(phraseCount = 3)

        assertEquals(TasbehCounter(count = 0, phraseIndex = 0), state)
    }
}
