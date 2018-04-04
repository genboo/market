package ru.devsp.apps.market.tests.api;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.List;

import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.objects.SearchGood;
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
public class SearchApiTest extends ApiTest {

    private MarketApi api;

    @Before
    public void init() throws IOException {
        api = createService(MarketApi.class);
    }

    @Test
    public void search() throws IOException, InterruptedException {
        RequestParams params = ApiTools.getParams("Search");

        enqueueResponse("SearchGood", params);

        List<SearchGood> items = getValue(api.search(params)).body;

        assertThat(items, notNullValue());
        assertThat(items.size(), is(1));
        assertThat(items.get(0).id, is(1L));
        assertThat(items.get(0).title, is("Ультрачерная тушь для ресниц «Бесконечный объем»"));
        assertThat(items.get(0).price, is(599f));
        assertThat(items.get(0).image, is("https://images.faberlic.com/images/fl/TflGoods/md/1000253179391_15132579061.jpg"));
    }

}
