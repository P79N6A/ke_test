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
package com.alibaba.dubbo.common.logger.support;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.NetUtils;

public class FailsafeLogger implements Logger {

    private Logger logger;

    public FailsafeLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private String appendContextMessage(String msg) {
        StringBuilder sb = new StringBuilder("[DUBBO]");
        sb.append("[").append(Version.getVersion()).append("]");
        sb.append("[").append(NetUtils.getLogHost()).append("]");
        String remoteAddres = (String) LoggerFactory.getValue(Constants.LOGGER_REMOTEADDRESS);
        sb.append("[remoteAddres:");
        if (remoteAddres != null) {
            sb.append(remoteAddres);
        }
        sb.append("]");
        String interfaceName = (String) LoggerFactory.getValue(Constants.LOGGER_INTERFACE);
        sb.append("[interfaceName:");
        if (interfaceName != null) {
            sb.append(interfaceName);
        }
        sb.append("]");
        String methodName = (String) LoggerFactory.getValue(Constants.LOGGER_METHODNAME);
        sb.append("[methodName:");
        if (methodName != null) {
            sb.append(methodName);
        }
        sb.append("]");
        Object reqId = LoggerFactory.getValue(Constants.LOGGER_REQ_ID);
        sb.append("[reqId:");
        if (reqId != null) {
            sb.append(reqId);
        }
        sb.append("]");
        sb.append(msg);
        return sb.toString();
    }

    public void trace(String msg, Throwable e) {
        try {
            logger.trace(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    public void trace(Throwable e) {
        try {
            logger.trace(e);
        } catch (Throwable t) {
        }
    }

    public void trace(String msg) {
        try {
            logger.trace(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void trace(String msg, Object o) {
        try {
            logger.trace(appendContextMessage(msg), o);
        } catch (Throwable t) {
        }
    }

    @Override
    public void trace(String s, Object... objects) {
        try {
            logger.trace(appendContextMessage(s), objects);
        } catch (Throwable t) {
        }
    }

    public void debug(String msg, Throwable e) {
        try {
            logger.debug(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    public void debug(Throwable e) {
        try {
            logger.debug(e);
        } catch (Throwable t) {
        }
    }

    public void debug(String msg) {
        try {
            logger.debug(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void debug(String msg, Object o) {
        try {
            logger.debug(appendContextMessage(msg), o);
        } catch (Throwable t) {
        }
    }

    @Override
    public void debug(String msg, Object... objects) {
        try {
            logger.debug(appendContextMessage(msg), objects);
        } catch (Throwable t) {
        }
    }

    public void info(String msg, Throwable e) {
        try {
            logger.info(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    public void info(String msg) {
        try {
            logger.info(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void info(String msg, Object o) {
        try {
            logger.info(appendContextMessage(msg), o);
        } catch (Throwable t) {
        }
    }

    @Override
    public void info(String msg, Object... objects) {
        try {
            logger.info(appendContextMessage(msg), objects);
        } catch (Throwable t) {
        }
    }

    public void warn(String msg, Throwable e) {
        try {
            logger.warn(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    public void warn(String msg) {
        try {
            logger.warn(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void warn(String msg, Object o) {
        try {
            logger.warn(appendContextMessage(msg), o);
        } catch (Throwable t) {
        }
    }

    @Override
    public void warn(String msg, Object... objects) {
        try {
            logger.warn(appendContextMessage(msg), objects);
        } catch (Throwable t) {
        }
    }

    public void error(String msg, Throwable e) {
        try {
            logger.error(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    public void error(String msg) {
        try {
            logger.error(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void error(String msg, Object o) {
        try {
            logger.error(appendContextMessage(msg), o);
        } catch (Throwable t) {
        }
    }

    @Override
    public void error(String msg, Object... objects) {
        try {
            logger.error(appendContextMessage(msg), objects);
        } catch (Throwable t) {
        }
    }

    public void error(Throwable e) {
        try {
            logger.error(e);
        } catch (Throwable t) {
        }
    }

    public void info(Throwable e) {
        try {
            logger.info(e);
        } catch (Throwable t) {
        }
    }

    public void warn(Throwable e) {
        try {
            logger.warn(e);
        } catch (Throwable t) {
        }
    }

    public boolean isTraceEnabled() {
        try {
            return logger.isTraceEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    public boolean isDebugEnabled() {
        try {
            return logger.isDebugEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    public boolean isInfoEnabled() {
        try {
            return logger.isInfoEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    public boolean isWarnEnabled() {
        try {
            return logger.isWarnEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    public boolean isErrorEnabled() {
        try {
            return logger.isErrorEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

}