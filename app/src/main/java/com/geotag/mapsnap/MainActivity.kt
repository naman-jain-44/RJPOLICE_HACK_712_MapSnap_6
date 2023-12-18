package com.geotag.mapsnap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView

class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapView)
        onMapReady()
    }
    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(17.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri("mapbox://styles/shrey1002/clpsn7gyl018z01pabppe3kdv")
    }
}