/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年2月23日上午10:46:17
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.produce;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 生产管理 - 工单转单
 * </pre>
 * @author       zhengby
 * @version 		 1.0, 2016年9月23日
 * @version 	   2.0, 2018年2月23日上午10:46:28, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/produce/transmit")
public class ProduceTransmitController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 跳转到工单转入库
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:47:20, zhengby
	 */
	@RequestMapping(value = "to_product_in")
	@RequiresPermissions("product:transmit:to_product_in")
	public String transmit_product_in()
	{
		return "produce/transmit/to_product_in";
	}

	/**
	 * <pre>
	 * 功能 - 返回工单转入库数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:47:55, zhengby
	 */
	@RequestMapping(value = "to_product_in_list")
	@ResponseBody
	public SearchResult<WorkProduct> transmit_product_in_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<WorkProduct> result = serviceFactory.getStockProductInService().findForTransmitProductIn(queryParam);
		Integer produceQty = new Integer(0);
		Integer inStockQty = new Integer(0);
		for (WorkProduct w : result.getResult())
		{
			produceQty += w.getProduceQty();
			inStockQty += w.getInStockQty();
		}
		WorkProduct work = new WorkProduct();
		work.setProduceQty(produceQty);
		work.setInStockQty(inStockQty);

		result.getResult().add(work);

		return result;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到工单转领料
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:48:23, zhengby
	 */
	@RequestMapping(value = "to_take")
	@RequiresPermissions("product:transmit:to_take")
	public String transmit_take()
	{
		return "produce/transmit/to_take";
	}

	/**
	 * <pre>
	 * 功能 - 返回工单转领料数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:48:43, zhengby
	 */
	@RequestMapping(value = "to_take_list")
	@ResponseBody
	public SearchResult<WorkMaterial> transmit_take_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<WorkMaterial> result = serviceFactory.getStockMaterialTakeService().findForTransmitTake(queryParam);
		// 查询所有工单WorkProduct，并设置到材料中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (WorkMaterial workMaterial : result.getResult())
		{
			long workId = workMaterial.getWorkId();
			List<WorkProduct> list = productMap.get(workId);
			if (null != list)
			{
				workMaterial.setProductList(list);
			}
		}
		BigDecimal qty = new BigDecimal(0);
		BigDecimal takeQty = new BigDecimal(0);
		for (WorkMaterial w : result.getResult())
		{
			qty = qty.add(w.getQty());
			takeQty = takeQty.add(w.getTakeQty());
		}
		WorkMaterial work = new WorkMaterial();
		work.setQty(qty.setScale(2, RoundingMode.HALF_UP));
		work.setTakeQty(takeQty.setScale(2, RoundingMode.HALF_UP));

		result.getResult().add(work);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到生产日报表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:49:09, zhengby
	 */
	@RequestMapping(value = "to_product_daily_report")
	@RequiresPermissions("daily:work:list")
	public String to_product_daily_report()
	{
		return "produce/work_daily_report";
	}

	/**
	 * <pre>
	 * 页面 - 跳转到未生产订单列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:50:04, zhengby
	 */
	@RequestMapping(value = "notProducedList")
	@RequiresPermissions("product:transmit:notProducedList")
	public String notProducedList()
	{
		return "produce/transmit/notProducedList";
	}
}
