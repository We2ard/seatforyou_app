package com.penelope.seatforyou.ui.main.detail.menulist;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.penelope.seatforyou.data.shop.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MenuListViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final Shop shop;


    @Inject
    public MenuListViewModel(SavedStateHandle savedStateHandle) {
        shop = savedStateHandle.get("shop");
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public Shop getShop() {
        return shop;
    }

    public List<Pair<String, Integer>> getMenuList() {
        List<Pair<String, Integer>> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : shop.getMenus().entrySet()) {
            list.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        return list;
    }


    public static class Event {

    }

}