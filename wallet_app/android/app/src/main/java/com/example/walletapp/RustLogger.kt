package com.example.walletapp

class RustLogger {
    companion object {
        init {
            System.loadLibrary("rust_android_logging")
        }

        external fun initLogger()
        external fun logInfo(message: String)
        external fun logError(message: String)
        external fun logWarn(message: String)
        external fun logDebug(message: String)
        external fun logTrace(message: String)

        fun init() {
            initLogger()
        }
    }
}
