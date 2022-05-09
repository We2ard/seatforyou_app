package com.penelope.seatforyou.data.review;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

public class ReviewRepository {

    private final CollectionReference reviewCollection;


    @Inject
    public ReviewRepository(FirebaseFirestore firestore) {
        reviewCollection = firestore.collection("reviews");
    }

    public void addReview(Review review, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {

        reviewCollection.document(review.getId())
                .set(review)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public LiveData<List<Review>> getReviewsLive(String shopId) {

        MutableLiveData<List<Review>> reviews = new MutableLiveData<>();

        reviewCollection.whereEqualTo("shopId", shopId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        error.printStackTrace();
                        reviews.setValue(null);
                        return;
                    }
                    List<Review> list = new ArrayList<>();
                    if (value == null || value.isEmpty()) {
                        reviews.setValue(list);
                        return;
                    }
                    for (DocumentSnapshot snapshot : value) {
                        Review review = snapshot.toObject(Review.class);
                        list.add(review);
                    }
                    list.sort((o1, o2) -> (int) (o2.getCreated() - o1.getCreated()));
                    reviews.setValue(list);
                });

        return reviews;
    }

    public void findReview(String shopId, String uid,
                           OnSuccessListener<Review> onSuccessListener, OnFailureListener onFailureListener) {

        reviewCollection.whereEqualTo("shopId", shopId)
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        onSuccessListener.onSuccess(null);
                        return;
                    }
                    onSuccessListener.onSuccess(queryDocumentSnapshots.getDocuments().get(0)
                            .toObject(Review.class));
                })
                .addOnFailureListener(onFailureListener);
    }

}






