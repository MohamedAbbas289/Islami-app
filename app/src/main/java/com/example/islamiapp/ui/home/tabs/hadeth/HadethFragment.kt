package com.example.islamiapp.ui.home.tabs.hadeth

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
import com.example.islamiapp.databinding.FragmentHadethBinding
import com.example.islamiapp.domain.model.Hadeth
import com.example.islamiapp.ui.Constants
import com.example.islamiapp.ui.common.ViewModelFactory
import com.example.islamiapp.ui.common.appContainer
import kotlinx.coroutines.launch

class HadethFragment : Fragment() {
    private var _binding: FragmentHadethBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: HadethViewModel by viewModels {
        ViewModelFactory { HadethViewModel(requireContext().appContainer.hadethRepository) }
    }
    private val adapter = HadethAdapter(::showHadethDetails)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHadethBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        binding.retryButton.setOnClickListener { viewModel.loadHadeths() }
        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::render)
            }
        }
    }

    private fun render(state: HadethUiState) = with(binding) {
        progressBar.isVisible = state.isLoading
        recyclerView.isVisible = !state.isLoading && state.errorMessage == null
        errorGroup.isVisible = state.errorMessage != null
        errorText.text = state.errorMessage
        adapter.submitList(state.hadeths)
    }

    private fun showHadethDetails(hadeth: Hadeth) {
        startActivity(Intent(requireContext(), HadethDetailsActivity::class.java).apply {
            putExtra(Constants.EXTRA_HADETH_ID, hadeth.id)
        })
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
