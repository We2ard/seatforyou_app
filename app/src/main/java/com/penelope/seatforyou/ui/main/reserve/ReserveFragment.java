package com.penelope.seatforyou.ui.main.reserve;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentReserveBinding;
import com.penelope.seatforyou.utils.ui.AuthListenerFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReserveFragment extends AuthListenerFragment {

    private FragmentReserveBinding binding;
    private ReserveViewModel viewModel;


    public ReserveFragment() {
        super(R.layout.fragment_reserve);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentReserveBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(ReserveViewModel.class);

        binding.button1p.setOnClickListener(v -> viewModel.onPersonNumberClick(1));
        binding.button2p.setOnClickListener(v -> viewModel.onPersonNumberClick(2));
        binding.button3p.setOnClickListener(v -> viewModel.onPersonNumberClick(3));
        binding.button4p.setOnClickListener(v -> viewModel.onPersonNumberClick(4));
        binding.button5p.setOnClickListener(v -> viewModel.onPersonNumberClick(5));
        binding.button6p.setOnClickListener(v -> viewModel.onPersonNumberClick(6));

        binding.button630pm.setOnClickListener(v -> viewModel.onTimeClick(18, 30));
        binding.button700pm.setOnClickListener(v -> viewModel.onTimeClick(19, 0));
        binding.button730pm.setOnClickListener(v -> viewModel.onTimeClick(19, 30));
        binding.button800pm.setOnClickListener(v -> viewModel.onTimeClick(20, 0));
        binding.button900pm.setOnClickListener(v -> viewModel.onTimeClick(21, 0));

        binding.datePicker.setOnDateChangedListener((view1, year, monthOfYear, dayOfMonth) ->
                viewModel.onDateClick(year, monthOfYear + 1, dayOfMonth));

        binding.buttonOk.setOnClickListener(v -> viewModel.onOkClick());
        binding.buttonBack.setOnClickListener(v -> viewModel.onBackClick());
        binding.buttonClose.setOnClickListener(v -> viewModel.onCloseClick());

        final Button[] buttonsPerson = {
                binding.button1p, binding.button2p, binding.button3p,
                binding.button4p, binding.button5p, binding.button6p
        };
        final Button[] buttonsTime = {
                binding.button630pm, binding.button700pm, binding.button730pm,
                binding.button800pm, binding.button900pm
        };
        final int colorBkgUnchecked = 0xFFFFFFFF;
        final int colorTextUnchecked = getResources().getColor(R.color.colorBlueDark, null);
        final int colorBkgChecked = getResources().getColor(R.color.colorBlue, null);
        final int colorTextChecked = 0xFFFFFFFF;

        viewModel.getPersonNumber().observe(getViewLifecycleOwner(), number -> {
            for (Button button : buttonsPerson) {
                button.setBackgroundColor(colorBkgUnchecked);
                button.setTextColor(colorTextUnchecked);
            }
            buttonsPerson[number - 1].setBackgroundColor(colorBkgChecked);
            buttonsPerson[number - 1].setTextColor(colorTextChecked);
        });

        viewModel.getTime().observe(getViewLifecycleOwner(), time -> {
            for (Button button : buttonsTime) {
                button.setBackgroundColor(colorBkgUnchecked);
                button.setTextColor(colorTextUnchecked);
            }
            int index = -1;
            if (time.getHour() == 18 && time.getMinute() == 30) {
                index = 0;
            } else if (time.getHour() == 19 && time.getMinute() == 0) {
                index = 1;
            } else if (time.getHour() == 19 && time.getMinute() == 30) {
                index = 2;
            } else if (time.getHour() == 20 && time.getMinute() == 0) {
                index = 3;
            } else if (time.getHour() == 21 && time.getMinute() == 0) {
                index = 4;
            }
            if (index != -1) {
                buttonsTime[index].setBackgroundColor(colorBkgChecked);
                buttonsTime[index].setTextColor(colorTextChecked);
            }
        });

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof ReserveViewModel.Event.NavigateBack) {
                Navigation.findNavController(requireView()).popBackStack();
            } else if (event instanceof ReserveViewModel.Event.NavigateBackWithResult) {
                boolean success = ((ReserveViewModel.Event.NavigateBackWithResult) event).success;
                Bundle result = new Bundle();
                result.putBoolean("success", success);
                getParentFragmentManager().setFragmentResult("reserve_fragment", result);
                Navigation.findNavController(requireView()).popBackStack();
            } else if (event instanceof ReserveViewModel.Event.ShowGeneralMessage) {
                String message = ((ReserveViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
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