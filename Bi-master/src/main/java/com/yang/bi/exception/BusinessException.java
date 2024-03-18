package com.yang.bi.exception;

import com.yang.bi.common.ErrorCode;

/**
 * 自定义异常类
 *
 * @author YANG FUCHAO
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public String getCode() {
        return code;
    }
}
