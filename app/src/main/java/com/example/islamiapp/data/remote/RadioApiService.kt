package com.example.islamiapp.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

interface RadioApiService {
    @GET("radios")
    suspend fun getRadios(): RadioResponseDto
}

data class RadioResponseDto(
    val radios: List<RadioDto> = emptyList()
)

data class RadioDto(
    val id: Int,
    val name: String,
    val url: String,
    @SerializedName("recent_date") val recentDate: String?
)
