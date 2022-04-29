package com.penelope.seatforyou.ui.main.auth.findpassword;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentFindPasswordBinding;

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

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}