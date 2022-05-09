package com.penelope.seatforyou.ui.manager.reservation;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.penelope.seatforyou.data.reservation.Reservation;
import com.penelope.seatforyou.data.user.User;
import com.penelope.seatforyou.databinding.ReservationItemBinding;
import com.penelope.seatforyou.utils.StringUtils;
import com.penelope.seatforyou.utils.TimeUtils;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

public class ReservationsAdapter extends ListAdapter<Reservation, ReservationsAdapter.ReservationViewHolder> {

    class ReservationViewHolder extends RecyclerView.ViewHolder {

        private final ReservationItemBinding binding;

        public ReservationViewHolder(ReservationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(position);
                }
            });

            binding.buttonEditReservation.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemSelectedListener != null) {
                    onItemSelectedListener.onEditClick(position);
                }
            });

            binding.buttonDeleteReservation.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemSelectedListener != null) {
                    onItemSelectedListener.onDeleteClick(position);
                }
            });
        }

        public void bind(Reservation model) {

            User user = userMap.get(model.getUid());

            if (user != null) {
                String strNamePhone = String.format(Locale.getDefault(), "%s, %s",
                        user.getName(), StringUtils.phone(user.getPhone()));
                binding.textViewReservationNamePhone.setText(strNamePhone);

                LocalDate date = TimeUtils.toDate(user.getCreated());
                String strRegistered = "가입 " + TimeUtils.formatDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
                binding.textViewReservationRegisterDateTime.setText(strRegistered);
            } else {
                binding.textViewReservationNamePhone.setText("");
                binding.textViewReservationRegisterDateTime.setText("");
            }

            String strPersonNumber = String.format(Locale.getDefault(), "%d명", model.getPersonNumber());
            binding.textViewReservationPersonNumber.setText(strPersonNumber);

            String strDate = TimeUtils.formatDateTime(model.getMonth(),
                    model.getDayOfMonth(), model.getHour(), model.getMinute());
            binding.textViewReservationDateTime.setText(strDate);
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    private OnItemSelectedListener onItemSelectedListener;
    private final Map<String, User> userMap;


    public ReservationsAdapter(Map<String, User> map) {
        super(new DiffUtilCallback());
        userMap = map;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ReservationItemBinding binding = ReservationItemBinding.inflate(layoutInflater, parent, false);
        return new ReservationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    static class DiffUtilCallback extends DiffUtil.ItemCallback<Reservation> {

        @Override
        public boolean areItemsTheSame(@NonNull Reservation oldItem, @NonNull Reservation newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reservation oldItem, @NonNull Reservation newItem) {
            return oldItem.equals(newItem);
        }
    }

}