package com.liu.forever.listener;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Log4j2
public class OpenAIBaseListener extends EventSourceListener {
	private final StringBuffer error = new StringBuffer();
	private int status = 0;

	public String getError() {
		return error.toString();
	}

	@Override
	public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
		log.info("OpenAI建立sse连接...");
	}

	@Override
	public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data) {

		log.info("返回{}", data);

		if ("[DONE]".equals(data)) {
			log.info("OpenAI返回数据结束了");
			onMessage("", 2);
			return;
		}

		JSONObject jsonObject = JSONUtil.parseObj(data);
		JSONObject delta = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("delta");
		if (delta.containsKey("content")) {
			String content = delta.getStr("content");

			onMessage(content, status);
			if (status == 0) {
				status = 1;
			}

		} else {
			onMessage("", 2);

		}
	}

	@Override
	public  void onClosed(@NotNull EventSource eventSource) {
		log.info("OpenAI关闭sse连接...");
		onMessage("", 2);
	}

	@SneakyThrows
	@Override
	public void onFailure(@NotNull EventSource eventSource, Throwable t, Response response) {
		if (t != null) {
			error.append(t.getMessage());
			t.printStackTrace();
		}

		if (Objects.isNull(response)) {
			if (t != null) {
				log.error("OpenAI  sse连接异常:{}", t.getMessage());
				error.append(t.getMessage());
			}
			eventSource.cancel();
			throw new RuntimeException("代理服务器异常");
		}
		ResponseBody body = response.body();
		if (Objects.nonNull(body)) {
			String string = body.string();
			log.error("OpenAI  body  sse连接异常data：{}，异常：{}", string, t);
			error.append(string);
		} else {
			log.error("OpenAI  Throwable sse连接异常data：{}，异常：{}", response, t);
			if (t != null) {
				error.append(t.getMessage());
			}
		}

		eventSource.cancel();

	}

	/**
	 * 收到回答时会调用此方法
	 *
	 * @param content 回答内容
	 * @param status  会话状态，取值为[0,1,2]；0代表首次结果；1代表中间结果；2代表最后一个结果，当status为2时，webSocket连接会自动关闭
	 */
	public void onMessage(String content, Integer status) {
		// 重写此方法，实现业务逻辑
		log.info("OpenAI返回数据：{} 状态: {}", content, status);
	}

}
