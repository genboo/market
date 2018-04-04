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

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.di.components.DaggerAppComponent;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.objects.Category;
import ru.devsp.apps.market.testing.SingleFragmentActivity;

import ru.devsp.apps.market.tools.ViewModelUtil;
import ru.devsp.apps.market.viewmodel.CategoriesViewModel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ru.devsp.apps.market.tools.TestUtils.withRecyclerView;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.not;

/**
 * Тестирование списка категорий
 * Created by gen on 15.12.2017.
 */
@RunWith(AndroidJUnit4.class)
public class CategoriesFragmentTest {

    private static final String CATEGORY_TITLE = "Категория";

    @Rule
    public final ActivityTestRule<SingleFragmentActivity> mRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    private CategoriesViewModel viewModel;
    private MutableLiveData<Resource<List<Category>>> categoriesList = new MutableLiveData<>();

    @Before
    public void init() {
        CategoriesFragment fragment = CategoriesFragment.getInstance(-1);

        viewModel = mock(CategoriesViewModel.class);
        when(viewModel.getCategories()).thenReturn(categoriesList);
        fragment.setComponent(DaggerAppComponent
                .builder()
                .context(mRule.getActivity().getApplicationContext())
                .build());
        fragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        mRule.getActivity().setFragment(fragment);
    }

    @Test
    public void categoriesListSuccess(){
        categoriesList.postValue(Resource.success(getCategories()));

        onView(withId(R.id.rv_list)).check(matches(isDisplayed()));
        int positionToCheck = 0;
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_category_title))
                .check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.rv_list).atPositionOnView(positionToCheck, R.id.tv_category_title))
                .check(matches(withText(CATEGORY_TITLE + positionToCheck)));
    }

    @Test
    public void categoriesListLoading() throws InterruptedException{
        categoriesList.postValue(Resource.loading(null));
        onView(withId(R.id.pb_load_progress)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_list)).check(matches(not(isDisplayed())));
    }

    private List<Category> getCategories(){
        List<Category> categories = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Category category = new Category(i, CATEGORY_TITLE + i);
            categories.add(category);
        }
        return categories;
    }

}
