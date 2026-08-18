package com.example.islamiapp.data.local

import android.content.res.AssetManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidAssetReader(
    private val assetManager: AssetManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AssetReader {
    override suspend fun readText(fileName: String): String = withContext(ioDispatcher) {
        assetManager.open(fileName).bufferedReader().use { it.readText() }
    }
}
