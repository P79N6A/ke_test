import com.lianjia.dubbo.gray.cluster.loadbalance.DubboGrayApolloConfig;
import com.lianjia.dubbo.gray.rule.GrayRulesCache;
import com.lianjia.dubbo.gray.rule.domain.GrayRule;
import com.lianjia.dubbo.gray.rule.domain.RuleInfo;

/**
 * @author liupinghe
 */
public class JSONTest {

    /**
     * 验证 示例 代码，updateGrayRulesCache 是 private 方法，此代码放到 DubboGrayApolloConfig 类即可正常执行
     *
     * @param args
     */
    public static void main(String[] args) {
        String dubboGray = "[\n" +
                "    {\n" +
                "        \"application\":\"sop.mls.lianjia.com\",\n" +
                "        \"isOpen\":\"true\",\n" +
                "        \"grayServerIpMap\":{\n" +
                "            \"10.200.16.138\":{\n" +
                "                \"grayUcIdSet\":[\n" +
                "                    \"1000001000024736\",\n" +
                "                    \"1000001000024721\"\n" +
                "                ],\n" +
                "                \"grayCityCodeMap\":{\n" +
                "                    \"666666\":\"20\"\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        \"serverPort\":\"28640\"\n" +
                "    }\n" +
                "]";
        DubboGrayApolloConfig d = new DubboGrayApolloConfig();
//        d.updateGrayRulesCache(dubboGray);

//        GrayRule grayRule = GrayRulesCache.getGrayRuleByServerAndPort("10.200.16.138", 28640);
//        RuleInfo ruleInfo = GrayRulesCache.getRuleInfoByGrayRule(grayRule, "10.200.16.138");
//        System.out.println(ruleInfo);
    }


}
