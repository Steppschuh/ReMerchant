package com.steppschuh.remerchant;

import android.graphics.drawable.Drawable;

import java.util.Comparator;
import java.util.Date;

public class Customer {

    public static final int RECENT_CUSTOMER_TIMEOUT = 1200000;

    private long id;
    private String name;
    private Drawable picture;
    private int loyality;
    private String deviceName;
    private String deviceId;
    private long lastVisit;
    private short rssi;

    public Customer(long id) {
        this.id = id;
    }

    public String getDevice() {
        return getDevice(deviceId, deviceName);
    }

    public static String getDevice(String deviceIdCur, String deviceNameCur) {
        if (deviceIdCur != null) {
            return deviceIdCur;
        } else if (deviceNameCur != null) {
            return getDeviceIdFromName(deviceNameCur);
        } else {
            return null;
        }
    }

    public static String getDeviceIdFromName(String deviceNameCur) {
        if (deviceNameCur.indexOf(" ") > 0) {
            return deviceNameCur.substring(deviceNameCur.lastIndexOf(" ") + 1);
        } else {
            return deviceNameCur;
        }
    }

    public boolean recentlySeen() {
        if (lastVisit + RECENT_CUSTOMER_TIMEOUT < (new Date()).getTime()) {
            return false;
        } else {
            return true;
        }
    }

    public String getLastSeenString() {
        long delta = getLastSeenDelta();
        if (delta > 60 * 60 * 24 * 7) {
            return "over a week ago";
        } else if (delta > 60 * 60 * 24) {
            return "a few days ago";
        } else if (delta > 60 * 60) {
            return "a few hours ago";
        } else if (delta > 60 * 5) {
            return "a few minutes ago";
        } else if (delta >= 30) {
            return "a few seconds ago";
        } else {
            return "just now";
        }
    }

    public static class LastSeenComparator implements Comparator<Customer> {
        @Override
        public int compare(Customer o1, Customer o2) {
            return (int) (o1.getLastVisit() - (o2.getLastVisit()));
        }
    }

    public long getLastSeenDelta() {
        return ((new Date()).getTime() - lastVisit) / 1000;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getPicture() {
        return picture;
    }

    public void setPicture(Drawable picture) {
        this.picture = picture;
    }

    public int getLoyality() {
        return loyality;
    }

    public void setLoyality(int loyality) {
        this.loyality = loyality;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(long lastVisit) {
        this.lastVisit = lastVisit;
    }

    public short getRssi() {
        return rssi;
    }

    public void setRssi(short rssi) {
        this.rssi = rssi;
    }
}

