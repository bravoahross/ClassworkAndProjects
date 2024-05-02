package com.example.as1;

/**
 * Class that holds all of a price report information so it can be used in the RecycleView.
 * @author Alex Brown
 */
public class ReportModal {
    //Variables
    private String username;
    private String stationPrice;
    private String time;

    //Constructor

    /**
     * Take in all of the information and make a new object holding that stuff.
     * @param username
     * @param stationPrice
     * @param time
     */
    public ReportModal(String username, String stationPrice, String time){
        this.username = username;
        this.stationPrice = stationPrice;
        this.time = time;
    }

    //Gets and Sets
    public String getUsername() {
        return username;
    }

    public void setStationName(String username) {
        this.username = username;
    }

    public String getStationPrice() {
        return stationPrice;
    }

    public void setStationPrice(String stationPrice) {
        this.stationPrice = stationPrice;
    }

    public String getTime() {
        return time;

    }

    public void setTime(String time) {
        this.time = time;
    }


}
