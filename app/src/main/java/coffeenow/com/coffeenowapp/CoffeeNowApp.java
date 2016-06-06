package coffeenow.com.coffeenowapp;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import coffeenow.com.coffeenowapp.models.User;

public class CoffeeNowApp extends Application {

    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        user = new User();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
