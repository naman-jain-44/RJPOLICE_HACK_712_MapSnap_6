package com.geotag.mapsnap

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.geotag.mapsnap.Fragments.RegisterFragment
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


        chipNavigationBarMain = findViewById<ChipNavigationBar>(R.id.main_nav)
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
//        -----------------------------------------------------------------
//        mapView = findViewById(R.id.mapView)
//        onMapReady()
//
//        findViewById<Button>(R.id.search).setOnClickListener{
//            var inputLatitude= findViewById<EditText>(R.id.latitude).text.toString()
//            var inputLongitude=findViewById<EditText>(R.id.longitude).text.toString()
//            if(TextUtils.isEmpty(inputLongitude) || TextUtils.isEmpty(inputLatitude)){
//                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                currentLatitude = inputLatitude.toDouble()
//                currentLongitude = inputLongitude.toDouble()
//                mapView.getMapboxMap().setCamera(
//                    CameraOptions.Builder()
//                        .center(Point.fromLngLat(currentLongitude, currentLatitude))
//                        .zoom(17.0)
//                        .build()
//                )
//                createLatLongForMarker()
//            }
//        }
    }

    private fun bottomMenu() {
        chipNavigationBarMain.setOnItemSelectedListener { id ->
            val fragment: Fragment = when (id) {
                R.id.bottom_nav_map -> MapFragment()
                R.id.bottom_nav_register -> RegisterFragment()
                else -> throw IllegalArgumentException("Invalid menu item ID")
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }


//    private fun onMapReady() {
//        mapView.getMapboxMap().setCamera(
//            CameraOptions.Builder()
//                .zoom(17.0)
//                .build()
//        )
//        mapView.getMapboxMap().loadStyle(
//            style(styleUri="mapbox://styles/shrey1002/clpsn7gyl018z01pabppe3kdv")
//            {
//                annotationApi = mapView?.annotations
//                annotaionConfig = AnnotationConfig(
//                    layerId = layerID
//                )
//                pointAnnotationManager = annotationApi?.createPointAnnotationManager(annotaionConfig)!!
//            }
//        )
//        mapView.getMapboxMap().addOnCameraChangeListener() {
//            val currentZoom = mapView.getMapboxMap().cameraState.zoom
//            if(currentZoom<10 && annotationAdded){
////                Toast.makeText(this, "$currentZoom", Toast.LENGTH_SHORT).show()
//                pointAnnotationManager?.deleteAll()
//                pointAnnotationManager?.deleteAll()
//                annotationAdded=false
//            }
//            else if(currentZoom>=10 && markerList.isNotEmpty() && !annotationAdded){
//                pointAnnotationManager?.create(markerList)
//                annotationAdded=true
//            }
////            else if(currentZoom>=10 && !markerList.isNotEmpty() ){
////                createMarkerList();
////            }
//        }
//
//    }
//    private fun createLatLongForMarker(){
//        coordinateList.clear()
//        val radius = 1000.0 // 2km radius
//        val numberOfPoints = 49
//        var inputLatitude= findViewById<EditText>(R.id.latitude).text.toString()
//        var inputLongitude=findViewById<EditText>(R.id.longitude).text.toString()
//            currentLatitude = inputLatitude.toDouble()
//            currentLongitude = inputLongitude.toDouble()
//        val centerLatitude = currentLatitude
//        val centerLongitude = currentLongitude
//
//        val random = Random.Default
//
//        for (i in 0 until numberOfPoints) {
//            val angle = random.nextDouble(0.0, 2 * Math.PI)
//            val distance = random.nextDouble(0.0, radius)
//
//            val latitudeOffset = distance * sin(angle) / 110574.0 // Convert to degrees
//            val longitudeOffset = distance * cos(angle) / (111320.0 * cos(centerLatitude * Math.PI / 180.0)) // Convert to degrees
//
//            val latitude = centerLatitude + latitudeOffset
//            val longitude = centerLongitude + longitudeOffset
//
//            coordinateList.add(Coordinate(latitude, longitude))
//        }
//        coordinateList.add(Coordinate((currentLatitude+0.0004),(currentLongitude+0.0004)))
//
//        createMarkerList()
//    }
//    private fun createMarkerList(){
//
//        clearAnnotation();
//        markerList.clear()
//
//
//        // It will work when we create marker
//        pointAnnotationManager?.addClickListener(OnPointAnnotationClickListener { annotation: PointAnnotation ->
//            var pointerLatitude=annotation.point.latitude()
//            var pointerLongitude=annotation.point.longitude()
//            onMarkerItemClick(annotation)
//            true
//        })
//        markerList =  ArrayList();
////        val randomThreeD=(15..30).random()
//        Collections.swap(coordinateList,0,23)
//        var stashIconSize=0.1
//        var stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(this, R.drawable.cam))
//        for (i in 0 until  50){
////            if(i<=1){
////                stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.ar_marker_new))
////            }
////            else if(i<=31){
////                stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.simple_marker))
////                stashIconSize=0.15
////            }
////            else if(i<=39){
////                stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.b))
////                stashIconSize=0.4
////            }
////            else if(i<=47){
////                stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.c))
////                stashIconSize=0.4
////            }
////            else if(i<=49){
////                stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.d))
////                stashIconSize=0.4
////            }
////            else{
////                stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.simple_marker))
////                stashIconSize=0.15
////            }
//            var keyJsonObject = JSONObject();
//            keyJsonObject.put("key",i);
//            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
//                .withPoint(Point.fromLngLat(coordinateList.get(i).longitude,coordinateList.get(i).latitude))
//                .withData(Gson().fromJson(keyJsonObject.toString(), JsonElement::class.java))
//                .withIconImage(stashIcon!!)
//                .withIconSize(stashIconSize)
//
//            markerList.add(pointAnnotationOptions);
//        }
//
//        pointAnnotationManager?.create(markerList)
//    }

//    fun clearAnnotation(){
//        markerList = ArrayList();
//        pointAnnotationManager?.deleteAll()
//    }
//    private fun onMarkerItemClick(marker: PointAnnotation) {
//        var dialog= Dialog(this)
//        dialog.setContentView(R.layout.details)
//        dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.findViewById<ImageButton>(R.id.close).setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.show()
//    }
//    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
//        if (sourceDrawable == null) {
//            return null
//        }
//        return if (sourceDrawable is BitmapDrawable) {
//            sourceDrawable.bitmap
//        } else {
//// copying drawable object to not manipulate on the same reference
//            val constantState = sourceDrawable.constantState ?: return null
//            val drawable = constantState.newDrawable().mutate()
//            val bitmap: Bitmap = Bitmap.createBitmap(
//                drawable.intrinsicWidth, drawable.intrinsicHeight,
//                Bitmap.Config.ARGB_8888
//            )
//            val canvas = Canvas(bitmap)
//            drawable.setBounds(0, 0, 10, 10)
//            drawable.draw(canvas)
//            bitmap
//        }
//    }
}