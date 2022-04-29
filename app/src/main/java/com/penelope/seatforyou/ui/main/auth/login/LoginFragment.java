package com.penelope.seatforyou.ui.main.auth.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentLoginBinding;
import com.penelope.seatforyou.utils.ui.OnTextChangeListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;


    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentLoginBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.editTextPhone.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onPhoneChange(text);
            }
        });
        binding.editTextPassword.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onPasswordChange(text);
            }
        });

        binding.buttonLogin.setOnClickListener(v -> viewModel.onLoginClick());
        binding.buttonRegister.setOnClickListener(v -> viewModel.onRegisterClick());
        binding.buttonFindPassword.setOnClickListener(v -> viewModel.onFindPasswordClick());
        binding.buttonRegisterInquiry.setOnClickListener(v -> viewModel.onRegisterInquiryClick());

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof LoginViewModel.Event.NavigateToRegisterScreen) {
                NavDirections navDirections = LoginFragmentDirections.actionLoginFragmentToRegisterFragment();
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof LoginViewModel.Event.NavigateToFindPasswordScreen) {
                NavDirections navDirections = LoginFragmentDirections.actionLoginFragmentToFindPasswordFragment();
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof LoginViewModel.Event.NavigateToInquireRegisterScreen) {
                NavDirections navDirections = LoginFragmentDirections.actionLoginFragmentToInquireRegisterFragment();
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof LoginViewModel.Event.ShowGeneralMessage) {
                String message = ((LoginViewModel.Event.ShowGeneralMessage) event).message;
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