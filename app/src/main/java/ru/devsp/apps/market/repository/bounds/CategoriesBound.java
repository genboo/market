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
import ru.devsp.apps.market.model.db.CategoriesDao;
import ru.devsp.apps.market.model.objects.Cache;
import ru.devsp.apps.market.model.objects.Category;
import ru.devsp.apps.market.tools.AppExecutors;

/**
 * Получение и кэширование категорий
 * Created by gen on 24.12.2017.
 */

public class CategoriesBound extends CachedNetworkBoundResource<List<Category>, List<Category>> {

    public static final String METHOD = "GetCategories";
    private static final String TYPE = Category.class.getSimpleName() + "::" + METHOD;

    private CacheDao mCacheDao;
    private CategoriesDao mCategoriesDao;
    private MarketApi mApi;

    public CategoriesBound(AppExecutors appExecutors, MarketApi api, CategoriesDao categoriesDao, CacheDao cacheDao) {
        super(appExecutors);
        mApi = api;
        mCategoriesDao = categoriesDao;
        mCacheDao = cacheDao;
    }

    public CategoriesBound setParams(RequestParams params) {
        mParams = params;
        return this;
    }

    @Override
    protected void saveCallResult(@NonNull List<Category> data) {
        if (!data.isEmpty()) {
            String cacheKey = getCacheKey();
            mCategoriesDao.clearOld(cacheKey);
            for(Category item : data){
                item.cacheKey = cacheKey;
                mCategoriesDao.insert(item);
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
    protected boolean shouldFetch(@Nullable List<Category> data) {
        return (data == null || data.isEmpty() || checkExpireCache(mCacheExpire));
    }

    @NonNull
    @Override
    protected LiveData<List<Category>> loadSaved() {
        return mCategoriesDao.getCategories(getCacheKey());
    }

    @NonNull
    @Override
    protected LiveData<ApiResponse<List<Category>>> createCall() {
        return mApi.getCategories(mParams);
    }

}