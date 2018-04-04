package ru.devsp.apps.market.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


/**
 * Категория
 * Created by gen on 02.10.2017.
 */

@Entity
public class Category {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;

    public String image;

    public long category;

    public int childs;

    @ColumnInfo(name = "cache_key")
    public String cacheKey;

    public Category(long id) {
        this.id = id;
    }

    @Ignore
    public Category(long id, String title) {
        this.id = id;
        this.title = title;
    }

}
