package com.example.islamiapp.di

import android.content.Context
import com.example.islamiapp.data.local.AndroidAssetReader
import com.example.islamiapp.data.local.AssetHadethRepository
import com.example.islamiapp.data.local.AssetQuranRepository
import com.example.islamiapp.data.local.SharedPreferencesThemeRepository
import com.example.islamiapp.data.player.Media3RadioPlayer
import com.example.islamiapp.data.remote.RadioApiService
import com.example.islamiapp.data.remote.RetrofitRadioRepository
import com.example.islamiapp.domain.player.RadioPlayer
import com.example.islamiapp.domain.repository.HadethRepository
import com.example.islamiapp.domain.repository.QuranRepository
import com.example.islamiapp.domain.repository.RadioRepository
import com.example.islamiapp.domain.repository.ThemeRepository
import com.example.islamiapp.domain.usecase.SearchQuranVersesUseCase
import com.example.islamiapp.domain.usecase.SearchRadioStationsUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val hadethRepository: HadethRepository
    val quranRepository: QuranRepository
    val radioRepository: RadioRepository
    val themeRepository: ThemeRepository
    val radioPlayer: RadioPlayer
    val searchQuranVerses: SearchQuranVersesUseCase
    val searchRadioStations: SearchRadioStationsUseCase
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val appContext = context.applicationContext
    private val assetReader = AndroidAssetReader(appContext.assets)
    private val radioApiService = Retrofit.Builder()
        .baseUrl("https://mp3quran.net/api/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RadioApiService::class.java)

    override val hadethRepository: HadethRepository = AssetHadethRepository(assetReader)
    override val quranRepository: QuranRepository = AssetQuranRepository(assetReader)
    override val radioRepository: RadioRepository = RetrofitRadioRepository(radioApiService)
    override val themeRepository: ThemeRepository = SharedPreferencesThemeRepository(
        appContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    )
    override val radioPlayer: RadioPlayer by lazy { Media3RadioPlayer(appContext) }
    override val searchQuranVerses: SearchQuranVersesUseCase by lazy {
        SearchQuranVersesUseCase(quranRepository)
    }
    override val searchRadioStations: SearchRadioStationsUseCase = SearchRadioStationsUseCase()

    private companion object {
        const val PREFERENCES_NAME = "islami_preferences"
    }
}
