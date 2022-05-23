package com.penelope.seatforyou.data.editor.assets;

import android.graphics.PointF;

public class Circle extends Figure{

    private float radius;

    public Circle(PointF spawnPoint, float radius) {
        super(spawnPoint);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }


}
