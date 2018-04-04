package ru.devsp.apps.market.tools;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import retrofit2.Response;
import ru.devsp.apps.market.model.api.ApiResponse;
import ru.devsp.apps.market.model.api.RequestParams;

/**
 *
 * Created by gen on 15.12.2017.
 */

public class ApiTools {

    public static RequestParams getParams(String method){
        RequestParams params = new RequestParams();
        params.setMethod(method);
        return params;
    }

    public static <T> LiveData<ApiResponse<T>> successCall(T data) {
        return createCall(Response.success(data));
    }

    public static <T> LiveData<ApiResponse<T>> createCall(Response<T> response) {
        MutableLiveData<ApiResponse<T>> data = new MutableLiveData<>();
        data.setValue(new ApiResponse<>(response));
        return data;
    }
}
