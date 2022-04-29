package com.penelope.seatforyou.ui.manager.reservation;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentReservationBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReservationFragment extends Fragment {

    private FragmentReservationBinding binding;
    private ReservationViewModel viewModel;


    public ReservationFragment() {
        super(R.layout.fragment_reservation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentReservationBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}