package com.penelope.seatforyou.ui.main.home;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.naver.maps.geometry.LatLng;
import com.penelope.seatforyou.data.recommended.Recommended;
import com.penelope.seatforyou.data.recommended.RecommendedRepository;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.data.shop.ShopRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final LiveData<List<Shop>> recommendedShops;
    private final LiveData<Shop> newestShop;

    private LatLng currentLocation;

    private final ShopRepository shopRepository;


    @Inject
    public HomeViewModel(ShopRepository shopRepository, RecommendedRepository recommendedRepository) {

        recommendedRepository.updateRecommended();

        LiveData<Recommended> recommended = recommendedRepository.getRecommended();
        recommendedShops = Transformations.switchMap(recommended, rec ->
                shopRepository.lives(rec.getItems(), shopRepository::getShops));

        newestShop = shopRepository.getNewestShopLive();

        this.shopRepository = shopRepository;
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<Shop> getNewestShop() {
        return newestShop;
    }

    public LiveData<List<Shop>> getRecommendedShops() {
        return recommendedShops;
    }


    public void onShopClick(Shop shop) {
        event.setValue(new Event.NavigateToDetailScreen(shop));
    }

    public void onNearbyClick() {

        if (currentLocation == null) {
            event.setValue(new Event.RequestLocationPermission());
            return;
        }

        event.setValue(new Event.ShowLoadingUI());

        shopRepository.findShopsNearby(currentLocation.latitude, currentLocation.longitude, 10000,
                shops -> {
                    String description = "내 주변 레스토랑";
                    event.setValue(new Event.NavigateToFilteredScreen(shops, description));
                },
                e -> event.setValue(new Event.ShowGeneralMessage("검색에 실패했습니다"))
        );
    }

    public void onRegionClick(String region) {

        event.setValue(new Event.ShowLoadingUI());

        shopRepository.findShopsByRegion(region,
                shops -> {
                    String description = region + " 소재 레스토랑";
                    event.setValue(new Event.NavigateToFilteredScreen(shops, description));
                },
                e -> event.setValue(new Event.ShowGeneralMessage("검색에 실패했습니다"))
        );
    }

    public void onLocationChange(Location location) {
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void onNewShopClick() {
        if (newestShop.getValue() != null) {
            event.setValue(new Event.NavigateToDetailScreen(newestShop.getValue()));
        }
    }


    public static class Event {

        public static class NavigateToDetailScreen extends Event {
            public final Shop shop;
            public NavigateToDetailScreen(Shop shop) {
                this.shop = shop;
            }
        }

        public static class NavigateToFilteredScreen extends Event {
            public final List<Shop> shops;
            public final String description;
            public NavigateToFilteredScreen(List<Shop> shops, String description) {
                this.shops = shops;
                this.description = description;
            }
        }

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class ShowLoadingUI extends Event {
        }

        public static class RequestLocationPermission extends Event {
        }
    }

}