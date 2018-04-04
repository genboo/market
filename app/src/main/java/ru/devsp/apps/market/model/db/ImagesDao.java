package ru.devsp.apps.market.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import ru.devsp.apps.market.model.objects.Image;


/**
 * Получение данных по сохраненным изображениям
 * Created by gen on 31.08.2017.
 */
@Dao
public interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Image item);

    @Query("SELECT i.image FROM Image i WHERE i.cache_key = :key")
    LiveData<String[]> getImages(String key);

    @Query("DELETE FROM `Image` WHERE cache_key = :key")
    void clearOld(String key);

}
