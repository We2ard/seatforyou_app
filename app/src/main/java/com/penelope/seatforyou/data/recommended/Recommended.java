package com.penelope.seatforyou.data.recommended;

import java.util.List;

public class Recommended {

    private int updatedDay;
    private List<String> items;

    public Recommended() {
    }

    public Recommended(int updatedDay, List<String> items) {
        this.updatedDay = updatedDay;
        this.items = items;
    }

    public int getUpdatedDay() {
        return updatedDay;
    }

    public List<String> getItems() {
        return items;
    }

    public void setUpdatedDay(int updatedDay) {
        this.updatedDay = updatedDay;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
