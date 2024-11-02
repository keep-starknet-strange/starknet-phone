package com.example.walletapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.walletapp.model.Token
import com.example.walletapp.utils.Converters
import com.swmansion.starknet.data.types.Felt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Token::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TokenDatabase : RoomDatabase() {

    abstract fun tokenDao(): TokenDao

    companion object {
        @Volatile
        private var INSTANCE: TokenDatabase? = null

        fun getDatabase(context: Context): TokenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TokenDatabase::class.java,
                    "token_database"
                )
                    .addCallback(DatabaseCallback())  // Add callback here
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.tokenDao())
                }
            }
        }

        suspend fun populateDatabase(tokenDao: TokenDao) {
            // Add default tokens
            val token1 = Token(contactAddress = Felt.fromHex("0x049d36570d4e46f48e99674bd3fcc84644ddd6b96f7c741b1562b82f9e004dc7"), name = "ethereum", symbol = "eth", decimals = 18,tokenId="ethereum")
            val token2 = Token(contactAddress = Felt.fromHex("0x04718f5a0fc34cc1af16a1cdee98ffb20c31f5cd61d6ab07201858f4287c938d"), name = "starknet", symbol = "strk", decimals = 18,tokenId="starknet")
            tokenDao.insert(token1)
            tokenDao.insert(token2)
        }
    }
}
