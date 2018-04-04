package ru.devsp.apps.market;

import android.app.Application;

import ru.devsp.apps.market.di.components.AppComponent;
import ru.devsp.apps.market.di.components.DaggerAppComponent;


/**
 * Приложение
 * Created by gen on 21.08.2017.
 */

public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder().context(this).build();
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }

}
