package com.penelope.seatforyou.data.editor.assets;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * 임의 개수의 꼭지점을 가진 다각형을 표현하는 클래스
 * 생성위치(중심점)과 다각형을 이루는 꼭지점들의 리스트를 받아서 생성됨
 */
public class Polygon extends Figure{

    private List<PointF> points = new ArrayList<>();
    protected double rotationAngle = 0;

    public Polygon(PointF spawnPoint) {
        super(spawnPoint);
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle % 360;
    }

    public double getArea(){
        float total = 0;
        for (int i = 0; i < points.size(); i++) {
            float addX = points.get(i).x;
            float addY = points.get(i == points.size() - 1 ? 0 : i + 1).y;
            float subX = points.get(i == points.size() - 1 ? 0 : i + 1).x;
            float subY = points.get(i).y;
            total += (addX * addY * 0.5);
            total -= (subX * subY * 0.5);
        }
        return Math.abs(total);
    }

    public List<PointF> getPoints() {
        return points;
    }

    public void setPoints(List<PointF> points) {
        this.points = points;
    }

    public PointF getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(PointF spawnPoint) {
        this.spawnPoint = spawnPoint;
    }
}
