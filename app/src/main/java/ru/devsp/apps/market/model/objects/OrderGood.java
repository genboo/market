package ru.devsp.apps.market.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Товары заказа
 * Created by gen on 02.01.2018.
 */
@Entity
public class OrderGood {

    @PrimaryKey(autoGenerate = true)
    public long sid;

    public long id;

    public String title;

    public String image;

    public float price;

    public int count;

    @ColumnInfo(name = "cache_key")
    public String cacheKey;
}
