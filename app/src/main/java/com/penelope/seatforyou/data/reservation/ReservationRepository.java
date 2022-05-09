package com.penelope.seatforyou.data.reservation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ReservationRepository {

    private final CollectionReference reservationCollection;

    @Inject
    public ReservationRepository(FirebaseFirestore firestore) {
        reservationCollection = firestore.collection("reservations");
    }

    public void addReservation(Reservation reservation,
                               OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {

        reservationCollection.document(reservation.getId())
                .set(reservation)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public LiveData<List<Reservation>> getCustomerReservationLive(String uid, int year, int month, int dayOfMonth) {

        MutableLiveData<List<Reservation>> reservations = new MutableLiveData<>();

        reservationCollection.whereEqualTo("uid", uid)
                .whereEqualTo("year", year)
                .whereEqualTo("month", month)
                .whereEqualTo("dayOfMonth", dayOfMonth)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        error.printStackTrace();
                        reservations.setValue(null);
                        return;
                    }
                    List<Reservation> reservationList = new ArrayList<>();
                    if (value == null || value.isEmpty()) {
                        reservations.setValue(reservationList);
                        return;
                    }
                    for (DocumentSnapshot snapshot : value) {
                        Reservation reservation = snapshot.toObject(Reservation.class);
                        if (reservation != null) {
                            reservationList.add(reservation);
                        }
                    }
                    reservations.setValue(reservationList);
                });

        return reservations;
    }

    public LiveData<List<Reservation>> getShopReservationLive(String shopId) {

        MutableLiveData<List<Reservation>> reservations = new MutableLiveData<>();

        reservationCollection.whereEqualTo("shopId", shopId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        error.printStackTrace();
                        reservations.setValue(null);
                        return;
                    }
                    List<Reservation> reservationList = new ArrayList<>();
                    if (value == null || value.isEmpty()) {
                        reservations.setValue(reservationList);
                        return;
                    }
                    for (DocumentSnapshot snapshot : value) {
                        Reservation reservation = snapshot.toObject(Reservation.class);
                        if (reservation != null) {
                            reservationList.add(reservation);
                        }
                    }
                    reservationList.sort((o1, o2) -> {
                        LocalDateTime dateTime1 = LocalDateTime.of(o1.getYear(), o1.getMonth(),
                                o1.getDayOfMonth(), o1.getHour(), o1.getMinute());
                        LocalDateTime dateTime2 = LocalDateTime.of(o2.getYear(), o2.getMonth(),
                                o2.getDayOfMonth(), o2.getHour(), o2.getMinute());
                        return dateTime1.compareTo(dateTime2);
                    });
                    reservations.setValue(reservationList);
                });

        return reservations;
    }

    public void getShopReservation(String shopId, LocalDateTime dateTime,
                                   OnSuccessListener<Reservation> onSuccessListener, OnFailureListener onFailureListener) {

        reservationCollection.whereEqualTo("shopId", shopId)
                .whereEqualTo("year", dateTime.getYear())
                .whereEqualTo("month", dateTime.getMonthValue())
                .whereEqualTo("dayOfMonth", dateTime.getDayOfMonth())
                .whereEqualTo("hour", dateTime.getHour())
                .whereEqualTo("minute", dateTime.getMinute())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots == null || queryDocumentSnapshots.isEmpty()) {
                        onSuccessListener.onSuccess(null);
                        return;
                    }
                    onSuccessListener.onSuccess(queryDocumentSnapshots.getDocuments().get(0)
                            .toObject(Reservation.class));
                })
                .addOnFailureListener(onFailureListener);
    }

    public void deleteReservation(Reservation reservation,
                                  OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {

        reservationCollection.document(reservation.getId())
                .delete()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

}

