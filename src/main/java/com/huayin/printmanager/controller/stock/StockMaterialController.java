/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月24日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
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
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 材料库存
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stock/material")
public class StockMaterialController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 材料库存列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:24:20, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:material:list")
	public String list()
	{
		return "stock/material/list";
	}

	/**
	 * <pre>
	 * 数据 - 材料库存列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:24:36, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockMaterial> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterial> result = serviceFactory.getMaterialStockService().findByCondition(queryParam.getMaterialName(), queryParam.getMaterialType(), queryParam.getWarehouseId(), queryParam.getMaterialClassId(), queryParam.getIsEmptyWare(), queryParam.getPageNumber(), queryParam.getPageSize(), queryParam.getCode(), queryParam.getSpecifications(), queryParam.getWeight());

		StockMaterial material = new StockMaterial();
		BigDecimal qty = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);
		for (StockMaterial stockMaterial : result.getResult())
		{
			qty = qty.add(stockMaterial.getQty());
			money = money.add(stockMaterial.getMoney());
		}
		Material _material = new Material();
		material.setMaterial(_material);
		material.setQty(qty);
		material.setMoney(money);
		result.getResult().add(material);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 快速选择库存列表
	 * </pre>
	 * @param warehouseId
	 * @param multiple
	 * @param isEmptyWare
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:24:52, think
	 */
	@RequestMapping(value = "quick_select")
	public String quickSelect(Long warehouseId, String multiple, String isEmptyWare, ModelMap map)
	{
		map.put("warehouseId", warehouseId);
		map.put("multiple", "false".equals(multiple) ? false : true);
		// 不显示0库存
		map.put("isEmptyWare", "no".equals(isEmptyWare) ? BoolValue.NO : BoolValue.YES);
		return "stock/material/quick_select";
	}

	/**
	 * <pre>
	 * 数据 - 快速选择库存列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:25:11, think
	 */
	@RequestMapping(value = "quick_ajaxList")
	@ResponseBody
	public SearchResult<StockMaterial> quick_ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterial> result = serviceFactory.getMaterialStockService().quickFindByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 材料库存预警
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:25:32, think
	 */
	@RequestMapping(value = "stock_warn")
	@RequiresPermissions("stock:material_stockwarn:list")
	public String stockWarn()
	{
		return "stock/material/warn";
	}

	/**
	 * <pre>
	 * 数据 - 材料库存预警
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:25:43, think
	 */
	@RequestMapping(value = "stock_warn_list")
	@ResponseBody
	public SearchResult<StockMaterial> transmit_purch_stock_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterial> result = serviceFactory.getMaterialStockService().stockWarn(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 材料出入库明细
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:25:58, think
	 */
	@RequestMapping(value = "logDetailList")
	@RequiresPermissions("stock:material:logDetailList")
	public String logDetailList(ModelMap map)
	{
		List<BillType> billTypeList = new ArrayList<BillType>();
		billTypeList.add(BillType.PURCH_PN);
		billTypeList.add(BillType.PURCH_PR);
		billTypeList.add(BillType.STOCK_SMOI);
		billTypeList.add(BillType.STOCK_SMOO);
		billTypeList.add(BillType.STOCK_SMI);
		billTypeList.add(BillType.STOCK_SMA);
		billTypeList.add(BillType.STOCK_SMT);
		billTypeList.add(BillType.STOCK_MR);
		billTypeList.add(BillType.STOCK_SM);
		billTypeList.add(BillType.STOCK_RM);
		billTypeList.add(BillType.BEGIN_MATERIAL);
		map.put("billTypeList", billTypeList);
		return "stock/material/logDetailList";
	}

	/**
	 * <pre>
	 * 数据 - 材料出入库明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:26:08, think
	 */
	@RequestMapping(value = "ajaxLogDetailList")
	@ResponseBody
	public SearchResult<StockMaterialLog> ajaxLogDetailList(@RequestBody QueryParam queryParam)
	{
		if (queryParam.getSearchContent() != "")
		{
			queryParam.setBillType(BillType.valueOf(queryParam.getSearchContent()));
		}
		SearchResult<StockMaterialLog> result = serviceFactory.getMaterialStockService().findStockMaterialLogDetailList(queryParam);

		StockMaterialLog materialLog = new StockMaterialLog();

		BigDecimal inQty = new BigDecimal(0);
		BigDecimal outQty = new BigDecimal(0);
		BigDecimal inMoney = new BigDecimal(0);
		BigDecimal outMoney = new BigDecimal(0);

		for (StockMaterialLog stockMaterialLog : result.getResult())
		{
			if (stockMaterialLog.getInQty() != null)
			{
				inQty = inQty.add(stockMaterialLog.getInQty());
			}
			if (stockMaterialLog.getOutQty() != null)
			{
				outQty = outQty.add(stockMaterialLog.getOutQty());
			}
			if (stockMaterialLog.getInMoney() != null)
			{
				inMoney = inMoney.add(stockMaterialLog.getInMoney());
			}
			if (stockMaterialLog.getOutMoney() != null)
			{
				outMoney = outMoney.add(stockMaterialLog.getOutMoney());
			}
		}
		materialLog.setInQty(inQty);
		materialLog.setOutQty(outQty);
		materialLog.setInMoney(inMoney);
		materialLog.setOutMoney(outMoney);
		result.getResult().add(materialLog);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 材料出入库汇总
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:26:17, think
	 */
	@RequestMapping(value = "logSumList")
	@RequiresPermissions("stock:material:logSumList")
	public String logSumList()
	{
		return "stock/material/logSumList";
	}

	/**
	 * <pre>
	 * 数据 - 材料出入库汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:26:27, think
	 */
	@RequestMapping(value = "ajaxlogSumList")
	@ResponseBody
	public SearchResult<StockMaterialLog> ajaxlogSumList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialLog> result = serviceFactory.getMaterialStockService().findStockMaterialLogSumList(queryParam);

		StockMaterialLog materialLog = new StockMaterialLog();

		BigDecimal inQty = new BigDecimal(0);
		BigDecimal outQty = new BigDecimal(0);
		BigDecimal inMoney = new BigDecimal(0);
		BigDecimal outMoney = new BigDecimal(0);

		for (StockMaterialLog stockMaterialLog : result.getResult())
		{
			inQty = inQty.add(stockMaterialLog.getInQty());
			outQty = outQty.add(stockMaterialLog.getOutQty());
			inMoney = inMoney.add(stockMaterialLog.getInMoney());
			outMoney = outMoney.add(stockMaterialLog.getOutMoney());
		}
		materialLog.setInQty(inQty.setScale(2, RoundingMode.HALF_UP));
		materialLog.setOutQty(outQty.setScale(2, RoundingMode.HALF_UP));
		materialLog.setInMoney(inMoney.setScale(2, RoundingMode.HALF_UP));
		materialLog.setOutMoney(outMoney.setScale(2, RoundingMode.HALF_UP));
		result.getResult().add(materialLog);

		return result;
	}
}
