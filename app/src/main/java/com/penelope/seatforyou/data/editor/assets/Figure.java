package com.penelope.seatforyou.data.editor.assets;

import android.graphics.PointF;

/**
 * 다각형, 원형을 포함한 모든 도형을 추상화한 클래스
 */
public class Figure {

    public PointF spawnPoint;

    public Figure(PointF spawnPoint) {
        this.spawnPoint = spawnPoint;
    }
}
