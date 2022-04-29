package com.penelope.seatforyou.ui.manager.shop.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CategoryViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final MutableLiveData<List<String>> categories = new MutableLiveData<>();

    private String categoryInput = "";


    @Inject
    public CategoryViewModel(SavedStateHandle savedStateHandle) {

        List<String> oldCategories = savedStateHandle.get("categories");
        categories.setValue(oldCategories == null ? new ArrayList<>() : oldCategories);
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<List<String>> getCategories() {
        return categories;
    }


    public void onCategoryInputChange(String text) {
        categoryInput = text.trim();
    }

    public void onAddCategoryClick() {

        if (categoryInput.isEmpty()) {
            event.setValue(new Event.ShowGeneralMessage("카테고리 이름을 입력해주세요"));
            return;
        }

        List<String> oldList = categories.getValue();
        assert oldList != null;

        List<String> newList = new ArrayList<>(oldList);
        newList.add(categoryInput);

        categories.setValue(newList);
    }

    public void onCategoryClick(String category) {

        event.setValue(new Event.ConfirmDeleteCategory(category));
    }

    public void onDeleteConfirm(String category) {

        List<String> oldList = categories.getValue();
        assert oldList != null;

        List<String> newList = new ArrayList<>(oldList);
        newList.remove(category);

        categories.setValue(newList);
    }

    public void onConfirmClick() {

        List<String> categoriesValue = categories.getValue();
        assert categoriesValue != null;

        if (categoriesValue.isEmpty()) {
            event.setValue(new Event.ShowGeneralMessage("하나 이상의 카테고리를 입력하세요"));
            return;
        }

        event.setValue(new Event.NavigateBackWithResult(categoriesValue));
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class ConfirmDeleteCategory extends Event {
            public final String category;
            public ConfirmDeleteCategory(String category) {
                this.category = category;
            }
        }

        public static class NavigateBackWithResult extends Event {
            public final List<String> categories;
            public NavigateBackWithResult(List<String> categories) {
                this.categories = categories;
            }
        }
    }

}