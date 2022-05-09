package com.penelope.seatforyou.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.function.BiConsumer;

public class BaseRepository<T> {

    public LiveData<T> live(BiConsumer<OnSuccessListener<T>, OnFailureListener> consumer) {
        MutableLiveData<T> liveData = new MutableLiveData<>();
        consumer.accept(liveData::setValue, e -> liveData.setValue(null));
        return liveData;
    }

    public LiveData<List<T>> lives(BiConsumer<OnSuccessListener<List<T>>, OnFailureListener> consumer) {
        MutableLiveData<List<T>> liveData = new MutableLiveData<>();
        consumer.accept(liveData::setValue, e -> liveData.setValue(null));
        return liveData;
    }

    public LiveData<T> live(String id, TriConsumer<String, OnSuccessListener<T>, OnFailureListener> consumer) {
        MutableLiveData<T> liveData = new MutableLiveData<>();
        consumer.accept(id, liveData::setValue, e -> liveData.setValue(null));
        return liveData;
    }

    public LiveData<List<T>> lives(List<String> ids, TriConsumer<List<String>, OnSuccessListener<List<T>>, OnFailureListener> consumer) {
        MutableLiveData<List<T>> liveData = new MutableLiveData<>();
        consumer.accept(ids, liveData::setValue, e -> liveData.setValue(null));
        return liveData;
    }

}
