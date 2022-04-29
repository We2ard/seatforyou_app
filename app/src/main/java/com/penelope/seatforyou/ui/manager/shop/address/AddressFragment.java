package com.penelope.seatforyou.ui.manager.shop.address;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.data.address.Address;
import com.penelope.seatforyou.databinding.FragmentAddressBinding;
import com.penelope.seatforyou.utils.ui.OnTextChangeListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddressFragment extends DialogFragment {

    private FragmentAddressBinding binding;
    private AddressViewModel viewModel;


    public AddressFragment() {
        super(R.layout.fragment_address);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentAddressBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        binding.editTextAddress.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onQueryChange(text);
            }
        });

        binding.editTextAddressDetail.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onAddressDetailChange(text);
            }
        });

        if (viewModel.getOldAddress() != null) {
            binding.editTextAddress.setText(viewModel.getOldAddress().getLoadAddress());
            binding.editTextAddressDetail.setText(viewModel.getOldAddress().getDetail());
        }

        AddressAdapter addressAdapter = new AddressAdapter();
        binding.recyclerAddress.setAdapter(addressAdapter);
        binding.recyclerAddress.setHasFixedSize(true);

        addressAdapter.setOnItemSelectedListener(position -> {
            Address address = addressAdapter.getCurrentList().get(position);
            viewModel.onAddressClick(address);
        });

        viewModel.getAddresses().observe(getViewLifecycleOwner(), addresses -> {
            if (addresses != null) {
                addressAdapter.submitList(addresses);
                binding.textViewNoAddress.setVisibility(addresses.isEmpty() ? View.VISIBLE : View.INVISIBLE);
            }
            binding.progressBar.setVisibility(View.INVISIBLE);
        });

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof AddressViewModel.Event.NavigateBackWithResult) {
                Address address = ((AddressViewModel.Event.NavigateBackWithResult) event).address;
                Bundle result = new Bundle();
                result.putSerializable("address", address);
                getParentFragmentManager().setFragmentResult("address_fragment", result);
                NavHostFragment.findNavController(this).popBackStack();
            } else if (event instanceof AddressViewModel.Event.ShowLoadingUI) {
                binding.progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}