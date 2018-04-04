package ru.devsp.apps.market.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Товар
 * Created by gen on 02.10.2017.
 */

@Entity
public class Good {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;

    public String image;

    @ColumnInfo(name = "description")
    public String desc;

    public float price;

    @ColumnInfo(name = "cache_key")
    public String cacheKey;

    @Ignore
    public String[] images;

    public Good(long id) {
        this.id = id;
    }

    @Ignore
    public Good(long id, String title) {
        this.id = id;
        this.title = title;
    }

}
