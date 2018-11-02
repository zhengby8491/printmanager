/**
 * Alipay.com Inc.
 * Copyright (c) 2005-2006 All Rights Reserved.
 */
package com.huayin.printmanager.pay.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.huayin.printmanager.utils.StringUtils;



/**
 * 参数构造工具类
 * 
 * @author
 */
public class ParameterUtil {
	
	
	
	
	
	
	/**
	 * 对现有的map数据按照字典排序，如果数据的值为空，则不参与组合；返回key=value&key2=value2...格式的数据；
	 * 一般用户加密排序，已知在用的有微信支付
	 * @param params
	 * @return
	 */
    public static String getSignDataIgnoreBlank(Map<String, String> params) {
        StringBuffer content = new StringBuffer();
        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if ("sign".equals(key)) {
                continue;
            }
            String value = (String) params.get(key);
            if(StringUtils.isBlank(value)){
            	continue;
            }
           content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        return content.toString();
    }
	
	

    /**
     * 将Map组装成待签名数据。
     * 待签名的数据必须按照一定的顺序排列 这个是规范，否则调用服务会通不过签名验证
     * @param params
     * @return
     */
    public static String getSignData(Map<String, String> params) {
        StringBuffer content = new StringBuffer();

        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if ("sign".equals(key)) {
                continue;
            }
            String value = (String) params.get(key);
            if (value != null) {
                content.append((i == 0 ? "" : "&") + key + "=" + value);
            } else {
                content.append((i == 0 ? "" : "&") + key + "=");
            }

        }

        return content.toString();
    }

    /**
     * 将Map中的数据组装成url
     * @param params
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static String mapToUrl(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (isFirst) {
                sb.append(key + "=" + URLEncoder.encode(value, "utf-8"));
                isFirst = false;
            } else {
                if (value != null) {
                    sb.append("&" + key + "=" + URLEncoder.encode(value, "utf-8"));
                } else {
                    sb.append("&" + key + "=");
                }
            }
        }
        return sb.toString();
    }

    
    
    /**
     * 将Map中的数据组装成url
     * @param params
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static String mapToUrlNotEncode(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (isFirst) {
                sb.append(key + "=" + value);
                isFirst = false;
            } else {
                if (value != null) {
                    sb.append("&" + key + "=" + value);
                } else {
                    sb.append("&" + key + "=");
                }
            }
        }
        return sb.toString();
    }
    
    
    /**
     * 取得URL中的参数值。
     * <p>如不存在，返回空值。</p>
     * 
     * @param url
     * @param name
     * @return
     */
    public static String getParameter(String url, String name) {
        if (name == null || name.equals("")) {
            return null;
        }
        name = name + "=";
        int start = url.indexOf(name);
        if (start < 0) {
            return null;
        }
        start += name.length();
        int end = url.indexOf("&", start);
        if (end == -1) {
            end = url.length();
        }
        return url.substring(start, end);
    }
}
