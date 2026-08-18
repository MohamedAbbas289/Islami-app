package com.example.islamiapp.ui.home.tabs.quran

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.islamiapp.R
import com.example.islamiapp.databinding.FragmentQuranBinding
import com.example.islamiapp.domain.model.QuranChapter
import com.example.islamiapp.domain.model.QuranSearchResult
import com.example.islamiapp.ui.Constants
import com.example.islamiapp.ui.common.ViewModelFactory
import com.example.islamiapp.ui.common.appContainer
import kotlinx.coroutines.launch

class QuranFragment : Fragment() {
    private var _binding: FragmentQuranBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: QuranViewModel by viewModels {
        ViewModelFactory {
            val container = requireContext().appContainer
            QuranViewModel(
                repository = container.quranRepository,
                searchQuranVerses = container.searchQuranVerses
            )
        }
    }
    private val adapter = ChapterNamesAdapter(::showSuraDetails)
    private val searchResultsAdapter = QuranSearchResultsAdapter(::showSearchResult)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        binding.searchResultsRecyclerView.adapter = searchResultsAdapter
        binding.quranSearchInput.doAfterTextChanged { text ->
            viewModel.updateSearchQuery(text?.toString().orEmpty())
        }
        binding.retryButton.setOnClickListener { viewModel.loadChapters() }
        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::render)
            }
        }
    }

    private fun render(state: QuranUiState) = with(binding) {
        progressBar.isVisible = state.isLoading
        val canShowContent = !state.isLoading && state.errorMessage == null
        tableHeader.isVisible = canShowContent && !state.isSearchMode
        recyclerView.isVisible = canShowContent && !state.isSearchMode
        searchResultsRecyclerView.isVisible = canShowContent &&
                state.isSearchMode &&
                state.searchResults.isNotEmpty()
        searchStatusRow.isVisible = canShowContent && state.isSearchMode
        searchProgress.isVisible = state.isSearching
        searchStatusText.isVisible = !state.isSearching
        searchStatusText.text = searchStatusMessage(state)
        errorGroup.isVisible = state.errorMessage != null
        errorText.text = state.errorMessage
        adapter.submitList(state.chapters)
        searchResultsAdapter.submitList(state.searchResults)
    }

    private fun searchStatusMessage(state: QuranUiState): String = when {
        !state.isSearchQueryValid -> getString(R.string.quran_search_minimum)
        state.searchError != null -> getString(R.string.quran_search_error)
        state.hasCompletedSearch && state.searchResults.isEmpty() ->
            getString(R.string.no_quran_search_results)

        state.hasCompletedSearch -> getString(
            R.string.quran_search_results_count,
            state.searchResults.size
        )

        else -> ""
    }

    private fun showSuraDetails(chapter: QuranChapter, verseNumber: Int? = null) {
        startActivity(Intent(requireContext(), SuraDetailsActivity::class.java).apply {
            putExtra(Constants.EXTRA_CHAPTER_NAME, chapter.name)
            putExtra(Constants.EXTRA_CHAPTER_NUMBER, chapter.number)
            verseNumber?.let { putExtra(Constants.EXTRA_VERSE_NUMBER, it) }
        })
    }

    private fun showSearchResult(result: QuranSearchResult) {
        showSuraDetails(chapter = result.chapter, verseNumber = result.verse.number)
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        binding.searchResultsRecyclerView.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
