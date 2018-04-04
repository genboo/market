package ru.devsp.apps.market.repository.bounds;

import android.arch.lifecycle.LiveData;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;


import java.util.Date;

import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.objects.Cache;
import ru.devsp.apps.market.model.objects.CacheTime;
import ru.devsp.apps.market.tools.AppExecutors;


/**
 * Получение данных из сети и их кэширование
 * Created by gen on 31.08.2017.
 */

abstract class CachedNetworkBoundResource<R, Q> extends NetworkBoundResource<R, Q> {

    /**
     * Время кэширования по умолчанию, 30 минут
     */
    private static final int DEFAULT_CACHE_TIME = 30 * 60 * 1000;

    long mCacheExpire = 0;

    @MainThread
    CachedNetworkBoundResource(AppExecutors appExecutors) {
        super(appExecutors);
    }

    @Override
    public void create() {
        result.setValue(Resource.loading(null));
        LiveData<Cache> dbCache = loadCache();
        result.addSource(dbCache, cache -> {
            result.removeSource(dbCache);
            if (cache != null) {
                mCacheExpire = cache.expire;
            }
            super.create();
        });
    }

    @WorkerThread
    int getCacheTime(CacheTime cacheTime){
        return cacheTime != null ? cacheTime.time : DEFAULT_CACHE_TIME;
    }

    /**
     * Проверка валидности кэша
     *
     * @return Если вернуло <b>true</b>, то время хранения кэша истекло
     */
    boolean checkExpireCache(long expire) {
        return expire < new Date().getTime();
    }

    @MainThread
    protected abstract LiveData<Cache> loadCache();

    protected abstract String getCacheKey();

}
