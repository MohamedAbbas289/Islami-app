package com.example.islamiapp.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory<VM : ViewModel>(
    private val initializer: () -> VM
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = initializer()
        require(modelClass.isInstance(viewModel)) {
            "ViewModelFactory created ${viewModel::class.java.name} for ${modelClass.name}"
        }
        return viewModel as T
    }
}
