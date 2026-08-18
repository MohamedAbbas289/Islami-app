package com.example.islamiapp.ui.home.tabs.hadeth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.islamiapp.databinding.ActivityHadethDetailsBinding
import com.example.islamiapp.ui.Constants
import com.example.islamiapp.ui.common.ViewModelFactory
import com.example.islamiapp.ui.common.appContainer
import kotlinx.coroutines.launch

class HadethDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHadethDetailsBinding
    private val hadethId by lazy {
        intent.getIntExtra(Constants.EXTRA_HADETH_ID, INVALID_HADETH)
    }
    private val viewModel: HadethDetailsViewModel by viewModels {
        ViewModelFactory {
            HadethDetailsViewModel(hadethId, appContainer.hadethRepository)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (hadethId == INVALID_HADETH) {
            finish()
            return
        }
        binding = ActivityHadethDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { finish() }
        binding.retryButton.setOnClickListener { viewModel.loadHadeth() }
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::render)
            }
        }
    }

    private fun render(state: HadethDetailsUiState) = with(binding) {
        progressBar.isVisible = state.isLoading
        contentGroup.isVisible = state.hadeth != null
        errorGroup.isVisible = state.errorMessage != null
        errorText.text = state.errorMessage
        hadethName.text = state.hadeth?.title
        hadethContent.text = state.hadeth?.content
    }

    private companion object {
        const val INVALID_HADETH = -1
    }
}
