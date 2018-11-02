/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.pay.support.PaySignSupport;
import com.huayin.printmanager.persist.entity.sys.Buy;
import com.huayin.printmanager.persist.entity.sys.BuyRecord;
import com.huayin.printmanager.persist.entity.sys.UserShare;
import com.huayin.printmanager.persist.enumerate.OrderState;
import com.huayin.printmanager.service.pay.PayService;
import com.huayin.printmanager.utils.CacheUtils;
import com.huayin.printmanager.wx.util.WxJsapiTicketUtil;
import com.huayin.printmanager.wx.util.WxSign;
import com.huayin.printmanager.wx.util.WxUtil;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.UserVo;
import com.huayin.printmanager.wx.vo.WXShareVo;

/**
 * <pre>
 * 微信 - 公众号支付
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/wx/pay")
public class WXPayController extends BaseController
{
	@Resource
	private PayService payService;

	/**
	 * <pre>
	 * 页面 - 第一步，选择购买的模块
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:30:47, think
	 */
	@RequestMapping(value = "view/step1/choose")
	public String viewStep1Choose(HttpServletRequest request, HttpServletResponse response, ModelMap map)
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
		return "wx/pay/setp1_module";
	}

	/**
	 * <pre>
	 * 页面 - 第二步，填写用户信息
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @param type
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:32:59, think
	 */
	@RequestMapping(value = "view/step2/order/{type}/{id}")
	public String viewStep2Order(HttpServletRequest request, HttpServletResponse response, ModelMap map, @PathVariable("type") Integer type, @PathVariable("id") Long id)
	{
		// Step1：公司信息
		String openid = WxUtil.getOpenId(request);
		WXShareVo wxVo = null;
		if (CacheUtils.get(openid) == null)
		{
			UserShare userShare = serviceFactory.getWXBasicService().getUserShareByOpenId(openid);
			wxVo = new WXShareVo(openid, userShare.getUserId(), userShare.getCompanyId());
			CacheUtils.put(openid, wxVo);
		}
		else
		{
			wxVo = (WXShareVo) CacheUtils.get(openid);
		}
		UserVo userVo = serviceFactory.getWXBasicService().getUser(wxVo.getUserId(), wxVo.getCompanyId());
		map.put("userVo", userVo);

		// Step2：服务清单
		Buy product = serviceFactory.getWXPayService().getByProductId(id);
		map.put("product", product);

		return "wx/pay/step2_order";
	}

	/**
	 * <pre>
	 * 功能 - 第二步，保存订单信息
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @param purchaseRecord
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:33:09, think
	 */
	@RequestMapping(value = "/step2/order/save")
	@ResponseBody
	public AjaxResponseBody step2SavaOrder(HttpServletRequest request, HttpServletResponse response, ModelMap map, @RequestBody BuyRecord purchaseRecord)
	{
		serviceFactory.getBuyService().savaOrer(purchaseRecord);
		return returnSuccessBody(purchaseRecord.getBillNo());
	}

	/**
	 * <pre>
	 * 页面 - 第三步，显示支付页面
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @param billNo
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:33:22, think
	 */
	@RequestMapping(value = "view/step3/choose/pay/{billNo}")
	public String viewStep3ChoosePay(HttpServletRequest request, HttpServletResponse response, ModelMap map, @PathVariable("billNo") String billNo)
	{
		// Step1: 查询订单信息
		BuyRecord purchaseRecord = serviceFactory.getBuyService().findOrderByBillNo(billNo);
		map.put("purchaseRecord", purchaseRecord);

		return "wx/pay/step3_choosepay";
	}

	/**
	 * <pre>
	 * 页面 - 第三步，预支付
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:33:32, think
	 */
	@RequestMapping(value = "prepay")
	public String prepay(HttpServletRequest request, HttpServletResponse response, ModelMap map)
	{
		String orderNo = request.getParameter("orderNo");
		String billNo = request.getParameter("billNo");
		String productId = request.getParameter("productId");
		String openid = WxUtil.getOpenId(request);

		Buy product = serviceFactory.getBuyService().get(Long.parseLong(productId));

		try
		{
			Map<String, Object> result = payService.weixinByPc(product, orderNo, openid, "JSAPI", "WX");
			if ((boolean) result.get("payStatus") == true)
			{
				// String codeUrl = (String)result.get("code_url");
				// String total_fee = (String)result.get("total_fee");
				String prepay_id = (String) result.get("prepay_id");

				map.put("prepay_id", prepay_id);

				Map<String, String> params = Maps.newHashMap();
				params.put("appId", SystemConfigUtil.getConfig(SysConstants.WX_APPID));
				params.put("nonceStr", WxSign.create_nonce_str());
				params.put("timeStamp", WxSign.create_timestamp());
				params.put("package", "prepay_id=" + prepay_id);
				params.put("signType", "MD5");
				params.put("sign_type", "MD5");
				String sign = PaySignSupport.buildRequestSignForWeixin(params, "huayin20177jav7mfqt9xpfjj2krz7ik");

				params.put("paySign", sign.toUpperCase());

				map.putAll(params);
				map.remove("package");

				// 订单信息
				BuyRecord purchaseRecord = serviceFactory.getBuyService().findOrderByBillNo(billNo);
				map.put("purchaseRecord", purchaseRecord);
				map.put("billNo", billNo);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return "wx/pay/step3_paying";
	}

	/**
	 * <pre>
	 * 页面 - 支付结果
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @param state
	 * @param billNo
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:33:49, think
	 */
	@RequestMapping(value = "view/step4/pay/{state}/{billNo}")
	public String viewStep4PayState(HttpServletRequest request, HttpServletResponse response, ModelMap map, @PathVariable("state") String state, @PathVariable("billNo") String billNo)
	{
		// 微信团队郑重提示：res.err_msg将在用户支付成功后返回 ok，但并不保证它绝对可靠。
		// TODO Step1: 等一会 让微信回调先更新订单

		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		// Step2: 查询订单信息
		BuyRecord purchaseRecord = serviceFactory.getBuyService().findOrderByBillNo(billNo);
		if(purchaseRecord.getPayTime()==null||"".equals(purchaseRecord.getPayTime())){
			purchaseRecord.setPayTime(new Date());
		}
		map.put("purchaseRecord", purchaseRecord);
		map.put("state", state);

		return "wx/pay/step4_state";
	}

	/**
	 * <pre>
	 * 页面 - 购买信息
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:34:06, think
	 */
	@RequestMapping(value = "view/buy/list")
	public String viewBuyInfo(HttpServletRequest request, ModelMap map)
	{
		return "wx/pay/step8_message2";
	}

	/**
	 * <pre>
	 * 数据 - 购买信息
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:34:22, think
	 */
	@RequestMapping(value = "ajaxBuyList")
	@ResponseBody
	public SearchResult<BuyRecord> ajaxBuyList(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<BuyRecord> result = serviceFactory.getWXPayService().findAllOrder(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 分享
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:34:36, think
	 */
	@RequestMapping(value = "view/share2")
	public String viewShare2(HttpServletRequest request, ModelMap map)
	{
		String jsapi_ticket = WxJsapiTicketUtil.getJSApiTicket();
		// 注意 URL 一定要动态获取，不能 hardcode
		String linkName = request.getParameter("linkName");
		try
		{
			linkName = URLEncoder.encode(linkName, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("编码失败");
		}
		String url = SystemConfigUtil.getConfig(SysConstants.WX_URL) + "/print/wx/pay/view/share2?linkName=" + linkName;
		Map<String, String> ret = WxSign.sign(jsapi_ticket, url);
		map.putAll(ret);
		map.put("appid", SystemConfigUtil.getConfig(SysConstants.WX_APPID));

		return "wx/pay/step6_shear2";
	}

	/**
	 * <pre>
	 * 功能 - 获取分享配置信息
	 * </pre>
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:34:53, think
	 */
	@RequestMapping(value = "getConfigInfo")
	@ResponseBody
	public AjaxResponseBody getConfigInfo(HttpServletRequest request)
	{
		String jsapi_ticket = WxJsapiTicketUtil.getJSApiTicket();
		String linkName = request.getParameter("linkName");
		try
		{
			linkName = URLEncoder.encode(linkName, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("编码失败");
		}
		// 注意 URL 一定要动态获取，不能 hardcode
		// String url = SystemConfigUtil.getConfig(SysConstants.WX_URL) + "/print/wx/check/view/index";
		String url = (String) request.getSession().getAttribute("WXSHARE") + "?linkName=" + linkName;
		// String url = SystemConfigUtil.getConfig(SysConstants.WX_URL) + "/print/" + uri;
		Map<String, String> ret = WxSign.sign(jsapi_ticket, url);

		Map<String, String> map = new HashMap<String, String>();
		map.putAll(ret);
		map.put("appid", SystemConfigUtil.getConfig(SysConstants.WX_APPID));
		return returnSuccessBody(map);
	}

	/**
	 * <pre>
	 * 功能 - 取消订单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:35:06, think
	 */
	@RequestMapping(value = "cancelOrder/{id}")
	@ResponseBody
	public AjaxResponseBody cancelOrder(@PathVariable Long id)
	{
		serviceFactory.getBuyService().cancelOrder(id);
		return returnSuccessBody();
	}
}
