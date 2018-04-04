package ru.devsp.apps.market.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Время хранения разных типов объектов
 * Created by gen on 14.09.2017.
 */

@Entity (indices = {@Index("cache_type")})
public class CacheTime {

    @PrimaryKey
    @ColumnInfo(name = "cache_type")
    @NonNull
    public final String cacheType;

    public final int time;

    public CacheTime(@NonNull String cacheType, int time){
        this.cacheType = cacheType;
        this.time = time;
    }

}
