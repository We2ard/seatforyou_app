package com.penelope.seatforyou.ui.main.filtered;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.databinding.FragmentFilteredBinding;
import com.penelope.seatforyou.ui.main.home.ShopsAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FilteredFragment extends Fragment {

    private FragmentFilteredBinding binding;
    private FilteredViewModel viewModel;


    public FilteredFragment() {
        super(R.layout.fragment_filtered);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentFilteredBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(FilteredViewModel.class);

        binding.textViewSearchDescription.setText(viewModel.getDescription());

        ShopsAdapter adapter = new ShopsAdapter(Glide.with(this));
        binding.recyclerShop.setAdapter(adapter);
        binding.recyclerShop.setHasFixedSize(true);

        adapter.setOnItemSelectedListener(position -> {
            Shop shop = adapter.getCurrentList().get(position);
            viewModel.onShopClick(shop);
        });

        adapter.submitList(viewModel.getShops());
        binding.textViewNoResults.setVisibility(viewModel.getShops().isEmpty() ? View.VISIBLE : View.INVISIBLE);

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof FilteredViewModel.Event.NavigateToDetailScreen) {
                Shop shop = ((FilteredViewModel.Event.NavigateToDetailScreen) event).shop;
                NavDirections navDirections = FilteredFragmentDirections.actionGlobalDetailFragment(shop);
                Navigation.findNavController(requireView()).navigate(navDirections);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}