package com.google.mlkit.vision.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowData extends AppCompatActivity {


    private ListView listViewData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        listViewData = findViewById(R.id.listViewData);

        // Get the selected option from the intent
        String selectedOption = getIntent().getStringExtra("selectedOption");
        ArrayList<String> cameraDataList ;

        if(selectedOption.equals("Car")||selectedOption.equals("People")||selectedOption.equals("Bus")||selectedOption.equals("Road")){
            // Sample data: replace this with your actual data
             cameraDataList = getCameraData(selectedOption);
        }else{
            // Sample data: replace this with your actual data
         cameraDataList = getCameraData(selectedOption);

        }


        // Create an ArrayAdapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cameraDataList);

        // Set the adapter for the ListView
        listViewData.setAdapter(adapter);
    }

    // Sample method to get camera data (replace this with your actual data retrieval logic)
    private ArrayList<String> getCameraData(String selectedOption) {
        ArrayList<String> data = new ArrayList<>();

        // TODO: Replace this with your actual data retrieval logic
        // For now, add some sample data
        data.add("Camera ID: 001 | Timestamp: 2023-01-01 12:00:00");
        data.add("Camera ID: 002 | Timestamp: 2023-01-01 12:30:00");
        data.add("Camera ID: 003 | Timestamp: 2023-01-01 13:00:00");

        return data;
    }
}