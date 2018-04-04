package ru.devsp.apps.market.tests.api;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.gson.GsonBuilder;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.api.tools.LiveDataCallAdapterFactory;
import ru.devsp.apps.market.model.api.tools.ResultTypeAdapterFactory;

/**
 *
 * Created by gen on 15.09.2017.
 */
abstract public class ApiTest {

    private MockWebServer mockWebServer;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    <T> T createService(final Class<T> service) throws IOException {
        mockWebServer = new MockWebServer();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new ResultTypeAdapterFactory());

        return new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(httpClient.build())
                .build()
                .create(service);
    }

    @After
    public void stopService() throws IOException {
        mockWebServer.shutdown();
    }

    void enqueueResponse(String object, RequestParams params) throws IOException {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("api-response/" + object + "-" + params.getMethod() + ".json");
        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        MockResponse mockResponse = new MockResponse();

        mockWebServer.enqueue(mockResponse
                .setBody(source.readString(Charset.defaultCharset())));
    }

}
