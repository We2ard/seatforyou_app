package com.penelope.seatforyou.data.shop;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

public class ShopRepository {

    private final CollectionReference shopCollection;


    @Inject
    public ShopRepository(FirebaseFirestore firestore) {
        shopCollection = firestore.collection("shops");
    }

    public void addShop(Shop shop, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        shopCollection.document(shop.getUid())
                .set(shop)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void getShop(String uid, OnSuccessListener<Shop> onSuccessListener, OnFailureListener onFailureListener) {
        shopCollection.document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot ->
                        onSuccessListener.onSuccess(documentSnapshot == null ?
                                null :
                                documentSnapshot.toObject(Shop.class))
                )
                .addOnFailureListener(onFailureListener);
    }

}
