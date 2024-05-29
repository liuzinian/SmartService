package com.liu.forever.service;

import cn.hutool.json.JSONUtil;
import com.liu.forever.pojo.dto.WebMessagesDTO;
import com.liu.forever.util.WebSocketManager;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import javax.annotation.Resource;
import java.io.IOException;

@Service
@Log4j2
public class WebsocketMessageHandler implements WebSocketHandler {


    @Resource
    private IntelligentBrain intelligentBrain;


    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {

    }


    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message) throws Exception {
// 处理接收到的消息
        if (message instanceof TextMessage) {
            TextMessage textMessage = new TextMessage(((TextMessage) message).getPayload());
            String payload = textMessage.getPayload();


            handleMessageInfo(session, payload);

        }
    }

    private void handleMessageInfo(WebSocketSession session, String payload) {
        if (!JSONUtil.isTypeJSONObject(payload)) {
            try {
                session.close();
            } catch (IOException e) {

            }
            return;
        }
        WebMessagesDTO webMessagesDTO = JSONUtil.parseObj(payload).toBean(WebMessagesDTO.class);

        WebSocketManager webSocketManager = new WebSocketManager(session, "66576b51232dac390fc9c8a3");

        try {
            intelligentBrain.handleMsg(webSocketManager, webMessagesDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
