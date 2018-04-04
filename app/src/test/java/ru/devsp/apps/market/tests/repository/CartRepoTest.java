package ru.devsp.apps.market.tests.repository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import ru.devsp.apps.market.model.db.CartDao;
import ru.devsp.apps.market.model.objects.Cart;
import ru.devsp.apps.market.repository.CartRepository;
import ru.devsp.apps.market.tools.InstantAppExecutors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирвоание CartRepository
 * Created by gen on 15.12.2017.
 */

@RunWith(JUnit4.class)
public class CartRepoTest {

    private CartRepository repo;
    private CartDao cartDao;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        cartDao = mock(CartDao.class);
        repo = new CartRepository(new InstantAppExecutors(), cartDao);
    }

    @Test
    public void getGoods() {
        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<Cart>> dbData = new MutableLiveData<>();
        when(cartDao.getGoods()).thenReturn(dbData);

        //Отслеживание изменений
        Observer<List<Cart>> observer = mock(Observer.class);
        repo.getGoods().observeForever(observer);

        MutableLiveData<List<Cart>> updatedDbData = new MutableLiveData<>();
        when(cartDao.getGoods()).thenReturn(updatedDbData);
        dbData.setValue(null);

        verify(cartDao).getGoods();
    }

    @Test
    public void deleteById() {
        repo.delete(1);
        verify(cartDao).delete(any());
    }

    @Test
    public void deleteByObject() {
        Cart cart = new Cart();
        cart.id = 1;
        repo.delete(cart);
        verify(cartDao).delete(cart);
    }

    @Test
    public void update() {
        Cart cart = new Cart();
        cart.id = 1;
        repo.update(cart);
        verify(cartDao).update(cart);
    }

}
