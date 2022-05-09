package com.penelope.seatforyou.data.recommended;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.data.shop.ShopRepository;
import com.penelope.seatforyou.utils.RandomUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class RecommendedRepository {

    private final CollectionReference recommendedCollection;
    private final ShopRepository shopRepository;

    @Inject
    public RecommendedRepository(FirebaseFirestore firestore, ShopRepository shopRepository) {
        this.recommendedCollection = firestore.collection("recommended");
        this.shopRepository = shopRepository;
    }

    public LiveData<Recommended> getRecommended() {

        MutableLiveData<Recommended> recommended = new MutableLiveData<>();

        recommendedCollection.document("recommended")
                .addSnapshotListener((value, error) -> {
                    if (value == null || error != null) {
                        recommended.setValue(null);
                        return;
                    }
                    Recommended r = value.toObject(Recommended.class);
                    recommended.setValue(r);
                });

        return recommended;
    }

    public void updateRecommended() {

        recommendedCollection.document("recommended")
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot == null) {
                        return;
                    }

                    Recommended recommended = documentSnapshot.toObject(Recommended.class);
                    if (recommended == null) {
                        return;
                    }

                    int today = LocalDate.now().getDayOfMonth();

                    if (recommended.getUpdatedDay() != today) {

                        shopRepository.getAllShops(
                                recipes -> {
                                    List<String> ids = recipes.stream().map(Shop::getUid).collect(Collectors.toList());
                                    List<String> picked = RandomUtils.pick(ids, 5);
                                    recommended.setItems(picked);
                                    recommended.setUpdatedDay(today);
                                    recommendedCollection.document("recommended").set(recommended);
                                },
                                Throwable::printStackTrace
                        );
                    }
                });
    }

}
