package com.penelope.seatforyou.ui.manager.shop.openclose;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentOpenCloseBinding;
import com.penelope.seatforyou.utils.TimeUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OpenCloseFragment extends DialogFragment {

    private FragmentOpenCloseBinding binding;
    private OpenCloseViewModel viewModel;


    public OpenCloseFragment() {
        super(R.layout.fragment_open_close);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentOpenCloseBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(OpenCloseViewModel.class);

        binding.textViewOpenTime.setOnClickListener(v -> viewModel.onOpenTimeClick());
        binding.textViewCloseTime.setOnClickListener(v -> viewModel.onCloseTimeClick());
        binding.buttonConfirm.setOnClickListener(v -> viewModel.onConfirmClick());

        viewModel.getOpenHour().observe(getViewLifecycleOwner(), openHour ->
                viewModel.getOpenMinute().observe(getViewLifecycleOwner(), openMinute -> {
                    String strOpen = String.format("OPEN : %s", TimeUtils.formatTime(openHour, openMinute));
                    binding.textViewOpenTime.setText(strOpen);
                }));

        viewModel.getCloseHour().observe(getViewLifecycleOwner(), closeHour ->
                viewModel.getCloseMinute().observe(getViewLifecycleOwner(), closeMinute -> {
                    String strClose = String.format("CLOSE : %s", TimeUtils.formatTime(closeHour, closeMinute));
                    binding.textViewCloseTime.setText(strClose);
                }));

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof OpenCloseViewModel.Event.PromptTime) {
                boolean openClose = ((OpenCloseViewModel.Event.PromptTime) event).openClose;
                int hour = ((OpenCloseViewModel.Event.PromptTime) event).hour;
                int minute = ((OpenCloseViewModel.Event.PromptTime) event).minute;
                showTimePicker(openClose, hour, minute);
            } else if (event instanceof OpenCloseViewModel.Event.NavigateBackWithResult) {
                int openHour = ((OpenCloseViewModel.Event.NavigateBackWithResult) event).openHour;
                int openMinute = ((OpenCloseViewModel.Event.NavigateBackWithResult) event).openMinute;
                int closeHour = ((OpenCloseViewModel.Event.NavigateBackWithResult) event).closeHour;
                int closeMinute = ((OpenCloseViewModel.Event.NavigateBackWithResult) event).closeMinute;
                Bundle result = new Bundle();
                result.putInt("open_hour", openHour);
                result.putInt("open_minute", openMinute);
                result.putInt("close_hour", closeHour);
                result.putInt("close_minute", closeMinute);
                getParentFragmentManager().setFragmentResult("open_close_fragment", result);
                NavHostFragment.findNavController(this).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showTimePicker(boolean openClose, int oldHour, int oldMinute) {

        TimePickerDialog dialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute) -> {
                    if (openClose) {
                        viewModel.onOpenTimeSelected(hourOfDay, minute);
                    } else {
                        viewModel.onCloseTimeSelected(hourOfDay, minute);
                    }
                },
                oldHour,
                oldMinute,
                false
        );

        dialog.show();
    }


}



