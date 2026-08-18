package com.example.islamiapp.ui.home.tabs.radio

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
import com.example.islamiapp.R
import com.example.islamiapp.databinding.FragmentRadioBinding
import com.example.islamiapp.ui.common.ViewModelFactory
import com.example.islamiapp.ui.common.appContainer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class RadioFragment : Fragment() {
    private var _binding: FragmentRadioBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: RadioViewModel by viewModels {
        ViewModelFactory {
            val container = requireContext().appContainer
            RadioViewModel(container.radioRepository, container.radioPlayer)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRadioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.previousButton.setOnClickListener { viewModel.previousStation() }
        binding.playButton.setOnClickListener { viewModel.togglePlayback() }
        binding.nextButton.setOnClickListener { viewModel.nextStation() }
        binding.stationName.setOnClickListener { showStationPicker() }
        binding.retryButton.setOnClickListener { viewModel.loadStations() }
        binding.playbackRetryButton.setOnClickListener { viewModel.retryPlayback() }
        observeState()
    }

    private fun showStationPicker() {
        val state = viewModel.state.value
        if (state.stations.isEmpty()) return

        val stationNames = state.stations.map { it.name }.toTypedArray()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.choose_radio_station)
            .setSingleChoiceItems(stationNames, state.currentIndex) { dialog, selectedIndex ->
                viewModel.selectStation(selectedIndex)
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::render)
            }
        }
    }

    private fun render(state: RadioUiState) = with(binding) {
        progressBar.isVisible = state.isLoading
        radioContent.isVisible = !state.isLoading && state.loadError == null
        errorGroup.isVisible = state.loadError != null
        errorText.text = state.loadError

        stationName.text = state.currentStation?.name.orEmpty()
        stationCounter.text = if (state.stations.isEmpty()) {
            ""
        } else {
            getString(
                R.string.radio_station_position,
                state.currentIndex + 1,
                state.stations.size
            )
        }

        bufferingIndicator.isVisible = state.isBuffering
        playbackErrorGroup.isVisible = state.playbackError != null
        playbackErrorText.text = state.playbackError
        previousButton.isEnabled = state.stations.size > 1
        nextButton.isEnabled = state.stations.size > 1
        stationName.isEnabled = state.stations.isNotEmpty()
        playButton.isEnabled = state.currentStation != null && !state.isBuffering
        playButton.setImageResource(
            if (state.isPlaying) R.drawable.radio_pause else R.drawable.radio_play
        )
        playButton.contentDescription = getString(
            if (state.isPlaying) R.string.pause_radio else R.string.play_radio
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
