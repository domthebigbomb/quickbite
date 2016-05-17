package com.cmsc436.quickbite;

/**
 * Created by dominic on 5/17/16.
 */
public class CheckIn {
    private long waitTime;
    private String placeName;
    private String address;

    public CheckIn() {}

    public CheckIn(long waitTime, String placeName, String address) {
        this.waitTime = waitTime;
        this.placeName = placeName;
        this.address = address;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getAddress() {
        return address;
    }
}
