package com.geotag.mapsnap.Fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.geotag.mapsnap.Camera
import com.geotag.mapsnap.R
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PendingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PendingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var v: View
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
        v=inflater.inflate(R.layout.fragment_pending, container, false)
        val data = ArrayList<String>()
        val ref=db.collection("pending")
        ref.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val camera=document.toObject(Camera::class.java)
                    var combinedString = """
                    Owner: ${camera?.owner}
                    Camera Model: ${camera?.model}
                    Resolution: ${camera?.resolution}
                    FPS: ${camera?.fps}
                    Visibility: ${camera?.range}
                    M.No: ${camera?.number}
                    Night Vision: ${camera?.nightVision}
                    ID: ${document.id}"""
                    combinedString=combinedString+"\n"+"\n"
                    data.add(combinedString);
                }
                var list=v.findViewById<ListView>(R.id.pendingList)
                val adapter = ArrayAdapter(v.context, android.R.layout.simple_list_item_1, data)

                // Set the adapter for the ListView
                list.adapter = adapter
            }
            .addOnFailureListener { e ->
                // Handle error during the fetching process
                Log.e(ContentValues.TAG, "Error fetching coordinates: $e")
            }
        return v;
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PendingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}