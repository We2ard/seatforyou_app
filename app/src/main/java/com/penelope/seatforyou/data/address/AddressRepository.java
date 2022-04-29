package com.penelope.seatforyou.data.address;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.penelope.seatforyou.api.address.AddressApi;

import java.util.List;

import javax.inject.Inject;

public class AddressRepository {

    @Inject
    public AddressRepository() {
    }

    public LiveData<List<Address>> getAddresses(String query) {

        MutableLiveData<List<Address>> addresses = new MutableLiveData<>();

        new Thread(() -> {
            List<Address> addressList = AddressApi.get(query);
            addresses.postValue(addressList);
        }).start();

        return addresses;
    }

}
