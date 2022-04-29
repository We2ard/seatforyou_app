package com.penelope.seatforyou.ui.manager;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.ActivityManagerBinding;
import com.penelope.seatforyou.utils.PrefUtils;
import com.penelope.seatforyou.utils.ui.AuthListenerActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManagerActivity extends AuthListenerActivity {

    private ActivityManagerBinding binding;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 뷰 바인딩을 실행한다
        binding = ActivityManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 액션바 타이틀을 숨긴다
        setSupportActionBar(binding.toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // 네비게이션 호스트 프래그먼트로부터 네비게이션 컨트롤러를 획득한다
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            // 액션바를 네비게이션 컨트롤러와 연동한다
            NavigationUI.setupActionBarWithNavController(this, navController);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // 네비게이션 컨트롤러에 뒤로가기 버튼 연동
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        // Preference 에 uid 를 기록한다
        PrefUtils.setCurrentUid(this, firebaseAuth.getUid());

        if (firebaseAuth.getUid() == null) {
            // 로그아웃이 감지되면 액티비티를 종료한다
            finish();
        }
    }

}