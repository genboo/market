package ru.devsp.apps.market.model.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Заказ
 * Created by gen on 02.10.2017.
 */

@Entity
public class Order {

    @PrimaryKey
    public long id;

    public float price;

    public String status;

    public String time;
    
    public int count;

}
