package ru.devsp.apps.market.view;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.Locale;

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.di.components.DaggerAppComponent;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.testing.SingleFragmentActivity;
import ru.devsp.apps.market.tools.ViewModelUtil;
import ru.devsp.apps.market.viewmodel.GoodsViewModel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование карточки товара
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class GoodFragmentTest {

    private static final String GOOD_TITLE = "Духи";
    private static final String GOOD_DESC = "Описание духов";
    private static final float GOOD_PRICE = 159f;

    @Rule
    public final ActivityTestRule<SingleFragmentActivity> mRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    private GoodsViewModel viewModel;
    private MutableLiveData<Resource<Good>> good = new MutableLiveData<>();
    private MutableLiveData<String[]> images = new MutableLiveData<>();

    @Before
    public void init() {
        GoodFragment fragment = GoodFragment.getInstance(1L);

        viewModel = mock(GoodsViewModel.class);
        when(viewModel.getGood()).thenReturn(good);
        when(viewModel.getImages()).thenReturn(images);
        fragment.setComponent(DaggerAppComponent
                .builder()
                .context(mRule.getActivity().getApplicationContext())
                .build());
        fragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        mRule.getActivity().setFragment(fragment);
    }

    @Test
    public void good(){
        good.postValue(Resource.success(getGood()));
        String pattern = "%.0f руб.";

        onView(withId(R.id.tv_good_price)).check(matches(withText(String.format(Locale.getDefault(), pattern, GOOD_PRICE))));
        onView(withId(R.id.tv_good_title)).check(matches(withText(GOOD_TITLE)));
        onView(withId(R.id.tv_good_desc)).check(matches(withText(GOOD_DESC)));

    }


    private Good getGood(){
        Good good = new Good(1L);
        good.price = GOOD_PRICE;
        good.title = GOOD_TITLE;
        good.desc = GOOD_DESC;
        return good;
    }

}
