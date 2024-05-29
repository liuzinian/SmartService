package com.liu.forever.pojo.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor

@Builder
public class OpenAISyncChatResponse {
    private String content;
    private boolean isOk;

    public OpenAISyncChatResponse() {
        this.content = "";
        this.isOk = false;
    }
}
