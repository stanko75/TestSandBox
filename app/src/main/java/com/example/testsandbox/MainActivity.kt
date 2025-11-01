package com.example.testsandbox

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.testsandbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val maxLines = 500

    private val uiReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val msg = intent?.getStringExtra("message")
            if (msg != null) appendLog(msg)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        }

        var isSending = false
        binding.btnStart.setOnClickListener {
            appendLog("\n" + binding.btnStart.text)

            isSending = !isSending
            if (isSending) {
                binding.btnStart.text = "Stop"

                registerReceiver(uiReceiver,
                    IntentFilter(IntentGlobalActions.UI_UPDATE), RECEIVER_NOT_EXPORTED)

                val intent = Intent(this, SeparateProcessServiceExample::class.java)
                intent.action = IntentGlobalActions.START_FOREGROUND_SERVICE
                startForegroundService(intent)
            } else {
                binding.btnStart.text = "Start"

                unregisterReceiver(uiReceiver)

                val intent = Intent(this, SeparateProcessServiceExample::class.java)
                intent.action = IntentGlobalActions.STOP_FOREGROUND_SERVICE
                startForegroundService(intent)
            }
        }
    }

    fun appendLog(message: String) {
        runOnUiThread {
            binding.tvLog.append("\n$message")

            val lines = binding.tvLog.text.split("\n")
            if (lines.size > maxLines) {
                val trimmed = lines.takeLast(maxLines).joinToString("\n")
                binding.tvLog.text = trimmed
            }

            binding.scrollLog.post {
                binding.scrollLog.fullScroll(View.FOCUS_DOWN)
            }
        }
    }
}