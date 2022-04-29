package com.penelope.seatforyou.ui.manager.shop.menu;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.databinding.FragmentMenuBinding;
import com.penelope.seatforyou.ui.manager.shop.category.CardsAdapter;
import com.penelope.seatforyou.utils.StringUtils;
import com.penelope.seatforyou.utils.ui.OnTextChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MenuFragment extends DialogFragment {

    private FragmentMenuBinding binding;
    private MenuViewModel viewModel;


    public MenuFragment() {
        super(R.layout.fragment_menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentMenuBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        binding.editTextMenuName.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onNameChange(text);
            }
        });
        binding.editTextMenuPrice.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onPriceChange(text);
            }
        });
        binding.imageViewAddMenu.setOnClickListener(v -> {
            viewModel.onAddMenuClick();
            binding.editTextMenuName.setText("");
            binding.editTextMenuPrice.setText("");
        });
        binding.buttonConfirm.setOnClickListener(v -> viewModel.onConfirmClick());

        CardsAdapter adapter = new CardsAdapter(false);
        binding.recyclerMenu.setAdapter(adapter);

        adapter.setOnItemSelectedListener(position -> {
            String menuName = adapter.getCurrentList().get(position);
            viewModel.onMenuClick(menuName);
        });

        viewModel.getMenus().observe(getViewLifecycleOwner(), menus -> {
            List<String> menuStrings = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : menus.entrySet()) {
                String menuName = entry.getKey();
                String menuPrice = StringUtils.price(entry.getValue());
                menuStrings.add(menuName + " " + menuPrice);
            }
            adapter.submitList(menuStrings);
        });

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof MenuViewModel.Event.ShowGeneralMessage) {
                String message = ((MenuViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            } else if (event instanceof MenuViewModel.Event.ConfirmDeleteMenu) {
                String menuName = ((MenuViewModel.Event.ConfirmDeleteMenu) event).menu;
                confirmDeleteMenu(menuName);
            } else if (event instanceof MenuViewModel.Event.NavigateBackWithResult) {
                List<String> menuNames = ((MenuViewModel.Event.NavigateBackWithResult) event).menuNames;
                List<Integer> menuPrices = ((MenuViewModel.Event.NavigateBackWithResult) event).menuPrices;
                Bundle result = new Bundle();
                result.putStringArrayList("menu_names", new ArrayList<>(menuNames));
                result.putIntegerArrayList("menu_prices", new ArrayList<>(menuPrices));
                getParentFragmentManager().setFragmentResult("menu_fragment", result);
                NavHostFragment.findNavController(this).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void confirmDeleteMenu(String menuName) {

        new AlertDialog.Builder(requireContext())
                .setTitle("메뉴 삭제")
                .setMessage(menuName + "를 삭제하시겠습니까?")
                .setPositiveButton("삭제", (dialog, which) -> viewModel.onDeleteMenuConfirm(menuName))
                .setNegativeButton("취소", null)
                .show();
    }

}