package com.example.testsandbox

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.testsandbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tvLog: TextView
    private lateinit var scrollLog: ScrollView
    private val maxLines = 500

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }

        tvLog = findViewById(R.id.tvLog)
        scrollLog = findViewById(R.id.scrollLog)

        var isSending = false

        binding.btnStart.setOnClickListener {

            val tvLog = findViewById<TextView>(R.id.tvLog)
            tvLog.append("\n" + binding.btnStart.text)

            isSending = !isSending
            if (isSending) {
                binding.btnStart.text = "Stop"

                val etUrl = findViewById<TextView>(R.id.etUrl)
                val baseUrl = etUrl.text

                val intent = Intent(this, TestSandBoxService::class.java)
                intent.action = IntentAction.START_FOREGROUND_SERVICE
                intent.putExtra("baseUrl", baseUrl.toString())
                startForegroundService(intent)
            } else {
                binding.btnStart.text = "Start"

                val intent = Intent(this, TestSandBoxService::class.java)
                intent.action = IntentAction.STOP_FOREGROUND_SERVICE
                startForegroundService(intent)
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