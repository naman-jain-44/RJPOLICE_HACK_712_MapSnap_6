package com.geotag.mapsnap

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.google.gson.Gson
import com.google.gson.JsonElement
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
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    data class Coordinate(val latitude: Double, val longitude: Double)
    private lateinit var mapView: MapView
    var currentLatitude=0.0
    var currentLongitude=0.0
    var markerList :ArrayList<PointAnnotationOptions> = ArrayList()
    var coordinateList: MutableList<Coordinate> = mutableListOf()

    lateinit var v: View

    var annotationApi : AnnotationPlugin? = null
    lateinit var annotaionConfig : AnnotationConfig
    var pointAnnotationManager : PointAnnotationManager? = null
    val layerID = "cameras";
    var annotationAdded = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_map, container, false)
        mapView = v.findViewById(R.id.mapView)
        onMapReady()

        v.findViewById<Button>(R.id.search).setOnClickListener{
            var inputLatitude= v.findViewById<EditText>(R.id.latitude).text.toString()
            var inputLongitude=v.findViewById<EditText>(R.id.longitude).text.toString()
            if(TextUtils.isEmpty(inputLongitude) || TextUtils.isEmpty(inputLatitude)){
                Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show();
            }
            else {
                currentLatitude = inputLatitude.toDouble()
                currentLongitude = inputLongitude.toDouble()
                mapView.getMapboxMap().setCamera(
                    CameraOptions.Builder()
                        .center(Point.fromLngLat(currentLongitude, currentLatitude))
                        .zoom(17.0)
                        .build()
                )
                createLatLongForMarker()
            }
        }
        return v;
    }
    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(17.0)
                .build()
        )
        mapView.getMapboxMap().loadStyle(
            style(styleUri="mapbox://styles/shrey1002/clpsn7gyl018z01pabppe3kdv")
            {
                annotationApi = mapView?.annotations
                annotaionConfig = AnnotationConfig(
                    layerId = layerID
                )
                pointAnnotationManager = annotationApi?.createPointAnnotationManager(annotaionConfig)!!
            }
        )
        mapView.getMapboxMap().addOnCameraChangeListener() {
            val currentZoom = mapView.getMapboxMap().cameraState.zoom
            if(currentZoom<10 && annotationAdded){
//                Toast.makeText(this, "$currentZoom", Toast.LENGTH_SHORT).show()
                pointAnnotationManager?.deleteAll()
                pointAnnotationManager?.deleteAll()
                annotationAdded=false
            }
            else if(currentZoom>=10 && markerList.isNotEmpty() && !annotationAdded){
                pointAnnotationManager?.create(markerList)
                annotationAdded=true
            }
//            else if(currentZoom>=10 && !markerList.isNotEmpty() ){
//                createMarkerList();
//            }
        }

    }

    private fun createLatLongForMarker(){
        coordinateList.clear()
        val radius = 1000.0 // 2km radius
        val numberOfPoints = 49
        var inputLatitude= v.findViewById<EditText>(R.id.latitude).text.toString()
        var inputLongitude=v.findViewById<EditText>(R.id.longitude).text.toString()
        currentLatitude = inputLatitude.toDouble()
        currentLongitude = inputLongitude.toDouble()
        val centerLatitude = currentLatitude
        val centerLongitude = currentLongitude

        val random = Random.Default

        for (i in 0 until numberOfPoints) {
            val angle = random.nextDouble(0.0, 2 * Math.PI)
            val distance = random.nextDouble(0.0, radius)

            val latitudeOffset = distance * sin(angle) / 110574.0 // Convert to degrees
            val longitudeOffset = distance * cos(angle) / (111320.0 * cos(centerLatitude * Math.PI / 180.0)) // Convert to degrees

            val latitude = centerLatitude + latitudeOffset
            val longitude = centerLongitude + longitudeOffset

            coordinateList.add(Coordinate(latitude, longitude))
        }
        coordinateList.add(
            Coordinate(
                (currentLatitude + 0.0004),
                (currentLongitude + 0.0004)
            )
        )

        createMarkerList()
    }

    private fun createMarkerList(){

        clearAnnotation();
        markerList.clear()


        // It will work when we create marker
        pointAnnotationManager?.addClickListener(OnPointAnnotationClickListener { annotation: PointAnnotation ->
            var pointerLatitude=annotation.point.latitude()
            var pointerLongitude=annotation.point.longitude()
            onMarkerItemClick(annotation)
            true
        })
        markerList =  ArrayList();
//        val randomThreeD=(15..30).random()
        Collections.swap(coordinateList,0,23)
        var stashIconSize=0.1
        var stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.cam))
        for (i in 0 until  50){
//            if(i<=1){
//                stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.ar_marker_new))
//            }
//            else if(i<=31){
//                stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.simple_marker))
//                stashIconSize=0.15
//            }
//            else if(i<=39){
//                stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.b))
//                stashIconSize=0.4
//            }
//            else if(i<=47){
//                stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.c))
//                stashIconSize=0.4
//            }
//            else if(i<=49){
//                stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.d))
//                stashIconSize=0.4
//            }
//            else{
//                stashIcon = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.simple_marker))
//                stashIconSize=0.15
//            }
            var keyJsonObject = JSONObject();
            keyJsonObject.put("key",i);
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(coordinateList.get(i).longitude,coordinateList.get(i).latitude))
                .withData(Gson().fromJson(keyJsonObject.toString(), JsonElement::class.java))
                .withIconImage(stashIcon!!)
                .withIconSize(stashIconSize)

            markerList.add(pointAnnotationOptions);
        }

        pointAnnotationManager?.create(markerList)
    }

    fun clearAnnotation(){
        markerList = ArrayList();
        pointAnnotationManager?.deleteAll()
    }
    private fun onMarkerItemClick(marker: PointAnnotation) {
        var dialog= Dialog(requireContext())
        dialog.setContentView(R.layout.details)
        dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.findViewById<ImageButton>(R.id.close).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
// copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, 10, 10)
            drawable.draw(canvas)
            bitmap
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}