package com.example.mobiledevassign2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class NewLocActivity extends AppCompatActivity {

    private DBHandler db;               //Defines variables
    private EditText addressET;
    private TextView latitudeTV, longitudeTV;
    private Button addButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_loc);

        db = new DBHandler(this);       //DBHandler object

        addressET = findViewById(R.id.address_input);       //Finds edittexts and textviews by id
        latitudeTV = findViewById(R.id.latitude_new);
        longitudeTV = findViewById(R.id.longitude_new);

        addButton = findViewById(R.id.add_button);          //Finds buttons by id
        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {      //SetOnClickListener for 'back' button
            @Override
            public void onClick(View v) {           //Uses intents to go back to main page
                Intent intent = new Intent(NewLocActivity.this, MainActivity.class);
                startActivity(intent);      //Starts intents
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {       //Set on click listener for add button
            @Override
            public void onClick(View v) {
                String addr = addressET.getText().toString();       //Gets the edit text and puts the values to string

                if (!addr.isEmpty()){
                    Geocoder geocoder = new Geocoder(NewLocActivity.this);      //Geocoder object
                    try{
                        List<Address> addressList = geocoder.getFromLocationName(addr, 1);
                        if (addr != null && !addressList.isEmpty()){        //If the address isn't null and the list isn't empty
                            Address locationAddr = addressList.get(0);
                            double latitude = locationAddr.getLatitude();       //Gets latitude
                            double longitude = locationAddr.getLongitude();     //Gets longitude

                            latitudeTV.setText(String.valueOf(latitude));           //Sets latitude
                            longitudeTV.setText(String.valueOf(longitude));     //Sets longitude

                            db.addNewLocation(addr, latitude, longitude);       //Calls .addNewLocation() method from DBHandler to add new location

                            Toast.makeText(NewLocActivity.this, "Location has been added!", Toast.LENGTH_SHORT).show();     //Toast if location has been added

                        } else {                                                //Toast if the address user put in was not found
                            Toast.makeText(NewLocActivity.this, "No address found.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e){                            //If the geocoding was not successfully completed
                        Toast.makeText(NewLocActivity.this, "Reverse geocoding was not able to be performed.", Toast.LENGTH_SHORT).show();
                    }
                } else {                                            //If the user leaves the address empty
                    Toast.makeText(NewLocActivity.this, "Please enter a value for address.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}