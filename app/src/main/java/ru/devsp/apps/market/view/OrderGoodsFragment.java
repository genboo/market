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

import java.util.List;

import javax.inject.Inject;

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.api.tools.Status;
import ru.devsp.apps.market.model.objects.OrderGood;
import ru.devsp.apps.market.view.adapters.OrderGoodsListAdapter;
import ru.devsp.apps.market.viewmodel.OrderGoodsViewModel;

/**
 * Список товаров в заказе
 * Created by gen on 22.12.2017.
 */

public class OrderGoodsFragment extends BaseFragment {

    private static final String ARG_ORDER_ID = "id";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RecyclerView mList;

    public static OrderGoodsFragment getInstance(long id) {
        Bundle args = new Bundle();
        args.putLong(ARG_ORDER_ID, id);
        OrderGoodsFragment fragment = new OrderGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_items, container, false);
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

        OrderGoodsViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(OrderGoodsViewModel.class);

        final OrderGoodsListAdapter adapter = new OrderGoodsListAdapter(null,
                getString(R.string.pattern_price),
                getString(R.string.pattern_count_order));
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mList.setAdapter(adapter);
        mList.setLayoutManager(manager);

        viewModel.getOrderGoods().observe(this, resource -> {
            if (resource != null) {
                processResult(resource, adapter);
            }
        });
        viewModel.setOrder(getArguments().getLong(ARG_ORDER_ID));

        showProgress();
    }

    private void processResult(Resource<List<OrderGood>> resource, OrderGoodsListAdapter adapter) {
        if (resource.data != null && !resource.data.isEmpty()) {
            adapter.setItems(resource.data);
            updateUi(adapter);
            hideProgress();
        } else if (resource.status == Status.ERROR) {
            showSnack(resource.message, null);
        }
        if(resource.status == Status.SUCCESS || resource.status == Status.ERROR){
            hideProgress();
        }
    }

    private void updateUi(OrderGoodsListAdapter adapter) {
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
        return "Список товаров";
    }

}
