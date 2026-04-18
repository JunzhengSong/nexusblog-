package com.nexusblog.common;

import lombok.Data;

@Data
public class ApiResult<T> {
    private int code;
    private T data;
    private String message;

    public ApiResult() {
    }

    public ApiResult(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResult<T> ok() {
        return new ApiResult<>(200, null, "success");
    }

    public static <T> ApiResult<T> ok(T data) {
        return new ApiResult<>(200, data, "success");
    }

    public static <T> ApiResult<T> ok(T data, String message) {
        return new ApiResult<>(200, data, message);
    }

    public static <T> ApiResult<T> error(IErrorCode errorCode) {
        return new ApiResult<>(errorCode.getCode(), null, errorCode.getMessage());
    }

    public static <T> ApiResult<T> error(int code, String message) {
        return new ApiResult<>(code, null, message);
    }

    public static <T> ApiResult<T> noContent(String message) {
        return new ApiResult<>(204, null, message);
    }
}
