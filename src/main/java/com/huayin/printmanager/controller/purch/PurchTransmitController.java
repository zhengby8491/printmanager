/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.purch;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.exterior.vo.ExteriorPurchOrder;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.service.purch.vo.NotReconcilDetailVo;
import com.huayin.printmanager.service.purch.vo.WorkToPurchVo;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 采购管理 - 采购未清
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年9月23日, raintear
 * @version 	   2.0, 2018年2月23日下午3:31:06, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/purch/transmit")
public class PurchTransmitController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 转采购(物料需求计划)
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:31:57, zhengby
	 */
	@RequestMapping(value = "to_purch_order")
	@RequiresPermissions("purch:transmit:to_purch_order")
	public String transmit_purch_order()
	{
		return "purch/transmit/to_purch_order";
	}

	/**
	 * <pre>
	 * 功能 - 返回转采购 数据(物料需求计划)主表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:32:17, zhengby
	 */
	@RequestMapping(value = "to_purch_order_list")
	@ResponseBody
	public SearchResult<WorkToPurchVo> transmit_purch_order_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<WorkToPurchVo> result = serviceFactory.getPurOrderService().transmitPurchOrderList(queryParam);
		WorkToPurchVo vo = new WorkToPurchVo();
		BigDecimal stockQty = new BigDecimal(0);
		BigDecimal workQty = new BigDecimal(0);
		BigDecimal purchQty = new BigDecimal(0);
		BigDecimal qty = new BigDecimal(0);
		for (WorkToPurchVo w : result.getResult())
		{
			stockQty = stockQty.add(w.getStockQty());
			workQty = workQty.add(w.getWorkQty());
			purchQty = purchQty.add(w.getPurchQty());
			qty = qty.add(qty);
		}
		vo.setStockQty(stockQty.setScale(2, RoundingMode.HALF_UP));
		vo.setWorkQty(workQty.setScale(2, RoundingMode.HALF_UP));
		vo.setPurchQty(purchQty.setScale(2, RoundingMode.HALF_UP));
		vo.setQty(qty.setScale(2, RoundingMode.HALF_UP));
		result.getResult().add(vo);
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 返回转采购 数据(物料需求计划)明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:32:44, zhengby
	 */
	@RequestMapping(value = "getWorkToPurchByMaterial")
	@ResponseBody
	public List<WorkMaterial> getWorkToPurchByMaterial(@RequestBody QueryParam queryParam)
	{
		List<WorkMaterial> materialList = serviceFactory.getPurOrderService().getWorkToPurchByMaterial(queryParam);

		// 查询所有工单WorkPart2Product，并设置到材料中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (WorkMaterial workMaterial : materialList)
		{
			long workId = workMaterial.getWorkId();
			List<WorkProduct> list = productMap.get(workId);
			if (null != list)
			{
				workMaterial.setProductList(list);
			}
		}

		return materialList;
	}

	/**
	 * <pre>
	 * 页面 - 转入库
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:33:18, zhengby
	 */
	@RequestMapping(value = "to_purch_stock")
	@RequiresPermissions("purch:transmit:to_purch_stock")
	public String transmit_purch_stock(Date dateMin, Date dateMax, ModelMap map)
	{
		map.put("dateMin", dateMin);
		map.put("dateMax", dateMax);
		return "purch/transmit/to_purch_stock";
	}

	/**
	 * <pre>
	 * 功能 - 返回转入库数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:33:53, zhengby
	 */
	@RequestMapping(value = "to_purch_stock_list")
	@ResponseBody
	public SearchResult<PurchOrderDetail> transmit_purch_stock_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<PurchOrderDetail> result = serviceFactory.getPurStockService().transmitPurchStockList(queryParam);

		// 查询所有工单WorkPart2Product，并设置到库存明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchOrderDetail purchOrderDetail : result.getResult())
		{
			if (null != purchOrderDetail.getSourceId())
			{
				long workId = purchOrderDetail.getSourceId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					purchOrderDetail.setProductList(list);
				}
			}
		}

		PurchOrderDetail vo = new PurchOrderDetail();
		BigDecimal storageQty = new BigDecimal(0); // 已入库数量
		BigDecimal qty = new BigDecimal(0); // 采购数量
		for (PurchOrderDetail p : result.getResult())
		{
			qty = qty.add(p.getQty());
			storageQty = storageQty.add(p.getStorageQty());
		}
		vo.setStorageQty(storageQty.setScale(2, RoundingMode.HALF_UP));
		vo.setQty(qty.setScale(2, RoundingMode.HALF_UP));
		result.getResult().add(vo);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 转对账
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:34:26, zhengby
	 */
	@RequestMapping(value = "to_purch_reconcil")
	@RequiresPermissions("purch:transmit:to_purch_reconcil")
	public String transmit_purch_reconcil()
	{
		return "purch/transmit/to_purch_reconcil";
	}

	/**
	 * <pre>
	 * 功能 - 返回转对账数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:34:41, zhengby
	 */
	@RequestMapping(value = "to_purch_reconcil_list")
	@ResponseBody
	public SearchResult<NotReconcilDetailVo> transmit_purch_reconcil_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<NotReconcilDetailVo> result = new SearchResult<NotReconcilDetailVo>();
		List<NotReconcilDetailVo> list = serviceFactory.getPurReconcilService().transmitPurchReconcilList(queryParam);
		result.setResult(list);

		// 查询所有工单WorkPart2Product，并设置到对账明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (NotReconcilDetailVo notReconcilDetailVo : result.getResult())
		{
			if (null != notReconcilDetailVo.getWorkId())
			{
				long workId = notReconcilDetailVo.getWorkId();
				List<WorkProduct> list2 = productMap.get(workId);
				if (null != list)
				{
					notReconcilDetailVo.setProductList(list2);
				}
			}
		}

		NotReconcilDetailVo vo = new NotReconcilDetailVo();
		BigDecimal qty = new BigDecimal(0);
		BigDecimal reconcilQty = new BigDecimal(0);
		for (NotReconcilDetailVo o : list)
		{
			if (o.getBillType() == BillType.PURCH_PR)
			{
				qty = qty.subtract(o.getQty());
				reconcilQty = reconcilQty.subtract(reconcilQty);
			}
			else
			{
				qty = qty.add(o.getQty());
				reconcilQty = reconcilQty.add(reconcilQty);
			}
		}
		vo.setQty(qty.setScale(2, RoundingMode.HALF_UP));
		vo.setReconcilQty(reconcilQty.setScale(2, RoundingMode.HALF_UP));
		result.getResult().add(vo);

		return result;
	}

	/**
	 * <pre>
	 * 页面  - 外部采购单列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年7月16日 下午3:41:03, zhengby
	 */
	@RequestMapping(value = "exteriorPurch")
	@RequiresPermissions("purch:transmit:to_purch_order")
	public String exteriorPurch()
	{
		return "purch/transmit/from_exterior_purch";
	}
	
	/**
	 * <pre>
	 * 数据  - 返回外部采购单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年7月16日 下午3:39:07, zhengby
	 */
	@RequestMapping(value = "fromExteriorPurchList")
	@ResponseBody
	public SearchResult<ExteriorPurchOrder> exteriorPurchList(@RequestBody QueryParam queryParam)
	{
		SearchResult<ExteriorPurchOrder> result = serviceFactory.getExteriorPurchService().exteriorPurchList(queryParam);
		ExteriorPurchOrder order = new ExteriorPurchOrder();
		BigDecimal orderPrice = new BigDecimal(0);
		for (ExteriorPurchOrder _order : result.getResult())
		{
			orderPrice = orderPrice.add(_order.getOrderPrice());
		}
		order.setOrderPrice(orderPrice);
		result.getResult().add(order);
		return result;
	}
}
