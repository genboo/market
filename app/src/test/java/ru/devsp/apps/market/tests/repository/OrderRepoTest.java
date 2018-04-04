package ru.devsp.apps.market.tests.repository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import ru.devsp.apps.market.model.api.ApiResponse;
import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.db.OrdersDao;
import ru.devsp.apps.market.model.objects.Order;
import ru.devsp.apps.market.model.objects.OrderItem;
import ru.devsp.apps.market.repository.OrdersRepository;
import ru.devsp.apps.market.repository.bounds.OrderBound;
import ru.devsp.apps.market.tools.ApiTools;
import ru.devsp.apps.market.tools.DataTools;
import ru.devsp.apps.market.tools.InstantAppExecutors;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирвоание CartRepository
 * Created by gen on 15.12.2017.
 */

@RunWith(JUnit4.class)
public class OrderRepoTest {

    private OrdersRepository repo;
    private OrdersDao ordersDao;
    private MarketApi api;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        ordersDao = mock(OrdersDao.class);
        api = mock(MarketApi.class);
        repo = new OrdersRepository(new InstantAppExecutors(), api, ordersDao);
    }

    @Test
    public void getOrders() {
        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<Order>> dbData = new MutableLiveData<>();
        when(ordersDao.getOrders()).thenReturn(dbData);

        //Отслеживание изменений
        Observer<List<Order>> observer = mock(Observer.class);
        repo.getOrders().observeForever(observer);

        MutableLiveData<List<Order>> updatedDbData = new MutableLiveData<>();
        when(ordersDao.getOrders()).thenReturn(updatedDbData);
        dbData.setValue(null);

        verify(ordersDao).getOrders();
    }

    @Test
    public void checkout() {
        //Имитация запроса в бд для поиска данных
        MutableLiveData<Order> dbData = new MutableLiveData<>();
        when(ordersDao.getOrder(anyLong())).thenReturn(dbData);

        //Имитация успешного овета
        LiveData<ApiResponse<Order>> call = ApiTools.successCall(DataTools.createOrder());

        RequestParams params = ApiTools.getParams(OrderBound.METHOD);
        OrderItem orderItem = new OrderItem(1, 2);
        params.setParam(RequestParams.PARAM_GOODS, new OrderItem[]{orderItem});
        when(api.checkout(params)).thenReturn(call);

        //Отслеживание изменений
        Observer<Resource<Order>> observer = mock(Observer.class);
        repo.checkout(params).observeForever(observer);
        verify(api, never()).getGoods(params);

        MutableLiveData<Order> updatedDbData = new MutableLiveData<>();
        when(ordersDao.getOrder(anyLong())).thenReturn(updatedDbData);
        dbData.setValue(null);

        //Запрос к API был
        verify(api).checkout(params);
    }


}
