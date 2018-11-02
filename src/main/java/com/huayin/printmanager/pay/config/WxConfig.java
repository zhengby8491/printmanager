package com.huayin.printmanager.pay.config;

public class WxConfig {

	public static final String ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	public static final String MCH_ID = "1407322802";
	public static final String APP_ID = "wx16cb063a2b20047f";
	public static final String APP_KEY = "huayin20177jav7mfqt9xpfjj2krz7ik";
	public static final String NOTIFY_URL = "http://171m5l9402.iok.la/print/pay/weixin/pc/notify";
	public static final String SUCCESS_RESPONSE = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
	public static final String TRADE_TYPE = "NATIVE";
	public static final String SIGN_TYPE = "MD5";
	
	
}
