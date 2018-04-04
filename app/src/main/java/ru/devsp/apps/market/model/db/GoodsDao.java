package ru.devsp.apps.market.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.devsp.apps.market.model.objects.Good;

/**
 * Получение данных по сохраненным товарам
 * Created by gen on 31.08.2017.
 */
@Dao
public interface GoodsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Good item);

    @Delete
    void delete(Good item);

    @Query("SELECT g.* FROM Good g WHERE g.id = :id")
    LiveData<Good> getGood(long id);

    @Query("SELECT g.* FROM Good g WHERE g.cache_key = :key")
    LiveData<List<Good>> getGoods(String key);

    @Query("DELETE FROM `Good` WHERE cache_key = :key")
    void clearOld(String key);

}
