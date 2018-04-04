package ru.devsp.apps.market.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.devsp.apps.market.model.objects.Cache;
import ru.devsp.apps.market.model.objects.CacheTime;
import ru.devsp.apps.market.model.objects.Cart;
import ru.devsp.apps.market.model.objects.Category;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.model.objects.Image;
import ru.devsp.apps.market.model.objects.Order;
import ru.devsp.apps.market.model.objects.OrderGood;
import ru.devsp.apps.market.model.objects.SearchGood;


/**
 * База данных
 * Created by gen on 31.08.2017.
 */
@Database(version = 1, entities = {
        Good.class,
        Category.class,
        Image.class,
        Cart.class,
        SearchGood.class,
        Order.class,
        OrderGood.class,
        Cache.class,
        CacheTime.class,
})
public abstract class MarketDatabase extends RoomDatabase {

    public abstract GoodsDao goodsDao();

    public abstract CategoriesDao categoriesDao();

    public abstract OrdersDao ordersDao();

    public abstract ImagesDao imagesDao();

    public abstract CartDao cartDao();

    public abstract SearchDao searchDao();

    public abstract OrderGoodsDao orderGoodsDao();

    public abstract CacheDao cacheDao();

}
