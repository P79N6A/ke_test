package com.alibaba.dubbo.rpc.protocol.restful;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.protocol.AbstractInvoker;

import java.util.Map;

/**
 * Created by chengtianliang on 2016/11/23.
 */
public class HttpRestfullInvoker<T> extends AbstractInvoker<T> {
    public HttpRestfullInvoker(Class<T> type, URL url) {
        super(type, url);
    }

    public HttpRestfullInvoker(Class<T> type, URL url, String[] keys) {
        super(type, url, keys);
    }

    public HttpRestfullInvoker(Class<T> type, URL url, Map<String, String> attachment) {
        super(type, url, attachment);
    }

    @Override
    protected Result doInvoke(Invocation invocation) throws Throwable {
        return null;
    }
}
