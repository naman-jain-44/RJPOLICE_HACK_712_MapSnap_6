package com.geotag.mapsnap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ShowData extends AppCompatActivity {

    private ListView listViewData;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        listViewData = findViewById(R.id.listViewData);

        // Get the selected option from the intent
        String selectedOption = getIntent().getStringExtra( "selectedOption");
        ArrayList<String> cameraDataList;
        getCameraData(selectedOption);



    }

    private void getCameraData(String searchString) {
        CollectionReference ref = db.collection("cameradata");
        ArrayList<String> resultList = new ArrayList<>();
        HashSet<String> stringSet = new HashSet<>();

        db.collection("cameradata")
                .whereEqualTo("objectData", searchString)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Integer prev=0;
                            Integer current=0;
                            String previd="-1",curid="-1";
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                current=Integer.parseInt(document.getId());
//                                curid=document.getData().get("CameraID").toString();


                                String temp=document.getId() + "#" + document.getData().get("CameraID");
//                                Log.d("TAGG",""+(Integer.parseInt(document.getId())));
//                                if((current-prev)/1000>10){
//                                    stringSet.add(temp);
//                                }
//                                if(!previd.equals(curid)){
                                    stringSet.add(temp);
//                                }
//                                previd=curid;
//                                prev=current;
                            }
                            ArrayList<String> data = new ArrayList<>();

                            for(String e:stringSet) {
                                String[] parts = e.split("#");
                                String combinedString = "Timestamp :"+parts[0]+"\n"
                                        +"CameraId"+parts[1]+"\n";
                                combinedString=combinedString+"\n"+"\n";
//                                Log.d("TAGG",""+parts);
                                data.add(combinedString);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShowData.this, android.R.layout.simple_list_item_1, data);
                            listViewData.setAdapter(adapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}