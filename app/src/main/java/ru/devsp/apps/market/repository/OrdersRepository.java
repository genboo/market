package ru.devsp.apps.market.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.db.CategoriesDao;
import ru.devsp.apps.market.model.db.OrdersDao;
import ru.devsp.apps.market.model.objects.Category;
import ru.devsp.apps.market.model.objects.Order;
import ru.devsp.apps.market.repository.bounds.CategoriesBound;
import ru.devsp.apps.market.repository.bounds.OrderBound;
import ru.devsp.apps.market.tools.AppExecutors;

/**
 * Репозиторий данных по заказам
 * Created by gen on 22.12.2017.
 */
@Singleton
public class OrdersRepository {

    final AppExecutors mAppExecutors;
    private MarketApi mApi;
    private OrdersDao mOrdersDao;

    @Inject
    public OrdersRepository(AppExecutors appExecutors, MarketApi api, OrdersDao ordersDao) {
        mAppExecutors = appExecutors;
        mApi = api;
        mOrdersDao = ordersDao;
    }

    public LiveData<List<Order>> getOrders(){
        return mOrdersDao.getOrders();
    }

    /**
     * Получение списка категорий
     *
     * @return
     */
    public LiveData<Resource<Order>> checkout(RequestParams params) {
        OrderBound bound =
                new OrderBound(mAppExecutors, mApi, mOrdersDao).setParams(params);
        bound.create();
        return bound.asLiveData();
    }

}
