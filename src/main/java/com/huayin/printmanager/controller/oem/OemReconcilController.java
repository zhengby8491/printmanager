/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月16日 上午9:00:28
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.oem;

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

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.Logical;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.oem.OemDeliverDetail;
import com.huayin.printmanager.persist.entity.oem.OemReconcil;
import com.huayin.printmanager.persist.entity.oem.OemReconcilDetail;
import com.huayin.printmanager.persist.entity.oem.OemReturnDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工管理  - 代工对账
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月16日上午9:00:28, zhengby
 */
@Controller
@RequestMapping(value = "${basePath}/oem/reconcil")
public class OemReconcilController extends BaseController
{
	/**
	 * <pre>
	 * 页面  - 跳转到对账单列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:33:13, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("oem:reconcil:list")
	public String list()
	{
		return "oem/reconcil/list";
	}

	/**
	 * <pre>
	 * 数据  - 返回对账单列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:33:37, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@RequiresPermissions("oem:reconcil:list")
	@ResponseBody
	public SearchResult<OemReconcil> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OemReconcil> result = serviceFactory.getOemReconcilService().findByCondition(queryParam);
		OemReconcil order = new OemReconcil();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal totalNoTaxMony = new BigDecimal(0);
		for (OemReconcil o : result.getResult())
		{
			totalMoney = totalMoney.add(o.getTotalMoney());
			totalTax = totalTax.add(o.getTotalTax());
			totalNoTaxMony = totalNoTaxMony.add(o.getNoTaxTotalMoney());
		}
		order.setTotalMoney(totalMoney);
		order.setTotalTax(totalTax);
		order.setNoTaxTotalMoney(totalNoTaxMony);
		order.setIsCheck(null);
		result.getResult().add(order);
		return result;
	}

	/**
	 * <pre>
	 * 页面  - 跳转到对账单明细列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:33:52, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("oem:reconcil:detail:list")
	public String detailList()
	{
		return "oem/reconcil/detailList";
	}

	/**
	 * <pre>
	 * 数据  - 返回对账单明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:34:04, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@RequiresPermissions("oem:reconcil:detail:list")
	@ResponseBody
	public SearchResult<OemReconcilDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OemReconcilDetail> result = serviceFactory.getOemReconcilService().findDetailBycondition(queryParam);
		OemReconcilDetail detail = new OemReconcilDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		BigDecimal totalQty = new BigDecimal(0);
		for (OemReconcilDetail o : result.getResult())
		{
			money = money.add(o.getMoney());
			tax = tax.add(o.getTax());
			noTaxMoney = noTaxMoney.add(o.getNoTaxMoney());
			totalQty = totalQty.add(o.getQty());
		}
		OemReconcil oemReturn = new OemReconcil();
		detail.setMaster(oemReturn);
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setQty(totalQty);
		result.getResult().add(detail);
		return result;
	}

	/**
	 * <pre>
	 * 页面  - 跳转到创建退货单页面
	 * </pre>
	 * @param customerId
	 * @param billNo
	 * @param map
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:34:20, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("oem:reconcil:create")
	public String create(Long customerId, Long[] deliverIds, Long[] returnIds, ModelMap map)
	{
		Customer customer = serviceFactory.getCustomerService().get(customerId);

		OemReconcil order = new OemReconcil();
		List<OemReconcilDetail> detailList = new ArrayList<OemReconcilDetail>();
		order.setDetailList(detailList);
		if (null != deliverIds)
		{
			for (Long id : deliverIds)
			{
				OemDeliverDetail bean = serviceFactory.getOemDeliverService().getDetailHasMaster(id);
				if (bean.getQty().compareTo(bean.getReconcilQty()) != 1)
				{
					continue;
				}
				order.setLinkName(bean.getMaster().getLinkName());
				order.setMobile(bean.getMaster().getMobile());
				
				OemReconcilDetail detail = new OemReconcilDetail();
				detail.setSourceBillType(bean.getMaster().getBillType());
				detail.setSourceQty(bean.getQty());
				detail.setSourceBillNo(bean.getMaster().getBillNo());
				detail.setSourceId(bean.getMasterId());
				detail.setSourceDetailId(bean.getId());
				detail.setOemOrderBillNo(bean.getOemOrderBillNo());
				detail.setOemOrderBillId(bean.getSourceId());
				detail.setProductName(bean.getProductName());
				detail.setProductId(bean.getProductId());
				detail.setProcedureName(bean.getProcedureName());
				detail.setProcedureId(bean.getProcedureId());
				detail.setOriginProcedureName(bean.getOriginProcedureName());
				detail.setOriginProcedureId(bean.getOriginProcedureId());
				detail.setOriginCompanyId(bean.getOriginCompanyId());
				detail.setOriginBillNo(bean.getOriginBillNo());
				detail.setOriginBillId(bean.getOriginBillId());
				detail.setStyle(bean.getStyle());
				detail.setQty(bean.getQty().subtract(bean.getReconcilQty()));
				detail.setMoney(bean.getMoney().subtract(bean.getReconcilMoney()));
				detail.setPrice(detail.getMoney().divide(detail.getQty(), 4, RoundingMode.HALF_UP));
				detail.setTax(bean.getTax());
				detail.setTaxRateId(bean.getTaxRateId());
				detail.setPercent(bean.getPercent());
				detail.setNoTaxPrice(bean.getNoTaxPrice());
				detail.setNoTaxMoney(bean.getNoTaxMoney());
				detail.setPartName(bean.getPartName());
				detail.setProcessRequire(bean.getProcessRequire());
				detail.setDeliveryTime(bean.getMaster().getDeliveryTime());
				detail.setMemo(bean.getMemo());
				detail.setReceiveMoney(new BigDecimal(0));
				detailList.add(detail);
			}
		}
		if (null != returnIds)
		{
			for (Long id : returnIds)
			{
				OemReturnDetail bean = serviceFactory.getOemReturnService().getDetailHasMaster(id);
				if (bean.getQty().compareTo(bean.getReconcilQty()) != 1)
				{
					continue;
				}
				OemReconcilDetail detail = new OemReconcilDetail();
				detail.setSourceBillType(bean.getMaster().getBillType());
				detail.setSourceQty(bean.getQty());
				detail.setSourceBillNo(bean.getMaster().getBillNo());
				detail.setSourceId(bean.getMasterId());
				detail.setSourceDetailId(bean.getId());
				detail.setOemOrderBillNo(bean.getOemOrderBillNo());
				detail.setOemOrderBillId(bean.getOemOrderBillId());
				detail.setProductName(bean.getProductName());
				detail.setProcedureName(bean.getProcedureName());
				detail.setProcedureId(bean.getProcedureId());
				detail.setStyle(bean.getStyle());
				detail.setProductId(bean.getProductId());
				detail.setOriginBillNo(bean.getOriginBillNo());
				detail.setOriginBillId(bean.getOriginBillId());
				detail.setQty(bean.getQty().subtract(bean.getReconcilQty()));
				detail.setMoney(bean.getMoney().subtract(bean.getReconcilMoney()));
				detail.setPrice(detail.getMoney().divide(detail.getQty(), 4, RoundingMode.HALF_UP));
				detail.setTax(bean.getTax());
				detail.setTaxRateId(bean.getTaxRateId());
				detail.setPercent(bean.getPercent());
				detail.setNoTaxPrice(bean.getNoTaxPrice());
				detail.setNoTaxMoney(bean.getNoTaxMoney());
				detail.setPartName(bean.getPartName());
				detail.setDeliveryTime(bean.getMaster().getCreateTime());
				detail.setProcessRequire(bean.getProcessRequire());
				detail.setMemo(bean.getMemo());
				detailList.add(detail);
			}
		}
		map.put("order", order);
		if (CollectionUtils.isNotEmpty(order.getDetailList()))
		{
			map.put("customer", customer);
		}
		return "oem/reconcil/create";
	}

	/**
	 * <pre>
	 * 功能  - 保存对账单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:34:31, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("oem:reconcil:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "代工对账单", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody OemReconcil order, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(order))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		
		// 保存之前验证是否已对账完，前端的已校验过，后台再校验一次，防止保存重复对账单
		for (OemReconcilDetail detail : order.getDetailList())
		{
			if (null == detail.getSourceDetailId())
			{
				continue;
			}
			if (detail.getSourceBillType() == BillType.OEM_ED)
			{
				OemDeliverDetail source = serviceFactory.getOemDeliverService().getDetail(detail.getSourceDetailId());
				if (source.getReconcilQty().compareTo(source.getQty()) != -1)
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
			if (detail.getSourceBillType() == BillType.OEM_ER)
			{
				OemReturnDetail source = serviceFactory.getOemReturnService().getDetail(detail.getSourceDetailId());
				if (source.getReconcilQty().compareTo(source.getQty().abs()) != -1)
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
		}
		try
		{
			serviceFactory.getOemReconcilService().save(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			logger.error(ex.getMessage(), "OemReconcil:" + JsonUtils.toJson(order));
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			return returnErrorBody(I18nResource.FAIL);
		}
	}

	/**
	 * <pre>
	 * 页面  - 跳转到对账单编辑页面
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:35:14, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("oem:reconcil:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		OemReconcil order = serviceFactory.getOemReconcilService().get(id);
		map.put("order", order);
		return "oem/reconcil/edit";
	}

	/**
	 * <pre>
	 * 页面  - 跳转到对账单查看页面
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:35:28, zhengby
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions(value = {"oem:reconcil:create","oem:reconcil:view"},logical = Logical.OR)
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		OemReconcil order = serviceFactory.getOemReconcilService().get(id);
		// 临时字段的特殊处理
		for (OemReconcilDetail d : order.getDetailList())
		{
			d.setSourceBillTypeText(d.getSourceBillTypeText());
		}
		map.put("order", order);
		return "oem/reconcil/view";
	}

	/**
	 * <pre>
	 * 功能  - 打印对账单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:35:41, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("oem:reconcil:list")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> mapData = null;
		try
		{
			OemReconcil order = serviceFactory.getOemReconcilService().get(id);
			mapData = ObjectUtils.objectToMap(order);
			mapData.put("companyName", UserUtils.getCompany().getName());
			mapData.put("companyAddress", UserUtils.getCompany().getAddress());
			mapData.put("companyFax", UserUtils.getCompany().getFax());
			mapData.put("companyLinkName", UserUtils.getCompany().getLinkName());
			mapData.put("companyTel", UserUtils.getCompany().getTel());
			mapData.put("companyEmail", UserUtils.getCompany().getEmail());
			mapData.put("printDate", DateUtils.formatDateTime(new Date()));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return mapData;
	}

	/**
	 * <pre>
	 *  功能  - 修改对账单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:36:00, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("oem:reconcil:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "代工退货单", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody OemReconcil order, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(order))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		try
		{
			serviceFactory.getOemReconcilService().update(order);
			// 更新旧数据
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order);
		}
		catch (BusinessException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			return returnErrorBody(I18nResource.FAIL);
		}
	}

	/**
	 * <pre>
	 * 功能  - 删除对账单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:36:14, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("oem:reconcil:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "代工退货单", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		try
		{
			OemReconcil order = serviceFactory.getOemReconcilService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getOemReconcilService().delete(id);
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
	 * <pre>
	 * 功能  - 审核对账单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:36:26, zhengby
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("oem:reconcil:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().audit(BillType.OEM_EC, id, BoolValue.YES))
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
	 * 功能  - 反审核对账单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:36:37, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "auditCancel/{id}")
	@ResponseBody
	@RequiresPermissions("oem:reconcil:auditcancel")
	public AjaxResponseBody auditCancel(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().audit(BillType.OEM_EC, id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.OEM_EC, id);
			return returnErrorBody(map);
		}
	}

	/**
	 * <pre>
	 * 功能  - 强制完工
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:37:13, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("oem:reconcil:complete")
	public AjaxResponseBody complete(@RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateArrayNullOrEmpty(ids))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().forceComplete(OemReconcil.class, ids, BoolValue.YES))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(I18nResource.FAIL);
		}
	}

	/**
	 * <pre>
	 * 功能  - 取消强制完工
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:37:21, zhengby
	 */
	@RequestMapping(value = "completeCancel")
	@ResponseBody
	@RequiresPermissions("oem:reconcil:completecancel")
	public AjaxResponseBody completeCancel(@RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateArrayNullOrEmpty(ids))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().forceComplete(OemReconcil.class, ids, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(I18nResource.FAIL);
		}
	}
}
