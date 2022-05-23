package com.penelope.seatforyou.ui.editor.adapter;

/**
 * 에디터 사이드탭에 있는 한 항목의 데이터를 저장하는 클래스
 */
public class SideTabData {
    int imageId;    // 이미지 id
    String text;    // 이미지 이름
    boolean selected = false;

    public SideTabData(int imageId, String text) {
        this.imageId = imageId;
        this.text = text;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}