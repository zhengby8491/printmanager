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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.persist.entity.sys.UserShare;
import com.huayin.printmanager.utils.CacheUtils;
import com.huayin.printmanager.wx.util.WxUtil;
import com.huayin.printmanager.wx.vo.HomePageCheckVo;
import com.huayin.printmanager.wx.vo.HomePageWarnVo;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.UserVo;
import com.huayin.printmanager.wx.vo.WXShareVo;

/**
 * <pre>
 * 微信 - 各模块首页
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/wx/homepage")
public class WXHomePageController extends BaseController
{

	/**
	 * <pre>
	 * 数据 - 未清预警
	 * </pre>
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:18:36, think
	 */
	@RequestMapping(value = "warn")
	@ResponseBody
	public HomePageWarnVo warn(HttpServletRequest request)
	{
		QueryParam queryParam = new QueryParam();
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		Integer workSaleSumQty = serviceFactory.getWXStatisticsService().getWorkSaleSumQty(queryParam);
		// 未送货订单
		Integer saleDeliverSumQty = serviceFactory.getWXStatisticsService().getDeliverSumQty(queryParam);
		// 未入库采购
		Integer purchStockSumQty = serviceFactory.getWXStatisticsService().getPurchSumQty(queryParam);
		// 未到货发外
		Integer arriveSumQty = serviceFactory.getWXStatisticsService().getArriveSumQty(queryParam);
		// 未收款销售
		Integer receiveSumQty = serviceFactory.getWXStatisticsService().getReceiveSumQty(queryParam);
		// 未付款采购
		Integer paymentPurchSumQty = serviceFactory.getWXStatisticsService().getPaymentPurchSumQty(queryParam);
		// 未付款发外
		Integer paymentOutSourceSumQty = serviceFactory.getWXStatisticsService().getPaymentOutSourceSumQty(queryParam);

		HomePageWarnVo resultVo = new HomePageWarnVo(workSaleSumQty, saleDeliverSumQty, purchStockSumQty, arriveSumQty, receiveSumQty, paymentPurchSumQty, paymentOutSourceSumQty);

		return resultVo;
	}

	/**
	 * <pre>
	 * 数据 - 订单审核
	 * </pre>
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:20:39, think
	 */
	@RequestMapping(value = "check")
	@ResponseBody
	public HomePageCheckVo check(HttpServletRequest request)
	{
		QueryParam queryParam = new QueryParam();
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		// 销售待审核记录数
		Integer saleCheckQty = serviceFactory.getWXStatisticsService().getSaleOrderCheckQty(queryParam);
		// 采购待审核记录数
		Integer purchCheckQty = serviceFactory.getWXStatisticsService().getPurchOrderCheckQty(queryParam);
		// 发外待审核记录数
		Integer outsourceCheckQty = serviceFactory.getWXStatisticsService().getOutSourceCheckQty(queryParam);
		// 收款待审核记录数
		Integer receiveCheckQty = serviceFactory.getWXStatisticsService().getReceiveCheckQty(queryParam);
		// 付款待审核记录数
		Integer paymentCheckQty = serviceFactory.getWXStatisticsService().getPaymentCheckQty(queryParam);
		// 付款核销待审核记录数
		Integer paymentWriteCheckQty = serviceFactory.getWXStatisticsService().getPaymentWriteCheckQty(queryParam);
		// 收款核销待审核记录数
		Integer receiveWriteCheckQty = serviceFactory.getWXStatisticsService().getReceiveWriteCheckQty(queryParam);

		return new HomePageCheckVo(saleCheckQty, purchCheckQty, outsourceCheckQty, receiveCheckQty, paymentCheckQty, paymentWriteCheckQty, receiveWriteCheckQty);
	}

	/**
	 * <pre>
	 * 页面 - 跳转个人中心
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws IOException
	 * @since 1.0, 2018年2月26日 上午9:21:06, think
	 */
	@RequestMapping(value = "center")
	public String indexView(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws IOException
	{
		String openid = WxUtil.getOpenId(request);
		WXShareVo wxVo = null;
		if (CacheUtils.get(openid) == null)
		{
			UserShare userShare = serviceFactory.getWXBasicService().getUserShareByOpenId(openid);
			if (userShare == null)
			{
				String url = request.getRequestURL().toString();
				request.getSession().setAttribute("WXTurnUrl", url);
				return "wx/bind";
			}
			wxVo = new WXShareVo(openid, userShare.getUserId(), userShare.getCompanyId());
			CacheUtils.put(openid, wxVo);
		}
		else
		{
			wxVo = (WXShareVo) CacheUtils.get(openid);
		}
		UserVo userVo = serviceFactory.getWXBasicService().getUser(wxVo.getUserId(), wxVo.getCompanyId());
		map.put("userVo", userVo);
		return "wx/center";
	}
}
