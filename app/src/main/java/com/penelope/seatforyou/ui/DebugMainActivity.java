package com.penelope.seatforyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.ui.editor.EditorActivity;
import com.penelope.seatforyou.ui.main.MainActivity;

/**
 * 디버깅용 화면입니다.
 * 어플을 시작하면 홈화면과 에디터 화면 중 어디로 갈지 선택할 수 있습니다.
 */
public class DebugMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debugmain);

        Button goto_home = (Button)findViewById(R.id.button_debug_tohome);
        Button goto_editor = (Button)findViewById(R.id.button_debug_toeditor);

        goto_home.setOnClickListener(v -> {
            // 상황에 맞게 작성하세요
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        goto_editor.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
            startActivity(intent);
        });
    }
}