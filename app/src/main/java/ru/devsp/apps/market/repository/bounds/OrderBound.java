package ru.devsp.apps.market.repository.bounds;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import ru.devsp.apps.market.model.api.ApiResponse;
import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.db.OrdersDao;
import ru.devsp.apps.market.model.objects.Order;
import ru.devsp.apps.market.tools.AppExecutors;

/**
 * Создание заказа
 * Created by gen on 24.12.2017.
 */

public class OrderBound extends NetworkBoundResource<Order, Order> {

    public static final String METHOD = "Checkout";

    private OrdersDao mOrderDao;
    private MarketApi mApi;

    private long id;

    public OrderBound(AppExecutors appExecutors, MarketApi api, OrdersDao orderDao) {
        super(appExecutors);
        mApi = api;
        mOrderDao = orderDao;
    }

    public OrderBound setParams(RequestParams params) {
        mParams = params;
        return this;
    }


    @Override
    protected void saveCallResult(@NonNull Order item) {
        id = item.id;
        mOrderDao.insert(item);
    }

    @Override
    protected boolean shouldFetch(@Nullable Order data) {
        return data == null;
    }

    @NonNull
    @Override
    protected LiveData<Order> loadSaved() {
        return mOrderDao.getOrder(id);
    }

    @NonNull
    @Override
    protected LiveData<ApiResponse<Order>> createCall() {
        return mApi.checkout(mParams);
    }
}