package com.penelope.seatforyou.ui.manager.shop.openclose;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class OpenCloseViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final MutableLiveData<Integer> openHour = new MutableLiveData<>(8);
    private final MutableLiveData<Integer> openMinute = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> closeHour = new MutableLiveData<>(20);
    private final MutableLiveData<Integer> closeMinute = new MutableLiveData<>(0);


    @Inject
    public OpenCloseViewModel(SavedStateHandle savedStateHandle) {
        Integer oldOpenHour = savedStateHandle.get("open_hour");
        Integer oldOpenMinute = savedStateHandle.get("open_minute");
        Integer oldCloseHour = savedStateHandle.get("close_hour");
        Integer oldCloseMinute = savedStateHandle.get("close_minute");
        if (oldOpenHour != null && oldOpenMinute != null && oldCloseHour != null && oldCloseMinute != null) {
            openHour.setValue(oldOpenHour);
            openMinute.setValue(oldOpenMinute);
            closeHour.setValue(oldCloseHour);
            closeMinute.setValue(oldCloseMinute);
        }
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<Integer> getOpenHour() {
        return openHour;
    }

    public LiveData<Integer> getOpenMinute() {
        return openMinute;
    }

    public LiveData<Integer> getCloseHour() {
        return closeHour;
    }

    public LiveData<Integer> getCloseMinute() {
        return closeMinute;
    }


    public void onOpenTimeClick() {

        Integer openHourValue = openHour.getValue();
        Integer openMinuteValue = openMinute.getValue();
        assert openHourValue != null && openMinuteValue != null;

        event.setValue(new Event.PromptTime(true, openHourValue, openMinuteValue));
    }

    public void onCloseTimeClick() {

        Integer closeHourValue = closeHour.getValue();
        Integer closeMinuteValue = closeMinute.getValue();
        assert closeHourValue != null && closeMinuteValue != null;

        event.setValue(new Event.PromptTime(false, closeHourValue, closeMinuteValue));
    }

    public void onOpenTimeSelected(int hour, int minute) {
        openHour.setValue(hour);
        openMinute.setValue(minute);
    }

    public void onCloseTimeSelected(int hour, int minute) {
        closeHour.setValue(hour);
        closeMinute.setValue(minute);
    }

    public void onConfirmClick() {
        Integer openHourValue = openHour.getValue();
        Integer openMinuteValue = openMinute.getValue();
        Integer closeHourValue = closeHour.getValue();
        Integer closeMinuteValue = closeMinute.getValue();
        assert openHourValue != null && openMinuteValue != null
                && closeHourValue != null && closeMinuteValue != null;

        event.setValue(new Event.NavigateBackWithResult(
                openHourValue, openMinuteValue,
                closeHourValue, closeMinuteValue)
        );
    }


    public static class Event {

        public static class NavigateBackWithResult extends Event {
            public final int openHour;
            public final int openMinute;
            public final int closeHour;
            public final int closeMinute;

            public NavigateBackWithResult(int openHour, int openMinute, int closeHour, int closeMinute) {
                this.openHour = openHour;
                this.openMinute = openMinute;
                this.closeHour = closeHour;
                this.closeMinute = closeMinute;
            }
        }

        public static class PromptTime extends Event {
            public boolean openClose;
            public final int hour;
            public final int minute;

            public PromptTime(boolean openClose, int hour, int minute) {
                this.openClose = openClose;
                this.hour = hour;
                this.minute = minute;
            }
        }
    }

}

