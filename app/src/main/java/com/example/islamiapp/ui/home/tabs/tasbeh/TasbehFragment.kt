package com.example.islamiapp.ui.home.tabs.tasbeh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.islamiapp.R
import com.example.islamiapp.databinding.FragmentTasbehBinding
import kotlinx.coroutines.launch

class TasbehFragment : Fragment() {
    private var _binding: FragmentTasbehBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: TasbehViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasbehBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sebhaBtn.setOnClickListener { viewModel.increment() }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::render)
            }
        }
    }

    private fun render(state: TasbehUiState) = with(binding) {
        counter.text = state.counter.count.toString()
        sebhaBody.rotation = state.rotationDegrees
        sebhaBtn.setText(PHRASE_RESOURCES[state.counter.phraseIndex])
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private companion object {
        val PHRASE_RESOURCES = intArrayOf(
            R.string.tasbeh_subhan_allah,
            R.string.tasbeh_alhamdulillah,
            R.string.tasbeh_allahu_akbar
        )
    }
}
