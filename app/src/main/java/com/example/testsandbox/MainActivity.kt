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

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val msg = intent?.getStringExtra("message")
            appendLog("Receiver: Got: $msg")
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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

        val filter = IntentFilter(IntentAction.TEST_MESSAGE_ACTION)
        registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)

        var isSending = false
        binding.btnStart.setOnClickListener {
            appendLog("\n" + binding.btnStart.text)

            isSending = !isSending
            if (isSending) {
                binding.btnStart.text = "Stop"

                val intent = Intent(this, TestSandBoxService::class.java)
                intent.action = IntentAction.START_FOREGROUND_SERVICE
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