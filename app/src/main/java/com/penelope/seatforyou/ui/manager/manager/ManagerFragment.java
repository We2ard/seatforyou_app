package com.penelope.seatforyou.ui.manager.manager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentManagerBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManagerFragment extends Fragment {

    private FragmentManagerBinding binding;
    private ManagerViewModel viewModel;


    public ManagerFragment() {
        super(R.layout.fragment_manager);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewModel.onBackClick();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentManagerBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(ManagerViewModel.class);

        binding.cardViewShop.setOnClickListener(v -> viewModel.onShopClick());
        binding.cardViewReservation.setOnClickListener(v -> viewModel.onReservationClick());
        binding.cardViewLogout.setOnClickListener(v -> viewModel.onLogoutClick());

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof ManagerViewModel.Event.NavigateToShopScreen) {
                NavDirections navDirections = ManagerFragmentDirections.actionManagerFragmentToShopFragment();
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof ManagerViewModel.Event.NavigateToReservationScreen) {
                NavDirections navDirections = ManagerFragmentDirections.actionManagerFragmentToReservationFragment();
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof ManagerViewModel.Event.ConfirmLogout) {
                showLogoutDialog();
            }  else if (event instanceof ManagerViewModel.Event.ShowGeneralMessage) {
                String message = ((ManagerViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        getParentFragmentManager().setFragmentResultListener("shop_fragment", getViewLifecycleOwner(),
                (requestKey, result) -> {
                    boolean enrollOrModify = result.getBoolean("enroll_or_modify");
                    viewModel.onShopFragmentResult(enrollOrModify);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showLogoutDialog() {
        // 로그아웃을 묻는 대화상자를 띄운다
        new AlertDialog.Builder(requireContext())
                .setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", (dialog, which) -> viewModel.onLogoutConfirm())
                .setNegativeButton("취소", null)
                .show();
    }

}