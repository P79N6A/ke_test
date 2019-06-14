package com.lianjia.dubbo.gray.filter;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.*;

/**
 * @Description: 城市计算百分比的工具类
 * @Author: qinxiaoyun001@lianjia.com
 * @Date: 2019/6/11 4:20 PM
 * @Version: 1.0
 */
public class CityFlowPercentUtil {

    /**
     * MAX_LIMIT决定了圈定用户的精度，murmurHash保证了数据的散列性，只有在数据量足够的情况下保证数据的均匀性;
     */
    private static final int MAX_LIMIT = 10001;

    public static boolean grayFlowMapping(String ucId, int limit) {
        HashFunction hf = Hashing.murmur3_32();
        int hashCode = hf.newHasher().putString(ucId, Charsets.UTF_8).hash().asInt();
        long unsignedInt = hashCode & 0x0FFFFFFFFL;
        long map = unsignedInt % MAX_LIMIT;
        return map <= limit * 100;
    }


    public static void main(String[] args) {

        int limit = 15;
        for (int i = 0; i < 300; i++) {
            testHash(i * 50, limit);
        }
    }


    public static void testHash(int total, int limit)  {

        File file = new File("/Users/qinxiaoyun/Desktop/user_id.txt");
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            InputStreamReader inputStreamReader = null;
            inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String userId = "";
            float gray = 0;
            float notGray = 0;
            int count = 0;
            try {
                while (StringUtils.isNotEmpty(userId = reader.readLine())) {
                    if (count > total) {
                        break;
                    }
                    if (grayFlowMapping(userId, limit)) {
                        gray++;
                    } else {
                        notGray++;
                    }
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("total: " + total + ", percent: " + gray / (gray + notGray)) ;
            try {
                fileInputStream.close();
                inputStreamReader.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("file not exist");
        }

    }
}
