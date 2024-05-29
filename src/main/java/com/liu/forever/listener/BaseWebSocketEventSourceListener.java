package com.liu.forever.listener;


import cn.hutool.core.text.StrBuilder;
import com.liu.forever.dao.AiChatRecordRepository;
import com.liu.forever.model.AiChatRecord;
import com.liu.forever.pojo.dto.WebMessagesDTO;
import com.liu.forever.util.WebSocketManager;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;


@Log4j2
public class BaseWebSocketEventSourceListener extends OpenAIBaseListener {
    private final AtomicInteger n = new AtomicInteger(0);
    private final WebSocketManager webSocketManager;
    private final WebMessagesDTO webMessagesDTO;
    private final AiChatRecordRepository aiChatRecordRepository;
    private final AiChatRecord chatRecordDO;
    private final StrBuilder strBuilder = new StrBuilder();

    public BaseWebSocketEventSourceListener(WebSocketManager webSocketManager, WebMessagesDTO webMessagesDTO, AiChatRecord chatRecordDO, AiChatRecordRepository aiChatRecordRepository) {
        this.webSocketManager = webSocketManager;
        this.webMessagesDTO = webMessagesDTO;
        this.aiChatRecordRepository = aiChatRecordRepository;
        this.chatRecordDO = chatRecordDO;
    }

    @Override
    public void onMessage(String content, Integer status) {


        if (status != 2) {
            strBuilder.append(content);
        } else {


            // 原子性地检查n的值，并尝试将其从0设置为1
            if (n.compareAndSet(0, 1)) {
                chatRecordDO.setAiReply(strBuilder.toString());
                chatRecordDO.setTaskState("已完成");
                chatRecordDO.setResponseCompletionTime(new Date());
                aiChatRecordRepository.saveAndFlush(chatRecordDO);
            }

        }
        webSocketManager.sendMsg(webMessagesDTO, content);
    }
}
