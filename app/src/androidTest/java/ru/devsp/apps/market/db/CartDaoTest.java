package ru.devsp.apps.market.db;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import ru.devsp.apps.market.model.objects.Cart;
import ru.devsp.apps.market.model.objects.Good;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.devsp.apps.market.tools.LiveDataTestUtil.getValue;

/**
 * Тестирование базы: корзина
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class CartDaoTest extends DbTest {

    private static final String CACHE_KEY = "goods::1";
    private static final String TITLE = "Духи";
    private static final String IMAGE = "http://test.org/image.png";

    @Test
    public void insertAndReadList() throws IOException, InterruptedException {
        db.cartDao().insert(getCartGood(1, 3));
        db.cartDao().insert(getCartGood(2, 2));
        db.cartDao().insert(getCartGood(3, 14));

        List<Cart> inserted = getValue(db.cartDao().getGoods());

        assertThat(inserted, notNullValue());
        assertThat(inserted.isEmpty(), is(false));
        assertThat(inserted.size(), is(3));
        assertThat(inserted.get(0).count, is(3));
        assertThat(inserted.get(1).count, is(2));
        assertThat(inserted.get(2).count, is(14));
        assertThat(inserted.get(0).good, notNullValue());
        assertThat(inserted.get(1).good, notNullValue());
        assertThat(inserted.get(2).good, notNullValue());
        assertThat(inserted.get(2).good.title, is(TITLE));
        assertThat(inserted.get(2).good.image, is(IMAGE));
    }

    @Test
    public void delete() throws IOException, InterruptedException {
        Cart item = getCartGood(1, 1);
        db.cartDao().insert(item);
        db.cartDao().delete(item);

        Cart inserted = getValue(db.cartDao().getGood(1));
        assertThat(inserted, nullValue());
    }

    @Test
    public void update() throws IOException, InterruptedException {
        long id = 1;
        Cart item = getCartGood(id, 1);
        db.cartDao().insert(item);

        Cart inserted = getValue(db.cartDao().getGood(id));
        assertThat(inserted, notNullValue());
        assertThat(inserted.count, is(1));

        Cart itemNew = getCartGood(id, 4);
        db.cartDao().update(itemNew);

        Cart updated = getValue(db.cartDao().getGood(id));
        assertThat(updated, notNullValue());
        assertThat(updated.count, is(4));
    }

    @Test
    public void clear() throws IOException, InterruptedException {
        db.cartDao().insert(getCartGood(1, 3));
        db.cartDao().insert(getCartGood(2, 2));
        db.cartDao().insert(getCartGood(3, 14));
        db.cartDao().clear();

        List<Cart> inserted = getValue(db.cartDao().getGoods());
        assertThat(inserted.isEmpty(), is(true));
    }

    private Cart getCartGood(long id, int count) {
        Cart item = new Cart();
        item.id = id;
        item.count = count;
        item.good = getGood(id);
        return item;
    }

    private Good getGood(long id) {
        Good item = new Good(id, TITLE);
        item.cacheKey = CACHE_KEY;
        item.image = IMAGE;
        return item;
    }
}
