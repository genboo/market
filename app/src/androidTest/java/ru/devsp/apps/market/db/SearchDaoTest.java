package ru.devsp.apps.market.db;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import ru.devsp.apps.market.model.objects.SearchGood;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.devsp.apps.market.tools.LiveDataTestUtil.getValue;

/**
 * Тестирование базы: поиск
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class SearchDaoTest extends DbTest {

    private static final String CACHE_KEY = "search::light";
    private static final String TITLE= "Духи";
    private static final float PRICE = 159f;
    private static final String IMAGE= "http://test.org/image.png";

    @Test
    public void insertAndReadList() throws IOException, InterruptedException {
        db.searchDao().insert(getSearchGood(1));
        db.searchDao().insert(getSearchGood(2));

        List<SearchGood> inserted = getValue(db.searchDao().getSearchGoods(CACHE_KEY));
        assertThat(inserted, notNullValue());
        assertThat(inserted.isEmpty(), is(false));
        assertThat(inserted.size(), is(2));
        assertThat(inserted.get(0).title, is(TITLE));
        assertThat(inserted.get(0).image, is(IMAGE));
        assertThat(inserted.get(0).price, is(PRICE));
        assertThat(inserted.get(0).id, is(1L));
    }


    @Test
    public void clearOld() throws IOException, InterruptedException {
        db.searchDao().insert(getSearchGood(1));
        db.searchDao().clearOld(CACHE_KEY);
        List<SearchGood> inserted = getValue(db.searchDao().getSearchGoods(CACHE_KEY));
        assertThat(inserted.isEmpty(), is(true));
    }

    private SearchGood getSearchGood(long id){
        SearchGood item = new SearchGood();
        item.cacheKey = CACHE_KEY;
        item.image = IMAGE;
        item.id = id;
        item.price = PRICE;
        item.title = TITLE;
        return item;
    }
}
