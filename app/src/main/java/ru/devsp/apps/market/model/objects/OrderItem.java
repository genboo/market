package ru.devsp.apps.market.model.objects;

/**
 * Параметры заказа
 * Created by gen on 29.01.2018.
 */

public class OrderItem {

    public long id;
    public int count;

    public OrderItem(long id, int count){
        this.id = id;
        this.count = count;
    }
}
