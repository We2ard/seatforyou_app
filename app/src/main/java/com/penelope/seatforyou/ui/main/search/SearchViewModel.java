package com.penelope.seatforyou.ui.main.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SearchViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();


    @Inject
    public SearchViewModel() {

    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }


    public static class Event {

    }

}