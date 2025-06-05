package com.snphone.lightclientservice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val startServiceButton: Button = findViewById(R.id.startServiceButton)
        startServiceButton.setOnClickListener {
            println("Attempting to start the service...")
            // Create an intent to start the service
            val serviceIntent = Intent(this, BeerusService::class.java)
            // Start the service
            startService(serviceIntent)
            println("service started?")
        }
    }
}