package com.penelope.seatforyou.ui.editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.data.editor.InteriorLevel;
import com.penelope.seatforyou.data.editor.InteriorProject;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.data.user.User;
import com.penelope.seatforyou.databinding.ActivityEditorBinding;
import com.penelope.seatforyou.ui.editor.adapter.EditorSideTabAdapter;
import com.penelope.seatforyou.ui.editor.adapter.SideTabData;
import com.penelope.seatforyou.ui.editor.draw.CanvasView;
import com.penelope.seatforyou.utils.TimeUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 에디터 화면을 나타내는 액티비티입니다.
 * 좌석배치를 편집할 수 있습니다.
 */
public class EditorActivity extends AppCompatActivity {

    public static RecyclerView sideTab;
    public static Context context;
    private LinearLayout levelList;
    private ScrollView levelScrollView;
    private ActivityEditorBinding binding;
    private List<String> tabItems = new ArrayList<>();
    private InteriorProject project;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initLayout();
    }

    private void initLayout() {
        levelScrollView = findViewById(R.id.scrollview_levels);
        levelList = findViewById(R.id.ll_levels);
        context = this.getApplicationContext();
        loadProject();
        initSideBar();
        initBottomTab();
    }

    // 데이터 클래스로부터 정보를 가져오는 메소드
    private void loadProject() {
        // TODO: db에 접속해서 현재 사용자의 식별자와 프로젝트의 식별자를 비교해서 데이터를 가져옴
        // 아래는 테스트용 ui, 실제로는 db에서 InteriorProject를 가져와야 함
        project = new InteriorProject("somethinguid");
        // 객체에서 현재 보여줄 뷰를 반환
        CanvasView view = project.getCurrentLevel().getCanvasView();

        // 받은 뷰를 화면에 출력
        binding.getRoot().addView(view);
        // 뷰에 제약조건 추가
        ConstraintSet set = new ConstraintSet();
        set.clone(binding.getRoot());
        set.connect(view.getId(), ConstraintSet.START, binding.getRoot().getId(), ConstraintSet.START, 0);
        set.connect(view.getId(), ConstraintSet.END, binding.getRoot().getId(), ConstraintSet.END, 0);
        set.connect(view.getId(), ConstraintSet.TOP, binding.getRoot().getId(), ConstraintSet.TOP, 0);
        set.connect(view.getId(), ConstraintSet.BOTTOM, binding.getRoot().getId(), ConstraintSet.BOTTOM, 0);
        set.applyTo(binding.getRoot());
    }

    private void addCanvasView() {
        // data class에 반영함

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initSideBar() {
        // TODO : 2차원 배열로 항목별 도형리스트를 관리할 것
        sideTab = findViewById(R.id.editor_sidebar);
        sideTab.setLayoutManager(new LinearLayoutManager(this));

        // TODO : 기본 도형정보는 열거형으로 관리할것
        // 리사이클러뷰에 데이터 입력
        List<SideTabData> dataList = new ArrayList<>();
        List<SideTabData> dataList2 = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            dataList.add(new SideTabData(R.drawable.ic_baseline_crop_square_24, "사각형"));
            if(i > 9)
                dataList2.add(new SideTabData(R.drawable.ic_baseline_panorama_fish_eye_24_circle, "동그라미"));
        }

        // 리스너에 어댑터 부착
        EditorSideTabAdapter adapter = new EditorSideTabAdapter(dataList, getDrawable(R.drawable.border_sidetab), getDrawable(R.drawable.border_sidetab_picked));
        sideTab.setAdapter(adapter);

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
        btn_category.setOnClickListener(v -> {
            // 팝업메뉴 설정
            PopupMenu popup = new PopupMenu(EditorActivity.this, btn_category);
            popup.getMenuInflater().inflate(R.menu.menu_editor_category, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.category_menu1:
                        adapter.setLocalDataSet(dataList);
                        break;
                    case R.id.category_menu2:
                        adapter.setLocalDataSet(dataList2);
                        break;
                }
                return true;
            });
            // 실제 실행코드
            popup.show();
        });
    }


    private void initBottomTab() {
        TabLayout tabLayout = findViewById(R.id.tablayout_editor_btmctrl);

        tabLayout.addTab(tabLayout.newTab().setText("되돌리기").setIcon(R.drawable.ic_baseline_undo_24));
        tabLayout.addTab(tabLayout.newTab().setText("다시실행").setIcon(R.drawable.ic_baseline_redo_24));
        tabLayout.addTab(tabLayout.newTab().setText("붙혀넣기").setIcon(R.drawable.ic_baseline_content_paste_24));
        tabLayout.addTab(tabLayout.newTab().setText("저장").setIcon(R.drawable.ic_baseline_save_24));
        tabLayout.addTab(tabLayout.newTab().setText("계층").setIcon(R.drawable.ic_baseline_stairs_24));

        ArrayList<InteriorLevel> levels = project.getLevels();

        for (InteriorLevel level : levels) {
            View levelView = getLayoutInflater().inflate(R.layout.view_editor_levels, levelList, false);
            TextView level_name = levelView.findViewById(R.id.tv_levelname);
            TextView area = levelView.findViewById(R.id.tv_area);
            level_name.setText(level.getLevelName());
            area.setText(level.getArea() + " m^2");
            levelView.setOnClickListener(v -> {
                // 현재층은 다시 눌러도 별다른 동작이 없음
                if (!(project.getCurrentLevel().getLevelId() == level.getLevelId())) {
                    project.setCurrentLevel(level.getLevelId());
                }

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
                        if (levelScrollView.getVisibility() == View.VISIBLE)
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
