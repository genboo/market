package ru.devsp.apps.market.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.devsp.apps.market.model.objects.OrderGood;


/**
 * Получение данных по сохраненным товарами заказа
 * Created by gen on 31.08.2017.
 */
@Dao
public interface OrderGoodsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrderGood item);

    @Query("SELECT c.* FROM OrderGood c WHERE c.cache_key = :key")
    LiveData<List<OrderGood>> getOrderGoods(String key);

    @Query("DELETE FROM `OrderGood` WHERE cache_key = :key")
    void clearOld(String key);
}
