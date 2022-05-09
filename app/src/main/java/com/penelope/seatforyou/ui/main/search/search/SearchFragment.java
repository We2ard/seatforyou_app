package com.penelope.seatforyou.ui.main.search.search;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.databinding.FragmentSearchBinding;
import com.penelope.seatforyou.utils.TimeUtils;
import com.penelope.seatforyou.utils.ui.OnTextChangeListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;


    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentSearchBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding.editTextQuery.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onQueryChange(text);
            }
        });
        binding.buttonSetDateTime.setOnClickListener(v -> viewModel.onSetDateTimeClick());
        binding.buttonClearQuery.setOnClickListener(v -> viewModel.onClearQueryClick());
        binding.textViewFilter.setOnClickListener(v -> viewModel.onSetFilterClick());
        binding.buttonSearch.setOnClickListener(v -> viewModel.onSearchClick());

        viewModel.getDateTime().observe(getViewLifecycleOwner(), dateTime -> {
            if (dateTime != null) {
                String strDateTime = TimeUtils.formatDateTime(dateTime);
                binding.textViewDateTime.setText(strDateTime);
            } else {
                binding.textViewDateTime.setText("");
            }
        });

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof SearchViewModel.Event.ShowGeneralMessage) {
                String message = ((SearchViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            } else if (event instanceof SearchViewModel.Event.ClearQueryUI) {
                binding.editTextQuery.setText("");
            } else if (event instanceof SearchViewModel.Event.NavigateToSetFilterScreen) {
                String region = ((SearchViewModel.Event.NavigateToSetFilterScreen) event).region;
                int price = ((SearchViewModel.Event.NavigateToSetFilterScreen) event).price;
                String category = ((SearchViewModel.Event.NavigateToSetFilterScreen) event).category;
                NavDirections navDirections = SearchFragmentDirections.actionSearchFragmentToSetFilterFragment(region, price, category);
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof SearchViewModel.Event.NavigateToSetDateTimeScreen) {
                LocalDateTime dateTime = ((SearchViewModel.Event.NavigateToSetDateTimeScreen) event).dateTime;
                NavDirections navDirections = SearchFragmentDirections.actionSearchFragmentToSetDateTimeFragment(dateTime);
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof SearchViewModel.Event.NavigateToFilteredScreen) {
                List<Shop> shops = ((SearchViewModel.Event.NavigateToFilteredScreen) event).shops;
                String description = ((SearchViewModel.Event.NavigateToFilteredScreen) event).description;
                NavDirections navDirections = SearchFragmentDirections.actionGlobalFilteredFragment(
                        new ArrayList<>(shops), description);
                Navigation.findNavController(requireView()).navigate(navDirections);
            }
        });

        getParentFragmentManager().setFragmentResultListener("set_date_time_fragment", getViewLifecycleOwner(),
                (requestKey, result) -> {
                    LocalDateTime dateTime = (LocalDateTime) result.get("date_time");
                    viewModel.onDateTimeResult(dateTime);
                });

        getParentFragmentManager().setFragmentResultListener("set_filter_fragment", getViewLifecycleOwner(),
                (requestKey, result) -> {
                    String region = result.getString("region");
                    int price = result.getInt("price");
                    String category = result.getString("category");
                    viewModel.onFilterResult(region, price, category);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}