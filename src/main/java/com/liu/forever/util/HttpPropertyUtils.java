package com.liu.forever.util;

import java.util.regex.Pattern;

public class HttpPropertyUtils {

    private static final Pattern SCRIPT_PATTERN = Pattern.compile("<script[^>]*?>[\\s\\S]*?<\\/script>");
    private static final Pattern STYLE_PATTERN = Pattern.compile("<style[^>]*?>[\\s\\S]*?<\\/style>");
    private static final Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");
    private static final Pattern SPACE_PATTERN = Pattern.compile("\\s*|\t|\r|\n");

    public static String deleteHtmlTags(String htmlStr) {
        // 过滤script标签
        htmlStr = htmlStr.replaceAll(SCRIPT_PATTERN.pattern(), "");
        // 过滤style标签
        htmlStr = htmlStr.replaceAll(STYLE_PATTERN.pattern(), "");
        // 过滤html标签
        htmlStr = htmlStr.replaceAll(HTML_PATTERN.pattern(), "");
        // 过滤空格等
        htmlStr = htmlStr.replaceAll(SPACE_PATTERN.pattern(), "");
        // 过滤&nbsp;
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        // 过滤&nbsp
        htmlStr = htmlStr.replaceAll("&nbsp", "");

        // 返回文本字符串
        return htmlStr.trim().replaceAll(" ", "");
    }
}