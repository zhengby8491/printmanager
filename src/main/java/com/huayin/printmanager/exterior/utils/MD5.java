/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月3日 下午2:44:35
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.utils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * <pre>
 * 外部接口（印刷家）专用md5工具
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月3日下午2:44:35, zhengby
 */
public class MD5
{
	/**
	 * 签名字符串
	 * @param text 需要签名的字符串(必须包含秘钥)
	 * @param input_charset 编码格式
	 * @return 签名结果
	 */
	public static String sign(String text,String input_charset)
	{
		return DigestUtils.md5Hex(getContentBytes(text, input_charset));
	}

	/**
	 * 签名字符串
	 * @param text 需要签名的字符串
	 * @param sign 签名结果
	 * @param key 密钥
	 * @param input_charset 编码格式
	 * @return 签名结果
	 */
	public static boolean verify(String text, String sign, String input_charset)
	{
		String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
		System.out.println("sign:"+ sign);
		System.out.println("mysign"+mysign);
		if (mysign.equals(sign))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @param content
	 * @param charset
	 * @return
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException 
	 */
	private static byte[] getContentBytes(String content, String charset)
	{
		if (charset == null || "".equals(charset))
		{
			return content.getBytes();
		}
		try
		{
			return content.getBytes(charset);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
		}
	}
}
