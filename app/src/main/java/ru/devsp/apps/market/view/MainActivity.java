package ru.devsp.apps.market.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.view.comopnents.Navigation;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawer;
    private Navigation mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().addOnBackStackChangedListener(this::updateToolbarIcon);

        mNavigation = new Navigation(getSupportFragmentManager());
        mNavigation.getOnToolbarNeedChange().observe(this, update -> {
            if (update != null && update) {
                updateToolbarIcon();
            }
        });

        if (savedInstanceState == null) {
            mNavigation.navigateToCategories();
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    public void updateDrawer(Toolbar toolbar) {
        mDrawer.removeDrawerListener(mToggle);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.setToolbarNavigationClickListener(v -> onBackPressed());
        mToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            updateToolbarIcon();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_categories) {
            mNavigation.navigateToCategories();
        } else if (item.getItemId() == R.id.nav_cart) {
            mNavigation.navigateToCart();
        } else if (item.getItemId() == R.id.nav_orders) {
            mNavigation.navigateToOrders();
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Navigation getNavigation() {
        return mNavigation;
    }

    public void updateToolbarIcon() {
        if (getSupportActionBar() != null) {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() == 0) {
                mToggle.setDrawerIndicatorEnabled(true);
            } else {
                mToggle.setDrawerIndicatorEnabled(false);
            }
        }
    }

}
