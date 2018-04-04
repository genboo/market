package ru.devsp.apps.market.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.objects.SearchGood;
import ru.devsp.apps.market.repository.SearchRepository;
import ru.devsp.apps.market.repository.bounds.SearchBound;
import ru.devsp.apps.market.tools.AbsentLiveData;

/**
 * Управление поиском
 * Created by gen on 31.08.2017.
 */

public class SearchViewModel extends ViewModel {

    private final LiveData<Resource<List<SearchGood>>> mGoods;
    private final MutableLiveData<String> mSwitcher = new MutableLiveData<>();


    @Inject
    SearchViewModel(SearchRepository repo) {
        mGoods = Transformations.switchMap(mSwitcher, query -> {
            if (query == null) {
                return AbsentLiveData.create();
            } else {
                return repo.search(getGoodsParams(query));
            }
        });
    }

    public LiveData<Resource<List<SearchGood>>> getGoods() {
        return mGoods;
    }


    public void setQuery(String query) {
        mSwitcher.postValue(query);
    }

    private RequestParams getGoodsParams(String query) {
        RequestParams params = RequestParams.getParams();
        params
                .setParam(RequestParams.PARAM_QUERY, query)
                .setMethod(SearchBound.METHOD);

        return params;
    }

}
