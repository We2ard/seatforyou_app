package com.penelope.seatforyou.ui.main.detail.menulist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentMenuListBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MenuListFragment extends Fragment {

    private FragmentMenuListBinding binding;
    private MenuListViewModel viewModel;


    public MenuListFragment() {
        super(R.layout.fragment_menu_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentMenuListBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(MenuListViewModel.class);

        MenusAdapter adapter = new MenusAdapter();
        binding.recyclerMenu.setAdapter(adapter);
        binding.recyclerMenu.setHasFixedSize(true);

        adapter.submitList(viewModel.getMenuList());

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}