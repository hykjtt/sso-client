package org.hr.sso.client.common;

import org.hr.sso.client.customenums.ErrorEnums;
import org.hr.sso.client.customenums.ErrorInterface;

/**
 * @author huangr
 * @version $Id: StResult.java, v0.1 2018/10/24 10:11 huangr Exp $$
 */
public class StResult<T> {

    public static final StResult SUCCESS;

    public static final StResult FAIL;

    static {
        SUCCESS = new StResult(Boolean.TRUE.booleanValue(), ErrorEnums.SUCCESS);
        FAIL = new StResult(Boolean.FALSE.booleanValue(), ErrorEnums.UNKOWN_ERROR);
    }

    private boolean success;
    private int statusCode;
    private String message;
    private T data;

    public StResult() {

    }

    public StResult(T data) {
        this(Boolean.TRUE.booleanValue(), ErrorEnums.SUCCESS, data);
    }

    public StResult(boolean success, ErrorInterface error) {
        this(success, error.getValue(), error.getDesc(), null);
    }

    public StResult(boolean success, ErrorInterface error, String message) {
        this(success, error.getValue(), message, null);
    }

    public StResult(boolean success, ErrorInterface error, T data) {
        this(success, error.getValue(), error.getDesc(), data);
    }

    public StResult(boolean success, int statusCode, String message, T data) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
