package com.geotag.mapsnap

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.geotag.mapsnap.Fragments.PendingFragment
import com.geotag.mapsnap.Fragments.RegisterFragment
import com.geotag.mapsnap.Fragments.ResultsFragment
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import org.json.JSONObject
import java.util.Collections
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    data class Coordinate(val latitude: Double, val longitude: Double)
    private lateinit var mapView: MapView
    var currentLatitude=0.0
    var currentLongitude=0.0
    var markerList :ArrayList<PointAnnotationOptions> = ArrayList()
    var coordinateList: MutableList<Coordinate> = mutableListOf()

    var annotationApi : AnnotationPlugin? = null
    lateinit var annotaionConfig : AnnotationConfig
    var pointAnnotationManager : PointAnnotationManager? = null
    val layerID = "cameras";
    var annotationAdded = false




//-------------------------------------------------------------------
    lateinit var chipNavigationBarMain : ChipNavigationBar;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideSystemBars()
        chipNavigationBarMain = findViewById(R.id.main_nav)
        chipNavigationBarMain.setItemSelected(
            R.id.bottom_nav_map,
            true
        )
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out
            )
            .replace(
                R.id.fragment_container,
                MapFragment()
            ).commit()
        bottomMenu()
    }

    private fun bottomMenu() {
        chipNavigationBarMain.setOnItemSelectedListener { id ->
            val fragment: Fragment = when (id) {
                R.id.bottom_nav_map -> MapFragment()
                R.id.bottom_nav_register -> RegisterFragment()
                R.id.bottom_nav_results->ResultsFragment()
                R.id.bottom_nav_pending->PendingFragment()
                else -> throw IllegalArgumentException("Invalid menu item ID")
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
    private fun hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let { controller ->
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                window.navigationBarColor = Color.TRANSPARENT // Set transparent color for navigation bar
                controller.hide(WindowInsets.Type.systemBars())
            }
        } else {
            // For versions prior to Android 10
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }
}