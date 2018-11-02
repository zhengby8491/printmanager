/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月7日 下午1:55:26
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.oem;

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
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.oem.OemOrder;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.service.vo.SumVo;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工管理  - 代工订单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月7日下午1:55:26, zhengby
 */
@Controller
@RequestMapping(value = "${basePath}/oem/order")
public class OemOrderController extends BaseController
{
	/**
	 * <pre>
	 * 页面  - 跳转到代工订单列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月7日 下午2:02:25, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("oem:order:list")
	public String list()
	{
		return "oem/order/list";
	}

	/**
	 * <pre>
	 * 数据  - 返回代工订单列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月12日 下午7:28:35, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@RequiresPermissions("oem:order:list")
	@ResponseBody
	public SearchResult<OemOrder> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OemOrder> result = serviceFactory.getOemOrderService().findByCondition(queryParam);
		OemOrder order = new OemOrder();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal totalNoTaxMony = new BigDecimal(0);
		for (OemOrder o : result.getResult())
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
	 * 页面  - 跳转到代工订单明细列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月7日 下午2:02:48, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("oem:order:detail:list")
	public String detailList()
	{
		return "oem/order/detailList";
	}

	/**
	 * <pre>
	 * 数据  - 返回代工订单明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月12日 下午7:29:15, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@RequiresPermissions("oem:order:detail:list")
	@ResponseBody
	public SearchResult<OemOrderDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OemOrderDetail> result = serviceFactory.getOemOrderService().findDetailBycondition(queryParam);
		OemOrderDetail detail = new OemOrderDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		BigDecimal deliverQty = new BigDecimal(0);// 已送货数量
		BigDecimal totalQty = new BigDecimal(0);
		for (OemOrderDetail o : result.getResult())
		{
			money = money.add(o.getMoney());
			tax = tax.add(o.getTax());
			noTaxMoney = noTaxMoney.add(o.getNoTaxMoney());
			deliverQty = deliverQty.add(o.getDeliverQty());
			totalQty = totalQty.add(o.getQty());
		}
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setDeliverQty(deliverQty);
		detail.setQty(totalQty);
		result.getResult().add(detail);

		return result;
	}

	/**
	 * <pre>
	 * 页面  - 跳转到创建代工订单页面
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年3月8日 上午11:12:12, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("oem:order:create")
	public String create(ModelMap map)
	{
		return "oem/order/create";
	}

	/**
	 * <pre>
	 * 页面 - 创建来自代工平台
	 * </pre>
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年3月20日 上午10:38:40, think
	 */
	@RequestMapping(value = "createFromTransmitOrder")
	@RequiresPermissions("oem:order:create")
	public String createFromTransmitOrder(ModelMap map, Long oemCustomerId, Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(oemCustomerId))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}

		if (Validate.validateArrayNullOrEmpty(ids))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}

		Customer customer = serviceFactory.getCustomerService().get(oemCustomerId);
		if (null == customer || StringUtils.isBlank(customer.getOriginCompanyId()))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}

		// 构造order
		OemOrder order = new OemOrder();
		List<OemOrderDetail> detailList = Lists.newArrayList();
		order.setDetailList(detailList);

		// 客户id
		order.setCustomerId(customer.getId());
		// 客户名称
		order.setCustomerName(customer.getName());
		// 联 系 人
		order.setLinkName(customer.getDefaultAddress().getUserName());
		// 联系电话
		order.setMobile(customer.getDefaultAddress().getMobile());
		// 销售员
		order.setEmployeeId(customer.getEmployeeId());
		// 送货地址
		order.setDeliveryAddress(customer.getDefaultAddress().getAddress());
		// 送货方式
		order.setDeliveryClassId(customer.getDeliveryClassId());
		// 付款方式
		order.setPaymentClassId(customer.getPaymentClassId());
		// 源单公司
		order.setOriginCompanyId(customer.getOriginCompanyId());
		// 设置detailList
		for (Long id : ids)
		{
			OutSourceProcessDetail outSourceDetail = serviceFactory.getOemZeroService().getOutSourceProcessDetail(id);

			// 先查询是否已经创建过当前发外工序
			OemOrderDetail oemOrderDetail = serviceFactory.getOemOrderService().getDetail(outSourceDetail.getCompanyId(), outSourceDetail.getId());
			if (oemOrderDetail != null)
			{
				continue;
			}

			OemOrderDetail _detail = new OemOrderDetail();
			// 查询工序是否存在，若存在则自动填上工序名称
			if (StringUtils.isNotBlank(outSourceDetail.getProcedureName()))
			{
				QueryParam queryParam = new QueryParam();
				queryParam.setProcedureName(outSourceDetail.getProcedureName());
				Procedure procedure = serviceFactory.getProcedureService().findByPrecise(queryParam);
				if (procedure != null)
				{
					_detail.setProcedureName(procedure.getName());
					_detail.setProcedureClassId(procedure.getProcedureClassId());
					_detail.setProcedureId(procedure.getId());
				}
			}
			List<String> companyList = Lists.newArrayList();
			companyList.add(outSourceDetail.getCompanyId());
			// 以companyId作为key，获取该公司下的工单产品列表
			Map<String, Map<Long, List<WorkProduct>>> workProductMap = serviceFactory.getZeroService().findAllProductForMap(companyList);
			for (String companyId : companyList)
			{
				Map<Long, List<WorkProduct>> _map = workProductMap.get(companyId);
				if (!_map.isEmpty())
				{
					List<WorkProduct> productList = _map.get(outSourceDetail.getWorkId());
					if (null != productList)
					{
						outSourceDetail.setProductList(productList);
					}
				}
			}
			// 产品名称
			_detail.setProductName(outSourceDetail.getProductNames());
			// 产品id
			//_detail.setProductId(outSourceDetail.getProductId());
			// 部件名称
			_detail.setPartName(outSourceDetail.getPartName());
			// 源单工序名称
			_detail.setOriginProcedureName(outSourceDetail.getProcedureName());
			// 源单工序ID
			_detail.setOriginProcedureId(outSourceDetail.getId());
			// 发外公司id
			_detail.setOriginCompanyId(outSourceDetail.getCompanyId());
			// 源单明细id
			_detail.setSourceDetailId(outSourceDetail.getId());
			// 源单号
			_detail.setSourceBillNo(outSourceDetail.getMaster().getBillNo());
			// 源单号id
			_detail.setSourceId(outSourceDetail.getMaster().getId());
			// 源单类型
			_detail.setSourceBillType(outSourceDetail.getMaster().getBillType());
			// 源单单号
			_detail.setOriginBillNo(outSourceDetail.getMaster().getBillNo());
			// 源单单号id
			_detail.setOriginBillId(outSourceDetail.getMaster().getId());
			// 源单数量
			_detail.setSourceQty(outSourceDetail.getQty());
			// 生产数量
			_detail.setProduceNum(outSourceDetail.getProduceNum());
			// 产品规格
			_detail.setStyle(outSourceDetail.getStyle());
			// 生产数量
			_detail.setQty(outSourceDetail.getQty());
			// 单价
			_detail.setPrice(outSourceDetail.getPrice());
			// 金额
			_detail.setMoney(outSourceDetail.getMoney());
			// 不含税单价
			_detail.setNoTaxPrice(outSourceDetail.getNoTaxPrice());
			// 不含税金额
			_detail.setNoTaxMoney(outSourceDetail.getNoTaxMoney());
			// 税额
			_detail.setTax(outSourceDetail.getTax());
			// 税率ID
			_detail.setTaxRateId(customer.getTaxRateId());
			// 送货时间
			_detail.setDeliveryTime(outSourceDetail.getMaster().getDeliveryTime());
			// 加工要求
			_detail.setProcessRequire(outSourceDetail.getProcessRequire());
			// 备注
			_detail.setMemo(outSourceDetail.getMemo());

			detailList.add(_detail);
		}

		if(detailList.size() > 0)
		{
			map.put("order", order);
			map.put("customer", customer);
		}

		return "oem/order/create";
	}

	/**
	 * <pre>
	 * 功能  - 保存代工订单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年3月8日 上午11:17:46, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("oem:order:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "代工订单", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody OemOrder order, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(order))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
    // 先查询是否已经创建过当前发外工序
    for (OemOrderDetail oemOrderDetail : order.getDetailList())
    {
      if (null != oemOrderDetail.getOriginCompanyId() && null != serviceFactory.getOemOrderService().getDetail(oemOrderDetail.getOriginCompanyId(), oemOrderDetail.getOriginBillId()))
      {
        return returnErrorBody("单据已生成，无需重复操作");
      }
    }
		try
		{
			serviceFactory.getOemOrderService().save(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			logger.error(ex.getMessage(), "OemOrder:" + JsonUtils.toJson(order));
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			return returnErrorBody(I18nResource.FAIL);
		}
	}

	/**
	 * <pre>
	 * 页面  - 跳转到代工订单编辑页面
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月8日 上午11:17:22, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("oem:order:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		OemOrder order = serviceFactory.getOemOrderService().get(id);
		map.put("order", order);
		return "oem/order/edit";
	}

	/**
	 * <pre>
	 * 页面  - 跳转到代工订单查看页面
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月12日 下午4:54:41, zhengby
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions(value = { "oem:order:create", "oem:order:view" }, logical = Logical.OR)
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		OemOrder order = serviceFactory.getOemOrderService().get(id);
		map.put("order", order);
		return "oem/order/view";
	}

	/**
	 * <pre>
	 * 功能  - 打印代工订单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月8日 上午11:09:07, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("oem:order:list")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> mapData = null;
		try
		{
			OemOrder order = serviceFactory.getOemOrderService().get(id);
			mapData = ObjectUtils.objectToMap(order);
			mapData.put("companyName", UserUtils.getCompany().getName());
			mapData.put("companyAddress", UserUtils.getCompany().getAddress());
			mapData.put("companyFax", UserUtils.getCompany().getFax());
			mapData.put("companyLinkName", UserUtils.getCompany().getLinkName());
			mapData.put("companyTel", UserUtils.getCompany().getTel());
			mapData.put("companyEmail", UserUtils.getCompany().getEmail());
			mapData.put("deliveryClassName", order.getDeliverClassName());
			mapData.put("printDate", DateUtils.formatDateTime(new Date()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mapData;
	}

	/**
	 * <pre>
	 * 功能  - 修改代工订单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年3月8日 下午1:44:18, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("oem:order:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "代工订单", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody OemOrder order, HttpServletRequest request)
	{
		try
		{
			serviceFactory.getOemOrderService().update(order);
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
	 * 功能  - 删除代工订单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月8日 下午1:46:38, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("oem:order:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "代工订单", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		try
		{
			OemOrder order = serviceFactory.getOemOrderService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getOemOrderService().delete(id);
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
	 * 功能  - 审核代工订单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月13日 上午10:19:24, zhengby
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("oem:order:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().audit(BillType.OEM_EO, id, BoolValue.YES))
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
	 * 功能  - 反审核代工订单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月13日 上午10:19:52, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "auditCancel/{id}")
	@ResponseBody
	@RequiresPermissions("oem:order:auditcancel")
	public AjaxResponseBody auditCancel(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().audit(BillType.OEM_EO, id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.OEM_EO, id);
			return returnErrorBody(map);
		}
	}

	/**
	 * <pre>
	 * 功能  - 审核所有代工订单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月13日 上午10:20:08, zhengby
	 */
	@RequestMapping(value = "checkAll")
	@ResponseBody
	@RequiresPermissions("oem:order:checkAll")
	public AjaxResponseBody checkAll()
	{
		if (serviceFactory.getSaleOrderService().checkAll())
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
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
	@RequiresPermissions("oem:order:complete")
	public AjaxResponseBody complete(@RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateArrayNullOrEmpty(ids))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().forceComplete(OemOrder.class, ids, BoolValue.YES))
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
	@RequiresPermissions("oem:order:completecancel")
	public AjaxResponseBody completeCancel(@RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateArrayNullOrEmpty(ids))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().forceComplete(OemOrder.class, ids, BoolValue.NO))
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
	 * 功能 - 查询代工订单进度表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:10:34, zhengby
	 */
	@RequestMapping(value = "ajaxFlowList")
	@RequiresPermissions("oem:detailflowlist:list")
	@ResponseBody
	public SearchResult<OemOrderDetail> ajaxFlowList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OemOrderDetail> result = serviceFactory.getOemOrderService().findFlowByCondition(queryParam);
		OemOrderDetail detail = new OemOrderDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		BigDecimal totalQty = new BigDecimal(0);
		for (OemOrderDetail oemOrderDetail : result.getResult())
		{
			money = money.add(oemOrderDetail.getMoney());
			tax = tax.add(oemOrderDetail.getTax());
			noTaxMoney = noTaxMoney.add(oemOrderDetail.getNoTaxMoney());
			totalQty = totalQty.add(oemOrderDetail.getQty());
		}
		OemOrder order = new OemOrder();
		detail.setMaster(order);
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setQty(totalQty);
		result.getResult().add(detail);
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 获取代工订单创建日期列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月26日 下午7:06:27, zhengby
	 */
	@RequestMapping(value = "getYearsFromOemOrder")
	@ResponseBody
	public List<OemOrder> getYearsFromOemOrderByCustomerName()
	{
		List<OemOrder> list = serviceFactory.getOemOrderService().getYearsFromOemOrder();
		return list;
	}

	/**
	 * <pre>
	 * 页面  - 代工订单汇总（按客户）
	 * </pre>
	 * @param type
	 * @return
	 * @since 1.0, 2018年3月26日 下午6:01:53, zhengby
	 */
	@RequestMapping(value = "sumCustomerList/{type}")
	@RequiresPermissions("oem:sum:customer:list")
	public String sumCustomerList(@PathVariable String type)
	{
		return "oem/sum/customer_" + type + "_list";
	}

	/**
	 * <pre>
	 * 页面  - 代工订单汇总（按工序）
	 * </pre>
	 * @param type
	 * @return
	 * @since 1.0, 2018年3月26日 下午6:13:53, zhengby
	 */
	@RequestMapping(value = "sumProcedureList/{type}")
	@RequiresPermissions("oem:sum:procedure:list")
	public String sumProductList(@PathVariable String type)
	{
		return "oem/sum/procedure_" + type + "_list";
	}

	/**
	 * <pre>
	 * 页面  - 代工订单汇总（按销售员）
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月26日 下午6:15:18, zhengby
	 */
	@RequestMapping(value = "sumSellerList")
	@RequiresPermissions("oem:sum:seller:list")
	public String sumSellerList()
	{
		return "oem/sum/seller_list";
	}

	/**
	 * <pre>
	 * 数据  - 代工订单汇总(按客户)
	 * </pre>
	 * @param queryParam
	 * @param type
	 * @return
	 * @since 1.0, 2018年3月26日 下午6:16:43, zhengby
	 */
	@RequestMapping(value = "sumOemOrderByCustomer/{type}")
	@RequiresPermissions("oem:sum:customer:list")
	@ResponseBody
	public SearchResult<SumVo> sumOemOrderByCustomer(@RequestBody QueryParam queryParam, @PathVariable String type)
	{
		SearchResult<SumVo> result = serviceFactory.getOemOrderService().sumOemOrderByCustomer(queryParam, type);
		SumVo vo = new SumVo();
		BigDecimal sumMoney = new BigDecimal(0);
		BigDecimal january = new BigDecimal(0);
		BigDecimal february = new BigDecimal(0);
		BigDecimal march = new BigDecimal(0);
		BigDecimal april = new BigDecimal(0);
		BigDecimal may = new BigDecimal(0);
		BigDecimal june = new BigDecimal(0);
		BigDecimal july = new BigDecimal(0);
		BigDecimal august = new BigDecimal(0);
		BigDecimal september = new BigDecimal(0);
		BigDecimal october = new BigDecimal(0);
		BigDecimal november = new BigDecimal(0);
		BigDecimal december = new BigDecimal(0);
		for (SumVo sumVo : result.getResult())
		{
			sumMoney = sumMoney.add(sumVo.getSumMoney());
			january = january.add(sumVo.getJanuary());
			february = february.add(sumVo.getFebruary());
			march = march.add(sumVo.getMarch());
			april = april.add(sumVo.getApril());
			may = may.add(sumVo.getMay());
			june = june.add(sumVo.getJune());
			july = july.add(sumVo.getJuly());
			august = august.add(sumVo.getAugust());
			september = september.add(sumVo.getSeptember());
			october = october.add(sumVo.getOctober());
			november = november.add(sumVo.getNovember());
			december = december.add(sumVo.getDecember());
		}
		vo.setSumMoney(sumMoney);
		vo.setJanuary(january);
		vo.setFebruary(february);
		vo.setMarch(march);
		vo.setApril(april);
		vo.setMay(may);
		vo.setJune(june);
		vo.setJuly(july);
		vo.setAugust(august);
		vo.setSeptember(september);
		vo.setOctober(october);
		vo.setNovember(november);
		vo.setDecember(december);
		result.getResult().add(vo);
		return result;
	}

	/**
	 * <pre>
	 * 数据  - 代工订单汇总(按工序)
	 * </pre>
	 * @param queryParam
	 * @param type
	 * @return
	 * @since 1.0, 2018年3月26日 下午7:18:00, zhengby
	 */
	@RequestMapping(value = "sumOemOrderByProcedure/{type}")
	@RequiresPermissions("oem:sum:procedure:list")
	@ResponseBody
	public SearchResult<SumVo> sumOemOrderByProcedure(@RequestBody QueryParam queryParam, @PathVariable String type)
	{
		SearchResult<SumVo> result = serviceFactory.getOemOrderService().sumOemOrderByProcedure(queryParam, type);
		SumVo vo = new SumVo();
		BigDecimal sumMoney = new BigDecimal(0);
		BigDecimal january = new BigDecimal(0);
		BigDecimal february = new BigDecimal(0);
		BigDecimal march = new BigDecimal(0);
		BigDecimal april = new BigDecimal(0);
		BigDecimal may = new BigDecimal(0);
		BigDecimal june = new BigDecimal(0);
		BigDecimal july = new BigDecimal(0);
		BigDecimal august = new BigDecimal(0);
		BigDecimal september = new BigDecimal(0);
		BigDecimal october = new BigDecimal(0);
		BigDecimal november = new BigDecimal(0);
		BigDecimal december = new BigDecimal(0);
		for (SumVo sumVo : result.getResult())
		{
			sumMoney = sumMoney.add(sumVo.getSumMoney());
			january = january.add(sumVo.getJanuary());
			february = february.add(sumVo.getFebruary());
			march = march.add(sumVo.getMarch());
			april = april.add(sumVo.getApril());
			may = may.add(sumVo.getMay());
			june = june.add(sumVo.getJune());
			july = july.add(sumVo.getJuly());
			august = august.add(sumVo.getAugust());
			september = september.add(sumVo.getSeptember());
			october = october.add(sumVo.getOctober());
			november = november.add(sumVo.getNovember());
			december = december.add(sumVo.getDecember());
		}
		vo.setSumMoney(sumMoney);
		vo.setJanuary(january);
		vo.setFebruary(february);
		vo.setMarch(march);
		vo.setApril(april);
		vo.setMay(may);
		vo.setJune(june);
		vo.setJuly(july);
		vo.setAugust(august);
		vo.setSeptember(september);
		vo.setOctober(october);
		vo.setNovember(november);
		vo.setDecember(december);
		result.getResult().add(vo);
		return result;
	}

	/**
	 * <pre>
	 * 数据  - 代工订单汇总(按销售员)
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月27日 上午10:22:22, zhengby
	 */
	@RequestMapping(value = "sumOemOrderBySeller")
	@RequiresPermissions("oem:sum:seller:list")
	@ResponseBody
	public SearchResult<SumVo> sumOemOrderBySeller(@RequestBody QueryParam queryParam)
	{
		SearchResult<SumVo> result = serviceFactory.getOemOrderService().sumOemOrderBySeller(queryParam);
		SumVo vo = new SumVo();
		BigDecimal sumMoney = new BigDecimal(0);
		BigDecimal january = new BigDecimal(0);
		BigDecimal february = new BigDecimal(0);
		BigDecimal march = new BigDecimal(0);
		BigDecimal april = new BigDecimal(0);
		BigDecimal may = new BigDecimal(0);
		BigDecimal june = new BigDecimal(0);
		BigDecimal july = new BigDecimal(0);
		BigDecimal august = new BigDecimal(0);
		BigDecimal september = new BigDecimal(0);
		BigDecimal october = new BigDecimal(0);
		BigDecimal november = new BigDecimal(0);
		BigDecimal december = new BigDecimal(0);
		for (SumVo sumVo : result.getResult())
		{
			sumMoney = sumMoney.add(sumVo.getSumMoney());
			january = january.add(sumVo.getJanuary());
			february = february.add(sumVo.getFebruary());
			march = march.add(sumVo.getMarch());
			april = april.add(sumVo.getApril());
			may = may.add(sumVo.getMay());
			june = june.add(sumVo.getJune());
			july = july.add(sumVo.getJuly());
			august = august.add(sumVo.getAugust());
			september = september.add(sumVo.getSeptember());
			october = october.add(sumVo.getOctober());
			november = november.add(sumVo.getNovember());
			december = december.add(sumVo.getDecember());
		}
		vo.setSumMoney(sumMoney);
		vo.setJanuary(january);
		vo.setFebruary(february);
		vo.setMarch(march);
		vo.setApril(april);
		vo.setMay(may);
		vo.setJune(june);
		vo.setJuly(july);
		vo.setAugust(august);
		vo.setSeptember(september);
		vo.setOctober(october);
		vo.setNovember(november);
		vo.setDecember(december);
		result.getResult().add(vo);
		return result;
	}
}
