package ru.devsp.apps.market.db;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import ru.devsp.apps.market.model.objects.Order;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.devsp.apps.market.tools.LiveDataTestUtil.getValue;

/**
 * Тестирование базы: заказ
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class OrdersDaoTest extends DbTest {

    private static final float PRICE = 159f;
    private static final int COUNT = 2;
    private static final String STATUS= "В работе";

    @Test
    public void insertAndReadList() throws IOException, InterruptedException {
        db.ordersDao().insert(getOrder(1));
        db.ordersDao().insert(getOrder(2));

        List<Order> inserted = getValue(db.ordersDao().getOrders());
        assertThat(inserted, notNullValue());
        assertThat(inserted.isEmpty(), is(false));
        assertThat(inserted.size(), is(2));
        assertThat(inserted.get(0).price, is(PRICE));
        assertThat(inserted.get(0).count, is(COUNT));
        assertThat(inserted.get(0).id, is(1L));
    }

    @Test
    public void insertAndReadSingle() throws IOException, InterruptedException {
        db.ordersDao().insert(getOrder(1));

        Order inserted = getValue(db.ordersDao().getOrder(1));
        assertThat(inserted, notNullValue());
        assertThat(inserted.price, is(PRICE));
        assertThat(inserted.count, is(COUNT));
        assertThat(inserted.id, is(1L));
    }

    private Order getOrder(long id){
        Order item = new Order();
        item.id = id;
        item.price = PRICE;
        item.status = STATUS;
        item.count = COUNT;
        return item;
    }
}
