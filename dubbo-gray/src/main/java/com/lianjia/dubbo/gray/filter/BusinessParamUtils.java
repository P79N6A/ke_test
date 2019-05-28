package com.lianjia.dubbo.gray.filter;

/**
 * @author liupinghe
 */
public class BusinessParamUtils {

    private static final ThreadLocal<String> ucIdCache = new ThreadLocal<String>();

    private static final ThreadLocal<String> cityCodeCache = new ThreadLocal<String>();

    private static final ThreadLocal<String> curWorkCityCodeCache = new ThreadLocal<>();


    public static String getUcId() {
        return ucIdCache.get();
    }

    public static void setUcId(String ucId) {
        ucIdCache.set(ucId);
    }

    public static String getCityCode() {
        return cityCodeCache.get();
    }

    public static void setCityCode(String cityCode) {
        cityCodeCache.set(cityCode);
    }

    public static String getCurWorkCityCode() {
        return curWorkCityCodeCache.get();
    }

    public static void setCurWorkCityCode(String curWorkCityCode){
        curWorkCityCodeCache.set(curWorkCityCode);
    }

    public static String getBusinessParamByKey(String key) {
        switch (key) {
            case GrayConstants.FILTER_PARAM_UCID:
                return getUcId();
            case GrayConstants.FILTER_PARAM_CITYCODE:
                return getCityCode();
            case GrayConstants.FILTER_PARAM_CUR_WORK_CITYCODE:
                return getCurWorkCityCode();
            default:
                return null;
        }
    }

    public static void setBusinessParamByKey(String key, String value) {
        switch (key) {
            case GrayConstants.FILTER_PARAM_UCID: {
                setUcId(value);
                break;
            }
            case GrayConstants.FILTER_PARAM_CITYCODE: {
                setCityCode(value);
                break;
            }
            default: {
            }
        }
    }

    public static void clear() {
        ucIdCache.remove();
        cityCodeCache.remove();
    }
}
