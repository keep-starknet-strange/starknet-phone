package com.example.walletapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.walletapp", appContext.packageName)
    }

    @Test
    fun testRustLogger() {
        RustLogger.init()
        RustLogger.logInfo("Test log message from Kotlin")
        // Note: This test will pass as long as no exception is thrown.
        // You'll need to check the logcat output to verify the log message.
    }
}
