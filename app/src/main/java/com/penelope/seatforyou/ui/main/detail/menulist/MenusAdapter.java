package com.penelope.seatforyou.ui.main.detail.menulist;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.penelope.seatforyou.databinding.MenuItemBinding;
import com.penelope.seatforyou.utils.StringUtils;

public class MenusAdapter extends ListAdapter<Pair<String, Integer>, MenusAdapter.MenuViewHolder> {

    class MenuViewHolder extends RecyclerView.ViewHolder {

        private final MenuItemBinding binding;

        public MenuViewHolder(MenuItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(position);
                }
            });
        }

        public void bind(Pair<String, Integer> model) {
            binding.textViewMenuName.setText(model.first);
            binding.textViewMenuPrice.setText(StringUtils.price(model.second));
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    private OnItemSelectedListener onItemSelectedListener;


    public MenusAdapter() {
        super(new DiffUtilCallback());
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MenuItemBinding binding = MenuItemBinding.inflate(layoutInflater, parent, false);
        return new MenuViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    static class DiffUtilCallback extends DiffUtil.ItemCallback<Pair<String, Integer>> {

        @Override
        public boolean areItemsTheSame(@NonNull Pair<String, Integer> oldItem, @NonNull Pair<String, Integer> newItem) {
            return oldItem.first.equals(newItem.first);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Pair<String, Integer> oldItem, @NonNull Pair<String, Integer> newItem) {
            return oldItem.first.equals(newItem.first) && oldItem.second.equals(newItem.second);
        }
    }

}