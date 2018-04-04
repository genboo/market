package ru.devsp.apps.market.view;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import ru.devsp.apps.market.App;
import ru.devsp.apps.market.R;
import ru.devsp.apps.market.di.components.AppComponent;
import ru.devsp.apps.market.view.comopnents.Navigation;

/**
 * Базовый фрагмент
 * Created by gen on 20.09.2017.
 */

public abstract class BaseFragment extends Fragment {

    protected View mProgressBlock;

    AppComponent component;

    /**
     * Инициализировать toolbar
     */
    protected void updateToolbar() {
        if (getActivity() instanceof MainActivity && getView() != null) {
            MainActivity activity = (MainActivity) getActivity();
            Toolbar toolbar = getView().findViewById(R.id.toolbar);
            activity.setSupportActionBar(toolbar);
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setTitle(getTitle());
                activity.getSupportActionBar().show();
                activity.updateDrawer(toolbar);
            }
        }
    }

    protected void updateTitle(String title) {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle(title);
            }
        }
    }

    protected void initComponent() {
        if (component == null) {
            component = ((App) getActivity().getApplication()).getAppComponent();
            inject();
        }
    }

    public void setComponent(AppComponent component) {
        this.component = component;
    }

    public AppComponent getComponent() {
        return component;
    }

    protected abstract void inject();

    protected String getTitle() {
        return getResources().getString(R.string.app_name);
    }

    public Navigation getNavigation() {
        return ((MainActivity) getActivity()).getNavigation();
    }

    protected void showSnack(int text, @Nullable View.OnClickListener action) {
        if (getView() != null) {
            Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.cl_main),
                    text, Snackbar.LENGTH_SHORT);
            if (action != null) {
                snackbar.setAction(R.string.action_cancel, action);
            }
            snackbar.show();
        }
    }

    protected void showSnack(String text, @Nullable View.OnClickListener action) {
        if (getView() != null) {
            Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.cl_main),
                    text, Snackbar.LENGTH_SHORT);
            if (action != null) {
                snackbar.setAction(R.string.action_cancel, action);
            }
            snackbar.show();
        }
    }

    protected void showProgress() {
        mProgressBlock.setVisibility(View.VISIBLE);
    }

    protected void hideProgress() {
        mProgressBlock.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Поиск");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNavigation().navigateToSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}
