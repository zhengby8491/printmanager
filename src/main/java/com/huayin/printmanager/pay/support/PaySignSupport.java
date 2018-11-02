package com.huayin.printmanager.pay.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huayin.printmanager.pay.util.MD5;
import com.huayin.printmanager.pay.util.RSA;

/**
 * 参考【阿里demo】
 * 
 * @author mys
 *
 */
public class PaySignSupport {

	public static final String SIGN_MD5 = "MD5";
	public static final String SIGN_RSA = "RSA";
	public static final String SIGN_RSA2 = "RS2";
	
	private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {

		Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	/**
	 * 构造支付宝的SIGN
	 * @param params
	 * @param key
	 * @return
	 */
	public static String buildRequestSignForAli(Map<String, String> params, String key) {
		Map<String, String> sfilter = PaySignSupport.paraFilter(params);
		String parstr = PaySignSupport.createLinkString(sfilter);
		String signType = params.get("sign_type");
		String sign = null;
		switch (signType.toUpperCase()) {
		case SIGN_MD5:
			sign = MD5.sign(parstr, key, "utf-8");
			break;
		case SIGN_RSA:
			sign = RSA.sign(parstr, key, "utf-8");
			break;
		case SIGN_RSA2:
			break;
		default:
			break;
		}
		return sign;
	}
	
	/**
	 * 构造微信的SIGN
	 * @param params
	 * @param key
	 * @return
	 */
	public static String buildRequestSignForWeixin(Map<String, String> params, String key) {
		Map<String, String> sfilter = PaySignSupport.paraFilter(params);
		String parstr = PaySignSupport.createLinkString(sfilter);
		parstr += "&key=" + key;
		String signType = params.remove("sign_type");
		String sign = null;
		switch (signType.toUpperCase()) {
		case SIGN_MD5:
			sign = MD5.sign(parstr, "", "utf-8");
			break;
		case SIGN_RSA:
			sign = RSA.sign(parstr, "", "utf-8");
			break;
		case SIGN_RSA2:
			break;
		default:
			break;
		}
		return sign;
	}
	
	/**
	 * 验证响应签名
	 * @param params
	 * @param partner
	 * @param publicKey
	 * @return
	 */
	public static boolean verify(Map<String, String> params, String partner, String publicKey ) {

        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
    	String responseTxt = "false";
		if(params.get("notify_id") != null) {
			String notify_id = params.get("notify_id");
			responseTxt = verifyResponse(partner, notify_id);
		}
		
	    String sign = "";
	    if(params.get("sign") != null) {sign = params.get("sign");}
	    boolean isSign = veryfyResponseSign(params, sign, publicKey);

        //写日志记录（若要调试，请取消下面两行注释）
        //String sWord = "responseTxt=" + responseTxt + "\n isSign=" + isSign + "\n 返回回来的参数：" + AlipayCore.createLinkString(params);
	    //AlipayCore.logResult(sWord);

        if (isSign && responseTxt.equals("true")) {
            return true;
        } else {
            return false;
        }
    }
	
	/**
	 * 验证响应签名
	 * @param params
	 * @param key
	 * @return
	 */
	private static boolean veryfyResponseSign(Map<String, String> params, String sign, String key) {
		Map<String, String> sfilter = PaySignSupport.paraFilter(params);
		String parstr = PaySignSupport.createLinkString(sfilter);
		String signType = params.get("sign_type");
		boolean isSign = false;
		switch (signType.toUpperCase()) {
		case SIGN_MD5:
			isSign = MD5.verify(parstr, sign, key, "utf-8");
			break;
		case SIGN_RSA:
			isSign = RSA.verify(parstr, sign, key, "utf-8");
			break;
		case SIGN_RSA2:
			break;
		default:
			break;
		}
		return isSign;
	}
	
	/**
    * 获取远程服务器ATN结果,验证返回URL
    * @param notify_id 通知校验ID
    * @return 服务器ATN结果
    * 验证结果集：
    * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
    * true 返回正确信息
    * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
    */
    private static String verifyResponse(String partner, String notify_id) {
        //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
        String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;
        return checkUrl(veryfy_url);
    }
    
    /**
     * 获取远程服务器ATN结果
     * @param urlvalue 指定URL路径地址
     * @return 服务器ATN结果
     * 验证结果集：
     * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
     * true 返回正确信息
     * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
     private static String checkUrl(String urlvalue) {
         String inputLine = "";

         try {
             URL url = new URL(urlvalue);
             HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
             BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection
                 .getInputStream()));
             inputLine = in.readLine().toString();
         } catch (Exception e) {
             e.printStackTrace();
             inputLine = "";
         }

         return inputLine;
     }
     
//     public static void main(String[] args) {
//		System.out.println(MD5.sign("appid=wx16cb063a2b20047f&attach=聚印网&body=团购&device_info=WEB&fee_type=CNY&mch_id=1407322802&nonce_str=53os31tasbteidgfrgkdkdaktkaj0mdt&notify_url=http://183.15.179.204:11001/api/pay/weixin/pc/notify&out_trade_no=170418151651424735337&spbill_create_ip=127.0.0.1&total_fee=0.01&trade_type=JSAPI&key=huayin20177jav7mfqt9xpfjj2krz7ik", "", "utf-8"));
//	}
}
