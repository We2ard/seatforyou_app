package com.penelope.seatforyou.ui.manager.addeditreservation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.penelope.seatforyou.data.reservation.Reservation;
import com.penelope.seatforyou.data.reservation.ReservationRepository;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.data.shop.ShopRepository;
import com.penelope.seatforyou.data.user.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddEditReservationViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private Shop shop;
    private final String shopId;
    private final Reservation oldReservation;

    private final MutableLiveData<Integer> personNumber = new MutableLiveData<>(1);
    private final MutableLiveData<LocalDate> date = new MutableLiveData<>();
    private final MutableLiveData<LocalTime> time = new MutableLiveData<>();
    private String uid;

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;


    @Inject
    public AddEditReservationViewModel(SavedStateHandle savedStateHandle,
                                       ReservationRepository reservationRepository,
                                       ShopRepository shopRepository,
                                       UserRepository userRepository) {

        shopId = savedStateHandle.get("shopId");
        oldReservation = savedStateHandle.get("reservation");

        if (oldReservation != null) {
            date.setValue(LocalDate.of(oldReservation.getYear(), oldReservation.getMonth(), oldReservation.getDayOfMonth()));
            time.setValue(LocalTime.of(oldReservation.getHour(), oldReservation.getMinute()));
            personNumber.setValue(oldReservation.getPersonNumber());
            uid = oldReservation.getUid();
        }

        shopRepository.getShop(shopId, s -> shop = s, Throwable::printStackTrace);

        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
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

    public LiveData<Integer> getPersonNumber() {
        return personNumber;
    }

    public String getUid() {
        return uid;
    }


    public void onDateClick() {
        event.setValue(new Event.PromptDate());
    }

    public void onTimeClick() {
        event.setValue(new Event.PromptTime());
    }

    public void onDateSelected(int year, int month, int dayOfMonth) {
        date.setValue(LocalDate.of(year, month, dayOfMonth));
    }

    public void onTimeSelected(int hour, int minute) {
        time.setValue(LocalTime.of(hour, minute));
    }

    public void onUidChange(String text) {
        uid = text.trim();
    }

    public void onPersonNumberChange(int value) {
        personNumber.setValue(value);
    }

    public void onOkClick() {

        if (shop == null) {
            return;
        }

        LocalDate dateValue = date.getValue();
        LocalTime timeValue = time.getValue();
        Integer personNumberValue = personNumber.getValue();

        if (dateValue == null || timeValue == null || personNumberValue == null || uid.isEmpty()) {
            event.setValue(new Event.ShowGeneralMessage("모두 입력해주세요"));
            return;
        }

        LocalTime openTime = LocalTime.of(shop.getOpenHour(), shop.getOpenMinute());
        LocalTime closeTime = LocalTime.of(shop.getCloseHour(), shop.getCloseMinute());
        if (timeValue.isBefore(openTime) || timeValue.isAfter(closeTime)) {
            event.setValue(new Event.ShowGeneralMessage("예약 가능한 시간이 아닙니다"));
            return;
        }

        Reservation newReservation = new Reservation(uid, shopId, personNumberValue,
                dateValue.getYear(), dateValue.getMonthValue(), dateValue.getDayOfMonth(),
                timeValue.getHour(), timeValue.getMinute());

        LocalDateTime dateTime = LocalDateTime.of(dateValue, timeValue);

        userRepository.getUser(uid,
                user -> {
                    if (user == null || !user.isCustomer()) {
                        event.setValue(new Event.ShowGeneralMessage("올바르지 않은 유저입니다"));
                        return;
                    }
                    reservationRepository.getShopReservation(shopId, dateTime,
                            existingReservation -> {
                                if (existingReservation == null || existingReservation.equals(oldReservation)) {
                                    addEditReservation(newReservation);
                                } else {
                                    event.setValue(new Event.ShowGeneralMessage("이미 예약되어 있는 시간입니다"));
                                }
                            },
                            e -> {
                                e.printStackTrace();
                                event.setValue(new Event.ShowGeneralMessage("네트워크를 확인해주세요"));
                            });
                },
                e -> {
                    e.printStackTrace();
                    event.setValue(new Event.ShowGeneralMessage("네트워크를 확인해주세요"));
                });
    }


    private void addEditReservation(Reservation newReservation) {

        if (oldReservation == null) {
            reservationRepository.addReservation(newReservation,
                    unused -> event.setValue(new Event.NavigateBackWithResult(true)),
                    e -> {
                        e.printStackTrace();
                        event.setValue(new Event.ShowGeneralMessage("네트워크를 확인해주세요"));
                    });
        } else {
            reservationRepository.deleteReservation(oldReservation,
                    unused -> {
                        //
                        reservationRepository.addReservation(newReservation,
                                unused1 -> event.setValue(new Event.NavigateBackWithResult(false)),
                                e -> {
                                    e.printStackTrace();
                                    event.setValue(new Event.ShowGeneralMessage("네트워크를 확인해주세요"));
                                });
                    },
                    e -> {
                        e.printStackTrace();
                        event.setValue(new Event.ShowGeneralMessage("네트워크를 확인해주세요"));
                    });
        }
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;

            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class NavigateBackWithResult extends Event {
            public final boolean addOrEdit;
            public NavigateBackWithResult(boolean addOrEdit) {
                this.addOrEdit = addOrEdit;
            }
        }

        public static class PromptDate extends Event {
        }

        public static class PromptTime extends Event {
        }
    }

}