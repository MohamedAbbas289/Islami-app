package com.example.islamiapp.data.local

interface AssetReader {
    suspend fun readText(fileName: String): String
}
