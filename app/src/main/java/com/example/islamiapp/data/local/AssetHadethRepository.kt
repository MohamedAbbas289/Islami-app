package com.example.islamiapp.data.local

import com.example.islamiapp.domain.model.Hadeth
import com.example.islamiapp.domain.repository.HadethRepository

class AssetHadethRepository(
    private val assetReader: AssetReader
) : HadethRepository {
    private var cache: List<Hadeth>? = null

    override suspend fun getHadeths(): List<Hadeth> = cache ?: HadethParser
        .parse(assetReader.readText(HADETH_FILE_NAME))
        .also { cache = it }

    override suspend fun getHadeth(id: Int): Hadeth? =
        getHadeths().firstOrNull { it.id == id }

    private companion object {
        const val HADETH_FILE_NAME = "ahadeth.txt"
    }
}
