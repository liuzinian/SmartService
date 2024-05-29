package com.liu.forever.util;

import cn.hutool.core.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AiUtils {
	public static String extractJsonStringsOne(String text) {
		List<String> strings = extractJsonStrings(text);
		if (ObjectUtil.isNotEmpty(strings)) {
			return strings.get(0);
		}
		return null;
	}

	public static List<String> extractJsonStrings(String text) {
		List<String> jsonList = new ArrayList<>();
		// 定义JSON的正则表达式
		String jsonPattern = "\\{[^{}]*\\}";
		Pattern pattern = Pattern.compile(jsonPattern);
		Matcher matcher = pattern.matcher(text);

		// 查找匹配的JSON字符串
		while (matcher.find()) {
			jsonList.add(matcher.group());
		}

		return jsonList;
	}

}
