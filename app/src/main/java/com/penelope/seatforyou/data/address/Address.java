package com.penelope.seatforyou.data.address;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {

    private String loadAddress;
    private String detail;
    private double latitude;
    private double longitude;


    public Address() {
    }

    public Address(String loadAddress, String detail, double latitude, double longitude) {
        this.loadAddress = loadAddress;
        this.detail = detail;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLoadAddress() {
        return loadAddress;
    }

    public String getDetail() {
        return detail;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLoadAddress(String loadAddress) {
        this.loadAddress = loadAddress;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Double.compare(address.latitude, latitude) == 0 && Double.compare(address.longitude, longitude) == 0 && loadAddress.equals(address.loadAddress) && detail.equals(address.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loadAddress, detail, latitude, longitude);
    }

    @Override
    public String toString() {
        return "Address{" +
                "loadAddress='" + loadAddress + '\'' +
                ", detail='" + detail + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
