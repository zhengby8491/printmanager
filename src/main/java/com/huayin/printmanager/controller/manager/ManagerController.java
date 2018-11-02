/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月26日 上午9:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.controller.vo.WorkDeskVo;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.utils.DateUtils;

/**
 * <pre>
 * 经理查询控制
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年5月18日, zhaojt
 * @version 	   2.0, 2018年2月26日上午10:13:54, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/manager")
public class ManagerController extends BaseController
{

	@RequestMapping(value = "sale/sum")
	@RequiresPermissions("manager:sale:sum")
	public String salesum(ModelMap map)
	{
		return "manager/sale/sum";
	}

	@RequestMapping(value = "sale/schedule")
	@RequiresPermissions("manager:sale:schedule")
	public String saleSchedule(ModelMap map)
	{
		return "manager/sale/schedule";
	}

	/**
	 * <pre>
	 * 不需按时间查询的的数据加载
	 * </pre>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "query")
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
		// 未清数量
		workDeskVo.setWorkSaleSumQty(serviceFactory.getWorkDeskService().getWorkSaleSumQty());
		workDeskVo.setDeliverSumQty(serviceFactory.getWorkDeskService().getDeliverSumQty());
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

	/**
	 * <pre>
	 * 需按时间条件查询的数据加载
	 * </pre>
	 * @param request
	 * @param map
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "queryByTime/{timeType}")
	@ResponseBody
	public AjaxResponseBody queryByTime(HttpServletRequest request, @PathVariable String timeType)
	{
		WorkDeskVo workDeskVo = new WorkDeskVo();
		Calendar cal = Calendar.getInstance();
		String dateMin = null;
		String dateMax = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if ("Yesterday".equals(timeType))
		{// 昨天
			cal.add(Calendar.DATE, -1);
			dateMin = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
			dateMax = dateMin;
		}
		else if ("ThisMonth".equals(timeType))
		{
			dateMin = DateUtils.getYear() + "-" + DateUtils.getMonth() + "-01";
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dateMax = format.format(cal.getTime());
		}
		else if ("BeforeMonth".equals(timeType))
		{
			cal.add(Calendar.MONTH, -1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			dateMin = format.format(cal.getTime());
			cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, 0);// 设置为1号,当前日期既为本月第一天
			dateMax = format.format(cal.getTime());
		}
		else if ("Quarter".equals(timeType))
		{
			dateMin = getCurrentQuarterStartTime();
			dateMax = getCurrentQuarterEndTime();
		}
		else if ("Year".equals(timeType))
		{
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DATE, 1);
			dateMin = format.format(cal.getTime());
			cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DATE, 31);
			dateMax = format.format(cal.getTime());
		}
		if (dateMax != null)
		{
			dateMin = dateMin + " 00:00:00";
			dateMax = dateMax + " 23:59:59";
		}
		workDeskVo.setSaleTotalQty(serviceFactory.getWorkDeskService().getTotalSaleQty(dateMin, dateMax));
		workDeskVo.setSaleTotalMoney(serviceFactory.getWorkDeskService().getTotalSaleMoney(dateMin, dateMax));
		workDeskVo.setPurchTotalQty(serviceFactory.getWorkDeskService().getTotalPurchQty(dateMin, dateMax));
		workDeskVo.setPurchTotalMoney(serviceFactory.getWorkDeskService().getTotalPurchMoney(dateMin, dateMax));
		workDeskVo.setCustomerDebt(serviceFactory.getWorkDeskService().getCustomerDebt(dateMin, dateMax));
		workDeskVo.setSupplierDebt(serviceFactory.getWorkDeskService().getSupplierDebt(dateMin, dateMax));
		return returnSuccessBody(workDeskVo);
	}

	@RequestMapping(value = "loadChart/{timeType}")
	@ResponseBody
	public AjaxResponseBody loadChart(HttpServletRequest request, @PathVariable String timeType)
	{
		Calendar cal = Calendar.getInstance();
		String dateMin = null;
		String dateMax = null;

		if ("ThisMonth".equals(timeType))
		{
			dateMin = DateUtils.getYear() + "-" + DateUtils.getMonth() + "-01";
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dateMax = DateUtils.formatDate(cal.getTime());
		}
		else if ("Year".equals(timeType))
		{
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DATE, 1);
			dateMin = DateUtils.formatDate(cal.getTime());
			cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DATE, 31);
			dateMax = DateUtils.formatDate(cal.getTime());
		}
		if (dateMax != null)
		{
			dateMin = dateMin + " 00:00:00";
			dateMax = dateMax + " 23:59:59";
		}
		Map<String, BigDecimal> totalSaleMoneyByDay = serviceFactory.getWorkDeskService().getTotalSaleMoneyByTimeType(dateMin, dateMax, timeType);
		Map<String, BigDecimal> totalMaterialMoneyByDay = serviceFactory.getWorkDeskService().getTotalMaterialMoneyByTimeType(dateMin, dateMax, timeType);
		Map<String, BigDecimal> totalOutSourceMoneyByDay = serviceFactory.getWorkDeskService().getTotalOutSourceMoneyByTimeType(dateMin, dateMax, timeType);
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("totalSaleMoneyByDay", totalSaleMoneyByDay);
		datas.put("totalMaterialMoneyByDay", totalMaterialMoneyByDay);
		datas.put("totalOutSourceMoneyByDay", totalOutSourceMoneyByDay);
		return returnSuccessBody(datas);
	}

	/**
	 * <pre>
	 * 本季度第一天
	 * </pre>
	 * @return
	 */
	private String getCurrentQuarterStartTime()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		if (currentMonth >= 1 && currentMonth <= 3)
			c.set(Calendar.MONTH, 0);
		else if (currentMonth >= 4 && currentMonth <= 6)
			c.set(Calendar.MONTH, 3);
		else if (currentMonth >= 7 && currentMonth <= 9)
			c.set(Calendar.MONTH, 6);
		else if (currentMonth >= 10 && currentMonth <= 12)
			c.set(Calendar.MONTH, 9);
		c.set(Calendar.DATE, 1);
		return format.format(c.getTime());

	}

	/**
	 * <pre>
	 * 本季度最后一天
	 * </pre>
	 * @return
	 */
	private String getCurrentQuarterEndTime()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		if (currentMonth >= 1 && currentMonth <= 3)
		{
			c.set(Calendar.MONTH, 2);
			c.set(Calendar.DATE, 31);
		}
		else if (currentMonth >= 4 && currentMonth <= 6)
		{
			c.set(Calendar.MONTH, 5);
			c.set(Calendar.DATE, 31);
		}
		else if (currentMonth >= 7 && currentMonth <= 9)
		{
			c.set(Calendar.MONTH, 8);
			c.set(Calendar.DATE, 31);
		}
		else if (currentMonth >= 10 && currentMonth <= 12)
		{
			c.set(Calendar.MONTH, 11);
			c.set(Calendar.DATE, 31);
		}
		return format.format(c.getTime());

	}
}
