package ru.devsp.apps.market.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Информация о кэше
 * Created by gen on 14.09.2017.
 */

@Entity(indices = {@Index("cache_key")})
public class Cache {

    @PrimaryKey
    @ColumnInfo(name = "cache_key")
    @NonNull
    public final String cacheKey;

    public final long expire;

    public Cache(String cacheKey, long expire) {
        this.cacheKey = cacheKey;
        this.expire = expire;
    }

}
