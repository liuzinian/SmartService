package com.liu.forever.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseAiMessageDTO {
	/**
	 * 内容
	 */
	private String content;


	/**
	 * 角色
	 */
	private String role;

	/**
	 * 图片地址
	 */
	private List<String> url;
}
