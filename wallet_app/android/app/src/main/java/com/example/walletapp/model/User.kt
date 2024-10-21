package com.example.walletapp.model

data class User (
    val accounts: Array<Account>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return accounts.contentEquals(other.accounts)
    }

    override fun hashCode(): Int {
        return accounts.contentHashCode()
    }
}
