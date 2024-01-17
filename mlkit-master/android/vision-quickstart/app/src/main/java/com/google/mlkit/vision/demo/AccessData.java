package com.google.mlkit.vision.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AccessData extends AppCompatActivity {

    private Spinner spinnerOptions;
    private EditText editTextVehicleNumber;
    private Button buttonSearch,buttonSearch2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_data);


        spinnerOptions = findViewById(R.id.spinnerOptions);
        editTextVehicleNumber = findViewById(R.id.editTextVehicleNumber);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch2=findViewById(R.id.buttonSearch2);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected option from the spinner
                String selectedOption = spinnerOptions.getSelectedItem().toString();

                // If the spinner option is not selected, use "Car" as the default
                if (selectedOption.isEmpty()) {
                    selectedOption = "Car";
                }

                // Create an Intent to start the ShowData activity
                Intent intent = new Intent(AccessData.this, ShowData.class);

                // Pass the selected option to the ShowData activity
                intent.putExtra("selectedOption", selectedOption);

                // Start the ShowData activity
                startActivity(intent);
            }
        });


        buttonSearch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to start the ShowData activity
                Intent intent = new Intent(AccessData.this, ShowData.class);

                // Pass the selected option to the ShowData activity
                intent.putExtra("selectedOption", editTextVehicleNumber.getText().toString());

                // Start the ShowData activity
                startActivity(intent);

            }
        });
    }
}