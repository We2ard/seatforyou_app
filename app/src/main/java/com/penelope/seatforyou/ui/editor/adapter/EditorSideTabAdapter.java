package com.penelope.seatforyou.ui.editor.adapter;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.utils.TimeUtils;

import java.util.List;

/**
 * 에디터 우측 사이드 탭의 요소를 생성하기 위한 어댑터 클래스
 */
public class EditorSideTabAdapter extends RecyclerView.Adapter<EditorSideTabAdapter.ViewHolder> {

    private List<SideTabData> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.tv_sidetab_element);
            imageView = (ImageView) view.findViewById(R.id.iv_sidetab_element);
        }

        public void setData(SideTabData data) {
            textView.setText(data.text);
            imageView.setImageResource(data.imageId);
        }

    }

    public EditorSideTabAdapter(List<SideTabData> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    // viewholder 생성
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_editor_sidebar, viewGroup, false);

        return new ViewHolder(view);
    }

    // viewholder 에 데이터 세팅
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.setData(localDataSet.get(position));
        RecyclerView sideBar = viewHolder.itemView.findViewById(R.id.editor_sidebar);

        // 사이드바 리스너 부착
        viewHolder.itemView.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            float curX = event.getX();
            float curY = event.getY();

            Vibrator vibrator = (Vibrator) viewHolder.itemView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            switch (action) {
                case MotionEvent.ACTION_DOWN:   // 화면에 손가락을 눌렀을 시점의 동작
                    vibrator.vibrate(TimeUtils.VIBE_SHORT);
                    Toast.makeText(v.getContext(), position + "번 요소", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_UP: // TODO:손가락을 땔 때 - 물체객체를 생성하고 배치해야함
                    // 드래그 할 때 위아래로 조금만 움직여도 손가락 때는 동작을 감지 못함
                    if(curX < 0){
                        Toast.makeText(v.getContext(), position + "번 요소 배치", Toast.LENGTH_SHORT).show();
                        vibrator.vibrate(TimeUtils.VIBE_SHORT);
                    }
                    break;
//                case MotionEvent.ACTION_MOVE:
//                    if(!v.isPressed()){
//                        Toast.makeText(v.getContext(), position + "번 요소 배치", Toast.LENGTH_SHORT).show();
//                        vibrator.vibrate(CommonUtil.VIBE_SHORT);
//                    }
                default:
                    v.performClick();
                    break;
            }
            return true;
        });



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}