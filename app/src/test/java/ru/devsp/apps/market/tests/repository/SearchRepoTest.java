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
import ru.devsp.apps.market.model.db.SearchDao;
import ru.devsp.apps.market.model.objects.Cache;
import ru.devsp.apps.market.model.objects.SearchGood;
import ru.devsp.apps.market.repository.SearchRepository;
import ru.devsp.apps.market.repository.bounds.GoodsBound;
import ru.devsp.apps.market.tools.ApiTools;
import ru.devsp.apps.market.tools.DataTools;
import ru.devsp.apps.market.tools.InstantAppExecutors;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирвание SearchRepository
 * Created by gen on 15.12.2017.
 */

@RunWith(JUnit4.class)
public class SearchRepoTest {

    private SearchRepository repo;
    private MarketApi api;
    private SearchDao searchDao;
    private CacheDao cacheDao;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        api = mock(MarketApi.class);
        searchDao = mock(SearchDao.class);
        cacheDao = mock(CacheDao.class);
        repo = new SearchRepository(new InstantAppExecutors(), api,
                searchDao, cacheDao);
    }

    @Test
    public void getGoodsNetwork() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(null);
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<SearchGood>> dbData = new MutableLiveData<>();
        when(searchDao.getSearchGoods(anyString())).thenReturn(dbData);

        //Имитация успешного овета
        LiveData<ApiResponse<List<SearchGood>>> call = ApiTools.successCall(DataTools.createSearchGoods());

        RequestParams params = ApiTools.getParams(GoodsBound.METHOD);
        params.setParam(RequestParams.PARAM_CATEGORY, 1);
        when(api.search(params)).thenReturn(call);

        //Отслеживание изменений
        Observer<Resource<List<SearchGood>>> observer = mock(Observer.class);
        repo.search(params).observeForever(observer);
        verify(api, never()).getGoods(params);

        MutableLiveData<List<SearchGood>> updatedDbData = new MutableLiveData<>();
        when(searchDao.getSearchGoods(anyString())).thenReturn(updatedDbData);
        dbData.setValue(null);

        //Запрос к API был
        verify(api).search(params);
    }

    @Test
    public void getGoodsExpiredCache() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(DataTools.createCache("Search", true));
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<SearchGood>> dbData = new MutableLiveData<>();
        dbData.setValue(DataTools.createSearchGoods());
        when(searchDao.getSearchGoods(anyString())).thenReturn(dbData);

        //Имитация успешного овета
        LiveData<ApiResponse<List<SearchGood>>> call = ApiTools.successCall(DataTools.createSearchGoods());
        RequestParams params = ApiTools.getParams(GoodsBound.METHOD);
        params.setParam(RequestParams.PARAM_CATEGORY, 1);
        when(api.search(params)).thenReturn(call);

        //Отслеживание изменений
        Observer<Resource<List<SearchGood>>> observer = mock(Observer.class);
        repo.search(params).observeForever(observer);

        MutableLiveData<List<SearchGood>> updatedDbData = new MutableLiveData<>();
        when(searchDao.getSearchGoods(anyString())).thenReturn(updatedDbData);
        dbData.setValue(null);

        //Запрос к API был
        verify(api).search(params);
    }

    @Test
    public void getGoodsCached() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(DataTools.createCache("Search", false));
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<SearchGood>> dbData = new MutableLiveData<>();
        dbData.setValue(DataTools.createSearchGoods());
        when(searchDao.getSearchGoods(anyString())).thenReturn(dbData);

        //Имитация вызова метода
        RequestParams params = ApiTools.getParams(GoodsBound.METHOD);
        params.setParam(RequestParams.PARAM_QUERY, "light");
        Observer<Resource<List<SearchGood>>> observer = mock(Observer.class);
        repo.search(params).observeForever(observer);

        //Запроса к API не было
        verify(api, never()).search(params);
    }

}
