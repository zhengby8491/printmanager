/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月13日 下午6:06:40
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.oem;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.huayin.printmanager.persist.entity.oem.OemDeliver;
import com.huayin.printmanager.persist.entity.oem.OemDeliverDetail;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工管理  - 代工送货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月13日下午6:06:40, zhengby
 */
@Controller
@RequestMapping(value = "${basePath}/oem/deliver")
public class OemDeliveryController extends BaseController
{
	/**
	 * <pre>
	 * 页面  - 跳转到送货单列表查看页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月14日 上午11:07:57, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("oem:deliver:list")
	public String list()
	{
		return "oem/deliver/list";
	}
	
	/**
	 * <pre>
	 * 数据  - 返回送货单列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月14日 上午11:08:31, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@RequiresPermissions("oem:deliver:list")
	@ResponseBody
	public SearchResult<OemDeliver> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OemDeliver> result = serviceFactory.getOemDeliverService().findByCondition(queryParam);
		OemDeliver order = new OemDeliver();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal totalNoTaxMony = new BigDecimal(0);
		for (OemDeliver o : result.getResult())
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
	 * 页面  - 跳转到代工送货明细查看列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月14日 上午11:09:11, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("oem:deliver:detail:list")
	public String detailList()
	{
		return "oem/deliver/detailList";
	}

	/**
	 * <pre>
	 * 数据  - 返回送货单明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月14日 上午11:09:46, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@RequiresPermissions("oem:deliver:detail:list")
	@ResponseBody
	public SearchResult<OemDeliverDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OemDeliverDetail> result = serviceFactory.getOemDeliverService().findDetailBycondition(queryParam);
		OemDeliverDetail detail = new OemDeliverDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		BigDecimal totalQty = new BigDecimal(0);
		for (OemDeliverDetail o : result.getResult())
		{
			money = money.add(o.getMoney());
			tax = tax.add(o.getTax());
			noTaxMoney = noTaxMoney.add(o.getNoTaxMoney());
			totalQty = totalQty.add(o.getQty());
		}
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setQty(totalQty);
		result.getResult().add(detail);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 创建送货单
	 * </pre>
	 * @param ids
	 * @param map
	 * @return
	 * @since 1.0, 2018年3月14日 上午9:09:48, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("oem:deliver:create")
	public String create(String[] ids, ModelMap map)
	{
		if (Validate.validateArrayNullOrEmpty(ids))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		
		OemDeliver order = new OemDeliver();
		List<OemDeliverDetail> detailList = Lists.newArrayList();
		for (String id : ids)
		{
			Long id_ = StringUtils.toLong(id);
   
			OemOrderDetail detail = serviceFactory.getOemOrderService().getDetailHasMaster(id_);
      if (detail.getDeliverQty().compareTo(detail.getQty()) != -1)
      {
        continue;
      }
			order.setCustomerId(detail.getMaster().getCustomerId());
			order.setOriginCompanyId(detail.getMaster().getOriginCompanyId());
			order.setLinkName(detail.getMaster().getLinkName());
			order.setEmployeeId(detail.getMaster().getEmployeeId());
			order.setDeliveryAddress(detail.getMaster().getDeliveryAddress());
			order.setDeliveryClassId(detail.getMaster().getDeliveryClassId());
			order.setMobile(detail.getMaster().getMobile());
			order.setSettlementClassId(detail.getMaster().getSettlementClassId());
			OemDeliverDetail new_detail = new OemDeliverDetail();
			new_detail.setSourceBillNo(detail.getMaster().getBillNo());
			new_detail.setSourceBillType(detail.getSourceBillType());
			new_detail.setSourceQty(detail.getQty());
			new_detail.setSourceId(detail.getMasterId());
			new_detail.setSourceDetailId(detail.getId());
			new_detail.setQty(detail.getQty().subtract(detail.getDeliverQty()));
			new_detail.setStyle(detail.getStyle());
			new_detail.setProductName(detail.getProductName());
			new_detail.setProductId(detail.getProductId());
			new_detail.setProcedureClassId(detail.getProcedureClassId());
			new_detail.setProcedureName(detail.getProcedureName());
			new_detail.setProcedureId(detail.getProcedureId());
			new_detail.setOriginCompanyId(detail.getOriginCompanyId());
			new_detail.setOriginProcedureName(detail.getOriginProcedureName());
			new_detail.setOriginProcedureId(detail.getOriginProcedureId());
			new_detail.setOriginBillNo(detail.getOriginBillNo());
			new_detail.setOriginBillId(detail.getOriginBillId());
			new_detail.setProcessRequire(detail.getProcessRequire());
			new_detail.setTaxRateId(detail.getTaxRateId());
			new_detail.setPercent(detail.getPercent());
			new_detail.setPartName(detail.getPartName());
			new_detail.setMemo(detail.getMemo());
			
			if ((detail.getMoney().subtract(detail.getDeliverMoney())).doubleValue() < 0)
			{
				BigDecimal deliverPrice = detail.getDeliverMoney().divide(detail.getDeliverQty(), 4, RoundingMode.HALF_UP);
				new_detail.setMoney(deliverPrice.multiply(new_detail.getQty()));
			}
			else
			{
				new_detail.setMoney(detail.getMoney().subtract(detail.getDeliverMoney()));
			}
			new_detail.setPrice(new_detail.getMoney().divide(new_detail.getQty(), 4, RoundingMode.HALF_UP));
			new_detail.setTax(detail.getTax());
			new_detail.setTaxRateId(detail.getTaxRateId());
			new_detail.setPercent(detail.getPercent());
			new_detail.setNoTaxPrice(detail.getNoTaxPrice());
			new_detail.setNoTaxMoney(detail.getNoTaxMoney());
			new_detail.setMemo(detail.getMemo());
			detailList.add(new_detail);
		}
		Customer customer = serviceFactory.getCustomerService().get(order.getCustomerId());
		order.setDetailList(detailList);
		

		map.put("order", order);
		map.put("customer", customer);
		
		return "oem/deliver/create";
	}

	/**
	 * <pre>
	 * 功能  - 保存送货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年3月8日 上午11:17:46, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("oem:deliver:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "代工送货", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody OemDeliver order, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(order))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		// 保存之前验证是否已对账完，前端的已校验过，后台再校验一次，防止保存重复对账单
		for (OemDeliverDetail detail : order.getDetailList())
		{
			if (null == detail.getSourceDetailId())
			{
				continue;
			}
			OemOrderDetail source = serviceFactory.getOemOrderService().getDetail(detail.getSourceDetailId());
			if (source.getDeliverQty().compareTo(source.getQty()) != -1)
			{
				return returnErrorBody("单据已生成，无需重复操作");
			}
		}
		try
		{
			serviceFactory.getOemDeliverService().save(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			logger.error(ex.getMessage(), "OemDeliver:" + JsonUtils.toJson(order));
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			return returnErrorBody(I18nResource.FAIL);
		}
	}

	/**
	 * <pre>
	 * 页面  - 跳转到送货单编辑页面
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月8日 上午11:17:22, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("oem:deliver:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map,I18nResource.VALIDATE_FAIL);
		}
		OemDeliver order = serviceFactory.getOemDeliverService().get(id);
		map.put("order", order);
		return "oem/deliver/edit";
	}

	/**
	 * <pre>
	 * 页面  - 跳转到送货订单查看页面
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月12日 下午4:54:41, zhengby
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions(value = {"oem:deliver:create", "oem:deliver:view"},logical = Logical.OR)
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map,I18nResource.VALIDATE_FAIL);
		}
		OemDeliver order = serviceFactory.getOemDeliverService().get(id);
		map.put("order", order);
		return "oem/deliver/view";
	}

	/**
	 * <pre>
	 * 功能  - 打印送货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月8日 上午11:09:07, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("oem:deliver:list")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> mapData = null;
		try
		{
			OemDeliver order = serviceFactory.getOemDeliverService().get(id);
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
	 * 功能  - 修改送货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年3月8日 下午1:44:18, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("oem:order:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "代工送货单", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody OemDeliver order, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(order.getId()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		try
		{
			serviceFactory.getOemDeliverService().update(order);
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
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 功能  - 删除送货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月8日 下午1:46:38, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("oem:deliver:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "代工送货", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		try
		{
			OemDeliver order = serviceFactory.getOemDeliverService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getOemDeliverService().delete(id);
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
	 * 功能  - 审核送货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月13日 上午10:19:24, zhengby
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("oem:deliver:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().audit(BillType.OEM_ED, id, BoolValue.YES))
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
	 * 功能  - 反审核送货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月13日 上午10:19:52, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "auditCancel/{id}")
	@ResponseBody
	@RequiresPermissions("oem:deliver:auditcancel")
	public AjaxResponseBody auditCancel(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().audit(BillType.OEM_ED, id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.OEM_ED, id);
			return returnErrorBody(map);
		}
	}

	/**
	 * <pre>
	 * 功能  - 强制完工
	 * </pre>
	 * @param tableType
	 * @param idsstr
	 * @return
	 * @since 1.0, 2018年3月13日 上午10:20:46, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("oem:deliver:complete")
	public AjaxResponseBody complete(@RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateArrayNullOrEmpty(ids))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().forceComplete(OemDeliver.class, ids, BoolValue.YES))
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
	 * @param tableType
	 * @param idsstr
	 * @return
	 * @since 1.0, 2018年3月13日 上午10:21:02, zhengby
	 */
	@RequestMapping(value = "completeCancel")
	@ResponseBody
	@RequiresPermissions("oem:deliver:completecancel")
	public AjaxResponseBody completeCancel(@RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateArrayNullOrEmpty(ids))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().forceComplete(OemDeliver.class, ids, BoolValue.NO))
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
	 * 页面 -  快速选择送货单列表
	 * </pre>
	 * @param multiple
	 * @param customerId
	 * @param rowIndex
	 * @param map
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年3月15日 上午9:17:33, zhengby
	 */
	@RequestMapping(value = "quick_select")
	public String quickSelectDeliver(Boolean multiple, Long customerId, Integer rowIndex, ModelMap map) throws Exception
	{
		map.put("multiple", multiple);
		map.put("customerId", customerId);
		map.put("rowIndex", rowIndex);
		return "oem/deliver/quick_select";
	}
	
	/**
	 * <pre>
	 * 数据 - 获取代工送货单明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月14日 下午7:17:43, zhengby
	 */
	@RequestMapping(value = "ajaxDeliverList")
	@ResponseBody
	public SearchResult<OemDeliverDetail> ajaxDeliverList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OemDeliverDetail> result = serviceFactory.getOemDeliverService().findDeliverRecords(queryParam);
		return result;
	}
}
