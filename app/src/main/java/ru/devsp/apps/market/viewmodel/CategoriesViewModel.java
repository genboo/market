package ru.devsp.apps.market.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import java.util.List;
import javax.inject.Inject;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.objects.Category;
import ru.devsp.apps.market.repository.CategoriesRepository;
import ru.devsp.apps.market.repository.bounds.CategoriesBound;
import ru.devsp.apps.market.tools.AbsentLiveData;

/**
 * Управление категориями
 * Created by gen on 31.08.2017.
 */

public class CategoriesViewModel extends ViewModel {

    private final LiveData<Resource<List<Category>>> mCategories;
    private final MutableLiveData<Long> mSwitcher = new MutableLiveData<>();

    @Inject
    CategoriesViewModel(CategoriesRepository repo) {
        mCategories = Transformations.switchMap(mSwitcher, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return repo.getCategories(getCategoriesParams(id));
            }
        });
    }

    public LiveData<Resource<List<Category>>> getCategories() {
        return mCategories;
    }

    public void setCategory(Long id) {
        mSwitcher.postValue(id);
    }

    private RequestParams getCategoriesParams(long id) {
        RequestParams params = RequestParams.getParams();
        params.setMethod(CategoriesBound.METHOD);
        if (id > 0) {
            params.setParam(RequestParams.PARAM_CATEGORY, id);
        }
        return params;
    }

}
