package com.cmsc436.quickbite;

import com.firebase.client.Firebase;

/**
 * Created by dominic on 5/14/16.
 */

public class MyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
