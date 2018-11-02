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
import java.math.RoundingMode;
import java.util.ArrayList;
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

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.SupplierAddress;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturnDetail;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 发外管理 - 发外对账
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年7月1日, zhaojt
 * @version 	   2.0, 2018年2月23日下午5:22:12, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/outsource/reconcil")
public class OutSourceReconcilController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 发外对账单创建
	 * </pre>
	 * @param supplierId
	 * @param arriveIds
	 * @param returnIds
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:22:45, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("outsource:reconcil:create")
	public String create(Long supplierId, Long[] arriveIds, Long[] returnIds, ModelMap map)
	{
		OutSourceReconcil order = new OutSourceReconcil();
		Supplier supplier = serviceFactory.getSupplierService().get(supplierId);
		if (supplier != null)
		{
			order.setSettlementClassId(supplier.getSettlementClassId());
			order.setEmployeeId(supplier.getEmployeeId());
			SupplierAddress supplierDefaultAddress = supplier.getDefaultAddress();
			if (supplierDefaultAddress != null)
			{
				order.setLinkName(supplierDefaultAddress.getUserName());
				order.setMobile(supplierDefaultAddress.getMobile());
				order.setSupplierAddress(supplierDefaultAddress.getAddress());
			}

			order.setCurrencyType(supplier.getCurrencyType());
		}
		List<OutSourceReconcilDetail> detailList = new ArrayList<OutSourceReconcilDetail>();
		order.setDetailList(detailList);
		if (arriveIds != null)
		{
			for (Long id : arriveIds)
			{
				OutSourceArriveDetail bean = serviceFactory.getOutSourceArriveService().getDetail(id);

				// 发外对账 >【入库数量 大于 已对账数量】
				if (null != bean && bean.getQty().compareTo(bean.getReconcilQty()) != 1)
					continue;

				if (order.getLinkName() != null)
				{
					order.setLinkName(bean.getMaster().getLinkName());
				}
				if (order.getMobile() != null)
				{
					order.setMobile(bean.getMaster().getMobile());
				}
				if (order.getSupplierAddress() != null)
				{
					order.setSupplierAddress(bean.getMaster().getSupplierAddress());
				}
				OutSourceReconcilDetail detail = new OutSourceReconcilDetail();
				detail.setType(bean.getType());
				detail.setSourceBillType(bean.getMaster().getBillType());
				detail.setSourceQty(bean.getQty());// 源单数
				detail.setSourceBillNo(bean.getMaster().getBillNo());
				detail.setSourceId(bean.getMasterId());
				detail.setSourceDetailId(bean.getId());
				detail.setWorkBillNo(bean.getWorkBillNo());// 工单单据编号
				detail.setWorkId(bean.getWorkId());
				detail.setProductId(bean.getProductId());
				detail.setProductName(bean.getProductName());
				detail.setStyle(bean.getStyle());
				detail.setPartName(bean.getPartName());
				detail.setProcedureId(bean.getProcedureId());
				detail.setProcedureCode(bean.getProcedureCode());
				detail.setProcedureType(bean.getProcedureType());
				detail.setProcedureName(bean.getProcedureName());
				detail.setProcessRequire(bean.getProcessRequire());
				detail.setProduceNum(bean.getProduceNum());
				detail.setQty(bean.getQty().subtract(bean.getReconcilQty()));// 未对账数量
				detail.setMoney(bean.getMoney().subtract(bean.getReconcilMoney()));
				detail.setPrice(detail.getMoney().divide(detail.getQty(), 4, RoundingMode.HALF_UP));
				detail.setTaxRateId(bean.getTaxRateId());
				detail.setTaxRateName(bean.getTaxRateName());
				detail.setTaxRatePercent(bean.getTaxRatePercent());
				detail.setMemo(bean.getMemo());
				detail.setOutSourceBillNo(bean.getOutSourceBillNo());
				detail.setProductType(bean.getProductType());
				detail.setWorkProcedureType(bean.getWorkProcedureType());
				detail.setDeliveryTime(bean.getMaster().getDeliveryTime());
				detailList.add(detail);
			}
		}
		if (returnIds != null)
		{

			for (Long id : returnIds)
			{
				OutSourceReturnDetail bean = serviceFactory.getOutSourceReturnService().getDetail(id);

				// 发外对账 >【入库数量 大于 已对账数量】
				if (null != bean && bean.getQty().compareTo(bean.getReconcilQty()) != 1)
					continue;

				if (order.getLinkName() != null)
				{
					order.setLinkName(bean.getMaster().getLinkName());
				}
				if (order.getMobile() != null)
				{
					order.setMobile(bean.getMaster().getMobile());
				}
				if (order.getSupplierAddress() != null)
				{
					order.setSupplierAddress(bean.getMaster().getSupplierAddress());
				}
				OutSourceReconcilDetail detail = new OutSourceReconcilDetail();
				detail.setType(bean.getType());
				detail.setSourceBillType(bean.getMaster().getBillType());
				detail.setSourceQty(bean.getQty());// 源单数
				detail.setSourceBillNo(bean.getMaster().getBillNo());
				detail.setSourceId(bean.getMasterId());
				detail.setSourceDetailId(bean.getId());
				detail.setWorkBillNo(bean.getWorkBillNo());// 工单单据编号
				detail.setWorkId(bean.getWorkId());
				detail.setProductId(bean.getProductId());
				detail.setProductName(bean.getProductName());
				detail.setStyle(bean.getStyle());
				detail.setPartName(bean.getPartName());
				detail.setProcedureId(bean.getProcedureId());
				detail.setProcedureCode(bean.getProcedureCode());
				detail.setProcedureType(bean.getProcedureType());
				detail.setProcedureName(bean.getProcedureName());
				detail.setProcessRequire(bean.getProcessRequire());
				detail.setProduceNum(bean.getProduceNum());
				BigDecimal qty = new BigDecimal(0);
				qty = new BigDecimal(0).subtract(new BigDecimal(Math.abs(bean.getQty().doubleValue())).subtract(bean.getReconcilQty()));
				detail.setQty(qty.setScale(2, RoundingMode.HALF_UP));// 未对账数量
				detail.setMoney(bean.getMoney().subtract(bean.getReconcilMoney()));
				detail.setPrice(detail.getMoney().divide(new BigDecimal(0).subtract(detail.getQty()), 4, RoundingMode.HALF_UP));
				detail.setTaxRateId(bean.getTaxRateId());
				detail.setTaxRateName(bean.getTaxRateName());
				detail.setTaxRatePercent(bean.getTaxRatePercent());
				detail.setMemo(bean.getMemo());
				detail.setOutSourceBillNo(bean.getOutSourceBillNo());
				detail.setProductType(bean.getProductType());
				detail.setWorkProcedureType(bean.getWorkProcedureType());
				detail.setDeliveryTime(bean.getMaster().getCreateTime());
				detailList.add(detail);
			}
		}
		if (detailList.size() > 0)
		{
			// 查询所有工单WorkPart2Product，并设置到发外对账明细中
			Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
			for (OutSourceReconcilDetail detail : detailList)
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
			map.put("supplier", supplier);
		}
		return "outsource/reconcil/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存发外对账单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:23:35, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("outsource:reconcil:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "发外对账", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody OutSourceReconcil order, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(order))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}

		// 保存之前验证是否已对账完，前端的已校验过，后台再校验一次，防止保存重复对账单
		for (OutSourceReconcilDetail detail : order.getDetailList())
		{
			if (null == detail.getSourceDetailId())
			{
				continue;
			}
			if (detail.getSourceBillType() == BillType.OUTSOURCE_OA)
			{
				OutSourceArriveDetail source = serviceFactory.getOutSourceArriveService().getDetail(detail.getSourceDetailId());
				if (source.getReconcilQty().compareTo(source.getQty()) != -1)
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
			if (detail.getSourceBillType() == BillType.OUTSOURCE_OR)
			{
				OutSourceReturnDetail source = serviceFactory.getOutSourceReturnService().getDetail(detail.getSourceDetailId());
				if (source.getReconcilQty().compareTo(source.getQty()) != -1)
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
		}

		try
		{
			serviceFactory.getOutSourceReconcilService().save(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order);
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			logger.error(ex.getMessage(), "OutSourceReconcil:" + JsonUtils.toJson(order));
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 页面 - 发外对账单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:24:37, zhengby
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("outsource:reconcil:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		OutSourceReconcil order = serviceFactory.getOutSourceReconcilService().get(id);

		// 查询所有工单WorkPart2Product，并设置到发外对账明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (OutSourceReconcilDetail detail : order.getDetailList())
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
		return "outsource/reconcil/view";
	}

	/**
	 * <pre>
	 * 功能 - 返回打印发外对账单数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:25:36, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:reconcil:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			OutSourceReconcil reconcil = serviceFactory.getOutSourceReconcilService().get(id);
			Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
			for (OutSourceReconcilDetail detail : reconcil.getDetailList())
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
			map = ObjectUtils.objectToMap(reconcil);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());
		}
		catch (Exception e)
		{
			logger.error("查询发外对账打印数据发生业务逻辑异常!", e);
		}
		return map;
	}

	/**
	 * <pre>
	 * 页面 - 发外对账单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:26:27, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("outsource:reconcil:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		OutSourceReconcil order = serviceFactory.getOutSourceReconcilService().get(id);

		// 查询所有工单WorkPart2Product，并设置到发外对账明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (OutSourceReconcilDetail detail : order.getDetailList())
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
		return "outsource/reconcil/edit";
	}

	/**
	 * <pre>
	 * 功能 - 更新发外对账单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:26:58, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("outsource:reconcil:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "发外对账", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody OutSourceReconcil order, HttpServletRequest request)
	{
		try
		{
			serviceFactory.getOutSourceReconcilService().update(order);
			// 更新旧数据
			OutSourceReconcil new_order = serviceFactory.getOutSourceReconcilService().get(order.getId());
			request.setAttribute(SystemLogAspect.BILLNO, new_order.getBillNo());
			return returnSuccessBody(new_order);
		}
		catch (BusinessException ex)
		{
			throw ex;
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
	 * 功能 - 审核发外对账单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:27:22, zhengby
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:reconcil:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		if (serviceFactory.getCommonService().audit(BillType.OUTSOURCE_OC, id, BoolValue.YES))
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
	 * 功能 - 反审核发外对账单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:27:43, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "audit_cancel/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:reconcil:audit_cancel")
	public AjaxResponseBody auditCancel(@PathVariable Long id)
	{
		if (serviceFactory.getCommonService().audit(BillType.OUTSOURCE_OC, id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.OUTSOURCE_OC, id);
			return returnErrorBody(map);
		}
	}

	/**
	 * <pre>
	 * 功能 - 强制完工发外对账单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids
	 * @param arriveIds
	 * @param returnIds
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:28:10, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("outsource:reconcil:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam(required = false, name = "ids[]") Long[] ids, @RequestParam(required = false, name = "arriveIds[]") Long[] arriveIds, @RequestParam(required = false, name = "returnIds[]") Long[] returnIds)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType))
		{
			return returnErrorBody("提交数据不完整");
		}
		if (serviceFactory.getOutSourceReconcilService().forceComplete(tableType, ids, arriveIds, returnIds, BoolValue.YES))
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
	 * 功能 - 取消强制完工发外对账单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids
	 * @param arriveIds
	 * @param returnIds
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:28:48, zhengby
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("outsource:reconcil:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam(required = false, name = "ids[]") Long[] ids, @RequestParam(required = false, name = "arriveIds[]") Long[] arriveIds, @RequestParam(required = false, name = "returnIds[]") Long[] returnIds)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType))
		{
			return returnErrorBody("提交数据不完整");
		}
		if (serviceFactory.getOutSourceReconcilService().forceComplete(tableType, ids, arriveIds, returnIds, BoolValue.NO))
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
	 * 功能 - 删除发外对账单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:29:21, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:reconcil:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "发外对账", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		try
		{

			OutSourceReconcil order = serviceFactory.getOutSourceReconcilService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getOutSourceReconcilService().delete(id);
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
	 * 页面 - 发外对账单列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:29:39, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("outsource:reconcil:list")
	public String list()
	{
		return "outsource/reconcil/list";
	}

	/**
	 * <pre>
	 * 功能 - 返回发外对账单列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:30:35, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<OutSourceReconcil> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceReconcil> result = serviceFactory.getOutSourceReconcilService().findByCondition(queryParam);
		OutSourceReconcil reconcil = new OutSourceReconcil();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal noTaxTotalMoney = new BigDecimal(0);
		for (OutSourceReconcil outSourceReconcil : result.getResult())
		{
			totalMoney = totalMoney.add(outSourceReconcil.getTotalMoney());
			totalTax = totalTax.add(outSourceReconcil.getTotalTax());
			noTaxTotalMoney = noTaxTotalMoney.add(outSourceReconcil.getNoTaxTotalMoney());
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
	 * 页面 - 发外对账单明细列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:31:06, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("outsource:reconcil_detail:list")
	public String detailList()
	{
		return "outsource/reconcil/detail_list";
	}

	/**
	 * <pre>
	 * 功能 - 返回发外对账单明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:31:37, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<OutSourceReconcilDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceReconcilDetail> result = serviceFactory.getOutSourceReconcilService().findDetailByCondition(queryParam);

		// 查询所有工单WorkPart2Product，并设置到发外对账明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (OutSourceReconcilDetail detail : result.getResult())
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

		OutSourceReconcilDetail detail = new OutSourceReconcilDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		BigDecimal price = new BigDecimal(0);
		BigDecimal totalQty = new BigDecimal(0);
		BigDecimal totalPaymentMoney = new BigDecimal(0);
		for (OutSourceReconcilDetail outSourceReconcilDetail : result.getResult())
		{
			money = money.add(outSourceReconcilDetail.getMoney());
			tax = tax.add(outSourceReconcilDetail.getTax());
			noTaxMoney = noTaxMoney.add(outSourceReconcilDetail.getNoTaxMoney());
			price = price.add(outSourceReconcilDetail.getPrice());
			totalQty = totalQty.add(outSourceReconcilDetail.getQty());
			totalPaymentMoney = totalPaymentMoney.add(outSourceReconcilDetail.getPaymentMoney());
		}
		OutSourceReconcil reconcil = new OutSourceReconcil();
		detail.setMaster(reconcil);
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setPrice(price);
		detail.setQty(totalQty);
		detail.setPaymentMoney(totalPaymentMoney);
		result.getResult().add(detail);
		return result;
	}

}
