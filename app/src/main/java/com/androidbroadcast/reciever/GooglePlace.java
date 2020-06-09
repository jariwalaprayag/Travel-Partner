package com.androidbroadcast.reciever;

public class GooglePlace {
    private String name;
    private String category;
    private String rating;
    private String open;
    private String placeId;
    private String formatted_address;
    private String photoRefId;
    private String longitude;
    private String latitude;


    public GooglePlace() {
        this.name = "";
        this.formatted_address = "";
        this.rating = "";
        this.photoRefId = "";
        this.open = "";
        this.setCategory("");
        this.placeId = "";
        this.longitude = "";
        this.latitude = "";
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getPhotoRefId() {
        return photoRefId;
    }

    public void setPhotoRefId(String photoRefId) {
        this.photoRefId = photoRefId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {

        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setOpenNow(String open) {
        this.open = open;
    }

    public String getOpenNow() {
        return open;
    }


}
