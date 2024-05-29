package com.liu.forever.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseAiPromptDTO {


	/**
	 * frequencyPenalty
	 */
	private String frequencyPenalty;

	/**
	 * 包含模板
	 */

	private Boolean isTemplateStr;

	/**
	 * 是否可用
	 */
	private Boolean isUsable;

	/**
	 * presencePenalty
	 */

	private String presencePenalty;


	/**
	 * 提示词内容
	 */

	private String prompt;

	/**
	 * temperature
	 */

	private String temperature;

	/**
	 * topP
	 */

	private String topP;

	/**
	 * 唯一标识KEY
	 */

	private String uniqueKey;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 最大Token
	 */

	private String maxTokens;

	/**
	 * GPT-S标识符
	 */

	private String gptStoreKey;

	/**
	 * 介绍功能用途
	 */

	private String introduceInfo;

	/**
	 * 是否是GptStore
	 */

	private Boolean isGptStore;

	/**
	 * 是不是角色
	 */

	private Boolean isRole;

	/**
	 * 模型
	 */
	private String modelType;

}
