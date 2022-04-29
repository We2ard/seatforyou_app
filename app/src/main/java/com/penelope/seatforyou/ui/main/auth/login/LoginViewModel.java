package com.penelope.seatforyou.ui.main.auth.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.data.user.UserRepository;
import com.penelope.seatforyou.utils.AuthUtils;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private String phone = "";
    private String password = "";

    private final FirebaseAuth auth;
    private final UserRepository userRepository;


    @Inject
    public LoginViewModel(FirebaseAuth firebaseAuth, UserRepository userRepository) {
        this.auth = firebaseAuth;
        this.userRepository = userRepository;
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }


    public void onPhoneChange(String text) {
        phone = text.trim();
    }

    public void onPasswordChange(String text) {
        password = text.trim();
    }

    public void onLoginClick() {

        if (phone.isEmpty() || password.isEmpty()) {
            event.setValue(new Event.ShowGeneralMessage("모두 입력해주세요"));
            return;
        }

        auth.signInWithEmailAndPassword(AuthUtils.emailize(phone), password)
                .addOnFailureListener(e -> event.setValue(
                        new Event.ShowGeneralMessage("회원정보를 확인해주세요"))
                );
    }

    public void onRegisterClick() {
        event.setValue(new Event.NavigateToRegisterScreen());
    }

    public void onFindPasswordClick() {
        event.setValue(new Event.NavigateToFindPasswordScreen());
    }

    public void onRegisterInquiryClick() {
        event.setValue(new Event.NavigateToInquireRegisterScreen());
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;

            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class NavigateToRegisterScreen extends Event {
        }

        public static class NavigateToFindPasswordScreen extends Event {
        }

        public static class NavigateToInquireRegisterScreen extends Event {
        }
    }

}