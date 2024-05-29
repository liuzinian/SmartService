package com.liu.forever.pojo.enums;

import lombok.Getter;

/**
 * 发送消息类型
 *
 * @author Admin
 * @date 2023/07/19
 */
@Getter
public enum SendMsgEnum {
    DING_DING("钉钉");


    private final String value;

    SendMsgEnum(String value) {
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
