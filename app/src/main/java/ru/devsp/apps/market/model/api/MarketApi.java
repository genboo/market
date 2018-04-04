package ru.devsp.apps.market.model.api;

import android.arch.lifecycle.LiveData;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.devsp.apps.market.model.objects.Category;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.model.objects.Order;
import ru.devsp.apps.market.model.objects.OrderGood;
import ru.devsp.apps.market.model.objects.SearchGood;

/**
 *
 * Created by gen on 02.10.2017.
 */

public interface MarketApi {

    @POST("/api.php")
    LiveData<ApiResponse<List<Good>>> getGoods(@Body RequestParams params);

    @POST("/api.php")
    LiveData<ApiResponse<List<SearchGood>>> search(@Body RequestParams params);

    @POST("/api.php")
    LiveData<ApiResponse<Good>> getGood(@Body RequestParams params);

    @POST("/api.php")
    LiveData<ApiResponse<List<Category>>> getCategories(@Body RequestParams params);

    @POST("/api.php")
    LiveData<ApiResponse<Order>> checkout(@Body RequestParams params);

    @POST("/api.php")
    LiveData<ApiResponse<List<OrderGood>>> getOrderItems(@Body RequestParams params);
}
