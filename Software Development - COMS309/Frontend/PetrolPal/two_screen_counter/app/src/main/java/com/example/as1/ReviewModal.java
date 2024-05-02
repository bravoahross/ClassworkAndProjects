package com.example.as1;

/**
 * Modal for displaying helpful information from ratings and reviews
 * Double rating: 0-5
 * String description: overview of the user's opinion
 * @author Noah Ross
 */
public class ReviewModal {
    //Variables
    String description;
    Double rating;


    //Constructor
    public ReviewModal(String description, Double rating) {
        this.description = description;
        this.rating = rating;
    }


    public String getDescription() {
        return description;
    }

    public Double getRating() {
        return rating;
    }
}
