package com.example.islamiapp.domain.repository

import com.example.islamiapp.domain.model.Hadeth

interface HadethRepository {
    suspend fun getHadeths(): List<Hadeth>
    suspend fun getHadeth(id: Int): Hadeth?
}
