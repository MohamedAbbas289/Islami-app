package com.example.islamiapp.ui.home.tabs.quran

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.islamiapp.databinding.ActivitySuraDetailsBinding
import com.example.islamiapp.ui.Constants
import java.io.IOException

class SuraDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuraDetailsBinding
    private lateinit var adapter: ChapterContentAdapter
    private var contents: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuraDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val suraName = intent.getStringExtra(Constants.EXTRA_CHAPTER_NAME)
        val suraPosition = intent.getIntExtra(Constants.EXTRA_CHAPTER_INDEX, 0)
        binding.suraName.text = "سورة $suraName"
        binding.btnBack.setOnClickListener {
            finish()
        }
        readFilesFromAssets(this, suraPosition)
        adapter = ChapterContentAdapter(contents)
        binding.recyclerView.adapter = adapter
    }

    private fun readFilesFromAssets(context: Context, suraPosition: Int) {
        try {
            val assetManager = context.assets
            val fileName = "${suraPosition + 1}.txt"
            val inputStream = assetManager.open(fileName)
            val text = inputStream.bufferedReader().use { it.readText() }
            contents.add(text)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}