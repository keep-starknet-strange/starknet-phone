package com.example.walletapp.utils

import androidx.room.TypeConverter
import com.swmansion.starknet.data.types.Felt

class Converters {

    @TypeConverter
    fun fromFelt(felt: Felt): String {
        return felt.hexString() // Convert Felt to String
    }

    @TypeConverter
    fun toFelt(feltString: String): Felt {
        return Felt.fromHex(feltString) // Convert String back to Felt
    }
}
