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
import ru.devsp.apps.market.model.objects.Order;
import ru.devsp.apps.market.testing.SingleFragmentActivity;
import ru.devsp.apps.market.tools.ViewModelUtil;
import ru.devsp.apps.market.viewmodel.OrdersViewModel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.devsp.apps.market.tools.TestUtils.withRecyclerView;

/**
 * Тестирование списка заказов
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class OrdersFragmentTest {

    private static final String ORDER_STATUS = "В работе";
    private static final float ORDER_PRICE = 1590f;
    private static final int ORDER_COUNT = 4;
    private static final String ORDER_TIME = "2017-12-15 13:25";

    @Rule
    public final ActivityTestRule<SingleFragmentActivity> mRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    private OrdersViewModel viewModel;
    private MutableLiveData<List<Order>> ordersList = new MutableLiveData<>();

    @Before
    public void init() {
        OrdersFragment fragment = new OrdersFragment();

        viewModel = mock(OrdersViewModel.class);
        when(viewModel.getOrders()).thenReturn(ordersList);
        fragment.setComponent(DaggerAppComponent
                .builder()
                .context(mRule.getActivity().getApplicationContext())
                .build());
        fragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        mRule.getActivity().setFragment(fragment);
    }

    @Test
    public void ordersList() {
        ordersList.postValue(getOrders());
        String patternSum = "Сумма: %.0f руб.";
        String patternCount = "Кол-во товаров: %s";
        String patternNum = "Номер заказа: %s";

        onView(withId(R.id.rv_list)).check(matches(isDisplayed()));
        int positionToCheck = 0;
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_order_status))
                .check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_order_status))
                .check(matches(withText(ORDER_STATUS)));
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_order_date))
                .check(matches(withText(ORDER_TIME)));
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_order_price))
                .check(matches(withText(String.format(Locale.getDefault(), patternSum, ORDER_PRICE))));
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_order_number))
                .check(matches(withText(String.format(Locale.getDefault(), patternNum, positionToCheck))));
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_order_count))
                .check(matches(withText(String.format(Locale.getDefault(), patternCount, ORDER_COUNT))));
    }

    private List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.price = ORDER_PRICE;
            order.id = i;
            order.status = ORDER_STATUS;
            order.count = ORDER_COUNT;
            order.time = ORDER_TIME;
            orders.add(order);
        }
        return orders;
    }

}
