/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月5日 上午10:44:14
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.support;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.huayin.common.util.JsonUtils;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.exterior.dto.RequestDto;
import com.huayin.printmanager.exterior.dto.ValidateReturnDto;
import com.huayin.printmanager.exterior.utils.MD5;

/**
 * <pre>
 * 验证签名机制
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月5日上午10:44:14, zhengby
 */
public class SignValidate
{
	private static Logger logger = LoggerFactory.getLogger(SignValidate.class);
	
	public static ValidateReturnDto validate(HttpServletRequest request, HttpServletResponse response)
	{
		String strcont = null;
		ValidateReturnDto returnDto = new ValidateReturnDto(); // 包含签字验证的是否通过与请求参数实体
		// 获取字符流
		strcont = getContent(request);
		if (strcont == null)
		{
			returnDto.setPass(false);
			logger.error("读取请求字符流失败");
			return returnDto;
		}
		// 签名机制要保持一个规则：对方reqBody传什么值就要全部转成json，包括空值
		JSONObject jsonObject = JSONObject.parseObject(strcont,Feature.OrderedField);

		JSONObject reqHead = (JSONObject) jsonObject.get("reqHead");
		JSONObject reqBody = (JSONObject) jsonObject.get("reqBody");
		// 签名sign生成方式：商户号+商户秘钥+接口名称+请求体，通过MD5-32进行加密
		String sign = reqHead.getString("sign"); // 请求中的签名

		StringBuilder toSign = new StringBuilder();
		toSign.append(SystemConfigUtil.getConfig(SysConstants.MERCHANT_FROM_YSJ)); // 商户号
		toSign.append(SystemConfigUtil.getConfig(SysConstants.TOKENKEY_FROM_YSJ)); // 商户秘钥
		toSign.append(reqHead.getString("action")); // 接口名称
		toSign.append(reqBody.toJSONString()); // 请求体
		// 验证签名
		RequestDto dto = JsonUtils.jsonToObject(strcont, RequestDto.class); // 字段值反射
		if (MD5.verify(toSign.toString(), sign, "utf-8"))
		{
			logger.info("签名验证成功!请求名称：" + reqHead.getString("action"));
			// 验证之后返回的实体，里面包含验证是否通过，和所有的请求参数
			returnDto.setRequestDto(dto);
			returnDto.setPass(true);
			return returnDto;
		}
		else
		{
			logger.info("签名验证失败!请求名称：" + reqHead.get("action"));
			returnDto.setPass(false);
			return returnDto;
		}

	}

	/**
	 * <pre>
	 * 获取请求中的字符流
	 * </pre>
	 * @param request
	 * @param strcont
	 * @return
	 * @since 1.0, 2018年7月19日 下午4:44:35, zhengby
	 */
	@SuppressWarnings("finally")
	private static String getContent(HttpServletRequest request)
	{
		ServletInputStream stream = null;
		String strcont = null;
		try
		{
			stream = request.getInputStream();

			StringBuilder content = new StringBuilder();
			byte[] b = new byte[1024];
			int lens = -1;
			while ((lens = stream.read(b)) > 0)
			{
				content.append(new String(b, 0, lens));
			}
			strcont = content.toString();// 内容
			logger.info("收到的字符串" + strcont);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				stream.close();// 关闭流
			}
			catch (IOException e)
			{
				e.printStackTrace();
			} 
			return strcont;
		}
	}
}
