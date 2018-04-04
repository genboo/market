package ru.devsp.apps.market.view;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.api.tools.Status;
import ru.devsp.apps.market.model.objects.Category;
import ru.devsp.apps.market.view.adapters.CategoriesListAdapter;
import ru.devsp.apps.market.viewmodel.CategoriesViewModel;

/**
 * Список категорий
 * Created by gen on 22.12.2017.
 */

public class CategoriesFragment extends BaseFragment {

    private static final String ARG_CATEGORY_ID = "id";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RecyclerView mList;

    public static CategoriesFragment getInstance(long id) {
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, id);
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        initComponent();

        mList = view.findViewById(R.id.rv_list);
        mProgressBlock = view.findViewById(R.id.pb_load_progress);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateToolbar();

        CategoriesViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoriesViewModel.class);

        CategoriesListAdapter adapter = new CategoriesListAdapter(null);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mList.setAdapter(adapter);
        mList.setLayoutManager(manager);

        viewModel.getCategories().observe(this, resource -> {
            if (resource != null) {
                processResult(resource, adapter);
            }
        });

        showProgress();
        viewModel.setCategory(getArguments().getLong(ARG_CATEGORY_ID));
    }

    private void processResult(Resource<List<Category>> resource, CategoriesListAdapter adapter) {
        if (resource.data != null && !resource.data.isEmpty()) {
            adapter.setItems(resource.data);
            adapter.setOnItemClickListener(position ->
                    navigate(resource.data.get(position)));
            updateList(adapter);
            hideProgress();
        } else if (resource.status == Status.ERROR) {
            showSnack(resource.message, null);
        }
        if (resource.status == Status.SUCCESS || resource.status == Status.ERROR) {
            hideProgress();
        }
    }

    private void navigate(Category item) {
        if (item.childs == 0) {
            getNavigation().navigateToGoods(item.id);
        } else {
            getNavigation().navigateToCategory(item.id);
        }
    }

    private void updateList(CategoriesListAdapter adapter) {
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
        return "Каталог";
    }

}
