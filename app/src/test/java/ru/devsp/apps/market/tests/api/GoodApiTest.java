package ru.devsp.apps.market.tests.api;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.List;

import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.objects.Good;
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
public class GoodApiTest extends ApiTest {

    private MarketApi api;

    @Before
    public void init() throws IOException {
        api = createService(MarketApi.class);
    }

    @Test
    public void getGoods() throws IOException, InterruptedException {
        RequestParams params = ApiTools.getParams("GetGoods");
        params.setParam(RequestParams.PARAM_CATEGORY, 2);

        enqueueResponse("Good", params);

        List<Good> items = getValue(api.getGoods(params)).body;

        assertThat(items, notNullValue());
        assertThat(items.size(), is(2));
        assertThat(items.get(0).id, is(2L));
        assertThat(items.get(0).title, is("Крем"));
        assertThat(items.get(0).image, is("https://images.faberlic.com/images/fl/TflGoods/md/1000344850272_15052989052.jpg"));
    }

    @Test
    public void getGood() throws IOException, InterruptedException {
        RequestParams params = ApiTools.getParams("GetGood");
        params.setParam(RequestParams.PARAM_GOOD, 1);

        enqueueResponse("Good", params);

        Good item = getValue(api.getGood(params)).body;

        assertThat(item, notNullValue());
        assertThat(item.id, is(1L));
        assertThat(item.title, is("Духи"));
        assertThat(item.images, notNullValue());
        assertThat(item.images.length, is(2));
        assertThat(item.images[1], is("https://images.faberlic.com/images/fl/TflGoods/md/1000607309817_15100386743.jpg"));
    }

}
