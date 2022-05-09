package com.penelope.seatforyou.data.reservation;

import java.io.Serializable;
import java.util.Objects;

public class Reservation implements Serializable {
    
    private String id;
    private String uid;
    private String shopId;
    private int personNumber;
    private int year;
    private int month;
    private int dayOfMonth;
    private int hour;
    private int minute;
    private long created;

    public Reservation() {
    }

    public Reservation(String uid, String shopId, int personNumber,
                       int year, int month, int dayOfMonth, int hour, int minute) {
        this.uid = uid;
        this.shopId = shopId;
        this.personNumber = personNumber;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.hour = hour;
        this.minute = minute;
        this.created = System.currentTimeMillis();
        this.id = uid + "_" + shopId + "_" + created;
    }

    public String getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getShopId() {
        return shopId;
    }

    public int getPersonNumber() {
        return personNumber;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public long getCreated() {
        return created;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setPersonNumber(int personNumber) {
        this.personNumber = personNumber;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return personNumber == that.personNumber && year == that.year && month == that.month && dayOfMonth == that.dayOfMonth && hour == that.hour && minute == that.minute && created == that.created && Objects.equals(id, that.id) && Objects.equals(uid, that.uid) && Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, shopId, personNumber, year, month, dayOfMonth, hour, minute, created);
    }
}
