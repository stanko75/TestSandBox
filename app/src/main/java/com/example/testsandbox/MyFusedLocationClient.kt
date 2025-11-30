package com.example.testsandbox

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.concurrent.TimeUnit

class MyFusedLocationClient(var logger: ILogger) {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var myActivity: Activity

    var locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResultLocal: LocationResult) {
            logger.log("\nLat: " + locationResultLocal.lastLocation?.latitude.toString()
                    + "\nLon: " + locationResultLocal.lastLocation?.longitude.toString()
                    + "\nAltitude" + locationResultLocal.lastLocation?.altitude.toString())
        }
    }

    fun startLocationUpdates() {
        myRequestLocationUpdates(myActivity)
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun myRequestLocationUpdates(myActivity: Activity) {

        if (ActivityCompat.checkSelfPermission(
                myActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                myActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                myActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }

        var locationRequest: LocationRequest
        val localInterval: Long = 0
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(localInterval)
        ).apply {
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setDurationMillis(TimeUnit.MINUTES.toMillis(Long.MAX_VALUE))
            setWaitForAccurateLocation(true)
            setMaxUpdates(Int.MAX_VALUE)
            setIntervalMillis(TimeUnit.SECONDS.toMillis(localInterval))
            setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(localInterval))
            setMinUpdateDistanceMeters(0F)
        }.build()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(myActivity)
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 123
    }

}