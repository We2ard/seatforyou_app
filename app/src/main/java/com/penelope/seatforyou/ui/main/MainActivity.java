package com.penelope.seatforyou.ui.main;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.data.user.UserRepository;
import com.penelope.seatforyou.databinding.ActivityMainBinding;
import com.penelope.seatforyou.ui.manager.ManagerActivity;
import com.penelope.seatforyou.utils.PrefUtils;
import com.penelope.seatforyou.utils.ui.AuthListenerActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AuthListenerActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private UserRepository userRepository;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userRepository = new UserRepository(FirebaseFirestore.getInstance());

        // 뷰 바인딩을 실행한다
        binding = ActivityMainBinding.inflate(getLayoutInflater());
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

        // 나가기 메뉴 클릭 시 로그아웃 대화상자를 띄운다
        binding.bottomNavMember.setItemOnTouchListener(R.id.actionExit,
                (v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        showLogoutDialog();
                    }
                    return false;
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // 네비게이션 컨트롤러에 뒤로가기 버튼 연동
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        NavDestination destination = navController.getCurrentDestination();
        if (destination != null && destination.getId() == R.id.homeFragment) {
            showLogoutDialog();
            return;
        }
        super.onBackPressed();
    }

    private void onLogoutClick() {
        // 로그아웃 처리한다
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        String uid = firebaseAuth.getUid();

        // Preference 에 uid 를 기록한다
        PrefUtils.setCurrentUid(this, uid);

        // 적절한 바텀 네비게이션을 보인다
        binding.bottomNavMember.setVisibility(uid != null ? View.VISIBLE : View.INVISIBLE);
        binding.bottomNavNotMember.setVisibility(uid != null ? View.INVISIBLE : View.VISIBLE);
        NavigationUI.setupWithNavController(
                uid != null ? binding.bottomNavMember : binding.bottomNavNotMember, navController);

        if (uid != null) {
            userRepository.getUser(uid,
                    user -> {
                        if (user != null) {
                            if (user.isCustomer()) {
                                // 고객 로그인이 감지되면 HomeFragment 를 보인다
                                binding.bottomNavMember.setSelectedItemId(R.id.homeFragment);
                            } else {
                                // 점주 로그인이 감지되면 ManagerActivity 를 보인다
                                Intent intent = new Intent(this, ManagerActivity.class);
                                startActivity(intent);
                            }
                        }
                    },
                    Throwable::printStackTrace);
        } else {
            // 로그아웃이 감지되면 LoginFragment 를 보인다
            binding.bottomNavNotMember.setSelectedItemId(R.id.loginFragment);
        }
    }

    private void showLogoutDialog() {
        // 로그아웃을 묻는 대화상자를 띄운다
        new AlertDialog.Builder(this)
                .setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", (dialog, which) -> onLogoutClick())
                .setNegativeButton("취소", null)
                .show();
    }

}