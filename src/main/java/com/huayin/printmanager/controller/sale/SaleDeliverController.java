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

import com.google.common.collect.Lists;
import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.sale.SaleDeliver;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
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
 * 销售管理 - 销售送货
 * </pre>
 * @author       zhengby
 * @version 		 1.0, 2016年6月16日
 * @version 	   2.0, 2018年2月22日下午5:33:56, zhengby, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/sale/deliver")
public class SaleDeliverController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 销售送货单列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:40:04, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sale:deliver:list")
	public String list()
	{
		return "sale/deliver/list";
	}

	/**
	 * <pre>
	 * 页面 - 销售送货单明细列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:40:30, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("sale:deliver_detail:list")
	public String detailList()
	{
		return "sale/deliver/detail_list";
	}

	/**
	 * <pre>
	 * 页面 - 销售送货单查看
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:41:00, zhengby
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	@RequiresPermissions("sale:deliver:list")
	public Map<String, Object> viewAjax(@PathVariable Long id)
	{
		Map<String, Object> mapData = null;
		try
		{
			SaleDeliver order = serviceFactory.getSaleDeliverService().get(id);
			mapData = ObjectUtils.objectToMap(order);
			mapData.put("printDate", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
			mapData.put("companyName", UserUtils.getCompany().getName());
			mapData.put("companyAddress", UserUtils.getCompany().getAddress());
			mapData.put("companyFax", UserUtils.getCompany().getFax());
			mapData.put("companyLinkName", UserUtils.getCompany().getLinkName());
			mapData.put("companyTel", UserUtils.getCompany().getTel());
			mapData.put("companyEmail", UserUtils.getCompany().getEmail());
			Integer totalQty = 0;
			for (SaleDeliverDetail detail : order.getDetailList())
			{
				totalQty += detail.getQty();
			}
			mapData.put("totalQty", totalQty);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mapData;
	}

	/**
	 * <pre>
	 * 页面 - 销售送货单创建
	 * </pre>
	 * @param idsBillNos
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:41:22, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("sale:deliver:create")
	public String create(String[] idsBillNos, ModelMap map)
	{
		List<Long> MOlist = Lists.newArrayList(); // 生产工单idList
		List<Long> SOlist = Lists.newArrayList(); // 销售订单idList
		try
		{
			for (String idBillNo : idsBillNos)
			{
				if ("MO".equals(idBillNo.substring(0, 2)))
				{
					MOlist.add(Long.valueOf(idBillNo.split("_")[1]));
				}
				if ("SO".equals(idBillNo.substring(0, 2)))
				{
					SOlist.add(Long.valueOf(idBillNo.split("_")[1]));
				}
			}
			SaleDeliver order = new SaleDeliver();
			Customer customer = null;
			List<SaleDeliverDetail> detailList = new ArrayList<SaleDeliverDetail>();
			order.setDetailList(detailList);
			if (CollectionUtils.isNotEmpty(MOlist))
			{
				customer = _deliverCreateByProduce(order, detailList, MOlist);
			}
			if (CollectionUtils.isNotEmpty(SOlist))
			{
				customer = _deliverCreateBySaleOrder(order, detailList, SOlist);
			}
			map.put("order", order);
			map.put("customer", customer);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "sale/deliver/create";
	}

	/**
	 * <pre>
	 * 功能 - 销售订单转成送货单
	 * </pre>
	 * @param order
	 * @param detailList
	 * @param customer
	 * @param SOlist
	 * @since 1.0, 2018年1月17日 下午4:16:55, zhengby
	 */
	private Customer _deliverCreateBySaleOrder(SaleDeliver order, List<SaleDeliverDetail> detailList, List<Long> SOlist)
	{
		Customer customer = null;
		if (CollectionUtils.isNotEmpty(SOlist))
		{
			for (Long id : SOlist)
			{
				SaleOrderDetail bean = serviceFactory.getSaleOrderService().getDetailHasMaster(id);

				if (bean.getQty() <= bean.getDeliverQty())
				{
					continue;
				}
				customer = serviceFactory.getCustomerService().get(bean.getMaster().getCustomerId());
				if (order.getDeliveryClassId() == null)
				{
					order.setDeliveryTime(bean.getDeliveryTime());
					order.setDeliveryClassId(bean.getMaster().getDeliveryClassId());
					order.setSettlementClassId(bean.getMaster().getSettlementClassId());
					order.setCurrencyType(bean.getMaster().getCurrencyType());
					order.setEmployeeId(bean.getMaster().getEmployeeId());
					order.setLinkName(bean.getMaster().getLinkName());
					order.setMobile(bean.getMaster().getMobile());
					order.setDeliveryAddress(bean.getMaster().getDeliveryAddress());
				}
				SaleDeliverDetail detail = new SaleDeliverDetail();
				detail.setSourceBillType(bean.getMaster().getBillType());
				detail.setSourceQty(bean.getQty());
				detail.setSourceBillNo(bean.getMaster().getBillNo());
				detail.setSourceId(bean.getMasterId());
				detail.setSourceDetailId(bean.getId());
				detail.setSaleOrderBillNo(bean.getSourceBillNo());
				detail.setProductName(bean.getProductName());
				detail.setStyle(bean.getStyle());
				detail.setProductId(bean.getProductId());
				detail.setProductCode(bean.getProductCode());
				detail.setCustomerMaterialCode(bean.getCustomerMaterialCode());
				detail.setCustomerBillNo(bean.getMaster().getCustomerBillNo());
				detail.setUnitId(bean.getUnitId());
				detail.setCustRequire(bean.getCustRequire());
				detail.setQty(bean.getQty() - bean.getDeliverQty());
				detail.setSpareQty(bean.getSpareQty() - bean.getDeliverSpareedQty());
				detail.setImgUrl(bean.getImgUrl());

				if ((bean.getMoney().subtract(bean.getDeliverMoney())).doubleValue() < 0)
				{
					BigDecimal deliverPrice = bean.getDeliverMoney().divide(new BigDecimal(bean.getDeliverQty()), 4, RoundingMode.HALF_UP);
					detail.setMoney(deliverPrice.multiply(new BigDecimal(detail.getQty())));
				}
				else
				{
					detail.setMoney(bean.getMoney().subtract(bean.getDeliverMoney()));
				}
				detail.setPrice(detail.getMoney().divide(new BigDecimal(detail.getQty()), 4, RoundingMode.HALF_UP));
				detail.setTax(bean.getTax());
				detail.setTaxRateId(bean.getTaxRateId());
				detail.setPercent(bean.getPercent());
				detail.setNoTaxPrice(bean.getNoTaxPrice());
				detail.setNoTaxMoney(bean.getNoTaxMoney());
				detail.setMemo(bean.getMemo());

				detailList.add(detail);
			}
		}
		return customer;
	}

	/**
	 * <pre>
	 * 功能 - 工单转成送货单
	 * </pre>
	 * @param order
	 * @param detailList
	 * @param customer
	 * @param MOlist
	 * @since 1.0, 2018年1月17日 下午4:16:49, zhengby
	 */
	private Customer _deliverCreateByProduce(SaleDeliver order, List<SaleDeliverDetail> detailList, List<Long> MOlist)
	{
		Customer customer = null;
		if (CollectionUtils.isNotEmpty(MOlist))
		{
			for (Long id : MOlist)
			{

				WorkProduct bean = serviceFactory.getWorkService().getProduct(id);
				if (bean.getSaleProduceQty() <= bean.getDeliverQty())
				{
					continue;
				}
				customer = serviceFactory.getCustomerService().get(bean.getCustomerId());

				if (order.getDeliveryClassId() == null)
				{
					order.setDeliveryTime(bean.getDeliveryTime());
					order.setDeliveryClassId(customer.getDeliveryClassId());
					order.setSettlementClassId(customer.getSettlementClassId());
					order.setCurrencyType(customer.getCurrencyType());
					order.setEmployeeId(customer.getEmployeeId());
					if (customer.getDefaultAddress() != null)
					{
						order.setLinkName(customer.getDefaultAddress().getUserName());
						order.setMobile(customer.getDefaultAddress().getMobile());
						order.setDeliveryAddress(customer.getDefaultAddress().getAddress());
					}
				}
				SaleDeliverDetail detail = new SaleDeliverDetail();
				detail.setSourceBillType(bean.getMaster().getBillType());
				detail.setSourceQty(bean.getSourceQty());
				detail.setSourceBillNo(bean.getMaster().getBillNo());
				detail.setSourceId(bean.getMasterId());
				detail.setSourceDetailId(bean.getId());
				detail.setSaleOrderBillNo(bean.getSourceBillNo());
				detail.setProductName(bean.getProductName());
				detail.setStyle(bean.getStyle());
				detail.setProductId(bean.getProductId());
				detail.setProductCode(bean.getProductCode());
				detail.setCustomerMaterialCode(bean.getCustomerMaterialCode());
				detail.setCustomerBillNo(bean.getCustomerBillNo());
				detail.setUnitId(bean.getUnitId());
				detail.setCustRequire(bean.getCustomerRequire());
				detail.setQty(bean.getSaleProduceQty() - bean.getDeliverQty());
				detail.setSpareQty(bean.getSpareProduceQty() - bean.getDeliverSpareedQty());
				detail.setImgUrl(bean.getImgUrl());

				if (bean.getMoney() != null)
				{
					if ((bean.getMoney().subtract(bean.getDeliverMoney())).doubleValue() < 0)
					{
						BigDecimal deliverPrice = bean.getDeliverMoney().divide(new BigDecimal(bean.getDeliverQty()), 4, RoundingMode.HALF_UP);
						detail.setMoney(deliverPrice.multiply(new BigDecimal(detail.getQty())));
					}
					else
					{
						detail.setMoney(bean.getMoney().subtract(bean.getDeliverMoney()));
					}

					detail.setPrice(detail.getMoney().divide(new BigDecimal(detail.getQty()), 4, RoundingMode.HALF_UP));
				}

				detail.setTax(bean.getTax());
				detail.setTaxRateId(bean.getTaxRateId());
				detail.setPercent(bean.getPercent());
				detail.setNoTaxPrice(bean.getNoTaxPrice());
				detail.setNoTaxMoney(bean.getNoTaxMoney());
				detail.setMemo(bean.getMemo());

				detailList.add(detail);
			}
		}
		return customer;
	}

	/**
	 * <pre>
	 * 功能 - 保存销售送货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:44:43, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("sale:deliver:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "销售送货", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody SaleDeliver order, HttpServletRequest request)
	{
		// 保存之前验证是否已送完货，前端的已校验过，后台再校验一次，防止保存重复送货单
		for (SaleDeliverDetail detail : order.getDetailList())
		{
			if (null == detail.getSourceDetailId())
			{
				continue;
			}
			if (detail.getSourceBillType() == BillType.PRODUCE_MO)
			{
				WorkProduct product = serviceFactory.getWorkService().getProduct(detail.getSourceDetailId());
				if (product.getDeliverQty() >= product.getSaleProduceQty())
				{
					// 已送货数量 >= 销售数量
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
			if (detail.getSourceBillType() == BillType.SALE_SO)
			{
				SaleOrderDetail saleOrderDetail = serviceFactory.getSaleOrderService().getDetail(detail.getSourceDetailId());
				if (saleOrderDetail.getDeliverQty() >= saleOrderDetail.getQty())
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
		}
		try
		{
			ServiceResult<SaleDeliver> result = serviceFactory.getSaleDeliverService().save(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(result);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return returnErrorBody(e);
		}
	}

	/**
	 * <pre>
	 * 页面 - 销售送货单打印
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:44:34, zhengby
	 */
	@RequestMapping(value = "print/{id}")
	@RequiresPermissions("sale:deliver:print")
	public String print(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		SaleDeliver order = serviceFactory.getSaleDeliverService().get(id);
		map.put("order", order);
		map.put("printDate", new Date());
		return "sale/deliver/print";
	}

	/**
	 * <pre>
	 * 页面- 销售送货单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:44:24, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("sale:deliver:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		SaleDeliver order = serviceFactory.getSaleDeliverService().get(id);
		Customer customer = serviceFactory.getCustomerService().get(order.getCustomerId());
		map.put("customer", customer);
		map.put("order", order);
		return "sale/deliver/edit";
	}

	/**
	 * <pre>
	 * 功能 - 更新销售送货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:44:16, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sale:deliver:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "销售送货", Operation = Operation.UPDATE)
	public ServiceResult<SaleDeliver> update(@RequestBody SaleDeliver order, HttpServletRequest request)
	{
		ServiceResult<SaleDeliver> result = serviceFactory.getSaleDeliverService().update(order);
		request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 审核销售送货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:44:00, zhengby
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("sale:deliver:audit")
	public AjaxResponseBody check(@PathVariable Long id)
	{
		List<StockProduct> list = serviceFactory.getSaleDeliverService().check(id, BoolValue.NO);
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
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 强制完工销售送货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:43:37, zhengby
	 */
	@RequestMapping(value = "forceCheck/{id}")
	@ResponseBody
	@RequiresPermissions("sale:deliver:audit")
	public AjaxResponseBody forceCheck(@PathVariable Long id)
	{
		List<StockProduct> list = serviceFactory.getSaleDeliverService().check(id, BoolValue.YES);
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
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 反审核
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月18日 上午10:06:31, think
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("sale:deliver:audit_cancel")
	public AjaxResponseBody checkBack(@PathVariable Long id)
	{
		if (serviceFactory.getSaleDeliverService().checkBack(id))
		{
			return returnSuccessBody();
		}
		else
		{
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.SALE_IV, id);
			return returnErrorBody(map);
		}
	}

	/**
	 * <pre>
	 * 功能 - 强制完工销售送货单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids 表ID
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:45:09, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("sale:deliver:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? SaleDeliver.class : SaleDeliverDetail.class;
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
	 * 功能 - 取消强制完工销售送货单:
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:45:41, zhengby
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("sale:deliver:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? SaleDeliver.class : SaleDeliverDetail.class;
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
	 * 功能 - 删除销售送货单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:46:23, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("sale:deliver:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "销售送货", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		try
		{

			SaleDeliver order = serviceFactory.getSaleDeliverService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getSaleDeliverService().delete(id);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody();
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			return returnErrorBody(ex);
		}
	}

	/**
		* 单据列表
		*/

	/*
	 * @RequestMapping(value = "list")
	 * @RequiresPermissions("sale:deliver:list") public String list() { return "sale/deliver/list"; }
	 */
	/**
	 * <pre>
	 * 功能 - 查看销售订单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:47:48, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<SaleDeliver> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SaleDeliver> result = serviceFactory.getSaleDeliverService().findByCondition(queryParam);
		SaleDeliver deliver = new SaleDeliver();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal noTaxTotalMoney = new BigDecimal(0);
		for (SaleDeliver saleDeliver : result.getResult())
		{
			totalMoney = totalMoney.add(saleDeliver.getTotalMoney());
			totalTax = totalTax.add(saleDeliver.getTotalTax());
			noTaxTotalMoney = noTaxTotalMoney.add(saleDeliver.getNoTaxTotalMoney());
		}
		deliver.setTotalMoney(totalMoney);
		deliver.setTotalTax(totalTax);
		deliver.setNoTaxTotalMoney(noTaxTotalMoney);
		deliver.setIsCheck(null);
		result.getResult().add(deliver);
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 查看销售送货单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:47:45, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<SaleDeliverDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SaleDeliverDetail> result = serviceFactory.getSaleDeliverService().findDetailByCondition(queryParam);
		SaleDeliverDetail detail = new SaleDeliverDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		Integer reconcilQty = new Integer(0);// 已对账数
		Integer returnQty = new Integer(0);// 已退货数量
		Integer spareQty = new Integer(0);// 备品数量
		BigDecimal price = new BigDecimal(0);// 单价
		Integer totalQty = new Integer(0);

		for (SaleDeliverDetail saleDeliverDetail : result.getResult())
		{
			money = money.add(saleDeliverDetail.getMoney());
			tax = tax.add(saleDeliverDetail.getTax());
			noTaxMoney = noTaxMoney.add(saleDeliverDetail.getNoTaxMoney());
			reconcilQty += saleDeliverDetail.getReconcilQty();
			returnQty += saleDeliverDetail.getReturnQty();
			spareQty += saleDeliverDetail.getSpareQty();
			price = price.add(saleDeliverDetail.getPrice());
			totalQty += saleDeliverDetail.getQty();
		}
		SaleDeliver deliver = new SaleDeliver();
		detail.setMaster(deliver);
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setReconcilQty(reconcilQty);
		detail.setReturnQty(returnQty);
		detail.setSpareQty(spareQty);
		detail.setPrice(price);
		detail.setQty(totalQty);
		result.getResult().add(detail);
		return result;
	}

	/**
	 * <pre>
	 * 页面 -  快速选择送货单列表
	 * </pre>
	 * @param multiple
	 * @param customerId
	 * @param rowIndex
	 * @param map
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月22日 下午5:48:29, zhengby
	 */
	@RequestMapping(value = "quick_select")
	public String quickSelectProduct(Boolean multiple, Long customerId, Integer rowIndex, ModelMap map) throws Exception
	{
		map.put("multiple", multiple);
		map.put("customerId", customerId);
		map.put("rowIndex", rowIndex);
		return "sale/deliver/quick_select";
	}

	/**
	 * <pre>
	 * 功能 - 获取销售送货单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:48:46, zhengby
	 */
	@RequestMapping(value = "ajaxDeliverList")
	@ResponseBody
	public SearchResult<SaleDeliverDetail> ajaxDeliverList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SaleDeliverDetail> result = serviceFactory.getSaleDeliverService().findDetailByConditions(queryParam);
		return result;
	}
}
