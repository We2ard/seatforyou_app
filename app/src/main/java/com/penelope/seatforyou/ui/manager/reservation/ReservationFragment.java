package com.penelope.seatforyou.ui.manager.reservation;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.R;
import com.penelope.seatforyou.data.reservation.Reservation;
import com.penelope.seatforyou.databinding.FragmentReservationBinding;
import com.penelope.seatforyou.utils.ui.AuthListenerFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReservationFragment extends AuthListenerFragment {

    private FragmentReservationBinding binding;
    private ReservationViewModel viewModel;


    public ReservationFragment() {
        super(R.layout.fragment_reservation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentReservationBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        binding.fabAddReservation.setOnClickListener(v -> viewModel.onAddClick());

        viewModel.getUserMap().observe(getViewLifecycleOwner(), userMap -> {
            if (userMap == null) {
                return;
            }
            ReservationsAdapter adapter = new ReservationsAdapter(userMap);
            binding.recyclerReservation.setAdapter(adapter);

            adapter.setOnItemSelectedListener(new ReservationsAdapter.OnItemSelectedListener() {
                @Override
                public void onItemSelected(int position) {
                }

                @Override
                public void onEditClick(int position) {
                    Reservation reservation = adapter.getCurrentList().get(position);
                    viewModel.onEditClick(reservation);
                }

                @Override
                public void onDeleteClick(int position) {
                    Reservation reservation = adapter.getCurrentList().get(position);
                    viewModel.onDeleteClick(reservation);
                }
            });

            viewModel.getReservations().observe(getViewLifecycleOwner(), reservations -> {
                if (reservations != null) {
                    adapter.submitList(reservations);
                    binding.textViewNoReservations.setVisibility(reservations.isEmpty() ? View.VISIBLE : View.INVISIBLE);
                }
                binding.progressBar9.setVisibility(View.INVISIBLE);
            });
        });

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof ReservationViewModel.Event.ShowGeneralMessage) {
                String message = ((ReservationViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            } else if (event instanceof ReservationViewModel.Event.ConfirmDelete) {
                Reservation reservation = ((ReservationViewModel.Event.ConfirmDelete) event).reservation;
                new AlertDialog.Builder(requireContext())
                        .setTitle("예약 삭제")
                        .setMessage("예약을 삭제하시겠습니까?")
                        .setPositiveButton("삭제", (dialog, which) -> viewModel.onDeleteConfirm(reservation))
                        .setNegativeButton("취소", null)
                        .show();
            } else if (event instanceof ReservationViewModel.Event.NavigateToEditReservationScreen) {
                String shopId = ((ReservationViewModel.Event.NavigateToEditReservationScreen) event).shopId;
                Reservation reservation = ((ReservationViewModel.Event.NavigateToEditReservationScreen) event).reservation;
                NavDirections navDirection = ReservationFragmentDirections.actionReservationFragmentToAddEditReservationFragment(reservation, shopId);
                Navigation.findNavController(requireView()).navigate(navDirection);
            } else if (event instanceof ReservationViewModel.Event.NavigateToAddReservationScreen) {
                String shopId = ((ReservationViewModel.Event.NavigateToAddReservationScreen) event).shopId;
                NavDirections navDirections = ReservationFragmentDirections.actionReservationFragmentToAddEditReservationFragment(null, shopId);
                Navigation.findNavController(requireView()).navigate(navDirections);
            }
        });

        getParentFragmentManager().setFragmentResultListener("add_edit_reservation_fragment",
                getViewLifecycleOwner(),
                (requestKey, result) -> {
                    boolean addOrEdit = result.getBoolean("add_or_edit");
                    viewModel.onAddEditResult(addOrEdit);
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