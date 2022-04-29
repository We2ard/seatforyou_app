package com.penelope.seatforyou.ui.main.auth.register;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentRegisterBinding;
import com.penelope.seatforyou.utils.ui.OnTextChangeListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;


    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentRegisterBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding.editTextName.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onNameChange(text);
            }
        });
        binding.editTextPassword.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onPasswordChange(text);
            }
        });
        binding.editTextPasswordConfirm.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onPasswordConfirmChange(text);
            }
        });
        binding.editTextPhone.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onPhoneChange(text);
            }
        });
        binding.radioGroupUserType.setOnCheckedChangeListener((group, checkedId) ->
                viewModel.onIsCustomerChange(checkedId == R.id.radioButtonCustomer)
        );
        binding.buttonRegister.setOnClickListener(v -> viewModel.onRegisterClick());

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof RegisterViewModel.Event.ShowGeneralMessage) {
                String message = ((RegisterViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}