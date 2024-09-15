package com.example.serviceexample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startServiceButton: Button = findViewById(R.id.startServiceButton)
        startServiceButton.setOnClickListener {
            // Create an intent to start the service
            val serviceIntent = Intent(this, MyService::class.java)
            // Start the service
            startService(serviceIntent)
        }
    }
}