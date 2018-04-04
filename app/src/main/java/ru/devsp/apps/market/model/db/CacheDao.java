package ru.devsp.apps.market.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import ru.devsp.apps.market.model.objects.Cache;
import ru.devsp.apps.market.model.objects.CacheTime;

/**
 * Управление кэшированием
 * Created by gen on 14.09.2017.
 */
@Dao
public interface CacheDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cache cache);

    @Query("SELECT * FROM Cache WHERE cache_key = :key")
    LiveData<Cache> getCache(String key);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CacheTime cache);

    @Query("select * from CacheTime where cache_type = :type")
    CacheTime getCacheType(String type);

}
