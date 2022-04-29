package com.penelope.seatforyou.ui.manager.shop.address;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.penelope.seatforyou.data.address.Address;
import com.penelope.seatforyou.data.address.AddressRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddressViewModel extends ViewModel {

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final MutableLiveData<String> query = new MutableLiveData<>();
    private final LiveData<List<Address>> addresses;

    private final Address oldAddress;

    private String addressDetail = "";


    @Inject
    public AddressViewModel(SavedStateHandle savedStateHandle, AddressRepository addressRepository) {

        oldAddress = savedStateHandle.get("oldAddress");

        addresses = Transformations.switchMap(query, addressRepository::getAddresses);
    }

    public LiveData<Event> getEvent() {
        event.setValue(null);
        return event;
    }

    public LiveData<List<Address>> getAddresses() {
        return addresses;
    }

    public Address getOldAddress() {
        return oldAddress;
    }


    public void onQueryChange(String text) {
        if (!text.trim().isEmpty()) {
            query.setValue(text.trim());
            event.setValue(new Event.ShowLoadingUI());
        }
    }

    public void onAddressClick(Address address) {
         if (address != null) {
             address.setDetail(addressDetail);
             event.setValue(new Event.NavigateBackWithResult(address));
         }
    }

    public void onAddressDetailChange(String text) {
        addressDetail = text.trim();
    }


    public static class Event {

        public static class NavigateBackWithResult extends Event {
            public final Address address;
            public NavigateBackWithResult(Address address) {
                this.address = address;
            }
        }

        public static class ShowLoadingUI extends Event {
        }

    }

}