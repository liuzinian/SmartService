package com.liu.forever.pojo.bo;


import com.liu.forever.pojo.annotate.Describe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息发送类
 *
 * @author Admin
 * @date 2023/07/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMsgBO {


    /**
     * 内容
     */
    @Describe("内容")
    private String content;
    /**
     * 当前时间
     */
    @Describe("当前时间")
    private String currentTime;

}
