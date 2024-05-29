package com.liu.forever.util;

import cn.hutool.json.JSONUtil;
import com.liu.forever.pojo.dto.WebMessagesDTO;
import com.liu.forever.pojo.vo.WebMessagesVO;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Log4j2
@Getter
public class WebSocketManager {
    private final WebSocketSession webSocketSession;

    private final String userId;

    public WebSocketManager(WebSocketSession webSocketSession, String userId) {
        this.webSocketSession = webSocketSession;
        this.userId = userId;
    }

    public boolean sendMsg(WebMessagesDTO webMessagesDTO, String content) {

        WebMessagesVO build = WebMessagesVO.builder()
                .messageId(webMessagesDTO.getMessageId())
                .content(content)
                .build();
        try {
            webSocketSession.sendMessage(new TextMessage(JSONUtil.parseObj(build).toJSONString(4)));
        } catch (IOException e) {
            return false;
        }

        return true;
    }

}
