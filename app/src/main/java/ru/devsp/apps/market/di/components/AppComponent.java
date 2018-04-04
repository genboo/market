package ru.devsp.apps.market.di.components;

import android.content.Context;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import ru.devsp.apps.market.di.modules.DbModule;
import ru.devsp.apps.market.di.modules.RetrofitModule;
import ru.devsp.apps.market.di.modules.ViewModelModule;
import ru.devsp.apps.market.view.CartFragment;
import ru.devsp.apps.market.view.CategoriesFragment;
import ru.devsp.apps.market.view.GoodFragment;
import ru.devsp.apps.market.view.GoodsFragment;
import ru.devsp.apps.market.view.MainActivity;
import ru.devsp.apps.market.view.OrderGoodsFragment;
import ru.devsp.apps.market.view.OrdersFragment;
import ru.devsp.apps.market.view.SearchFragment;
import ru.devsp.apps.market.view.SuccessFragment;

/**
 * Компонент di
 * Created by gen on 27.09.2017.
 */
@Component(modules = {
        ViewModelModule.class,
        RetrofitModule.class,
        DbModule.class
})
@Singleton
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);

        AppComponent build();
    }

    void inject(MainActivity activity);

    void inject(GoodsFragment fragment);

    void inject(GoodFragment fragment);

    void inject(CartFragment fragment);

    void inject(CategoriesFragment fragment);

    void inject(SearchFragment fragment);

    void inject(SuccessFragment fragment);

    void inject(OrdersFragment fragment);

    void inject(OrderGoodsFragment fragment);

}
