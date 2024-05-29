package com.liu.forever.pojo.enums;

import lombok.Getter;

/**
 * 付款枚举
 *
 * @author 刘子年
 * @date 2023/07/19
 */
@Getter
public enum PaymentEnum {
    PAYMENT_SUCCESS("支付成功"),
    IN_PAYMENT_PROCESS("支付中"),
    PAYMENT_CANCELLATION("支付取消"),
    WX("微信"),
    PREFERENTIAL("优惠劵");


    final String value;


    PaymentEnum(String value) {
        this.value = value;
    }
}
