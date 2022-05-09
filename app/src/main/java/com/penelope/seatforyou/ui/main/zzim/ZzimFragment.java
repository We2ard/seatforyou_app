package com.penelope.seatforyou.ui.main.zzim;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.databinding.FragmentZzimBinding;
import com.penelope.seatforyou.ui.main.home.ShopsAdapter;
import com.penelope.seatforyou.utils.ui.AuthListenerFragment;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ZzimFragment extends AuthListenerFragment {

    private FragmentZzimBinding binding;
    private ZzimViewModel viewModel;


    public ZzimFragment() {
        super(R.layout.fragment_zzim);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentZzimBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(ZzimViewModel.class);

        ShopsAdapter adapter = new ShopsAdapter(Glide.with(this));
        binding.recyclerZzim.setAdapter(adapter);
        binding.recyclerZzim.setHasFixedSize(true);

        adapter.setOnItemSelectedListener(position -> {
            Shop shop = adapter.getCurrentList().get(position);
            viewModel.onShopClick(shop);
        });

        viewModel.getShops().observe(getViewLifecycleOwner(), shops -> {
            if (shops != null) {
                adapter.submitList(null);
                adapter.submitList(new ArrayList<>(shops));
                binding.textViewNoZzim.setVisibility(shops.isEmpty() ? View.VISIBLE : View.INVISIBLE);
            }
            binding.progressBar3.setVisibility(View.INVISIBLE);
        });

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof ZzimViewModel.Event.NavigateToDetailScreen) {
                Shop shop = ((ZzimViewModel.Event.NavigateToDetailScreen) event).shop;
                NavDirections navDirections = ZzimFragmentDirections.actionGlobalDetailFragment(shop);
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof ZzimViewModel.Event.HideLoadingUI) {
                binding.progressBar3.setVisibility(View.INVISIBLE);
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