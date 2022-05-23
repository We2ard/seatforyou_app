package com.penelope.seatforyou.ui.editor.adapter;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private boolean selected;
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
            selected = data.selected;
        }
    }

    private static List<SideTabData> localDataSet;
    private Drawable defaultSkin;
    private Drawable selectedSkin;

    public EditorSideTabAdapter(List<SideTabData> dataSet, Drawable defaultSkin, Drawable selectedSkin) {
        localDataSet = dataSet;
        this.defaultSkin = defaultSkin;
        this.selectedSkin = selectedSkin;
    }

    public void setLocalDataSet(List<SideTabData> localDataSet) {
        for (SideTabData data : localDataSet){
            data.selected = false;
        }
        EditorSideTabAdapter.localDataSet = localDataSet;
        notifyDataSetChanged();
    }

    public static List<SideTabData> getLocalDataSet() {
        return localDataSet;
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
    // 사이드 탭에서 도형은 한개만 선택이 가능하며 선택된 도형을 다시 터치하면 선택이 해제된다.
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        SideTabData curData = localDataSet.get(position);
        viewHolder.setData(curData);
        View itemView = viewHolder.itemView;
        itemView.setBackground(curData.selected ? selectedSkin : defaultSkin);

        itemView.setOnClickListener(v -> {
            for(int i = 0; i< localDataSet.size(); i++){
                if(i != position)
                    localDataSet.get(i).selected = false;
            }
            localDataSet.get(position).selected = !localDataSet.get(position).selected;
            notifyDataSetChanged();
            // 어떤 항목에서 몇번 도형이 선택되었는지 canvasView 로 데이터를 전송

        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

