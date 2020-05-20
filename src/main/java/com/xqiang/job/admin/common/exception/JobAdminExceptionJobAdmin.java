package com.xqiang.job.admin.common.exception;


import com.xqiang.job.admin.common.enums.ExceptionEnumInterface;

/**
 * 业务异常
 *
 * @author mengq
 */
public class JobAdminExceptionJobAdmin extends JobAdminBaseException {

    public JobAdminExceptionJobAdmin(String message) {
        super(message);
    }

    public JobAdminExceptionJobAdmin(Integer code, String message) {
        super(code, message);
    }

    public JobAdminExceptionJobAdmin(ExceptionEnumInterface enums, Object... args) {
        super(enums.getCode(), enums.getMsg(), args);
    }

}