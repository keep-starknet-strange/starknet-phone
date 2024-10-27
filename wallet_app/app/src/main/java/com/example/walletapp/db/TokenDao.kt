package com.example.walletapp.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.walletapp.model.Token
import kotlinx.coroutines.flow.Flow
import androidx.room.Query

@Dao
interface TokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(token: Token)

    @Query("SELECT * FROM token_table")
    fun getAllTokens(): Flow<List<Token>>
}
