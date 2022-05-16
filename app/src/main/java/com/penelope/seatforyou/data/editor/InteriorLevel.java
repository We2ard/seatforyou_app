package com.penelope.seatforyou.data.editor;

import androidx.constraintlayout.widget.ConstraintSet;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.ActivityEditorBinding;
import com.penelope.seatforyou.ui.editor.EditorActivity;
import com.penelope.seatforyou.ui.editor.draw.CanvasView;

/**
 * 가게 각 층의 정보를 저장하는 함수
 */
public class InteriorLevel {
    private String levelName;
    private static int levelIdCount = 0;
    private final int levelId;
    private long fieldWidth = 0;
    private long fieldHeight = 0;
    private CanvasView canvasView;
    private ActivityEditorBinding binding;

    public InteriorLevel(String levelName) {
        this.levelName = levelName;
        this.canvasView = new CanvasView(EditorActivity.context);
        levelId = levelIdCount++;
    }

    // 현재 층에 있는 모든 요소를 그리는 함수
    public void drawAll(){
    }

    public int getLevelId() {
        return levelId;
    }

    public long getArea(){
        return fieldWidth * fieldHeight;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public CanvasView getCanvasView() {
        return canvasView;
    }
}
