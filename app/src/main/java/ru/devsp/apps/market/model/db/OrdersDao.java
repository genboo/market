package ru.devsp.apps.market.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;
import ru.devsp.apps.market.model.objects.Order;


/**
 * Получение данных по сохраненным заказам
 * Created by gen on 31.08.2017.
 */
@Dao
public interface OrdersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Order item);

    @Query("SELECT o.* FROM `Order` o ORDER BY o.time DESC")
    LiveData<List<Order>> getOrders();

    @Query("SELECT o.* FROM `Order` o WHERE o.id = :id")
    LiveData<Order> getOrder(long id);

}
