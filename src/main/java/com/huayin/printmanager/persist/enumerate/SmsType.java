package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 短信发送类型
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年6月20日
 */
public enum SmsType
{

	/**
	 * 普通
	 */
	COMMON("普通", 2),
	/**
	 * 促销
	 */
	PROMOTION("促销", 1),
	/**
	 * 告警
	 */
	WARN("告警", 5),
	/**
	 * 支付
	 */
	IMPREST("支付", 3),
	/**
	 * 提款
	 */
	DRAW("提款", 3),
	/**
	 * 注册
	 */
	REGEDIT_VALIDCODE("注册验证码", 4),
	/**
	 * 找回密码
	 */
	RECOVER_PWD_VALIDCODE("找回密码验证码", 4),
	/**
	 * 短信接入商
	 */
	SMSPORTAL("短信接入商",2),
	/**
	 * 其他
	 */
	OTHER("其他",1);

	private String text;

	private int value;

	SmsType(String text,int value)
	{
		this.text = text;
		this.value=value;
	}

	public String getText()
	{
		return text;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public String createCodeMsg(String validCode)
	{
		switch (this)
		{
			case REGEDIT_VALIDCODE:
				return "【印管家】尊敬的用户,您注册时请求的验证码是" + validCode;
			case RECOVER_PWD_VALIDCODE:
				return "【印管家】尊敬的用户,您找回密码时请求的验证码是：" + validCode;
			default:
				return "【印管家】尊敬的用户,您请求的验证码是：" + validCode;
		}
	}
}
