/*
 * Copyright 2014 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.module;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class ResultDTO<T extends Serializable> implements Serializable {
    private static final long   serialVersionUID = 3682481175041925854L;

    private static final String DEFAULT_ERR_CODE = "xin.unknown.error";

    private String              errorMsg;

    private String              errorCode;

    private boolean             success;

    private T                   module;

    public ResultDTO(String errorCode, String errorMsg, T obj) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.success = false;
        this.module = obj;
    }

    public ResultDTO() {
        buildSuccessResult();
    }

    public ResultDTO(T obj) {
        this.success = true;
        this.module = obj;
    }

    public static <T extends Serializable> ResultDTO<T> buildSuccessResult() {
        return new ResultDTO<T>(null);
    }

    public static <T extends Serializable> ResultDTO<T> buildSuccessResult(T obj) {
        return new ResultDTO<T>(obj);
    }

    public static <T extends Serializable> ResultDTO<T> buildFailedResult(String errCode, String errMsg, T obj) {
        return new ResultDTO<T>(errCode, errMsg, obj);
    }

    public static <T extends Serializable> ResultDTO<T> buildFailedResult(String errCode, String errMsg) {
        return new ResultDTO<T>(errCode, errMsg, null);
    }

    public static <T extends Serializable> ResultDTO<T> buildFailedResult(String errMsg) {
        return new ResultDTO<T>(DEFAULT_ERR_CODE, errMsg, null);
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getModule() {
        return module;
    }

    public void setModule(T module) {
        this.module = module;
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

}
