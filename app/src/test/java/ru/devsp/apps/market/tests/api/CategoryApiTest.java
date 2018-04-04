package ru.devsp.apps.market.tests.api;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.List;

import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.objects.Category;
import ru.devsp.apps.market.tools.ApiTools;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static ru.devsp.apps.market.tools.LiveDataTestUtil.getValue;

/**
 *
 * Created by gen on 15.09.2017.
 */
@RunWith(JUnit4.class)
public class CategoryApiTest extends ApiTest {

    private MarketApi api;

    @Before
    public void init() throws IOException {
        api = createService(MarketApi.class);
    }

    @Test
    public void getCategories() throws IOException, InterruptedException {
        RequestParams params = ApiTools.getParams("GetCategories");

        enqueueResponse("Category", params);

        List<Category> items = getValue(api.getCategories(params)).body;

        assertThat(items, notNullValue());
        assertThat(items.size(), is(2));
        assertThat(items.get(1).id, is(4L));
        assertThat(items.get(1).title, is("Уход за лицом"));
    }

}
