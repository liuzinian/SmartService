package com.liu.forever.pojo.bo;

import cn.hutool.core.bean.BeanUtil;

import com.liu.forever.pojo.annotate.Describe;
import lombok.*;

/**
 * 异常消息 发送 类
 *
 * @author Admin
 * @date 2023/07/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "builderExSendMsgBO")
public class ExSendMsgBO extends SendMsgBO {
    /**
     * 请求方法
     */
    @Describe("请求方法")
    private String method;
    /**
     * 内容类型
     */
    @Describe("内容类型")
    private String contentType;

    /**
     * 请求URL地址
     */
    @Describe("请求URL地址")
    private String requestUrl;
    /**
     * 请求参数
     */
    @Describe("请求参数")
    private String requestParameters;
    /**
     * 请求ip
     */
    @Describe("请求ip")
    private String requestIp;


    public static ExSendMsgBO buildExSendMsgBO(SendMsgBO sendMsgBO) {
        ExSendMsgBO exSendMsgBO = new ExSendMsgBO();
        BeanUtil.copyProperties(sendMsgBO, exSendMsgBO);
        return exSendMsgBO;
    }

    public static ExSendMsgBO buildExSendMsgBO(SendMsgBO sendMsgBO, ExSendMsgBO exSendMsgBO) {
        BeanUtil.copyProperties(sendMsgBO, exSendMsgBO);
        return exSendMsgBO;
    }

}
