package ru.devsp.apps.market.db;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;

import ru.devsp.apps.market.model.objects.Cache;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.devsp.apps.market.tools.LiveDataTestUtil.getValue;

/**
 * Тестирование базы: кэш
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class CacheDaoTest extends DbTest {

    private static final String CACHE_KEY = "good::1";

    @Test
    public void insertAndRead() throws IOException, InterruptedException {
        db.cacheDao().insert(getCache());

        Cache inserted = getValue(db.cacheDao().getCache(CACHE_KEY));
        assertThat(inserted, notNullValue());
        assertThat(inserted.expire > new Date().getTime(), is(true));
        assertThat(inserted.cacheKey, is(CACHE_KEY));
    }

    private Cache getCache() {
        return new Cache(CACHE_KEY, new Date().getTime() + 30);
    }
}
