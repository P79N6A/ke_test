import com.alibaba.fastjson.JSONArray;
import com.lianjia.dubbo.gray.rule.domain.GrayRule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liupinghe
 */
public class JSONTest {

    public static void main(String[] args) {
        List<GrayRule> grayRuleList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GrayRule grayRule = new GrayRule();
            grayRule.setApplication("application_" + i);
            grayRule.setOpen(true);
            Set set = new HashSet<String>(){};
            set.add("10.33.76.22");
            grayRule.setGrayServerIpSet(set);
            grayRule.setServerPort(20881);
            Set<String> ucIdSet = new HashSet<>();
            ucIdSet.add("123456");
            ucIdSet.add("123");
            grayRule.setGrayUcIdSet(ucIdSet);
            grayRuleList.add(grayRule);

        }

        String json = JSONArray.toJSONString(grayRuleList);
        System.out.println(json);

    }


}
