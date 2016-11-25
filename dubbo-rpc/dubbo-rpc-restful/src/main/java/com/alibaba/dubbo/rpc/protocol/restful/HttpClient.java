package com.alibaba.dubbo.rpc.protocol.restful;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by chengtianliang on 2016/11/25.
 */
public class HttpClient {

    private OkHttpClient client;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public HttpClient(int connectionTimeout, int readTimeout) {
        client = new OkHttpClient.Builder()
                .connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .followRedirects(true).build();
    }

    /**
     * @param url
     * @param reqHeader
     * @param jsonReq
     * @param respHeader
     * @return
     */
    public String invokeWithJson(String url,
                                 Map<String, String> reqHeader, String jsonReq, Map<String, String> respHeader)
            throws IOException {

        RequestBody body = RequestBody.create(JSON, jsonReq);

        Request.Builder builder = new Request.Builder();
        if (null != reqHeader) {
            for (Map.Entry<String, String> entry : reqHeader.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.url(url).post(body).build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        if (respHeader != null) {
            Headers headers = response.headers();
            for (String name : headers.names()) {
                respHeader.put(name, headers.get(name));
            }
        }
        return json;
    }
}
