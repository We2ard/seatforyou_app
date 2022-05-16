package com.penelope.seatforyou.data.editor.assets;

/**
 * 배치 도형들의 공통기능을 정의한 인터페이스
 */
public interface AssetData {

    // create
    void crate();
    // delete
    void delete();
    // move
    void move(float x, float y, int listIndex);
}
