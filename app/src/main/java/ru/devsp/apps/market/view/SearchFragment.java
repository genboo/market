package ru.devsp.apps.market.view;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.api.tools.Status;
import ru.devsp.apps.market.model.objects.SearchGood;
import ru.devsp.apps.market.view.adapters.SearchListAdapter;
import ru.devsp.apps.market.viewmodel.SearchViewModel;

/**
 * Поиск
 * Created by gen on 22.12.2017.
 */

public class SearchFragment extends BaseFragment {

    private static final String ARG_QUERY = "query";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RecyclerView mList;

    private SearchViewModel mViewModel;

    public static SearchFragment getInstance(String query) {
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
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

        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);

        SearchListAdapter adapter = new SearchListAdapter(null, getString(R.string.pattern_price));

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mList.setAdapter(adapter);
        mList.setLayoutManager(manager);

        mViewModel.getGoods().observe(this, resource -> {
            if (resource != null) {
                processResult(resource, adapter);
            }
        });
        showProgress();
        mViewModel.setQuery(getArguments().getString(ARG_QUERY));
    }

    private void processResult(Resource<List<SearchGood>> resource, SearchListAdapter adapter) {
        if (resource.data != null && !resource.data.isEmpty()) {
            adapter.setItems(resource.data);
            adapter.setOnItemClickListener(position ->
                    getNavigation().navigateToGood(resource.data.get(position).id));
            updateList(adapter);
            hideProgress();
        } else if (resource.status == Status.ERROR) {
            showSnack(resource.message, null);
        }
        if (resource.status == Status.SUCCESS || resource.status == Status.ERROR) {
            hideProgress();
        }
    }

    private void updateList(SearchListAdapter adapter) {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQuery(getArguments().getString(ARG_QUERY), false);
        searchView.setQueryHint("Поиск");
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.setQuery(query);
                Bundle args = new Bundle();
                args.putString(ARG_QUERY, query);
                setArguments(args);
                updateTitle(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @Override
    protected String getTitle() {
        return getArguments().getString(ARG_QUERY);
    }

}
