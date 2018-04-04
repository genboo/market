package ru.devsp.apps.market.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.devsp.apps.market.model.objects.SearchGood;


/**
 * Получение данных по сохраненным поисковым запросам
 * Created by gen on 31.08.2017.
 */
@Dao
public interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchGood item);

    @Query("SELECT g.* FROM SearchGood g WHERE g.cache_key = :key")
    LiveData<List<SearchGood>> getSearchGoods(String key);

    @Query("DELETE FROM `SearchGood` WHERE cache_key = :key")
    void clearOld(String key);
}
