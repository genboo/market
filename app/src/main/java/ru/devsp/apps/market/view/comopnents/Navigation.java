package ru.devsp.apps.market.view.comopnents;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.view.CartFragment;
import ru.devsp.apps.market.view.CategoriesFragment;
import ru.devsp.apps.market.view.GoodFragment;
import ru.devsp.apps.market.view.GoodsFragment;
import ru.devsp.apps.market.view.OrderGoodsFragment;
import ru.devsp.apps.market.view.OrdersFragment;
import ru.devsp.apps.market.view.SearchFragment;
import ru.devsp.apps.market.view.SuccessFragment;


/**
 * Навигация
 * Created by gen on 21.10.2017.
 */

public class Navigation {

    private FragmentManager mFragmentManager;

    private MutableLiveData<Boolean> mUpdateToolbar = new MutableLiveData<>();

    public Navigation(FragmentManager fm) {
        mFragmentManager = fm;
    }

    public LiveData<Boolean> getOnToolbarNeedChange() {
        return mUpdateToolbar;
    }

    public void navigateToGoods(long category) {
        navigate(GoodsFragment.getInstance(category), "goods" + category, true);
    }

    public void navigateToGood(long good) {
        navigate(GoodFragment.getInstance(good), "good" + good, true);
    }

    public void navigateToCategories() {
        navigate(CategoriesFragment.getInstance(-1), "categories", false);
    }

    public void navigateToCategory(long category) {
        navigate(CategoriesFragment.getInstance(category), "category" + category, true);
    }

    public void navigateToCart(){
        navigate(new CartFragment(), "cart", false);
    }

    public void navigateToOrders(){
        navigate(new OrdersFragment(), "orders", false);
    }

    public void navigateToOrderItems(long id){
        navigate(OrderGoodsFragment.getInstance(id), "order" + id, true);
    }

    public void navigateToSearch(String query) {
        navigate(SearchFragment.getInstance(query), "search", true);
    }


    public void navigateToSuccess() {
        navigate(new SuccessFragment(), "success", false);
    }


    /**
     * Общий метод перехода к новому фрагменту
     *
     * @param fragment
     * @param tag
     * @param back
     */
    private void navigate(Fragment fragment, String tag, boolean back) {
        if (!back) {
            mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (back) {
            ft.setCustomAnimations(R.anim.fade_in, R.anim.zoom_out,
                    R.anim.zoom_in, R.anim.fade_out);
        } else {
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out,
                    R.anim.fade_in, R.anim.fade_out);
        }
        if (back) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.content, fragment);
        ft.commit();

        mUpdateToolbar.postValue(true);
    }


}
