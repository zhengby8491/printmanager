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
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.service.purch.vo.PurchScheduleVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.service.vo.SumVo;

/**
 * <pre>
 * 采购管理 - 采购订单汇总
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年9月23日, raintear
 * @version 	   1.0, 2018年2月23日下午3:15:23, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/purch/sum")
public class PurchSumController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 跳转到采购订单汇总表（按供应商名称）
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:16:08, zhengby
	 */
	@RequestMapping(value = "by_supplier_name")
	@RequiresPermissions("purch:sum:by_supplier")
	public String by_supplier_name()
	{
		return "purch/sum/by_supplier_name";
	}

	/**
	 * <pre>
	 * 功能 - 返回采购订单汇总表数据（按供应商名称）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:16:58, zhengby
	 */
	@RequestMapping(value = "by_supplier_name_list")
	@ResponseBody
	public SearchResult<SumVo> by_supplier_name_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<SumVo> result = serviceFactory.getPurchSumService().findBySupplierName(queryParam);
		SumVo vo=new SumVo();
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
		for (SumVo sumVo : result.getResult()) {
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
	 * 页面 - 跳转到采购订单汇总表（按供应商分类）
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:17:39, zhengby
	 */
	@RequestMapping(value = "by_supplier_class")
	@RequiresPermissions("purch:sum:by_supplier")
	public String by_supplier_class()
	{
		return "purch/sum/by_supplier_class";
	}

	/**
	 * <pre>
	 * 功能 - 返回采购订单汇总表数据（按供应商分类）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:19:32, zhengby
	 */
	@RequestMapping(value = "by_supplier_class_list")
	@ResponseBody
	public SearchResult<SumVo> by_supplier_class_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<SumVo> result = serviceFactory.getPurchSumService().findBySupplierClass(queryParam);
		SumVo vo=new SumVo();
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
		for (SumVo sumVo : result.getResult()) {
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
	 * 页面 - 跳转到采购订单汇总表（按材料名称）
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:19:57, zhengby
	 */
	@RequestMapping(value = "by_material_name")
	@RequiresPermissions("purch:sum:by_material")
	public String by_material_name()
	{
		return "purch/sum/by_material_name";
	}

	/**
	 * <pre>
	 * 功能 - 返回采购订单汇总表数据（按材料名称）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:20:22, zhengby
	 */
	@RequestMapping(value = "by_material_name_list")
	@ResponseBody
	public SearchResult<SumVo> by_material_name_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<SumVo> result = serviceFactory.getPurchSumService().findByMaterialName(queryParam);
		SumVo vo=new SumVo();
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
		for (SumVo sumVo : result.getResult()) {
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
	 * 页面 - 跳转到采购订单汇总表（按材料分类）
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:20:54, zhengby
	 */
	@RequestMapping(value = "by_material_class")
	@RequiresPermissions("purch:sum:by_material")
	public String by_material_class()
	{
		return "purch/sum/by_material_class";
	}

	/**
	 * <pre>
	 * 功能 - 返回采购订单汇总表数据（按材料分类）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:21:22, zhengby
	 */
	@RequestMapping(value = "by_material_class_list")
	@ResponseBody
	public SearchResult<SumVo> by_material_class_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<SumVo> result = serviceFactory.getPurchSumService().findByMaterialClass(queryParam);
		SumVo vo=new SumVo();
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
		for (SumVo sumVo : result.getResult()) {
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
	 * 页面 - 跳转到采购订单汇总表（按采购员）
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:22:50, zhengby
	 */
	@RequestMapping(value = "by_employee")
	@RequiresPermissions("purch:sum:by_employee")
	public String by_employee()
	{
		return "purch/sum/by_employee";
	}

	/**
	 * <pre>
	 * 功能 - 返回采购订单汇总表数据（按采购员）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:23:18, zhengby
	 */
	@RequestMapping(value = "by_employee_list")
	@ResponseBody
	public SearchResult<SumVo> by_employee_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<SumVo> result = serviceFactory.getPurchSumService().findByEmployeeName(queryParam);
		SumVo vo=new SumVo();
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
		for (SumVo sumVo : result.getResult()) {
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
	 * 页面 - 跳转到采购订单汇总表（按采购进度）
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:24:06, zhengby
	 */
	@RequestMapping(value = "purch_schedule")
	@RequiresPermissions("purch:sum:purch_schedule")
	public String purchSchedule()
	{
		return "purch/sum/purch_schedule";
	}
	
	/**
	 * <pre>
	 * 功能 - 返回采购订单汇总表数据（采购进度）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:24:43, zhengby
	 */
	@RequestMapping(value = "purch_schedule_list")
	@ResponseBody
	public SearchResult<PurchScheduleVo> purch_schedule_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<PurchScheduleVo> result = serviceFactory.getPurchSumService().findPurchSchedule(queryParam);
		// 查询所有工单WorkProduct，并设置到订单明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchScheduleVo vo : result.getResult())
		{
			if (null != vo.getWorkId())
			{
				long workId = vo.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					vo.setProductList(list);
				}
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 返回采购订单创建日期列表数据
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:25:06, zhengby
	 */
	@RequestMapping(value = "getYearsFromPurchOrder")
	@ResponseBody
	public List<PurchOrder> getYearsFromPurchOrder()
	{
		List<PurchOrder> list = serviceFactory.getPurchSumService().getYearsFromPurchOrder();
		return list;
	}
}
