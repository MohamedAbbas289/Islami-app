package com.example.islamiapp.data.local

import com.example.islamiapp.domain.model.Hadeth

object HadethParser {
    fun parse(text: String): List<Hadeth> = text
        .removePrefix("\uFEFF")
        .split('#')
        .mapIndexedNotNull { index, block ->
            val lines = block.trim().lines()
            val title = lines.firstOrNull()?.removePrefix("\uFEFF")?.trim().orEmpty()
            val content = lines.drop(1).joinToString("\n").trim()

            if (title.isBlank() || content.isBlank()) {
                null
            } else {
                Hadeth(id = index, title = title, content = content)
            }
        }
}
