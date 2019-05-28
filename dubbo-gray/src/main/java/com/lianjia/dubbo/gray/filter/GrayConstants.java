/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lianjia.dubbo.gray.filter;


public class GrayConstants {

    //ketrace参数前缀
    public static final String FILTER_PARAM_PREFIX = "ketracespid";

    //经纪人登录UCID
    public static final String FILTER_PARAM_UCID = FILTER_PARAM_PREFIX + "ucid";

    //所在城市编码
    public static final String FILTER_PARAM_CITYCODE = FILTER_PARAM_PREFIX + "citycode";

    //当前作业城市编码
    public static final String FILTER_PARAM_CUR_WORK_CITYCODE = FILTER_PARAM_PREFIX + "curworkcitycode";

    //左边中括号
    public static final String BRACKET_LEFT = "[";

    //右边中括号
    public static final String BRACKET_RIGHT = "]";

    public static final String EMPTY_STR = "";

    public static final String DOUBLE_QUOTES = "\"";

    public static final String SINGLE_QUOTES = "'";
}
