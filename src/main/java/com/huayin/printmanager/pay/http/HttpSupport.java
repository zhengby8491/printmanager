/**
 * 
 */
package com.huayin.printmanager.pay.http;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 * @author mys
 *
 */
public class HttpSupport {
	
	public static String buildXmlRequest(Object obj) throws Exception {
		// 解决XStream对出现双下划线的bug
		XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
	
		// 将要提交给API的数据对象转换成XML格式数据Post给API
		return xStreamForRequestPostData.toXML(obj);
	}
	
	/**
	 * <xml> <appid>wx2421b1c4370ec43b</appid> 
	 * <attach>支付测试</attach>
	 * <body>JSAPI支付测试</body> <mch_id>10000100</mch_id> <detail><![CDATA[{
	 * "goods_detail":[ { "goods_id":"iphone6s_16G", "wxpay_goods_id":"1001",
	 * "goods_name":"iPhone6s 16G", "quantity":1, "price":528800,
	 * "goods_category":"123456", "body":"苹果手机" }, { "goods_id":"iphone6s_32G",
	 * "wxpay_goods_id":"1002", "goods_name":"iPhone6s 32G", "quantity":1,
	 * "price":608800, "goods_category":"123789", "body":"苹果手机" } ]
	 * }]]></detail> <nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>
	 * <notify_url>http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php</notify_url>
	 * <openid>oUpF8uMuAJO_M2pxb1Q9zNjWeS6o</openid>
	 * <out_trade_no>1415659990</out_trade_no>
	 * <spbill_create_ip>14.23.150.211</spbill_create_ip>
	 * <total_fee>1</total_fee> <trade_type>JSAPI</trade_type>
	 * <sign>0CB01533B8C1EF103065174F50BCA001</sign> </xml>
	 */
	public static String buildXmlRequest2wxrequest(Map<String, String> params) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();
		Element xml = document.createElement("xml");
		Element elememt = null;
		Iterator<Entry<String, String>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
			elememt = document.createElement(entry.getKey());
			elememt.setTextContent(entry.getValue());
			xml.appendChild(elememt);
		}
		document.appendChild(xml);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(document), new StreamResult(writer));
		String string = writer.toString();
		String replace = string.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");

		return replace;

	}
	
	/**
	 * 发送xml请求
	 * 
	 * @param url
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, String xml) throws Exception {
		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		request.setUrl(url);
		request.setXmlData(xml);
		HttpResponse response = httpProtocolHandler.execute(request, "", "");
		return response.getStringResult();
	}
	
	/**
	 * 发送xml请求
	 * 
	 * @param url
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String post(String url, Object obj) throws Exception {
		String xml = buildXmlRequest(obj);
		if(obj instanceof Map){
			Map<String,String> map =(Map<String, String>) obj;
			xml = buildXmlRequest2wxrequest(map);
		}
		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		request.setUrl(url);
		request.setXmlData(xml);
		HttpResponse response = httpProtocolHandler.execute(request, "", "");
		return new String(response.getByteResult(), "UTF-8");
	}
	
	public static void main(String[] args) {
		try {
			Map<String,String> params = Maps.newHashMap();
			params.put("appid", "wx16cb063a2b20047f");
			params.put("aa", "11");
			params.put("bb", "22");
			params.put("cc", "33");
			buildXmlRequest2wxrequest(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
