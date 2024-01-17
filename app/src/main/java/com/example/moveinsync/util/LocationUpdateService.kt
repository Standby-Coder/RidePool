package com.example.moveinsync.util

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class LocationUpdateService : Service() {

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var handler: Handler
    private val updateInterval: Long = 5000 // 5 seconds

    override fun onCreate() {
        super.onCreate()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Handle location updates, you can send broadcasts or update your API here
                // For simplicity, let's just log the location
                val latitude = location.latitude
                val longitude = location.longitude
                Log.d("LocationUpdateService", "Latitude: $latitude, Longitude: $longitude")
                Toast.makeText(this@LocationUpdateService, "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_SHORT).show()
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        handler = Handler()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Schedule the location update runnable
        handler.post(updateRunnable)
        return START_STICKY
    }

    private val updateRunnable = object : Runnable {
        override fun run() {
            try {
                // Request location updates
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0f,
                    locationListener
                )
            } catch (ex: SecurityException) {
                Log.d("LocationUpdateService", "Error requesting location updates: $ex")
            }

            // Schedule the next update
            handler.postDelayed(this, updateInterval)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        // Remove callbacks and stop location updates when the service is destroyed
        handler.removeCallbacks(updateRunnable)
        try {
            locationManager.removeUpdates(locationListener)
        } catch (ex: SecurityException) {
            // Handle exception
        }

        super.onDestroy()
    }
}
