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
import ru.devsp.apps.market.model.objects.Cart;
import ru.devsp.apps.market.model.objects.CartTotal;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.model.objects.Order;
import ru.devsp.apps.market.testing.SingleFragmentActivity;
import ru.devsp.apps.market.tools.ViewModelUtil;
import ru.devsp.apps.market.viewmodel.CartViewModel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.devsp.apps.market.tools.TestUtils.withRecyclerView;

/**
 * Тестирование корзины
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class CartFragmentTest {

    private static final int CART_COUNT = 4;
    private static final String GOOD_TITLE = "Духи";
    private static final float GOOD_PRICE = 155f;
    private static final float CART_TOTAL = 1550f;


    @Rule
    public final ActivityTestRule<SingleFragmentActivity> mRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    private CartViewModel viewModel;
    private MutableLiveData<List<Cart>> goodsList = new MutableLiveData<>();
    private MutableLiveData<Resource<Order>> order = new MutableLiveData<>();
    private MutableLiveData<CartTotal> total = new MutableLiveData<>();

    @Before
    public void init() {
        CartFragment fragment = new CartFragment();

        viewModel = mock(CartViewModel.class);
        when(viewModel.getGoods()).thenReturn(goodsList);
        when(viewModel.getTotal()).thenReturn(total);
        when(viewModel.getOrder()).thenReturn(order);

        fragment.setComponent(DaggerAppComponent
                .builder()
                .context(mRule.getActivity().getApplicationContext())
                .build());
        fragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        mRule.getActivity().setFragment(fragment);
    }

    @Test
    public void goodsList() {
        goodsList.postValue(getCart());
        total.postValue(getTotal());
        String patternSum = "%.0f руб.";

        onView(withId(R.id.rv_list)).check(matches(isDisplayed()));
        int positionToCheck = 0;
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_good_title))
                .check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_good_title))
                .check(matches(withText(GOOD_TITLE + positionToCheck)));
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_good_price))
                .check(matches(withText(String.format(Locale.getDefault(), patternSum, GOOD_PRICE))));
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_cart_count))
                .check(matches(withText("" + CART_COUNT)));

        onView(withId(R.id.tv_cart_total)).check(matches(withText(String.format(Locale.getDefault(), patternSum, CART_TOTAL))));

    }

    @Test
    public void actions() {
        goodsList.postValue(getCart());
        total.postValue(getTotal());

        onView(withId(R.id.rv_list)).check(matches(isDisplayed()));
        int positionToCheck = 0;


        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.ib_cart_count_plus))
                .perform(click());

        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_cart_count))
                .check(matches(withText("" + (CART_COUNT + 1))));

        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.ib_cart_count_minus))
                .perform(click());
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.ib_cart_count_minus))
                .perform(click());

        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_cart_count))
                .check(matches(withText("" + (CART_COUNT - 1))));

        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.ib_cart_delete))
                .perform(click());

        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_good_title))
                .check(matches(withText(GOOD_TITLE + (positionToCheck + 1))));
    }

    private List<Cart> getCart() {
        List<Cart> goods = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Cart item = new Cart();
            item.count = CART_COUNT;
            item.good = new Good(i, GOOD_TITLE + i);
            item.good.price = GOOD_PRICE;
            goods.add(item);
        }
        return goods;
    }

    private CartTotal getTotal(){
        CartTotal total = new CartTotal();
        total.total = CART_TOTAL;
        return total;
    }

}
