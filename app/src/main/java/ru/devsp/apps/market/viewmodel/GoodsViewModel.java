package ru.devsp.apps.market.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.repository.CartRepository;
import ru.devsp.apps.market.repository.GoodsRepository;
import ru.devsp.apps.market.repository.bounds.GoodBound;
import ru.devsp.apps.market.repository.bounds.GoodsBound;
import ru.devsp.apps.market.tools.AbsentLiveData;

/**
 * Управление товарами
 * Created by gen on 31.08.2017.
 */

public class GoodsViewModel extends ViewModel {

    private CartRepository mCartRepo;

    private final LiveData<Resource<List<Good>>> mGoods;
    private final MutableLiveData<Long> mSwitcher = new MutableLiveData<>();

    private final LiveData<Resource<Good>> mGood;
    private final LiveData<String[]> mImages;
    private final MutableLiveData<Long> mSwitcherGood = new MutableLiveData<>();

    @Inject
    GoodsViewModel(GoodsRepository repo, CartRepository cartRepo) {
        mCartRepo = cartRepo;
        mGoods = Transformations.switchMap(mSwitcher, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return repo.getGoods(getGoodsParams(id));
            }
        });

        mGood = Transformations.switchMap(mSwitcherGood, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return repo.getGood(getGoodParams(id));
            }
        });

        mImages = Transformations.switchMap(mSwitcherGood, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return repo.getImages(id);
            }
        });
    }

    public LiveData<Resource<List<Good>>> getGoods() {
        return mGoods;
    }

    public LiveData<Resource<Good>> getGood() {
        return mGood;
    }

    public LiveData<String[]> getImages() {
        return mImages;
    }

    public void setCategory(Long id) {
        mSwitcher.postValue(id);
    }

    public void setGood(Long id) {
        mSwitcherGood.postValue(id);
    }

    public LiveData<Long> addToCart(Good good, int count) {
        return mCartRepo.addToCart(good, count);
    }

    public void deleteFromCart(long id){
        mCartRepo.delete(id);
    }

    private RequestParams getGoodsParams(long id) {
        RequestParams params = RequestParams.getParams();
        params
                .setParam(RequestParams.PARAM_CATEGORY, id)
                .setMethod(GoodsBound.METHOD);

        return params;
    }

    private RequestParams getGoodParams(long id) {
        RequestParams params = RequestParams.getParams();
        params
                .setParam(RequestParams.PARAM_GOOD, id)
                .setMethod(GoodBound.METHOD);

        return params;
    }

}
