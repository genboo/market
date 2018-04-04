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
import ru.devsp.apps.market.model.db.CacheDao;
import ru.devsp.apps.market.model.db.OrderGoodsDao;
import ru.devsp.apps.market.model.objects.Cache;
import ru.devsp.apps.market.model.objects.OrderGood;
import ru.devsp.apps.market.repository.OrderGoodsRepository;
import ru.devsp.apps.market.repository.bounds.OrderGoodsBound;
import ru.devsp.apps.market.tools.ApiTools;
import ru.devsp.apps.market.tools.DataTools;
import ru.devsp.apps.market.tools.InstantAppExecutors;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирвоание CategoriesRepository
 * Created by gen on 15.12.2017.
 */

@RunWith(JUnit4.class)
public class OrderGoodsRepoTest {

    private OrderGoodsRepository repo;
    private MarketApi api;
    private OrderGoodsDao orderGoodsDao;
    private CacheDao cacheDao;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        api = mock(MarketApi.class);
        orderGoodsDao = mock(OrderGoodsDao.class);
        cacheDao = mock(CacheDao.class);
        repo = new OrderGoodsRepository(new InstantAppExecutors(), api,
                orderGoodsDao, cacheDao);
    }

    @Test
    public void getOrderGoodsNetwork() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(null);
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<OrderGood>> dbData = new MutableLiveData<>();
        when(orderGoodsDao.getOrderGoods(anyString())).thenReturn(dbData);

        //Имитация успешного овета
        LiveData<ApiResponse<List<OrderGood>>> call = ApiTools.successCall(DataTools.createOrderGoods());

        RequestParams params = ApiTools.getParams(OrderGoodsBound.METHOD);
        when(api.getOrderItems(params)).thenReturn(call);

        //Отслеживание изменений
        Observer<Resource<List<OrderGood>>> observer = mock(Observer.class);
        repo.getOrderGoods(params).observeForever(observer);
        verify(api, never()).getOrderItems(params);

        MutableLiveData<List<OrderGood>> updatedDbData = new MutableLiveData<>();
        when(orderGoodsDao.getOrderGoods(anyString())).thenReturn(updatedDbData);
        dbData.setValue(null);

        //Запрос к API был
        verify(api).getOrderItems(params);
    }

    @Test
    public void getOrderGoodsExpiredCache() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(DataTools.createCache("GetOrderGoods", true));
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<OrderGood>> dbData = new MutableLiveData<>();
        dbData.setValue(DataTools.createOrderGoods());
        when(orderGoodsDao.getOrderGoods(anyString())).thenReturn(dbData);

        //Имитация успешного овета
        LiveData<ApiResponse<List<OrderGood>>> call = ApiTools.successCall(DataTools.createOrderGoods());
        RequestParams params = ApiTools.getParams(OrderGoodsBound.METHOD);
        when(api.getOrderItems(params)).thenReturn(call);

        //Отслеживание изменений
        Observer<Resource<List<OrderGood>>> observer = mock(Observer.class);
        repo.getOrderGoods(params).observeForever(observer);

        MutableLiveData<List<OrderGood>> updatedDbData = new MutableLiveData<>();
        when(orderGoodsDao.getOrderGoods(anyString())).thenReturn(updatedDbData);
        dbData.setValue(null);

        //Запрос к API был
        verify(api).getOrderItems(params);
    }

    @Test
    public void getOrderGoodsCached() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(DataTools.createCache("GetOrderGoods", false));
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<OrderGood>> dbData = new MutableLiveData<>();
        dbData.setValue(DataTools.createOrderGoods());
        when(orderGoodsDao.getOrderGoods(anyString())).thenReturn(dbData);

        //Имитация вызова метода
        RequestParams params = ApiTools.getParams(OrderGoodsBound.METHOD);
        Observer<Resource<List<OrderGood>>> observer = mock(Observer.class);
        repo.getOrderGoods(params).observeForever(observer);

        //Запроса к API не было
        verify(api, never()).getOrderItems(params);
    }

}
