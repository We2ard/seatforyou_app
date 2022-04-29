package com.penelope.seatforyou.ui.main.zzim;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentZzimBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ZzimFragment extends Fragment {

    private FragmentZzimBinding binding;
    private ZzimViewModel viewModel;


    public ZzimFragment() {
        super(R.layout.fragment_zzim);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentZzimBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(ZzimViewModel.class);

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}