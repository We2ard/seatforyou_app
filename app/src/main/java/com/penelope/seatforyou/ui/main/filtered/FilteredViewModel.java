package com.penelope.seatforyou.ui.main.filtered;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.penelope.seatforyou.data.shop.Shop;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FilteredViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final List<Shop> shops;
    private final String description;


    @Inject
    public FilteredViewModel(SavedStateHandle savedStateHandle) {
        shops = savedStateHandle.get("shops");
        description = savedStateHandle.get("description");
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public List<Shop> getShops() {
        return shops;
    }

    public String getDescription() {
        return description;
    }


    public void onShopClick(Shop shop) {
        event.setValue(new Event.NavigateToDetailScreen(shop));
    }


    public static class Event {

        public static class NavigateToDetailScreen extends Event {
            public final Shop shop;
            public NavigateToDetailScreen(Shop shop) {
                this.shop = shop;
            }
        }
    }

}