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
package com.alibaba.dubbo.common.logger.log4j;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.support.FailsafeLogger;
import org.apache.log4j.Level;

@Deprecated
public class Log4jLogger implements Logger {

    private static final String FQCN = FailsafeLogger.class.getName();

    private final org.apache.log4j.Logger logger;

    public Log4jLogger(org.apache.log4j.Logger logger) {
        this.logger = logger;
    }

    public void trace(String msg) {
        logger.log(FQCN, Level.TRACE, msg, null);
    }

    @Override
    public void trace(String msg, Object o) {

    }

    @Override
    public void trace(String s, Object... objects) {

    }

    public void trace(Throwable e) {
        logger.log(FQCN, Level.TRACE, e == null ? null : e.getMessage(), e);
    }

    public void trace(String msg, Throwable e) {
        logger.log(FQCN, Level.TRACE, msg, e);
    }

    public void debug(String msg) {
        logger.log(FQCN, Level.DEBUG, msg, null);
    }

    @Override
    public void debug(String msg, Object o) {

    }

    @Override
    public void debug(String msg, Object... objects) {

    }

    public void debug(Throwable e) {
        logger.log(FQCN, Level.DEBUG, e == null ? null : e.getMessage(), e);
    }

    public void debug(String msg, Throwable e) {
        logger.log(FQCN, Level.DEBUG, msg, e);
    }

    public void info(String msg) {
        logger.log(FQCN, Level.INFO, msg, null);
    }

    @Override
    public void info(String msg, Object o) {

    }

    @Override
    public void info(String msg, Object... objects) {

    }

    public void info(Throwable e) {
        logger.log(FQCN, Level.INFO, e == null ? null : e.getMessage(), e);
    }

    public void info(String msg, Throwable e) {
        logger.log(FQCN, Level.INFO, msg, e);
    }

    public void warn(String msg) {
        logger.log(FQCN, Level.WARN, msg, null);
    }

    @Override
    public void warn(String msg, Object o) {

    }

    @Override
    public void warn(String msg, Object... objects) {

    }

    public void warn(Throwable e) {
        logger.log(FQCN, Level.WARN, e == null ? null : e.getMessage(), e);
    }

    public void warn(String msg, Throwable e) {
        logger.log(FQCN, Level.WARN, msg, e);
    }

    public void error(String msg) {
        logger.log(FQCN, Level.ERROR, msg, null);
    }

    @Override
    public void error(String msg, Object o) {

    }

    @Override
    public void error(String msg, Object... objects) {

    }

    public void error(Throwable e) {
        logger.log(FQCN, Level.ERROR, e == null ? null : e.getMessage(), e);
    }

    public void error(String msg, Throwable e) {
        logger.log(FQCN, Level.ERROR, msg, e);
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
        return logger.isEnabledFor(Level.WARN);
    }

    public boolean isErrorEnabled() {
        return logger.isEnabledFor(Level.ERROR);
    }

}