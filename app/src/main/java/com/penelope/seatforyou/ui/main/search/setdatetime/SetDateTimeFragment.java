package com.penelope.seatforyou.ui.main.search.setdatetime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentSetDateTimeBinding;
import com.penelope.seatforyou.utils.TimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SetDateTimeFragment extends Fragment {

    private FragmentSetDateTimeBinding binding;
    private SetDateTimeViewModel viewModel;


    public SetDateTimeFragment() {
        super(R.layout.fragment_set_date_time);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentSetDateTimeBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(SetDateTimeViewModel.class);

        binding.textViewDate.setOnClickListener(v -> viewModel.onDateClick());
        binding.textViewTime.setOnClickListener(v -> viewModel.onTimeClick());
        binding.buttonOk.setOnClickListener(v -> viewModel.onOkClick());

        viewModel.getDate().observe(getViewLifecycleOwner(), date -> {
            String strDate = TimeUtils.formatDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            binding.textViewDate.setText(strDate);
        });

        viewModel.getTime().observe(getViewLifecycleOwner(), time -> {
            String strTime = TimeUtils.formatTime(time.getHour(), time.getMinute());
            binding.textViewTime.setText(strTime);
        });

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof SetDateTimeViewModel.Event.ShowGeneralMessage) {
                String message = ((SetDateTimeViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            } else if (event instanceof SetDateTimeViewModel.Event.NavigateBackWithResult) {
                LocalDateTime dateTime = ((SetDateTimeViewModel.Event.NavigateBackWithResult) event).dateTime;
                Bundle result = new Bundle();
                result.putSerializable("date_time", dateTime);
                getParentFragmentManager().setFragmentResult("set_date_time_fragment", result);
                Navigation.findNavController(requireView()).popBackStack();
            } else if (event instanceof SetDateTimeViewModel.Event.PromptDate) {
                showDateDialog();
            } else if (event instanceof SetDateTimeViewModel.Event.PromptTime) {
                showTimeDialog();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void showDateDialog() {

        int nowYear = LocalDate.now().getYear();
        int nowMonth = LocalDate.now().getMonthValue() - 1;
        int nowDayOfMonth = LocalDate.now().getDayOfMonth();

        DatePickerDialog dialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> viewModel.onDateSelected(year, month + 1, dayOfMonth),
                nowYear, nowMonth, nowDayOfMonth
        );

        dialog.show();
    }

    public void showTimeDialog() {

        int nowHour = LocalDateTime.now().getHour();
        int nowMinute = LocalDateTime.now().getMinute();

        TimePickerDialog dialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute) -> viewModel.onTimeSelected(hourOfDay, minute),
                nowHour, nowMinute, false
        );

        dialog.show();
    }

}




