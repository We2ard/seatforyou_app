package com.penelope.seatforyou.ui.manager.shop.shop;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.penelope.seatforyou.data.address.Address;
import com.penelope.seatforyou.data.image.ImageRepository;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.data.shop.ShopRepository;
import com.penelope.seatforyou.utils.PrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ShopViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final String uid;
    private boolean everEnrolled;

    private final MutableLiveData<String> name = new MutableLiveData<>();
    private final MutableLiveData<String> description = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Integer>> menus = new MutableLiveData<>(new HashMap<>());
    private final MutableLiveData<Address> address = new MutableLiveData<>();
    private final MutableLiveData<String> phone = new MutableLiveData<>();
    private final MutableLiveData<List<String>> categories = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> openHour = new MutableLiveData<>();
    private final MutableLiveData<Integer> openMinute = new MutableLiveData<>();
    private final MutableLiveData<Integer> closeHour = new MutableLiveData<>();
    private final MutableLiveData<Integer> closeMinute = new MutableLiveData<>();
    private final MutableLiveData<Bitmap> image = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isSettingName = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isSettingDescription = new MutableLiveData<>(false);

    private String nameInput = "";
    private String descriptionInput = "";

    private final ShopRepository shopRepository;
    private final ImageRepository imageRepository;


    @Inject
    public ShopViewModel(Application application, ShopRepository shopRepository, ImageRepository imageRepository) {

        uid = PrefUtils.getCurrentUid(application);

        shopRepository.getShop(uid,
                shop -> {
                    if (shop != null) {
                        name.setValue(shop.getName());
                        description.setValue(shop.getDescription());
                        menus.setValue(shop.getMenus());
                        address.setValue(shop.getAddress());
                        phone.setValue(shop.getPhone());
                        categories.setValue(shop.getCategories());
                        openHour.setValue(shop.getOpenHour());
                        openMinute.setValue(shop.getOpenMinute());
                        closeHour.setValue(shop.getCloseHour());
                        closeMinute.setValue(shop.getCloseMinute());
                        everEnrolled = true;
                    }
                    event.setValue(new Event.HideLoadingUI());
                },
                Throwable::printStackTrace);

        imageRepository.getShopImage(uid,
                image::setValue,
                Throwable::printStackTrace);

        this.shopRepository = shopRepository;
        this.imageRepository = imageRepository;
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<String> getName() {
        return name;
    }

    public LiveData<String> getDescription() {
        return description;
    }

    public LiveData<Map<String, Integer>> getMenus() {
        return menus;
    }

    public LiveData<Address> getAddress() {
        return address;
    }

    public LiveData<String> getPhone() {
        return phone;
    }

    public LiveData<List<String>> getCategories() {
        return categories;
    }

    public LiveData<Integer> getOpenHour() {
        return openHour;
    }

    public LiveData<Integer> getOpenMinute() {
        return openMinute;
    }

    public LiveData<Integer> getCloseHour() {
        return closeHour;
    }

    public LiveData<Integer> getCloseMinute() {
        return closeMinute;
    }

    public LiveData<Bitmap> getImage() {
        return image;
    }

    public LiveData<Boolean> isSettingName() {
        return isSettingName;
    }

    public LiveData<Boolean> isSettingDescription() {
        return isSettingDescription;
    }


    public void onSetNameClick() {

        Boolean isSettingNameValue = isSettingName.getValue();
        assert isSettingNameValue != null;

        if (!isSettingNameValue) {
            isSettingName.setValue(true);
        } else {
            if (!nameInput.isEmpty()) {
                name.setValue(nameInput);
            } else {
                event.setValue(new Event.ShowGeneralMessage("가게 이름을 입력해주세요"));
            }
            isSettingName.setValue(false);
        }
    }

    public void onSetDescriptionClick() {

        Boolean isSettingDescriptionValue = isSettingDescription.getValue();
        assert isSettingDescriptionValue != null;

        if (!isSettingDescriptionValue) {
            isSettingDescription.setValue(true);
        } else {
            if (!descriptionInput.isEmpty()) {
                description.setValue(descriptionInput);
            } else {
                event.setValue(new Event.ShowGeneralMessage("가게 설명을 입력해주세요"));
            }
            isSettingDescription.setValue(false);
        }
    }

    public void onSetImageClick() {
        event.setValue(new Event.PromptImage());
    }

    public void onImageSelected(Bitmap bitmap) {
        if (bitmap != null) {
            image.setValue(bitmap);
        } else {
            event.setValue(new Event.ShowGeneralMessage("이미지를 불러오지 못했습니다"));
        }
    }

    public void onSetMenusClick() {
        event.setValue(new Event.NavigateToMenuScreen(menus.getValue()));
    }

    public void onMenusResult(List<String> menuNames, List<Integer> menuPrices) {

        if (menuNames.size() != menuPrices.size()) {
            return;
        }

        Map<String, Integer> menus = new HashMap<>();

        for (int i = 0; i < menuNames.size(); i++) {
            String name = menuNames.get(i);
            int price = menuPrices.get(i);
            menus.put(name, price);
        }

        this.menus.setValue(menus);
    }

    public void onSetAddressClick() {
        event.setValue(new Event.NavigateToAddressScreen(address.getValue()));
    }

    public void onSetPhoneClick() {
        String phoneValue = phone.getValue();
        event.setValue(new Event.PromptPhone(phoneValue == null ? "" : phoneValue));
    }

    public void onPhoneSelected(String value) {
        value = value.trim();
        if (!value.isEmpty()) {
            phone.setValue(value);
        } else {
            event.setValue(new Event.ShowGeneralMessage("휴대폰 번호를 입력해주세요"));
        }
    }

    public void onSetCategoriesClick() {
        event.setValue(new Event.NavigateToCategoryScreen(categories.getValue()));
    }

    public void onCategoriesResult(List<String> categories) {
        this.categories.setValue(categories);
    }

    public void onSetOpenCloseClick() {

        Integer openHourValue = openHour.getValue();
        Integer openMinuteValue = openMinute.getValue();
        Integer closeHourValue = closeHour.getValue();
        Integer closeMinuteValue = closeMinute.getValue();

        if (openHourValue != null && openMinuteValue != null
                && closeHourValue != null && closeMinuteValue != null) {
            event.setValue(new Event.NavigateToOpenCloseScreen(
                    openHourValue, openMinuteValue, closeHourValue, closeMinuteValue));
        } else {
            event.setValue(new Event.NavigateToOpenCloseScreen(8, 0, 20, 0));
        }
    }

    public void onOpenCloseResult(int openHour, int openMinute, int closeHour, int closeMinute) {
        this.openHour.setValue(openHour);
        this.openMinute.setValue(openMinute);
        this.closeHour.setValue(closeHour);
        this.closeMinute.setValue(closeMinute);
    }

    public void onNameInputChange(String text) {
        nameInput = text.trim();
    }

    public void onDescriptionInputChange(String text) {
        descriptionInput = text.trim();
    }

    public void onAddressResult(Address value) {
        if (value != null) {
            address.setValue(value);
        }
    }

    public void onSubmitClick() {

        String nameValue = name.getValue();
        String descriptionValue = description.getValue();
        Map<String, Integer> menusValue = menus.getValue();
        Address addressValue = address.getValue();
        String phoneValue = phone.getValue();
        List<String> categoriesValue = categories.getValue();
        Integer openHourValue = openHour.getValue();
        Integer openMinuteValue = openMinute.getValue();
        Integer closeHourValue = closeHour.getValue();
        Integer closeMinuteValue = closeMinute.getValue();
        Bitmap imageValue = image.getValue();

        if (nameValue == null || descriptionValue == null
                || addressValue == null || phoneValue == null
                || menusValue == null || categoriesValue == null
                || openHourValue == null || openMinuteValue == null
                || closeHourValue == null || closeMinuteValue == null || imageValue == null) {
            event.setValue(new Event.ShowGeneralMessage("모두 입력해주세요"));
            return;
        }

        if (menusValue.isEmpty()) {
            event.setValue(new Event.ShowGeneralMessage("메뉴를 입력해주세요"));
            return;
        }

        if (categoriesValue.isEmpty()) {
            event.setValue(new Event.ShowGeneralMessage("카테고리를 입력해주세요"));
            return;
        }

        Shop shop = new Shop(uid, nameValue, descriptionValue, menusValue, addressValue, phoneValue,
                categoriesValue, openHourValue, openMinuteValue, closeHourValue, closeMinuteValue);

        shopRepository.addShop(shop,
                unused -> imageRepository.addShopImage(uid, imageValue,
                        unused1 -> event.setValue(new Event.NavigateBackWithResult(!everEnrolled)),
                        Throwable::printStackTrace),
                e -> {
                    e.printStackTrace();
                    event.setValue(new Event.ShowGeneralMessage("등록에 실패했습니다"));
                });
    }


    public static class Event {

        public static class ShowGeneralMessage extends Event {
            public final String message;

            public ShowGeneralMessage(String message) {
                this.message = message;
            }
        }

        public static class PromptImage extends Event {
        }

        public static class PromptPhone extends Event {
            public final String phone;

            public PromptPhone(String phone) {
                this.phone = phone;
            }
        }

        public static class NavigateToAddressScreen extends Event {
            public final Address oldAddress;

            public NavigateToAddressScreen(Address oldAddress) {
                this.oldAddress = oldAddress;
            }
        }

        public static class NavigateToOpenCloseScreen extends Event {
            public final int openHour;
            public final int openMinute;
            public final int closeHour;
            public final int closeMinute;

            public NavigateToOpenCloseScreen(int openHour, int openMinute, int closeHour, int closeMinute) {
                this.openHour = openHour;
                this.openMinute = openMinute;
                this.closeHour = closeHour;
                this.closeMinute = closeMinute;
            }
        }

        public static class NavigateToCategoryScreen extends Event {
            public final List<String> categories;

            public NavigateToCategoryScreen(List<String> categories) {
                this.categories = categories;
            }
        }

        public static class NavigateToMenuScreen extends Event {
            public final Map<String, Integer> menus;

            public NavigateToMenuScreen(Map<String, Integer> menus) {
                this.menus = menus;
            }
        }

        public static class NavigateBackWithResult extends Event {
            public final boolean enrollOrModify;

            public NavigateBackWithResult(boolean enrollOrModify) {
                this.enrollOrModify = enrollOrModify;
            }
        }

        public static class HideLoadingUI extends Event {
        }
    }

}






