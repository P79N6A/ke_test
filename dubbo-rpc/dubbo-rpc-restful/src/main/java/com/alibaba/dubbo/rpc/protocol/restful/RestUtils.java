package com.alibaba.dubbo.rpc.protocol.restful;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ReflectUtils;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengtianliang on 2016/11/23.
 */
public class RestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestUtils.class);

    /**
     * 解码
     *
     * @param request
     * @param response
     * @param path
     * @param descMap
     * @return
     * @throws ProtocolErrorException
     * @throws IOException
     */
    static RpcInvocation decode(HttpServletRequest request, HttpServletResponse response, String path, Map<String, String> descMap) throws ProtocolErrorException, IOException {
        RpcInvocation rpcInvocation;
        String[] ps = path.split("/");
        if (ps.length == 0) {
            throw new ProtocolErrorException("url中没有包含接口类和方法名");
        }
        if (ps.length == 1) {
            return null;//TODO 兼容lianjia老RPC
        }
        String interaceName = ps[0];
        String methodName = ps[1];
        String json;
        if (request.getMethod().equalsIgnoreCase("get")) {
            json = request.getParameter(Constants.REST_METHOD_PARM);
        } else {
            InputStream in = request.getInputStream();
            int length = request.getContentLength();
            byte[] bytes = new byte[length];
            in.read(bytes);
            json = new String(bytes, "utf8");
        }
        LOGGER.debug("收到请求参数：" + json);
        String version = request.getParameter("version");
        if (null == version) {
            version = Constants.DEFAULT_SERVICE_VERSION;
        }
        String methodDesc = descMap.get(serviceKey(interaceName, methodName, version));
        Class<?>[] pts;
        Object[] args;
        try {
            rpcInvocation = new RpcInvocation();
            rpcInvocation.setAttachment(Constants.DUBBO_VERSION_KEY, Version.getVersion());
            rpcInvocation.setAttachment(Constants.PATH_KEY, interaceName);
            rpcInvocation.setAttachment(Constants.VERSION_KEY, version);
            rpcInvocation.setMethodName(methodName);
            pts = ReflectUtils.desc2classArray(methodDesc);
            args = new Object[pts.length];
            JSONObject po = JSON.parseObject(json);
            JSONArray jps = po.getJSONArray("params");
            for (int i = 0; i < args.length; i++) {
                args[i] = jps.getObject(i, pts[i]);
            }
            rpcInvocation.setParameterTypes(pts);
            rpcInvocation.setArguments(args);
            Map<String, Object> atts = po.getJSONObject("attachments");
            if (null != atts) {
                Map<String, String> map = rpcInvocation.getAttachments();
                if (null == map) map = new HashMap<>();
                for (Map.Entry<String, Object> entry : atts.entrySet()) {
                    if (entry.getValue() != null) {
                        map.put(entry.getKey(), entry.getValue().toString());
                    }
                }
                rpcInvocation.setAttachments(map);
            }
        } catch (ClassNotFoundException e) {
            throw new ProtocolErrorException("未找到对应服务，请确认服务是否正确");
        }
        return rpcInvocation;
    }

    static String getRelativePath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && contextPath.length() > 0) {
            uri = uri.substring(contextPath.length());
        }
        return uri;
    }

    static String serviceKey(String interaceName, String method, String version) {
        return new StringBuilder(interaceName).append("#").append(method).append("_").append(version).toString();
    }
}
