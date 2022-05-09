package com.penelope.seatforyou.ui.main.calendar;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.data.reservation.Reservation;
import com.penelope.seatforyou.data.reservation.ReservationRepository;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.data.shop.ShopRepository;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CalendarViewModel extends ViewModel implements FirebaseAuth.AuthStateListener {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final MutableLiveData<String> uid = new MutableLiveData<>();

    private final MutableLiveData<LocalDate> date = new MutableLiveData<>();

    private final LiveData<Reservation> reservation;
    private final LiveData<Shop> shop;


    @Inject
    public CalendarViewModel(ReservationRepository reservationRepository, ShopRepository shopRepository) {

        LiveData<List<Reservation>> reservations = Transformations.switchMap(uid, uidValue ->
                Transformations.switchMap(date, dateValue -> {
                    int year = dateValue.getYear();
                    int month = dateValue.getMonthValue();
                    int dayOfMonth = dateValue.getDayOfMonth();
                    return reservationRepository.getCustomerReservationLive(uidValue, year, month, dayOfMonth);
                }));

        reservation = Transformations.map(reservations, list -> (list != null && !list.isEmpty())
                ? list.get(0) : null);

        shop = Transformations.switchMap(reservation, reservationValue -> {
            if (reservationValue != null) {
                return shopRepository.live(reservationValue.getShopId(), shopRepository::getShop);
            }
            return new MutableLiveData<>(null);
        });
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<Reservation> getReservation() {
        return reservation;
    }

    public LiveData<Shop> getShop() {
        return shop;
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getUid() != null) {
            uid.setValue(firebaseAuth.getUid());
        } else {
            event.setValue(new Event.HideLoadingUI());
        }
    }

    public void onDateClick(int year, int month, int dayOfMonth) {

        date.setValue(LocalDate.of(year, month, dayOfMonth));
        if (uid.getValue() != null) {
            event.setValue(new Event.ShowLoadingUI());
        }
    }


    public static class Event {

        public static class ShowLoadingUI extends Event {
        }

        public static class HideLoadingUI extends Event {
        }
    }

}