package ru.devsp.apps.market.db;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import ru.devsp.apps.market.model.objects.Good;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.devsp.apps.market.tools.LiveDataTestUtil.getValue;

/**
 * Тестирование базы: товары
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class GoodsDaoTest extends DbTest {

    private static final String CACHE_KEY = "goods::1";
    private static final String TITLE= "Духи";
    private static final String DESC= "Описание товара";
    private static final float PRICE = 159f;
    private static final String IMAGE= "http://test.org/image.png";

    @Test
    public void insertAndReadList() throws IOException, InterruptedException {
        db.goodsDao().insert(getGood(1));
        db.goodsDao().insert(getGood(2));

        List<Good> inserted = getValue(db.goodsDao().getGoods(CACHE_KEY));
        assertThat(inserted, notNullValue());
        assertThat(inserted.isEmpty(), is(false));
        assertThat(inserted.size(), is(2));
        assertThat(inserted.get(0).title, is(TITLE));
        assertThat(inserted.get(0).image, is(IMAGE));
        assertThat(inserted.get(0).price, is(PRICE));
        assertThat(inserted.get(0).desc, is(DESC));
    }

    @Test
    public void insertAndReadSingle() throws IOException, InterruptedException {
        db.goodsDao().insert(getGood(1));

        Good inserted = getValue(db.goodsDao().getGood(1));
        assertThat(inserted, notNullValue());
        assertThat(inserted.title, is(TITLE));
        assertThat(inserted.image, is(IMAGE));
        assertThat(inserted.price, is(PRICE));
        assertThat(inserted.desc, is(DESC));
    }

    @Test
    public void deleteSingle() throws IOException, InterruptedException {
        Good good = getGood(1);
        db.goodsDao().insert(good);
        db.goodsDao().delete(good);

        Good inserted = getValue(db.goodsDao().getGood(1));
        assertThat(inserted, nullValue());
    }

    @Test
    public void clearOld() throws IOException, InterruptedException {
        db.goodsDao().insert(getGood(1));
        db.goodsDao().clearOld(CACHE_KEY);
        List<Good> inserted = getValue(db.goodsDao().getGoods(CACHE_KEY));
        assertThat(inserted.isEmpty(), is(true));
    }

    private Good getGood(long id){
        Good item = new Good(id, TITLE);
        item.cacheKey = CACHE_KEY;
        item.image = IMAGE;
        item.price = PRICE;
        item.desc = DESC;
        return item;
    }
}
