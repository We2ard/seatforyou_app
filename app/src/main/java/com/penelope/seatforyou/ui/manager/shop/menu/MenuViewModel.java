package com.penelope.seatforyou.ui.manager.shop.menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MenuViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final MutableLiveData<Map<String, Integer>> menus = new MutableLiveData<>();

    private String menuName = "";
    private int menuPrice = 0;


    @Inject
    public MenuViewModel(SavedStateHandle savedStateHandle) {

        Map<String, Integer> menus = savedStateHandle.get("menus");
        this.menus.setValue(menus == null ? new HashMap<>() : menus);

    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<Map<String, Integer>> getMenus() {
        return menus;
    }


    public void onNameChange(String text) {
        menuName = text.trim();
    }

    public void onPriceChange(String text) {
        try {
            menuPrice = Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void onAddMenuClick() {

        if (menuName.isEmpty() || menuPrice == 0) {
            event.setValue(new Event.ShowGeneralMessage("메뉴 정보를 정확히 입력해주세요"));
            return;
        }

        Map<String, Integer> oldMenus = menus.getValue();
        assert oldMenus != null;

        if (oldMenus.containsKey(menuName)) {
            event.setValue(new Event.ShowGeneralMessage("이미 존재하는 메뉴입니다"));
            return;
        }

        Map<String, Integer> newMenus = new HashMap<>(oldMenus);
        newMenus.put(menuName, menuPrice);

        menus.setValue(newMenus);
    }

    public void onMenuClick(String menuName) {
        event.setValue(new Event.ConfirmDeleteMenu(menuName));
    }

    public void onDeleteMenuConfirm(String menuName) {

        Map<String, Integer> oldMenus = menus.getValue();
        assert oldMenus != null;

        Map<String, Integer> newMenus = new HashMap<>(oldMenus);
        newMenus.remove(menuName);

        menus.setValue(newMenus);
    }

    public void onConfirmClick() {

        Map<String, Integer> menusValue = menus.getValue();
        assert menusValue != null;

        if (menusValue.isEmpty()) {
            event.setValue(new Event.ShowGeneralMessage("메뉴를 입력해주세요"));
            return;
        }

        List<String> menuNames = new ArrayList<>();
        List<Integer> menuPrices = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : menusValue.entrySet()) {
            menuNames.add(entry.getKey());
            menuPrices.add(entry.getValue());
        }

        event.setValue(new Event.NavigateBackWithResult(menuNames, menuPrices));
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class ConfirmDeleteMenu extends Event {
            public final String menu;
            public ConfirmDeleteMenu(String menu) {
                this.menu = menu;
            }
        }

        public static class NavigateBackWithResult extends Event {
            public final List<String> menuNames;
            public final List<Integer> menuPrices;
            public NavigateBackWithResult(List<String> menuNames, List<Integer> menuPrices) {
                this.menuNames = menuNames;
                this.menuPrices = menuPrices;
            }
        }
    }

}