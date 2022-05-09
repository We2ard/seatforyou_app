package com.penelope.seatforyou.ui.main.auth.inquireregister;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class InquireRegisterViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();


    @Inject
    public InquireRegisterViewModel() {

    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }


    public void onRegisterClick() {
        event.setValue(new Event.NavigateToRegisterScreen());
    }


    public static class Event {

        public static class NavigateToRegisterScreen extends Event {
        }
    }

}