package com.penelope.seatforyou.ui.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();


    @Inject
    public HomeViewModel() {

    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }


    public static class Event {

    }

}