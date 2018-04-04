package ru.devsp.apps.market.repository.bounds;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

import ru.devsp.apps.market.model.api.ApiResponse;
import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.db.CacheDao;
import ru.devsp.apps.market.model.db.OrderGoodsDao;
import ru.devsp.apps.market.model.objects.Cache;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.model.objects.OrderGood;
import ru.devsp.apps.market.tools.AppExecutors;

/**
 * Получение и кэширование товаров в заказе
 * Created by gen on 24.12.2017.
 */

public class OrderGoodsBound extends CachedNetworkBoundResource<List<OrderGood>, List<OrderGood>> {

    public static final String METHOD = "GetOrderItems";
    private static final String TYPE = Good.class.getSimpleName() + "::" + METHOD;

    private CacheDao mCacheDao;
    private OrderGoodsDao mOrderGoodsDao;
    private MarketApi mApi;

    public OrderGoodsBound(AppExecutors appExecutors, MarketApi api, OrderGoodsDao orderGoodsDao, CacheDao cacheDao) {
        super(appExecutors);
        mApi = api;
        mOrderGoodsDao = orderGoodsDao;
        mCacheDao = cacheDao;
    }

    public OrderGoodsBound setParams(RequestParams params) {
        mParams = params;
        return this;
    }

    @Override
    protected void saveCallResult(@NonNull List<OrderGood> data) {
        if (!data.isEmpty()) {
            String cacheKey = getCacheKey();
            mOrderGoodsDao.clearOld(cacheKey);
            for(OrderGood item : data){
                item.cacheKey = cacheKey;
                mOrderGoodsDao.insert(item);
            }
            Cache cache = new Cache(cacheKey,
                    new Date().getTime() + getCacheTime(mCacheDao.getCacheType(TYPE)));
            mCacheDao.insert(cache);
        }
    }

    @Override
    protected LiveData<Cache> loadCache() {
        return mCacheDao.getCache(getCacheKey());
    }

    @Override
    protected String getCacheKey() {
        return TYPE + "-" + mParams.getParam(RequestParams.PARAM_ORDER);
    }

    @Override
    protected boolean shouldFetch(@Nullable List<OrderGood> data) {
        return (data == null || data.isEmpty() || checkExpireCache(mCacheExpire));
    }

    @NonNull
    @Override
    protected LiveData<List<OrderGood>> loadSaved() {
        return mOrderGoodsDao.getOrderGoods(getCacheKey());
    }

    @NonNull
    @Override
    protected LiveData<ApiResponse<List<OrderGood>>> createCall() {
        return mApi.getOrderItems(mParams);
    }

}