package com.lianjia.dubbo.gray.filter;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * @Description: 城市计算百分比的工具类
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/6/11 4:20 PM
 * @Version: 1.0
 */
public class CityFlowPercentUtil {

    /**
     *  MAX_LIMIT决定了圈定用户的精度，murmurHash保证了数据的散列性，只有在数据量足够的情况下保证数据的均匀性;
     */
    private static int MAX_LIMIT = 10001;

    public static boolean grayFlowMapping(String ucId, int limit) {
        HashFunction hf = Hashing.murmur3_32();
        int hashCode = hf.newHasher().putString(ucId, Charsets.UTF_8).hash().asInt();
        long unsignedInt = hashCode & 0x0FFFFFFFFL;
        long map = unsignedInt % MAX_LIMIT;
        return map <= limit * 100;
    }
}
