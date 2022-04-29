package com.penelope.seatforyou.ui.manager.shop.category;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentCategoryBinding;
import com.penelope.seatforyou.utils.ui.OnTextChangeListener;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CategoryFragment extends DialogFragment {

    private FragmentCategoryBinding binding;
    private CategoryViewModel viewModel;


    public CategoryFragment() {
        super(R.layout.fragment_category);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentCategoryBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        binding.editTextCategory.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onCategoryInputChange(text);
            }
        });
        binding.imageViewAddCategory.setOnClickListener(v -> {
            viewModel.onAddCategoryClick();
            binding.editTextCategory.setText("");
        });
        binding.buttonConfirm.setOnClickListener(v -> viewModel.onConfirmClick());

        CardsAdapter adapter = new CardsAdapter(true);
        binding.recyclerCategory.setAdapter(adapter);

        adapter.setOnItemSelectedListener(position -> {
            String category = adapter.getCurrentList().get(position);
            viewModel.onCategoryClick(category);
        });

        viewModel.getCategories().observe(getViewLifecycleOwner(), adapter::submitList);

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof CategoryViewModel.Event.ShowGeneralMessage) {
                String message = ((CategoryViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            } else if (event instanceof CategoryViewModel.Event.ConfirmDeleteCategory) {
                String category = ((CategoryViewModel.Event.ConfirmDeleteCategory) event).category;
                confirmDeleteCategory(category);
            } else if (event instanceof CategoryViewModel.Event.NavigateBackWithResult) {
                List<String> categories = ((CategoryViewModel.Event.NavigateBackWithResult) event).categories;
                Bundle result = new Bundle();
                result.putStringArrayList("categories", new ArrayList<>(categories));
                getParentFragmentManager().setFragmentResult("category_fragment", result);
                NavHostFragment.findNavController(this).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void confirmDeleteCategory(String category) {

        new AlertDialog.Builder(requireContext())
                .setTitle("카테고리 삭제")
                .setMessage(category + "를 삭제하시겠습니까?")
                .setPositiveButton("삭제", (dialog, which) -> viewModel.onDeleteConfirm(category))
                .setNegativeButton("취소", null)
                .show();
    }

}