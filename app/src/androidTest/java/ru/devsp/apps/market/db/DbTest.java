package ru.devsp.apps.market.db;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;

import ru.devsp.apps.market.model.db.MarketDatabase;

/**
 * Тестирование БД
 * Created by gen on 23.01.2018.
 */

abstract public class DbTest {

    protected MarketDatabase db;

    @Before
    public void initDb() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                MarketDatabase.class).build();
    }

    @After
    public void closeDb() {
        db.close();
    }
}
