package ru.devsp.apps.market.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


/**
 * Изображение товара
 * Created by gen on 02.11.2017.
 */

@Entity
public class Image {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String image;

    @ColumnInfo(name = "cache_key")
    public String cacheKey;

    public Image(String image){
        this.image = image;
    }

}
