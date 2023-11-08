package com.example.mobiledevassign2;

import androidx.appcompat.app.AppCompatActivity;            //Imports libraries
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button addnewLocButton;                             //Defines buttons, recyclerview, adapter, db, and array list
    RecyclerView recyclerView;
    LocationAdapter adapter;
    DBHandler db;
    ArrayList<Location> locArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHandler(this);       //Creates a new db object
        locArrayList = db.readLocations();      //Uses the .readLocations() method from DBHandler to read locations to array list

        recyclerView = findViewById(R.id.locateView);       //Finds recyclerview by id
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LocationAdapter(locArrayList,this);       //New LocationAdapter object
        recyclerView.setAdapter(adapter);   //Sets the adapter

        SearchView searchView = findViewById(R.id.searchView);     //Finds search view by id
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){         //When the user submits a text query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){       //When the search view changes
                ArrayList<Location> filtLocList = new ArrayList<>();    //New array list
                for (int i = 0; i < locArrayList.size(); i++){      //Iterating through array lists
                    Location location = locArrayList.get(i);
                    if (location.getAddress().toLowerCase().contains(newText.toLowerCase())){   //Checks if the lowercase address contains the new text in lowercase
                        filtLocList.add(location);      //If it does, location is added to the filtered location list
                    }
                }

                adapter.updateList(filtLocList);        //Updates the list
                return false;
            }
        });

        try {
            InputStream inputstrm = getAssets().open("locations.txt");      //Uses input stream to open the locations.txt file
            BufferedReader buff = new BufferedReader(new InputStreamReader(inputstrm));     //New buffered reader object
            String input_line;

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());        //Geocoder object from the geocoder class
            while((input_line = buff.readLine()) != null){      //While the line isn't empty (end of file)
                String[] arr = input_line.split(",");       //Splits it based on the comma (',') and is stored in an array
                double latitude, longitude;
                latitude = Double.parseDouble(arr[0]);          //Parses the latitude
                longitude = Double.parseDouble(arr[1]);         //Parses longitude

                try {       /*Reverse geocoding */
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);       //uses .getFromLocation method to get addresses
                    if (addressList != null && !addressList.isEmpty()){     //If the list isn't null or empty
                        String addr = addressList.get(0).getAddressLine(0);     //Gets the first object
                        db.addNewLocation(addr, latitude, longitude);   //Calls new .addNewLocation() method to add to the database
                    }
                } catch (IOException e){    //Catches exceptions and prints the stack trace
                    e.printStackTrace();
                }
            }

            buff.close();               //Closes buffer
        } catch (IOException e){        //Catches exceptions and prints the stack trace
            e.printStackTrace();
        }

        addnewLocButton = findViewById(R.id.addnewbtn);     //Find the add new location button by id

        addnewLocButton.setOnClickListener(new View.OnClickListener() {     //OnClickListener for add new location button
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewLocActivity.class);    //Intent to navigate to the new location activity
                startActivity(intent);      //Starts intent
            }
        });
    }
    @Override
    protected void onResume(){      //onResume() method
        super.onResume();

        locArrayList.clear();
        locArrayList.addAll(db.readLocations());
        adapter.notifyDataSetChanged();         //Notifies the adapter that the data has changed
    }
}