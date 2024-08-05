package com.example.rust_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle



class MainActivity : AppCompatActivity() {
    companion object {

        private external fun rustHello()

        init {
            rustHello()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        System.loadLibrary("rust_example_project")
    }
}
