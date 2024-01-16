package com.geotag.mapsnap.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.geotag.mapsnap.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var v: View
    lateinit var latitude: EditText
    lateinit var longitude: EditText
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_register, container, false)

        var currentLocation = v.findViewById<Button>(R.id.gps)
        currentLocation.setOnClickListener { checkLocationPermission() }
        latitude=v.findViewById(R.id.latitude)
        longitude=v.findViewById(R.id.longitude)

        var registerBtn = v.findViewById<Button>(R.id.register)
        registerBtn.setOnClickListener { registerCamera() }


        // Initialize the spinner
        val constantSpinner: Spinner = v.findViewById(R.id.constant)
        val nightSpinner: Spinner = v.findViewById(R.id.night)

        // Define the options for the spinner
        val constantOptions = listOf("Select the constant element","Tree", "Building")
        val nightOptions = listOf("Select if Night mode present","Yes", "No")

        // Create an ArrayAdapter using the string array and a default spinner layout
        val constantAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, constantOptions)
        val nightAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nightOptions)

        // Specify the layout to use when the list of choices appears
        constantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        nightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        constantSpinner.adapter = constantAdapter
        nightSpinner.adapter = nightAdapter

        return v;
    }
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, proceed to get location
            getLocation()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to get location
                getLocation()
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        // Use FusedLocationProviderClient for better accuracy and battery efficiency
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatitude = location.latitude
                    val currentLongitude = location.longitude
                    latitude.setText(currentLatitude.toString());
                    longitude.setText(currentLongitude.toString());

                } else {
                    // Location is null, handle accordingly
                    Toast.makeText(requireContext(), "Location not available, enable GPS and retry", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
    private fun registerCamera() {


        val latitude = v?.findViewById<EditText>(R.id.latitude)?.text.toString().toDouble()
        val longitude = v?.findViewById<EditText>(R.id.longitude)?.text.toString().toDouble()
        val model = v?.findViewById<EditText>(R.id.model)?.text.toString()
        val resolution = v?.findViewById<EditText>(R.id.resolution)?.text.toString()
        val fps = v?.findViewById<EditText>(R.id.fps)?.text.toString()
        val range = v?.findViewById<EditText>(R.id.range)?.text.toString()
        val owner = v?.findViewById<EditText>(R.id.owner)?.text.toString()
        val number = v?.findViewById<EditText>(R.id.number)?.text.toString()
        val constant=v?.findViewById<Spinner>(R.id.constant)?.selectedItem.toString()
        val night=v?.findViewById<Spinner>(R.id.night)?.selectedItem.toString()
        if (constant == "Select the constant element" || night=="Select if Night mode present") {
            Toast.makeText(requireContext(), "Please select an option", Toast.LENGTH_SHORT).show()
        } else {
            val cameraData = hashMapOf(
                "latitude" to latitude,
                "longitude" to longitude,
                "model" to model,
                "resolution" to resolution,
                "fps" to fps,
                "range" to range,
                "owner" to owner,
                "number" to number,
                "constant" to constant,
                "nightVision" to night
            )
            val decimalFormat = DecimalFormat("#.#")
            var latVal = (decimalFormat.format(latitude).toDouble()).toString()
            var lonVal = (decimalFormat.format(longitude).toDouble()).toString()
            var docId = number+"#"+latitude.toString()+"#"+longitude.toString()
            db.collection("coordinates").document(latVal).collection(lonVal).document(docId)
                .set(cameraData)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}