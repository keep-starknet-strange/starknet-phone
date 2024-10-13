package com.snphone.lightclientservice

import android.content.ContextWrapper
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

import com.snphone.lightclientservice.BuildConfig


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
        assertEquals("com.example.lightclientservice", appContext.packageName)
    }

    @Test
    fun echo() {
        val msg = BeerusClient.echo("Hello");
        assertEquals("hello", msg)
    }

    @Test
    fun echoBlock() {
        val msg = BeerusClient.echoBlock("hello");
        assertEquals("hello", msg)
    }

    @Test
    fun runBeerus() {

        val dataDir = "data/data/com.snphone.lightclientservice"

        val result = BeerusClient.run(
            BuildConfig.ETH_SEPOLIA_RPC_URL,
            BuildConfig.STARKNET_SEPOLIA_RPC_URL,
            dataDir
        );
        assertEquals("Beerus client run successfully", result)
    }
}