package ru.devsp.apps.market.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import ru.devsp.apps.market.BuildConfig;
import ru.devsp.apps.market.tools.Logger;

/**
 * Параметры запроса
 * Created by gen on 30.08.2017.
 */
@SuppressWarnings("unused")
public class RequestParams {

    private static final String API_JSON = "2.0";
    public static final String PARAM_CATEGORY = "category";
    public static final String PARAM_GOOD = "good";
    public static final String PARAM_GOODS = "goods";
    public static final String PARAM_QUERY = "query";
    public static final String PARAM_ORDER = "order";

    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("jsonrpc")
    @Expose
    private String jsonrpc;

    @SerializedName("id")
    @Expose
    private String id = BuildConfig.VERSION_NAME + ":" + System.currentTimeMillis() / 1000L;

    @SerializedName("params")
    @Expose
    private Map<String, Object> params = new HashMap<>();

    public RequestParams setParam(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public String getParam(String key) {
        try {
            return params.containsKey(key) ? String.valueOf(params.get(key)) : "";
        }catch (Exception ex){
            Logger.e(ex);
            return "";
        }
    }

    private void setJsonRpc(String jsonRpc) {
        this.jsonrpc = jsonRpc;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    /**
     * Базовые параметры запроса
     *
     * @return Сформированные параметры запроса
     */
    public static RequestParams getParams() {
        RequestParams params = new RequestParams();
        params.setJsonRpc(API_JSON);
        return params;
    }
}