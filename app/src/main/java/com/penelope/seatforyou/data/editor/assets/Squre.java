package com.penelope.seatforyou.data.editor.assets;

import android.content.Context;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

public class Squre extends Polygon {

    public Squre(PointF spawnPoint, float width, float height) {
        super(spawnPoint);
        List<PointF> points = new ArrayList<>();
        float halfX = width / 2;
        float halfY = height / 2;

        points.add(new PointF(spawnPoint.x - halfX, spawnPoint.y - halfY)); // 좌상
        points.add(new PointF(spawnPoint.x + halfX, spawnPoint.y - halfY)); // 우상
        points.add(new PointF(spawnPoint.x + halfX, spawnPoint.y + halfY)); // 우하
        points.add(new PointF(spawnPoint.x - halfX, spawnPoint.y + halfY)); // 좌하

        setPoints(points);
    }
}
