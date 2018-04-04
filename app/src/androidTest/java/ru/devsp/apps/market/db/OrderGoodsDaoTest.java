package ru.devsp.apps.market.db;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import ru.devsp.apps.market.model.objects.OrderGood;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.devsp.apps.market.tools.LiveDataTestUtil.getValue;

/**
 * Тестирование базы: товары в заказе
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class OrderGoodsDaoTest extends DbTest {

    private static final String CACHE_KEY = "ordergoods::1";
    private static final String TITLE= "Духи";
    private static final float PRICE = 159f;
    private static final int COUNT = 3;
    private static final String IMAGE= "http://test.org/image.png";

    @Test
    public void insertAndRead() throws IOException, InterruptedException {
        db.orderGoodsDao().insert(getOrderGoods(1));
        db.orderGoodsDao().insert(getOrderGoods(2));

        List<OrderGood> inserted = getValue(db.orderGoodsDao().getOrderGoods(CACHE_KEY));
        assertThat(inserted, notNullValue());
        assertThat(inserted.isEmpty(), is(false));
        assertThat(inserted.size(), is(2));
        assertThat(inserted.get(0).title, is(TITLE));
        assertThat(inserted.get(0).count, is(COUNT));
        assertThat(inserted.get(0).sid, is(1L));
        assertThat(inserted.get(0).image, is(IMAGE));
        assertThat(inserted.get(0).price, is(PRICE));
    }


    @Test
    public void clearOld() throws IOException, InterruptedException {
        db.orderGoodsDao().insert(getOrderGoods(1));
        db.orderGoodsDao().clearOld(CACHE_KEY);
        List<OrderGood> inserted = getValue(db.orderGoodsDao().getOrderGoods(CACHE_KEY));
        assertThat(inserted.isEmpty(), is(true));
    }


    private OrderGood getOrderGoods(long id){
        OrderGood item = new OrderGood();
        item.cacheKey = CACHE_KEY;
        item.title = TITLE;
        item.count = COUNT;
        item.price = PRICE;
        item.sid = id;
        item.image = IMAGE;
        return item;
    }
}
