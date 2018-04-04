package ru.devsp.apps.market.db;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import ru.devsp.apps.market.model.objects.Image;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.devsp.apps.market.tools.LiveDataTestUtil.getValue;

/**
 * Тестирование базы: изображения
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class ImagesDaoTest extends DbTest {

    private static final String CACHE_KEY = "good::1";
    private static final String IMAGE= "http://test.org/image.png";

    @Test
    public void insertAndRead() throws IOException, InterruptedException {
        db.imagesDao().insert(getImage());
        db.imagesDao().insert(getImage());

        String[] inserted = getValue(db.imagesDao().getImages(CACHE_KEY));
        assertThat(inserted, notNullValue());
        assertThat(inserted.length, is(2));
        assertThat(inserted[0], is(IMAGE));
        assertThat(inserted[1], is(IMAGE));
    }

    @Test
    public void clearOld() throws IOException, InterruptedException {
        db.imagesDao().insert(getImage());
        db.imagesDao().insert(getImage());

        db.imagesDao().clearOld(CACHE_KEY);

        String[] inserted = getValue(db.imagesDao().getImages(CACHE_KEY));
        assertThat(inserted, notNullValue());
        assertThat(inserted.length, is(0));
    }

    private Image getImage(){
        Image item = new Image(IMAGE);
        item.cacheKey = CACHE_KEY;
        return item;
    }
}
