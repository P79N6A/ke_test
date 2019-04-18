package com.lianjia.dubbo.rpc.filter;

/**
 * @author liupinghe
 */
public class BusinessParamUtils {

    private static final ThreadLocal<String> ucIdCache = new ThreadLocal<String>();

    public static String getUcId() {
        return ucIdCache.get();
    }

    public static void setUcId(String traceId) {
        ucIdCache.set(traceId);
    }

    public static void clear() {
        ucIdCache.remove();
    }
}
