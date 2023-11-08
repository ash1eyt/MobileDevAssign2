package com.example.mobiledevassign2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;             //Imports libraries

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private ArrayList<Location> locArrayList;       //ArrayList
    private Context context;        //Context object

    public LocationAdapter(ArrayList<Location> locArrayList, Context context){      //Constructor for LocationAdapter
        this.locArrayList = locArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){    //Method that is connected to the layout which displays how each pinned location will look
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pinnedlocations, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Location location = locArrayList.get(position);     //Gets position of the array list
        holder.addressTV.setText(location.getAddress());    //Gets address and sets the text
        holder.latitudeTV.setText(String.valueOf(location.getLatitude()));      //Gets latitude and sets the text
        holder.longitudeTV.setText(String.valueOf(location.getLongitude()));    //Gets longitude and sets the text
    }

    public int getItemCount(){
        return locArrayList.size();
    }       //Gets the item count and returns the size of the array list

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView addressTV, latitudeTV, longitudeTV;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
                                                                            //Finds text views by id
            addressTV = itemView.findViewById(R.id.pinned_address);
            latitudeTV = itemView.findViewById(R.id.pinned_latitude);
            longitudeTV = itemView.findViewById(R.id.pinned_longitude);

            itemView.setOnClickListener(new View.OnClickListener() {        //OnClickListener so the pinned locations are clickable
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();        //Gets adapter position
                    if (position != RecyclerView.NO_POSITION){
                        Location location = locArrayList.get(position);     //Gets position
                        Intent intent = new Intent(context, LocationDetailsActivity.class);
                        intent.putExtra("locationId", location.getId());        //Puts an intent with the name locationId, gets the id of location
                        context.startActivity(intent);  //Starts intent
                    }
                }
            });
        }
    }

    public void updateList(ArrayList<Location> updatelist){             //Method to updated the list
        locArrayList = updatelist;
        notifyDataSetChanged();                             //Method to notify when the data has changed
    }
}
