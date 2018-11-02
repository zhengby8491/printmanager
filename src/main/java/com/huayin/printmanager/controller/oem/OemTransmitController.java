/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月9日 上午8:55:17
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.oem.OemBaseEntityDetail;
import com.huayin.printmanager.persist.entity.oem.OemDeliver;
import com.huayin.printmanager.persist.entity.oem.OemDeliverDetail;
import com.huayin.printmanager.persist.entity.oem.OemOrder;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.persist.entity.oem.OemReturn;
import com.huayin.printmanager.persist.entity.oem.OemReturnDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 代工管理  - 代工未清
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月9日上午8:55:17, zhengby
 */
@Controller
@RequestMapping(value = "${basePath}/oem/transmit")
public class OemTransmitController extends BaseController
{
	/**
	 * <pre>
	 * 页面  - 跳转到未送货列表
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param map
	 * @return
	 * @since 1.0, 2018年3月13日 下午4:15:01, zhengby
	 */
	@RequestMapping(value = "toDeliver")
	@RequiresPermissions("oem:deliver:transmit")
	public String transmitDeliver(Date dateMin, Date dateMax, ModelMap map)
	{
		map.put("dateMin", dateMin);
		map.put("dateMax", dateMax);
		return "oem/transmit/to_deliver";
	}

	/**
	 * <pre>
	 * 数据  - 返回未送货列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月13日 下午4:15:59, zhengby
	 */
	@RequestMapping(value = "toDeliverList")
	@ResponseBody
	public SearchResult<OemOrderDetail> transmitDeliverList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OemOrderDetail> result = serviceFactory.getOemTransmitService().findOrderForTransmitDeliver(queryParam);

		BigDecimal qty = new BigDecimal(0);
		BigDecimal deliverQty = new BigDecimal(0);
		for (OemOrderDetail s : result.getResult())
		{
			qty = qty.add(s.getQty());
			deliverQty = deliverQty.add(s.getDeliverQty());
		}
		OemOrderDetail detail = new OemOrderDetail();
		detail.setQty(qty);
		detail.setDeliverQty(deliverQty);
		result.getResult().add(detail);
		return result;
	}

	/**
	 * <pre>
	 * 页面  - 跳转到送货未对账页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月13日 下午4:16:44, zhengby
	 */
	@RequestMapping(value = "toReconcil")
	@RequiresPermissions("oem:reconcil:transmit")
	public String transmitReconcil()
	{
		return "oem/transmit/to_reconcil";
	}

	/**
	 * <pre>
	 * 数据  - 返回送货未对账列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月13日 下午4:18:26, zhengby
	 */
	@RequestMapping(value = "toReconcilList")
	@ResponseBody
	public SearchResult<OemBaseEntityDetail> transmitReconcilList(@RequestBody QueryParam queryParam)
	{
		// 送货
		SearchResult<OemDeliverDetail> deliverResult = serviceFactory.getOemTransmitService().findDeliverForTransmitReconcil(queryParam);
		// 退货
		SearchResult<OemReturnDetail> returnResult = serviceFactory.getOemTransmitService().findReturnForTransmitReconcil(queryParam);
		// 合并
		SearchResult<OemBaseEntityDetail> result = new SearchResult<OemBaseEntityDetail>();
		result.setResult(new ArrayList<OemBaseEntityDetail>());
		result.getResult().addAll(deliverResult.getResult());
		result.getResult().addAll(returnResult.getResult());
		result.setCount(deliverResult.getCount() + returnResult.getCount());
		BigDecimal qty = new BigDecimal(0);
		BigDecimal price = new BigDecimal(0);
		BigDecimal reconcilQty = new BigDecimal(0);
		for (OemDeliverDetail s : deliverResult.getResult())
		{
			qty = qty.add(s.getQty());
			price = price.add(s.getPrice());
			reconcilQty = reconcilQty.add(s.getReconcilQty());
		}
		for (OemReturnDetail s2 : returnResult.getResult())
		{
			qty = qty.subtract(s2.getQty());
			price = price.subtract(s2.getPrice());
			reconcilQty = reconcilQty.add(s2.getReconcilQty());
		}

		OemOrderDetail detail = new OemOrderDetail();
		detail.setQty(qty);
		detail.setPrice(price.setScale(2, RoundingMode.HALF_UP));
		detail.setReconcilQty(reconcilQty);
		result.getResult().add(detail);

		return result;
	}

	/**
	 * <pre>
	 * 页面 - 代工平台（来自发外订单）
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月19日 上午9:35:23, think
	 */
	@RequestMapping(value = "toOrder")
	@RequiresPermissions("oem:order:transmit")
	public String toOrder()
	{
		return "oem/transmit/to_order";
	}

	/**
	 * <pre>
	 * 数据 - 代工平台（来自发外订单）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月19日 上午9:35:40, think
	 */
	@RequestMapping(value = "toOrderList")
	@ResponseBody
	public SearchResult<OutSourceProcessDetail> toOrderList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceProcessDetail> result = serviceFactory.getOemZeroService().findTransmitOrderByOutSource(queryParam);

		// 设置基础资料是否存在代工平台客户（用于代工平台创建时，提示绑定）
		SearchResult<Customer> customerListResult = serviceFactory.getZeroService().findCustomerList(queryParam);
		// 查询所有工单WorkPart2Product，并设置到发外加工明细中
		List<String> companyList = Lists.newArrayList();
		// Map<代工平台公司名称, Customer>
		Map<String, Customer> customerNameMap = Maps.newHashMap();
		for (Customer customer : customerListResult.getResult())
		{
			customerNameMap.put(customer.getName(), customer);
		}
		for (OutSourceProcessDetail osDetail : result.getResult())
		{
			Company oemCompany = osDetail.getOemCompany();
			Customer oemCustomer = osDetail.getOemCustomer();
			if (oemCompany != null && oemCustomer == null)
			{
				Customer _self2 = customerNameMap.get(oemCompany.getName());
				if (null != _self2)
				{
					oemCustomer = new Customer();
					oemCustomer.setId(_self2.getId());
					oemCustomer.setOriginCompanyExit(BoolValue.YES);
					osDetail.setOemCustomer(oemCustomer);
				}
			}
			companyList.add(osDetail.getCompanyId());
		}
		// 以companyId作为key，获取该公司下的工单产品列表
		Map<String, Map<Long, List<WorkProduct>>> map = serviceFactory.getZeroService().findAllProductForMap(companyList);
		for (OutSourceProcessDetail osDetail : result.getResult())
		{
			for (String companyId : companyList)
			{
				Map<Long, List<WorkProduct>> _map = map.get(companyId);
				if (!_map.isEmpty())
				{
					List<WorkProduct> productList = _map.get(osDetail.getWorkId());
					if (null != productList)
					{
						osDetail.setProductList(productList);
					}
				}
			}
		}
		// 汇总
		OutSourceProcessDetail detail = new OutSourceProcessDetail();
		BigDecimal qty = new BigDecimal(0);
		BigDecimal arriveQty = new BigDecimal(0);
		for (OutSourceProcessDetail o : result.getResult())
		{
			qty = qty.add(o.getQty());
			arriveQty = arriveQty.add(o.getArriveQty());
		}
		detail.setQty(qty.setScale(2, RoundingMode.HALF_UP));
		detail.setArriveQty(arriveQty.setScale(2, RoundingMode.HALF_UP));
		result.getResult().add(detail);

		return result;
	}

	/**
	 * <pre>
	 * 功能  - 强制完工未清代工单
	 * </pre>
	 * @param tableType
	 * @param idsstr
	 * @return
	 * @since 1.0, 2018年3月16日 上午10:25:49, zhengby
	 */
	@RequestMapping(value = "completeOrder")
	@ResponseBody
	@RequiresPermissions("oem:deliver:transmit:complete")
	public AjaxResponseBody completeOrder(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? OemOrder.class : OemOrderDetail.class;
		if (serviceFactory.getCommonService().forceComplete(cla, ids, BoolValue.YES))
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
	 * @since 1.0, 2018年3月16日 上午10:26:08, zhengby
	 */
	@RequestMapping(value = "completeOrderCancel")
	@ResponseBody
	@RequiresPermissions("oem:deliver:transmit:completecancel")
	public AjaxResponseBody completeOrderCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{

		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? OemOrder.class : OemOrderDetail.class;
		if (serviceFactory.getCommonService().forceComplete(cla, ids, BoolValue.NO))
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
	 * 功能  - 强制完工代工未对账
	 * </pre>
	 * @param tableType
	 * @param deliverIds
	 * @param returnIds
	 * @return
	 * @since 1.0, 2018年3月27日 上午8:59:46, zhengby
	 */
	@RequestMapping(value = "completeDeliver")
	@ResponseBody
	@RequiresPermissions("oem:reconcil:transmit:complete")
	public AjaxResponseBody completeDeliver(TableType tableType, @RequestParam(required = false, name = "deliverIds[]") Long[] deliverIds, @RequestParam(required = false, name = "returnIds[]") Long[] returnIds)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, deliverIds) && Validate.validateObjectsNullOrEmpty(tableType, returnIds))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Boolean isComplete = false;
		if (!Validate.validateArrayNullOrEmpty(deliverIds))
		{
			Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? OemDeliver.class : OemDeliverDetail.class;
			if (serviceFactory.getCommonService().forceComplete(cla, deliverIds, BoolValue.YES))
			{
				isComplete = true;
			}
			else
			{
				return returnErrorBody(I18nResource.FAIL);
			}
		}
		if (!Validate.validateArrayNullOrEmpty(returnIds))
		{
			Class<? extends BaseBillTableEntity> cla2 = (tableType == TableType.MASTER) ? OemReturn.class : OemReturnDetail.class;
			if (serviceFactory.getCommonService().forceComplete(cla2, returnIds, BoolValue.YES))
			{
				isComplete = true;
			}
			else
			{
				return returnErrorBody(I18nResource.FAIL);
			}
		}
		if (isComplete)
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
	 * 功能  - 取消强制完工代工未对账
	 * </pre>
	 * @param tableType
	 * @param deliverIds
	 * @param returnIds
	 * @return
	 * @since 1.0, 2018年3月27日 上午8:59:51, zhengby
	 */
	@RequestMapping(value = "completeDeliverCancel")
	@ResponseBody
	@RequiresPermissions("oem:reconcil:transmit:completecancel")
	public AjaxResponseBody completeDeliverCancel(TableType tableType, @RequestParam(required = false, name = "deliverIds[]") Long[] deliverIds, @RequestParam(required = false, name = "returnIds[]") Long[] returnIds)
	{

		if (Validate.validateObjectsNullOrEmpty(tableType, deliverIds) && Validate.validateObjectsNullOrEmpty(tableType, returnIds))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Boolean isComplete = false;
		if (!Validate.validateArrayNullOrEmpty(deliverIds))
		{
			Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? OemDeliver.class : OemDeliverDetail.class;
			if (serviceFactory.getCommonService().forceComplete(cla, deliverIds, BoolValue.NO))
			{
				isComplete = true;
			}
			else
			{
				return returnErrorBody(I18nResource.FAIL);
			}
		}
		if (!Validate.validateArrayNullOrEmpty(returnIds))
		{
			Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? OemReturn.class : OemReturnDetail.class;
			if (serviceFactory.getCommonService().forceComplete(cla, returnIds, BoolValue.NO))
			{
				isComplete = true;
			}
			else
			{
				return returnErrorBody(I18nResource.FAIL);
			}
		}
		if (isComplete)
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
	 * 功能 - 强制完工代工平台
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年3月20日 上午9:53:33, think
	 */
	@RequestMapping(value = "completeOutsourceOrder")
	@ResponseBody
	@RequiresPermissions("oem:order:transmit:complete")
	public AjaxResponseBody completeOrder(@RequestParam("ids[]") Long[] ids)
	{
		return _completeOrder(ids, BoolValue.YES);
	}

	/**
	 * <pre>
	 * 功能 - 取消强制完工代工平台
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年3月20日 上午9:53:39, think
	 */
	@RequestMapping(value = "completeOutsourceOrderCancel")
	@ResponseBody
	@RequiresPermissions("oem:order:transmit:completecancel")
	public AjaxResponseBody completeOrderCancel(@RequestParam("ids[]") Long[] ids)
	{
		return _completeOrder(ids, BoolValue.NO);
	}

	/**
	 * <pre>
	 * 功能 - 强制完工和取消强制完工代工平台
	 * </pre>
	 * @param ids
	 * @param falg
	 * @return
	 * @since 1.0, 2018年3月27日 下午2:31:38, think
	 */
	private AjaxResponseBody _completeOrder(Long[] ids, BoolValue flag)
	{
		if (Validate.validateArrayNullOrEmpty(ids))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}

		try
		{
			// 拿到公司id，不能通过页面传，怕数据注入攻击（循环性能有点慢）
			Map<String, List<Long>> companyDetailIdsMap = Maps.newHashMap();
			for (Long id : ids)
			{
				OutSourceProcessDetail outSourceProcess = serviceFactory.getOemZeroService().getOutSourceProcessDetail(id);
				if (null != outSourceProcess)
				{
					String companyId = outSourceProcess.getCompanyId();
					List<Long> idsList = companyDetailIdsMap.get(companyId);
					if (null == idsList)
					{
						idsList = Lists.newArrayList();
						companyDetailIdsMap.put(companyId, idsList);
					}
					idsList.add(id);
				}
			}

			// 根据公司id更新
			for (Iterator<Entry<String, List<Long>>> it = companyDetailIdsMap.entrySet().iterator(); it.hasNext();)
			{
				Entry<String, List<Long>> next = it.next();
				String _companyId = next.getKey();
				List<Long> _ids = next.getValue();
				serviceFactory.getCommonService().forceComplete(_companyId, OutSourceProcessDetail.class, _ids.toArray(new Long[0]), flag);
			}

			return returnSuccessBody();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return returnErrorBody(I18nResource.FAIL);
		}
	}
}
