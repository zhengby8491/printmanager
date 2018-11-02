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
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.SupplierAddress;
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArrive;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.OutSourceType;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 发外管理 - 发外到货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年7月1日, zhaojt
 * @version 	   2.0, 2018年2月23日下午3:43:23, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/outsource/arrive")
public class OutSourceArriveController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 发外到货单创建
	 * </pre>
	 * @param supplierId
	 * @param ids
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:44:45, zhengby
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "create")
	@RequiresPermissions("outsource:arrive:create")
	public String create(Long supplierId, Long[] ids, ModelMap map)
	{
		OutSourceArrive order = new OutSourceArrive();
		Supplier supplier = serviceFactory.getSupplierService().get(supplierId);
		if (supplier != null)
		{
			order.setDeliveryClassId(supplier.getDeliveryClassId());
			order.setSettlementClassId(supplier.getSettlementClassId());
			order.setEmployeeId(supplier.getEmployeeId());
			SupplierAddress supplierDefaulAddress = supplier.getDefaultAddress();
			if (supplierDefaulAddress != null)
			{
				order.setLinkName(supplierDefaulAddress.getUserName());
				order.setMobile(supplierDefaulAddress.getMobile());
				order.setSupplierAddress(supplierDefaulAddress.getAddress());
			}
			order.setCurrencyType(supplier.getCurrencyType());
		}
		List<OutSourceArriveDetail> detailList = new ArrayList<OutSourceArriveDetail>();
		order.setDetailList(detailList);
		for (Long id : ids)
		{
			OutSourceProcessDetail bean = serviceFactory.getOutSourceProcessService().getDetail(id);

			// 加工数 大于 到货数
			if (null != bean && bean.getQty().compareTo(bean.getArriveQty()) != 1)
				continue;

			if (order.getLinkName() == null)
			{
				order.setLinkName(bean.getMaster().getLinkName());
			}
			if (order.getMobile() == null)
			{
				order.setMobile(bean.getMaster().getMobile());
			}
			if (order.getSupplierAddress() == null)
			{
				order.setSupplierAddress(bean.getMaster().getSupplierAddress());
			}
			OutSourceArriveDetail detail = new OutSourceArriveDetail();
			detail.setType(bean.getType());
			detail.setSourceBillType(bean.getMaster().getBillType());
			detail.setSourceQty(bean.getQty());// 源单数=投入数
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
			detail.setQty(bean.getQty().subtract(bean.getArriveQty()));// 未到货数量
			detail.setProduceNum(bean.getProduceNum());
			detail.setProductType(bean.getProductType()); // 工单类型
			detail.setWorkProcedureType(bean.getWorkProcedureType()); // 工单部件类型

			if ((bean.getMoney().subtract(bean.getArriveMoney())).doubleValue() < 0)
			{
				BigDecimal price = bean.getArriveMoney().divide(bean.getArriveQty(), 4, RoundingMode.HALF_UP);
				detail.setMoney(price.multiply(detail.getQty()));
			}
			else
			{
				detail.setMoney(bean.getMoney().subtract(bean.getArriveMoney()));
			}

			detail.setPrice(detail.getMoney().divide(detail.getQty(), 4, RoundingMode.HALF_UP));
			detail.setTax(bean.getTax());
			detail.setTaxRateId(bean.getTaxRateId());
			detail.setTaxRateName(bean.getTaxRateName());
			detail.setTaxRatePercent(bean.getTaxRatePercent());
			detail.setNoTaxPrice(bean.getNoTaxPrice());
			detail.setNoTaxMoney(bean.getNoTaxMoney());
			detail.setMemo(bean.getMemo());
			detail.setOutSourceBillNo(bean.getMaster().getBillNo());
			detailList.add(detail);
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
					}
				}
			}

			List<Warehouse> product_warehouseList = (List<Warehouse>) UserUtils.getBasicListParam(BasicType.WAREHOUSE.name(), "warehouseType", "PRODUCT");
			List<Warehouse> semi_product_warehouseList = (List<Warehouse>) UserUtils.getBasicListParam(BasicType.WAREHOUSE.name(), "warehouseType", "SEMI_PRODUCT");

			map.put("product_warehouseList", product_warehouseList);
			map.put("semi_product_warehouseList", semi_product_warehouseList);
			map.put("order", order);
			map.put("supplier", supplier);
		}
		return "outsource/arrive/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存发外到货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:46:14, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("outsource:arrive:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "发外到货", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody OutSourceArrive order, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(order))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		// 保存之前验证是否已到货完，前端的已校验过，后台再校验一次，防止保存重复发外到货单
		for (OutSourceArriveDetail detail : order.getDetailList())
		{
			if (null == detail.getSourceDetailId())
			{
				continue;
			}
			OutSourceProcessDetail source = serviceFactory.getOutSourceProcessService().getDetail(detail.getSourceDetailId());
			if (source.getArriveQty().compareTo(source.getQty()) != -1)
			{
				return returnErrorBody("单据已生成，无需重复操作");
			}
		}
		try
		{
			serviceFactory.getOutSourceArriveService().save(order);

			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order);
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			logger.error(ex.getMessage(), "OutSourceArrive:" + JsonUtils.toJson(order));
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 页面 - 跳转到发外到货单查看页面
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:46:37, zhengby
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("outsource:arrive:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		OutSourceArrive order = serviceFactory.getOutSourceArriveService().get(id);

		// 查询所有工单WorkPart2Product，并设置到发外加工明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (OutSourceArriveDetail detail : order.getDetailList())
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
		return "outsource/arrive/view";
	}

	/**
	 * <pre>
	 * 功能 - 返回发外到货单打印数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:47:10, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:arrive:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{

		Map<String, Object> map = null;
		try
		{
			OutSourceArrive order = serviceFactory.getOutSourceArriveService().get(id);
			Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
			for (OutSourceArriveDetail detail : order.getDetailList())
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
			map.put("printDate", DateUtils.formatDateTime(new Date()));
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到发外到货单编辑页面
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:48:15, zhengby
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("outsource:arrive:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		OutSourceArrive order = serviceFactory.getOutSourceArriveService().get(id);

		// 查询所有工单WorkPart2Product，并设置到发外加工明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (OutSourceArriveDetail detail : order.getDetailList())
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

		List<Warehouse> product_warehouseList = (List<Warehouse>) UserUtils.getBasicListParam(BasicType.WAREHOUSE.name(), "warehouseType", "PRODUCT");
		List<Warehouse> semi_product_warehouseList = (List<Warehouse>) UserUtils.getBasicListParam(BasicType.WAREHOUSE.name(), "warehouseType", "SEMI_PRODUCT");

		map.put("product_warehouseList", product_warehouseList);
		map.put("semi_product_warehouseList", semi_product_warehouseList);
		map.put("order", order);
		return "outsource/arrive/edit";
	}

	/**
	 * <pre>
	 * 功能 - 更新发外到货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:48:58, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("outsource:arrive:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "发外到货", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody OutSourceArrive order, HttpServletRequest request)
	{
		try
		{
			serviceFactory.getOutSourceArriveService().update(order);
			// 更新旧数据
			OutSourceArrive new_order = serviceFactory.getOutSourceArriveService().get(order.getId());
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
	 * 功能 - 审核发外到货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:49:24, zhengby
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:arrive:audit")
	public AjaxResponseBody check(@PathVariable Long id)
	{
		if (serviceFactory.getOutSourceArriveService().check(id, BoolValue.YES))
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
	 * 功能 - 反审核发外到货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:49:55, zhengby
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:arrive:audit_cancel")
	public AjaxResponseBody checkBack(@PathVariable Long id)
	{
		// 判断是否是工序外发工单
		OutSourceArrive out = serviceFactory.getOutSourceArriveService().get(id);
		// 判断是否已审核
		if (out.getIsCheck() == BoolValue.NO)
		{
			throw new BusinessException("已反审核");
		}
		for (OutSourceArriveDetail detail : out.getDetailList())
		{
			// 成品验证库存
			if (OutSourceType.PRODUCT == detail.getType())
			{
				List<StockProduct> list = serviceFactory.getOutSourceArriveService().checkBack(id);
				if (list.size() > 0)
				{
					return returnSuccessBody(list);
				}
			}
		}

		if (serviceFactory.getOutSourceArriveService().check(id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			@SuppressWarnings("rawtypes")
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.OUTSOURCE_OA, id);
			return returnErrorBody(map);
		}
	}

	/**
	 * <pre>
	 * 功能 - 强制反审核，不要判断库存
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月19日 下午4:07:22, think
	 */
	@RequestMapping(value = "forceCheckBack/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:arrive:audit_cancel")
	public AjaxResponseBody forceCheckBack(@PathVariable Long id)
	{
		if (serviceFactory.getOutSourceArriveService().check(id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			@SuppressWarnings("rawtypes")
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.OUTSOURCE_OA, id);
			return returnErrorBody(map);
		}
	}

	/**
	 * <pre>
	 * 功能 - 强制完工发外到货单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:50:30, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("outsource:arrive:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? OutSourceArrive.class : OutSourceProcessDetail.class;
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
	 * 功能 - 取消强制完工发外到货单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:50:56, zhengby
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("outsource:arrive:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? OutSourceArrive.class : OutSourceProcessDetail.class;
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
	 * 功能 - 删除发外到货单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:53:38, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:arrive:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "发外到货", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		try
		{
			OutSourceArrive order = serviceFactory.getOutSourceArriveService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getOutSourceArriveService().delete(id);
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
	 * 页面 - 跳转到发外到货单列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:54:15, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("outsource:arrive:list")
	public String list()
	{
		return "outsource/arrive/list";
	}

	/**
	 * <pre>
	 * 功能 - 返回发外到货单列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:54:57, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<OutSourceArrive> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceArrive> result = serviceFactory.getOutSourceArriveService().findByCondition(queryParam);
		OutSourceArrive arrive = new OutSourceArrive();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal noTaxTotalMoney = new BigDecimal(0);
		for (OutSourceArrive outSourceArrive : result.getResult())
		{
			totalMoney = totalMoney.add(outSourceArrive.getTotalMoney());
			totalTax = totalTax.add(outSourceArrive.getTotalTax());
			noTaxTotalMoney = noTaxTotalMoney.add(outSourceArrive.getNoTaxTotalMoney());
		}
		arrive.setTotalMoney(totalMoney);
		arrive.setTotalTax(totalTax);
		arrive.setNoTaxTotalMoney(noTaxTotalMoney);
		arrive.setIsCheck(null);
		result.getResult().add(arrive);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到发外到货明细列表页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:55:46, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("outsource:arrive_detail:list")
	public String detailList()
	{
		return "outsource/arrive/detail_list";
	}

	/**
	 * <pre>
	 * 功能 - 返回发外到货明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:56:19, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<OutSourceArriveDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceArriveDetail> result = serviceFactory.getOutSourceArriveService().findDetailByCondition(queryParam);

		// 查询所有工单WorkPart2Product，并设置到发外加工明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
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

		OutSourceArriveDetail detail = new OutSourceArriveDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		BigDecimal reconcilQty = new BigDecimal(0);
		BigDecimal totalQty = new BigDecimal(0);
		for (OutSourceArriveDetail outSourceArriveDetail : result.getResult())
		{
			money = money.add(outSourceArriveDetail.getMoney());
			tax = tax.add(outSourceArriveDetail.getTax());
			noTaxMoney = noTaxMoney.add(outSourceArriveDetail.getNoTaxMoney());
			reconcilQty = reconcilQty.add(outSourceArriveDetail.getReconcilQty());
			totalQty = totalQty.add(outSourceArriveDetail.getQty());
		}
		OutSourceArrive arrive = new OutSourceArrive();
		detail.setMaster(arrive);
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setReconcilQty(reconcilQty);
		detail.setQty(totalQty);
		result.getResult().add(detail);

		return result;
	}

}
