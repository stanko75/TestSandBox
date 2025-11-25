package com.example.testsandbox

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.testsandbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tvLog: TextView
    private lateinit var scrollLog: ScrollView
    private val maxLines = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvLog = findViewById(R.id.tvLog)
        scrollLog = findViewById(R.id.scrollLog)


        var isSendingStartFusedLocationProviderClient = false
        binding.btnStartFusedLocationProviderClient.setOnClickListener {
            appendLog("\n" + binding.btnStartFusedLocationProviderClient.text)

            isSendingStartFusedLocationProviderClient = !isSendingStartFusedLocationProviderClient
            if (isSendingStartFusedLocationProviderClient) {
                binding.btnStartFusedLocationProviderClient.text = "Stop FusedLocationProviderClient"
            } else {
                binding.btnStartFusedLocationProviderClient.text = "Start FusedLocationProviderClient"
            }
        }

        var isSendingStartLocationManager = false
        binding.btnStartLocationManager.setOnClickListener {
            appendLog("\n" + binding.btnStartLocationManager.text)

            isSendingStartLocationManager = !isSendingStartLocationManager
            if (isSendingStartLocationManager) {
                binding.btnStartLocationManager.text = "Stop LocationManager"
            } else {
                binding.btnStartLocationManager.text = "Start LocationManager"
            }
        }
    }

    fun appendLog(message: String) {
        tvLog.append("\n$message")

        val lines = tvLog.text.split("\n")
        if (lines.size > maxLines) {
            val trimmed = lines.takeLast(maxLines).joinToString("\n")
            tvLog.text = trimmed
        }

        scrollLog.post {
            scrollLog.fullScroll(View.FOCUS_DOWN)
        }
    }
}