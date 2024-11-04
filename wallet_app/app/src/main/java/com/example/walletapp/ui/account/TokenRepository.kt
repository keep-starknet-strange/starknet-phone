package com.example.walletapp.ui.account

import com.example.walletapp.db.TokenDao
import com.example.walletapp.model.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TokenRepository(private val tokenDao: TokenDao) {

    // Get all tokens from the database
    val allTokens: Flow<List<Token>> = tokenDao.getAllTokens()

    // Insert a token into the database
    suspend fun insertToken(token: Token) {
        withContext(Dispatchers.IO) {
            tokenDao.insert(token)
        }
    }
}
