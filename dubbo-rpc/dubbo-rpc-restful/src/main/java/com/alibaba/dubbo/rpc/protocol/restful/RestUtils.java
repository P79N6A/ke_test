package com.alibaba.dubbo.rpc.protocol.restful;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ReflectUtils;
import com.alibaba.dubbo.remoting.exchange.Response;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.support.RpcUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Enumeration;
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
        if (path.startsWith("/")) path = path.substring(1);
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
        String version = request.getHeader("version");
        rpcInvocation = new RpcInvocation();
        if (null == version) {
            version = Constants.DEFAULT_SERVICE_VERSION;
        } else {
            rpcInvocation.setAttachment(Constants.VERSION_KEY, version);
        }
        String methodDesc = descMap.get(serviceKey(interaceName, methodName, version));
        if (methodDesc == null) {
            throw new NoServiceFoundException("未找到服务");
        }
        Class<?>[] pts;
        Object[] args;
        try {

            rpcInvocation.setAttachment(Constants.DUBBO_VERSION_KEY, Version.getVersion());
            rpcInvocation.setAttachment(Constants.PATH_KEY, interaceName);
            rpcInvocation.setMethodName(methodName);
            pts = ReflectUtils.desc2classArray(methodDesc);
            args = new Object[pts.length];
            if (StringUtils.isNotBlank(json)) {
                if (json.startsWith("[") && json.endsWith("]")) {
                    JSONArray params = JSON.parseArray(json);
                    if (params.size() != args.length) {
                        LOGGER.info("[服务参数不符合，请求参数个数:{},注册服务个数：{}]", params.size(), args.length);
                        throw new NoServiceFoundException("未找到服务");
                    }
                    for (int i = 0; i < args.length; i++) {
                        args[i] = params.getObject(i, pts[i]);
                    }
                } else {
                    if (args.length == 1) {
                        args[0] = JSON.parseObject(json, pts[0]);
                    } else {
                        throw new ProtocolErrorException("未找到对应服务，请确认服务是否正确");
                    }
                }
                rpcInvocation.setParameterTypes(pts);
                rpcInvocation.setArguments(args);
            }

            Enumeration<String> hns = request.getHeaderNames();
            Map<String, String> map = rpcInvocation.getAttachments();
            if (null == map) map = new HashMap<>();
            while (hns.hasMoreElements()) {
                String hn = hns.nextElement();
                if (hn.equals("version")) continue;
                //过滤标准http头，但保留refer和UA、contentType
                if (hn.equalsIgnoreCase("Accept") ||
                        hn.equalsIgnoreCase("Accept-Charset") ||
                        hn.equalsIgnoreCase("Accept-Encoding") ||
                        hn.equalsIgnoreCase("Accept-Language") ||
                        hn.equalsIgnoreCase("Authorization") ||
                        hn.equalsIgnoreCase("Connection") ||
                        hn.equalsIgnoreCase("Content-Length") ||
                        hn.equalsIgnoreCase("Cookie") ||
                        hn.equalsIgnoreCase("From") ||
                        hn.equalsIgnoreCase("Host") ||
                        hn.equalsIgnoreCase("If-Modified-Since")) {
                    continue;
                }
                String value = request.getHeader(hn);
                map.put(hn, value);
            }
            rpcInvocation.setAttachments(map);
        } catch (ClassNotFoundException e) {
            throw new ProtocolErrorException("未找到对应服务，请确认服务是否正确");
        }
        return rpcInvocation;
    }

    static Result invoke(HttpClient client, RpcInvocation inv, URL url) throws IOException {
        long startTime = System.currentTimeMillis();
        try {
            Map<String, String> head = new HashMap<>(inv.getAttachments());
            String httpUrl = buildHttpUrl(inv, url);
            String content = buildRequestJson(inv);
            Map<String, String> respHeader = new HashMap<>();
            String data = client.invokeWithJson(httpUrl, head, content, respHeader);
            String status = respHeader.get(Constants.REST_RESP_STATUS);
            String msg = respHeader.get(Constants.REST_RESP_MSG);
            byte state = Byte.parseByte(status);
            if (state != Response.OK) {
                return new RpcResult(new RpcException(state, msg));
            }
            Object o = null;
            Type[] returnTypes = RpcUtils.getReturnTypes(inv);
            if (returnTypes != null && returnTypes.length > 0) {
                o = JSON.parseObject(data, returnTypes[0]);
            }

            return new RpcResult(o);
        } finally {
            LOGGER.info("[Call {} cost {} ms]", url.toIdentityString(), System.currentTimeMillis() - startTime);
        }
    }

    private static String buildRequestJson(RpcInvocation inv) {
        String content;
        Object[] args = inv.getArguments();
        if (args != null) {
            if (args.length == 1) {
                content = JSON.toJSONString(inv.getArguments()[0], SerializerFeature.WriteClassName,
                        SerializerFeature.DisableCircularReferenceDetect);
            } else if (args.length > 1) {
                content = JSON.toJSONString(inv.getArguments(), SerializerFeature.WriteClassName,
                        SerializerFeature.DisableCircularReferenceDetect);
            } else {
                content = "";
            }
        } else {
            content = "";
        }
        return content;
    }

    private static String buildHttpUrl(RpcInvocation inv, URL url) {
        StringBuilder buf = new StringBuilder();
        buf.append("http://");
        String username = url.getUsername();
        String password = url.getPassword();
        if (username != null && username.length() > 0) {
            buf.append(username);
            if (password != null && password.length() > 0) {
                buf.append(":");
                buf.append(password);
            }
            buf.append("@");
        }
        String host = url.getHost();
        if (host != null && host.length() > 0) {
            buf.append(host);
            int port = url.getPort();
            if (port > 0) {
                buf.append(":");
                buf.append(port);
            }
        }
        String path = inv.getInvoker().getInterface().getCanonicalName();
        buf.append("/").append(path).append("/").append(inv.getMethodName());
        return buf.toString();
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
