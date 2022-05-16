package com.penelope.seatforyou.ui.editor.listener;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.View;

/**
 * 물체를 드래그앤 드랍으로 배치하기 위한
 * 긴 터치 이벤트를 처리하는 리스너
 */
public class LongClickListener implements View.OnLongClickListener {
    @Override
    public boolean onLongClick(View view) {

        // 태그 생성
        ClipData.Item item = new ClipData.Item(
                (CharSequence) view.getTag());

        String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
        ClipData data = new ClipData(view.getTag().toString(),
                mimeTypes, item);


        view.startDragAndDrop(data, // data to be dragged
                null, // drag shadow
                view, // 드래그 드랍할  Vew
                0 // 필요없은 플래그
        );

//        view.setVisibility(View.INVISIBLE);
        return true;
    }
}
