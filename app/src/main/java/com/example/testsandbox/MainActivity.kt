package com.example.testsandbox

import android.os.Bundle
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

        val logger = Logger()
        logger.scrollLog = scrollLog
        logger.tvLog = tvLog
        logger.maxLines = maxLines

        val myFusedLocationClient = MyFusedLocationClient(logger)
        myFusedLocationClient.myActivity = this

        binding.btnStartFusedLocationProviderClient.text = "Start FusedLocationProviderClient"

        var isSendingStartFusedLocationProviderClient = false
        binding.btnStartFusedLocationProviderClient.setOnClickListener {
            logger.log("\n" + binding.btnStartFusedLocationProviderClient.text)

            isSendingStartFusedLocationProviderClient = !isSendingStartFusedLocationProviderClient
            if (isSendingStartFusedLocationProviderClient) {
                binding.btnStartFusedLocationProviderClient.text = "Stop FusedLocationProviderClient"

                myFusedLocationClient.startLocationUpdates()

            } else {
                binding.btnStartFusedLocationProviderClient.text = "Start FusedLocationProviderClient"
                myFusedLocationClient.stopLocationUpdates()
            }
        }

        var isSendingStartLocationManager = false
        binding.btnStartLocationManager.setOnClickListener {
            logger.log("\n" + binding.btnStartLocationManager.text)

            isSendingStartLocationManager = !isSendingStartLocationManager
            if (isSendingStartLocationManager) {
                binding.btnStartLocationManager.text = "Stop LocationManager"
            } else {
                binding.btnStartLocationManager.text = "Start LocationManager"
            }
        }
    }

}