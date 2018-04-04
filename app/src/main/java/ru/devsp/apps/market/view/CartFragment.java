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
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import javax.inject.Inject;

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.model.api.tools.Status;
import ru.devsp.apps.market.view.adapters.CartListAdapter;
import ru.devsp.apps.market.viewmodel.CartViewModel;

/**
 * Корзина
 * Created by gen on 22.12.2017.
 */

public class CartFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RecyclerView mList;
    private TextView mTotal;
    private Button mCheckout;
    private View mContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        initComponent();

        mList = view.findViewById(R.id.rv_list);
        mProgressBlock = view.findViewById(R.id.pb_load_progress);
        mTotal = view.findViewById(R.id.tv_cart_total);
        mCheckout = view.findViewById(R.id.btn_offer);
        mContent = view.findViewById(R.id.cl_content_block);

        setHasOptionsMenu(false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateToolbar();

        CartViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(CartViewModel.class);

        final CartListAdapter adapter = new CartListAdapter(null, getString(R.string.pattern_price));
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mList.setAdapter(adapter);
        mList.setLayoutManager(manager);

        viewModel.getGoods().observe(this, goods -> {
            viewModel.getGoods().removeObservers(CartFragment.this);
            adapter.setItems(goods);
            updateUi(adapter);
            hideProgress();
        });

        viewModel.getTotal().observe(this, total -> {
            if (total != null) {
                mTotal.setText(String.format(Locale.getDefault(), getString(R.string.pattern_price), total.total));
            }
        });

        adapter.setOnItemClickListener(position ->
                getNavigation().navigateToGood(adapter.getItem(position).id));

        adapter.setOnChangeCountListener((position, cart) -> viewModel.update(cart));
        adapter.setOnDeleteListener((position, cart) -> {
            viewModel.delete(cart);
            showSnack(R.string.action_deleted, v -> {
                viewModel.addToCart(cart);
                adapter.add(position, cart);
                updateUi(adapter);
            });
            updateUi(adapter);
        });

        mCheckout.setOnClickListener(v -> {
            showProgress();
            viewModel.checkout(adapter.getItems());
        });

        viewModel.getOrder().observe(this, resource -> {
            if (resource != null) {
                if (resource.status == Status.SUCCESS) {
                    viewModel.clear();
                    getNavigation().navigateToSuccess();
                } else if (resource.status == Status.ERROR) {
                    hideProgress();
                    showSnack(resource.message, null);
                }
            }
        });

        showProgress();
    }

    private void updateUi(CartListAdapter adapter) {
        if (mList.isComputingLayout()) {
            mList.post(adapter::notifyDataSetChanged);
        } else {
            adapter.notifyDataSetChanged();
        }
        if (adapter.getItemCount() == 0) {
            mCheckout.setEnabled(false);
        } else {
            mCheckout.setEnabled(true);
        }
    }

    @Override
    protected void showProgress() {
        super.showProgress();
        mContent.setVisibility(View.GONE);
    }

    @Override
    protected void hideProgress() {
        super.hideProgress();
        mContent.setVisibility(View.VISIBLE);
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @Override
    protected String getTitle() {
        return "Корзина";
    }

}
