/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月4日 下午3:03:54
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.utils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.huayin.common.util.Reflections;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.exterior.dto.ReqBody;
import com.huayin.printmanager.exterior.dto.ReqHeader;
import com.huayin.printmanager.exterior.dto.RequestDto;
import com.huayin.printmanager.exterior.dto.ResponseDto;
import com.huayin.printmanager.exterior.enums.ActionType;
import com.huayin.printmanager.exterior.support.SignSupport;
import com.huayin.printmanager.pay.util.JsonMapper;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 外部接口帮助类
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月4日下午3:03:54, zhengby
 */
public class ExteriorHelper
{

	public static Log logger = LogFactory.getLog("ExteriorHelper");

	/**
	 * <pre>
	 * 外部接口-外部接口请求/响应对象转换成JSONObject
	 * 过滤掉字段值为空的字段(仅适用于外部接的实体类开发)
	 * </pre>
	 * @param object
	 * @return
	 * @since 1.0, 2018年7月4日 下午3:47:13, zhengby
	 */
	public static JSONObject toJsonObejct(Object object)
	{
		JSONObject jsonObejct = new JSONObject(new TreeMap<String,Object>());
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields)
		{
			Object value = Reflections.getFieldValue(object, field.getName());
			if (null != value && !"".equals(value))
			{
				if (field.getType().equals(String.class))
				{
					jsonObejct.put(field.getName(), value);
				}
				else
				{
					JSONObject jsonObejctSup = ExteriorHelper.toJsonObejct(value);
					jsonObejct.put(field.getName(), jsonObejctSup);
				}
			}
		}
		return jsonObejct;
	}

	/**
	 * <pre>
	 * 外部接口--Map<String,String> 转换成json字符串
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年7月5日 上午9:41:58, zhengby
	 */
	public static String mapToJsonString(Map<String, String> map)
	{
		JSONObject jsonObejct = new JSONObject();
		for (String key : map.keySet())
		{
			jsonObejct.put(key, map.get(key));
		}
		return jsonObejct.toString();
	}

	/**
	 * <pre>
	 * 生成回调url登录印管家
	 * </pre>
	 * @return
	 * @since 1.0, 2018年7月11日 下午7:18:54, zhengby
	 */
	public static String url(ReqBody reqBody)
	{
		// 回调URl
		String url = SystemConfigUtil.getConfig(SysConstants.SYSTEM_URL);
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(url).append(SystemConfigUtil.getBasePath());
		strBuf.append("/exterior/login?");
		strBuf.append("u=" + reqBody.getUid() + "&");
		strBuf.append("t=" + reqBody.getToken() + "&");
		strBuf.append("PH=" + reqBody.getIsPHUser());
		return strBuf.toString();
	}

	/**
	 * <pre>
	 * 构造请求体
	 * </pre>
	 * @param body
	 * @return
	 * @since 1.0, 2018年7月17日 下午5:33:42, zhengby
	 */
	public static ResponseDto buildRequest(ReqBody body, ActionType actionType)
	{
		// 请求header
		ReqHeader head = new ReqHeader();
		head.setAction(actionType.getText());
		head.setMerchant(SystemConfigUtil.getConfig(SysConstants.MERCHANT_FROM_YSJ));
		head.setTokenKey(SystemConfigUtil.getConfig(SysConstants.TOKENKEY_FROM_YSJ));
		head.setSignStyle(SystemConfigUtil.getConfig(SysConstants.SIGNSTYLE_FROM_YSJ));
		head.setRequestTime(StringUtils.toString(new Date().getTime()));// 请求时间
		head.setSeriNumber(StringUtils.genRandomCode(20));// 序列号
		head.setVersion("1.0");

		// 签名
		head.setSign(SignSupport.getMD5(head, body));

		RequestDto request = new RequestDto();
		request.setReqBody(body);
		request.setReqHead(head);
		String requestStr = ExteriorHelper.toJsonObejct(request).toString();

		// 获取请求路径
		String requestUrl = SystemConfigUtil.getConfig(SysConstants.URL_TO_YSJ);
		// 发送请求
		String responseStr = HttpRequest.sendPost(requestUrl, requestStr);
		if (null == responseStr)
		{
			logger.error("Action:" + head.getAction());
			logger.error("请求无响应");
			if (head.getAction().equals("purchaseOrder"))
			{
				logger.error("purchaseOrder"+request.getReqBody().getPurchaseOrderNm());
			}
			return null;
		} else
		{
			ResponseDto rspDto = JsonMapper.getInstance().fromJson(responseStr, ResponseDto.class);
			logger.info("Action:" + head.getAction());
			logger.info("响应:" + "{code:" + rspDto.getResponseHeader().getRspCode() + ",msg:" + rspDto.getResponseHeader().getRspMsg()+"}");
			return JsonMapper.getInstance().fromJson(responseStr, ResponseDto.class);
		}
	}

}
