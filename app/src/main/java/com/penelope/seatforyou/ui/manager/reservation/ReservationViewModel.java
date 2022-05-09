package com.penelope.seatforyou.ui.manager.reservation;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.data.reservation.Reservation;
import com.penelope.seatforyou.data.reservation.ReservationRepository;
import com.penelope.seatforyou.data.user.User;
import com.penelope.seatforyou.data.user.UserRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReservationViewModel extends ViewModel implements FirebaseAuth.AuthStateListener {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final MutableLiveData<String> uid = new MutableLiveData<>();

    private final LiveData<List<Reservation>> reservations;
    private final LiveData<Map<String, User>> userMap;

    private final ReservationRepository reservationRepository;


    @Inject
    public ReservationViewModel(ReservationRepository reservationRepository,
                                UserRepository userRepository) {

        reservations = Transformations.switchMap(uid, reservationRepository::getShopReservationLive);

        userMap = userRepository.getUserMap();

        this.reservationRepository = reservationRepository;
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<List<Reservation>> getReservations() {
        return reservations;
    }

    public LiveData<Map<String, User>> getUserMap() {
        return userMap;
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getUid() != null) {
            uid.setValue(firebaseAuth.getUid());
        }
    }

    public void onAddClick() {
        event.setValue(new Event.NavigateToAddReservationScreen(uid.getValue()));
    }

    public void onEditClick(Reservation reservation) {
        event.setValue(new Event.NavigateToEditReservationScreen(uid.getValue(), reservation));
    }

    public void onDeleteClick(Reservation reservation) {
        event.setValue(new Event.ConfirmDelete(reservation));
    }

    public void onDeleteConfirm(Reservation reservation) {

        reservationRepository.deleteReservation(reservation,
                unused -> event.setValue(new Event.ShowGeneralMessage("예약이 삭제되었습니다")),
                e -> {
                    e.printStackTrace();
                    event.setValue(new Event.ShowGeneralMessage("삭제에 실패했습니다"));
                });
    }

    public void onAddEditResult(boolean addOrEdit) {
        if (addOrEdit) {
            event.setValue(new Event.ShowGeneralMessage("예약이 추가되었습니다"));
        } else {
            event.setValue(new Event.ShowGeneralMessage("예약이 수정되었습니다"));
        }
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class ConfirmDelete extends Event {
            public final Reservation reservation;

            public ConfirmDelete(Reservation reservation) {
                this.reservation = reservation;
            }
        }

        public static class NavigateToEditReservationScreen extends Event {
            public final String shopId;
            public final Reservation reservation;
            public NavigateToEditReservationScreen(String shopId, Reservation reservation) {
                this.shopId = shopId;
                this.reservation = reservation;
            }
        }

        public static class NavigateToAddReservationScreen extends Event {
            public final String shopId;
            public NavigateToAddReservationScreen(String shopId) {
                this.shopId = shopId;
            }
        }
    }

}