package com.example.walletapp.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.walletapp.model.Token
import kotlinx.coroutines.launch

class TokenViewModel(application: Application, private val repository: TokenRepository) : AndroidViewModel(application) {


    // Expose all tokens as LiveData
    val tokens: LiveData<List<Token>> = repository.allTokens.asLiveData()

    // Function to insert a new token
    fun insertToken(token: Token) {
        viewModelScope.launch {
            repository.insertToken(token)
        }
    }

    // Define a factory to pass the repository
    class Factory(private val application: Application, private val repository: TokenRepository) :
        ViewModelProvider.AndroidViewModelFactory(application) {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TokenViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TokenViewModel(application, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

