package com.penelope.seatforyou.ui.editor.listener;

import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.penelope.seatforyou.R;

/**
 * 드래그앤 드롭 이벤트 리스너
 */
public class DragListener implements View.OnDragListener {
    @Override
    public boolean onDrag(View v, DragEvent event) {
        // 이벤트 시작
        switch (event.getAction()) {

            // 이미지를 드래그 시작될때
            case DragEvent.ACTION_DRAG_STARTED:
                Log.d("DragClickListener", "ACTION_DRAG_STARTED");
                break;

            // 드래그한 이미지를 옮길려는 지역으로 들어왔을때
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d("DragClickListener", "ACTION_DRAG_ENTERED");
                // 이미지가 들어왔다는 것을 알려주기 위해 배경이미지 변경
                break;

            // 드래그한 이미지가 영역을 빠져 나갈때
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d("DragClickListener", "ACTION_DRAG_EXITED");
                break;

//            // 이미지를 드래그해서 드랍시켰을때
//            case DragEvent.ACTION_DROP:
//                Log.d("DragClickListener", "ACTION_DROP");
//
//                if (v == v.findViewById(R.id.bottomlinear)) {
//                    View view = (View) event.getLocalState();
//                    ViewGroup viewgroup = (ViewGroup) view
//                            .getParent();
//                    viewgroup.removeView(view);
//
//                    // change the text
//                    TextView text = (TextView) v
//                            .findViewById(R.id.text);
//                    text.setText("이미지가 드랍되었습니다.");
//
//                    LinearLayout containView = (LinearLayout) v;
//                    containView.addView(view);
//                    view.setVisibility(View.VISIBLE);
//
//                }else if (v == findViewById(R.id.toplinear)) {
//                    View view = (View) event.getLocalState();
//                    ViewGroup viewgroup = (ViewGroup) view
//                            .getParent();
//                    viewgroup.removeView(view);
//
//                    LinearLayout containView = (LinearLayout) v;
//                    containView.addView(view);
//                    view.setVisibility(View.VISIBLE);
//
//                }else {
//                    View view = (View) event.getLocalState();
//                    view.setVisibility(View.VISIBLE);
//                    Context context = getApplicationContext();
//                    Toast.makeText(context,
//                            "이미지를 다른 지역에 드랍할수 없습니다.",
//                            Toast.LENGTH_LONG).show();
//                    break;
//                }
//                break;

            case DragEvent.ACTION_DRAG_ENDED:
                Log.d("DragClickListener", "ACTION_DRAG_ENDED");

            default:
                break;
        }
        return true;
    }
}
