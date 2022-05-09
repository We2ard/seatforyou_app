package com.penelope.seatforyou.ui.main.detail.overview;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentOverviewBinding;
import com.penelope.seatforyou.utils.ImageUtils;
import com.penelope.seatforyou.utils.StringUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OverviewFragment extends Fragment {

    private FragmentOverviewBinding binding;
    private OverviewViewModel viewModel;


    public OverviewFragment() {
        super(R.layout.fragment_overview);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentOverviewBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(OverviewViewModel.class);

        binding.textViewShopName.setText(viewModel.getShop().getName());
        binding.textViewShopDescription.setText(viewModel.getShop().getDescription());
        binding.textViewShopAddress.setText(viewModel.getShop().getAddress().getLoadAddress());

        Glide.with(this).load(ImageUtils.getShopImageUrl(viewModel.getShop().getUid()))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.progressBar7.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.imageViewShop);

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}