package com.example.rust_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rust_example.RustLib


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val result = RustLib.hello("TEST")
        println(result)
    }
}
