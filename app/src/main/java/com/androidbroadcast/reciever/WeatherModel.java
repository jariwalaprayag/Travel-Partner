package com.androidbroadcast.reciever;


public class WeatherModel {
    private String description;
    private String temperature;
    private String wind;

    public WeatherModel() {
        this.description = "";
        this.temperature = "";
        this.wind = "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
