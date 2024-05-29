package com.liu.forever.core.ex;

/**
 * 基反应
 *
 * @author Admin
 * @date 2023/07/28
 */
public interface BaseResponse {
    String getMessage();

    int getHttpCode();

    int getCode();
}
