package com.shadiz.uzergin.robotcontroller;

import android.app.Application;
import android.content.Intent;

import com.shadiz.uzergin.robotcontroller.di.AppComponent;
import com.shadiz.uzergin.robotcontroller.di.DaggerAppComponent;
import com.shadiz.uzergin.robotcontroller.di.modules.AppModule;
import com.shadiz.uzergin.robotcontroller.service.SensorService;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by OldMan on 07.11.2016.
 */

public class App extends Application {
    private static AppComponent appComponent;
    private static App context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        setAppComponent();
        startService(new Intent(this, SensorService.class));
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    private void setAppComponent() {
        if (appComponent == null) {
            buildAppComponent();
        }
    }

    private static AppComponent buildAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context))
                .build();
        return appComponent;
    }

}