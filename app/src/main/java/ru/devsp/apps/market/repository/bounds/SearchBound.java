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
import ru.devsp.apps.market.model.db.SearchDao;
import ru.devsp.apps.market.model.objects.Cache;
import ru.devsp.apps.market.model.objects.SearchGood;
import ru.devsp.apps.market.tools.AppExecutors;

/**
 * Получение и кэширование товаров
 * Created by gen on 24.12.2017.
 */

public class SearchBound extends CachedNetworkBoundResource<List<SearchGood>, List<SearchGood>> {

    public static final String METHOD = "Search";
    private static final String TYPE = SearchGood.class.getSimpleName() + "::" + METHOD;

    private CacheDao mCacheDao;
    private SearchDao mSearchDao;
    private MarketApi mApi;

    public SearchBound(AppExecutors appExecutors, MarketApi api, SearchDao searchDao, CacheDao cacheDao) {
        super(appExecutors);
        mApi = api;
        mSearchDao = searchDao;
        mCacheDao = cacheDao;
    }

    public SearchBound setParams(RequestParams params) {
        mParams = params;
        return this;
    }

    @Override
    protected void saveCallResult(@NonNull List<SearchGood> data) {
        if (!data.isEmpty()) {
            String cacheKey = getCacheKey();
            mSearchDao.clearOld(cacheKey);
            for(SearchGood item : data){
                item.cacheKey = cacheKey;
                mSearchDao.insert(item);
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
        return TYPE + "-" + mParams.getParam(RequestParams.PARAM_QUERY);
    }

    @Override
    protected boolean shouldFetch(@Nullable List<SearchGood> data) {
        return (data == null || data.isEmpty() || checkExpireCache(mCacheExpire));
    }

    @NonNull
    @Override
    protected LiveData<List<SearchGood>> loadSaved() {
        return mSearchDao.getSearchGoods(getCacheKey());
    }

    @NonNull
    @Override
    protected LiveData<ApiResponse<List<SearchGood>>> createCall() {
        return mApi.search(mParams);
    }

}