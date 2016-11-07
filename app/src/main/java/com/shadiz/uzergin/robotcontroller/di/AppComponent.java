package com.shadiz.uzergin.robotcontroller.di;

import android.content.Context;
import android.content.res.Resources;

import com.shadiz.uzergin.robotcontroller.MapsActivity;
import com.shadiz.uzergin.robotcontroller.di.modules.AppModule;
import com.shadiz.uzergin.robotcontroller.service.SensorService;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by OldMan on 07.11.2016.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Context getContext();
    EventBus getEventBus();
    Resources getResources();

    Calendar getCalendar();

    void inject(MapsActivity activity);

    void inject(SensorService sensorService);

}
