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
 * Adapter for list of reviews from a gas station. Implements useful methods to connect and alter
 * a list of reviews
 * @author Noah Ross
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<ReviewModal> reviewModalArrayList;
    private Context context;

    // creating a constructor for our variables.
    public ReviewAdapter(ArrayList<ReviewModal> reviewModalArrayList, Context context) {
        this.reviewModalArrayList = reviewModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_rv_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder holder, int position) {
        // setting data to our views of recycler view.
        ReviewModal modal = reviewModalArrayList.get(position);
        holder.rating.setText(modal.getRating().toString());
        holder.description.setText(modal.getDescription());
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return reviewModalArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private TextView rating, description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our views with their ids.
            rating = itemView.findViewById(R.id.idRating);
            description = itemView.findViewById(R.id.idDescription);
        }
    }
}