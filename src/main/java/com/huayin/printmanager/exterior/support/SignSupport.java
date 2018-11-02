/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月3日 下午2:04:16
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.huayin.printmanager.exterior.dto.ReqBody;
import com.huayin.printmanager.exterior.dto.ReqHeader;
import com.huayin.printmanager.exterior.utils.ExteriorHelper;
import com.huayin.printmanager.exterior.utils.MD5;

/**
 * <pre>
 * 外部接口签名
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月3日下午2:04:16, zhengby
 */
public class SignSupport
{
	/**
	 * <pre>
	 * 根据传入的参数生成md5签名
	 * 签名机制：	签名sign生成方式：商户号+商户秘钥+接口名称+请求体，通过MD5-32进行加密
	 *					接口名称取调用接口action的值
	 *					请求体取调用接口reqBody的值
	 * </pre>
	 * @param header
	 * @param body
	 * @return
	 * @since 1.0, 2018年7月5日 下午3:11:40, zhengby
	 */
	public static String getMD5(ReqHeader header, ReqBody body)
	{
		JSONObject reqHeader = ExteriorHelper.toJsonObejct(header);
		JSONObject reqBody = ExteriorHelper.toJsonObejct(body);
		StringBuffer sign = new StringBuffer();
		sign.append(reqHeader.getString("merchant"));// 商户号
		sign.append(reqHeader.getString("tokenKey"));// 商户秘钥
		sign.append(reqHeader.getString("action"));// 接口名称
		sign.append(reqBody.toString()); // 请求体
		System.out.println("sign:"+sign.toString());
		String MD5cbg = SignSupport.MD5sign(sign.toString(), "utf-8");
		return MD5cbg;
	}
	
	/**
	 * <pre>
	 * 生成sign的方法
	 * </pre>
	 * @param text
	 * @return
	 * @since 1.0, 2018年7月3日 下午3:01:00, zhengby
	 */
	public static String MD5sign(String text, String chartset)
	{
		String sign = MD5.sign(text, chartset);
		return sign;
	}

	public static String mapFilter(Map<String, String> params)
	{
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++)
		{
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1)
			{// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			}
			else
			{
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}

	// ------ 另外一种生成32位签名的方法
//
//	public static String getMD5(String str,String chartset)
//	{
//		try
//		{
//			// 生成一个MD5加密计算摘要
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			str = MD5.sign(str, chartset);
//			// 计算md5函数
//			md.update(str.getBytes());
//			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
//			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
//			String md5 = new BigInteger(1, md.digest()).toString(16);
//			// BigInteger会把0省略掉，需补全至32位
//			return fillMD5(md5);
//		}
//		catch (Exception e)
//		{
//			throw new RuntimeException("MD5加密错误:" + e.getMessage(), e);
//		}
//	}
//
//	public static String fillMD5(String md5)
//	{
//		return md5.length() == 32 ? md5 : fillMD5("0" + md5);
//	}
//	
	
}
