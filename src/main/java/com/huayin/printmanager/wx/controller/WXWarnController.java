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

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.wx.util.WxUtil;
import com.huayin.printmanager.wx.vo.OutSourceNotArriveVo;
import com.huayin.printmanager.wx.vo.OutSourceNotPaymentVo;
import com.huayin.printmanager.wx.vo.PurchNotPaymentVo;
import com.huayin.printmanager.wx.vo.PurchNotStockVo;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.SaleNotDeliveryVo;
import com.huayin.printmanager.wx.vo.SaleNotProductionVo;
import com.huayin.printmanager.wx.vo.SaleNotReceiveVo;

/**
 * <pre>
 * 微信 - 未清预警
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/wx/warn")
public class WXWarnController extends BaseController
{
	/**
	 * <pre>
	 * 数据 - 未生产订单
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:18:39, think
	 */
	@RequestMapping(value = "saleNotProduction")
	@ResponseBody
	public SearchResult<SaleNotProductionVo> saleNotProduction(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<SaleNotProductionVo> result = serviceFactory.getWXWarnService().findNotProductionSaleByCondition(queryParam);
		// 算剩余天数百分比
		for (SaleNotProductionVo detail : result.getResult())
		{
			double days = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), detail.getDeliveryTime());
			if (days == 0)
			{
				detail.setDeliveryPercent(new BigDecimal(-100));
			}
			else
			{
				double forDays = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), new Date());
				detail.setDeliveryPercent(new BigDecimal(forDays * 100 / days).setScale(0, BigDecimal.ROUND_HALF_UP));
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 未送货订单
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:19:27, think
	 */
	@RequestMapping(value = "saleNotDelivery")
	@ResponseBody
	public SearchResult<SaleNotDeliveryVo> saleNotDelivery(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<SaleNotDeliveryVo> result = serviceFactory.getWXWarnService().findNotDeliverSaleByCondition(queryParam);
		// 算剩余天数百分比
		for (SaleNotDeliveryVo detail : result.getResult())
		{
			double days = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), detail.getDeliveryTime());
			if (days == 0)
			{
				detail.setDeliveryPercent(new BigDecimal(-100));
			}
			else
			{
				double forDays = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), new Date());
				detail.setDeliveryPercent(new BigDecimal(forDays * 100 / days).setScale(0, BigDecimal.ROUND_HALF_UP));
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 未入库采购
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:19:38, think
	 */
	@RequestMapping(value = "purchNotStock")
	@ResponseBody
	public SearchResult<PurchNotStockVo> purchNotStock(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<PurchNotStockVo> result = serviceFactory.getWXWarnService().findNotStockPurchByCondition(queryParam);
		for (PurchNotStockVo detail : result.getResult())
		{
			double days = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), detail.getDeliveryTime());
			if (days == 0)
			{
				detail.setDeliveryPercent(new BigDecimal(-100));
			}
			else
			{
				double forDays = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), new Date());
				detail.setDeliveryPercent(new BigDecimal(forDays * 100 / days).setScale(0, BigDecimal.ROUND_HALF_UP));
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 未到货发外
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:19:49, think
	 */
	@RequestMapping(value = "outSourceNotArrive")
	@ResponseBody
	public SearchResult<OutSourceNotArriveVo> outSourceNotArrive(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<OutSourceNotArriveVo> result = serviceFactory.getWXWarnService().findNotArriveOutSourceByCondition(queryParam);
		for (OutSourceNotArriveVo detail : result.getResult())
		{
			double days = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), detail.getDeliveryTime());
			if (days == 0)
			{
				detail.setDeliveryPercent(new BigDecimal(-100));
			}
			else
			{
				double forDays = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), new Date());
				detail.setDeliveryPercent(new BigDecimal(forDays * 100 / days).setScale(0, BigDecimal.ROUND_HALF_UP));
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 未收款销售
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:19:59, think
	 */
	@RequestMapping(value = "saleNotReceive")
	@ResponseBody
	public SearchResult<SaleNotReceiveVo> saleNotReceive(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<SaleNotReceiveVo> result = serviceFactory.getWXWarnService().findShouldReceive(queryParam);
		for (SaleNotReceiveVo detail : result.getResult())
		{
			double days = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), detail.getReconcilTime());
			if (days == 0)
			{
				detail.setReconcilPercent(new BigDecimal(-100));
			}
			else
			{
				double forDays = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), new Date());
				detail.setReconcilPercent(new BigDecimal(forDays * 100 / days).setScale(0, BigDecimal.ROUND_HALF_UP));
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 未付款采购
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:20:11, think
	 */
	@RequestMapping(value = "purchNotPayment")
	@ResponseBody
	public SearchResult<PurchNotPaymentVo> purchNotPayment(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<PurchNotPaymentVo> result = serviceFactory.getWXWarnService().findPurchShouldPayment(queryParam);
		for (PurchNotPaymentVo detail : result.getResult())
		{
			double days = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), detail.getReconcilTime());
			if (days == 0)
			{
				detail.setReconcilPercent(new BigDecimal(-100));
			}
			else
			{
				double forDays = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), new Date());
				detail.setReconcilPercent(new BigDecimal(forDays * 100 / days).setScale(0, BigDecimal.ROUND_HALF_UP));
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 未付款发外
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:20:21, think
	 */
	@RequestMapping(value = "outSourceNotPayment")
	@ResponseBody
	public SearchResult<OutSourceNotPaymentVo> outSourceNotPayment(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<OutSourceNotPaymentVo> result = null;
		try
		{
			result = serviceFactory.getWXWarnService().findOutSourceShouldPayment(queryParam);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		for (OutSourceNotPaymentVo detail : result.getResult())
		{
			double days = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), detail.getReconcilTime());
			if (days == 0)
			{
				detail.setReconcilPercent(new BigDecimal(-100));
			}
			else
			{
				double forDays = DateUtils.getDistanceOfTwoDate(detail.getCreateTime(), new Date());
				detail.setReconcilPercent(new BigDecimal(forDays * 100 / days).setScale(0, BigDecimal.ROUND_HALF_UP));
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 未清首页
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:20:31, think
	 */
	@RequestMapping(value = "view/index")
	public String indexView(HttpServletRequest request, ModelMap map)
	{
		return "wx/warn/index";
	}

	/**
	 * <pre>
	 * 页面 - 未生产订单
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:20:41, think
	 */
	@RequestMapping(value = "view/saleNotProduction")
	public String saleNotProductionView(HttpServletRequest request, ModelMap map)
	{
		return "wx/warn/sale_not_produce";
	}

	/**
	 * <pre>
	 * 页面 - 未送货订单
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:21:30, think
	 */
	@RequestMapping(value = "view/saleNotDelivery")
	public String saleNotDeliveryView(HttpServletRequest request, ModelMap map)
	{
		return "wx/warn/sale_not_deliver";
	}

	/**
	 * <pre>
	 * 页面 - 未入库采购
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:21:56, think
	 */
	@RequestMapping(value = "view/purchNotStock")
	public String purchNotStockView(HttpServletRequest request, ModelMap map)
	{
		return "wx/warn/purch_not_stock";
	}

	/**
	 * <pre>
	 * 页面 - 未到货发外
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:22:05, think
	 */
	@RequestMapping(value = "view/outSourceNotArrive")
	public String outSourceNotArriveView(HttpServletRequest request, ModelMap map)
	{
		return "wx/warn/outsource_not_arrive";
	}

	/**
	 * <pre>
	 * 页面 - 未收款销售
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:22:16, think
	 */
	@RequestMapping(value = "view/saleNotReceive")
	public String saleNotReceiveView(HttpServletRequest request, ModelMap map)
	{
		return "wx/warn/sale_not_receive";
	}

	/**
	 * <pre>
	 * 页面 - 未付款采购
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:22:25, think
	 */
	@RequestMapping(value = "view/purchNotPayment")
	public String purchNotPaymentView(HttpServletRequest request, ModelMap map)
	{
		return "wx/warn/puch_not_pay";
	}

	/**
	 * <pre>
	 * 页面 - 未付款发外
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:22:36, think
	 */
	@RequestMapping(value = "view/outSourceNotPayment")
	public String outSourceNotPaymentView(HttpServletRequest request, ModelMap map)
	{
		return "wx/warn/outsouce_not_pay";
	}

}
