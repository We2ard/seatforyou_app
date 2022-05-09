package com.penelope.seatforyou.ui.main.detail.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.data.user.UserRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DetailViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private String uid;

    private final Shop shop;

    private final UserRepository userRepository;


    @Inject
    public DetailViewModel(SavedStateHandle savedStateHandle, UserRepository userRepository) {

        this.shop = savedStateHandle.get("shop");

        this.userRepository = userRepository;
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public Shop getShop() {
        return shop;
    }


    public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
        uid = firebaseAuth.getUid();
    }

    public void onBackClick() {
        event.setValue(new Event.NavigateBack());
    }

    public void onCloseClick() {
        event.setValue(new Event.NavigateBack());
    }

    public void onPhoneClick() {
        event.setValue(new Event.DialPhone(shop.getPhone()));
    }

    public void onZzimClick() {
        if (uid != null) {
            userRepository.addDib(uid, shop.getUid(),
                    unused -> event.setValue(new Event.ShowGeneralMessage("찜 목록에 추가되었습니다")),
                    e -> event.setValue(new Event.ShowGeneralMessage("찜 목록에 추가하지 못했습니다"))
            );
        } else {
            event.setValue(new Event.ShowGeneralMessage("비회원은 찜 기능을 이용할 수 없습니다"));
        }
    }

    public void onReserveClick() {
        event.setValue(new Event.NavigateToReserveScreen(shop));
    }

    public void onReserveResult(boolean success) {
        if (success) {
            event.setValue(new Event.ShowGeneralMessage("예약이 완료되었습니다"));
        }
    }


    public static class Event {

        public static class DialPhone extends Event {
            public final String phone;
            public DialPhone(String phone) {
                this.phone = phone;
            }
        }

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class NavigateToReserveScreen extends Event {
            public final Shop shop;
            public NavigateToReserveScreen(Shop shop) {
                this.shop = shop;
            }
        }

        public static class NavigateBack extends Event {
        }

    }

}