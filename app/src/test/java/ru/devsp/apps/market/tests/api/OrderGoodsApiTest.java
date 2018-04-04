package ru.devsp.apps.market.tests.api;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.List;

import ru.devsp.apps.market.model.api.MarketApi;
import ru.devsp.apps.market.model.api.RequestParams;
import ru.devsp.apps.market.model.objects.OrderGood;
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
public class OrderGoodsApiTest extends ApiTest {

    private MarketApi api;

    @Before
    public void init() throws IOException {
        api = createService(MarketApi.class);
    }

    @Test
    public void getOrderItems() throws IOException, InterruptedException {
        RequestParams params = ApiTools.getParams("GetOrderItems");

        enqueueResponse("OrderGood", params);

        List<OrderGood> items = getValue(api.getOrderItems(params)).body;

        assertThat(items, notNullValue());
        assertThat(items.size(), is(2));
        assertThat(items.get(1).id, is(25L));
        assertThat(items.get(1).title, is("Бальзам для губ «Вишневый конфитюр»"));
        assertThat(items.get(1).image, is("https://images.faberlic.com/images/fl/TflGoods/md/1000212003561_15005613512.jpg"));
        assertThat(items.get(1).price, is(159f));
        assertThat(items.get(1).count, is(3));
    }

}
