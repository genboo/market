package ru.devsp.apps.market.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.db.CacheDao;
import ru.devsp.apps.market.model.db.OrderGoodsDao;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.model.objects.OrderGood;
import ru.devsp.apps.market.repository.bounds.OrderGoodsBound;
import ru.devsp.apps.market.tools.AppExecutors;

/**
 * Репозиторий данных по товарам
 * Created by gen on 22.12.2017.
 */
@Singleton
public class OrderGoodsRepository {

    final AppExecutors mAppExecutors;
    private MarketApi mApi;
    private CacheDao mCacheDao;
    private OrderGoodsDao mOrderGoodsDao;

    @Inject
    public OrderGoodsRepository(AppExecutors appExecutors, MarketApi api, OrderGoodsDao goodsDao, CacheDao cacheDao) {
        mAppExecutors = appExecutors;
        mApi = api;
        mOrderGoodsDao = goodsDao;
        mCacheDao = cacheDao;
    }

    /**
     * Получение списка товаров
     *
     * @return
     */
    public LiveData<Resource<List<OrderGood>>> getOrderGoods(RequestParams params) {
        OrderGoodsBound bound =
                new OrderGoodsBound(mAppExecutors, mApi, mOrderGoodsDao, mCacheDao).setParams(params);
        bound.create();
        return bound.asLiveData();
    }

}
