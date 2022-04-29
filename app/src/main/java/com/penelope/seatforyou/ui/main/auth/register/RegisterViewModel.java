package com.penelope.seatforyou.ui.main.auth.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.data.user.User;
import com.penelope.seatforyou.data.user.UserRepository;
import com.penelope.seatforyou.utils.AuthUtils;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private String name = "";
    private String phone = "";
    private String password = "";
    private String passwordConfirm = "";
    private boolean isCustomer = true;

    private final FirebaseAuth auth;
    private final UserRepository userRepository;


    @Inject
    public RegisterViewModel(FirebaseAuth firebaseAuth, UserRepository userRepository) {
        this.auth = firebaseAuth;
        this.userRepository = userRepository;
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }


    public void onNameChange(String text) {
        name = text.trim();
    }

    public void onPhoneChange(String text) {
        phone = text.trim();
    }

    public void onPasswordChange(String text) {
        password = text.trim();
    }

    public void onPasswordConfirmChange(String text) {
        passwordConfirm = text.trim();
    }

    public void onIsCustomerChange(boolean isCustomer) {
        this.isCustomer = isCustomer;
    }

    public void onRegisterClick() {

        if (name.isEmpty() || phone.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            event.setValue(new Event.ShowGeneralMessage("모두 입력해주세요"));
            return;
        }

        if (phone.length() != 11) {
            event.setValue(new Event.ShowGeneralMessage("휴대폰 번호를 정확하게 입력하세요"));
            return;
        }

        if (password.length() < 6) {
            event.setValue(new Event.ShowGeneralMessage("비밀번호를 6자리 이상 입력하세요"));
            return;
        }

        if (!passwordConfirm.equals(password)) {
            event.setValue(new Event.ShowGeneralMessage("비밀번호를 정확하게 입력하세요"));
            return;
        }

        auth.createUserWithEmailAndPassword(AuthUtils.emailize(phone), password)
                .addOnSuccessListener(authResult -> {
                    if (authResult.getUser() != null) {
                        String uid = authResult.getUser().getUid();
                        User user = new User(uid, phone, name, password, isCustomer);
                        userRepository.addUser(user,
                                unused -> {
                                    // 로그인을 진행한다
                                    auth.signInWithEmailAndPassword(AuthUtils.emailize(phone), password);
                                },
                                e -> {
                                    event.setValue(new Event.ShowGeneralMessage("회원정보 생성에 실패했습니다"));
                                    e.printStackTrace();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    event.setValue(new Event.ShowGeneralMessage("이미 가입된 회원이 있습니다"));
                    e.printStackTrace();
                });
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }
    }

}