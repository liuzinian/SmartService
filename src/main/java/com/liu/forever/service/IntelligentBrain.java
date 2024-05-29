package com.liu.forever.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.liu.forever.dao.AiChatRecordRepository;
import com.liu.forever.dao.AiRoleDORepository;
import com.liu.forever.dao.EventInfoRepository;
import com.liu.forever.listener.BaseWebSocketEventSourceListener;
import com.liu.forever.model.AiChatRecord;
import com.liu.forever.model.EventInfo;
import com.liu.forever.model.TaiYoAiRoleDO;
import com.liu.forever.pojo.dto.BaseAiMessageDTO;
import com.liu.forever.pojo.dto.BaseGptParameterDTO;
import com.liu.forever.pojo.dto.BaseRole;
import com.liu.forever.pojo.dto.WebMessagesDTO;
import com.liu.forever.util.RedisOperator;
import com.liu.forever.util.WebSocketManager;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class IntelligentBrain {
    @Resource
    private AiChatRecordRepository aiChatRecordRepository;
    @Resource
    private RedisOperator redisOperator;

    @Resource
    private EventInfoRepository eventInfoRepository;

    @Resource
    private AiRoleDORepository aiRoleDORepository;

    @Resource
    private BaseOpenAiService openAiService;

    public void handleMsg(WebSocketManager webSocketManager, WebMessagesDTO webMessagesDTO) {


        List<BaseAiMessageDTO> messages = webMessagesDTO.getMessages();
        messages.remove(messages.size() - 1);

        BaseAiMessageDTO baseAiMessageDTO = messages.get(webMessagesDTO.getMessages().size() - 1);


        List<TaiYoAiRoleDO> yoAiRoleDOS = aiRoleDORepository.findAll();

        Map<String, TaiYoAiRoleDO> aiRoleDOMap = yoAiRoleDOS.stream()
                .collect(Collectors.toConcurrentMap(TaiYoAiRoleDO::getUniqueKey, yoAiRoleDO -> yoAiRoleDO));
        // 判断 获取 当前合适的 角色

        TaiYoAiRoleDO aiRoleDO = null;

        List<EventInfo> eventInfoList = eventInfoRepository.findByTaskState("未处理");

        // 获取用户当前的状态
        String userState = redisOperator.get("userState:" + webSocketManager.getUserId());
        // 如果有未处理的 事件 交给 售后去处理
        if (ObjectUtil.isNotEmpty(eventInfoList)) {
            aiRoleDO = aiRoleDOMap.get("after_sales");
        }
        // 判断 用户 是否 咨询产品
        else if (isUserInquiringProduct(baseAiMessageDTO.getContent())) {
            aiRoleDO = aiRoleDOMap.get("cosmetics_sales");
            redisOperator.set("userState:" + webSocketManager.getUserId(), "产品咨询");


        } else {
            if (StrUtil.isNotBlank(userState) && userState.equals("产品咨询")) {
                aiRoleDO = aiRoleDOMap.get("cosmetics_sales");
            }

            aiRoleDO = aiRoleDOMap.get("Emotional");
        }


        // 处理消息
        BaseGptParameterDTO baseGptParameterDTO = new BaseGptParameterDTO(aiRoleDO);
        AiChatRecord chatRecordDO = builderInfo(baseAiMessageDTO, webSocketManager.getUserId());


        List<BaseAiMessageDTO> privateChatHistoryMessage = getPrivateChatHistoryMessage(baseAiMessageDTO, baseGptParameterDTO.getPrompt(), webSocketManager.getUserId());


        BaseWebSocketEventSourceListener taiYoOpenAIWebSocketEventSourceListener = new BaseWebSocketEventSourceListener(webSocketManager, webMessagesDTO, chatRecordDO,aiChatRecordRepository);
        openAiService.webSocketMessage(privateChatHistoryMessage, baseGptParameterDTO, taiYoOpenAIWebSocketEventSourceListener);

    }

    private AiChatRecord builderInfo(BaseAiMessageDTO baseAiMessageDTO, String userId) {
        return AiChatRecord.builder()
                .aiId(IdUtil.objectId())
                .userQuestion(baseAiMessageDTO.getContent())
                .userId(userId)
                .requestTime(new Date())
                .taskState("进行中")
                .originalMessage(JSONUtil.parseObj(baseAiMessageDTO).toJSONString(4))
                .build();
    }

    private List<BaseAiMessageDTO> getPrivateChatHistoryMessage(BaseAiMessageDTO baseAiMessageDTOTemp, String qualifier, String userId) {

        List<AiChatRecord> taiYoAiChatRecordDOList = aiChatRecordRepository.findByUserId(userId);
        List<BaseAiMessageDTO> baseAiMessageDTOList = new ArrayList<>();

        for (AiChatRecord taiYoAiChatRecordDO : taiYoAiChatRecordDOList) {


            BaseAiMessageDTO baseAiMessageDTO = new BaseAiMessageDTO();

            baseAiMessageDTO.setRole(BaseRole.USER.getValue());
            baseAiMessageDTO.setContent(taiYoAiChatRecordDO.getUserQuestion());


            baseAiMessageDTOList.add(baseAiMessageDTO);


            BaseAiMessageDTO resMessageDTO = new BaseAiMessageDTO();

            resMessageDTO.setRole(BaseRole.ASSISTANT.getValue());
            String aiReply = taiYoAiChatRecordDO.getAiReply();

            resMessageDTO.setContent(aiReply);
            baseAiMessageDTOList.add(resMessageDTO);
        }
        return handleMessagesOrder(baseAiMessageDTOList, qualifier, baseAiMessageDTOTemp);
    }

    public List<BaseAiMessageDTO> handleMessagesOrder(List<BaseAiMessageDTO> messages, String qualifier, BaseAiMessageDTO baseAiMessageDTO) {
        boolean found = false;

        if (messages == null) {
            messages = new ArrayList<>();
        }
        if (ObjectUtil.isAllNotEmpty(messages)) {
            Iterator<BaseAiMessageDTO> iterator = messages.iterator();
            while (iterator.hasNext()) {
                BaseAiMessageDTO message = iterator.next();
                String role = message.getRole();
                String content = message.getContent();
                if (content == null) {
                    iterator.remove();
                }
                if (role.equals(Message.Role.SYSTEM.getName())) {
                    message.setContent(qualifier);
                    iterator.remove();
                    messages.add(0, message);
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            BaseAiMessageDTO newMessage = BaseAiMessageDTO.builder()
                    .content(qualifier)
                    .role(BaseRole.SYSTEM.getValue())
                    .build();
            messages.add(0, newMessage);

        }


        messages.add(baseAiMessageDTO);

        return messages;

    }


    /**
     * 判断用户是否在咨询产品
     *
     * @param userInput 用户输入的字符串
     * @return 如果用户在咨询产品，返回 true；否则返回 false
     */
    public boolean isUserInquiringProduct(String userInput) {
        // 这里可以添加具体的逻辑来判断用户输入是否在咨询产品
        // 例如，可以检查用户输入中是否包含某些关键字
        if (userInput == null || userInput.isEmpty()) {
            return false;
        }

        String[] productKeywords = {"产品", "价格", "功能", "规格", "购买"};
        for (String keyword : productKeywords) {
            if (userInput.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}
