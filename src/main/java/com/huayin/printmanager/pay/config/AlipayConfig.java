package com.huayin.printmanager.pay.config;


public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String partner = "2088621702103536";
	
	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_id = "2088621702103536";

	// MD5密钥，安全检验码，由数字和字母组成的32位字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
    public static String key = "";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://171m5l9402.iok.la/print/pay/alipay/pc/notifyAsyn";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://171m5l9402.iok.la/print/pay/alipay/pc/notify";

	// 签名方式
	public static String sign_type = "RSA";
	
	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "";
		
	// 字符编码格式 目前支持utf-8
	public static String input_charset = "utf-8";
		
	// 支付类型 ，无需修改
	public static String payment_type = "1";
		
	// 调用的接口名，无需修改
	public static String service = "alipay.wap.create.direct.pay.by.user";
	
	//支付宝私钥
	public static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALPt89AKKawab2QBDZPBYVVrHwnB12wwwuBqr8ORtQmfYJi2UCc4a+Q9p3GjujACtqAYsZJBHogf0bnnxaRfOflL0KW7rarx8EjdHA/SIOzbKkvb2UVtqcTTd1ldkHgGe7+YwX1nMkIf9WEjZBce7ZOWQg48rmMIvEYDI5eWGBUVAgMBAAECgYBdZ/wYVg5IH/kPq1RHDi7fTxrvz8drjOy93+Sa3DtlYW2eGZcGpU2tUSxp3Bs97UqtVeRpDpExDYOsxNtfd2f6WbJ1F1gD05GFc4wy72lkqhg/Q689dAm7IR6g+5+kSefXl189q1afT6g+N3BOQRYmb85Hm8kRIdIsMA+j/xwmAQJBAOZjr+9ZWIz5dtap27YKjVSS3Gs7xhQ61DWTCLB1r2oahwG8gEmO6J3LpqRBvmtKNUdOXseqTcmTEN3dhsly9NUCQQDH7k0VmFW9CUsDBH35OzKbqx/UrDysGqf/n1grMjsT3lfK5wiA6ipuzFW3HOLPzpfL1sAZ1BWleEpc8Qmwyb9BAkBP7vJRprwUXVEf9VrndA3I9BNJmTmGN0r9M4BmSM6Nj5qbvZz86HpIlSnB/5VjgCGwFb7Io4vNjhrh/0QOjLXpAkEAjsFmAxRpq/fVusrbVDljsmlm6gktrUm/STOy1e0g59fr4fJDTSFmuQRn877JaM4/7wlg01BvQF564pAtdjEnwQJBAJvx/V1EpFPV1dVjVshkouaU4hdCHpYnXyxYt1ya/7UgriRaTMTrgx2ioN1NvsXtwmxLsu/I1W0AjfaIfQPQWX4=";
	
	
	//支付宝公钥匙
	public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

}

