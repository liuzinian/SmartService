package com.liu.forever.pojo.dto;

import lombok.Getter;


@Getter
public enum BaseRole {

	/**
	 * The human side.
	 */
	USER("user"),
	ASSISTANT("assistant"),
	/**
	 * The model side.
	 */
	BOT("bot"),
	SYSTEM("system"),
	ATTACHMENT("attachment"),
	;

	private final String value;

	BaseRole(String value) {
		this.value = value;
	}

}