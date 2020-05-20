package com.xqiang.job.admin.common.exception;

import java.text.MessageFormat;

/**
 * 基础异常
 *
 * @author mengq
 */
public class JobAdminBaseException extends RuntimeException {
    protected String msg;
    protected Integer code;

    protected JobAdminBaseException(String message) {
        super(message);
    }

    protected JobAdminBaseException(Integer code, String msgFormat, Object... args) {
        super(MessageFormat.format(msgFormat, args));
        this.code = code;
        this.msg = MessageFormat.format(msgFormat, args);
    }

    public String getMsg() {
        return this.msg;
    }

    public Integer getCode() {
        return this.code;
    }
}