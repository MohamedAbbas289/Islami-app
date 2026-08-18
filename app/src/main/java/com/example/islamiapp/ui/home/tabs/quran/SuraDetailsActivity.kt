package com.example.islamiapp.ui.home.tabs.quran

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.islamiapp.R
import com.example.islamiapp.databinding.ActivitySuraDetailsBinding
import com.example.islamiapp.ui.Constants
import com.example.islamiapp.ui.common.ViewModelFactory
import com.example.islamiapp.ui.common.appContainer
import kotlinx.coroutines.launch

class SuraDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuraDetailsBinding
    private val chapterNumber by lazy {
        intent.getIntExtra(Constants.EXTRA_CHAPTER_NUMBER, INVALID_CHAPTER)
    }
    private val targetVerseNumber by lazy {
        intent.getIntExtra(Constants.EXTRA_VERSE_NUMBER, INVALID_VERSE)
    }
    private var hasScrolledToTargetVerse = false
    private val viewModel: SuraDetailsViewModel by viewModels {
        ViewModelFactory {
            SuraDetailsViewModel(chapterNumber, appContainer.quranRepository)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (chapterNumber == INVALID_CHAPTER) {
            finish()
            return
        }
        binding = ActivitySuraDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.suraName.text = getString(
            R.string.sura_title,
            intent.getStringExtra(Constants.EXTRA_CHAPTER_NAME).orEmpty()
        )
        binding.btnBack.setOnClickListener { finish() }
        binding.retryButton.setOnClickListener { viewModel.loadVerses() }
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::render)
            }
        }
    }

    private fun render(state: SuraDetailsUiState) = with(binding) {
        progressBar.isVisible = state.isLoading
        contentScroll.isVisible = !state.isLoading && state.errorMessage == null
        errorGroup.isVisible = state.errorMessage != null
        errorText.text = state.errorMessage
        val formattedText = QuranTextFormatter.formatWithVerseNumberRanges(state.verses)
        suraContent.text = QuranVerseNumberStyler.style(
            formattedText = formattedText,
            numberColor = ContextCompat.getColor(
                this@SuraDetailsActivity,
                R.color.verse_number_color
            ),
            highlightedVerseNumber = targetVerseNumber.takeIf { it != INVALID_VERSE },
            highlightColor = ContextCompat.getColor(
                this@SuraDetailsActivity,
                R.color.verse_search_highlight
            )
        )
        scrollToTargetVerse(formattedText)
    }

    private fun scrollToTargetVerse(formattedText: FormattedQuranText) {
        if (hasScrolledToTargetVerse || targetVerseNumber == INVALID_VERSE) return
        val targetRange = formattedText.verseRanges[targetVerseNumber] ?: return
        hasScrolledToTargetVerse = true

        binding.suraContent.post {
            val textLayout = binding.suraContent.layout ?: return@post
            val targetLine = textLayout.getLineForOffset(targetRange.first)
            val targetTop = textLayout.getLineTop(targetLine)
            val topPadding = (24 * resources.displayMetrics.density).toInt()
            binding.contentScroll.smoothScrollTo(0, (targetTop - topPadding).coerceAtLeast(0))
        }
    }

    private companion object {
        const val INVALID_CHAPTER = -1
        const val INVALID_VERSE = -1
    }
}
