package ru.devsp.apps.market.tests.api;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.objects.Order;
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
public class OrderApiTest extends ApiTest {

    private MarketApi api;

    @Before
    public void init() throws IOException {
        api = createService(MarketApi.class);
    }

    @Test
    public void checkout() throws IOException, InterruptedException {
        RequestParams params = ApiTools.getParams("Checkout");

        enqueueResponse("Order", params);

        Order items = getValue(api.checkout(params)).body;

        assertThat(items, notNullValue());
        assertThat(items.id, is(50L));
        assertThat(items.status, is("В работе"));
        assertThat(items.count, is(8));
        assertThat(items.price, is(3472f));
        assertThat(items.time, is("2018-01-30 12:03:35"));
    }

}
