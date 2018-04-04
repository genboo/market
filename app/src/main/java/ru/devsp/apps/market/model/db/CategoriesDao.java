package ru.devsp.apps.market.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.devsp.apps.market.model.objects.Category;


/**
 * Получение данных по сохраненным категориям
 * Created by gen on 31.08.2017.
 */
@Dao
public interface CategoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category item);

    @Query("SELECT c.* FROM Category c WHERE c.cache_key = :key")
    LiveData<List<Category>> getCategories(String key);

    @Query("DELETE FROM `Category` WHERE cache_key = :key")
    void clearOld(String key);
}
