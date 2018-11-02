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
import java.util.HashMap;
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
import com.google.common.collect.Maps;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.pay.util.JsonMapper;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.offer.OfferOrder;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.sale.SaleOrder;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.ProductType;
import com.huayin.printmanager.persist.enumerate.ResultType;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.service.vo.SumVo;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 - 销售订单
 * </pre>
 * @author       zhengby
 * @version 		 1.0, 2016年6月16日
 * @version 	   2.0, 2018年2月22日下午5:38:47, zhengby, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/sale/order")
public class SaleOrderController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 销售订单列表查看
	 * </pre>
	 * @param auditflag
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月22日 下午4:51:31, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sale:order:list")
	public String list(String auditflag, ModelMap map)
	{
		map.put("auditflag", auditflag);
		return "sale/order/list";
	}

	/**
	 * <pre>
	 * 功能 - 查看销售订单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午4:57:56, zhengby
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	@RequiresPermissions("sale:order:list")
	public Map<String, Object> viewAjax(@PathVariable Long id)
	{
		Map<String, Object> mapData = null;
		try
		{
			SaleOrder order = serviceFactory.getSaleOrderService().getHasChildren(id);
			mapData = ObjectUtils.objectToMap(order);
			mapData.put("companyName", UserUtils.getCompany().getName());
			mapData.put("companyAddress", UserUtils.getCompany().getAddress());
			mapData.put("companyFax", UserUtils.getCompany().getFax());
			mapData.put("companyLinkName", UserUtils.getCompany().getLinkName());
			mapData.put("companyTel", UserUtils.getCompany().getTel());
			mapData.put("companyEmail", UserUtils.getCompany().getEmail());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mapData;
	}

	/**
	 * <pre>
	 * 功能 - 点击源单单号查看
	 * </pre>
	 * @param request
	 * @param billNo
	 * @return
	 * @since 1.0, 2017年12月28日 上午9:45:51, zhengby
	 */
	@RequestMapping(value = "toViewAjax/{billNo}")
	@ResponseBody
	@RequiresPermissions("sale:order:list")
	public Map<String, Object> toViewAjax(HttpServletRequest request, @PathVariable String billNo)
	{
		SaleOrder order = serviceFactory.getSaleOrderService().get(billNo);
		return this.viewAjax(order.getId());
	}

	/**
	 * <pre>
	 * 页面 - 销售订单创建
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月7日 上午10:05:26, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("sale:order:create")
	public String create(ModelMap map)
	{
		return "sale/order/create";
	}
	
	/**
	 * <pre>
	 * 功能 - 报价转订单之前,检查是否需要同步基础数据
	 * </pre>
	 * @param ids
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月8日 上午10:27:07, think
	 */
	@RequestMapping(value = "createFromOfferCheck")
	@ResponseBody
	public AjaxResponseBody createFromOfferCheck(@RequestParam(name="ids[]") List<String> ids, ModelMap map)
	{
		Map<ResultType, List<String>> result = serviceFactory.getSaleOrderService().genFromOfferCheck(ids);
		List<String> tips = result.get(ResultType.SUCCESS);
		String flag = ResultType.SUCCESS.name();
		if(null == tips)
		{
			tips = result.get(ResultType.ERROR);
			flag = ResultType.ERROR.name();
		}
		
		Map<String, Object> ret = Maps.newHashMap();
		ret.put(flag, tips);
		return returnSuccessBody(ret);
	}
	
	/**
	 * <pre>
	 * 页面 - 报价转订单
	 * </pre>
	 * @param ids
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月9日 上午9:21:06, think
	 */
	@RequestMapping(value = "createFromOffer")
	@RequiresPermissions("sale:order:create")
	public String createFromOffer(@RequestParam(name="ids[]") List<String> ids, ModelMap map)
	{
		try
		{
			SaleOrder saleOrder = serviceFactory.getSaleOrderService().genFromOffer(ids);
			if(null != saleOrder)
			{
				// 设置送货方式和付款方式
				saleOrder.setDeliveryClassId(saleOrder.getCustomer().getDeliveryClassId());
				saleOrder.setPaymentClassId(saleOrder.getCustomer().getPaymentClassId());
				map.put("order", saleOrder);
				map.put("customer", saleOrder.getCustomer());
				List<SaleOrderDetail> detailList = saleOrder.getDetailList();
				if(detailList.size() > 0)
				{
					SaleOrderDetail saleOrderDetail = detailList.get(0);
					map.put("isOffer", true);
					map.put("offerOrderIdList", JsonMapper.toJsonString(ids));
					map.put("offerDetail", saleOrderDetail);
					map.put("offerPartList", JsonMapper.toJsonString(saleOrderDetail.getPartList()));
					map.put("offerPack", JsonMapper.toJsonString(saleOrderDetail.getPack()));
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		
		return "sale/order/create";
	}

	/**
	 * <pre>
	 * 页面 - 销售订单复制
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:13:33, zhengby
	 */
	@RequestMapping(value = "copy/{id}")
	@RequiresPermissions("sale:order:create")
	public String copy(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		SaleOrder order = serviceFactory.getSaleOrderService().getHasChildren(id);
		Customer customer = serviceFactory.getCustomerService().get(order.getCustomerId());
		map.put("order", order);
		map.put("customer", customer);
		map.put("isCopy", true);
		return "sale/order/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存销售订单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:13:44, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("sale:order:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "销售订单", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody SaleOrder order, HttpServletRequest request)
	{
		// 保存之前验证是否已送完货，前端的已校验过，后台再校验一次，防止保存重复送货单
		for (SaleOrderDetail detail : order.getDetailList())
		{
			if (null == detail.getOfferId())
			{
				continue;
			}
			OfferOrder offerOrder = serviceFactory.getOfferOrderService().get(detail.getOfferId());
      if (null != offerOrder.getSaleId())
      {
        return returnErrorBody("单据已生成，无需重复操作");
      }
    }
		try
		{
			serviceFactory.getSaleOrderService().save(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			logger.error(ex.getMessage(), "SaleOrder:" + JsonUtils.toJson(order));
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 页面 - 销售订单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:13:15, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("sale:order:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		SaleOrder order = serviceFactory.getSaleOrderService().getHasChildren(id);
		Customer customer = serviceFactory.getCustomerService().get(order.getCustomerId());
		map.put("order", order);
		map.put("customer", customer);
		return "sale/order/edit";
	}

	/**
	 * <pre>
	 * 页面 - 销售订单更新
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:13:01, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sale:order:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "销售订单", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody SaleOrder order, HttpServletRequest request)
	{
		serviceFactory.getSaleOrderService().update(order);
		// 更新旧数据
		// SaleOrder new_order = serviceFactory.getSaleOrderService().get(order.getId());
		request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
		return returnSuccessBody(order.getId());
	}

	/**
	 * <pre>
	 * 功能 - 审核销售订单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:12:48, zhengby
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("sale:order:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		// 销售订单审核后，对应的产品的销售单价反写到基础资料里产品信息里对应的产品的单价
		if (serviceFactory.getCommonService().audit(BillType.SALE_SO, id, BoolValue.YES))
		{
			// 查找出最新创建的订单
			SaleOrder latest = serviceFactory.getSaleOrderService().getLatestOrderHasDetail();
			// 判断先当前审核的订单是否是最新创建的订单
			if (latest.getId().longValue() == id.longValue())
			{
				Map<Long, BigDecimal> map = new HashMap<>();
				for (SaleOrderDetail detail : latest.getDetailList())
				{
					map.put(detail.getProductId(), detail.getPrice());
				}
				// 传递一个map，根据map更新产品的单价
				serviceFactory.getProductService().updatePrice(map);
			}
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 反审核销售订单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:12:35, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "audit_cancel/{id}")
	@ResponseBody
	@RequiresPermissions("sale:order:audit_cancel")
	public AjaxResponseBody auditCancel(@PathVariable Long id)
	{
		if (serviceFactory.getCommonService().audit(BillType.SALE_SO, id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.SALE_SO, id);
			return returnErrorBody(map);
		}
	}

	/**
	 * <pre>
	 * 功能 - 审核所有销售订单（TODO 已经没用）
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:12:23, zhengby
	 */
	@RequestMapping(value = "checkAll")
	@ResponseBody
	@RequiresPermissions("sale:order:checkAll")
	public AjaxResponseBody checkAll()
	{
		if (serviceFactory.getSaleOrderService().checkAll())
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
	 * 功能 - 强制完工销售订单
	 * </pre>
	 * @param tableType 
	 * @param id 表ID
	 * @param flag 完工标记
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:11:08, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("sale:order:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam("ids[]") List<String> idsstr)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, idsstr))
		{
			return returnErrorBody("提交数据不完整");
		}

		boolean flag = false;
		if (tableType == TableType.DETAIL)
		{
			// "1,S".split("|");
			List<Long> sids = Lists.newArrayList();
			List<Long> mids = Lists.newArrayList();
			for (int i = 0; i < idsstr.size(); i++)
			{
				String id = idsstr.get(i);
				String[] v = id.split(",");
				if (v.length != 2)
				{
					logger.error("参数错误");
					continue;
				}
				if ("S".equals(v[1]))
					sids.add(Long.valueOf(v[0]));
				else if ("M".equals(v[1]))
					mids.add(Long.valueOf(v[0]));
			}

			if (sids.size() > 0)
			{
				Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? SaleOrder.class : SaleOrderDetail.class;
				flag = serviceFactory.getCommonService().forceComplete2(cla, sids, BoolValue.YES);
			}

			if (mids.size() > 0)
			{
				Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? Work.class : WorkProduct.class;
				flag = serviceFactory.getCommonService().forceComplete2(cla, mids, BoolValue.YES);
			}
		}
		else
		{
			List<Long> sids = Lists.newArrayList();
			for (int i = 0; i < idsstr.size(); i++)
			{
				sids.add(Long.valueOf(idsstr.get(i)));
			}
			Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? SaleOrder.class : SaleOrderDetail.class;
			flag = serviceFactory.getCommonService().forceComplete2(cla, sids, BoolValue.YES);
		}

		if (flag)
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
	 * 功能 - 取消强制完工销售订单
	 * </pre>
	 * @param tableType 
	 * @param id 表ID
	 * @param flag 完工标记
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:10:12, zhengby
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("sale:order:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam("ids[]") List<String> idsstr)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, idsstr))
		{
			return returnErrorBody("提交数据不完整");
		}

		boolean flag = false;
		if (tableType == TableType.DETAIL)
		{
			List<Long> sids = Lists.newArrayList();
			List<Long> mids = Lists.newArrayList();
			for (int i = 0; i < idsstr.size(); i++)
			{
				String id = idsstr.get(i);
				String[] v = id.split(",");
				if (v.length != 2)
				{
					logger.error("参数错误");
					continue;
				}
				if ("S".equals(v[1]))
					sids.add(Long.valueOf(v[0]));
				else if ("M".equals(v[1]))
					mids.add(Long.valueOf(v[0]));
			}

			if (sids.size() > 0)
			{
				Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? SaleOrder.class : SaleOrderDetail.class;
				flag = serviceFactory.getCommonService().forceComplete2(cla, sids, BoolValue.NO);
			}

			if (mids.size() > 0)
			{
				Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? Work.class : WorkProduct.class;
				flag = serviceFactory.getCommonService().forceComplete2(cla, mids, BoolValue.NO);
			}
		}
		else
		{
			List<Long> sids = Lists.newArrayList();
			for (int i = 0; i < idsstr.size(); i++)
			{
				sids.add(Long.valueOf(idsstr.get(i)));
			}
			Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? SaleOrder.class : SaleOrderDetail.class;
			flag = serviceFactory.getCommonService().forceComplete2(cla, sids, BoolValue.NO);
		}

		if (flag)
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
	 * 功能 - 删除销售订单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:09:58, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("sale:order:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "销售订单", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		try
		{

			SaleOrder order = serviceFactory.getSaleOrderService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getSaleOrderService().delete(id);
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
	 * 页面 - 销售订单打印
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:09:45, zhengby
	 */
	@RequestMapping(value = "print/{id}")
	@RequiresPermissions("sale:order:print")
	public String print(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		SaleOrder order = serviceFactory.getSaleOrderService().getHasChildren(id);
		map.put("order", order);
		return "sale/order/print";
	}
	
	/**
	 * <pre>
	 * 页面 - 快速选择工序材料信息
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param productType
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月22日 下午5:09:19, zhengby
	 */
	@RequestMapping(value = "quick_procedure")
	public String quickProcedure(HttpServletRequest request, ModelMap map, Long id, String productType) throws Exception
	{
		if (id != null)
		{
			SaleOrderDetail detail = serviceFactory.getSaleOrderService().getDetail(id);
			if (null != detail)
			{
				map.put("order", detail);
			}
		}
		map.put("productType", productType);
		return "sale/order/quick_procedure";
	}

	/**
	 * <pre>
	 * 页面 - 快速选择销售订单列表（TODO 销售订单不需要）
	 * </pre>
	 * @param productType
	 * @param request
	 * @param map
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月22日 下午5:09:34, zhengby
	 */
	@RequestMapping(value = "quick_select_forwork")
	public String quickSelectProduct(ProductType productType, HttpServletRequest request, ModelMap map) throws Exception
	{
		map.put("productType", productType);
		return "sale/order/quick_select_forwork";
	}

	
	/**
	 * <pre>
	 * 页面 - 销售订单明细列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月22日 下午4:52:06, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("sale:order_detail:list")
	public String detailList()
	{
		return "sale/order/detail_list";
	}

	/**
	 * <pre>
	 * 功能 - 查看销售订单明细
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月22日 下午5:14:17, zhengby
	 */
	@RequestMapping(value = "ajaxSaleDetail")
	@ResponseBody
	public SaleOrderDetail ajaxSaleDetail(HttpServletRequest request, ModelMap map, Long id) throws Exception
	{
		return serviceFactory.getSaleOrderService().getDetail(id);
	}
	
	/**
	 * <pre>
	 * 页面 - 销售订单进度表列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月22日 下午4:55:50, zhengby
	 */
	@RequestMapping(value = "detailFlowList")
	@RequiresPermissions("sale:detailFlowList:list")
	public String detailFlowList()
	{
		return "sale/order/detailFlowList";
	}

	/**
	 * <pre>
	 * 功能 - 快速选择销售列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:15:08, zhengby
	 */
	@RequestMapping(value = "ajaxSaleListForWork")
	@ResponseBody
	public SearchResult<SaleOrderDetail> ajaxSaleListForWork(@RequestBody QueryParam queryParam)
	{
		SearchResult<SaleOrderDetail> result = serviceFactory.getSaleOrderService().quickFindForWorkByCondition(queryParam);
		SaleOrderDetail detail = new SaleOrderDetail();
		Integer qty = new Integer(0);// 订单数量
		Integer produceedQty = new Integer(0); // 已生产数量
		Integer spareQty = new Integer(0); // 备品数量
		Integer produceSpareedQty = new Integer(0); // 已备品数量

		for (SaleOrderDetail s : result.getResult())
		{
			qty += s.getQty();
			produceedQty += s.getProduceedQty();
			spareQty += s.getSpareQty();
			produceSpareedQty += s.getProduceSpareedQty();
		}
		detail.setQty(qty);
		detail.setSpareQty(spareQty);
		detail.setProduceedQty(produceedQty);
		detail.setProduceSpareedQty(produceSpareedQty);
		result.getResult().add(detail);

		return result;
	}

	/**
	 * <pre>
	 * 功能 - 获取销售订单创建日期列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:15:42, zhengby
	 */
	@RequestMapping(value = "getYearsFromSaleOrder")
	@ResponseBody
	public List<SaleOrder> getYearsFromSaleOrderByCustomerName()
	{
		List<SaleOrder> list = serviceFactory.getSaleOrderService().getYearsFromSaleOrder();
		return list;
	}

	/**
	 * <pre>
	 * 页面 - 销售订单汇总表(按客户)页面
	 * </pre>
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:16:01, zhengby
	 */
	@RequestMapping(value = "collect_customer_list/{type}")
	@RequiresPermissions("sale:collect_customer:list")
	public String collectCustomerList(@PathVariable String type)
	{
		return "sale/order/collect/customer_" + type + "_list";
	}

	/**
	 * <pre>
	 * 页面 - 销售订单汇总表(按产品)页面
	 * </pre>
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:16:23, zhengby
	 */
	@RequestMapping(value = "collect_product_list/{type}")
	@RequiresPermissions("sale:collect_product:list")
	public String collectProductList(@PathVariable String type)
	{
		return "sale/order/collect/product_" + type + "_list";
	}

	/**
	 * <pre>
	 * 页面 - 销售订单汇总表(按销售员)页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:16:39, zhengby
	 */
	@RequestMapping(value = "collect_seller_list")
	@RequiresPermissions("sale:collect_seller:list")
	public String collectSellerList()
	{
		return "sale/order/collect/seller_list";
	}
	
	/**
	 * <pre>
	 * 页面 - 销售订单汇总表(按客户)请求
	 * </pre>
	 * @param queryParam
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:16:56, zhengby
	 */
	@RequestMapping(value = "sumSaleOrderByCustomer/{type}")
	@RequiresPermissions("sale:collect_customer:list")
	@ResponseBody
	public SearchResult<SumVo> sumSaleOrderByCustomer(@RequestBody QueryParam queryParam, @PathVariable String type)
	{
		SearchResult<SumVo> result = serviceFactory.getSaleOrderService().sumSaleOrderByCustomer(queryParam, type);
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
	 * 页面 - 销售订单汇总表(按产品)请求
	 * </pre>
	 * @param queryParam
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:17:16, zhengby
	 */
	@RequestMapping(value = "sumSaleOrderByProduct/{type}")
	@RequiresPermissions("sale:collect_product:list")
	@ResponseBody
	public SearchResult<SumVo> sumSaleOrderByProduct(@RequestBody QueryParam queryParam, @PathVariable String type)
	{
		SearchResult<SumVo> result = serviceFactory.getSaleOrderService().sumSaleOrderByProduct(queryParam, type);
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
	 * 页面- 销售订单汇总表(按销售员)请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:17:41, zhengby
	 */
	@RequestMapping(value = "sumSaleOrderBySeller")
	@RequiresPermissions("sale:collect_seller:list")
	@ResponseBody
	public SearchResult<SumVo> sumSaleOrderBySeller(@RequestBody QueryParam queryParam)
	{
		SearchResult<SumVo> result = serviceFactory.getSaleOrderService().sumSaleOrderBySeller(queryParam);
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
	 * 页面 - 单价变更编辑
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:17:58, zhengby
	 */
	@RequestMapping(value = "editPrice/{id}")
	public String editPrice(@PathVariable Long id, ModelMap map)
	{
		SaleOrderDetail saleOrderDetail = serviceFactory.getSaleOrderService().getDetail(id);
		map.put("saleOrderDetail", saleOrderDetail);
		return "sale/order/editPrice";
	}

	/**
	 * <pre>
	 * 功能 - 修改单价
	 * </pre>
	 * @param saleOrderDetail
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:18:19, zhengby
	 */
	@RequestMapping(value = "changePrice")
	@RequiresPermissions("sale:order:changePrice")
	@ResponseBody
	public AjaxResponseBody changePrice(@RequestBody SaleOrderDetail saleOrderDetail)
	{
		try
		{
			serviceFactory.getSaleOrderService().changePrice(saleOrderDetail);
			// 查找出最新创建的订单
			SaleOrder latest = serviceFactory.getSaleOrderService().getLatestOrderHasDetail();
			// 判断先当前审核的订单是否是最新创建的订单
			if (latest.getId().longValue() == saleOrderDetail.getMasterId().longValue())
			{
				Map<Long, BigDecimal> map = new HashMap<>();
				
				map.put(saleOrderDetail.getProductId(), saleOrderDetail.getPrice());
				// 传递一个map，根据map更新产品的单价
				serviceFactory.getProductService().updatePrice(map);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
			return returnErrorBody("修改失败");
		}

		return returnSuccessBody();
	}

	// ==================== 新规范 - 代码重构 ====================

	/**
	 * <pre>
	 * 功能 - 销售订单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月22日 上午11:22:07, think
	 */
	@RequestMapping(value = "ajaxList")
	@RequiresPermissions("sale:order:list")
	@ResponseBody
	public SearchResult<SaleOrder> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SaleOrder> result = serviceFactory.getSaleOrderService().findByCondition(queryParam);
		SaleOrder order = new SaleOrder();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal noTaxTotalMoney = new BigDecimal(0);
		for (SaleOrder saleOrder : result.getResult())
		{
			totalMoney = totalMoney.add(saleOrder.getTotalMoney());
			totalTax = totalTax.add(saleOrder.getTotalTax());
			noTaxTotalMoney = noTaxTotalMoney.add(saleOrder.getNoTaxTotalMoney());
		}
		order.setTotalMoney(totalMoney);
		order.setTotalTax(totalTax);
		order.setNoTaxTotalMoney(noTaxTotalMoney);
		order.setIsCheck(null);
		result.getResult().add(order);
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 查询销售订单明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月22日 上午10:35:16, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@RequiresPermissions("sale:order_detail:list")
	@ResponseBody
	public SearchResult<SaleOrderDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SaleOrderDetail> result = serviceFactory.getSaleOrderService().findDetailByCondition(queryParam);
		SaleOrderDetail detail = new SaleOrderDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		Integer produceedQty = new Integer(0);// 已生产数
		Integer deliverQty = new Integer(0);// 已送货数量
		Integer spareQty = new Integer(0);// 备品数量
		BigDecimal price = new BigDecimal(0);// 单价
		Integer totalQty = new Integer(0);
		for (SaleOrderDetail saleOrderDetail : result.getResult())
		{
			money = money.add(saleOrderDetail.getMoney());
			tax = tax.add(saleOrderDetail.getTax());
			noTaxMoney = noTaxMoney.add(saleOrderDetail.getNoTaxMoney());
			produceedQty += saleOrderDetail.getProduceedQty();
			deliverQty += saleOrderDetail.getDeliverQty();
			spareQty += saleOrderDetail.getSpareQty();
			price = price.add(saleOrderDetail.getPrice());
			totalQty += saleOrderDetail.getQty();
		}
		SaleOrder order = new SaleOrder();
		detail.setMaster(order);
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setProduceedQty(produceedQty);
		detail.setDeliverQty(deliverQty);
		detail.setSpareQty(spareQty);
		detail.setPrice(price);
		detail.setQty(totalQty);
		result.getResult().add(detail);
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 查询销售订单明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月22日 上午10:34:42, think
	 */
	@RequestMapping(value = "ajaxFlowList")
	@RequiresPermissions("sale:detailFlowList:list")
	@ResponseBody
	public SearchResult<SaleOrderDetail> ajaxFlowList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SaleOrderDetail> result = serviceFactory.getSaleOrderService().findFlowByCondition(queryParam);
		SaleOrderDetail detail = new SaleOrderDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		Integer totalQty = new Integer(0);
		for (SaleOrderDetail saleOrderDetail : result.getResult())
		{
			money = money.add(saleOrderDetail.getMoney());
			tax = tax.add(saleOrderDetail.getTax());
			noTaxMoney = noTaxMoney.add(saleOrderDetail.getNoTaxMoney());
			totalQty += saleOrderDetail.getQty();
		}
		SaleOrder order = new SaleOrder();
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
	 * 页面  - 查看历史单价
	 * </pre>
	 * @return
	 * @since 1.0, 2018年4月13日 下午2:54:24, zhengby
	 */
	@RequestMapping(value = "historyPrice/{id}")
	@RequiresPermissions("sale:order:historyPrice")
	public String historyPrice(ModelMap map, @PathVariable Long id)
	{
		map.put("productId", id);
		return "sale/order/historyPrice";
	}
}
