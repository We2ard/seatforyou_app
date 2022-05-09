package com.penelope.seatforyou.ui.main.search.setfilter;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.DialogCategoryBinding;
import com.penelope.seatforyou.databinding.DialogPriceBinding;
import com.penelope.seatforyou.databinding.DialogRegionBinding;
import com.penelope.seatforyou.databinding.FragmentSetFilterBinding;
import com.penelope.seatforyou.utils.StringUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SetFilterFragment extends Fragment {

    private FragmentSetFilterBinding binding;
    private SetFilterViewModel viewModel;


    public SetFilterFragment() {
        super(R.layout.fragment_set_filter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentSetFilterBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(SetFilterViewModel.class);

        binding.buttonClose.setOnClickListener(v -> viewModel.onCloseClick());
        binding.buttonSetPrice.setOnClickListener(v -> viewModel.onSetPriceClick());
        binding.buttonSetRegion.setOnClickListener(v -> viewModel.onSetRegionClick());
        binding.buttonSetCategory.setOnClickListener(v -> viewModel.onSetCategoryClick());
        binding.buttonClearAll.setOnClickListener(v -> viewModel.onClearClick());
        binding.buttonApply.setOnClickListener(v -> viewModel.onApplyClick());

        viewModel.getPrice().observe(getViewLifecycleOwner(), price -> {
            if (price != null) {
                String strPrice = StringUtils.price(price);
                binding.textViewPrice.setText(strPrice);
            } else {
                binding.textViewPrice.setText("");
            }
        });

        viewModel.getRegion().observe(getViewLifecycleOwner(), region -> {
            if (region != null) {
                binding.textViewRegion.setText(region);
            } else {
                binding.textViewRegion.setText("");
            }
        });

        viewModel.getCategory().observe(getViewLifecycleOwner(), category -> {
            if (category != null) {
                binding.textViewCategory.setText(category);
            } else {
                binding.textViewCategory.setText("");
            }
        });

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof SetFilterViewModel.Event.ShowGeneralMessage) {
                String message = ((SetFilterViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            } else if (event instanceof SetFilterViewModel.Event.NavigateBack) {
                Navigation.findNavController(requireView()).popBackStack();
            } else if (event instanceof SetFilterViewModel.Event.PromptRegion) {
                showRegionDialog();
            } else if (event instanceof SetFilterViewModel.Event.PromptPrice) {
                showPriceDialog();
            } else if (event instanceof SetFilterViewModel.Event.PromptCategory) {
                showCategoryDialog();
            } else if (event instanceof SetFilterViewModel.Event.NavigateBackWithResult) {
                String region = ((SetFilterViewModel.Event.NavigateBackWithResult) event).region;
                int price = ((SetFilterViewModel.Event.NavigateBackWithResult) event).price;
                String category = ((SetFilterViewModel.Event.NavigateBackWithResult) event).category;
                Bundle result = new Bundle();
                result.putString("region", region);
                result.putInt("price", price);
                result.putString("category", category);
                getParentFragmentManager().setFragmentResult("set_filter_fragment", result);
                Navigation.findNavController(requireView()).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showRegionDialog() {

        DialogRegionBinding binding = DialogRegionBinding.inflate(getLayoutInflater());

        new AlertDialog.Builder(requireContext())
                .setTitle("지역 설정")
                .setView(binding.getRoot())
                .setPositiveButton("설정", (dialog, which) ->
                        viewModel.onRegionSelected(binding.editTextRegion.getText().toString()))
                .setNegativeButton("취소", null)
                .show();
    }

    private void showPriceDialog() {

        DialogPriceBinding binding = DialogPriceBinding.inflate(getLayoutInflater());

        new AlertDialog.Builder(requireContext())
                .setTitle("1인당 가격 설정")
                .setView(binding.getRoot())
                .setPositiveButton("설정", (dialog, which) ->
                        viewModel.onPriceSelected(binding.editTextPrice.getText().toString()))
                .setNegativeButton("취소", null)
                .show();
    }

    private void showCategoryDialog() {

        DialogCategoryBinding binding = DialogCategoryBinding.inflate(getLayoutInflater());

        new AlertDialog.Builder(requireContext())
                .setTitle("카테고리 설정")
                .setView(binding.getRoot())
                .setPositiveButton("설정", (dialog, which) ->
                        viewModel.onCategorySelected(binding.editTextCategory.getText().toString()))
                .setNegativeButton("취소", null)
                .show();
    }

}




