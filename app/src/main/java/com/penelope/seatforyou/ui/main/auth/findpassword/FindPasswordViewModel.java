package com.penelope.seatforyou.ui.main.auth.findpassword;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.penelope.seatforyou.data.user.UserRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FindPasswordViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private String phone = "";
    private String name = "";

    private final UserRepository userRepository;


    @Inject
    public FindPasswordViewModel(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }


    public void onPhoneChange(String text) {
        phone = text.trim();
    }

    public void onNameChange(String text) {
        name = text.trim();
    }

    public void onSearchClick() {

        if (phone.isEmpty() || name.isEmpty()) {
            event.setValue(new Event.ShowGeneralMessage("모두 입력해주세요"));
            return;
        }

        userRepository.findUserByPhoneAndName(phone, name,
                user -> {
                    if (user != null) {
                        event.setValue(new Event.ShowPassword(user.getPassword()));
                    } else {
                        event.setValue(new Event.ShowGeneralMessage("가입되지 않은 정보입니다"));
                    }
                },
                e -> {
                    e.printStackTrace();
                    event.setValue(new Event.ShowGeneralMessage("네트워크를 확인해주세요"));
                });
    }

    public void onLoginClick() {
        event.setValue(new Event.NavigateBack());
    }



    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class ShowPassword extends Event {
            public final String password;
            public ShowPassword(String password) {
                this.password = password;
            }
        }
        
        public static class NavigateBack extends Event {
        }
    }

}