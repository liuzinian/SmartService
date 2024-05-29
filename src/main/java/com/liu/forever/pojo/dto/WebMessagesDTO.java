package com.liu.forever.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class WebMessagesDTO {

    private List<BaseAiMessageDTO> messages;
    private String messageId;

}
