package com.shadiz.uzergin.robotcontroller.di.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.shadiz.uzergin.robotcontroller.App;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by OldMan on 07.11.2016.
 */
@Module
public class AppModule {
    private App app;
    private EventBus eventBus;
    private Resources resources;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return app;
    }

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return eventBus;
    }
    @Provides
    @Singleton
    public SharedPreferences providesPreference() {
        return  PreferenceManager.getDefaultSharedPreferences(app);
    }
    @Provides
    @Singleton
    public Resources providesResources() {
        return  app.getApplicationContext().getResources();
    }
    @Provides
    @Singleton
    public Calendar providesCalendar() {
        return Calendar.getInstance();
    }
//    @Provides
//    @Singleton
//    public Preferences providesMyPreference() {
//        return new  Preferences(app);
//    }
}
