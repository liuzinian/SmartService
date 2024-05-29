package com.liu.forever.core.ex;


import com.liu.forever.pojo.enums.Code;
import org.springframework.http.HttpStatus;

/**
 * http异常
 *
 * @author Admin
 * @date 2023/07/21
 */
public class HttpException extends RuntimeException implements BaseResponse {

    protected int httpCode;
    protected int code;


    public HttpException() {
        super(Code.INTERNAL_SERVER_ERROR.getDescription());
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
    }


    public HttpException(Code code) {
        super(code.getZhDescription());
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = code.getCode();
    }


    public HttpException(String message) {
        super(message);
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
    }

    public HttpException(int code) {
        super(Code.INTERNAL_SERVER_ERROR.getDescription());
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
        this.code = code;
    }

    public HttpException(int code, int httpCode) {
        super(Code.INTERNAL_SERVER_ERROR.getDescription());
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
        this.httpCode = httpCode;
        this.code = code;
    }

    public HttpException(int code, String message) {
        super(message);
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
        this.code = code;
    }

    public HttpException(int code, String message, int httpCode) {
        super(message);
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
        this.code = code;
        this.httpCode = httpCode;
    }


    @Override
    public int getHttpCode() {
        return this.code;
    }

    @Override
    public int getCode() {
        return this.httpCode;
    }
}