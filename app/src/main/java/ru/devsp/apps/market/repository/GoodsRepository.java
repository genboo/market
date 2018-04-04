package ru.devsp.apps.market.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.db.CacheDao;
import ru.devsp.apps.market.model.db.GoodsDao;
import ru.devsp.apps.market.model.db.ImagesDao;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.repository.bounds.GoodBound;
import ru.devsp.apps.market.repository.bounds.GoodsBound;
import ru.devsp.apps.market.tools.AppExecutors;

/**
 * Репозиторий данных по товарам
 * Created by gen on 22.12.2017.
 */
@Singleton
public class GoodsRepository {

    final AppExecutors mAppExecutors;
    private MarketApi mApi;
    private CacheDao mCacheDao;
    private GoodsDao mGoodsDao;
    private ImagesDao mImagesDao;

    @Inject
    public GoodsRepository(AppExecutors appExecutors, MarketApi api, GoodsDao goodsDao, ImagesDao imagesDao, CacheDao cacheDao) {
        mAppExecutors = appExecutors;
        mApi = api;
        mGoodsDao = goodsDao;
        mImagesDao = imagesDao;
        mCacheDao = cacheDao;
    }

    /**
     * Получение списка товаров
     *
     * @return
     */
    public LiveData<Resource<List<Good>>> getGoods(RequestParams params) {
        GoodsBound bound =
                new GoodsBound(mAppExecutors, mApi, mGoodsDao, mCacheDao).setParams(params);
        bound.create();
        return bound.asLiveData();
    }

    /**
     * Получение товара
     *
     * @return
     */
    public LiveData<Resource<Good>> getGood(RequestParams params) {
        GoodBound bound =
                new GoodBound(mAppExecutors, mApi, mGoodsDao, mImagesDao, mCacheDao).setParams(params);
        bound.create();
        return bound.asLiveData();
    }

    public LiveData<String[]> getImages(long id){
        return mImagesDao.getImages(GoodBound.getCacheKey(id));
    }


}
