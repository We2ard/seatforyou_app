package com.penelope.seatforyou.ui.main.detail.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.databinding.FragmentDetailBinding;
import com.penelope.seatforyou.utils.ui.AuthListenerFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailFragment extends AuthListenerFragment implements
        TabLayout.OnTabSelectedListener,
        NavController.OnDestinationChangedListener {

    private FragmentDetailBinding binding;
    private DetailViewModel viewModel;
    private NavController navController;


    public DetailFragment() {
        super(R.layout.fragment_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentDetailBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager()
                .findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            navController.setGraph(R.navigation.nav_graph_detail, getArguments());
        }

        navController.addOnDestinationChangedListener(this);
        binding.tabLayout.addOnTabSelectedListener(this);
        binding.buttonBack.setOnClickListener(v -> viewModel.onBackClick());
        binding.buttonClose.setOnClickListener(v -> viewModel.onCloseClick());
        binding.imageViewPhone.setOnClickListener(v -> viewModel.onPhoneClick());
        binding.imageViewZzim.setOnClickListener(v -> viewModel.onZzimClick());
        binding.buttonReserve.setOnClickListener(v -> viewModel.onReserveClick());

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof DetailViewModel.Event.DialPhone) {
                String phone = ((DetailViewModel.Event.DialPhone) event).phone;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            } else if (event instanceof DetailViewModel.Event.ShowGeneralMessage) {
                String message = ((DetailViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            } else if (event instanceof DetailViewModel.Event.NavigateToReserveScreen) {
                Shop shop = ((DetailViewModel.Event.NavigateToReserveScreen) event).shop;
                NavDirections navDirections = DetailFragmentDirections.actionDetailFragmentToReserveFragment(shop);
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof DetailViewModel.Event.NavigateBack) {
                Navigation.findNavController(requireView()).popBackStack();
            }
        });

        getParentFragmentManager().setFragmentResultListener("reserve_fragment", getViewLifecycleOwner(),
                (requestKey, result) -> {
                    boolean success = result.getBoolean("success");
                    viewModel.onReserveResult(success);
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

    @Override
    public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {

        int fragmentId = navDestination.getId();

        if (fragmentId == R.id.overviewFragment) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0));
        } else if (fragmentId == R.id.menuListFragment) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1));
        } else if (fragmentId == R.id.pictureFragment) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(2));
        } else if (fragmentId == R.id.reviewFragment) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(3));
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        switch (tab.getPosition()) {
            case 0:
                navController.navigate(R.id.overviewFragment, getArguments());
                break;
            case 1:
                navController.navigate(R.id.menuListFragment, getArguments());
                break;
            case 2:
                navController.navigate(R.id.pictureFragment, getArguments());
                break;
            case 3:
                navController.navigate(R.id.reviewFragment, getArguments());
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

}








