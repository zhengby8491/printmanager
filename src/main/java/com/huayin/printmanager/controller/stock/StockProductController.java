/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 成品库存
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stock/product")
public class StockProductController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 成品库存列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:54:21, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:product:list")
	public String list()
	{
		return "stock/product/list";
	}

	/**
	 * <pre>
	 * 数据 - 成品库存列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:54:36, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockProduct> productStockList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProduct> result = serviceFactory.getStockProductService().findByCondition(queryParam);

		StockProduct product = new StockProduct();
		Integer qty = new Integer(0);
		BigDecimal money = new BigDecimal(0);
		for (StockProduct stockProduct : result.getResult())
		{
			qty += stockProduct.getQty();
			money = money.add(stockProduct.getMoney());
		}
		Product _product = new Product();
		product.setProduct(_product);
		product.setQty(qty);
		product.setMoney(money);
		result.getResult().add(product);

		return result;
	}

	/**
	 * <pre>
	 * 页面 - 快速选择库存列表（TODO 可以放到quickController中）
	 * </pre>
	 * @param warehouseId
	 * @param isEmptyWare
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:54:48, think
	 */
	@RequestMapping(value = "quick_select")
	public String quickSelect(Long warehouseId, String isEmptyWare, ModelMap map)
	{
		map.put("warehouseId", warehouseId);
		// 不显示0库存
		map.put("isEmptyWare", "no".equals(isEmptyWare) ? BoolValue.NO : BoolValue.YES);
		return "stock/product/quick_select";
	}

	/**
	 * <pre>
	 * 数据 - 快速选择库存列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:55:22, think
	 */
	@RequestMapping(value = "quick_ajaxList")
	@ResponseBody
	public SearchResult<StockProduct> quick_ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProduct> result = serviceFactory.getStockProductService().quickFindByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 成品库存明细
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:55:41, think
	 */
	@RequestMapping(value = "logDetailList")
	@RequiresPermissions("stock:product:logDetailList")
	public String logDetailList(ModelMap map)
	{
		List<BillType> billTypeList = new ArrayList<BillType>();
		billTypeList.add(BillType.SALE_IV);
		billTypeList.add(BillType.SALE_IR);
		billTypeList.add(BillType.STOCK_SPIN);
		billTypeList.add(BillType.STOCK_SPOI);
		billTypeList.add(BillType.STOCK_SPOO);
		billTypeList.add(BillType.STOCK_SPI);
		billTypeList.add(BillType.STOCK_SPA);
		billTypeList.add(BillType.STOCK_SPT);
		billTypeList.add(BillType.BEGIN_PRODUCT);
		billTypeList.add(BillType.OUTSOURCE_OA);
		billTypeList.add(BillType.OUTSOURCE_OR);
		map.put("billTypeList", billTypeList);
		return "stock/product/logDetailList";
	}

	/**
	 * <pre>
	 * 数据 - 成品库存明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:55:59, think
	 */
	@RequestMapping(value = "ajaxLogDetailList")
	@ResponseBody
	public SearchResult<StockProductLog> ajaxLogDetailList(@RequestBody QueryParam queryParam)
	{
		if (queryParam.getSearchContent() != "")
		{
			queryParam.setBillType(BillType.valueOf(queryParam.getSearchContent()));
		}
		SearchResult<StockProductLog> result = serviceFactory.getStockProductService().findStockProductLogDetailList(queryParam);

		StockProductLog productLog = new StockProductLog();

		Integer inQty = new Integer(0);
		Integer outQty = new Integer(0);
		BigDecimal inMoney = new BigDecimal(0);
		BigDecimal outMoney = new BigDecimal(0);

		for (StockProductLog stockProductLog : result.getResult())
		{
			if (stockProductLog.getInQty() != null)
			{
				inQty += stockProductLog.getInQty();
			}
			if (stockProductLog.getOutQty() != null)
			{
				outQty += stockProductLog.getOutQty();
			}
			if (stockProductLog.getInMoney() != null)
			{
				inMoney = inMoney.add(stockProductLog.getInMoney());
			}
			if (stockProductLog.getOutMoney() != null)
			{
				outMoney = outMoney.add(stockProductLog.getOutMoney());
			}
			// 产品图片
			QueryParam imgQ = new QueryParam();
			imgQ.setProductCode(stockProductLog.getCode());
			SearchResult<Product> _result = serviceFactory.getProductService().findByCondition(imgQ);
			stockProductLog.setImgUrl(_result.getResult().get(0).getImgUrl());
		}
		productLog.setInQty(inQty);
		productLog.setOutQty(outQty);
		productLog.setInMoney(inMoney);
		productLog.setOutMoney(outMoney);
		result.getResult().add(productLog);

		return result;
	}

	/**
	 * <pre>
	 * 页面 - 成品库存汇总
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:56:08, think
	 */
	@RequestMapping(value = "logSumList")
	@RequiresPermissions("stock:product:logSumList")
	public String logSumList()
	{
		return "stock/product/logSumList";
	}

	/**
	 * <pre>
	 * 数据 - 成品库存汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:56:19, think
	 */
	@RequestMapping(value = "ajaxlogSumList")
	@ResponseBody
	public SearchResult<StockProductLog> ajaxlogSumList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProductLog> result = serviceFactory.getStockProductService().findStockProductLogSumList(queryParam);

		StockProductLog productLog = new StockProductLog();

		Integer inQty = new Integer(0);
		Integer outQty = new Integer(0);
		BigDecimal inMoney = new BigDecimal(0);
		BigDecimal outMoney = new BigDecimal(0);

		for (StockProductLog stockProductLog : result.getResult())
		{
			inQty += stockProductLog.getInQty();
			outQty += stockProductLog.getOutQty();
			inMoney = inMoney.add(stockProductLog.getInMoney());
			outMoney = outMoney.add(stockProductLog.getOutMoney());
			// 产品图片
			QueryParam imgQ = new QueryParam();
			imgQ.setProductCode(stockProductLog.getCode());
			SearchResult<Product> _result = serviceFactory.getProductService().findByCondition(imgQ);
			stockProductLog.setImgUrl(_result.getResult().get(0).getImgUrl());
		}
		productLog.setInQty(inQty);
		productLog.setOutQty(outQty);
		productLog.setInMoney(inMoney.setScale(2, RoundingMode.HALF_UP));
		productLog.setOutMoney(outMoney.setScale(2, RoundingMode.HALF_UP));
		result.getResult().add(productLog);

		return result;
	}
}
