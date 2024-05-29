package com.liu.forever.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;

/**
 * 请求跑龙套
 *
 * @author Admin
 * @date 2023/07/21
 */
@Log4j2
public class RequestUtils {


    /**
     * 获取请求头
     *
     * @param request 请求
     * @param headerName 请求头名称
     * @return {@link String}
     */
    public static String getHeader(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }

    /**
     * get方法
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getMethod(HttpServletRequest request) {
        return request.getMethod();
    }

    /**
     * 获取url
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getUrl(HttpServletRequest request) {
        return request.getRequestURI();
    }


    /**
     * 获取当前请求的IP,对使用代理后都有效
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getRequestIp(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取GET请求的参数
     *
     * @param request   请求
     * @param paramName 参数名
     * @return {@link String}
     */
    public static String getGetParameter(HttpServletRequest request, String paramName) {
        return request.getParameter(paramName);
    }

    /**
     * 获取GET请求的所有参数
     *
     * @param request 请求
     * @return {@link Map<String,String>}
     */
    public static JSONObject getAllGetParameters(HttpServletRequest request) {
        JSONObject parameters = new JSONObject();
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            parameters.set(paramName, request.getParameter(paramName));
        }

        return parameters;
    }

    /**
     * 获取POST请求中的所有参数
     *
     * @param request 请求
     * @return {@link JSONObject}
     */
    public static JSONObject getAllPostParameters(HttpServletRequest request) {
        // 检查Content-Type头
        String contentType = request.getContentType();
        JSONObject parameters = new JSONObject();

        if (contentType != null && contentType.contains("application/json")) {
            try {
                // 从请求中获取输入流
                InputStream in = request.getInputStream();
                // 使用BufferedReader和InputStreamReader从输入流中读取字符串
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String jsonStr = sb.toString();
                // 解析JSON字符串
                parameters = JSONUtil.parseObj(jsonStr);
            } catch (IOException e) {
                log.error("获取POST请求参数失败", e);
            }
        } else {
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                parameters.set(paramName, request.getParameter(paramName));
            }
        }
        return parameters;
    }

    /**
     * 获取请求的Content-Type
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getContentType(HttpServletRequest request) {
        return request.getContentType();
    }
}