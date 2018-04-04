package ru.devsp.apps.market.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.objects.Cart;
import ru.devsp.apps.market.model.objects.CartTotal;
import ru.devsp.apps.market.model.objects.Order;
import ru.devsp.apps.market.model.objects.OrderItem;
import ru.devsp.apps.market.repository.CartRepository;
import ru.devsp.apps.market.repository.OrdersRepository;
import ru.devsp.apps.market.repository.bounds.OrderBound;
import ru.devsp.apps.market.tools.AbsentLiveData;

/**
 * Управление товарами
 * Created by gen on 31.08.2017.
 */

public class CartViewModel extends ViewModel {

    private final LiveData<List<Cart>> mGoods;
    private final LiveData<CartTotal> mTotal;
    private CartRepository mRepo;
    private OrdersRepository mOrdersRepo;
    private LiveData<Resource<Order>> mOrder;
    private MutableLiveData<List<Cart>> mSwitcher = new MutableLiveData<>();

    @Inject
    CartViewModel(CartRepository repo, OrdersRepository ordersRepo) {
        mRepo = repo;
        mOrdersRepo = ordersRepo;
        mGoods = repo.getGoods();
        mTotal = repo.getTotal();

        mOrder = Transformations.switchMap(mSwitcher, items -> {
            if (items == null) {
                return AbsentLiveData.create();
            } else {
                return mOrdersRepo.checkout(getCheckoutParams(items));
            }
        });
    }

    public LiveData<List<Cart>> getGoods() {
        return mGoods;
    }

    public LiveData<Resource<Order>> getOrder() {
        return mOrder;
    }

    public LiveData<CartTotal> getTotal() {
        return mTotal;
    }

    public void checkout(List<Cart> items) {
        mSwitcher.postValue(items);
    }

    public void update(Cart cart) {
        mRepo.update(cart);
    }

    public void delete(Cart cart) {
        mRepo.delete(cart);
    }

    public void clear() {
        mRepo.clear();
    }

    public LiveData<Long> addToCart(Cart cart) {
        return mRepo.addToCart(cart);
    }

    private RequestParams getCheckoutParams(List<Cart> items) {
        RequestParams params = RequestParams.getParams();
        params.setMethod(OrderBound.METHOD);
        OrderItem[] orderItems = new OrderItem[items.size()];
        for (int i = 0; i < items.size(); i++) {
            orderItems[i] = new OrderItem(items.get(i).good.id, items.get(i).count);
        }
        params.setParam(RequestParams.PARAM_GOODS, orderItems);
        return params;
    }

}
