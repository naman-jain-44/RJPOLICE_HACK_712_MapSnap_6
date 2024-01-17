package com.geotag.mapsnap.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.geotag.mapsnap.DisplayData
import com.geotag.mapsnap.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var spinnerOptions: Spinner
    lateinit var editTextVehicleNumber: EditText
    lateinit var buttonSearch: Button
    lateinit var buttonSearch2:Button
    lateinit var v : View

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
        v=inflater.inflate(R.layout.fragment_results, container, false)
        spinnerOptions = v.findViewById(R.id.spinnerOptions);
        val options = listOf("People","Car","Bus","Road")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinnerOptions.adapter = adapter
        editTextVehicleNumber = v.findViewById(R.id.editTextVehicleNumber);
        buttonSearch = v.findViewById(R.id.buttonSearch);
        buttonSearch2= v.findViewById(R.id.buttonSearch2);

        buttonSearch.setOnClickListener { // Get the selected option from the spinner
            var selectedOption = spinnerOptions.selectedItem.toString()

            // If the spinner option is not selected, use "Car" as the default
            if (selectedOption.isEmpty()) {
                selectedOption = "Car"
            }

            // Create an Intent to start the ShowData activity
            val intent = Intent(requireContext(), DisplayData::class.java)

            // Pass the selected option to the ShowData activity
            intent.putExtra("selectedOption", selectedOption)

            // Start the ShowData activity
            startActivity(intent)
        }


        buttonSearch2.setOnClickListener { // Create an Intent to start the ShowData activity
            val intent = Intent(requireContext(), DisplayData::class.java)

            // Pass the selected option to the ShowData activity
            intent.putExtra("selectedOption", editTextVehicleNumber.text.toString())

            // Start the ShowData activity
            startActivity(intent)
        }

        return v;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResultsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}