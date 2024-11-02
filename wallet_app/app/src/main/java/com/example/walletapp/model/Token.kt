package com.example.walletapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.swmansion.starknet.data.types.Felt

@Entity(tableName = "token_table")
data class Token(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contactAddress: Felt,
    val name: String,
    val symbol: String,
    val decimals: Int,
    val tokenId:String
)