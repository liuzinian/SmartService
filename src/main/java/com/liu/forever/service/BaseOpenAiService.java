package com.liu.forever.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.liu.forever.listener.BaseWebSocketEventSourceListener;
import com.liu.forever.listener.OpenAISyncChatListener;
import com.liu.forever.pojo.dto.BaseAiMessageDTO;
import com.liu.forever.pojo.dto.BaseGptParameterDTO;
import com.liu.forever.pojo.dto.BaseRole;
import com.liu.forever.pojo.dto.OpenAiConfigDTO;
import com.liu.forever.pojo.vo.OpenAISyncChatResponse;
import com.liu.forever.util.AiUtils;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.*;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 最基础的 openai服务
 *
 * @author 刘子年
 * @date 2024/03/02
 */
@Data
@Log4j2
@Service
public class BaseOpenAiService {

    private String gptJSONMode = "gpt-4o-2024-05-13";
    private OpenAiConfigDTO openAiConfigDTO;


    public BaseOpenAiService() {

        openAiConfigDTO = OpenAiConfigDTO.builder()
                .apiHost("https://api.ephone.ai/")
                .apiKey("sk-ySYbAQmK0nlpIajx611cA53c78Bc4266Ae4f8aC91b7f7839")
                .build();
    }

    public JSONObject getJSONRequest(List<BaseAiMessageDTO> messages, String gptJSONMode) {

        OpenAiStreamClient openAiClient = getOpenAiStreamClient();
//		OpenAiClient openAiClient = getOpenAiClient();
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .messages(convert(messages))
                .responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.TEXT.getName()).build())
//				.model(gptJSONMode)
                .build();

        String resMsgContent = streamChatCompletion(openAiClient, chatCompletion);
        if (StrUtil.isNotEmpty(resMsgContent)) {
            String stringsOne = AiUtils.extractJsonStringsOne(resMsgContent);
            if (StrUtil.isNotEmpty(stringsOne) && JSONUtil.isTypeJSON(stringsOne)) {
                return JSONUtil.parseObj(stringsOne);
            }
        }

        return new JSONObject();


    }

    public JSONObject getJSONRequest(List<BaseAiMessageDTO> messages) {
        return getJSONRequest(messages, gptJSONMode);

    }


    public String streamChatCompletion(OpenAiStreamClient openAiClient, ChatCompletion chatCompletion) {
        OpenAISyncChatResponse openAISyncChatResponse = new OpenAISyncChatResponse();

        OpenAISyncChatListener openAISyncChatListener = new OpenAISyncChatListener(openAISyncChatResponse);

        openAiClient.streamChatCompletion(chatCompletion, openAISyncChatListener);


        while (!openAISyncChatResponse.isOk()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return openAISyncChatResponse.getContent();
    }

    public OkHttpClient getOkHttpClient() {
        //日志拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //！！！！千万别再生产或者测试环境打开BODY级别日志！！！！
        //！！！生产或者测试环境建议设置为这三种级别：NONE,BASIC,HEADERS,！！！
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)//自定义日志
                .connectTimeout(15000, TimeUnit.SECONDS)//自定义超时时间
                .writeTimeout(15000, TimeUnit.SECONDS)//自定义超时时间
                .readTimeout(15000, TimeUnit.SECONDS)//自定义超时时间
                .build();
    }

    public OpenAiStreamClient getOpenAiStreamClient() {
        return getOpenAiStreamClient(openAiConfigDTO);
    }

    public OpenAiStreamClient getOpenAiStreamClient(OpenAiConfigDTO openAiConfig) {


        return OpenAiStreamClient.builder()
                .apiKey(Collections.singletonList(openAiConfig.getApiKey()))
                .okHttpClient(getOkHttpClient())
                .apiHost(openAiConfig.getApiHost())

                .build();
    }


    public OpenAiClient getOpenAiClient(OpenAiConfigDTO openAiConfig) {

        return OpenAiClient.builder()
                .apiKey(List.of(openAiConfig.getApiKey()))
                .okHttpClient(getOkHttpClient())
                .apiHost(openAiConfig.getApiHost())
                .build();
    }

    public OpenAiClient getOpenAiClient() {

        return OpenAiClient.builder()
                .apiKey(List.of(openAiConfigDTO.getApiKey()))
                .okHttpClient(getOkHttpClient())
                .apiHost(openAiConfigDTO.getApiHost())
                .build();
    }

    public List<Message> convert(List<BaseAiMessageDTO> messages) {
        List<Message> messagesList = new ArrayList<>();

        for (BaseAiMessageDTO message : messages) {
            Message build = Message.builder()
                    .role(message.getRole())
                    .content(message.getContent()).build();
            messagesList.add(build);
        }
        return messagesList;
    }

    public List<MessagePicture> convertPicture(List<BaseAiMessageDTO> messages) {
        List<MessagePicture> messagesList = new ArrayList<>();

        for (BaseAiMessageDTO message : messages) {
            List<Content> contentList = new ArrayList<>();
            List<String> url = message.getUrl();
            if (url != null) {
                for (String string : url) {
                    ImageUrl imageUrl = ImageUrl.builder().url(string).build();
                    Content imageContent = Content.builder()
                            .imageUrl(imageUrl)
                            .type(Content.Type.IMAGE_URL.getName()).build();
                    contentList.add(imageContent);
                }
            }
            Content textContent = Content.builder().text(message.getContent())
                    .type(Content.Type.TEXT.getName())
                    .build();
            contentList.add(textContent);

            MessagePicture messageTemp = MessagePicture.builder()
                    .role(message.getRole())
                    .content(contentList)
                    .build();


            messagesList.add(messageTemp);
        }
        return messagesList;
    }


    private ChatCompletionWithPicture getChatCompletionWithPicture(List<BaseAiMessageDTO> messages, BaseGptParameterDTO baseGptParameterDTO) {


        ChatCompletionWithPicture getChatCompletionWithPicture = ChatCompletionWithPicture
                .builder()
                .messages(convertPicture(messages))
                .model(ChatCompletion.Model.GPT_4_VISION_PREVIEW.getName())
                .build();
        BeanUtil.copyProperties(baseGptParameterDTO, getChatCompletionWithPicture);
        return getChatCompletionWithPicture;
    }

    private ChatCompletion getChatCompletion(List<BaseAiMessageDTO> messages, BaseGptParameterDTO baseGptParameterDTO) {


        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(convert(messages))
                .model(baseGptParameterDTO.getModel())
                .build();
        BeanUtil.copyProperties(baseGptParameterDTO, chatCompletion);
        return chatCompletion;
    }

    public BaseAiMessageDTO syncChat(List<BaseAiMessageDTO> messages, BaseGptParameterDTO baseGptParameterDTO) {
        OpenAiStreamClient client = getOpenAiStreamClient();
        ChatCompletion chatCompletion = getChatCompletion(messages, baseGptParameterDTO);


        String content = streamChatCompletion(client, chatCompletion);


        return BaseAiMessageDTO.builder()
                .content(content)
                .role(BaseRole.ASSISTANT.getValue())
                .build();


    }

    public void webSocketMessage(List<BaseAiMessageDTO> messages, BaseGptParameterDTO baseGptParameterDTO, BaseWebSocketEventSourceListener openAIBaseWebSocketEventSourceListener) {


        OpenAiStreamClient openAiStreamClient = getOpenAiStreamClient();

        ChatCompletion chatCompletion = getChatCompletion(messages, baseGptParameterDTO);
        openAiStreamClient.streamChatCompletion(chatCompletion, openAIBaseWebSocketEventSourceListener);


    }


    public void webSocketMessageByMode(List<BaseAiMessageDTO> messages, BaseGptParameterDTO baseGptParameterDTO, BaseWebSocketEventSourceListener openAIBaseWebSocketEventSourceListener, String mode) {


        OpenAiStreamClient openAiStreamClient = getOpenAiStreamClient();
        ChatCompletion chatCompletion = getChatCompletion(messages, baseGptParameterDTO);


        //获取请求的tokens数量
//		long tokens = chatCompletion.tokens();
        //这种方式也可以
//        long tokens = TikTokensUtil.tokens(chatCompletion.getModel(),messages);

//		log.info("本地计算的请求的 chatCompletion  tokens数{}", tokens);


        openAiStreamClient.streamChatCompletion(chatCompletion, openAIBaseWebSocketEventSourceListener);


    }

}
