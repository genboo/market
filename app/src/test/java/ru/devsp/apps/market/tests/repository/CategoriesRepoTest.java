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
import ru.devsp.apps.market.model.db.CategoriesDao;
import ru.devsp.apps.market.model.objects.Cache;
import ru.devsp.apps.market.model.objects.Category;
import ru.devsp.apps.market.repository.CategoriesRepository;
import ru.devsp.apps.market.repository.bounds.CategoriesBound;
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
public class CategoriesRepoTest {

    private CategoriesRepository repo;
    private MarketApi api;
    private CategoriesDao categoriesDao;
    private CacheDao cacheDao;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        api = mock(MarketApi.class);
        categoriesDao = mock(CategoriesDao.class);
        cacheDao = mock(CacheDao.class);
        repo = new CategoriesRepository(new InstantAppExecutors(), api,
                categoriesDao, cacheDao);
    }

    @Test
    public void getCategoriesNetwork() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(null);
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<Category>> dbData = new MutableLiveData<>();
        when(categoriesDao.getCategories(anyString())).thenReturn(dbData);

        //Имитация успешного овета
        LiveData<ApiResponse<List<Category>>> call = ApiTools.successCall(DataTools.createCategories());

        RequestParams params = ApiTools.getParams(CategoriesBound.METHOD);
        when(api.getCategories(params)).thenReturn(call);

        //Отслеживание изменений
        Observer<Resource<List<Category>>> observer = mock(Observer.class);
        repo.getCategories(params).observeForever(observer);
        verify(api, never()).getCategories(params);

        MutableLiveData<List<Category>> updatedDbData = new MutableLiveData<>();
        when(categoriesDao.getCategories(anyString())).thenReturn(updatedDbData);
        dbData.setValue(null);

        //Запрос к API был
        verify(api).getCategories(params);
    }

    @Test
    public void getCategoriesExpiredCache() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(DataTools.createCache("GetCategories", true));
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<Category>> dbData = new MutableLiveData<>();
        dbData.setValue(DataTools.createCategories());
        when(categoriesDao.getCategories(anyString())).thenReturn(dbData);

        //Имитация успешного овета
        LiveData<ApiResponse<List<Category>>> call = ApiTools.successCall(DataTools.createCategories());
        RequestParams params = ApiTools.getParams(CategoriesBound.METHOD);
        when(api.getCategories(params)).thenReturn(call);

        //Отслеживание изменений
        Observer<Resource<List<Category>>> observer = mock(Observer.class);
        repo.getCategories(params).observeForever(observer);

        MutableLiveData<List<Category>> updatedDbData = new MutableLiveData<>();
        when(categoriesDao.getCategories(anyString())).thenReturn(updatedDbData);
        dbData.setValue(null);

        //Запрос к API был
        verify(api).getCategories(params);
    }

    @Test
    public void getCategoriesCached() {
        //Имитация запроса в бд для поиска кэша
        MutableLiveData<Cache> dbCache = new MutableLiveData<>();
        dbCache.setValue(DataTools.createCache("GetCategories", false));
        when(cacheDao.getCache(anyString())).thenReturn(dbCache);

        //Имитация запроса в бд для поиска данных
        MutableLiveData<List<Category>> dbData = new MutableLiveData<>();
        dbData.setValue(DataTools.createCategories());
        when(categoriesDao.getCategories(anyString())).thenReturn(dbData);

        //Имитация вызова метода
        RequestParams params = ApiTools.getParams(CategoriesBound.METHOD);
        Observer<Resource<List<Category>>> observer = mock(Observer.class);
        repo.getCategories(params).observeForever(observer);

        //Запроса к API не было
        verify(api, never()).getCategories(params);
    }

}
