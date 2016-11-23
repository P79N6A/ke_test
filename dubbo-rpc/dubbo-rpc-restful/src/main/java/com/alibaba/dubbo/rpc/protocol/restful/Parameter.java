package com.alibaba.dubbo.rpc.protocol.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chengtianliang on 2016/11/23.
 */
public class Parameter {
    private List<Object> params = new ArrayList<>();

    private Map<String, String> attachments = new HashMap<>();

    public List<?> getParams() {
        return params;
    }

    public void addParameter(Object param) {
        params.add(param);
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }
}
