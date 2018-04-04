package ru.devsp.apps.market.view;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.di.components.DaggerAppComponent;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.model.objects.SearchGood;
import ru.devsp.apps.market.testing.SingleFragmentActivity;
import ru.devsp.apps.market.tools.ViewModelUtil;
import ru.devsp.apps.market.viewmodel.GoodsViewModel;
import ru.devsp.apps.market.viewmodel.SearchViewModel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.devsp.apps.market.tools.TestUtils.withRecyclerView;

/**
 * Тестирование поиска товаров
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class SearchFragmentTest {

    private static final String GOOD_TITLE = "Товар";
    private static final float GOOD_PRICE = 159f;

    @Rule
    public final ActivityTestRule<SingleFragmentActivity> mRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    private SearchViewModel viewModel;
    private MutableLiveData<Resource<List<SearchGood>>> goodsList = new MutableLiveData<>();

    @Before
    public void init() {
        SearchFragment fragment = SearchFragment.getInstance("test query");

        viewModel = mock(SearchViewModel.class);
        when(viewModel.getGoods()).thenReturn(goodsList);
        fragment.setComponent(DaggerAppComponent
                .builder()
                .context(mRule.getActivity().getApplicationContext())
                .build());
        fragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        mRule.getActivity().setFragment(fragment);
    }

    @Test
    public void categoriesListSuccess() {
        goodsList.postValue(Resource.success(getGoods()));
        String patternPrice = "%.0f руб.";

        onView(withId(R.id.rv_list)).check(matches(isDisplayed()));
        int positionToCheck = 0;
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_good_title))
                .check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_good_title))
                .check(matches(withText(GOOD_TITLE + positionToCheck)));
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_good_price))
                .check(matches(withText(String.format(Locale.getDefault(), patternPrice, GOOD_PRICE))));
    }

    @Test
    public void categoriesListLoading() throws InterruptedException {
        goodsList.postValue(Resource.loading(null));
        onView(withId(R.id.pb_load_progress)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_list)).check(matches(not(isDisplayed())));
    }

    private List<SearchGood> getGoods() {
        List<SearchGood> goods = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SearchGood good = new SearchGood();
            good.id = i;
            good.price = GOOD_PRICE;
            good.title = GOOD_TITLE + i;
            goods.add(good);
        }
        return goods;
    }

}
