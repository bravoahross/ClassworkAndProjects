package com.example.as1;

/**
 * Class that holds all of a stations information so it can be used in the RecycleView.
 * @author Alex Brown
 */
public class StationModal {
    //Variables
    private String stationName;
    private long stationID;
    private String stationPrice;
    private String stationAddress;
    private String stationDescription;

    //Constructor

    /**
     * Take in all of the information and make a new object holding that stuff.
     * @param stationName
     * @param stationID
     * @param stationPrice
     * @param stationAddress
     * @param stationDescription
     */
    public StationModal(String stationName,long stationID, String stationPrice, String stationAddress, String stationDescription){
        this.stationName = stationName;
        this.stationID = stationID;
        this.stationPrice = stationPrice;
        this.stationAddress = stationAddress;
        this.stationDescription = stationDescription;
    }

    //Gets and Sets
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationPrice() {
        return stationPrice;
    }

    public void setStationPrice(String stationPrice) {
        this.stationPrice = stationPrice;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public String getStationDescription() {
        return stationDescription;
    }

    public void setStationDescription(String stationDescription) {
        this.stationDescription = stationDescription;
    }

    public long getStationID(){
        return stationID;
    }

    public void setStationID(long stationID){
        this.stationID = stationID;
    }

}
