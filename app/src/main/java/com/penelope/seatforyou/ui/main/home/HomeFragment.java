package com.penelope.seatforyou.ui.main.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.collect.Lists;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.util.FusedLocationSource;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.databinding.FragmentHomeBinding;
import com.penelope.seatforyou.ui.manager.shop.category.CardsAdapter;
import com.penelope.seatforyou.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements OnMapReadyCallback, NaverMap.OnLocationChangeListener {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    private NaverMap naverMap;
    private FusedLocationSource fusedLocationSource;
    private ActivityResultLauncher<String> locationPermissionLauncher;


    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationSource = new FusedLocationSource(requireActivity(), LOCATION_PERMISSION_REQUEST_CODE);
        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (fusedLocationSource.onRequestPermissionsResult(
                            LOCATION_PERMISSION_REQUEST_CODE,
                            new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                            new int[] { result ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED }
                    )) {
                        if (!fusedLocationSource.isActivated()) {
                            naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                            return;
                        }
                        configureMap();
                    }
                }
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentHomeBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        binding.progressBarSearching.setVisibility(View.INVISIBLE);
        binding.textViewNewShop.setOnClickListener(v -> viewModel.onNewShopClick());

        CardsAdapter regionAdapter = new CardsAdapter(true);
        binding.recyclerRegion.setAdapter(regionAdapter);

        regionAdapter.submitList(Lists.newArrayList(
                "주변", "서울", "경기", "대전", "광주", "부산", "울산",
                "강원", "충남", "충북", "전남", "전북", "경남", "경북", "제주"
        ));

        regionAdapter.setOnItemSelectedListener(position -> {
            if (position == 0) {
                viewModel.onNearbyClick();
            } else {
                String region = regionAdapter.getCurrentList().get(position);
                viewModel.onRegionClick(region);
            }
        });

        ShopsAdapter shopAdapter = new ShopsAdapter(Glide.with(this));
        binding.recyclerRecommended.setAdapter(shopAdapter);

        shopAdapter.setOnItemSelectedListener(position -> {
            Shop shop = shopAdapter.getCurrentList().get(position);
            viewModel.onShopClick(shop);
        });

        viewModel.getRecommendedShops().observe(getViewLifecycleOwner(), shops -> {
            if (shops != null) {
                shopAdapter.submitList(shops);
            }
            binding.progressBar3.setVisibility(View.INVISIBLE);
        });

        viewModel.getNewestShop().observe(getViewLifecycleOwner(), shop -> {
            if (shop != null) {
                binding.textViewNewShopName.setText(shop.getName());
                Glide.with(this)
                        .load(ImageUtils.getShopImageUrl(shop.getUid()))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(binding.imageViewNewShop);
            } else {
                String title = "Seat for You";
                binding.textViewNewShopName.setText(title);
            }
        });

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof HomeViewModel.Event.NavigateToDetailScreen) {
                Shop shop = ((HomeViewModel.Event.NavigateToDetailScreen) event).shop;
                NavDirections navDirections = HomeFragmentDirections.actionGlobalDetailFragment(shop);
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof HomeViewModel.Event.ShowGeneralMessage) {
                String message = ((HomeViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            } else if (event instanceof HomeViewModel.Event.ShowLoadingUI) {
                binding.progressBarSearching.setVisibility(View.VISIBLE);
            } else if (event instanceof HomeViewModel.Event.NavigateToFilteredScreen) {
                List<Shop> shops = ((HomeViewModel.Event.NavigateToFilteredScreen) event).shops;
                String description = ((HomeViewModel.Event.NavigateToFilteredScreen) event).description;
                NavDirections navDirections = HomeFragmentDirections.actionGlobalFilteredFragment(
                        new ArrayList<>(shops), description);
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof HomeViewModel.Event.RequestLocationPermission) {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull NaverMap map) {

        naverMap = map;

        if (requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            configureMap();
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void configureMap() {

        naverMap.addOnLocationChangeListener(this);
        naverMap.setLocationSource(fusedLocationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        naverMap.getUiSettings().setLocationButtonEnabled(true);
    }

    @Override
    public void onLocationChange(@NonNull Location location) {
        viewModel.onLocationChange(location);
    }

}