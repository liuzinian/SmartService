package com.liu.forever.core.response;


import com.liu.forever.pojo.enums.Code;
import lombok.Data;

import java.io.Serializable;


/**
 * 结果
 *
 * @author 刘子年
 * @date 2023/07/19
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 8708569195046968619L;
    private Integer code;
    private String msg;
    private T data;

    private Result() {
        this.setCode(Code.SUCCESS.getCode());
        this.setMsg(Code.SUCCESS.getZhDescription());
    }

    public static <T> Result<T> ok() {
        return new Result<>();
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail() {
        Result<T> result = new Result<>();
        result.setCode(Code.FAIL.getCode());
        result.setMsg(Code.FAIL.getZhDescription());
        return result;
    }

    public static <T> Result<T> fail(Code resultEnum) {
        Result<T> result = new Result<>();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getZhDescription());
        return result;
    }

    public static <T> Result<T> fail(String msg) {
        Result<T> result = new Result<>();
        result.setCode(Code.FAIL.getCode());
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
