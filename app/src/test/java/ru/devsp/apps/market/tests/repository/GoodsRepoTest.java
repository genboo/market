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
import ru.devsp.apps.market.model.db.GoodsDao;
import ru.devsp.apps.market.model.db.ImagesDao;
import ru.devsp.apps.market.model.objects.Cache;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.repository.GoodsRepository;
import ru.devsp.apps.market.repository.bounds.GoodBound;
import ru.devsp.apps.market.repository.bounds.GoodsBound;
import ru.devsp.apps.market.tools.ApiTools;
import ru.devsp.apps.market.tools.DataTools;
import ru.devsp.apps.market.tools.InstantAppExecutors;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирвоание GoodsRepository
 * Created by gen on 15.12.2017.
 */

@RunWith(JUnit4.class)
public class GoodsRepoTest {

    private GoodsRepository repo;
    private MarketApi api;
    private GoodsDao goodsDao;
    private ImagesDao imagesDao;
    private CacheDao cacheDao;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        api = mock(MarketApi.class);
        goodsDao = mock(GoodsDao.class);
        imagesDao = mock(ImagesDao.class);
        cacheDao = mock(CacheDao.class);
        repo = new GoodsRepository(new InstantAppExecutors(), api,
                goodsDao, imagesDao, cacheDao);
    }

    @Test
    public void getGoodsNetwork() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(null);
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<Good>> dbData = new MutableLiveData<>();
        when(goodsDao.getGoods(anyString())).thenReturn(dbData);

        //Имитация успешного овета
        LiveData<ApiResponse<List<Good>>> call = ApiTools.successCall(DataTools.createGoods());

        RequestParams params = ApiTools.getParams(GoodsBound.METHOD);
        params.setParam(RequestParams.PARAM_CATEGORY, 1);
        when(api.getGoods(params)).thenReturn(call);

        //Отслеживание изменений
        Observer<Resource<List<Good>>> observer = mock(Observer.class);
        repo.getGoods(params).observeForever(observer);
        verify(api, never()).getGoods(params);

        MutableLiveData<List<Good>> updatedDbData = new MutableLiveData<>();
        when(goodsDao.getGoods(anyString())).thenReturn(updatedDbData);
        dbData.setValue(null);

        //Запрос к API был
        verify(api).getGoods(params);
    }

    @Test
    public void getGoodsExpiredCache() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(DataTools.createCache("GetGoods", true));
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<Good>> dbData = new MutableLiveData<>();
        dbData.setValue(DataTools.createGoods());
        when(goodsDao.getGoods(anyString())).thenReturn(dbData);

        //Имитация успешного овета
        LiveData<ApiResponse<List<Good>>> call = ApiTools.successCall(DataTools.createGoods());
        RequestParams params = ApiTools.getParams(GoodsBound.METHOD);
        params.setParam(RequestParams.PARAM_CATEGORY, 1);
        when(api.getGoods(params)).thenReturn(call);

        //Отслеживание изменений
        Observer<Resource<List<Good>>> observer = mock(Observer.class);
        repo.getGoods(params).observeForever(observer);

        MutableLiveData<List<Good>> updatedDbData = new MutableLiveData<>();
        when(goodsDao.getGoods(anyString())).thenReturn(updatedDbData);
        dbData.setValue(null);

        //Запрос к API был
        verify(api).getGoods(params);
    }

    @Test
    public void getGoodsCached() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(DataTools.createCache("GetGoods", false));
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<Good>> dbData = new MutableLiveData<>();
        dbData.setValue(DataTools.createGoods());
        when(goodsDao.getGoods(anyString())).thenReturn(dbData);

        //Имитация вызова метода
        RequestParams params = ApiTools.getParams(GoodsBound.METHOD);
        params.setParam(RequestParams.PARAM_CATEGORY, 1);
        Observer<Resource<List<Good>>> observer = mock(Observer.class);
        repo.getGoods(params).observeForever(observer);

        //Запроса к API не было
        verify(api, never()).getGoods(params);
    }


    @Test
    public void getGoodNetwork() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(null);
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<Good> dbData = new MutableLiveData<>();
        when(goodsDao.getGood(anyLong())).thenReturn(dbData);

        //Имитация успешного овета
        LiveData<ApiResponse<Good>> call = ApiTools.successCall(DataTools.createGood());

        RequestParams params = ApiTools.getParams(GoodBound.METHOD);
        params.setParam(RequestParams.PARAM_GOOD, 1);
        when(api.getGood(params)).thenReturn(call);

        //Отслеживание изменений
        Observer<Resource<Good>> observer = mock(Observer.class);
        repo.getGood(params).observeForever(observer);
        verify(api, never()).getGood(params);

        MutableLiveData<Good> updatedDbData = new MutableLiveData<>();
        when(goodsDao.getGood(anyLong())).thenReturn(updatedDbData);
        dbData.setValue(null);

        //Запрос к API был
        verify(api).getGood(params);
    }

    @Test
    public void getGoodExpiredCache() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(DataTools.createCache("GetGood", true));
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<Good> dbData = new MutableLiveData<>();
        dbData.setValue(DataTools.createGood());
        when(goodsDao.getGood(anyLong())).thenReturn(dbData);

        //Имитация успешного овета
        LiveData<ApiResponse<Good>> call = ApiTools.successCall(DataTools.createGood());
        RequestParams params = ApiTools.getParams(GoodBound.METHOD);
        params.setParam(RequestParams.PARAM_GOOD, 1);
        when(api.getGood(params)).thenReturn(call);

        //Отслеживание изменений
        Observer<Resource<Good>> observer = mock(Observer.class);
        repo.getGood(params).observeForever(observer);

        MutableLiveData<Good> updatedDbData = new MutableLiveData<>();
        when(goodsDao.getGood(anyLong())).thenReturn(updatedDbData);
        dbData.setValue(null);

        //Запрос к API был
        verify(api).getGood(params);
    }

    @Test
    public void getGoodCached() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(DataTools.createCache("GetGood", false));
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<Good> dbData = new MutableLiveData<>();
        dbData.setValue(DataTools.createGood());
        when(goodsDao.getGood(anyLong())).thenReturn(dbData);

        //Имитация вызова метода
        RequestParams params = ApiTools.getParams(GoodBound.METHOD);
        params.setParam(RequestParams.PARAM_GOOD, 1);
        Observer<Resource<Good>> observer = mock(Observer.class);
        repo.getGood(params).observeForever(observer);

        //Запроса к API не было
        verify(api, never()).getGood(params);
    }

    @Test
    public void getImages() {
        //Имитация запроса в бд для поиска данных
        MutableLiveData<String[]> dbData = new MutableLiveData<>();
        dbData.setValue(DataTools.createImages());
        when(imagesDao.getImages(anyString())).thenReturn(dbData);

        //Имитация вызова метода
        Observer<String[]> observer = mock(Observer.class);
        repo.getImages(anyLong()).observeForever(observer);

        verify(imagesDao).getImages(anyString());
    }

}
