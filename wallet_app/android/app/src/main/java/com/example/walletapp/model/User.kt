package com.example.walletapp.model

class User {
    private var accounts: Array<Account> = emptyArray();

    fun hasAccount(): Boolean {
        return accounts.isNotEmpty()
    }

    fun getAccounts(): Array<Account> {
        return accounts
    }
}
