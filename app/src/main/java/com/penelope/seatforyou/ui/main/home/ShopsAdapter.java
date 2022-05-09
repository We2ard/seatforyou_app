package com.penelope.seatforyou.ui.main.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.databinding.ShopItemBinding;
import com.penelope.seatforyou.utils.ImageUtils;

public class ShopsAdapter extends ListAdapter<Shop, ShopsAdapter.ShopViewHolder> {

    class ShopViewHolder extends RecyclerView.ViewHolder {

        private final ShopItemBinding binding;

        public ShopViewHolder(ShopItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(position);
                }
            });
        }

        public void bind(Shop model) {

            binding.textViewShopName.setText(model.getName());
            binding.textViewShopDescription.setText(model.getDescription());

            glide.load(ImageUtils.getShopImageUrl(model.getUid()))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.imageViewShop);
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    private OnItemSelectedListener onItemSelectedListener;
    private final RequestManager glide;


    public ShopsAdapter(RequestManager glide) {
        super(new DiffUtilCallback());
        this.glide = glide;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.onItemSelectedListener = listener;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ShopItemBinding binding = ShopItemBinding.inflate(layoutInflater, parent, false);
        return new ShopViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    static class DiffUtilCallback extends DiffUtil.ItemCallback<Shop> {

        @Override
        public boolean areItemsTheSame(@NonNull Shop oldItem, @NonNull Shop newItem) {
            return oldItem.getUid().equals(newItem.getUid());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Shop oldItem, @NonNull Shop newItem) {
            return oldItem.equals(newItem);
        }
    }

}