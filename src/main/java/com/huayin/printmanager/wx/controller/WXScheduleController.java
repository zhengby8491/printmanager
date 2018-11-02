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

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.persist.enumerate.WXSumQueryType;
import com.huayin.printmanager.wx.util.WxUtil;
import com.huayin.printmanager.wx.vo.OutSourceScheduleVo;
import com.huayin.printmanager.wx.vo.ProduceScheduleVo;
import com.huayin.printmanager.wx.vo.PurchScheduleVo;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.SaleScheduleVo;

/**
 * <pre>
 * 微信 - 进度追踪
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/wx/schedule")
public class WXScheduleController extends BaseController
{
	/**
	 * <pre>
	 * 数据 - 销售进度追踪
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:51:15, think
	 */
	@RequestMapping(value = "saleSchedule")
	@ResponseBody
	public SearchResult<SaleScheduleVo> saleSchedule(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<SaleScheduleVo> result = serviceFactory.getWXScheduleService().findSaleScheduleByCondition(queryParam);
		for (SaleScheduleVo saleScheduleVo : result.getResult())
		{
			Integer percent = 0;
			if (saleScheduleVo.getReceiveMoney().doubleValue() > 0)
			{
				saleScheduleVo.setScheduleState("已收款");
				percent = saleScheduleVo.getReceiveMoney().multiply(new BigDecimal(100)).divide(saleScheduleVo.getSaleMoney(), 0, BigDecimal.ROUND_HALF_UP).intValue();

			}
			else if (saleScheduleVo.getReconcilQty() > 0)
			{
				saleScheduleVo.setScheduleState("已对账");
				percent = new BigDecimal(saleScheduleVo.getReconcilQty() * 100).divide(new BigDecimal(saleScheduleVo.getSaleQty()), 0, BigDecimal.ROUND_HALF_UP).intValue();
			}
			else if (saleScheduleVo.getDeliverQty() > 0)
			{
				saleScheduleVo.setScheduleState("已送货");
				percent = new BigDecimal(saleScheduleVo.getDeliverQty() * 100).divide(new BigDecimal(saleScheduleVo.getSaleQty()), 0, BigDecimal.ROUND_HALF_UP).intValue();
			}
			else
			{
				saleScheduleVo.setScheduleState("已生产");
				percent = new BigDecimal(saleScheduleVo.getProduceedQty() * 100).divide(new BigDecimal(saleScheduleVo.getSaleQty()), 0, BigDecimal.ROUND_HALF_UP).intValue();
			}
			saleScheduleVo.setSchedulePercent(percent > 100 ? 100 : percent);
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 生产工单进度
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:51:34, think
	 */
	@RequestMapping(value = "workSchedule")
	@ResponseBody
	public SearchResult<ProduceScheduleVo> workSchedule(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<ProduceScheduleVo> result = serviceFactory.getWXScheduleService().findWorkScheduleByCondition(queryParam);
		for (ProduceScheduleVo produceScheduleVo : result.getResult())
		{
			Integer percent = new BigDecimal(produceScheduleVo.getInStockQty() * 100).divide(new BigDecimal(produceScheduleVo.getWorkQty()), 0, BigDecimal.ROUND_HALF_UP).intValue();
			produceScheduleVo.setSchedulePercent(percent > 100 ? 100 : percent);
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 采购订单进度
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:51:44, think
	 */
	@RequestMapping(value = "purchSchedule")
	@ResponseBody
	public SearchResult<PurchScheduleVo> purchSchedule(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<PurchScheduleVo> resultPurch = null;
		try
		{
			resultPurch = serviceFactory.getWXScheduleService().findPurchScheduleByCondition(queryParam);
			for (PurchScheduleVo purchScheduleVo : resultPurch.getResult())
			{
				Integer percent = 0;
				if (purchScheduleVo.getPaymentMoney().doubleValue() > 0)
				{
					purchScheduleVo.setScheduleState("已付款");
					percent = purchScheduleVo.getPaymentMoney().multiply(new BigDecimal(100)).divide(purchScheduleVo.getPurchMoney(), 0, BigDecimal.ROUND_HALF_UP).intValue();

				}
				else if (purchScheduleVo.getReconcilQty().doubleValue() > 0)
				{
					purchScheduleVo.setScheduleState("已对账");
					percent = purchScheduleVo.getReconcilQty().multiply(new BigDecimal(100)).divide(purchScheduleVo.getPurchQty(), 0, BigDecimal.ROUND_HALF_UP).intValue();
				}
				else
				{
					purchScheduleVo.setScheduleState("已入库");
					percent = purchScheduleVo.getStockQty().multiply(new BigDecimal(100)).divide(purchScheduleVo.getPurchQty(), 0, BigDecimal.ROUND_HALF_UP).intValue();
				}
				purchScheduleVo.setSchedulePercent(percent > 100 ? 100 : percent);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return resultPurch;
	}

	/**
	 * <pre>
	 * 数据 - 发外工单进度
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:51:54, think
	 */
	@RequestMapping(value = "outSourceSchedule")
	@ResponseBody
	public SearchResult<OutSourceScheduleVo> outSourceSchedule(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<OutSourceScheduleVo> result = serviceFactory.getWXScheduleService().findOutSourceDetailByCondition(queryParam);
		for (OutSourceScheduleVo detail : result.getResult())
		{
			Integer percent = 0;
			if (detail.getPaymentMoney().doubleValue() > 0)
			{
				detail.setScheduleState("已付款");
				percent = detail.getPaymentMoney().multiply(new BigDecimal(100)).divide(detail.getMoney(), 0, BigDecimal.ROUND_HALF_UP).intValue();

			}
			else if (detail.getReconcilQty().compareTo(new BigDecimal(0)) == 1)
			{
				detail.setScheduleState("已对账");
				percent = detail.getReconcilQty().multiply(new BigDecimal(100)).divide(detail.getQty(), 0, BigDecimal.ROUND_HALF_UP).intValue();
			}
			else
			{
				detail.setScheduleState("已到货");
				percent = detail.getArriveQty().multiply(new BigDecimal(100)).divide(detail.getQty(), 0, BigDecimal.ROUND_HALF_UP).intValue();
			}
			detail.setSchedulePercent(percent > 100 ? 100 : percent);
		}
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 销售进度
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:52:08, think
	 */
	@RequestMapping(value = "view/saleSchedule")
	public String saleScheduleView(HttpServletRequest request, ModelMap map)
	{
		return "wx/schedule/sale_schedule";
	}

	/**
	 * <pre>
	 * 页面 - 采购进度
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:52:18, think
	 */
	@RequestMapping(value = "view/purchSchedule")
	public String purchScheduleView(HttpServletRequest request, ModelMap map)
	{
		return "wx/schedule/purch_schedule";
	}

	/**
	 * <pre>
	 * 页面 - 生产工单进度
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:52:28, think
	 */
	@RequestMapping(value = "view/workSchedule")
	public String workScheduleView(HttpServletRequest request, ModelMap map)
	{
		return "wx/schedule/work_schedule";
	}

	/**
	 * <pre>
	 * 页面 - 生产工序信息
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:52:42, think
	 */
	@RequestMapping(value = "view/orderSchedule")
	public String orderScheduleView(HttpServletRequest request, ModelMap map)
	{
		return "wx/schedule/order_schedule";
		// return "wx/schedule/work_schedule";

	}

	/**
	 * <pre>
	 * 跳转
	 * </pre>
	 * @return
	 */
	/**
	 * <pre>
	 * 页面 - 发外进度
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:52:54, think
	 */
	@RequestMapping(value = "view/outSourceSchedule")
	public String outsourceScheduleView(HttpServletRequest request, ModelMap map)
	{
		return "wx/schedule/outsource_schedule";
	}

	/**
	 * <pre>
	 * 页面 - 进度查询首页
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:53:26, think
	 */
	@RequestMapping(value = "view/index")
	public String indexView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		return "wx/schedule/index";
	}
}
