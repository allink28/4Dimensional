package com.github.allink28.fourdimensional.models;

import android.location.Location;

/**
 * A trip object is basically just a start and endpoint, but they can both be
 * null if the GPS is off so start time and end time are separated out.
 * Created on 2/28/2016.
 * @author Allen
 */
public class Trip {
    private long startTime, endTime = 0;
    private Location startLocation, endLocation;

    public Trip(Location currentLocation) {
        startTime = System.currentTimeMillis();
        startLocation = currentLocation;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }
}
