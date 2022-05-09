package com.penelope.seatforyou.utils.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.penelope.seatforyou.databinding.BooleanItemBinding;

public class BooleansAdapter extends ListAdapter<Boolean, BooleansAdapter.BooleanViewHolder> {

    class BooleanViewHolder extends RecyclerView.ViewHolder {

        private final BooleanItemBinding binding;

        public BooleanViewHolder(BooleanItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(position);
                }
            });
        }

        public void bind(Boolean model) {
            if (model) {
                binding.getRoot().setCardBackgroundColor(0xFF777777);
            } else {
                binding.getRoot().setCardBackgroundColor(0xFFCCCCCC);
            }
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    private OnItemSelectedListener onItemSelectedListener;


    public BooleansAdapter() {
        super(new DiffUtilCallback());
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }

    @NonNull
    @Override
    public BooleanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        BooleanItemBinding binding = BooleanItemBinding.inflate(layoutInflater, parent, false);
        return new BooleanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BooleanViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    static class DiffUtilCallback extends DiffUtil.ItemCallback<Boolean> {

        @Override
        public boolean areItemsTheSame(@NonNull Boolean oldItem, @NonNull Boolean newItem) {
            return true;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Boolean oldItem, @NonNull Boolean newItem) {
            return false;
        }
    }

}