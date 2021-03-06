package ru.devsp.apps.market.di.modules;


import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.devsp.apps.market.BuildConfig;
import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.tools.LiveDataCallAdapterFactory;
import ru.devsp.apps.market.model.api.tools.ResultTypeAdapterFactory;

/**
 * Инициализация Retrofit
 * Created by gen on 12.09.2017.
 */

@Module
public class RetrofitModule {

    private static final long READ_TIMEOUT = 30;
    private static final long CONNECT_TIMEOUT = 30;

    /**
     * Базовый провайдер Retrofit
     * @return Retrofit
     */
    @Singleton
    @Provides
    Retrofit provideRetrofit(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);

        setDebugOptions(httpClient);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new ResultTypeAdapterFactory());

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(httpClient.build())
                .build();
    }

    /**
     * API
     * @param retrofit Retrofit
     * @return API
     */
    @Singleton
    @Provides
    MarketApi provideMarketApi(Retrofit retrofit){
        return retrofit.create(MarketApi.class);
    }

    /**
     * Устанавливает опции для отладки
     * @param httpClient Клиент
     */
    private void setDebugOptions(OkHttpClient.Builder httpClient){
        if (BuildConfig.DEBUG_MODE) {
            //Логгер в режиме отладки для всех запросов
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }
    }
}
