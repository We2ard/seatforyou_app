package com.penelope.seatforyou.ui.editor.adapter;

import android.app.LauncherActivity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.ui.editor.EditorActivity;
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

        // 아이템별 드래그앤 드랍 구현
        ViewHolder viewHolder = new ViewHolder(view);
        View shape = viewHolder.imageView;

        viewHolder.itemView.setOnLongClickListener(v -> {
            Toast.makeText(v.getContext(), "드래그 시작", Toast.LENGTH_SHORT).show();
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(shape);
            ViewCompat.startDragAndDrop(shape, null, shadowBuilder, null, 0);
            return true;
        });
        return viewHolder;
    }

    // viewholder 에 데이터 세팅
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.setData(localDataSet.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}