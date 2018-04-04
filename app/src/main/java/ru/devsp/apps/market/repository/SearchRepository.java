package ru.devsp.apps.market.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.db.CacheDao;
import ru.devsp.apps.market.model.db.SearchDao;
import ru.devsp.apps.market.model.objects.SearchGood;
import ru.devsp.apps.market.repository.bounds.SearchBound;
import ru.devsp.apps.market.tools.AppExecutors;

/**
 * Репозиторий данных по товарам
 * Created by gen on 22.12.2017.
 */
@Singleton
public class SearchRepository {

    final AppExecutors mAppExecutors;
    private MarketApi mApi;
    private CacheDao mCacheDao;
    private SearchDao mSearchDao;

    @Inject
    public SearchRepository(AppExecutors appExecutors, MarketApi api, SearchDao searchDao, CacheDao cacheDao) {
        mAppExecutors = appExecutors;
        mApi = api;
        mSearchDao = searchDao;
        mCacheDao = cacheDao;
    }

    /**
     * Поиск товаров
     *
     * @return
     */
    public LiveData<Resource<List<SearchGood>>> search(RequestParams params) {
        SearchBound bound =
                new SearchBound(mAppExecutors, mApi, mSearchDao, mCacheDao).setParams(params);
        bound.create();
        return bound.asLiveData();
    }

}
