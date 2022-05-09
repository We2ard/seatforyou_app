package com.penelope.seatforyou.data.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.penelope.seatforyou.utils.BaseRepository;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class UserRepository extends BaseRepository<User> {

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

    public LiveData<User> getUserLive(String uid) {

        MutableLiveData<User> user = new MutableLiveData<>();
        userCollection.document(uid)
                .addSnapshotListener((value, error) -> {
                    if (value == null || error != null) {
                        user.setValue(null);
                        return;
                    }
                    user.setValue(value.toObject(User.class));
                });

        return user;
    }

    public LiveData<Map<String, User>> getUserMap() {

        MutableLiveData<Map<String, User>> userMap = new MutableLiveData<>();

        userCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Map<String, User> map = new HashMap<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        User user = snapshot.toObject(User.class);
                        if (user != null) {
                            map.put(user.getUid(), user);
                        }
                    }
                    userMap.setValue(map);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    userMap.setValue(null);
                });

        return userMap;
    }

    public void addDib(String uid, String shopId, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        getUser(uid,
                user -> {
                    if (user == null) {
                        onFailureListener.onFailure(new Exception("User not found"));
                        return;
                    }
                    user.getDibs().remove(shopId);
                    user.getDibs().add(shopId);
                    userCollection.document(uid)
                            .set(user)
                            .addOnSuccessListener(onSuccessListener)
                            .addOnFailureListener(onFailureListener);
                },
                onFailureListener);
    }

    public void findUserByPhoneAndName(String phone, String name,
                                       OnSuccessListener<User> onSuccessListener, OnFailureListener onFailureListener) {

        userCollection.whereEqualTo("name", name)
                .whereEqualTo("phone", phone)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot snapshot = queryDocumentSnapshots.getDocuments().get(0);
                        onSuccessListener.onSuccess(snapshot.toObject(User.class));
                    } else {
                        onSuccessListener.onSuccess(null);
                    }
                })
                .addOnFailureListener(onFailureListener);
    }

}



