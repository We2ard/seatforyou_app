package com.penelope.seatforyou.ui.main.auth.findpassword;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentFindPasswordBinding;
import com.penelope.seatforyou.utils.ui.OnTextChangeListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FindPasswordFragment extends Fragment {

    private FragmentFindPasswordBinding binding;
    private FindPasswordViewModel viewModel;


    public FindPasswordFragment() {
        super(R.layout.fragment_find_password);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentFindPasswordBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(FindPasswordViewModel.class);

        binding.editTextUserName.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onNameChange(text);
            }
        });
        binding.editTextUserPhone.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onPhoneChange(text);
            }
        });
        binding.buttonSearchPassword.setOnClickListener(v -> viewModel.onSearchClick());

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof FindPasswordViewModel.Event.ShowGeneralMessage) {
                String message = ((FindPasswordViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            } else if (event instanceof FindPasswordViewModel.Event.ShowPassword) {
                String password = ((FindPasswordViewModel.Event.ShowPassword) event).password;
                new AlertDialog.Builder(requireContext())
                        .setTitle("비밀번호 확인")
                        .setMessage("고객님의 비밀번호는 '" + password + "' 입니다")
                        .setPositiveButton("로그인하기", (dialog, which) -> {
                            dialog.dismiss();
                            viewModel.onLoginClick();
                        })
                        .show();
            } else if (event instanceof FindPasswordViewModel.Event.NavigateBack) {
                Navigation.findNavController(requireView()).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}