package com.alibaba.dubbo.rpc.protocol.restful;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ReflectUtils;
import com.alibaba.dubbo.remoting.exchange.Response;
import com.alibaba.dubbo.remoting.http.HttpBinder;
import com.alibaba.dubbo.remoting.http.HttpHandler;
import com.alibaba.dubbo.remoting.http.HttpServer;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.protocol.AbstractInvoker;
import com.alibaba.dubbo.rpc.protocol.AbstractProxyProtocol;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 只对json
 * Created by chengtianliang on 2016/11/23.
 */
public class HttpRestfulProtocol extends AbstractProxyProtocol {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRestfulProtocol.class);

    private static final int DEFAULT_PORT = 2220;

    private final Map<String, HttpServer> serverMap = new ConcurrentHashMap<String, HttpServer>();

    private final Map<String, String> methodDescMap = new ConcurrentHashMap<>();

    private static final Pattern METHOD_SPLIT = Pattern.compile("\\s*,\\s*");

    private HttpBinder httpBinder;

    public HttpRestfulProtocol() {
        super(IOException.class);
    }

    @Override
    public int getDefaultPort() {
        return DEFAULT_PORT;
    }

    public HttpBinder getHttpBinder() {
        return httpBinder;
    }

    public void setHttpBinder(HttpBinder httpBinder) {
        this.httpBinder = httpBinder;
    }

    private ConcurrentHashMap<String, HttpClient> clients = new ConcurrentHashMap<>();

    private class InternalHandler implements HttpHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            String path = RestUtils.getRelativePath(request);
            if (path.endsWith("favicon.ico")) {
                return;
            }
            try {
                RpcContext.getContext().setRemoteAddress(request.getRemoteHost(), request.getRemotePort());
                RpcInvocation rpcInvocation = RestUtils.decode(request, response, path, methodDescMap);
                int port = request.getLocalPort();
                String interfaceName = rpcInvocation.getAttachments().get(Constants.PATH_KEY);
                String serviceKey = serviceKey(port, interfaceName, rpcInvocation.getAttachments().get(Constants.VERSION_KEY),
                        rpcInvocation.getAttachments().get(Constants.GROUP_KEY));
                Exporter<?> exporter = exporterMap.get(serviceKey);
                if (exporter == null) {
                    response.addHeader(Constants.REST_RESP_STATUS, String.valueOf(Response.SERVICE_NOT_FOUND));
                    return;
                }

                Invoker<?> invoker = exporter.getInvoker();
                Result result = invoker.invoke(rpcInvocation);
                if (result.hasException()) {
                    response.addHeader(Constants.REST_RESP_STATUS, String.valueOf(Response.SERVICE_ERROR));
                    response.addHeader(Constants.REST_RESP_MSG, result.getException().getMessage());
                } else {
                    response.addHeader(Constants.REST_RESP_STATUS, String.valueOf(Response.OK));
                    Map<String, String> atts = result.getAttachments();
                    if (null != atts) {
                        for (Map.Entry<String, String> entry : atts.entrySet()) {
                            response.addHeader(entry.getKey(), entry.getValue());
                        }
                    }
                    Object value = result.getValue();
                    response.setContentType("application/json;charset=UTF-8;pageEncoding=UTF-8");
                    PrintWriter out = response.getWriter();
                    response.setCharacterEncoding("UTF-8");
                    if (value != null) {
                        boolean isJavaClient = isJavaClient(rpcInvocation);
                        if (isJavaClient) {
                            out.print(
                                    JSON.toJSONString(value,
                                            SerializerFeature.WriteClassName,
                                            SerializerFeature.DisableCircularReferenceDetect));
                        } else {
                            out.print(
                                    JSON.toJSONString(value,
                                            SerializerFeature.DisableCircularReferenceDetect));
                        }
                    }
                }
            } catch (NoServiceFoundException e) {
                response.addHeader(Constants.REST_RESP_STATUS, String.valueOf(Response.SERVICE_NOT_FOUND));
                response.addHeader(Constants.REST_RESP_MSG, e.getMessage() == null ? "" : e.getMessage());
            } catch (ProtocolErrorException e) {
                response.addHeader(Constants.REST_RESP_STATUS, String.valueOf(Response.BAD_REQUEST));
                response.addHeader(Constants.REST_RESP_MSG, e.getMessage() == null ? "" : e.getMessage());
            } catch (Throwable e) {
                LOGGER.info(e);
                response.addHeader(Constants.REST_RESP_STATUS, String.valueOf(Response.SERVER_ERROR));
                response.addHeader(Constants.REST_RESP_MSG, e.getMessage() == null ? "" : e.getMessage());
            }
        }
    }

    private boolean isJavaClient(RpcInvocation rpcInvocation) {
        boolean isJavaClient = false;
        String isJavaClientStr = rpcInvocation.getAttachment(Constants.REST_HTTP_JAVA_CLIENT);
        if (isJavaClientStr != null) {
            try {
                isJavaClient = Boolean.parseBoolean(isJavaClientStr);
            } catch (Exception e) {
                isJavaClient = false;
            }
        }
        return isJavaClient;
    }


    @Override
    protected <T> Runnable doExport(T impl, Class<T> type, URL url) throws RpcException {
        String addr = url.getIp() + ":" + url.getPort();
        HttpServer server = serverMap.get(addr);
        if (server == null) {
            server = httpBinder.bind(url, new InternalHandler());
            serverMap.put(addr, server);
        }
        String interfaceName = url.getParameter(Constants.INTERFACE_KEY);
        String methodNames = url.getParameter(Constants.METHODS_KEY);
        String version = url.getParameter(Constants.VERSION_KEY, Constants.DEFAULT_SERVICE_VERSION);
        if (null == interfaceName || methodNames == null) {
            throw new RpcException("暴露RestFul必须有interfaceName和methodName");
        }
        final List<String> keys = new ArrayList<>();
        String[] methodNameAttr = METHOD_SPLIT.split(methodNames);
        Method[] methods = type.getMethods();
        for (String methodName : methodNameAttr) {
            final String key = RestUtils.serviceKey(interfaceName, methodName, version);
            keys.add(key);
            Method method = findMethod(methods, methodName);
            String desc = ReflectUtils.getDesc(method.getParameterTypes());
            methodDescMap.put(key, desc);
        }


        return new Runnable() {
            @Override
            public void run() {
                for (String key : keys) {
                    methodDescMap.remove(key);
                }
            }
        };
    }

    @Override
    protected <T> T doRefer(Class<T> type, URL url) throws RpcException {
        return null;
    }

    @Override
    public <T> Invoker<T> refer(final Class<T> type, URL url) throws RpcException {
        Invoker<T> invoker = new AbstractInvoker<T>(type, url) {
            @Override
            protected Result doInvoke(Invocation invocation) throws Throwable {
                if (!(invocation instanceof RpcInvocation)) return super.invoke(invocation);
                RpcInvocation rInv = (RpcInvocation) invocation;
                URL url = getUrl();
                int connectionTimeout = url.getMethodParameter(rInv.getMethodName(),
                        Constants.CONNECT_TIMEOUT_KEY, Constants.DEFAULT_CONNECT_TIMEOUT);
                int readTimeout = url.getMethodParameter(rInv.getMethodName(),
                        Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
                rInv.setAttachmentIfAbsent(Constants.CONNECT_TIMEOUT_KEY, String.valueOf(connectionTimeout));
                rInv.setAttachmentIfAbsent(Constants.TIMEOUT_KEY, String.valueOf(readTimeout));
                try {
                    return RestUtils.invoke(getClient(invocation, getUrl()), rInv, getUrl());
                } catch (Exception e) {
                    throw getRpcException(type, getUrl(), invocation, e);
                }
            }
        };
        invokers.add(invoker);
        return invoker;
    }

    private Method findMethod(Method[] methods, String methodName) {
        if (null == methods || methodName == null) return null;
        Method method = null;
        int methodNum = 0;
        for (Method m : methods) {
            if (!Modifier.isPublic(m.getModifiers())) continue;
            if (m.getName().equals(methodName)) {
                method = m;
                methodNum++;
            }
        }
        if (methodNum > 1) {
            throw new RpcException("RestFul不支持服务方法重载");
        }
        return method;
    }

    private HttpClient getClient(Invocation inv, URL url) {
        String id = url.toIdentityString();
        int connectionTimeout = url.getMethodParameter(inv.getMethodName(), Constants.CONNECT_TIMEOUT_KEY, Constants.DEFAULT_CONNECT_TIMEOUT);
        int readTimeout = url.getMethodParameter(inv.getMethodName(), Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
        id = id + "&connectionTimeout=" + connectionTimeout + "&readTimeout=" + readTimeout;
        HttpClient client = clients.get(id);
        if (client == null) {
            client = createClient(url, connectionTimeout, readTimeout);
            clients.put(id, client);
        }
        return client;
    }

    private HttpClient createClient(URL url, int connectionTimeout, int readTimeout) {
        HttpClient client = new HttpClient(connectionTimeout, readTimeout);
        return client;
    }

//    public static void main(String[] args) {
//        Parameter parameter = new Parameter();
//        parameter.getParams().add("helloWorld");
//        System.out.println(JSON.toJSONString(parameter));
//    }

}
