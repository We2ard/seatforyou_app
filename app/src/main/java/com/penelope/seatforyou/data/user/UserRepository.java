package com.penelope.seatforyou.data.user;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

public class UserRepository {

    private final CollectionReference userCollection;

    @Inject
    public UserRepository(FirebaseFirestore firestore) {
        userCollection = firestore.collection("users");
    }

    public void addUser(User user, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        userCollection.document(user.getUid())
                .set(user)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void getUser(String uid, OnSuccessListener<User> onSuccessListener, OnFailureListener onFailureListener) {
        userCollection.document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot == null) {
                        onSuccessListener.onSuccess(null);
                        return;
                    }
                    User user = documentSnapshot.toObject(User.class);
                    onSuccessListener.onSuccess(user);
                })
                .addOnFailureListener(onFailureListener);
    }

}



