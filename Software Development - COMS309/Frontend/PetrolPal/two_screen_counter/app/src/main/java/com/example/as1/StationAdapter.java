package com.example.as1;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

/**
 * Class for holding the items that are going into the recycle view. An arraylist of StationModal type that has its own functions.
 * @author Alex Brown
 */
public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<StationModal> stationModalArrayList;
    private Context context;

    private final RecyclerViewInterface recyclerViewInterface;

    // creating a constructor for our variables.
    public StationAdapter(ArrayList<StationModal> stationModalArrayList, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.stationModalArrayList = stationModalArrayList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public StationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_rv_item, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull StationAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        StationModal modal = stationModalArrayList.get(position);
        holder.stationNameTV.setText(modal.getStationName());
        holder.stationAddressTV.setText(modal.getStationAddress());
        //holder.stationDescriptionTV.setText(modal.getStationDescription());
        holder.stationPriceTV.setText(modal.getStationPrice());
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return stationModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private TextView stationNameTV, stationAddressTV, stationDescriptionTV, stationPriceTV;


        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            // initializing our views with their ids.
            stationNameTV = itemView.findViewById(R.id.idReportedPrice);
            stationAddressTV = itemView.findViewById(R.id.idTime);
            //stationDescriptionTV = itemView.findViewById(R.id.idDescription);
            stationPriceTV = itemView.findViewById(R.id.idUser);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}