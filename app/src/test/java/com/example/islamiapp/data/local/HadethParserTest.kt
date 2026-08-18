package com.example.islamiapp.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class HadethParserTest {
    @Test
    fun `parse separates titles from content and ignores trailing delimiter`() {
        val source = """
            ﻿الحديث الأول
            المتن الأول
            السطر الثاني
            #
            الحديث الثاني
            المتن الثاني
            #
        """.trimIndent()

        val result = HadethParser.parse(source)

        assertEquals(2, result.size)
        assertEquals(0, result[0].id)
        assertEquals("الحديث الأول", result[0].title)
        assertEquals("المتن الأول\nالسطر الثاني", result[0].content)
        assertEquals("الحديث الثاني", result[1].title)
    }
}
