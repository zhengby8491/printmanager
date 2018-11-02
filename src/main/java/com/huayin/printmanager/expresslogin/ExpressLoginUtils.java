/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.expresslogin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.usertype.UserType;

/**
 * <pre>
 * 第三方登录工具
 * </pre>
 * @author mys
 * @version 1.0, 2017年2月28日
 */
public class ExpressLoginUtils
{
//	/**
//	 * 生成要请求给支付宝的参数数组
//	 * @param sParaTemp 请求前的参数数组
//	 * @return 要请求的参数数组
//	 */
//	private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp, Map<String, String> config)
//	{
//		// 除去数组中的空值和签名参数
//		Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
//		// 生成签名结果
//		String mysign = AlipayCore.buildMysign(sPara, config);
//
//		// 签名结果与签名方式加入请求提交参数组中
//		sPara.put("sign", mysign);
//		sPara.put("sign_type", AlipayConfig.sign_type);
//		return sPara;
//	}
//
//	public static String buildAlipayForm(Map<String, String> sParaTemp, String gateway, String strMethod,
//			String strButtonName, Map<String, String> config)
//	{
//		// 待请求参数数组
//		Map<String, String> sPara = buildRequestPara(sParaTemp, config);
//		String url = gateway + "_input_charset" + AlipayConfig.input_charset;
//		return buildForm(sPara, url, strMethod, strButtonName);
//	}

	/**
	 * 构造提交表单HTML数据
	 * @param sParaTemp 请求参数数组
	 * @param gateway 网关地址
	 * @param strMethod 提交方式。两个值可选：post、get
	 * @param strButtonName 确认按钮显示文字
	 * @return 提交表单HTML文本
	 */
	public static String buildForm(Map<String, String> sParaTemp, String gateway, String strMethod, String strButtonName)
	{
		StringBuffer sbHtml = new StringBuffer();
		sbHtml.append("页面正在跳转，请稍后...");
		sbHtml.append("<form id=\"express_submit\" name=\"express_submit\" action=\"" + gateway + "\" method=\""
				+ strMethod + "\">");
		for (Map.Entry<String, String> entry : sParaTemp.entrySet())
		{
			String name = entry.getKey();
			String value = entry.getValue();
			sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
		}
		// submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms['express_submit'].submit();</script>");
		return sbHtml.toString();
	}

	public static String createReturnUrl(HttpServletRequest request, UserType type)
	{
		String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/expressReturn/" + type.toString()+".htm";
		return returnUrl;
	}
}
