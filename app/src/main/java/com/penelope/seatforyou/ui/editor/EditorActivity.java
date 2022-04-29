package com.penelope.seatforyou.ui.editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.ui.editor.adapter.EditorSideTabAdapter;
import com.penelope.seatforyou.ui.editor.adapter.SideTabData;
import com.penelope.seatforyou.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 에디터 화면을 나타내는 액티비티입니다.
 * 좌석배치를 편집할 수 있습니다.
 */
public class EditorActivity extends AppCompatActivity {

    private LinearLayout levelList;
    private ScrollView levelScrollView;
    List<String> tabItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        initLayout();
    }

    private void initLayout() {
        levelScrollView = findViewById(R.id.scrollview_levels);
        levelList = findViewById(R.id.ll_levels);
        initSideBar();
        initBottomTab();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initSideBar() {
        RecyclerView sideTab = findViewById(R.id.editor_sidebar);
        sideTab.setLayoutManager(new LinearLayoutManager(this));

        // TODO : 기본 도형정보는 열거형으로 관리할것
        // 리사이클러뷰에 데이터 입력
        List<SideTabData> dataList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            dataList.add(new SideTabData(R.drawable.ic_baseline_crop_square_24, "사각형"));
        }

        // 리사이클러 뷰 어댑터 장착 및 아이템별 구분선 표시설정
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        sideTab.setAdapter(new EditorSideTabAdapter(dataList));
        sideTab.addItemDecoration(decoration);

        // 사이드탭 토글버튼 초기화
        ImageButton btn_slide = findViewById(R.id.button_sidebar_slide);
        btn_slide.setOnTouchListener((v, event) -> {
            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    btn_slide.setRotation((btn_slide.getRotation() == 0) ? 180 : 0);    // 버튼의 화살표 아이콘 상하 반전
                    Vibrator vibrator = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(TimeUtils.VIBE_SHORT);
                    v.performClick();
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    return false;
            }
            return true;
        });

        // 항목버튼 초기화
        Button btn_category = findViewById(R.id.btn_sidebar_category);
        btn_category.setOnTouchListener((v, event) -> {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN) {
                // TODO : 물체 카테고리 리스트 열려야됨
                Toast.makeText(this, "항목버튼", Toast.LENGTH_SHORT).show();
                return true;
            } else return false;
        });
    }

    private void initBottomTab() {
        TabLayout tabLayout = findViewById(R.id.tablayout_editor_btmctrl);

        tabLayout.addTab(tabLayout.newTab().setText("되돌리기").setIcon(R.drawable.ic_baseline_undo_24));
        tabLayout.addTab(tabLayout.newTab().setText("다시실행").setIcon(R.drawable.ic_baseline_redo_24));
        tabLayout.addTab(tabLayout.newTab().setText("붙혀넣기").setIcon(R.drawable.ic_baseline_content_paste_24));
        tabLayout.addTab(tabLayout.newTab().setText("저장").setIcon(R.drawable.ic_baseline_save_24));
        tabLayout.addTab(tabLayout.newTab().setText("계층").setIcon(R.drawable.ic_baseline_stairs_24));

        // 테스트용 데이터 10개개
       // TODO: 현재 프로젝트의 계층현황을 얻어와서 화면에 반영해야함
        for (int i = 0; i < 10; i++) {
            View levelView = getLayoutInflater().inflate(R.layout.view_editor_levels, levelList, false);
            TextView level_name = levelView.findViewById(R.id.tv_levelname);
            TextView area = levelView.findViewById(R.id.tv_area);
            level_name.setText("테스트 - " + i + "층");
            area.setText(i * i + "m^2");
            levelView.setOnClickListener(v -> {
                Toast.makeText(this, level_name.getText(), Toast.LENGTH_SHORT).show();
            });
            levelList.addView(levelView);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: // 되돌리기
                        undo();
                        break;
                    case 1: // 다시실행
                        redo();
                        break;
                    case 2: // 붙혀넣기
                        paste();
                        break;
                    case 3: // 저장
                        saveProject();
                        break;
                    case 4: // 계층
                        toggleLayerView();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: // 되돌리기
                        undo();
                        break;
                    case 1: // 다시실행
                        redo();
                        break;
                    case 2: // 붙혀넣기
                        paste();
                        break;
                    case 3: // 저장
                        saveProject();
                        break;
                    case 4: // 계층
                        levelScrollView.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: // 되돌리기
                        undo();
                        break;
                    case 1: // 다시실행
                        redo();
                        break;
                    case 2: // 붙혀넣기
                        paste();
                        break;
                    case 3: // 저장
                        saveProject();
                        break;
                    case 4: // 계층
                        toggleLayerView();
                        break;
                }
            }
        });
    }

    private void undo() {
    }

    private void redo() {
    }

    private void paste() {
    }

    private void saveProject() {
    }

    // 계층창 토글기능
    private void toggleLayerView() {
        int status = levelScrollView.getVisibility();
        // TODO : 추후 invisible 을 gone 으로 바꿀 것
        // 참고자료 - https://zion830.tistory.com/141
        levelScrollView.setVisibility(status == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
    }


}
