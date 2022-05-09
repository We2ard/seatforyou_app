package com.penelope.seatforyou.data.shop;

import com.penelope.seatforyou.data.address.Address;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private long created;


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
        this.created = System.currentTimeMillis();
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

    public long getCreated() {
        return created;
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

    public void setCreated(long created) {
        this.created = created;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shop shop = (Shop) o;
        return openHour == shop.openHour && openMinute == shop.openMinute && closeHour == shop.closeHour && closeMinute == shop.closeMinute && created == shop.created && uid.equals(shop.uid) && name.equals(shop.name) && description.equals(shop.description) && menus.equals(shop.menus) && address.equals(shop.address) && phone.equals(shop.phone) && categories.equals(shop.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, description, menus, address, phone, categories, openHour, openMinute, closeHour, closeMinute, created);
    }

    @Override
    public String toString() {
        return "Shop{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", menus=" + menus +
                ", address=" + address +
                ", phone='" + phone + '\'' +
                ", categories=" + categories +
                ", openHour=" + openHour +
                ", openMinute=" + openMinute +
                ", closeHour=" + closeHour +
                ", closeMinute=" + closeMinute +
                ", created=" + created +
                '}';
    }
}
