package ru.devsp.apps.market.repository.bounds;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

import ru.devsp.apps.market.model.api.ApiResponse;
import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.db.CacheDao;
import ru.devsp.apps.market.model.db.GoodsDao;
import ru.devsp.apps.market.model.db.ImagesDao;
import ru.devsp.apps.market.model.objects.Cache;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.model.objects.Image;
import ru.devsp.apps.market.tools.AppExecutors;

/**
 * Получение и кэширование товара
 * Created by gen on 24.12.2017.
 */

public class GoodBound extends CachedNetworkBoundResource<Good, Good> {

    public static final String METHOD = "GetGood";
    private static final String TYPE = Good.class.getSimpleName() + "::" + METHOD;

    private CacheDao mCacheDao;
    private GoodsDao mGoodsDao;
    private ImagesDao mImagesDao;
    private MarketApi mApi;

    private String mCacheKey;

    public GoodBound(AppExecutors appExecutors, MarketApi api, GoodsDao goodsDao, ImagesDao imagesDao, CacheDao cacheDao) {
        super(appExecutors);
        mApi = api;
        mGoodsDao = goodsDao;
        mImagesDao = imagesDao;
        mCacheDao = cacheDao;
    }

    public GoodBound setParams(RequestParams params) {
        mParams = params;
        return this;
    }

    @Override
    protected void saveCallResult(@NonNull Good data) {
        if (data.images != null) {
            String cacheKey = getCacheKey(data.id);
            mImagesDao.clearOld(cacheKey);
            for (String image : data.images) {
                Image img = new Image(image);
                img.cacheKey = cacheKey;
                mImagesDao.insert(img);
            }
        }
        Cache cache = new Cache(getCacheKey(),
                new Date().getTime() + getCacheTime(mCacheDao.getCacheType(TYPE)));
        mCacheDao.insert(cache);
        data.cacheKey = mCacheKey;
        mGoodsDao.insert(data);
    }

    @Override
    protected LiveData<Cache> loadCache() {
        return mCacheDao.getCache(getCacheKey());
    }

    @Override
    public String getCacheKey() {
        return TYPE + "-" + mParams.getParam(RequestParams.PARAM_GOOD);
    }

    public static String getCacheKey(long id){
        return TYPE + "-" + id;
    }

    @Override
    protected boolean shouldFetch(@Nullable Good data) {
        if(data != null){
            mCacheKey = data.cacheKey;
        }
        return (data == null || checkExpireCache(mCacheExpire));
    }

    @NonNull
    @Override
    protected LiveData<Good> loadSaved() {
        return mGoodsDao.getGood(Long.valueOf(mParams.getParam(RequestParams.PARAM_GOOD)));
    }

    @NonNull
    @Override
    protected LiveData<ApiResponse<Good>> createCall() {
        return mApi.getGood(mParams);
    }

}