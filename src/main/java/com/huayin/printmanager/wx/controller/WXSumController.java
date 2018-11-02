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
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.WXSumQueryType;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.wx.util.WxUtil;
import com.huayin.printmanager.wx.vo.FinanceDetailSumVo;
import com.huayin.printmanager.wx.vo.OutSourceDetailCheckVo;
import com.huayin.printmanager.wx.vo.PurchCheckDetailVo;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.SaleCheckDetailVo;
import com.huayin.printmanager.wx.vo.StockMaterialDetailVo;
import com.huayin.printmanager.wx.vo.StockProductDetailVo;
import com.huayin.printmanager.wx.vo.StockSumVo;
import com.huayin.printmanager.wx.vo.SupOrCustSumVo;

/**
 * <pre>
 * 微信 - 汇总
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/wx/sum")
public class WXSumController extends BaseController
{
	/**
	 * <pre>
	 * 数据 - 销售汇总
	 * </pre>
	 * @param request
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:54:37, think
	 */
	@RequestMapping(value = "saleSum")
	@ResponseBody
	public BigDecimal saleSum(HttpServletRequest request, @RequestBody QueryParam queryParam)
	{
		BigDecimal result = new BigDecimal(0);
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		if (queryParam.getwXSumQueryType() == WXSumQueryType.DAY)
		{
			queryParam.setDateMin(DateUtils.parseDate(DateUtils.getDate()));
			queryParam.setDateMax(DateUtils.parseDate(DateUtils.getDate()));
			queryParam.setBillType(BillType.SALE_SO);
			result = serviceFactory.getWXSumService().getTotalMoney(queryParam);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.MONTH)
		{
			Calendar cal = Calendar.getInstance();
			String dateMin = DateUtils.getYear() + "-" + DateUtils.getMonth() + "-01";
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			String dateMax = DateUtils.formatDate(cal.getTime());
			queryParam.setDateMin(DateUtils.parseDate(dateMin));
			queryParam.setDateMax(DateUtils.parseDate(dateMax));
			queryParam.setBillType(BillType.SALE_SO);
			result = serviceFactory.getWXSumService().getTotalMoney(queryParam);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD)
		{
			queryParam.setIsExpire(BoolValue.NO);
			queryParam.setBillType(BillType.SALE_SK);
			result = serviceFactory.getWXSumService().getReceiveMoney(queryParam);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.EXPIRE)
		{
			queryParam.setIsExpire(BoolValue.YES);
			queryParam.setBillType(BillType.SALE_SK);
			result = serviceFactory.getWXSumService().getReceiveMoney(queryParam);
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 采购汇总
	 * </pre>
	 * @param request
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:08:57, think
	 */
	@RequestMapping(value = "purchSum")
	@ResponseBody
	public BigDecimal purchSum(HttpServletRequest request, @RequestBody QueryParam queryParam)
	{
		BigDecimal result = new BigDecimal(0);
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		if (queryParam.getwXSumQueryType() == WXSumQueryType.DAY)
		{
			queryParam.setDateMin(DateUtils.parseDate(DateUtils.getDate()));
			queryParam.setDateMax(DateUtils.parseDate(DateUtils.getDate()));
			queryParam.setBillType(BillType.PURCH_PO);
			result = serviceFactory.getWXSumService().getTotalMoney(queryParam);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.MONTH)
		{
			Calendar cal = Calendar.getInstance();
			String dateMin = DateUtils.getYear() + "-" + DateUtils.getMonth() + "-01";
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			String dateMax = DateUtils.formatDate(cal.getTime());
			queryParam.setDateMin(DateUtils.parseDate(dateMin));
			queryParam.setDateMax(DateUtils.parseDate(dateMax));
			queryParam.setBillType(BillType.PURCH_PO);
			result = serviceFactory.getWXSumService().getTotalMoney(queryParam);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD)
		{
			queryParam.setIsExpire(BoolValue.NO);
			queryParam.setBillType(BillType.PURCH_PK);
			result = serviceFactory.getWXSumService().getPaymentMoney(queryParam);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.EXPIRE)
		{
			queryParam.setIsExpire(BoolValue.YES);
			queryParam.setBillType(BillType.PURCH_PK);
			result = serviceFactory.getWXSumService().getPaymentMoney(queryParam);
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 发外汇总
	 * </pre>
	 * @param request
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:09:09, think
	 */
	@RequestMapping(value = "outsourceSum")
	@ResponseBody
	public BigDecimal outsourceSum(HttpServletRequest request, @RequestBody QueryParam queryParam)
	{
		BigDecimal result = new BigDecimal(0);
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		if (queryParam.getwXSumQueryType() == WXSumQueryType.DAY)
		{
			queryParam.setDateMin(DateUtils.parseDate(DateUtils.getDate()));
			queryParam.setDateMax(DateUtils.parseDate(DateUtils.getDate()));
			queryParam.setBillType(BillType.OUTSOURCE_OP);
			result = serviceFactory.getWXSumService().getTotalMoney(queryParam);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.MONTH)
		{
			Calendar cal = Calendar.getInstance();
			String dateMin = DateUtils.getYear() + "-" + DateUtils.getMonth() + "-01";
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			String dateMax = DateUtils.formatDate(cal.getTime());
			queryParam.setDateMin(DateUtils.parseDate(dateMin));
			queryParam.setDateMax(DateUtils.parseDate(dateMax));
			queryParam.setBillType(BillType.OUTSOURCE_OP);
			result = serviceFactory.getWXSumService().getTotalMoney(queryParam);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD)
		{
			queryParam.setIsExpire(BoolValue.NO);
			queryParam.setBillType(BillType.OUTSOURCE_OC);
			result = serviceFactory.getWXSumService().getPaymentMoney(queryParam);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.EXPIRE)
		{
			queryParam.setIsExpire(BoolValue.YES);
			queryParam.setBillType(BillType.OUTSOURCE_OC);
			result = serviceFactory.getWXSumService().getPaymentMoney(queryParam);
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 销售客户汇总
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:09:19, think
	 */
	@RequestMapping(value = "saleCustomerSum")
	@ResponseBody
	public SearchResult<SupOrCustSumVo> saleCustomerSum(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		if (queryParam.getwXSumQueryType() == WXSumQueryType.DAY)
		{
			queryParam.setDateMin(new Date());
			queryParam.setDateMax(new Date());
			queryParam.setBillType(BillType.SALE_SO);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.MONTH)
		{
			Calendar cal = Calendar.getInstance();
			// 本月的第一天和最后一天
			String dateMin = DateUtils.getYear() + "-" + DateUtils.getMonth() + "-01";
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			String dateMax = DateUtils.formatDate(cal.getTime());
			queryParam.setDateMin(DateUtils.parseDate(dateMin));
			queryParam.setDateMax(DateUtils.parseDate(dateMax));
			queryParam.setBillType(BillType.SALE_SO);

		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD)
		{
			queryParam.setBillType(BillType.SALE_SK);
		}
		else
		{
			queryParam.setReconcilDate(new Date());
			queryParam.setBillType(BillType.SALE_SK);
		}
		SearchResult<SupOrCustSumVo> result = serviceFactory.getWXSumService().findSaleCusotmerSumByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 销售客户汇总明细
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:09:30, think
	 */
	@RequestMapping(value = "saleCustomerSumDetail")
	@ResponseBody
	public SearchResult<SaleCheckDetailVo> saleCustomerSumDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		if (queryParam.getwXSumQueryType() == WXSumQueryType.DAY)
		{
			queryParam.setDateMin(new Date());
			queryParam.setDateMax(new Date());
			queryParam.setBillType(BillType.SALE_SO);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.MONTH)
		{
			Calendar cal = Calendar.getInstance();
			// 本月的第一天和最后一天
			String dateMin = DateUtils.getYear() + "-" + DateUtils.getMonth() + "-01";
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			String dateMax = DateUtils.formatDate(cal.getTime());
			queryParam.setDateMin(DateUtils.parseDate(dateMin));
			queryParam.setDateMax(DateUtils.parseDate(dateMax));
			queryParam.setBillType(BillType.SALE_SO);

		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD)
		{
			queryParam.setBillType(BillType.SALE_SK);
		}
		else
		{
			queryParam.setReconcilDate(new Date());
			queryParam.setBillType(BillType.SALE_SK);
		}
		SearchResult<SaleCheckDetailVo> result = serviceFactory.getWXSumService().findSaleDetail(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 采购供应商汇总
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:09:41, think
	 */
	@RequestMapping(value = "purchSupplierSum")
	@ResponseBody
	public SearchResult<SupOrCustSumVo> purchSupplierSum(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		if (queryParam.getwXSumQueryType() == WXSumQueryType.DAY)
		{
			queryParam.setDateMin(new Date());
			queryParam.setDateMax(new Date());
			queryParam.setBillType(BillType.PURCH_PO);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.MONTH)
		{
			Calendar cal = Calendar.getInstance();
			// 本月的第一天和最后一天
			String dateMin = DateUtils.getYear() + "-" + DateUtils.getMonth() + "-01";
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			String dateMax = DateUtils.formatDate(cal.getTime());
			queryParam.setDateMin(DateUtils.parseDate(dateMin));
			queryParam.setDateMax(DateUtils.parseDate(dateMax));
			queryParam.setBillType(BillType.PURCH_PO);

		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD)
		{
			queryParam.setBillType(BillType.PURCH_PK);
		}
		else
		{
			queryParam.setReconcilDate(new Date());
			queryParam.setBillType(BillType.PURCH_PK);
		}
		SearchResult<SupOrCustSumVo> result = serviceFactory.getWXSumService().findPurOrOutSupSumByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 采购供应商汇总明细
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:09:51, think
	 */
	@RequestMapping(value = "purchSupplierSumDetail")
	@ResponseBody
	public SearchResult<PurchCheckDetailVo> purchSupplierSumDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		if (queryParam.getwXSumQueryType() == WXSumQueryType.DAY)
		{
			queryParam.setDateMin(new Date());
			queryParam.setDateMax(new Date());
			queryParam.setBillType(BillType.PURCH_PO);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.MONTH)
		{
			Calendar cal = Calendar.getInstance();
			// 本月的第一天和最后一天
			String dateMin = DateUtils.getYear() + "-" + DateUtils.getMonth() + "-01";
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			String dateMax = DateUtils.formatDate(cal.getTime());
			queryParam.setDateMin(DateUtils.parseDate(dateMin));
			queryParam.setDateMax(DateUtils.parseDate(dateMax));
			queryParam.setBillType(BillType.PURCH_PO);

		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD)
		{
			queryParam.setBillType(BillType.PURCH_PK);
		}
		else
		{
			queryParam.setReconcilDate(new Date());
			queryParam.setBillType(BillType.PURCH_PK);
		}
		SearchResult<PurchCheckDetailVo> result = serviceFactory.getWXSumService().findPurchDetail(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 发外加工商汇总
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:10:02, think
	 */
	@RequestMapping(value = "outsourceSupplierSum")
	@ResponseBody
	public SearchResult<SupOrCustSumVo> outsourceSupplierSum(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		if (queryParam.getwXSumQueryType() == WXSumQueryType.DAY)
		{
			queryParam.setDateMin(new Date());
			queryParam.setDateMax(new Date());
			queryParam.setBillType(BillType.OUTSOURCE_OP);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.MONTH)
		{
			Calendar cal = Calendar.getInstance();
			// 本月的第一天和最后一天
			String dateMin = DateUtils.getYear() + "-" + DateUtils.getMonth() + "-01";
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			String dateMax = DateUtils.formatDate(cal.getTime());
			queryParam.setDateMin(DateUtils.parseDate(dateMin));
			queryParam.setDateMax(DateUtils.parseDate(dateMax));
			queryParam.setBillType(BillType.OUTSOURCE_OP);

		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD)
		{
			queryParam.setBillType(BillType.OUTSOURCE_OC);
		}
		else
		{
			queryParam.setReconcilDate(new Date());
			queryParam.setBillType(BillType.OUTSOURCE_OC);
		}
		SearchResult<SupOrCustSumVo> result = serviceFactory.getWXSumService().findPurOrOutSupSumByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 发外加工商汇总明细
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:10:12, think
	 */
	@RequestMapping(value = "outsourceSupplierSumDetail")
	@ResponseBody
	public SearchResult<OutSourceDetailCheckVo> outsourceSupplierSumDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		if (queryParam.getwXSumQueryType() == WXSumQueryType.DAY)
		{
			queryParam.setDateMin(new Date());
			queryParam.setDateMax(new Date());
			queryParam.setBillType(BillType.OUTSOURCE_OP);
		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.MONTH)
		{
			Calendar cal = Calendar.getInstance();
			// 本月的第一天和最后一天
			String dateMin = DateUtils.getYear() + "-" + DateUtils.getMonth() + "-01";
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			String dateMax = DateUtils.formatDate(cal.getTime());
			queryParam.setDateMin(DateUtils.parseDate(dateMin));
			queryParam.setDateMax(DateUtils.parseDate(dateMax));
			queryParam.setBillType(BillType.OUTSOURCE_OP);

		}
		else if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD)
		{
			queryParam.setBillType(BillType.OUTSOURCE_OC);
		}
		else
		{
			queryParam.setReconcilDate(new Date());
			queryParam.setBillType(BillType.OUTSOURCE_OC);
		}
		SearchResult<OutSourceDetailCheckVo> result = serviceFactory.getWXSumService().findOutSourceDetail(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 财务收入汇总
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:10:23, think
	 */
	@RequestMapping(value = "financeReceiveSum")
	@ResponseBody
	public BigDecimal financeReceiveSum(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		BigDecimal receiveMoney = serviceFactory.getWXSumService().getSumReceiveMoney(queryParam);
		return receiveMoney;
	}

	/**
	 * <pre>
	 * 数据 - 财务支出汇总
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:10:39, think
	 */
	@RequestMapping(value = "financePaymentSum")
	@ResponseBody
	public BigDecimal financePaymentSum(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		BigDecimal paymentMoney = serviceFactory.getWXSumService().getSumPaymentMoney(queryParam);
		return paymentMoney;
	}

	/**
	 * <pre>
	 * 数据 - 财务收入明细
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:10:51, think
	 */
	@RequestMapping(value = "financeReceiveDetail")
	@ResponseBody
	public SearchResult<FinanceDetailSumVo> financeReceiveDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		queryParam.setBillType(BillType.FINANCE_REC);
		SearchResult<FinanceDetailSumVo> result = serviceFactory.getWXSumService().findFinanceSumDetail(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 财务支出明细
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:11:00, think
	 */
	@RequestMapping(value = "financePaymentDetail")
	@ResponseBody
	public SearchResult<FinanceDetailSumVo> financePaymentDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		queryParam.setBillType(BillType.FINANCE_PAY);
		SearchResult<FinanceDetailSumVo> result = serviceFactory.getWXSumService().findFinanceSumDetail(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 材料库存汇总
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:11:10, think
	 */
	@RequestMapping(value = "materialStockSum")
	@ResponseBody
	public StockSumVo materialStockSum(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		String companyId = request.getSession().getAttribute("companyId").toString();
		BigDecimal materialQty = serviceFactory.getWXSumService().getMaterialStockQty(companyId, queryParam.getSearchContent());
		BigDecimal materialMoney = serviceFactory.getWXSumService().getMaterialStockMoney(companyId, queryParam.getSearchContent());
		StockSumVo resultVo = new StockSumVo(materialQty, materialMoney);
		return resultVo;
	}

	/**
	 * <pre>
	 * 数据 - 成品库存汇总
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:11:20, think
	 */
	@RequestMapping(value = "productStockSum")
	@ResponseBody
	public StockSumVo productStockSum(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		String companyId = request.getSession().getAttribute("companyId").toString();
		Integer productQty = serviceFactory.getWXSumService().getProductStockQty(companyId, queryParam.getSearchContent());
		BigDecimal productMoney = serviceFactory.getWXSumService().getProductStockMoney(companyId, queryParam.getSearchContent());
		StockSumVo resultVo = new StockSumVo(new BigDecimal(productQty), productMoney);
		return resultVo;
	}

	/**
	 * <pre>
	 * 数据 - 成品库存
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:11:32, think
	 */
	@RequestMapping(value = "stockProductDetail")
	@ResponseBody
	public SearchResult<StockProductDetailVo> stockProductDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<StockProductDetailVo> result = serviceFactory.getWXSumService().findStockProductList(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 材料库存
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:11:45, think
	 */
	@RequestMapping(value = "stockMaterialDetail")
	@ResponseBody
	public SearchResult<StockMaterialDetailVo> stockMaterialDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<StockMaterialDetailVo> result = serviceFactory.getWXSumService().findStockMaterialList(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 汇总首页
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:11:59, think
	 */
	@RequestMapping(value = "view/index")
	public String indexView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		return "wx/sum/index";
	}

	/**
	 * <pre>
	 * 页面 - 财务汇总
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:12:09, think
	 */
	@RequestMapping(value = "view/financeSum")
	public String financeSumView(HttpServletRequest request, ModelMap map)
	{
		return "wx/sum/finance_sum";
	}

	/**
	 * <pre>
	 * 页面 - 采购汇总
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:12:19, think
	 */
	@RequestMapping(value = "view/purchSum")
	public String purchSumView(HttpServletRequest request, ModelMap map)
	{
		return "wx/sum/purch_sum";
	}

	/**
	 * <pre>
	 * 页面 - 采购汇总明细
	 * </pre>
	 * @param request
	 * @param map
	 * @param supplierId
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:12:30, think
	 */
	@RequestMapping(value = "view/purchSumDetail")
	public String purchSumDetailView(HttpServletRequest request, ModelMap map, Long supplierId, WXSumQueryType type)
	{
		map.put("supplierId", supplierId);
		map.put("type", type);
		return "wx/sum/detail/purch_detail";
	}

	/**
	 * <pre>
	 * 页面 - 发外汇总
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:12:40, think
	 */
	@RequestMapping(value = "view/outSourceSum")
	public String outSourceSumView(HttpServletRequest request, ModelMap map)
	{
		return "wx/sum/outsource_sum";
	}

	/**
	 * <pre>
	 * 页面 - 发外汇总明细
	 * </pre>
	 * @param request
	 * @param map
	 * @param supplierId
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:12:50, think
	 */
	@RequestMapping(value = "view/outSourceSumDetail")
	public String outSourceSumDetailView(HttpServletRequest request, ModelMap map, Long supplierId, WXSumQueryType type)
	{
		map.put("supplierId", supplierId);
		map.put("type", type);
		return "wx/sum/detail/outsource_detail";
	}

	/**
	 * <pre>
	 * 页面 - 销售汇总
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:13:02, think
	 */
	@RequestMapping(value = "view/saleSum")
	public String saleSum(HttpServletRequest request, ModelMap map)
	{
		return "wx/sum/sale_sum";
	}

	/**
	 * <pre>
	 * 页面 - 销售汇总明细
	 * </pre>
	 * @param request
	 * @param map
	 * @param customerId
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:13:13, think
	 */
	@RequestMapping(value = "view/saleSumDetail")
	public String saleSumDetailView(HttpServletRequest request, ModelMap map, Long customerId, WXSumQueryType type)
	{
		map.put("customerId", customerId);
		map.put("type", type);
		return "wx/sum/detail/sale_detail";
	}

	/**
	 * <pre>
	 * 页面 - 库存汇总
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:13:25, think
	 */
	@RequestMapping(value = "view/stockSum")
	public String stockSumView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		return "wx/sum/stock_sum";
	}

}
