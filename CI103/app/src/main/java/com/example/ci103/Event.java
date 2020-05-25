package com.example.ci103;

import java.io.Serializable;

public class Event implements Serializable {
    private  String id;
    private String event_name;
    private String address;
    private String date;
    private String time;
    private String description;
    private String host_id;
    private String going,type;
    private double latitude,longitude;

    public String getId() {
        return id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }

    public String getHost_id() {
        return host_id;
    }

    public String getGoing() {
        return going;
    }

    public void setGoing(String going) {
        this.going = going;
    }

    @Override
    public String toString() {
        return id+"  "+this.getEvent_name();
    }

    public Event() {
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Event(String id, String event_name, String address, String date, String time, String description, String host_id, String type, Double latitude,Double longitude) {
        this.id = id;
        this.event_name = event_name;
        this.address = address;
        this.date = date;
        this.time = time;
        this.description = description;
        this.host_id=host_id;
        this.going="";
        this.type=type;
        this.latitude=latitude;
        this.longitude=longitude;
    }
}
