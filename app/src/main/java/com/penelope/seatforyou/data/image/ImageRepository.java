package com.penelope.seatforyou.data.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.penelope.seatforyou.utils.BaseRepository;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

public class ImageRepository extends BaseRepository<Bitmap> {

    private static final int MEGABYTES = 1024 * 1024;

    private final StorageReference shopImages;


    @Inject
    public ImageRepository(FirebaseStorage storage) {
        shopImages = storage.getReference("shops");
    }

    public void addShopImage(String uid, Bitmap bitmap,
                             OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {

        // 스토리지에 가게 이미지를 업로드한다

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        shopImages.child(uid + ".jpg")
                .putBytes(data)
                .addOnSuccessListener(taskSnapshot -> onSuccessListener.onSuccess(null))
                .addOnFailureListener(onFailureListener);
    }

    public void getShopImage(String uid,
                               OnSuccessListener<Bitmap> onSuccessListener,
                               OnFailureListener onFailureListener) {

        // 스토리지에서 가게 이미지를 불러온다

        shopImages.child(uid + ".jpg")
                .getBytes(10 * MEGABYTES)
                .addOnSuccessListener(bytes -> {
                    Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    onSuccessListener.onSuccess(image);
                })
                .addOnFailureListener(onFailureListener);
    }

}
