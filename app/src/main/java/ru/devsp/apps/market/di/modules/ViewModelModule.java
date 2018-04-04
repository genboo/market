package ru.devsp.apps.market.di.modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import ru.devsp.apps.market.di.ViewModelFactory;
import ru.devsp.apps.market.viewmodel.CartViewModel;
import ru.devsp.apps.market.viewmodel.CategoriesViewModel;
import ru.devsp.apps.market.viewmodel.GoodsViewModel;
import ru.devsp.apps.market.viewmodel.OrderGoodsViewModel;
import ru.devsp.apps.market.viewmodel.OrdersViewModel;
import ru.devsp.apps.market.viewmodel.SearchViewModel;

/**
 * ViewModel
 * Created by gen on 12.09.2017.
 */

@SuppressWarnings("unused")
@Module
public interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel.class)
    ViewModel bindCategoriesViewModel(CategoriesViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GoodsViewModel.class)
    ViewModel bindGoodsViewModel(GoodsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CartViewModel.class)
    ViewModel bindCartViewModel(CartViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    ViewModel bindSearchViewModel(SearchViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(OrdersViewModel.class)
    ViewModel bindOrdersViewModel(OrdersViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(OrderGoodsViewModel.class)
    ViewModel bindOrderGoodsViewModel(OrderGoodsViewModel viewModel);

    @Binds
    ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

}