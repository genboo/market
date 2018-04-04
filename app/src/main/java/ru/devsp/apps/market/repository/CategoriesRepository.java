package ru.devsp.apps.market.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.db.CacheDao;
import ru.devsp.apps.market.model.db.CategoriesDao;
import ru.devsp.apps.market.model.objects.Category;
import ru.devsp.apps.market.repository.bounds.CategoriesBound;
import ru.devsp.apps.market.tools.AppExecutors;

/**
 * Репозиторий данных по категориям
 * Created by gen on 22.12.2017.
 */
@Singleton
public class CategoriesRepository {

    final AppExecutors mAppExecutors;
    private MarketApi mApi;
    private CacheDao mCacheDao;
    private CategoriesDao mCategoriesDao;

    @Inject
    public CategoriesRepository(AppExecutors appExecutors, MarketApi api, CategoriesDao categoriesDao, CacheDao cacheDao) {
        mAppExecutors = appExecutors;
        mApi = api;
        mCategoriesDao = categoriesDao;
        mCacheDao = cacheDao;
    }

    /**
     * Получение списка категорий
     *
     * @return
     */
    public LiveData<Resource<List<Category>>> getCategories(RequestParams params) {
        CategoriesBound bound =
                new CategoriesBound(mAppExecutors, mApi, mCategoriesDao, mCacheDao).setParams(params);
        bound.create();
        return bound.asLiveData();
    }

}
