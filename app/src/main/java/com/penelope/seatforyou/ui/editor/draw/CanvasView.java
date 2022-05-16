package com.penelope.seatforyou.ui.editor.draw;

import static android.content.ContentValues.TAG;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.data.editor.assets.AssetData;
import com.penelope.seatforyou.ui.editor.listener.DragListener;

import java.util.ArrayList;

/**
 * 실제 그림이 그려지는 뷰 클래스
 */
public class CanvasView extends View {
    // 배치할 도형
    private ArrayList<AssetData> assetList = new ArrayList<>();

    private Paint paint;
    Canvas canvas;

    public CanvasView(Context context) {
        super(context);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);
        paint.setColor(Color.BLACK);
        init();
    }

    // 드래그 감지시 대기하다 손이 리스트 밖으로 나가면 그 시점의 손가락 위치에서 물체 생성 이후
    // 손가락을 뗄 때 까지 손가락을 따라다님
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        this.canvas = canvas;
    }

    // 뷰 초기 세팅
    private void init() {
        setId(View.generateViewId());
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);

        this.setOnDragListener(new DragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                float dropX = event.getX();
                float dropY = event.getY();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Toast.makeText(v.getContext(), "물체가 들어옴", Toast.LENGTH_SHORT).show();
                        CanvasView.this.setBackgroundColor(GREEN);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Toast.makeText(v.getContext(), "물체가 나감", Toast.LENGTH_SHORT).show();
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        CanvasView.this.setBackgroundColor(WHITE);
                        break;
                    case DragEvent.ACTION_DROP:
                        Toast.makeText(v.getContext(), "x : " + dropX + " y : " + dropY, Toast.LENGTH_SHORT).show();
                        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_crop_square_24, null);
                        assert drawable != null;
                        drawable.setBounds((int) event.getX(), (int) event.getY(), drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        drawable.draw(canvas);
//                        invalidate();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}
