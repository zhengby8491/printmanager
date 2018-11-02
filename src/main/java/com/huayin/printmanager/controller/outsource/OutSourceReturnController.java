/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.outsource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturn;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturnDetail;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.enumerate.BasicType;
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
 * 发外管理 - 发外退货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年7月1日, zhaojt
 * @version 	   2.0, 2018年2月23日下午5:32:38, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/outsource/return")
public class OutSourceReturnController extends BaseController
{

	/**
	 * <pre>
	 * 页面 - 跳转到新增发外退货单
	 * </pre>
	 * @param supplierId
	 * @param arriveIds
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:33:40, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("outsource:return:create")
	public String create(Long supplierId, Long[] arriveIds, ModelMap map)
	{
		// 供应商
		Supplier supplier = serviceFactory.getSupplierService().get(supplierId);

		List<OutSourceArriveDetail> detailList = Lists.newArrayList();
		if (null != arriveIds)
		{
			for (Long id : arriveIds)
			{
				OutSourceArriveDetail detail = serviceFactory.getOutSourceArriveService().getDetail(id);

				// 发外退回 >【入库数量 大于 已退货数量】 并且 【入库数量 大于 已对账数量】
				if (detail.getQty().compareTo(detail.getReturnQty()) != 1 || detail.getQty().compareTo(detail.getReconcilQty()) != 1)
					continue;

				detailList.add(detail);
			}
		}
		if (detailList.size() > 0)
		{
			// 查询所有工单WorkPart2Product，并设置到发外加工明细中
			Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
			for (OutSourceArriveDetail detail : detailList)
			{
				if (null != detail.getWorkId())
				{
					long workId = detail.getWorkId();
					List<WorkProduct> list = productMap.get(workId);
					if (null != list)
					{
						detail.setProductList(list);
						// 因为页面实用json格式，所以必须给字段设置数据，否则json数据是不存在的
						detail.setProductNames(detail.getProductNames());
					}
				}
			}

			map.put("detailList", detailList);
			map.put("supplier", supplier);
			map.put("detailListJson", JsonUtils.toJson(detailList));
			map.put("supplierJson", JsonUtils.toJson(supplier));
		}
		return "outsource/return/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存发外退货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:34:16, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("outsource:return:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "发外退货", Operation = Operation.ADD)
	public ServiceResult<OutSourceReturn> save(@RequestBody OutSourceReturn order, HttpServletRequest request)
	{
		try
		{
			ServiceResult<OutSourceReturn> result = serviceFactory.getOutSourceReturnService().save(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return result;
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			logger.error(ex.getMessage(), "OutSourceReturn:" + JsonUtils.toJson(order));
			return null;
		}
	}

	/**
	 * <pre>
	 * 页面 - 发外退货单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:36:32, zhengby
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("outsource:return:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		OutSourceReturn order = serviceFactory.getOutSourceReturnService().get(id);

		// 查询所有工单WorkPart2Product，并设置到发外退货明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (OutSourceReturnDetail detail : order.getDetailList())
		{
			if (null != detail.getWorkId())
			{
				long workId = detail.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					detail.setProductList(list);
					// 因为页面实用json格式，所以必须给字段设置数据，否则json数据是不存在的
					detail.setProductNames(detail.getProductNames());
				}
			}
		}

		map.put("order", order);
		return "outsource/return/view";
	}

	/**
	 * <pre>
	 * 功能 - 返回发外退货单打印数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:37:01, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:return:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{

		Map<String, Object> map = null;
		try
		{
			OutSourceReturn order = serviceFactory.getOutSourceReturnService().get(id);
			Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
			for (OutSourceReturnDetail detail : order.getDetailList())
			{
				if (null != detail.getWorkId())
				{
					long workId = detail.getWorkId();
					List<WorkProduct> list = productMap.get(workId);
					if (null != list)
					{
						detail.setProductList(list);
						// 因为页面实用json格式，所以必须给字段设置数据，否则json数据是不存在的
						detail.setProductName(detail.getProductNames());
					}
				}
			}
			map = ObjectUtils.objectToMap(order);
			map.put("printDate", new Date());
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
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * <pre>
	 * 页面 - 发外退货单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:37:32, zhengby
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("outsource:return:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		OutSourceReturn order = serviceFactory.getOutSourceReturnService().get(id);

		// 查询所有工单WorkPart2Product，并设置到发外退货明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (OutSourceReturnDetail detail : order.getDetailList())
		{
			if (null != detail.getWorkId())
			{
				long workId = detail.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					detail.setProductList(list);
					// 因为页面实用json格式，所以必须给字段设置数据，否则json数据是不存在的
					detail.setProductNames(detail.getProductNames());
				}
			}
		}

		List<Warehouse> product_warehouseList = (List<Warehouse>) UserUtils.getBasicListParam(BasicType.WAREHOUSE.name(), "warehouseType", "PRODUCT");
		List<Warehouse> semi_product_warehouseList = (List<Warehouse>) UserUtils.getBasicListParam(BasicType.WAREHOUSE.name(), "warehouseType", "SEMI_PRODUCT");

		map.put("product_warehouseList", product_warehouseList);
		map.put("semi_product_warehouseList", semi_product_warehouseList);
		map.put("order", order);
		return "outsource/return/edit";
	}

	/**
	 * <pre>
	 * 功能 - 更新发外退货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:38:04, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("outsource:return:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "发外退货", Operation = Operation.UPDATE)
	public ServiceResult<OutSourceReturn> update(@RequestBody OutSourceReturn order, HttpServletRequest request)
	{
		try
		{
			ServiceResult<OutSourceReturn> result = serviceFactory.getOutSourceReturnService().update(order);
			// 更新旧数据
			OutSourceReturn new_order = serviceFactory.getOutSourceReturnService().get(order.getId());
			request.setAttribute(SystemLogAspect.BILLNO, new_order.getBillNo());
			return result;
		}
		catch (BusinessException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * <pre>
	 * 功能 - 审核发外退货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:38:25, zhengby
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:return:audit")
	public AjaxResponseBody check(@PathVariable Long id)
	{
		List<StockProduct> list = serviceFactory.getOutSourceReturnService().check(id, BoolValue.NO);
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
	 * 功能 - 强制审核
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月18日 上午10:41:33, think
	 */
	@RequestMapping(value = "forceCheck/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:return:audit")
	public AjaxResponseBody forceCheck(@PathVariable Long id)
	{
		List<StockProduct> list = serviceFactory.getOutSourceReturnService().check(id, BoolValue.YES);
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
	 * 功能 - 反审核发外退货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:39:42, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:return:audit_cancel")
	public AjaxResponseBody checkBack(@PathVariable Long id)
	{
		List<StockProduct> list = serviceFactory.getOutSourceReturnService().checkBack(id);
		if (list != null)
		{
			return returnSuccessBody();
		}
		else
		{
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.OUTSOURCE_OR, id);
			return returnErrorBody(map);
		}

	}

	/**
	 * <pre>
	 * 功能 - 强制完工发外退货单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:40:03, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("outsource:return:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? OutSourceReturn.class : OutSourceReturnDetail.class;
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
	 * 功能 - 取消强制完工发外退货单
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:40:26, zhengby
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("outsource:return:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? OutSourceReturn.class : OutSourceReturnDetail.class;
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
	 * 功能 - 删除发外退货单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:40:56, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:return:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "发外退货", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		try
		{

			OutSourceReturn order = serviceFactory.getOutSourceReturnService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getOutSourceReturnService().delete(id);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody();
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 页面 - 发外退货单列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:41:14, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("outsource:return:list")
	public String list()
	{
		return "outsource/return/list";
	}

	/**
	 * <pre>
	 * 功能 - 返回发外退货单列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:41:42, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<OutSourceReturn> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceReturn> result = serviceFactory.getOutSourceReturnService().findByCondition(queryParam);
		OutSourceReturn sourceReturn = new OutSourceReturn();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal noTaxTotalMoney = new BigDecimal(0);
		for (OutSourceReturn outSourceReturn : result.getResult())
		{
			totalMoney = totalMoney.add(outSourceReturn.getTotalMoney());
			totalTax = totalTax.add(outSourceReturn.getTotalTax());
			noTaxTotalMoney = noTaxTotalMoney.add(outSourceReturn.getNoTaxTotalMoney());
		}
		sourceReturn.setTotalMoney(totalMoney);
		sourceReturn.setTotalTax(totalTax);
		sourceReturn.setNoTaxTotalMoney(noTaxTotalMoney);
		sourceReturn.setIsCheck(null);
		result.getResult().add(sourceReturn);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到快速选择来源发外到货的页面
	 * </pre>
	 * @param supplierId
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:42:07, zhengby
	 */
	@RequestMapping(value = "quick_select")
	public String quick_select(Long supplierId, ModelMap map)
	{
		map.put("supplierId", supplierId);
		return "outsource/return/quick_select";
	}

	/**
	 * <pre>
	 * 页面 - 发外退货单明细表列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:43:54, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("outsource:return_detail:list")
	public String detailList()
	{
		return "outsource/return/detail_list";
	}

	/**
	 * <pre>
	 * 功能 - 发外退货单明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:44:45, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<OutSourceReturnDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceReturnDetail> result = serviceFactory.getOutSourceReturnService().findDetailByCondition(queryParam);

		// 查询所有工单WorkPart2Product，并设置到发外退货明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (OutSourceReturnDetail detail : result.getResult())
		{
			if (null != detail.getWorkId())
			{
				long workId = detail.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					detail.setProductList(list);
				}
			}
		}

		OutSourceReturnDetail detail = new OutSourceReturnDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal reconcilQty = new BigDecimal(0);
		BigDecimal price = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		BigDecimal totalQty = new BigDecimal(0);
		for (OutSourceReturnDetail outSourceReturnDetail : result.getResult())
		{
			money = money.add(outSourceReturnDetail.getMoney());
			tax = tax.add(outSourceReturnDetail.getTax());
			reconcilQty = reconcilQty.add(outSourceReturnDetail.getReconcilQty());
			price = price.add(outSourceReturnDetail.getPrice());
			noTaxMoney = noTaxMoney.add(outSourceReturnDetail.getNoTaxMoney());
			totalQty = totalQty.add(outSourceReturnDetail.getQty());
		}
		OutSourceReturn sourceReturn = new OutSourceReturn();
		detail.setMaster(sourceReturn);
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setReconcilQty(reconcilQty);
		detail.setPrice(price);
		detail.setQty(totalQty);
		result.getResult().add(detail);
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 返回发外到货明细表列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:45:15, zhengby
	 */
	@RequestMapping(value = "ajaxSource")
	@ResponseBody
	public SearchResult<OutSourceArriveDetail> source(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceArriveDetail> result = serviceFactory.getOutSourceArriveService().findArriveSource(queryParam.getDateMin(), queryParam.getDateMax(), queryParam.getSupplierId(), queryParam.getBillNo(), queryParam.getPageSize(), queryParam.getPageNumber());
		// 查询所有工单WorkPart2Product，并设置到发外退货明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		// TODO 可以做发外类型判断，如果是整单的则不需填充，工序则需要填充
		for (OutSourceArriveDetail detail : result.getResult())
		{
			if (null != detail.getWorkId())
			{
				long workId = detail.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					detail.setProductList(list);
				}
			}
		}
		return result;
	}
}
