package ru.devsp.apps.market.di.modules;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.devsp.apps.market.model.db.CacheDao;
import ru.devsp.apps.market.model.db.CartDao;
import ru.devsp.apps.market.model.db.CategoriesDao;
import ru.devsp.apps.market.model.db.GoodsDao;
import ru.devsp.apps.market.model.db.ImagesDao;
import ru.devsp.apps.market.model.db.MarketDatabase;
import ru.devsp.apps.market.model.db.OrderGoodsDao;
import ru.devsp.apps.market.model.db.OrdersDao;
import ru.devsp.apps.market.model.db.SearchDao;

/**
 * Инициализация базы данных
 * Created by gen on 12.09.2017.
 */
@Module
public class DbModule {

    private static final String DB_NAME = "market";

    /**
     * Список миграций
     */
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //example
        }
    };



    /**
     * Базовый провайдер базы данных
     *
     * @param context Контекст
     * @return База данных
     */
    @Provides
    @Singleton
    MarketDatabase provideDatabase(Context context) {
        return Room
                .databaseBuilder(context, MarketDatabase.class, DB_NAME)
                .addMigrations(
                        MIGRATION_1_2
                )
                .build();
    }

    /**
     * Сохраненные товары
     *
     * @param db База данных
     * @return Dao сохраненных товаров
     */
    @Provides
    @Singleton
    GoodsDao provideGoodsDao(MarketDatabase db) {
        return db.goodsDao();
    }

    /**
     * Сохраненные изображения товаров
     *
     * @param db База данных
     * @return Dao сохраненных изображений
     */
    @Provides
    @Singleton
    ImagesDao provideImagesDao(MarketDatabase db) {
        return db.imagesDao();
    }

    /**
     * Срхраненные результаты поиска
     *
     * @param db База данных
     * @return Dao сохраненных результатов
     */
    @Provides
    @Singleton
    SearchDao provideSearchDao(MarketDatabase db) {
        return db.searchDao();
    }

    /**
     * Товары в корзине
     *
     * @param db База данных
     * @return Dao товаров в корзине
     */
    @Provides
    @Singleton
    CartDao provideCartDao(MarketDatabase db) {
        return db.cartDao();
    }

    /**
     * Сохраненные категории
     *
     * @param db База данных
     * @return Dao сохраненных категорий
     */
    @Provides
    @Singleton
    CategoriesDao provideCategoriesDao(MarketDatabase db) {
        return db.categoriesDao();
    }

    /**
     * Сохраненные заказы
     *
     * @param db База данных
     * @return Dao сохраненных категорий
     */
    @Provides
    @Singleton
    OrdersDao provideOrdersDao(MarketDatabase db) {
        return db.ordersDao();
    }

    /**
     * Сохраненные товары в заказе
     *
     * @param db База данных
     * @return Dao сохраненных категорий
     */
    @Provides
    @Singleton
    OrderGoodsDao provideOrderGoodsDao(MarketDatabase db) {
        return db.orderGoodsDao();
    }

    /**
     * Доступ к настройкам кэша
     *
     * @param db База данных
     * @return Dao кэша
     */
    @Provides
    @Singleton
    CacheDao provideCacheDao(MarketDatabase db) {
        return db.cacheDao();
    }
}
