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
 * Class for holding the items that are going into the recycle view. An arraylist of ReportModal type that has its own functions.
 * @author Alex Brown
 */
public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<ReportModal> reportModalArrayList;
    private Context context;

    private final RecyclerViewInterface recyclerViewInterface;

    // creating a constructor for our variables.
    public ReportAdapter(ArrayList<ReportModal> reportModalArrayList, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.reportModalArrayList = reportModalArrayList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_rv_item, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        ReportModal modal = reportModalArrayList.get(position);
        holder.usernameTV.setText(modal.getUsername());
        holder.timeTV.setText(modal.getTime());
        //holder.stationDescriptionTV.setText(modal.getStationDescription());
        holder.stationPriceTV.setText(modal.getStationPrice());
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return reportModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private TextView usernameTV, timeTV, stationPriceTV;


        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            // initializing our views with their ids.
            usernameTV = itemView.findViewById(R.id.idUser);
            timeTV = itemView.findViewById(R.id.idTime);
            stationPriceTV = itemView.findViewById(R.id.idReportedPrice);

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