package ru.devsp.apps.market.model.objects;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Корзина
 * Created by gen on 02.01.2018.
 */
@Entity
public class Cart {

    @PrimaryKey
    public long id;

    @Embedded(prefix = "good_")
    public Good good;

    public int count;

}
