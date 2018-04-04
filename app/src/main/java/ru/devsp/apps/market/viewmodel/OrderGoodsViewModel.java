package ru.devsp.apps.market.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.objects.OrderGood;
import ru.devsp.apps.market.repository.CategoriesRepository;
import ru.devsp.apps.market.repository.OrderGoodsRepository;
import ru.devsp.apps.market.repository.bounds.CategoriesBound;
import ru.devsp.apps.market.repository.bounds.OrderGoodsBound;
import ru.devsp.apps.market.tools.AbsentLiveData;

/**
 * Управление товарами в заказе
 * Created by gen on 31.08.2017.
 */

public class OrderGoodsViewModel extends ViewModel {

    private final LiveData<Resource<List<OrderGood>>> mOrderGoods;
    private final MutableLiveData<Long> mSwitcher = new MutableLiveData<>();

    @Inject
    OrderGoodsViewModel(OrderGoodsRepository repo) {
        mOrderGoods = Transformations.switchMap(mSwitcher, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return repo.getOrderGoods(getOrderGoodsParams(id));
            }
        });
    }

    public LiveData<Resource<List<OrderGood>>> getOrderGoods() {
        return mOrderGoods;
    }

    public void setOrder(Long id) {
        mSwitcher.postValue(id);
    }

    private RequestParams getOrderGoodsParams(long id) {
        RequestParams params = RequestParams.getParams();
        params.setMethod(OrderGoodsBound.METHOD);
        params.setParam(RequestParams.PARAM_ORDER, id);
        return params;
    }

}
