package com.penelope.seatforyou.ui.main.search.setfilter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SetFilterViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final MutableLiveData<String> region = new MutableLiveData<>();
    private final MutableLiveData<Integer> price = new MutableLiveData<>();
    private final MutableLiveData<String> category = new MutableLiveData<>();


    @Inject
    public SetFilterViewModel(SavedStateHandle savedStateHandle) {

        String regionValue = savedStateHandle.get("region");
        Integer priceValue = savedStateHandle.get("price");
        String categoryValue = savedStateHandle.get("category");

        if (regionValue != null) {
            region.setValue(regionValue);
        }
        if (priceValue != null && priceValue != 0) {
            price.setValue(priceValue);
        }
        if (categoryValue != null) {
            category.setValue(categoryValue);
        }
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<String> getRegion() {
        return region;
    }

    public LiveData<Integer> getPrice() {
        return price;
    }

    public LiveData<String> getCategory() {
        return category;
    }


    public void onCloseClick() {
        event.setValue(new Event.NavigateBack());
    }

    public void onSetPriceClick() {
        event.setValue(new Event.PromptPrice());
    }

    public void onSetRegionClick() {
        event.setValue(new Event.PromptRegion());
    }

    public void onSetCategoryClick() {
        event.setValue(new Event.PromptCategory());
    }

    public void onClearClick() {
        region.setValue(null);
        price.setValue(null);
    }

    public void onRegionSelected(String value) {
        value = value.trim();
        if (!value.isEmpty()) {
            region.setValue(value);
        } else {
            region.setValue(null);
        }
    }

    public void onPriceSelected(String value) {
        value = value.trim();
        if (!value.isEmpty() && Integer.parseInt(value) > 0) {
            price.setValue(Integer.parseInt(value));
        } else {
            price.setValue(null);
        }
    }

    public void onCategorySelected(String value) {
        value = value.trim();
        if (!value.isEmpty()) {
            category.setValue(value);
        } else {
            category.setValue(null);
        }
    }


    public void onApplyClick() {
        event.setValue(new Event.NavigateBackWithResult(
                region.getValue(),
                price.getValue() != null ? price.getValue() : 0,
                category.getValue()
        ));
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class NavigateBack extends Event {
        }

        public static class PromptRegion extends Event {
        }

        public static class PromptPrice extends Event {
        }

        public static class PromptCategory extends Event {
        }

        public static class NavigateBackWithResult extends Event {
            public final String region;
            public final int price;
            public final String category;

            public NavigateBackWithResult(String region, int price, String category) {
                this.region = region;
                this.price = price;
                this.category = category;
            }
        }
    }

}