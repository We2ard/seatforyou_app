package com.penelope.seatforyou.ui.manager.addeditreservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentAddEditReservationBinding;
import com.penelope.seatforyou.utils.TimeUtils;
import com.penelope.seatforyou.utils.ui.OnTextChangeListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddEditReservationFragment extends Fragment {

    private FragmentAddEditReservationBinding binding;
    private AddEditReservationViewModel viewModel;


    public AddEditReservationFragment() {
        super(R.layout.fragment_add_edit_reservation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentAddEditReservationBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(AddEditReservationViewModel.class);

        binding.textViewDate.setOnClickListener(v -> viewModel.onDateClick());
        binding.textViewTime.setOnClickListener(v -> viewModel.onTimeClick());

        binding.editTextReservationUid.setText(viewModel.getUid());
        binding.editTextReservationUid.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onUidChange(text);
            }
        });

        binding.numberPickerPersonNumber.setMinValue(1);
        binding.numberPickerPersonNumber.setMaxValue(10);
        binding.numberPickerPersonNumber.setOnValueChangedListener((picker, oldVal, newVal) ->
                viewModel.onPersonNumberChange(newVal));

        binding.buttonOk.setOnClickListener(v -> viewModel.onOkClick());

        viewModel.getDate().observe(getViewLifecycleOwner(), date -> {
            String strDate = TimeUtils.formatDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            binding.textViewDate.setText(strDate);
        });

        viewModel.getTime().observe(getViewLifecycleOwner(), time -> {
            String strTime = TimeUtils.formatTime(time.getHour(), time.getMinute());
            binding.textViewTime.setText(strTime);
        });

        viewModel.getPersonNumber().observe(getViewLifecycleOwner(), personNumber ->
                binding.numberPickerPersonNumber.setValue(personNumber));

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof AddEditReservationViewModel.Event.ShowGeneralMessage) {
                String message = ((AddEditReservationViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            } else if (event instanceof AddEditReservationViewModel.Event.NavigateBackWithResult) {
                boolean addOrEdit = ((AddEditReservationViewModel.Event.NavigateBackWithResult) event).addOrEdit;
                Bundle result = new Bundle();
                result.putBoolean("add_or_edit", addOrEdit);
                getParentFragmentManager().setFragmentResult("add_edit_reservation_fragment", result);
                Navigation.findNavController(requireView()).popBackStack();
            } else if (event instanceof AddEditReservationViewModel.Event.PromptDate) {
                showDateDialog();
            } else if (event instanceof AddEditReservationViewModel.Event.PromptTime) {
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