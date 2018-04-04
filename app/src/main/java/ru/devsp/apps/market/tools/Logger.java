package ru.devsp.apps.market.tools;

import android.util.Log;
import ru.devsp.apps.market.BuildConfig;


/**
 * Логирование
 * Created by gen on 30.08.2017.
 */

public class Logger {

    private Logger(){
        //empty constructor
    }

    private static final String DEFAULT_LOGGER = BuildConfig.APPLICATION_ID;

    public static void e(String tag, String message){
        if(BuildConfig.DEBUG){
            Log.e(tag, message);
        }
    }

    public static void e(String tag, Exception message){
        if(BuildConfig.DEBUG) {
            Logger.e(tag, message.getMessage());
        }
    }

    public static void e(Exception message){
        if(BuildConfig.DEBUG) {
            Logger.e(DEFAULT_LOGGER, message.getMessage());
        }
    }

    public static void e(String message){
        if(BuildConfig.DEBUG) {
            Logger.e(DEFAULT_LOGGER, message);
        }
    }

    public static void d(String tag, String message){
        if(BuildConfig.DEBUG){
            Log.d(tag, message);
        }
    }

    public static void d(String tag, Exception message){
        Logger.d(tag, message.getMessage());
    }

    public static void d(Exception message){
        Logger.d(DEFAULT_LOGGER, message.getMessage());
    }

    public static void d(String message){
        Logger.d(DEFAULT_LOGGER, message);
    }
}
