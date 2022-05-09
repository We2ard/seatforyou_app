package com.penelope.seatforyou.ui.main.detail.picture;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.penelope.seatforyou.data.shop.Shop;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PictureViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final Shop shop;


    @Inject
    public PictureViewModel(SavedStateHandle savedStateHandle) {
        shop = savedStateHandle.get("shop");
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public Shop getShop() {
        return shop;
    }


    public static class Event {

    }

}