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
import java.math.RoundingMode;
import java.util.ArrayList;
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

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcil;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 - 销售对账
 * </pre>
 * @author       zhengby
 * @version 		 1.0, 2016年6月16日
 * @version 	   2.0, 2018年2月22日下午6:04:18, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/sale/reconcil")
public class SaleReconcilController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 销售对账单列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:04:54, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sale:reconcil:list")
	public String list()
	{
		return "sale/reconcil/list";
	}
	
	/**
	 * <pre>
	 * 页面 - 销售对账单明细列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:05:34, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("sale:reconcil_detail:list")
	public String detailList()
	{
		return "sale/reconcil/detail_list";
	}
	
	/**
	 * <pre>
	 * 页面 - 销售对账单查看
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:06:06, zhengby
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	@RequiresPermissions("sale:reconcil:list")
	public SaleReconcil view(@PathVariable Long id)
	{
		SaleReconcil order = serviceFactory.getSaleReconcilService().get(id);
	    return order;
	}
	
	/**
	 * <pre>
	 * 功能  - 返回打印数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:06:16, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("sale:reconcil:print")
	public Map<String,Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map=null;
		try
		{
			SaleReconcil reconcil = serviceFactory.getSaleReconcilService().get(id);
			map = ObjectUtils.objectToMap(reconcil);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());
			Integer totalQty = 0 ;
			for (SaleReconcilDetail detail : reconcil.getDetailList())
			{
				totalQty += detail.getQty();
			}
			map.put("totalQty", totalQty);
		}
		catch (Exception e)
		{
			logger.error("查询销售对账打印数据发生业务逻辑异常!",e);
		}
		return map;
	}

	/**
	 * <pre>
	 * 页面  - 跳转到创建对账单页面
	 * </pre>
	 * @param customerId
	 * @param deliverIds
	 * @param returnIds
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:06:27, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("sale:reconcil:create")
	public String create(Long customerId, Long[] deliverIds, Long[] returnIds, ModelMap map)
	{
		Customer customer = serviceFactory.getCustomerService().get(customerId);

		SaleReconcil order = new SaleReconcil();
		List<SaleReconcilDetail> detailList = new ArrayList<SaleReconcilDetail>();
		order.setDetailList(detailList);
		if(null!=deliverIds){
			for(Long id : deliverIds){
				SaleDeliverDetail bean = serviceFactory.getSaleDeliverService().getDetail(id);
				if(bean.getQty() <= bean.getReconcilQty()){
					continue;
				}
				SaleReconcilDetail detail = new SaleReconcilDetail();
				detail.setSourceBillType(bean.getMaster().getBillType());
				detail.setSourceQty(bean.getQty());
				detail.setSourceBillNo(bean.getMaster().getBillNo());
				detail.setSourceId(bean.getMasterId());
				detail.setSourceDetailId(bean.getId());
				detail.setSaleOrderBillNo(bean.getSaleOrderBillNo());
				detail.setProductName(bean.getProductName());
				detail.setStyle(bean.getStyle());
				detail.setProductId(bean.getProductId());
				detail.setProductCode(bean.getProductCode());
				detail.setSaleOrderId(bean.getSourceId());
				detail.setCustomerMaterialCode(bean.getCustomerMaterialCode());
				detail.setCustomerBillNo(bean.getCustomerBillNo());
				detail.setUnitId(bean.getUnitId());
				detail.setCustRequire(bean.getCustRequire());
				detail.setQty(bean.getQty()-bean.getReconcilQty());
				detail.setSpareQty(bean.getSpareQty());
				detail.setMoney(bean.getMoney().subtract(bean.getReconcilMoney()));
				detail.setPrice(detail.getMoney().divide(new BigDecimal(detail.getQty()),4,RoundingMode.HALF_UP));
				detail.setTax(bean.getTax());
				detail.setTaxRateId(bean.getTaxRateId());
				detail.setPercent(bean.getPercent());
				detail.setNoTaxPrice(bean.getNoTaxPrice());
				detail.setNoTaxMoney(bean.getNoTaxMoney());
				detail.setDeliveryTime(bean.getMaster().getDeliveryTime());
				detail.setMemo(bean.getMemo());
				detail.setReceiveMoney(new BigDecimal(0));
				detail.setImgUrl(bean.getImgUrl());
				detailList.add(detail);
			}
		}
		if(null!=returnIds){
			for(Long id : returnIds){
				SaleReturnDetail bean = serviceFactory.getSaleReturnService().getDetail(id);
				if (bean.getQty() <= bean.getReconcilQty())
				{
					continue; 
				}
				SaleReconcilDetail detail = new SaleReconcilDetail();
				detail.setSourceBillType(bean.getMaster().getBillType());
				detail.setSourceQty(bean.getQty());
				detail.setSourceBillNo(bean.getMaster().getBillNo());
				detail.setSourceId(bean.getMasterId());
				detail.setSourceDetailId(bean.getId());
				detail.setSaleOrderBillNo(bean.getSaleOrderBillNo());
				detail.setSaleOrderId(bean.getSaleOrderId());
				detail.setProductName(bean.getProductName());
				detail.setStyle(bean.getStyle());
				detail.setProductId(bean.getProductId());
				detail.setProductCode(bean.getProductCode());
				detail.setCustomerMaterialCode(bean.getCustomerMaterialCode());
				detail.setCustomerBillNo(bean.getCustomerBillNo());
				detail.setUnitId(bean.getUnitId());
				detail.setCustRequire(bean.getCustRequire());
				detail.setQty(bean.getQty()-bean.getReconcilQty());
				detail.setSpareQty(bean.getSpareQty());
				detail.setMoney(bean.getMoney().subtract(bean.getReconcilMoney()));
				detail.setPrice(detail.getMoney().divide(new BigDecimal(detail.getQty()),4,RoundingMode.HALF_UP));
				detail.setTax(bean.getTax());
				detail.setTaxRateId(bean.getTaxRateId());
				detail.setPercent(bean.getPercent());
				detail.setNoTaxPrice(bean.getNoTaxPrice());
				detail.setNoTaxMoney(bean.getNoTaxMoney());
				detail.setDeliveryTime(bean.getMaster().getCreateTime());
				detail.setMemo(bean.getMemo());
				detail.setImgUrl(bean.getImgUrl());
				detailList.add(detail);
			}
		}
		map.put("order", order);
		if(CollectionUtils.isNotEmpty(order.getDetailList())){
			map.put("customer", customer);
		}
		return "sale/reconcil/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存销售对账单
	 * </pre>
	 * @param order
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:07:23, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("sale:reconcil:create")
	@SystemControllerLog(SystemLogType=SystemLogType.BUSINESS,BillTypeText="销售对账",Operation=Operation.ADD)
	public AjaxResponseBody save(@RequestBody SaleReconcil order, ModelMap map)
	{
		// 保存之前验证是否已对完账，前端的已校验过，后台再校验一次，防止保存重复对账单
		for (SaleReconcilDetail detail : order.getDetailList())
		{
			if (null == detail.getSourceDetailId())
			{
				continue;
			}
			if (detail.getSourceBillType() == BillType.SALE_IV)
			{
				SaleDeliverDetail deliver = serviceFactory.getSaleDeliverService().getDetail(detail.getSourceDetailId());
				if (deliver.getReconcilQty() >= deliver.getQty())
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
			if (detail.getSourceBillType() == BillType.SALE_IR)
			{
				SaleReturnDetail re = serviceFactory.getSaleReturnService().getDetail(detail.getSourceDetailId());
				if (re.getReconcilQty() >= re.getQty())
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
		}
		try
		{
			serviceFactory.getSaleReconcilService().save(order);
			return returnSuccessBody(order);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			logger.error(ex.getMessage(),"SaleReconcil:"+JsonUtils.toJson(order));
			return returnErrorBody(ex);
		}
	}

	/**
	 * <pre>
	 * 页面 - 销售对账单编辑 
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:08:01, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("sale:reconcil:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		SaleReconcil order = serviceFactory.getSaleReconcilService().get(id);
		Customer customer = serviceFactory.getCustomerService().get(order.getCustomerId());
		map.put("customer", customer);
		map.put("order", order);
		return "sale/reconcil/edit";
	}
	
	/**
	 * <pre>
	 * 功能 - 更新销售对账单
	 * </pre>
	 * @param order
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:08:35, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sale:reconcil:edit")
	@SystemControllerLog(SystemLogType=SystemLogType.BUSINESS,BillTypeText="销售对账",Operation=Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody SaleReconcil order, ModelMap map)
	{
		serviceFactory.getSaleReconcilService().update(order);
		// 更新旧数据
		SaleReconcil new_order = serviceFactory.getSaleReconcilService().get(order.getId());

		return returnSuccessBody(new_order);
	}
	
	/**
	 * <pre>
	 * 功能 - 审核销售对账单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:09:04, zhengby
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("sale:reconcil:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		if (serviceFactory.getCommonService().audit(BillType.SALE_SK, id, BoolValue.YES))
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
	 * 功能 - 反审核销售对账单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:11:10, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "audit_cancel/{id}")
	@ResponseBody
	@RequiresPermissions("sale:reconcil:audit_cancel")
	public AjaxResponseBody auditCancel(@PathVariable Long id)
	{
		if (serviceFactory.getCommonService().audit(BillType.SALE_SK, id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.SALE_SK, id);
			return returnErrorBody(map);
		}
	}

	/**
	 * <pre>
	 * 功能 - 强制完工销售对账单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids 表ID
	 * @param deliverIds
	 * @param returnIds
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:11:22, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("sale:reconcil:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam(required = false, name = "ids[]") Long[] ids,
			@RequestParam(required = false, name = "deliverIds[]") Long[] deliverIds,
			@RequestParam(required = false, name = "returnIds[]") Long[] returnIds)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType))
		{
			return returnErrorBody("提交数据不完整");
		}
		if (serviceFactory.getSaleReconcilService().forceComplete(tableType, ids, deliverIds, returnIds, BoolValue.YES))
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
	 * 功能 - 取消强制完工销售对账单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids
	 * @param deliverIds
	 * @param returnIds
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:12:00, zhengby
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("sale:reconcil:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam(required = false, name = "ids[]") Long[] ids,
			@RequestParam(required = false, name = "deliverIds[]") Long[] deliverIds,
			@RequestParam(required = false, name = "returnIds[]") Long[] returnIds)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType))
		{
			return returnErrorBody("提交数据不完整");
		}
		if (serviceFactory.getSaleReconcilService().forceComplete(tableType, ids, deliverIds, returnIds, BoolValue.NO))
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
	 * 功能 - 删除销售对账单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:13:39, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("sale:reconcil:del")
	@SystemControllerLog(SystemLogType=SystemLogType.BUSINESS,BillTypeText="销售对账",Operation=Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		SaleReconcil order = serviceFactory.getSaleReconcilService().get(id);
		if (order.getIsCheck() == BoolValue.YES)
		{// 已审核的不允许删除
			return returnErrorBody("已审核的不允许删除");
		}
		serviceFactory.getSaleReconcilService().delete(id);
		return returnSuccessBody();
	}
	
	/**
	 * <pre>
	 * 功能 - 获取销售对账单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:13:57, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<SaleReconcil> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SaleReconcil> result = serviceFactory.getSaleReconcilService().findByCondition(queryParam);
		SaleReconcil reconcil=new SaleReconcil();
		BigDecimal totalMoney=new BigDecimal(0);
		BigDecimal totalTax=new BigDecimal(0);
		BigDecimal noTaxTotalMoney=new BigDecimal(0);
		for (SaleReconcil saleReconcil : result.getResult()) {
			totalMoney=totalMoney.add(saleReconcil.getTotalMoney());
			totalTax=totalTax.add(saleReconcil.getTotalTax());
			noTaxTotalMoney= noTaxTotalMoney.add(saleReconcil.getNoTaxTotalMoney());
		}
		reconcil.setTotalMoney(totalMoney);
		reconcil.setTotalTax(totalTax);
		reconcil.setNoTaxTotalMoney(noTaxTotalMoney);
		reconcil.setIsCheck(null);
		result.getResult().add(reconcil);
		return result;
	}
	
	/**
	 * <pre>
	 * 功能 - 获取销售对账单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:15:01, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<SaleReconcilDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SaleReconcilDetail> result = serviceFactory.getSaleReconcilService().findDetailByCondition(queryParam);
		SaleReconcilDetail detail=new SaleReconcilDetail();
		BigDecimal money=new BigDecimal(0);
		BigDecimal tax=new BigDecimal(0);
		BigDecimal noTaxMoney=new BigDecimal(0);
		BigDecimal recieve=new BigDecimal(0);//付款
		BigDecimal price=new BigDecimal(0);//单价
		Integer spareQty = new Integer(0);//备品数量
		Integer totalQty = new Integer(0);
		for (SaleReconcilDetail saleReconcilDetail : result.getResult()) {
			money=money.add(saleReconcilDetail.getMoney());
			tax=tax.add(saleReconcilDetail.getTax());
			noTaxMoney= noTaxMoney.add(saleReconcilDetail.getNoTaxMoney());
			recieve=recieve.add(saleReconcilDetail.getReceiveMoney());
			price=price.add(saleReconcilDetail.getPrice());
			spareQty+=saleReconcilDetail.getSpareQty();
			totalQty += saleReconcilDetail.getQty();
		}
		SaleReconcil reconcil=new SaleReconcil();
		detail.setMaster(reconcil);
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setPrice(price);
		detail.setSpareQty(spareQty);
		detail.setReceiveMoney(recieve);
		detail.setQty(totalQty);
		result.getResult().add(detail);
		return result;
	}
}
