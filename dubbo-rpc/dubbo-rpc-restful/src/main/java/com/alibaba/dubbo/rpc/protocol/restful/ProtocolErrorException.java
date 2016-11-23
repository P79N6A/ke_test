package com.alibaba.dubbo.rpc.protocol.restful;

/**
 * Created by chengtianliang on 2016/11/23.
 */
public class ProtocolErrorException extends Exception {
    public ProtocolErrorException() {
    }

    public ProtocolErrorException(String message) {
        super(message);
    }

    public ProtocolErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolErrorException(Throwable cause) {
        super(cause);
    }

    public ProtocolErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
