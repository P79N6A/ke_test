package com.alibaba.dubbo.rpc.protocol.restful;

/**
 * Created by chengtianliang on 2016/11/24.
 */
public class NoServiceFoundException extends ProtocolErrorException {
    public NoServiceFoundException() {
        super();
    }

    public NoServiceFoundException(String message) {
        super(message);
    }

    public NoServiceFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoServiceFoundException(Throwable cause) {
        super(cause);
    }

    public NoServiceFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
