package com.penelope.seatforyou.data.shop;

import com.penelope.seatforyou.data.address.Address;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Shop implements Serializable {

    private String uid;
    private String name;
    private String description;
    private Map<String, Integer> menus;
    private Address address;
    private String phone;
    private List<String> categories;
    private int openHour;
    private int openMinute;
    private int closeHour;
    private int closeMinute;

    public Shop() {
    }

    public Shop(String uid, String name, String description, Map<String, Integer> menus, Address address, String phone, List<String> categories, int openHour, int openMinute, int closeHour, int closeMinute) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.menus = menus;
        this.address = address;
        this.phone = phone;
        this.categories = categories;
        this.openHour = openHour;
        this.openMinute = openMinute;
        this.closeHour = closeHour;
        this.closeMinute = closeMinute;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Integer> getMenus() {
        return menus;
    }

    public Address getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getCategories() {
        return categories;
    }

    public int getOpenHour() {
        return openHour;
    }

    public int getOpenMinute() {
        return openMinute;
    }

    public int getCloseHour() {
        return closeHour;
    }

    public int getCloseMinute() {
        return closeMinute;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMenus(Map<String, Integer> menus) {
        this.menus = menus;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setOpenHour(int openHour) {
        this.openHour = openHour;
    }

    public void setOpenMinute(int openMinute) {
        this.openMinute = openMinute;
    }

    public void setCloseHour(int closeHour) {
        this.closeHour = closeHour;
    }

    public void setCloseMinute(int closeMinute) {
        this.closeMinute = closeMinute;
    }

}
