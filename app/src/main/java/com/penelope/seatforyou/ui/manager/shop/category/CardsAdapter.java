package com.penelope.seatforyou.ui.manager.shop.category;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.penelope.seatforyou.databinding.CardHorizontalItemBinding;
import com.penelope.seatforyou.databinding.CardVerticalItemBinding;

public class CardsAdapter extends ListAdapter<String, RecyclerView.ViewHolder> {

    class CardHorizontalViewHolder extends RecyclerView.ViewHolder {

        private final CardHorizontalItemBinding binding;

        public CardHorizontalViewHolder(CardHorizontalItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(position);
                }
            });
        }

        public void bind(String title) {
            binding.textViewTitle.setText(title);
        }
    }

    class CardVerticalViewHolder extends RecyclerView.ViewHolder {

        private final CardVerticalItemBinding binding;

        public CardVerticalViewHolder(CardVerticalItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(position);
                }
            });
        }

        public void bind(String title) {
            binding.textViewTitle.setText(title);
        }
    }


    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    private final boolean horizontalVertical;
    private OnItemSelectedListener onItemSelectedListener;


    public CardsAdapter(boolean horizontalVertical) {
        super(new DiffUtilCallback());
        this.horizontalVertical = horizontalVertical;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        return horizontalVertical ?
                new CardHorizontalViewHolder(CardHorizontalItemBinding.inflate(layoutInflater, parent, false)) :
                new CardVerticalViewHolder(CardVerticalItemBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (horizontalVertical) {
            ((CardHorizontalViewHolder) holder).bind(getItem(position));
        } else {
            ((CardVerticalViewHolder) holder).bind(getItem(position));
        }
    }


    static class DiffUtilCallback extends DiffUtil.ItemCallback<String> {

        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    }

}
