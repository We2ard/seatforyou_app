package com.penelope.seatforyou.ui.main.search.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.penelope.seatforyou.data.reservation.ReservationRepository;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.data.shop.ShopRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SearchViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private String query = "";
    private final MutableLiveData<LocalDateTime> dateTime = new MutableLiveData<>();
    private String filterRegion;
    private int filterPrice;
    private String filterCategory;

    private final ShopRepository shopRepository;
    private final ReservationRepository reservationRepository;


    @Inject
    public SearchViewModel(ShopRepository shopRepository, ReservationRepository reservationRepository) {
        this.shopRepository = shopRepository;
        this.reservationRepository = reservationRepository;
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<LocalDateTime> getDateTime() {
        return dateTime;
    }


    public void onQueryChange(String text) {
        query = text.trim();
    }

    public void onClearQueryClick() {

        query = "";
        event.setValue(new Event.ClearQueryUI());

        dateTime.setValue(null);

        filterRegion = null;
        filterPrice = 0;
        filterCategory = null;
    }

    public void onSetDateTimeClick() {
        event.setValue(new Event.NavigateToSetDateTimeScreen(dateTime.getValue()));
    }

    public void onSetFilterClick() {
        event.setValue(new Event.NavigateToSetFilterScreen(filterRegion, filterPrice, filterCategory));
    }

    public void onSearchClick() {

        if (query.isEmpty()) {
            event.setValue(new Event.ShowGeneralMessage("검색어를 입력하세요"));
            return;
        }

        shopRepository.findShopsByNameOrRegion(query,
                shops -> {
                    if (filterRegion != null) {
                        for (int i = shops.size() - 1; i >= 0; i--) {
                            Shop shop = shops.get(i);
                            if (!shop.getAddress().getLoadAddress().contains(filterRegion)) {
                                shops.remove(i);
                            }
                        }
                    }
                    if (filterPrice > 0) {
                        for (int i = shops.size() - 1; i >= 0; i--) {
                            Shop shop = shops.get(i);
                            if (shop.getMenus().values().stream().noneMatch(price -> price <= filterPrice)) {
                                shops.remove(i);
                            }
                        }
                    }
                    if (filterCategory != null) {
                        for (int i = shops.size() - 1; i >= 0; i--) {
                            Shop shop = shops.get(i);
                            if (!shop.getCategories().contains(filterCategory)) {
                                shops.remove(i);
                            }
                        }
                    }
                    if (dateTime.getValue() != null) {
                        List<Shop> filtered = new ArrayList<>();
                        for (Shop shop : shops) {
                            reservationRepository.getShopReservation(shop.getUid(), dateTime.getValue(),
                                    reservation -> {
                                        if (reservation == null) {
                                            LocalTime openTime = LocalTime.of(shop.getOpenHour(), shop.getOpenMinute());
                                            LocalTime closeTime = LocalTime.of(shop.getCloseHour(), shop.getCloseMinute());
                                            LocalTime selectedTime = dateTime.getValue().toLocalTime();
                                            if (!selectedTime.isBefore(openTime) && !selectedTime.isAfter(closeTime)) {
                                                filtered.add(shop);
                                            }
                                        }
                                        if (shop.equals(shops.get(shops.size() - 1))) {
                                            event.setValue(new Event.NavigateToFilteredScreen(filtered, "검색 결과: " + query));
                                        }
                                    },
                                    Throwable::printStackTrace);
                        }
                    } else {
                        event.setValue(new Event.NavigateToFilteredScreen(shops, "검색 결과: " + query));
                    }
                },
                e -> {
                    e.printStackTrace();
                    event.setValue(new Event.ShowGeneralMessage("검색에 실패했습니다"));
                });
    }

    public void onDateTimeResult(LocalDateTime value) {
        if (value != null) {
            dateTime.setValue(value);
        }
    }

    public void onFilterResult(String region, int price, String category) {
        filterRegion = region;
        filterPrice = price;
        filterCategory = category;
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;
            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class ClearQueryUI extends Event {
        }

        public static class NavigateToSetFilterScreen extends Event {
            public final String region;
            public final int price;
            public final String category;
            public NavigateToSetFilterScreen(String region, int price, String category) {
                this.region = region;
                this.price = price;
                this.category = category;
            }
        }

        public static class NavigateToSetDateTimeScreen extends Event {
            public final LocalDateTime dateTime;
            public NavigateToSetDateTimeScreen(LocalDateTime dateTime) {
                this.dateTime = dateTime;
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
    }

}