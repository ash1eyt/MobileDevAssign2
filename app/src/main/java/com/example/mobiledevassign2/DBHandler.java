package com.example.mobiledevassign2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    //Variables for the database name, version, table name, and columns
    private static final String DB_NAME = "locationdb";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "locationtable";
    private static final String ID_COL = "id";
    private static final String ADDRESS_COL = "address";
    private static final String LATITUDE_COL = "latitude";
    private static final String LONGITUDE_COL = "longitude";

    public DBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Creates a table with the 4 columns for id, address, latitude, longitude

    @Override
    public void onCreate(SQLiteDatabase db){
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ADDRESS_COL + " TEXT,"
                + LATITUDE_COL + " REAL,"
                + LONGITUDE_COL + " REAL)";

        db.execSQL(query);
    }

    //Method to add a new location and the values of address, latitude, longitude are put into their respective columns in the db

    public void addNewLocation(String address, double latitude, double longitude){
        SQLiteDatabase db = this.getWritableDatabase();                 //Writeable database
        //Added
        Cursor cursor = db.query(TABLE_NAME,                //Cursor object
                new String[] {ADDRESS_COL},
                ADDRESS_COL + " =?",                //Queries the address column for any duplicate addresses
                new String[] {address},
                null,
                null,
                null);

        if (!cursor.moveToFirst()) {                        //If the cursor is empty, add the values
            ContentValues values = new ContentValues();

            values.put(ADDRESS_COL, address);               //Put the values into the columns
            values.put(LATITUDE_COL, latitude);
            values.put(LONGITUDE_COL, longitude);

            db.insert(TABLE_NAME, null, values);        //Insert method to insert it into the table

        }
        cursor.close();             //Closes cursor and db
        db.close();
    }

    //Method to read the locations, uses an ArrayList and a cursor object
    public ArrayList<Location> readLocations(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);       //Raw query to select all rows from the table
        ArrayList<Location> locArrayList = new ArrayList<>();       //New ArrayList object

        if(cursor.moveToFirst()){       //Moves cursor to first row, makes sure there's at least one row
            do {
                Location location = new Location(cursor.getString(1),       //Gets the data from the columns
                        cursor.getDouble(2), cursor.getDouble(3));
                location.setId(cursor.getInt(0));       //Sets id for location
                locArrayList.add(location);     //Adds location to array list
            } while (cursor.moveToNext());  //While it moves to the next
        }
        cursor.close();                 //Closes cursor
        return locArrayList;    //Returns array list

    }

    //Method to get the location
    public Location getLocation(int id){
        SQLiteDatabase db = this.getReadableDatabase();     //Readable database
        Cursor cursor = db.query(TABLE_NAME, new String[]{ID_COL, ADDRESS_COL, LATITUDE_COL, LONGITUDE_COL}, ID_COL + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
       //Checks if cursor isn't null and moves it to first row
        if (cursor != null && cursor.moveToFirst()){
            //Uses cursor to get the data from the columns which are based on the column index
            Location location = new Location(cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3));
            location.setId(Integer.parseInt(cursor.getString(0)));      //Sets id of location

            cursor.close();
            return location;
        } else {                //If cursor is null, log a message
            Log.d("DBHandler", "No location of " + id + "found");
            return null;
        }
    }

    //Method to update the location, has parameters for id, address, latitude, longitude
    public void updateLocation(int id, String address, double latitude, double longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ADDRESS_COL, address);       //Puts values into their columns
        values.put(LATITUDE_COL, latitude);
        values.put(LONGITUDE_COL, longitude);

        //Uses update method to update the values
        db.update(TABLE_NAME, values, ID_COL + " =?", new String[]{String.valueOf(id)});
        db.close();
    }

    //Method to delete a location, uses the .getWriteableDatabase() method
    public void deleteLocation(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_COL + " =?", new String[] {String.valueOf(id)});
        db.close();
    }

    //onUpgrade method
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
