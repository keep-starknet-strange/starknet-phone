package com.example.walletapp.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.walletapp.data.repository.StarknetRepository

class StarknetViewModelFactory(private val repository: StarknetRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StarknetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StarknetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
