package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 第三方共享登录类型
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年6月20日
 */
public enum UserShareType
{
	ALIPAY("支付宝", "A"), BAIDU("百度", "B"),  SINA("新浪微博帐号", "SI"),  TENCENT(
			"腾讯QQ", "T"), WEIXIN("微信","WX");

	private String prefix;

	private String text;

	UserShareType(String text, String prefix)
	{
		this.text = text;
		this.prefix = prefix;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public String getText()
	{
		return text;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}
}
