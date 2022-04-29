package com.penelope.seatforyou.ui.manager.manager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManagerViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final FirebaseAuth firebaseAuth;


    @Inject
    public ManagerViewModel(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }


    public void onShopClick() {
        event.setValue(new Event.NavigateToShopScreen());
    }

    public void onReservationClick() {
        event.setValue(new Event.NavigateToReservationScreen());
    }

    public void onLogoutClick() {
        event.setValue(new Event.ConfirmLogout());
    }

    public void onBackClick() {
        event.setValue(new Event.ConfirmLogout());;
    }

    public void onLogoutConfirm() {
        firebaseAuth.signOut();
    }

    public void onShopFragmentResult(boolean enrollOrModify) {
        if (enrollOrModify) {
            event.setValue(new Event.ShowGeneralMessage("가게 정보가 등록되었습니다"));
        } else {
            event.setValue(new Event.ShowGeneralMessage("가게 정보가 수정되었습니다"));
        }
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class NavigateToShopScreen extends Event {
        }

        public static class NavigateToReservationScreen extends Event {
        }

        public static class ConfirmLogout extends Event {
        }
    }

}