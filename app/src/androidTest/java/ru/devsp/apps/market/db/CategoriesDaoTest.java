package ru.devsp.apps.market.db;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import ru.devsp.apps.market.model.objects.Category;

import static ru.devsp.apps.market.tools.LiveDataTestUtil.getValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование базы: категории
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class CategoriesDaoTest extends DbTest {

    private static final String CACHE_KEY = "category::1";
    private static final String TITLE= "Детям";
    private static final String IMAGE= "http://test.org/image.png";

    @Test
    public void insertAndRead() throws IOException, InterruptedException {
        db.categoriesDao().insert(getCategory(1));
        db.categoriesDao().insert(getCategory(2));

        List<Category> inserted = getValue(db.categoriesDao().getCategories(CACHE_KEY));
        assertThat(inserted, notNullValue());
        assertThat(inserted.isEmpty(), is(false));
        assertThat(inserted.size(), is(2));
        assertThat(inserted.get(0).title, is(TITLE));
        assertThat(inserted.get(0).category, is(1L));
        assertThat(inserted.get(0).childs, is(2));
        assertThat(inserted.get(0).image, is(IMAGE));
    }


    @Test
    public void clearOld() throws IOException, InterruptedException {
        db.categoriesDao().insert(getCategory(1));
        db.categoriesDao().clearOld(CACHE_KEY);
        List<Category> inserted = getValue(db.categoriesDao().getCategories(CACHE_KEY));
        assertThat(inserted.isEmpty(), is(true));
    }


    private Category getCategory(long id){
        Category item = new Category(id, TITLE);
        item.cacheKey = CACHE_KEY;
        item.category = 1;
        item.childs = 2;
        item.image = IMAGE;
        return item;
    }
}
