package com.cmsc436.quickbite;

import com.firebase.client.Firebase;

/**
 * Created by dominic on 5/14/16.
 */

public class MyApplication extends android.app.Application {

    private User currentUser; // Current user

    public User getCurrentUser() {
        return currentUser;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
