package io.github.gengkev.firebaseapp;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by 2017kgeng on 4/18/2016.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
