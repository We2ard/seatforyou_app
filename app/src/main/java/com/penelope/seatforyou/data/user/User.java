package com.penelope.seatforyou.data.user;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String uid;
    private String phone;
    private String name;
    private String password;
    private boolean isCustomer;
    private long created;
    private List<String> dibs;

    public User() {
    }

    public User(String uid, String phone, String name, String password, boolean isCustomer) {
        this.uid = uid;
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.isCustomer = isCustomer;
        this.created = System.currentTimeMillis();
        this.dibs = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean isCustomer() {
        return isCustomer;
    }

    public long getCreated() {
        return created;
    }

    public List<String> getDibs() {
        return dibs;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCustomer(boolean customer) {
        isCustomer = customer;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void setDibs(List<String> dibs) {
        this.dibs = dibs;
    }

}
