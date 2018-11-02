/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.pay.config.WxConfig;
import com.huayin.printmanager.pay.support.PaySignSupport;
import com.huayin.printmanager.pay.util.QRCodeUtils;
import com.huayin.printmanager.persist.entity.sys.Buy;
import com.huayin.printmanager.persist.entity.sys.BuyRecord;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OrderState;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;
import com.itextpdf.text.pdf.qrcode.EncodeHintType;

/**
 * <pre>
 * 公共  - 支付接口
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年7月11日, mxl
 * @version 	   1.0, 2018年2月27日下午2:21:05, zhengby, 代码规范
 */
@RequestMapping(value = "${basePath}/pay")
@Controller
public class PayController extends BaseController
{

	private static final int AILIPAY = 1;

	private static final int WEIXIN = 2;

	/**
	 * <pre>
	 * 获取微信支付二维码
	 * </pre>
	 * @param request
	 * @param response
	 * @param orderNo
	 * @param productId
	 * @since 1.0, 2018年2月27日 下午2:22:09, zhengby
	 */
	@RequestMapping(value = "weixin/pc")
	public void weixinByPc(HttpServletRequest request, HttpServletResponse response, @RequestParam("orderNo") String orderNo, @RequestParam("productId") Long productId)
	{

		try
		{
			Buy product = serviceFactory.getBuyService().get(productId);
			if (product == null)
			{
				logger.error("服务不存在");
				return;
			}
			Map<String, Object> result = serviceFactory.getPayService().weixinByPc(product, orderNo, null, "NATIVE", "PC");
			if ((boolean) result.get("payStatus") == true)
			{
				String codeUrl = (String) result.get("code_url");
				this.encodeQrcode(codeUrl, response);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * <pre>
	 * 生成二维码图片 不存储 直接以流的形式输出到页面 
	 * </pre>
	 * @param code_url
	 * @param response
	 * @since 1.0, 2018年2月27日 下午2:22:21, zhengby
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "weixin/pc/qrcode")
	public void encodeQrcode(@RequestParam("qrcode_url") String code_url, HttpServletResponse response)
	{
		if (code_url == null || "".equals(code_url))
			return;
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 设置字符集编码类型
		BitMatrix bitMatrix = null;
		try
		{
			bitMatrix = multiFormatWriter.encode(code_url, BarcodeFormat.QR_CODE, 300, 300, hints);
			BufferedImage image = QRCodeUtils.toBufferedImage(bitMatrix);
			// 输出二维码图片流
			ImageIO.write(image, "png", response.getOutputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (WriterException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * <pre>
	 * 微信网页支付通知（回调）
	 * 
	 * 支付完成后，微信会把相关支付结果和用户信息发送给商户，商户需要接收处理，并返回应答。
		对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，尽可能提高通知的成功率，但微信不保证通知最终能成功。 
		（通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）
		注意：同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
		推荐的做法是，当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返回结果成功。
		在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
		特别提醒：商户系统对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致，防止数据泄漏导致出现“假通知”，造成资金损失。
	 * </pre>
	 * @param request
	 * @param response
	 * @since 1.0, 2018年2月27日 下午2:22:36, zhengby
	 */
	@RequestMapping(value = "weixin/pc/notify")
	public void notify(HttpServletRequest request, HttpServletResponse response)
	{

		String xmlString = null;
		byte[] buffer = new byte[64 * 1024];
		byte[] data = null;
		InputStream in = null;
		int length = 0;
		try
		{
			in = request.getInputStream();
			length = in.read(buffer);
			data = new byte[length];
			System.arraycopy(buffer, 0, data, 0, length);
			xmlString = new String(data, request.getCharacterEncoding());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{

			// 数据
			System.out.println("数据为：" + xmlString);

			// 将字符xml转换成map
			if (!xmlString.substring(xmlString.length() - 1).equals(">"))
			{
				return;
			}
			Map<String, String> params = StringUtils.xmlString2Map(xmlString);
			params.put("sign_type", WxConfig.SIGN_TYPE);

			// 印管家订单号
			String out_trade_no = params.get("out_trade_no");

			// 附加数据
			String attach = params.get("attach");

			// 先判断是否已经处理,如果已经处理过 不在进行处理
			// BuyRecord purchaseRecord = null;
			// if("PC".equals(attach)){
			// purchaseRecord =serviceFactory.getBuyService().getOrder(Long.parseLong(out_trade_no));
			// }
			// if("WX".equals(attach)){
			// purchaseRecord = serviceFactory.getBuyService().findOrderByBillNo(out_trade_no);
			// }

			String billNo = out_trade_no.substring(0, out_trade_no.indexOf("_"));
			//
			BuyRecord purchaseRecord = serviceFactory.getBuyService().findOrderByBillNo(billNo);

			if (purchaseRecord != null && purchaseRecord.getIsPay() == BoolValue.YES)
			{
				response.getWriter().write(WxConfig.SUCCESS_RESPONSE);
				return;
			}

			// 校验微信签名
			String sign = PaySignSupport.buildRequestSignForWeixin(params, WxConfig.APP_KEY).toUpperCase();
			System.out.println("++++++++++正确签名为：" + sign);

			if (!params.get("sign").equals(sign))
			{
				System.out.println("+++++++++==签名错误");
				return;
			}

			String result_code = params.get("result_code");

			// 支付成功
			if (StringUtils.isNotBlank(result_code) && "SUCCESS".equalsIgnoreCase(result_code) && purchaseRecord != null)
			{
				System.out.println("++++++++++++支付成功了！");

				// 微信订单号
				String transaction_id = params.get("transaction_id");

				if (null != billNo)
				{
					// 完成订单
					serviceFactory.getBuyService().updateOrderInfo(billNo, transaction_id, WEIXIN, attach);
					// if(!serviceFactory.getBuyService().findOrderByBillNo(billNo).getIsPay().equals("YES"))
					// {
					// serviceFactory.getBuyService().updateOrderInfo(billNo,transaction_id,WEIXIN,attach);
					// }
				}
				else
				{
					return;
				}

				// 返回成功
				response.getWriter().write(WxConfig.SUCCESS_RESPONSE);
			}

		}
		catch (Exception e)
		{

			e.printStackTrace();
		}

	}

	/**
	 * <pre>
	 * 第一步,选择模块
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:23:00, zhengby
	 */
	@RequestMapping(value = "step1")
	public ModelAndView step1()
	{
		return new ModelAndView("pay/step1_choose");
	}

	/**
	 * <pre>
	 * 第二步,跳转填写订单页面
	 * </pre>
	 * @param map
	 * @param productId
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:23:08, zhengby
	 */
	@RequestMapping(value = "step2/{productId}")
	public ModelAndView step2(ModelMap map, @PathVariable Long productId)
	{
		Buy product = serviceFactory.getBuyService().get(productId);
		map.put("company", UserUtils.getCompany());
		map.put("product", product);
		return new ModelAndView("pay/step2_oder");
	}

	/**
	 * <pre>
	 * 第三步,跳转支付选择页面
	 * </pre>
	 * @param billNo
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:23:19, zhengby
	 */
	@RequestMapping(value = "step3/{billNo}")
	public ModelAndView step3(@PathVariable String billNo, ModelMap map)
	{
		BuyRecord purchaseRecord = serviceFactory.getBuyService().findOrderByBillNo(billNo);
		map.put("purchaseRecord", purchaseRecord);
		return new ModelAndView("pay/step3_payment");
	}

	/**
	 * <pre>
	 * 第四步,跳到支付成功页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:23:29, zhengby
	 */
	@RequestMapping(value = "step4")
	public ModelAndView step4()
	{
		return new ModelAndView("pay/step4_succed");
	}

	/**
	 * <pre>
	 * 发起支付宝支付
	 * </pre>
	 * @param map
	 * @param productId
	 * @param orderId
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:23:39, zhengby
	 */
	@RequestMapping(value = "alipay/pc/step1jump/{productId}/{orderId}")
	public ModelAndView step1jump(ModelMap map, @PathVariable Long productId, @PathVariable Long orderId)
	{
		Map<String, Object> result = null;
		try
		{
			Buy product = serviceFactory.getBuyService().get(productId);
			if (product == null)
			{
				map.put("message", "服务不存在");
				return new ModelAndView("error/error.jsp");
			}
			result = serviceFactory.getPayService().alipayByPc(product, orderId);
			map.put("form", result.get("form"));
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}
		return new ModelAndView("pay/pc/step1_jump");
	}

	/**
	 * <pre>
	 * 支付宝支付同步页面回调接口
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:23:55, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "alipay/pc/notify")
	public String aliPayNotify(HttpServletRequest request, HttpServletResponse response, ModelMap map)
	{
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();)
		{
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++)
			{
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		try
		{
			// 商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

			boolean status = serviceFactory.getPayService().verifyAlipay(out_trade_no, trade_no, trade_status, params);

			// 支付成功则回写订单
			if (status)
			{
				serviceFactory.getBuyService().updateOrderInfo(out_trade_no, trade_no, AILIPAY);
			}
			map.put("orderId", out_trade_no);
			map.put("status", status);
			return "pay/step4_succed";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.debug("支付失败", e.getMessage() + "\n" + e.getLocalizedMessage());
			map.put("status", false);
			return "pay/step4_succed";
		}
	}

	/**
	 * <pre>
	 * 服务器异步回调
	 * </pre>
	 * @param request
	 * @param response
	 * @since 1.0, 2018年2月27日 下午2:24:04, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "alipay/pc/notifyAsyn")
	public void aliPayNotifyAsyn(HttpServletRequest request, HttpServletResponse response)
	{
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();)
		{
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++)
			{
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		try
		{
			// 商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

			boolean status = serviceFactory.getPayService().verifyAlipay(out_trade_no, trade_no, trade_status, params);

			// 支付成功则回写订单
			if (status)
			{
				serviceFactory.getBuyService().updateOrderInfo(out_trade_no, trade_no, AILIPAY);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.debug("支付失败", e.getMessage() + "\n" + e.getLocalizedMessage());
		}
	}

	/**
	 * <pre>
	 * 第一步，选择购买的模块
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:24:17, zhengby
	 */
	@RequestMapping(value = "step1/choose")
	public String step1ChooseView(ModelMap map)
	{
		Map<String, Object> ret = serviceFactory.getWXPayService().findProductAndMenu();
		map.putAll(ret);

		boolean flag = serviceFactory.getBuyService().check(OrderState.COMPLETED.id);
		Integer type = flag ? 1 : 2;

		if (type == 2)
		{
			BuyRecord purchaseRecord = serviceFactory.getBuyService().findByOrderState();
			map.put("purchaseRecord", purchaseRecord);
		}
		map.put("type", type);
		return "pay/step1_choose";
	}

	/**
	 * <pre>
	 * 保存购买记录
	 * </pre>
	 * @param map
	 * @param purchaseRecord
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:24:26, zhengby
	 */
	@RequestMapping(value = "savaOrder")
	@ResponseBody
	public AjaxResponseBody savaOrder(ModelMap map, BuyRecord purchaseRecord)
	{
		boolean flag = serviceFactory.getBuyService().check(OrderState.WAT_PAY.id);
		if (flag)
		{
			purchaseRecord.setOrderFrom(1);
			BuyRecord result = serviceFactory.getBuyService().savaOrer(purchaseRecord);
			return returnSuccessBody(result);
		}

		return returnErrorBody("您有未支付订单，请到购买信息里面查看继续购买");
	}

	/**
	 * <pre>
	 * 查询订单信息
	 * </pre>
	 * @param orderId
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:24:40, zhengby
	 */
	@RequestMapping(value = "findOrdrInfo")
	@ResponseBody
	public AjaxResponseBody findOrdrInfo(@RequestParam("orderId") Long orderId)
	{
		BuyRecord purchaseRecord = serviceFactory.getBuyService().getOrder(orderId);
		if (purchaseRecord != null)
		{
			return returnSuccessBody(purchaseRecord);
		}
		else
		{
			return returnErrorBody("订单不存在");
		}

	}
}
