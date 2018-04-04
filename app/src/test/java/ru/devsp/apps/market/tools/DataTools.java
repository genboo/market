package ru.devsp.apps.market.tools;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.devsp.apps.market.model.objects.Cache;
import ru.devsp.apps.market.model.objects.Category;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.model.objects.Order;
import ru.devsp.apps.market.model.objects.OrderGood;
import ru.devsp.apps.market.model.objects.SearchGood;

/**
 * Создание тестовых объектов
 * Created by gen on 28.09.2017.
 */

public class DataTools {

    private static final String CATEGORY_TITLE = "Категория";
    private static final String GOOD_TITLE = "Крем";

    /**
     * Создание объекта кэша
     * @param key Ключ
     * @param expired Просроченный или нет
     * @return
     */
    public static Cache createCache(String key, boolean expired){
        return new Cache(key, new Date().getTime() + (expired ? -30 * 1000 : 30 * 1000));
    }


    /**
     * Список категорий
     * @return
     */
    public static List<Category> createCategories() {
        List<Category> items = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Category item = new Category(i, CATEGORY_TITLE + i);
            items.add(item);
        }
        return items;
    }


    /**
     * Список товаров
     * @return
     */
    public static List<Good> createGoods() {
        List<Good> items = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Good item = new Good(i, GOOD_TITLE + i);
            items.add(item);
        }
        return items;
    }

    /**
     * Список товаров заказа
     * @return
     */
    public static List<OrderGood> createOrderGoods() {
        List<OrderGood> items = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            OrderGood item = new OrderGood();
            item.id = i;
            item.sid = i;
            item.title = GOOD_TITLE + i;
            items.add(item);
        }
        return items;
    }

    /**
     * Список найденых товаров
     * @return
     */
    public static List<SearchGood> createSearchGoods() {
        List<SearchGood> items = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            SearchGood item = new SearchGood();
            item.id = i;
            item.title = "good " + i;
            items.add(item);
        }
        return items;
    }


    /**
     * Товар
     * @return
     */
    public static Good createGood() {
        Good item = new Good(1, GOOD_TITLE);
        return item;
    }

    /**
     * Заказ
     * @return
     */
    public static Order createOrder() {
        Order item = new Order();
        item.id = 1;
        item.price = 599f;
        item.count = 3;
        item.status = "В работе";
        item.time = "2018-01-15 15:10:12";
        return item;
    }

    /**
     * Набор изображений
     * @return
     */
    public static String[] createImages() {
        return new String[]{
                "https://images.faberlic.com/images/fl/TflGoods/md/1000333163657_15042692191.jpg",
                "https://images.faberlic.com/images/fl/TflGoods/md/1000333163657_15042692191.jpg"
        };
    }


}
