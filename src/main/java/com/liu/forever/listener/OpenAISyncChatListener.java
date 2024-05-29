package com.liu.forever.listener;


import com.liu.forever.pojo.vo.OpenAISyncChatResponse;

public class OpenAISyncChatListener extends OpenAIBaseListener {
	private final StringBuilder stringBuilder = new StringBuilder();
	private final OpenAISyncChatResponse openAISyncChatResponse;


	public OpenAISyncChatListener(OpenAISyncChatResponse openAISyncChatResponse) {
		this.openAISyncChatResponse = openAISyncChatResponse;
	}

	@Override
	public void onMessage(String content, Integer status) {
		super.onMessage(content, status);
		stringBuilder.append(content);
		if (2 == status) {
			openAISyncChatResponse.setContent(stringBuilder.toString());
			openAISyncChatResponse.setOk(true);
		}
	}

	public void setOnResponseListener(Object responseIsNotOk) {

	}
}
