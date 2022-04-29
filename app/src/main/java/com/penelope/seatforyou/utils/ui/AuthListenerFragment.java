package com.penelope.seatforyou.utils.ui;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public abstract class AuthListenerFragment extends Fragment implements FirebaseAuth.AuthStateListener {

    protected final FirebaseAuth auth;

    public AuthListenerFragment(int layout) {
        super(layout);
        auth = FirebaseAuth.getInstance();
    }

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