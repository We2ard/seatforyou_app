package com.penelope.seatforyou.ui.main.search.setdatetime;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SetDateTimeViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final MutableLiveData<LocalDate> date = new MutableLiveData<>();
    private final MutableLiveData<LocalTime> time = new MutableLiveData<>();


    @Inject
    public SetDateTimeViewModel(SavedStateHandle savedStateHandle) {
        LocalDateTime dateTime = savedStateHandle.get("date_time");
        if (dateTime != null) {
            date.setValue(dateTime.toLocalDate());
            time.setValue(dateTime.toLocalTime());
        }
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<LocalDate> getDate() {
        return date;
    }

    public LiveData<LocalTime> getTime() {
        return time;
    }


    public void onDateClick() {
        event.setValue(new Event.PromptDate());
    }

    public void onTimeClick() {
        event.setValue(new Event.PromptTime());
    }

    public void onOkClick() {

        LocalDate dateValue = date.getValue();
        LocalTime timeValue = time.getValue();

        if (dateValue == null || timeValue == null) {
            event.setValue(new Event.ShowGeneralMessage("모두 입력해주세요"));
            return;
        }

        LocalDateTime dateTime = LocalDateTime.of(dateValue, timeValue);
        event.setValue(new Event.NavigateBackWithResult(dateTime));
    }

    public void onDateSelected(int year, int month, int dayOfMonth) {
        date.setValue(LocalDate.of(year, month, dayOfMonth));
    }

    public void onTimeSelected(int hour, int minute) {
        time.setValue(LocalTime.of(hour, minute));
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class NavigateBackWithResult extends Event {
            public final LocalDateTime dateTime;
            public NavigateBackWithResult(LocalDateTime dateTime) {
                this.dateTime = dateTime;
            }
        }

        public static class PromptDate extends Event {
        }

        public static class PromptTime extends Event {
        }
    }

}