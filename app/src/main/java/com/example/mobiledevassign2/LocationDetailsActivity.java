package com.example.mobiledevassign2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;                      //Imports libraries
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LocationDetailsActivity extends AppCompatActivity {

    private Location location;                          //Defines variables
    private DBHandler db;
    private EditText addressET, latitudeET, longitudeET;
    private Button updateButton, deleteButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        db = new DBHandler(this);           //DBHandler object

        int locationId = getIntent().getIntExtra("locationId", -1);   //Gets intent with the name "locationId" and passes it to locationId
        if (locationId != -1){
            location = db.getLocation(locationId);
            if (location != null){          //If the location value isn't null
                addressET = findViewById(R.id.address);         //Finds edittexts by ids
                latitudeET = findViewById(R.id.latitude);
                longitudeET = findViewById(R.id.longitude);

                updateButton = findViewById(R.id.update_button);        //Finds buttons by id
                deleteButton = findViewById(R.id.delete_button);
                backButton = findViewById(R.id.back_button);

                addressET.setText(location.getAddress());           //Sets the text
                latitudeET.setText(String.valueOf(location.getLatitude()));
                longitudeET.setText(String.valueOf(location.getLongitude()));

                updateButton.setOnClickListener(new View.OnClickListener() {        //onClickListener for the update button
                    @Override
                    public void onClick(View v) {
                        String updatedAddr = addressET.getText().toString();        //Gets the updated address and puts it to string
                        double updatedLat = Double.parseDouble(latitudeET.getText().toString());    //Parses the latitude
                        double updatedLong = Double.parseDouble(longitudeET.getText().toString());  //Parses the longitude

                        db.updateLocation(location.getId(), updatedAddr, updatedLat, updatedLong);  //Calls the .updateLocation() method in DBHandler to update
                        Toast.makeText(LocationDetailsActivity.this, "Location has been updated.", Toast.LENGTH_SHORT).show();      //Toast when location has been updated
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {        //onClickListener for the delete button
                    @Override
                    public void onClick(View v) {
                        db.deleteLocation(location.getId());        //Calls the deleteLocation() method from DBHandler
                        finish();
                    }
                });

                backButton.setOnClickListener(new View.OnClickListener() {      //OnClickListener for the 'back' button
                    @Override
                    public void onClick(View v) {           //Uses intents to navigate back to main page
                        Intent intent = new Intent(LocationDetailsActivity.this, MainActivity.class);
                        startActivity(intent);      //Starts intent
                    }
                });
            }
        }
    }
}