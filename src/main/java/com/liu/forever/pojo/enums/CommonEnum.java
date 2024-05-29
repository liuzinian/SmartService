package com.liu.forever.pojo.enums;

import lombok.Getter;

/**
 * 通用枚举类
 */
@Getter
public enum CommonEnum {


    /**
     * PC
     */
    PC("PC"),

    /**
     * H5
     */
    H5("H5");

    private final String value;

    CommonEnum(String value) {
        this.value = value;
    }

    /**
     * 获取枚举值
     *
     * @return 枚举值
     */
    public String getValue() {
        return this.value;
    }
}