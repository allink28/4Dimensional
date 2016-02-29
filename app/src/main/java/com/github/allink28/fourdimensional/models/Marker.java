package com.github.allink28.fourdimensional.models;

import java.io.Serializable;
import java.util.Calendar;

public class Marker implements Serializable {

    private static final long serialVersionUID = 1L;
    private Calendar time;
    private long latitude;
    private long longitude;
    private long altitude;

    public Marker(boolean markTime) {
        if (markTime) {
            time = Calendar.getInstance();
        }
    }

    public Marker(boolean markTime, long lat, long lon, long alt) {
        this(markTime);
        latitude = lat;
        longitude = lon;
        altitude = alt;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getAltitude() {
        return altitude;
    }

    public void setAltitude(long altitude) {
        this.altitude = altitude;
    }


}
