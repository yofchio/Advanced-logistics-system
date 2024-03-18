package com.yang.bi.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 * @author YANG FUCHAO
 */
@Data
public class BaseResponse<T> implements Serializable {

    private String code;

    private T data;

    private String message;

    public BaseResponse(String code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(String code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
