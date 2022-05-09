package com.penelope.seatforyou.ui.main.detail.addreview;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentAddReviewBinding;
import com.penelope.seatforyou.utils.ui.AuthListenerFragment;
import com.penelope.seatforyou.utils.ui.OnTextChangeListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddReviewFragment extends AuthListenerFragment {

    private FragmentAddReviewBinding binding;
    private AddReviewViewModel viewModel;


    public AddReviewFragment() {
        super(R.layout.fragment_add_review);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentAddReviewBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(AddReviewViewModel.class);

        binding.editTextReviewSummary.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onSummaryChange(text);
            }
        });
        binding.editTextReviewDetail.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onDetailChange(text);
            }
        });
        binding.ratingBarReview.setOnRatingBarChangeListener((ratingBar, rating, fromUser) ->
                viewModel.onRatingChange(rating));

        binding.fabSubmitReview.setOnClickListener(v -> viewModel.onSubmitClick());

        viewModel.isUploadInProgress().observe(getViewLifecycleOwner(), isUploadInProgress ->
                binding.progressBar6.setVisibility(isUploadInProgress ? View.VISIBLE : View.INVISIBLE));

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof AddReviewViewModel.Event.NavigateBackWithResult) {
                boolean success = ((AddReviewViewModel.Event.NavigateBackWithResult) event).success;
                Bundle result = new Bundle();
                result.putBoolean("success", success);
                getParentFragmentManager().setFragmentResult("add_review_fragment", result);
                Navigation.findNavController(requireView()).popBackStack();
            } else if (event instanceof AddReviewViewModel.Event.ShowGeneralMessage) {
                String message = ((AddReviewViewModel.Event.ShowGeneralMessage) event).message;
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