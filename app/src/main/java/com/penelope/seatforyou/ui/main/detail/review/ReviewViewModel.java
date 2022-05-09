package com.penelope.seatforyou.ui.main.detail.review;

import android.view.animation.Transformation;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.data.review.Review;
import com.penelope.seatforyou.data.review.ReviewRepository;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.data.user.User;
import com.penelope.seatforyou.data.user.UserRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReviewViewModel extends ViewModel implements FirebaseAuth.AuthStateListener {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final Shop shop;
    private String uid;

    private final LiveData<List<Review>> reviews;
    private final LiveData<Map<String, User>> userMap;

    private final LiveData<Integer> reviewNumber;
    private final LiveData<Float> averageRating;

    private final ReviewRepository reviewRepository;


    @Inject
    public ReviewViewModel(SavedStateHandle savedStateHandle, ReviewRepository reviewRepository, UserRepository userRepository) {

        shop = savedStateHandle.get("shop");
        assert shop != null;

        reviews = reviewRepository.getReviewsLive(shop.getUid());
        userMap = userRepository.getUserMap();

        reviewNumber = Transformations.map(reviews, list -> list != null ? list.size() : null);
        averageRating = Transformations.map(reviews, list -> {
            if (list != null && !list.isEmpty()) {
                float sum = 0;
                for (Review review : list) {
                    sum += review.getRating();
                }
                return sum / list.size();
            } else {
                return null;
            }
        });

        this.reviewRepository = reviewRepository;
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public Shop getShop() {
        return shop;
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public LiveData<Map<String, User>> getUserMap() {
        return userMap;
    }

    public LiveData<Integer> getReviewNumber() {
        return reviewNumber;
    }

    public LiveData<Float> getAverageRating() {
        return averageRating;
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getUid() != null) {
            uid = firebaseAuth.getUid();
        }
    }

    public void onAddReviewClick() {

        if (uid == null) {
            event.setValue(new Event.ShowGeneralMessage("비회원은 리뷰 작성이 제한됩니다"));
            return;
        }

        reviewRepository.findReview(shop.getUid(), uid,
                review -> {
                    if (review == null) {
                        event.setValue(new Event.NavigateToAddReviewScreen());
                    } else {
                        event.setValue(new Event.ShowGeneralMessage("이미 작성한 리뷰가 있습니다"));
                    }
                },
                e -> {
                    e.printStackTrace();
                    event.setValue(new Event.ShowGeneralMessage("네트워크를 확인해주세요"));
                });

    }

    public void onAddReviewResult(boolean success) {
        if (success) {
            event.setValue(new Event.ShowGeneralMessage("리뷰가 작성되었습니다"));
        }
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class NavigateToAddReviewScreen extends Event {
        }
    }

}