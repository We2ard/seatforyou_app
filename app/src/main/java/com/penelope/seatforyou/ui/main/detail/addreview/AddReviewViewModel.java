package com.penelope.seatforyou.ui.main.detail.addreview;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.data.review.Review;
import com.penelope.seatforyou.data.review.ReviewRepository;
import com.penelope.seatforyou.data.shop.Shop;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddReviewViewModel extends ViewModel implements FirebaseAuth.AuthStateListener {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final Shop shop;
    private String uid;

    private String summary = "";
    private String detail = "";
    private float rating = 0;

    private final MutableLiveData<Boolean> isUploadInProgress = new MutableLiveData<>(false);

    private final ReviewRepository reviewRepository;


    @Inject
    public AddReviewViewModel(SavedStateHandle savedStateHandle, ReviewRepository reviewRepository) {
        shop = savedStateHandle.get("shop");
        this.reviewRepository = reviewRepository;
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<Boolean> isUploadInProgress() {
        return isUploadInProgress;
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            uid = firebaseAuth.getCurrentUser().getUid();
        }
    }

    public void onSummaryChange(String text) {
        summary = text.trim();
    }

    public void onDetailChange(String text) {
        detail = text.trim();
    }

    public void onRatingChange(float value) {
        rating = value;
    }

    public void onSubmitClick() {

        Boolean isUploadInProgressValue = isUploadInProgress.getValue();
        assert isUploadInProgressValue != null;

        if (uid == null) {
            event.setValue(new Event.ShowGeneralMessage("비회원은 리뷰 작성이 제한됩니다"));
            return;
        }

        if (isUploadInProgressValue) {
            return;
        }

        if (summary.isEmpty() || detail.isEmpty()) {
            event.setValue(new Event.ShowGeneralMessage("모두 입력해주세요"));
            return;
        }

        if (summary.length() > 20) {
            event.setValue(new Event.ShowGeneralMessage("한줄 리뷰는 20자 이내로 입력하세요"));
            return;
        }

        if (detail.length() < 10) {
            event.setValue(new Event.ShowGeneralMessage("상세 리뷰는 10자 이상 입력하세요"));
            return;
        }

        Review review = new Review(shop.getUid(), uid, summary, detail, Math.round(rating));

        isUploadInProgress.setValue(true);

        reviewRepository.addReview(review,
                unused -> event.setValue(new Event.NavigateBackWithResult(true)),
                e -> {
                    e.printStackTrace();
                    event.setValue(new Event.ShowGeneralMessage("리뷰 업로드에 실패했습니다"));
                    isUploadInProgress.setValue(false);
                });
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class NavigateBackWithResult extends Event {
            public final boolean success;
            public NavigateBackWithResult(boolean success) {
                this.success = success;
            }
        }

    }

}