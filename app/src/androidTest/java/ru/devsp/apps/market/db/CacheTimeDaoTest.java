package ru.devsp.apps.market.db;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;
import ru.devsp.apps.market.model.objects.CacheTime;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование базы: время жизни кэша
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class CacheTimeDaoTest extends DbTest {

    private static final String CACHE_TYPE = "Good::GetList";

    @Test
    public void insertAndRead() throws IOException, InterruptedException {
        db.cacheDao().insert(getCache());

        CacheTime inserted = db.cacheDao().getCacheType(CACHE_TYPE);
        assertThat(inserted, notNullValue());
        assertThat(inserted.time, is(30));
        assertThat(inserted.cacheType, is(CACHE_TYPE));
    }

    private CacheTime getCache() {
        return new CacheTime(CACHE_TYPE, 30);
    }
}
