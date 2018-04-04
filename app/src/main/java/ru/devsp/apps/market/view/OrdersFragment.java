package ru.devsp.apps.market.view;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.view.adapters.OrdersListAdapter;
import ru.devsp.apps.market.viewmodel.OrdersViewModel;

/**
 * Заказы
 * Created by gen on 22.12.2017.
 */

public class OrdersFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RecyclerView mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        initComponent();

        mList = view.findViewById(R.id.rv_list);
        mProgressBlock = view.findViewById(R.id.pb_load_progress);

        setHasOptionsMenu(false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateToolbar();

        OrdersViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(OrdersViewModel.class);

        final OrdersListAdapter adapter
                = new OrdersListAdapter(null,
                getString(R.string.pattern_price_order),
                getString(R.string.pattern_count_order),
                getString(R.string.pattern_number_order));
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mList.setAdapter(adapter);
        mList.setLayoutManager(manager);

        viewModel.getOrders().observe(this, orders -> {
            adapter.setItems(orders);
            updateUi(adapter);
            hideProgress();
        });

        adapter.setOnItemClickListener(position ->
                getNavigation().navigateToOrderItems(adapter.getItem(position).id));

        showProgress();
    }

    private void updateUi(OrdersListAdapter adapter) {
        if (mList.isComputingLayout()) {
            mList.post(adapter::notifyDataSetChanged);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void showProgress() {
        super.showProgress();
        mList.setVisibility(View.GONE);
    }

    @Override
    protected void hideProgress() {
        super.hideProgress();
        mList.setVisibility(View.VISIBLE);
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @Override
    protected String getTitle() {
        return "Заказы";
    }

}
