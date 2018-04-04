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
import ru.devsp.apps.market.model.db.GoodsDao;
import ru.devsp.apps.market.model.objects.Cache;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.tools.AppExecutors;

/**
 * Получение и кэширование товаров
 * Created by gen on 24.12.2017.
 */

public class GoodsBound extends CachedNetworkBoundResource<List<Good>, List<Good>> {

    public static final String METHOD = "GetGoods";
    private static final String TYPE = Good.class.getSimpleName() + "::" + METHOD;

    private CacheDao mCacheDao;
    private GoodsDao mGoodsDao;
    private MarketApi mApi;

    public GoodsBound(AppExecutors appExecutors, MarketApi api, GoodsDao goodsDao, CacheDao cacheDao) {
        super(appExecutors);
        mApi = api;
        mGoodsDao = goodsDao;
        mCacheDao = cacheDao;
    }

    public GoodsBound setParams(RequestParams params) {
        mParams = params;
        return this;
    }

    @Override
    protected void saveCallResult(@NonNull List<Good> data) {
        if (!data.isEmpty()) {
            String cacheKey = getCacheKey();
            mGoodsDao.clearOld(cacheKey);
            for(Good item : data){
                item.cacheKey = cacheKey;
                mGoodsDao.insert(item);
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
        return TYPE + "-" + mParams.getParam(RequestParams.PARAM_CATEGORY);
    }

    @Override
    protected boolean shouldFetch(@Nullable List<Good> data) {
        return (data == null || data.isEmpty() || checkExpireCache(mCacheExpire));
    }

    @NonNull
    @Override
    protected LiveData<List<Good>> loadSaved() {
        return mGoodsDao.getGoods(getCacheKey());
    }

    @NonNull
    @Override
    protected LiveData<ApiResponse<List<Good>>> createCall() {
        return mApi.getGoods(mParams);
    }

}