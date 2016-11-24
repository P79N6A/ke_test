package com.alibaba.dubbo.common.logger.jcl;

import java.io.Serializable;

import org.apache.commons.logging.Log;

import com.alibaba.dubbo.common.logger.Logger;

/**
 * 适配CommonsLogging，依赖于commons-logging.jar
 * <br/>
 * 有关CommonsLogging详细信息请参阅：<a target="_blank" href="http://www.apache.org/">http://www.apache.org/</a>
 *
 * @author liangfei0201@163.com
 *
 */
@Deprecated
public class JclLogger implements Logger, Serializable {

	private static final long serialVersionUID = 1L;

	private final Log logger;

	public JclLogger(Log logger) {
		this.logger = logger;
	}

    public void trace(String msg) {
        logger.trace(msg);
    }

	@Override
	public void trace(String msg, Object o) {

	}

	@Override
	public void trace(String s, Object... objects) {

	}

	public void trace(Throwable e) {
        logger.trace(e);
    }

    public void trace(String msg, Throwable e) {
        logger.trace(msg, e);
    }

	public void debug(String msg) {
		logger.debug(msg);
	}

	@Override
	public void debug(String msg, Object o) {

	}

	@Override
	public void debug(String msg, Object... objects) {

	}

	public void debug(Throwable e) {
        logger.debug(e);
    }

	public void debug(String msg, Throwable e) {
		logger.debug(msg, e);
	}

	public void info(String msg) {
		logger.info(msg);
	}

	@Override
	public void info(String msg, Object o) {

	}

	@Override
	public void info(String msg, Object... objects) {

	}

	public void info(Throwable e) {
        logger.info(e);
    }

	public void info(String msg, Throwable e) {
		logger.info(msg, e);
	}

	public void warn(String msg) {
		logger.warn(msg);
	}

	@Override
	public void warn(String msg, Object o) {

	}

	@Override
	public void warn(String msg, Object... objects) {

	}

	public void warn(Throwable e) {
        logger.warn(e);
    }

	public void warn(String msg, Throwable e) {
		logger.warn(msg, e);
	}

	public void error(String msg) {
		logger.error(msg);
	}

	@Override
	public void error(String msg, Object o) {

	}

	@Override
	public void error(String msg, Object... objects) {

	}

	public void error(Throwable e) {
        logger.error(e);
    }

	public void error(String msg, Throwable e) {
		logger.error(msg, e);
	}

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

}
