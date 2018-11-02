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

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.vo.WorkDeskVo;

/**
 * <pre>
 * 框架  - 全局属性控制
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年5月18日, zhaojt
 * @version 	   2.0, 2018年2月27日下午2:40:14, zhengby, 代码规范
 */
@Controller
public class IndexController extends BaseController
{
	
	/**
	 * <pre>
	 * 页面  - 跳转到默认页
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:40:58, zhengby
	 */
	@RequestMapping(value = "/")
	public String main()
	{
		return "redirect:" + "/index";
	}

	/**
	 * <pre>
	 * 页面  - 跳转到首页
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:41:20, zhengby
	 */
	@RequestMapping(value = "index")
	public String index(ModelMap map)
	{
		return "index";
	}
	
	/**
	 * <pre>
	 * 页面  - 跳转到工作台（标准版）
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:41:37, zhengby
	 */
	@RequestMapping(value = "welcome")
	public String welcome(ModelMap map)
	{	
		map.put("notice", serviceFactory.getWorkbenchNoticeService().getNotice());
		map.put("oftenList", serviceFactory.getWorkbenchOftenService().get());
		return "welcome";
	}
	
	/**
	 * <pre>
	 * 页面  - 跳转到工作台（工单版）
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:42:09, zhengby
	 */
	@RequestMapping(value = "welcomeWork")
	public String welcomeWork(ModelMap map)
	{	
		map.put("notice", serviceFactory.getWorkbenchNoticeService().getNotice());
		map.put("oftenList", serviceFactory.getWorkbenchOftenService().get());
		return "welcomeWork";
	}

	/**
	 * <pre>
	 * 功能  - 查询是否需要提醒与提醒内容
	 * </pre>
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:42:49, zhengby
	 */
	@RequestMapping(value = "${basePath}/queryExpire")
	@ResponseBody
	public AjaxResponseBody queryExpire(HttpServletRequest request)
	{
		return returnSuccessBody(serviceFactory.getWorkDeskService().queryExpire());
	}
	
	/**
	 * <pre>
	 * 功能  - 不需按时间查询的数据加载
	 * </pre>
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:43:05, zhengby
	 */
	@RequestMapping(value = "${basePath}/query")
	@ResponseBody
	public AjaxResponseBody query(HttpServletRequest request)
	{
		WorkDeskVo workDeskVo = new WorkDeskVo();
		workDeskVo.setProductStockQty(serviceFactory.getWorkDeskService().getProductStockQty());
		workDeskVo.setProductStockMoney(serviceFactory.getWorkDeskService().getProductStockMoney());
		workDeskVo.setMaterialStockQty(serviceFactory.getWorkDeskService().getMaterialStockQty());
		workDeskVo.setMaterialStockMoney(serviceFactory.getWorkDeskService().getMaterialStockMoney());
		workDeskVo.setMaterialMinStock(serviceFactory.getWorkDeskService().getMaterialMinStock());
		workDeskVo.setNotArriveOutSource(serviceFactory.getWorkDeskService().getNotArriveOutSource());
		workDeskVo.setNotStockPurch(serviceFactory.getWorkDeskService().getNotStockPurch());
		workDeskVo.setNotDeliveSale(serviceFactory.getWorkDeskService().getNotDeliveSale());
		workDeskVo.setNotPayment(serviceFactory.getWorkDeskService().getNotPayment());
		workDeskVo.setNotReceiveOrder(serviceFactory.getWorkDeskService().getNotReceiveOrder());
		workDeskVo.setNotCheckSale(serviceFactory.getWorkDeskService().getNotCheckSale());
		workDeskVo.setNotCheckPurch(serviceFactory.getWorkDeskService().getNotCheckPurch());
		workDeskVo.setNotCheckWork(serviceFactory.getWorkDeskService().getNotCheckWork());
		workDeskVo.setNotCheckPayment(serviceFactory.getWorkDeskService().getNotCheckPayment());
		workDeskVo.setNotCheckOutSource(serviceFactory.getWorkDeskService().getNotCheckOutSource());
		workDeskVo.setNotCheckReceive(serviceFactory.getWorkDeskService().getNotCheckReceive());
		workDeskVo.setNotCheckWriteoffPayment(serviceFactory.getWorkDeskService().getNotCheckWriteoffPayment());
		workDeskVo.setNotCheckWriteoffReceive(serviceFactory.getWorkDeskService().getNotCheckWriteoffReceive());
		//未清数量
		workDeskVo.setWorkSaleSumQty(serviceFactory.getWorkDeskService().getWorkSaleSumQty());
		workDeskVo.setDeliverSumQty(serviceFactory.getWorkDeskService().getDeliverSumQty() + serviceFactory.getWorkDeskService().getDeliverWorkSumQty());
		workDeskVo.setPurchSumQty(serviceFactory.getWorkDeskService().getPurchSumQty());
		workDeskVo.setArriveSumQty(serviceFactory.getWorkDeskService().getArriveSumQty());
		workDeskVo.setReceiveSumQty(serviceFactory.getWorkDeskService().getReceiveSumQty());
		workDeskVo.setPaymentPurchSumQty(serviceFactory.getWorkDeskService().getPaymentPurchSumQty());
		workDeskVo.setPaymentOutSourceSumQty(serviceFactory.getWorkDeskService().getPaymentOutSourceSumQty());
		workDeskVo.setWorkPurchQty(serviceFactory.getWorkDeskService().getWorkPurchQty());
		workDeskVo.setWorkTakeQty(serviceFactory.getWorkDeskService().getWorkTakeQty());
		workDeskVo.setPurchReconcilQty(serviceFactory.getWorkDeskService().getPurchReconcilQty());
		workDeskVo.setSaleReconcilQty(serviceFactory.getWorkDeskService().getSaleReconcilQty());
		workDeskVo.setProcessReconcilQty(serviceFactory.getWorkDeskService().getProcessReconcilQty());
		workDeskVo.setWorkStockQty(serviceFactory.getWorkDeskService().getWorkStockQty());
		return returnSuccessBody(workDeskVo);
	}

	@RequestMapping(value = "${basePath}/getVersionInfo")
	@ResponseBody
	public AjaxResponseBody getVersionInfo(HttpServletRequest request)
	{
		return returnSuccessBody(serviceFactory.getWorkDeskService().getVersionNotice());
	}
	
	
}
