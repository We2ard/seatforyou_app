package com.penelope.seatforyou.ui.main.detail.review;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentReviewBinding;
import com.penelope.seatforyou.utils.ui.AuthListenerFragment;

import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReviewFragment extends AuthListenerFragment {

    private FragmentReviewBinding binding;
    private ReviewViewModel viewModel;


    public ReviewFragment() {
        super(R.layout.fragment_review);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentReviewBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        binding.fabAddReview.setOnClickListener(v -> viewModel.onAddReviewClick());

        viewModel.getReviewNumber().observe(getViewLifecycleOwner(), reviewNumber -> {
            if (reviewNumber != null) {
                String strReview = String.format(Locale.getDefault(), "리뷰 (%d)", reviewNumber);
                binding.textViewReviewTitle.setText(strReview);
            } else {
                binding.textViewReviewTitle.setText("리뷰");
            }
        });

        viewModel.getAverageRating().observe(getViewLifecycleOwner(), rating -> {
            if (rating != null) {
                binding.ratingBarAverageReview.setRating(rating);
            }
            binding.ratingBarAverageReview.setVisibility(rating != null ? View.VISIBLE : View.INVISIBLE);
        });

        viewModel.getUserMap().observe(getViewLifecycleOwner(), userMap -> {
            if (userMap == null) {
                return;
            }
            ReviewsAdapter adapter = new ReviewsAdapter(userMap);
            binding.recyclerReview.setAdapter(adapter);
            binding.recyclerReview.setHasFixedSize(true);

            viewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
                if (reviews != null) {
                    adapter.submitList(reviews);
                    binding.textViewNoReview.setVisibility(reviews.isEmpty() ? View.VISIBLE : View.INVISIBLE);
                }
                binding.progressBar5.setVisibility(View.INVISIBLE);
            });
        });

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof ReviewViewModel.Event.NavigateToAddReviewScreen) {
                NavDirections navDirections = ReviewFragmentDirections.actionReviewFragmentToAddReviewFragment(viewModel.getShop());
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof ReviewViewModel.Event.ShowGeneralMessage) {
                String message = ((ReviewViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        getParentFragmentManager().setFragmentResultListener("add_review_fragment", getViewLifecycleOwner(),
                (requestKey, result) -> {
                    boolean success = result.getBoolean("success");
                    viewModel.onAddReviewResult(success);
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