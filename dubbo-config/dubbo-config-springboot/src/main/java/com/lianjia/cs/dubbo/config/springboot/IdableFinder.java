package com.lianjia.cs.dubbo.config.springboot;

import java.util.List;

/**
 * Created by chengtianliang on 2016/12/2.
 */
public class IdableFinder {
    public static  <T extends Idable> T findProperty(String id, List<T> list) {
        if (list == null) return null;
        for (T obj : list) {
            if (id.equals(obj)) {
                return obj;
            }
        }
        return null;
    }
}
