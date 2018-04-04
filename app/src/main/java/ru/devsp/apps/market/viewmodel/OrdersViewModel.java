package ru.devsp.apps.market.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.apps.market.model.objects.Cart;
import ru.devsp.apps.market.model.objects.CartTotal;
import ru.devsp.apps.market.model.objects.Order;
import ru.devsp.apps.market.repository.OrdersRepository;

/**
 * Управление товарами
 * Created by gen on 31.08.2017.
 */

public class OrdersViewModel extends ViewModel {

    private final LiveData<List<Order>> mOrders;
    private OrdersRepository mRepo;

    @Inject
    OrdersViewModel(OrdersRepository repo) {
        mRepo = repo;
        mOrders = repo.getOrders();
    }

    public LiveData<List<Order>> getOrders() {
        return mOrders;
    }

}
