/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:14:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.sale;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReturn;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 - 销售退货
 * </pre>
 * @author       zhengby
 * @version 		 1.0, 2016年6月16日
 * @version 	   2.0, 2018年2月22日下午6:25:55, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/sale/return")
public class SaleReturnController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 销售退货单列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:28:38, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sale:return:list")
	public String list()
	{
		return "sale/return/list";
	}
	
	/**
	 * <pre>
	 * 页面 - 销售退货单明细列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:29:01, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("sale:return_detail:list")
	public String detailList()
	{
		return "sale/return/detail_list";
	}
	
	/**
	 * <pre>
	 * 页面 - 销售退货单创建
	 * </pre>
	 * @param customerId
	 * @param billNo
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:29:15, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("sale:return:create")
	public String create(Long customerId, String billNo,ModelMap map)
	{
		if(customerId != null){
			QueryParam queryParam = new QueryParam();
			queryParam.setId(customerId);
			queryParam.setBillNo(billNo);
			SearchResult<SaleDeliverDetail> result = serviceFactory.getSaleDeliverService().findDetailByConditions(queryParam);
			SearchResult<Customer> res = serviceFactory.getCustomerService().quickFindByCondition(queryParam);
			if(CollectionUtils.isEmpty(result.getResult())){
				return "sale/return/create";
			}
			String customerJSON = JsonUtils.toJson(res.getResult().get(0));
			String deliverDetail = JsonUtils.toJson(result.getResult());
		
			map.put("customerJSON", customerJSON);
			map.put("deliverDetailJSON",deliverDetail);
		}
		return "sale/return/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存销售退货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:29:34, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("sale:return:create")
	@SystemControllerLog(SystemLogType=SystemLogType.BUSINESS,BillTypeText="销售退货",Operation=Operation.ADD)
	public ServiceResult<SaleReturn> save(@RequestBody SaleReturn order,
			HttpServletRequest request)
	{
		ServiceResult<SaleReturn> serviceResult = new ServiceResult<SaleReturn>();
		serviceResult.setReturnValue(order);
		try
		{
			serviceFactory.getSaleReturnService().save(order);
			serviceResult.setReturnObject(true);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
		}
		catch (Exception e)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT,OperationResult.FAILE);
			e.printStackTrace();
			serviceResult.setReturnObject(false);
			serviceResult.setIsSuccess(false);
			logger.error(e.getMessage(), "SaleReturn:"+JsonUtils.toJson(order));
		}
		return serviceResult;
	}

	/**
	 * <pre>
	 * 功能 - 查看销售退货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:30:05, zhengby
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	@RequiresPermissions("sale:return:list")
	public SaleReturn view(@PathVariable Long id)
	{
		SaleReturn order = serviceFactory.getSaleReturnService().get(id);
		return order;
	}
	
	/**
	 * <pre>
	 * 功能 - 销售对账单打印
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:31:23, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("sale:return:print")
	public Map<String,Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map=null;
		try
		{
			SaleReturn order = serviceFactory.getSaleReturnService().get(id);
			map = ObjectUtils.objectToMap(order);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());
			map.put("printDate", DateUtils.formatDateTime(new Date()));
		}
		catch (Exception e)
		{
			logger.error("查询销售对账打印数据发生业务逻辑异常!",e);
		}
		return map;
	}

	/**
	 * <pre>
	 * 页面 - 销售退货单编辑 
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:31:50, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("sale:return:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		SaleReturn order = serviceFactory.getSaleReturnService().get(id);
		Customer customer = serviceFactory.getCustomerService().get(order.getCustomerId());
		map.put("customer", customer);
		map.put("order", order);
		return "sale/return/edit";
	}

	/**
	 * <pre>
	 * 功能 - 更新销售退货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:32:34, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sale:return:edit")
	@SystemControllerLog(SystemLogType=SystemLogType.BUSINESS,BillTypeText="销售退货",Operation=Operation.UPDATE)
	public ServiceResult<SaleReturn> update(@RequestBody SaleReturn order,
			HttpServletRequest request)
	{
		ServiceResult<SaleReturn> serviceResult = new ServiceResult<SaleReturn>();
		serviceResult.setReturnValue(order);
		try
		{
			serviceFactory.getSaleReturnService().update(order);
			serviceResult.setReturnObject(true);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
		}
		catch (BusinessException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT,OperationResult.FAILE);
			serviceResult.setReturnObject(false);
		}

		return serviceResult;
	}

	/**
	 * <pre>
	 * 功能 - 审核销售退货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:33:09, zhengby
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("sale:return:audit")
	public AjaxResponseBody check(@PathVariable Long id)
	{
		serviceFactory.getSaleReturnService().check(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 反审核销售退货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:33:34, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("sale:return:audit_cancel")
	public AjaxResponseBody checkBack(@PathVariable Long id)
	{
		List<StockProduct> list = serviceFactory.getSaleReturnService().checkBack(id, BoolValue.NO);
		if (list != null)
		{
			if (list.size() > 0)
			{
				return returnSuccessBody(list);
			}
			else
			{
				return returnSuccessBody();
			}
		}
		else
		{
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.SALE_IR, id);
			return returnErrorBody(map);
		}
	}
	
	/**
	 * <pre>
	 * 功能 - 强制反审核销售退货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月19日 下午3:58:59, think
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "forceCheckBack/{id}")
	@ResponseBody
	@RequiresPermissions("sale:return:audit_cancel")
	public AjaxResponseBody forceCheckBack(@PathVariable Long id)
	{
		List<StockProduct> list = serviceFactory.getSaleReturnService().checkBack(id, BoolValue.YES);
		if (list != null)
		{
			if (list.size() > 0)
			{
				return returnSuccessBody(list);
			}
			else
			{
				return returnSuccessBody();
			}
		}
		else
		{
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.SALE_IR, id);
			return returnErrorBody(map);
		}
	}

	/**
	 * <pre>
	 * 功能 - 强制完工销售退货单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param id 表ID
	 * @param flag 完工标记
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:34:38, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("sale:return:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? SaleReturn.class
				: SaleReturnDetail.class;
		if (serviceFactory.getCommonService().forceComplete(cla, ids, BoolValue.YES))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 取消强制完工销售送货单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param id 表ID
	 * @param flag 完工标记
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:35:17, zhengby
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("sale:return:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? SaleReturn.class
				: SaleReturnDetail.class;
		if (serviceFactory.getCommonService().forceComplete(cla, ids, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 删除销售退货单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:35:50, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("sale:return:del")
	@SystemControllerLog(SystemLogType=SystemLogType.BUSINESS,BillTypeText="销售退货",Operation=Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id,
			HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		try
		{

			SaleReturn order = serviceFactory.getSaleReturnService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT,OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getSaleReturnService().delete(id);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody();
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT,OperationResult.FAILE);
			ex.printStackTrace();
			return returnErrorBody(ex);
		}
	}

	/**
	 * <pre>
	 * 功能 - 获取销售退货单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:36:18, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<SaleReturn> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SaleReturn> result = serviceFactory.getSaleReturnService().findByCondition(queryParam);
		SaleReturn _return =new SaleReturn();
		BigDecimal totalMoney=new BigDecimal(0);
		BigDecimal totalTax=new BigDecimal(0);
		BigDecimal noTaxTotalMoney=new BigDecimal(0);
		for (SaleReturn saleReturn : result.getResult()) {
			totalMoney=totalMoney.add(saleReturn.getTotalMoney());
			totalTax=totalTax.add(saleReturn.getTotalTax());
			noTaxTotalMoney= noTaxTotalMoney.add(saleReturn.getNoTaxTotalMoney());
		}
		_return.setTotalMoney(totalMoney);
		_return.setTotalTax(totalTax);
		_return.setNoTaxTotalMoney(noTaxTotalMoney);
		_return.setIsCheck(null);
		result.getResult().add(_return);
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 获取销售退货单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:36:53, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<SaleReturnDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SaleReturnDetail> result = serviceFactory.getSaleReturnService()
				.findDetailByCondition(queryParam);
		SaleReturnDetail detail=new SaleReturnDetail();
		BigDecimal money=new BigDecimal(0);
		BigDecimal tax=new BigDecimal(0);
		BigDecimal noTaxMoney=new BigDecimal(0);
		BigDecimal price = new BigDecimal(0);//单价
		Integer reconcilQty = new Integer(0);//已对账数
		Integer spareQty = new Integer(0);//备品数量
		Integer totalQty = new Integer(0);
		for (SaleReturnDetail saleReturnDetail : result.getResult()) {
			money=money.add(saleReturnDetail.getMoney());
			tax=tax.add(saleReturnDetail.getTax());
			noTaxMoney= noTaxMoney.add(saleReturnDetail.getNoTaxMoney());
			price = price.add(saleReturnDetail.getPrice());
			reconcilQty += saleReturnDetail.getReconcilQty();
			spareQty += saleReturnDetail.getSpareQty();
			totalQty += saleReturnDetail.getQty();
		}
		SaleReturn saleReturn =new SaleReturn();
		detail.setMaster(saleReturn);
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setPrice(price);
		detail.setReconcilQty(reconcilQty);
		detail.setSpareQty(spareQty);
		detail.setQty(totalQty);
		result.getResult().add(detail);
		return result;
	}
}
