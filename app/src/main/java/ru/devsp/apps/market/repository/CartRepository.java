package ru.devsp.apps.market.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.apps.market.model.db.CartDao;
import ru.devsp.apps.market.model.objects.Cart;
import ru.devsp.apps.market.model.objects.CartTotal;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.tools.AppExecutors;

/**
 * Репозиторий данных по товарам
 * Created by gen on 22.12.2017.
 */
@Singleton
public class CartRepository {

    private final AppExecutors mAppExecutors;
    private CartDao mCartDao;

    @Inject
    public CartRepository(AppExecutors appExecutors, CartDao cartDao) {
        mAppExecutors = appExecutors;
        mCartDao = cartDao;
    }

    public LiveData<List<Cart>> getGoods() {
        return mCartDao.getGoods();
    }

    public LiveData<CartTotal> getTotal() {
        return mCartDao.getTotal();
    }

    public LiveData<Long> addToCart(Good good, int count) {
        MutableLiveData<Long> result = new MutableLiveData<>();
        mAppExecutors.diskIO().execute(() -> {
            Cart cart = new Cart();
            cart.id = good.id;
            cart.good = good;
            cart.count = count;
            long id = mCartDao.insert(cart);
            mAppExecutors.mainThread().execute(() -> result.postValue(id));
        });
        return result;
    }

    public LiveData<Long> addToCart(Cart cart) {
        MutableLiveData<Long> result = new MutableLiveData<>();
        mAppExecutors.diskIO().execute(() -> {
            long id = mCartDao.insert(cart);
            mAppExecutors.mainThread().execute(() -> result.postValue(id));
        });
        return result;
    }

    public void delete(Cart cart) {
        mAppExecutors.diskIO().execute(() -> mCartDao.delete(cart));
    }

    public void delete(long id) {
        mAppExecutors.diskIO().execute(() -> {
            Cart cart = new Cart();
            cart.id = id;
            mCartDao.delete(cart);
        });
    }

    public void update(Cart cart) {
        mAppExecutors.diskIO().execute(() -> mCartDao.update(cart));
    }

    public void clear() {
        mAppExecutors.diskIO().execute(() -> mCartDao.clear());
    }
}
