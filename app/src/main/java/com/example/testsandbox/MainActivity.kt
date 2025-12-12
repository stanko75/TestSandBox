package com.example.testsandbox

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.testsandbox.MyFusedLocationClient.Companion.REQUEST_LOCATION_PERMISSION
import com.example.testsandbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var gpsListener: LocationListener
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

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        var isSendingStartLocationManager = false
        binding.btnStartLocationManager.setOnClickListener {
            logger.log("\n" + binding.btnStartLocationManager.text)

            isSendingStartLocationManager = !isSendingStartLocationManager
            if (isSendingStartLocationManager) {
                binding.btnStartLocationManager.text = "Stop LocationManager"

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_LOCATION_PERMISSION
                    )
                }

                gpsListener = LocationListener { location ->
                    logger.log("\nLocationManager: "
                        + "\nLat: " + location.latitude.toString()
                                + "\nLon: " + location.longitude.toString()
                                + "\nAltitude: " + location.altitude.toString()
                    )
                }

                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000L,
                    0f,
                    gpsListener
                )

            } else {
                binding.btnStartLocationManager.text = "Start LocationManager"
                locationManager.removeUpdates(gpsListener)
            }
        }
    }

}