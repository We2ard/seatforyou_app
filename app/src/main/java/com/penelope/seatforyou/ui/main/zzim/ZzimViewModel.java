package com.penelope.seatforyou.ui.main.zzim;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.data.shop.ShopRepository;
import com.penelope.seatforyou.data.user.User;
import com.penelope.seatforyou.data.user.UserRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ZzimViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final MutableLiveData<String> uid = new MutableLiveData<>();

    private final LiveData<List<Shop>> shops;


    @Inject
    public ZzimViewModel(UserRepository userRepository, ShopRepository shopRepository) {

        LiveData<User> user = Transformations.switchMap(uid, userRepository::getUserLive);
        shops = Transformations.switchMap(user, u -> shopRepository.lives(u.getDibs(), shopRepository::getShops));
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<List<Shop>> getShops() {
        return shops;
    }


    public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getUid() != null) {
            uid.setValue(firebaseAuth.getUid());
        } else {
            event.setValue(new Event.HideLoadingUI());
        }
    }

    public void onShopClick(Shop shop) {
        event.setValue(new Event.NavigateToDetailScreen(shop));
    }


    public static class Event {

        public static class NavigateToDetailScreen extends Event {
            public final Shop shop;
            public NavigateToDetailScreen(Shop shop) {
                this.shop = shop;
            }
        }
        
        public static class HideLoadingUI extends Event {
        }
    }

}