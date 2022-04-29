package com.penelope.seatforyou.utils.ui;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public abstract class AuthListenerActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    protected final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public void onResume() {
        super.onResume();
        auth.addAuthStateListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        auth.removeAuthStateListener(this);
    }
}
