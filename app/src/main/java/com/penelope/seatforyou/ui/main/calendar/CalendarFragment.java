package com.penelope.seatforyou.ui.main.calendar;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentCalendarBinding;
import com.penelope.seatforyou.utils.ImageUtils;
import com.penelope.seatforyou.utils.TimeUtils;
import com.penelope.seatforyou.utils.ui.AuthListenerFragment;

import java.time.LocalDate;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CalendarFragment extends AuthListenerFragment {

    private FragmentCalendarBinding binding;
    private CalendarViewModel viewModel;


    public CalendarFragment() {
        super(R.layout.fragment_calendar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentCalendarBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        binding.calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) ->
                viewModel.onDateClick(year, month + 1, dayOfMonth));

        LocalDate now = LocalDate.now();
        viewModel.onDateClick(now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        viewModel.getReservation().observe(getViewLifecycleOwner(), reservation -> {
            if (reservation != null) {
                String strPersonNumber = String.format(Locale.getDefault(), "예약 인원 %d명", reservation.getPersonNumber());
                binding.textViewReservationPersonNumber.setText(strPersonNumber);

                String strDateTime = TimeUtils.formatDateTime(
                        reservation.getMonth(), reservation.getDayOfMonth(),
                        reservation.getHour(), reservation.getMinute());
                binding.textViewReservationDateTime.setText(strDateTime);
            }
            binding.textViewReservationPersonNumber.setVisibility(reservation != null ? View.VISIBLE : View.INVISIBLE);
            binding.textViewReservationDateTime.setVisibility(reservation != null ? View.VISIBLE : View.INVISIBLE);
            binding.textViewNoReservations.setVisibility(reservation == null ? View.VISIBLE : View.INVISIBLE);
            binding.progressBar4.setVisibility(View.INVISIBLE);
        });

        viewModel.getShop().observe(getViewLifecycleOwner(), shop -> {
            if (shop != null) {
                binding.textViewReservationShopName.setText(shop.getName());
                binding.textViewReservationDescription.setText(shop.getDescription());
                Glide.with(this)
                        .load(ImageUtils.getShopImageUrl(shop.getUid()))
                        .into(binding.imageViewReservationShop);
            }
            binding.textViewReservationShopName.setVisibility(shop != null ? View.VISIBLE : View.INVISIBLE);
            binding.textViewReservationDescription.setVisibility(shop != null ? View.VISIBLE : View.INVISIBLE);
            binding.imageViewReservationShop.setVisibility(shop != null ? View.VISIBLE : View.INVISIBLE);
        });

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof CalendarViewModel.Event.ShowLoadingUI) {
                binding.progressBar4.setVisibility(View.VISIBLE);
            } else if (event instanceof CalendarViewModel.Event.HideLoadingUI) {
                binding.progressBar4.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        viewModel.onAuthStateChanged(firebaseAuth);
    }

}