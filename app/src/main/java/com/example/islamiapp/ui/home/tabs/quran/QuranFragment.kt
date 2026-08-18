package com.example.islamiapp.ui.home.tabs.quran

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.islamiapp.databinding.FragmentQuranBinding
import com.example.islamiapp.domain.model.QuranChapter
import com.example.islamiapp.ui.Constants
import com.example.islamiapp.ui.common.ViewModelFactory
import com.example.islamiapp.ui.common.appContainer
import kotlinx.coroutines.launch

class QuranFragment : Fragment() {
    private var _binding: FragmentQuranBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: QuranViewModel by viewModels {
        ViewModelFactory { QuranViewModel(requireContext().appContainer.quranRepository) }
    }
    private val adapter = ChapterNamesAdapter(::showSuraDetails)

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
        recyclerView.isVisible = !state.isLoading && state.errorMessage == null
        errorGroup.isVisible = state.errorMessage != null
        errorText.text = state.errorMessage
        adapter.submitList(state.chapters)
    }

    private fun showSuraDetails(chapter: QuranChapter) {
        startActivity(Intent(requireContext(), SuraDetailsActivity::class.java).apply {
            putExtra(Constants.EXTRA_CHAPTER_NAME, chapter.name)
            putExtra(Constants.EXTRA_CHAPTER_NUMBER, chapter.number)
        })
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
