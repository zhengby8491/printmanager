/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月7日 上午10:04:45
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.sale;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleDetailBaseEntity;
import com.huayin.printmanager.persist.entity.sale.SaleOrder;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 销售管理 - 销售未清
 * </pre>
 * @author       think
 * @since        1.0, 2018年2月7日
 */
@Controller
@RequestMapping(value = "${basePath}/sale/transmit")
public class SaleTransmitController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 销售未送货
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月7日 上午9:52:15, think
	 */
	@RequestMapping(value = "to_deliver")
	@RequiresPermissions("sale:deliver:transmit")
	public String transmit_deliver(Date dateMin, Date dateMax, ModelMap map)
	{
		map.put("dateMin", dateMin);
		map.put("dateMax", dateMax);
		return "sale/transmit/to_deliver";
	}

	/**
	 * <pre>
	 * 功能 - 销售未送货列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月7日 上午9:52:25, think
	 */
	@RequestMapping(value = "to_deliver_list")
	@ResponseBody
	public SearchResult<SaleOrderDetail> transmit_deliver_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<SaleOrderDetail> result = new SearchResult<SaleOrderDetail>();
		SearchResult<SaleOrderDetail> result1 = serviceFactory.getSaleTransmitService().findForTransmitDeliverByWork(queryParam);
		SearchResult<SaleOrderDetail> result2 = serviceFactory.getSaleTransmitService().findForTransmitDeliver(queryParam);

		List<SaleOrderDetail> list = new ArrayList<SaleOrderDetail>(1000);
		list.addAll(result1.getResult());
		list.addAll(result2.getResult());
		
		List<Long> idList = Lists.newArrayList();
		for (SaleOrderDetail detail : list)
		{
			if (!idList.contains(detail.getProductId()) && null != detail.getProductId())
			{
				idList.add(detail.getProductId());
			}
		}
		// 查询成品库存数量
		Map<Long,Integer> map = new HashMap<>();
		if (idList.size()>0)
		{
			map = serviceFactory.getStockProductService().findStockQty(idList);
		}
		
		Integer qty = new Integer(0);
		Integer spareQty = new Integer(0);
		Integer deliverQty = new Integer(0);
		Integer storageQty = new Integer(0);
		for (SaleOrderDetail s : list)
		{
			s.setStorageQty(map.get(s.getProductId()));
			qty += s.getQty();
			spareQty += s.getSpareQty();
			deliverQty += s.getDeliverQty();
			if ( null != s.getStorageQty())
			{
				storageQty += s.getStorageQty();
			}
		}
		SaleOrderDetail detail = new SaleOrderDetail();
		detail.setQty(qty);
		detail.setSpareQty(spareQty);
		detail.setDeliverQty(deliverQty);
		detail.setStorageQty(storageQty);
		list.add(detail);

		result.setResult(list);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 送货未对账
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月7日 上午9:53:16, think
	 */
	@RequestMapping(value = "to_reconcil")
	@RequiresPermissions("sale:reconcil:transmit")
	public String transmit_reconcil()
	{
		return "sale/transmit/to_reconcil";
	}

	/**
	 * <pre>
	 * 功能 - 送货未对账
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月7日 上午9:53:49, think
	 */
	@RequestMapping(value = "to_reconcil_list")
	@ResponseBody
	public SearchResult<SaleDetailBaseEntity> transmit_reconcil_list(@RequestBody QueryParam queryParam)
	{
		// 送货
		SearchResult<SaleDeliverDetail> resultDeliver = serviceFactory.getSaleTransmitService().findDeliverForTransmitReconcil(queryParam);
		// 退货
		SearchResult<SaleReturnDetail> resultReturn = serviceFactory.getSaleTransmitService().findReturnForTransmitReconcil(queryParam);
		// 合并
		SearchResult<SaleDetailBaseEntity> result = new SearchResult<SaleDetailBaseEntity>();
		result.setResult(new ArrayList<SaleDetailBaseEntity>());
		result.getResult().addAll(resultDeliver.getResult());
		result.getResult().addAll(resultReturn.getResult());
		result.setCount(resultDeliver.getCount() + resultReturn.getCount());
		Integer qty = new Integer(0);
		BigDecimal price = new BigDecimal(0);
		Integer reconcilQty = new Integer(0);
		for (SaleDeliverDetail s : resultDeliver.getResult())
		{
			qty += s.getQty();
			price = price.add(s.getPrice());
			reconcilQty += s.getReconcilQty();
		}
		for (SaleReturnDetail s2 : resultReturn.getResult())
		{
			qty -= s2.getQty();
			price = price.subtract(s2.getPrice());
			reconcilQty += s2.getReconcilQty();
		}

		SaleOrderDetail detail = new SaleOrderDetail();
		detail.setQty(qty);
		detail.setPrice(price.setScale(2, RoundingMode.HALF_UP));
		detail.setReconcilQty(reconcilQty);
		result.getResult().add(detail);

		return result;
	}
	
	/**
	 * <pre>
	 * 页面 - 报价未下单
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月7日 上午9:56:01, think
	 */
	@RequestMapping(value = "fromOffer")
	@RequiresPermissions("sale:offer:transmit")
	public String fromOffer(Date dateMin, Date dateMax, ModelMap map)
	{
		map.put("dateMin", dateMin);
		map.put("dateMax", dateMax);
		return "sale/transmit/from_offer";
	}

	/**
	 * <pre>
	 * 功能 - 强制完工:
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param id 表ID
	 * @param flag 完工标记
	 * @return
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
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
	 * 功能 - 取消强制完工:
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）6
	 * @param id 表ID
	 * @param flag 完工标记
	 * @return
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
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

	
}
