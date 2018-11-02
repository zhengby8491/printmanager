/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月12日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.domain.annotation.AdminAuth;
import com.huayin.printmanager.persist.entity.basic.Account;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.persist.entity.begin.SupplierBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjustDetail;
import com.huayin.printmanager.persist.entity.oem.OemReconcilDetail;
import com.huayin.printmanager.persist.entity.offer.OfferBflute;
import com.huayin.printmanager.persist.entity.offer.OfferPaper;
import com.huayin.printmanager.persist.entity.offer.OfferProcedure;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.enumerate.AdjustType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OfferMaterialType;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.persist.enumerate.ProductType;
import com.huayin.printmanager.persist.enumerate.SupplierType;
import com.huayin.printmanager.service.finance.vo.FinanceShouldSumVo;
import com.huayin.printmanager.service.stock.vo.StockMaterialVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 公共 - 快捷选择
 * </pre>
 * @author       think
 * @since        1.0, 2018年2月12日
 */
@Controller
@RequestMapping(value = "${basePath}/quick")
public class QuickController extends BaseController
{
	private static String path = "quick/";

	/**
	 * <pre>
	 * 页面 - 供应商选择
	 * </pre>
	 * @param map
	 * @param supplierType
	 * @param supplierClassId
	 * @param supplierName
	 * @param multiple
	 * @param isBegin
	 * @return
	 * @since 1.0, 2018年2月12日 上午9:56:51, think
	 */
	@RequestMapping(value = "supplier_select")
	public String supplierSelect(ModelMap map, SupplierType supplierType, Long supplierClassId, String supplierName, Boolean multiple, String isBegin)
	{
		if (supplierType != null)
		{
			map.put("supplierType", supplierType);
		}
		map.put("supplierName", supplierName);
		map.put("supplierClassId", supplierClassId);
		map.put("multiple", multiple == null ? false : multiple);
		map.put("isBegin", isBegin);
		return path + "supplier_select";
	}

	/**
	 * <pre>
	 * 数据 - 供应商选择
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午9:57:08, think
	 */
	@RequestMapping(value = "supplier_list")
	@ResponseBody
	public SearchResult<Supplier> supplierList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getSupplierService().quickFindByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 供应商代工平台
	 * </pre>
	 * @param map
	 * @param supplierType
	 * @param supplierClassId
	 * @param supplierName
	 * @param multiple
	 * @param isBegin
	 * @return
	 * @since 1.0, 2018年2月12日 上午9:56:51, think
	 */
	@RequestMapping(value = "supplierOem")
	public String supplierOem(ModelMap map, SupplierType supplierType, Long supplierClassId, String supplierName, Boolean multiple, String isBegin)
	{
		if (supplierType != null)
		{
			map.put("supplierType", supplierType);
		}
		map.put("supplierName", supplierName);
		map.put("supplierClassId", supplierClassId);
		map.put("multiple", multiple == null ? false : multiple);
		map.put("isBegin", isBegin);
		return path + "supplier_oem";
	}

	/**
	 * <pre>
	 * 数据 - 供应商代工平台
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午9:57:08, think
	 */
	@RequestMapping(value = "supplierOemList")
	@ResponseBody
	public SearchResult<Supplier> supplierOemList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getQuickService().findSupplierOemList(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 供应商添加
	 * </pre>
	 * @param map
	 * @param defaultSupplierType
	 * @return
	 * @since 1.0, 2018年2月12日 上午9:58:32, think
	 */
	@RequestMapping(value = "supplier_add")
	public String supplierAdd(ModelMap map, SupplierType defaultSupplierType, String supplierName, String supplierAddress, String supplierLinkName, String supplierMobile, String originCompanyId)
	{
		map.put("defaultSupplierType", defaultSupplierType);
		if (StringUtils.isNotBlank(supplierName))
		{
			map.put("supplierName", supplierName);
		}
		if (StringUtils.isNotBlank(supplierAddress))
		{
			map.put("supplierAddress", supplierAddress);
		}
		if (StringUtils.isNotBlank(supplierLinkName))
		{
			map.put("supplierLinkName", supplierLinkName);
		}
		if (StringUtils.isNotBlank(supplierMobile))
		{
			map.put("supplierMobile", supplierMobile);
		}
		if (StringUtils.isNotBlank(originCompanyId))
		{
			map.put("originCompanyId", originCompanyId);
		}

		return path + "supplier_add";
	}

	/**
	 * <pre>
	 * 页面 - 客户选择
	 * </pre>
	 * @param map
	 * @param customerClassId
	 * @param customerName
	 * @param multiple
	 * @param isBegin
	 * @return
	 * @since 1.0, 2018年2月12日 上午9:58:48, think
	 */
	@RequestMapping(value = "customer_select")
	public String customerSelect(ModelMap map, Long customerClassId, String customerName, Boolean multiple, String isBegin)
	{
		map.put("customerName", customerName);
		map.put("customerClassId", customerClassId);
		map.put("isBegin", isBegin);
		map.put("multiple", multiple == null ? false : multiple);
		return path + "customer_select";
	}

	/**
	 * <pre>
	 * 数据 - 客户选择
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午9:59:42, think
	 */
	@RequestMapping(value = "customer_list")
	@ResponseBody
	public SearchResult<Customer> customerList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getCustomerService().quickFindByCondition(queryParam);
	}
	
	/**
	 * <pre>
	 * 页面 - 客户代工平台
	 * </pre>
	 * @param map
	 * @param customerClassId
	 * @param customerName
	 * @param multiple
	 * @param isBegin
	 * @return
	 * @since 1.0, 2018年3月19日 上午10:42:10, think
	 */
	@RequestMapping(value = "customerOem")
	public String customerOem(ModelMap map, Long customerClassId, String customerName, Boolean multiple, String isBegin)
	{
		map.put("customerName", customerName);
		map.put("customerClassId", customerClassId);
		map.put("isBegin", isBegin);
		map.put("multiple", multiple == null ? false : multiple);
		return path + "customer_oem";
	}
	
	/**
	 * <pre>
	 * 数据 - 客户代工平台
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月19日 上午10:42:26, think
	 */
	@RequestMapping(value = "customerOemList")
	@ResponseBody
	public SearchResult<Customer> customerOemList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getQuickService().findCustomerOemList(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 客户添加
	 * </pre>
	 * @param map
	 * @param customerName
	 * @param customerAddress
	 * @param customerLinkName
	 * @param customerMobile
	 * @param originCompanyId
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:00:09, think
	 * @since 2.0, 2018年3月19日 下午2:08:28, think, 增加支持代工平台
	 */
	@RequestMapping(value = "customer_add")
	public String customerAdd(ModelMap map, String customerName, String customerAddress, String customerLinkName, String customerMobile, String originCompanyId, String callAfter)
	{
		if (StringUtils.isNotBlank(customerName))
		{
			map.put("customerName", customerName);
		}
		if (StringUtils.isNotBlank(customerAddress))
		{
			map.put("customerAddress", customerAddress);
		}
		if (StringUtils.isNotBlank(customerLinkName))
		{
			map.put("customerLinkName", customerLinkName);
		}
		if (StringUtils.isNotBlank(customerMobile))
		{
			map.put("customerMobile", customerMobile);
		}
		if (StringUtils.isNotBlank(originCompanyId))
		{
			map.put("originCompanyId", originCompanyId);
		}
		if (StringUtils.isNoneBlank(callAfter))
		{
			map.put("callAfter", callAfter);
		}
		
		return path + "customer_add";
	}

	/**
	 * <pre>
	 * 页面 - 员工信息选择
	 * </pre>
	 * @param map
	 * @param customerClassId
	 * @param customerName
	 * @param multiple
	 * @param isBegin
	 * @return
	 * @since 1.0, 2018年2月12日 上午9:59:02, think
	 */
	@RequestMapping(value = "employee_select")
	public String employeeSelect(ModelMap map, Long customerClassId, String customerName, Boolean multiple, String isBegin)
	{
		map.put("customerName", customerName);
		map.put("customerClassId", customerClassId);
		map.put("isBegin", isBegin);
		map.put("multiple", multiple == null ? false : multiple);
		return path + "employee_select";
	}

	/**
	 * <pre>
	 * 页面 - 公司选择
	 * </pre>
	 * @param map
	 * @param multiple
	 * @return
	 * @since 1.0, 2018年2月12日 上午9:59:15, think
	 */
	@RequestMapping(value = "company_select")
	public String companySelect(ModelMap map, Boolean multiple)
	{
		map.put("multiple", multiple == null ? false : multiple);
		return path + "company_select";
	}

	/**
	 * <pre>
	 * 页面  - 产品选择
	 * </pre>
	 * @param map
	 * @param multiple
	 * @param productType
	 * @param customerId
	 * @param customerName
	 * @param productClassId
	 * @param productName
	 * @param isBegin
	 * @param warehouseId
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:00:24, think
	 */
	@RequestMapping("product_select")
	public String productSelect(ModelMap map, Boolean multiple, ProductType productType, Long customerId, String customerName, Long productClassId, String productName, BoolValue isBegin, Long warehouseId)
	{
		map.put("multiple", multiple == null ? false : multiple);
		map.put("productType", productType);
		map.put("customerId", customerId);
		map.put("customerName", customerName);
		map.put("productClassId", productClassId);
		map.put("productName", productName);
		map.put("isBegin", isBegin);
		map.put("warehouseId", warehouseId);
		return path + "product_select";
	}

	/**
	 * <pre>
	 * 数据 - 产品选择
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:00:43, think
	 */
	@RequestMapping(value = "product_list")
	@ResponseBody
	public SearchResult<Product> productList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getProductService().quickFindByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 产品添加
	 * </pre>
	 * @param customerId
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:00:55, think
	 */
	@RequestMapping(value = "product_add")
	public String productAdd(Long customerId, ModelMap map)
	{
		map.put("customerId", customerId);
		return path + "product_add";
	}

	/**
	 * <pre>
	 * 页面 - 材料选择
	 * </pre>
	 * @param map
	 * @param multiple
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:01:29, think
	 */
	@RequestMapping(value = "material_select")
	public String materialSelect(ModelMap map, Boolean multiple)
	{
		map.put("multiple", multiple == null ? false : multiple);
		return path + "material_select";
	}

	/**
	 * 数据 - 材料选择
	 */
	@RequestMapping(value = "material_list")
	@ResponseBody
	public SearchResult<Material> materialList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getMaterialService().quickFindByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 功能 - 材料添加
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:05:00, think
	 */
	@RequestMapping(value = "material_add")
	public String materialAdd()
	{
		return path + "material_add";
	}

	/**
	 * <pre>
	 * 页面 - 工单材料选择
	 * </pre>
	 * @param map
	 * @param multiple
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:01:42, think
	 */
	@RequestMapping(value = "work_material_select")
	public String workMaterialSelect(ModelMap map, Boolean multiple)
	{
		map.put("multiple", multiple == null ? false : multiple);
		return path + "work_material_select";
	}

	/**
	 * <pre>
	 * 报价工序材料选择
	 * </pre>
	 * @param map
	 * @param multiple
	 * @return
	 * @since 1.0, 2018年2月12日 上午9:05:34, think
	 */
	@RequestMapping(value = "offer_material_select")
	public String offerMaterialSelect(ModelMap map, Boolean multiple)
	{
		map.put("multiple", multiple == null ? false : multiple);
		return path + "offer_material_select";
	}

	/**
	 * <pre>
	 * 数据 - 报价工序材料
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午9:08:38, think
	 */
	@RequestMapping(value = "offerMaterialList")
	@ResponseBody
	public SearchResult<Map<String, Object>> offerMaterialList(@RequestBody QueryParam queryParam)
	{
		SearchResult<Map<String, Object>> result = new SearchResult<>();
		List<Map<String, Object>> resultList = Lists.newArrayList();
		result.setResult(resultList);
		List<String> materialNameList = Lists.newArrayList();
		// 材料集合
		List<OfferPaper> offerPaperList = Lists.newArrayList();
		// 坑纸集合
		List<OfferBflute> offerBfluteList = Lists.newArrayList();

		// 查询全部
		if (queryParam.getOfferMaterialType() == null)
		{
			SearchResult<OfferPaper> paperList = serviceFactory.getOfferSettingService().getPaperList(queryParam);
			offerPaperList.addAll(paperList.getResult());
			SearchResult<OfferBflute> bfluteList = serviceFactory.getOfferSettingService().getBfluteList(queryParam);
			offerBfluteList.addAll(bfluteList.getResult());
		}
		// 查询材料
		else if (queryParam.getOfferMaterialType() == OfferMaterialType.MATERIAL)
		{
			SearchResult<OfferPaper> paperList = serviceFactory.getOfferSettingService().getPaperList(queryParam);
			offerPaperList.addAll(paperList.getResult());
		}
		// 查询坑纸
		else if (queryParam.getOfferMaterialType() == OfferMaterialType.BFLUTE)
		{
			SearchResult<OfferBflute> bfluteList = serviceFactory.getOfferSettingService().getBfluteList(queryParam);
			offerBfluteList.addAll(bfluteList.getResult());
		}

		// 材料去除重复
		for (OfferPaper offerPaper : offerPaperList)
		{
			String name = offerPaper.getWeight() + "克" + offerPaper.getName();
			if (!materialNameList.contains(name))
			{
				materialNameList.add(name);
				Map<String, Object> obj = Maps.newHashMap();
				obj.put("type", OfferMaterialType.MATERIAL);
				obj.put("typeText", "材料");
				obj.put("name", name);
				obj.put("weight", offerPaper.getWeight());
				obj.put("price", offerPaper.getTonPrice());
				obj.put("className", offerPaper.getName());
				resultList.add(obj);
			}
		}

		// 坑纸去除重复
		for (OfferBflute offerBflute : offerBfluteList)
		{
			String name = offerBflute.getPit() + offerBflute.getPaperQuality();
			if (!materialNameList.contains(name))
			{
				materialNameList.add(name);
				Map<String, Object> obj = Maps.newHashMap();
				obj.put("type", OfferMaterialType.BFLUTE);
				obj.put("typeText", "坑纸");
				obj.put("name", name);
				obj.put("weight", 0);
				obj.put("price", offerBflute.getPrice());
				obj.put("className", offerBflute.getPit());
				resultList.add(obj);
			}
		}

		// 最后根据查询条件过滤名称
		List<Map<String, Object>> conditions = Lists.newArrayList();
		for (Map<String, Object> r : resultList)
		{
			String name = (String) r.get("name");
			if (StringUtils.isNotBlank(queryParam.getMaterialName()) && name.indexOf(queryParam.getMaterialName()) != -1)
			{
				conditions.add(r);
			}
		}
		if (conditions.size() > 0)
		{
			resultList.clear();
			resultList.addAll(conditions);
		}

		// 设置总数量
		result.setCount(resultList.size());

		return result;
	}

	/**
	 * <pre>
	 * 根据材料ID 查此材料的库存数据
	 * </pre>
	 * @param materialId
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:04:40, think
	 */
	@RequestMapping(value = "findStockByMaterialId")
	@ResponseBody
	public List<StockMaterialVo> findStockByMaterialId(Long materialId)
	{
		return serviceFactory.getMaterialStockService().findByMaterialId(materialId);
	}

	/**
	 * <pre>
	 * 页面  - 工序选择
	 * </pre>
	 * @param map
	 * @param multiple
	 * @param procedureClassId
	 * @param procedureName
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:05:48, think
	 */
	@RequestMapping(value = "procedure_select")
	public String procedureSelect(ModelMap map, Boolean multiple, Long procedureClassId, String procedureName, Boolean isToOem)
	{
		map.put("multiple", multiple == null ? false : multiple);
		map.put("procedureClassId", procedureClassId);
		map.put("procedureName", procedureName);
		map.put("isToOem", isToOem);
		return path + "procedure_select";
	}

	/**
	 * <pre>
	 * 数据 - 工序选择
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:06:43, think
	 */
	@RequestMapping(value = "procedure_list")
	@ResponseBody
	public SearchResult<Procedure> procedureList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getProcedureService().quickFindByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 数据 - 工序选择(根据类型过滤)
	 * </pre>
	 * @param map
	 * @param multiple
	 * @param procedureTypeArray
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:06:06, think
	 */
	@RequestMapping(value = "procedure_select_filter")
	public String procedureSelect(ModelMap map, Boolean multiple, String procedureTypeArray)
	{
		map.put("multiple", multiple == null ? false : multiple);
		String[] split = procedureTypeArray.split(",");
		ProcedureType[] procedureTypes = new ProcedureType[split.length];
		for (int i = 0; i < split.length; i++)
		{
			procedureTypes[i] = ProcedureType.valueOf(split[i]);
		}
		map.put("procedureTypeArray", procedureTypes);
		return path + "procedure_select";
	}

	/**
	 * <pre>
	 * 页面 - 来源报价系统工序
	 * </pre>
	 * @param map
	 * @param multiple
	 * @return
	 * @since 1.0, 2018年2月12日 上午9:38:55, think
	 */
	@RequestMapping(value = "offer_procedure_select")
	public String offerProcedureSelect(ModelMap map, Boolean multiple)
	{
		map.put("multiple", multiple == null ? false : multiple);
		return path + "offer_procedure_select";
	}

	/**
	 * <pre>
	 * 数据 - 来源报价系统
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午9:40:58, think
	 */
	@RequestMapping(value = "offerProcedureList")
	@ResponseBody
	public SearchResult<Map<String, Object>> offerProcedureList(@RequestBody QueryParam queryParam)
	{
		SearchResult<Map<String, Object>> result = new SearchResult<>();
		List<Map<String, Object>> resultList = Lists.newArrayList();
		result.setResult(resultList);

		List<String> procedureNameList = Lists.newArrayList();

		SearchResult<OfferProcedure> procedureList = serviceFactory.getOfferSettingService().findProcedureDuplicate(queryParam);

		// 工序去除重复（这里已经属于多余代码，已经在sql那边去除重复了，但是不影响性能。 因为页面需要修改对应字段没有改，所以没有删除代码，请注意！）
		for (OfferProcedure offerProcedure : procedureList.getResult())
		{
			String name = offerProcedure.getName();
			if (!procedureNameList.contains(name))
			{
				procedureNameList.add(name);
				Map<String, Object> obj = Maps.newHashMap();
				obj.put("type", offerProcedure.getProcedureType());
				obj.put("typeText", offerProcedure.getProcedureType().getText());
				obj.put("name", name);
				obj.put("className", offerProcedure.getProcedureClass());
				resultList.add(obj);
			}
		}

		// 最后根据查询条件过滤名称
		List<Map<String, Object>> conditions = Lists.newArrayList();
		for (Map<String, Object> r : resultList)
		{
			String name = (String) r.get("name");
			if (StringUtils.isNotBlank(queryParam.getProcedureName()) && name.indexOf(queryParam.getProcedureName()) != -1)
			{
				conditions.add(r);
			}
		}
		if (conditions.size() > 0)
		{
			resultList.clear();
			resultList.addAll(conditions);
		}

		// 设置总数量
		result.setCount(resultList.size());

		return result;
	}

	/**
	 * <pre>
	 * 页面 - 账户选择
	 * </pre>
	 * @param map
	 * @param multiple
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:08:03, think
	 */
	@RequestMapping(value = "account_select")
	public String accountSelect(ModelMap map, Boolean multiple, String isBegin)
	{
		map.put("multiple", multiple == null ? false : multiple);
		map.put("isBegin", isBegin);
		return path + "account_select";
	}

	/**
	 * <pre>
	 * 数据 - 账户选择
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:08:15, think
	 */
	@RequestMapping(value = "account_list")
	@ResponseBody
	public List<Account> accountList(Boolean isBegin)
	{
		return serviceFactory.getAccountService().quickSelect(isBegin);
	}

	/**
	 * <pre>
	 * 页面 - 付款单选择
	 * </pre>
	 * @param multiple
	 * @param supplierId
	 * @param map
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月12日 上午10:08:31, think
	 */
	@RequestMapping(value = "payment_select")
	public String paymentSelect(Boolean multiple, Long supplierId, ModelMap map) throws Exception
	{
		map.put("supplierId", supplierId);
		map.put("multiple", multiple);
		return path + "payment_select";
	}

	/**
	 * <pre>
	 * 数据 - 付款单选择 - 发外对账
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:09:22, think
	 */
	@RequestMapping(value = "payment_list_outSource")
	@ResponseBody
	public SearchResult<OutSourceReconcilDetail> paymentListOutSource(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getPaymentService().findOutSourceReconcilDetail(queryParam);
	}

	/**
	 * <pre>
	 * 数据 - 付款单选择 - 采购对账
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:09:46, think
	 */
	@RequestMapping(value = "payment_list_purch")
	@ResponseBody
	public SearchResult<PurchReconcilDetail> paymentListPurch(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getPaymentService().findPurchReconcilDetail(queryParam);
	}

	/**
	 * <pre>
	 * 数据 - 付款单选择 - 综合供应商对账
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:10:02, think
	 */
	@RequestMapping(value = "payment_list_compre")
	@ResponseBody
	public SearchResult<FinanceShouldSumVo> compreSupplierList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinanceShouldSumVo> result = serviceFactory.getPaymentService().findCompreSupplierPayment(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 付款单选择 - 供应商期初
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:10:28, think
	 */
	@RequestMapping(value = "payment_list_supplierBegin")
	@ResponseBody
	public SearchResult<SupplierBeginDetail> paymentListSupplierBegin(@RequestBody QueryParam queryParam)
	{
		SearchResult<SupplierBeginDetail> result = serviceFactory.getPaymentService().findSupplierBeginDetail(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 收款单选择
	 * </pre>
	 * @param multiple
	 * @param customerId
	 * @param map
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月12日 上午10:10:39, think
	 */
	@RequestMapping(value = "receive_select")
	public String receiveSelect(Boolean multiple, Long customerId, ModelMap map) throws Exception
	{
		map.put("customerId", customerId);
		map.put("multiple", multiple);
		return path + "receive_select";
	}

	/**
	 * <pre>
	 * 数据 - 收款单选择 - 销售对账
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:10:59, think
	 */
	@RequestMapping(value = "receive_list_sale")
	@ResponseBody
	public SearchResult<SaleReconcilDetail> receiveListSale(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getReceiveService().findSaleReconcilDetailList(queryParam);
	}
	
	/**
	 * <pre>
	 * 数据  - 收款单选择  - 代工对账
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月19日 下午3:56:21, zhengby
	 */
	@RequestMapping(value = "receive_list_oem")
	@ResponseBody
	public SearchResult<OemReconcilDetail> receiveListOem(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getReceiveService().findOemReconcilDetailList(queryParam);
	}

	/**
	 * <pre>
	 * 数据 - 收款单选择 - 客户期初
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:11:30, think
	 */
	@RequestMapping(value = "receive_list_customerBegin")
	@ResponseBody
	public SearchResult<CustomerBeginDetail> receiveListCustomerBegin(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getReceiveService().findCustomerBeginDetailList(queryParam);
	}
	
	/**
	 * <pre>
	 * 数据  - 收款单选择-财务调整单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年6月26日 下午2:11:14, zhengby
	 */
	@RequestMapping(value = "receive_list_financeAdjust")
	@ResponseBody
	public SearchResult<FinanceAdjustDetail> receiveListFinanceAdjust(@RequestBody QueryParam queryParam)
	{
		queryParam.setAdjustType(AdjustType.RECEIVE);
		return serviceFactory.getReceiveService().findFinanceAdjustDetailList(queryParam);
	}
	
	/**
	 * <pre>
	 * 数据  - 付款单选择-财务调整单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年6月26日 下午2:13:59, zhengby
	 */
	@RequestMapping(value = "pay_list_financeAdjust")
	@ResponseBody
	public SearchResult<FinanceAdjustDetail> payListFinanceAdjust(@RequestBody QueryParam queryParam)
	{
		queryParam.setAdjustType(AdjustType.PAY);
		return serviceFactory.getPaymentService().findFinanceAdjustDetailList(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 机台信息选择
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:11:53, think
	 */
	@RequestMapping(value = "machine_select")
	public String machineSelect(ModelMap map)
	{
		return path + "machine_select";
	}

	/**
	 * <pre>
	 * 页面 - 购买支付产品版本选择
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:12:22, think
	 */
	@RequestMapping(value = "buyProductSelect")
	@AdminAuth
	public String buyProductSelect(HttpServletRequest request, ModelMap map)
	{
		String type = request.getParameter("type");
		map.put("type", type);
		return path + "buy_product_select";
	}

	/**
	 * <pre>
	 * 数据 - 查找供应商期初资料
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:16:39, think
	 */
	@RequestMapping(value = "findSupplierBegin")
	@ResponseBody
	public SearchResult<SupplierBeginDetail> findSupplierBegin(@RequestBody QueryParam queryParam)
	{
		SearchResult<SupplierBeginDetail> result = new SearchResult<SupplierBeginDetail>();
		List<SupplierBeginDetail> list = new ArrayList<SupplierBeginDetail>();
		// 从供应商期初资料里查找期初应付款
		SupplierBeginDetail s = serviceFactory.getSupplierBeginService().findSupplierBeginDetail(queryParam.getId());
		// 从供应商基础资料查找供应商的预付款
		SearchResult<Supplier> _result = serviceFactory.getSupplierService().findByCondition(queryParam);
		if (s != null)
		{
			s.setAdvanceMoney(_result.getResult().get(0).getAdvanceMoney());
			list.add(s);
			result.setCount(1);
			result.setResult(list);
		}
		else
		{
			result.setCount(0);
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 查询客户期初
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月12日 上午10:13:09, think
	 */
	@RequestMapping(value = "findCustomerBegin")
	@ResponseBody
	public SearchResult<CustomerBeginDetail> findCustomerBegin(@RequestBody QueryParam queryParam)
	{
		SearchResult<CustomerBeginDetail> result = new SearchResult<CustomerBeginDetail>();
		List<CustomerBeginDetail> list = new ArrayList<CustomerBeginDetail>();
		CustomerBeginDetail c = serviceFactory.getCustomerBeginService().findCustomerBeginDetail(queryParam.getId());
		SearchResult<Customer> _result = serviceFactory.getCustomerService().findByCondition(queryParam);
		if (c != null)
		{
			c.setAdvanceMoney(_result.getResult().get(0).getAdvanceMoney());
			list.add(c);
			result.setCount(1);
			result.setResult(list);
		}
		else
		{
			result.setCount(0);
		}
		return result;
	}
	
	/**
	 * <pre>
	 * 页面  - 跳转到选择发外源单页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月21日 下午4:40:57, zhengby
	 */
	@RequestMapping(value = "oem_source_select")
	public String oem_source_select(ModelMap map,Long originCompanyId)
	{
		map.put("originCompanyId", originCompanyId);
		return path + "oem_source_select";
	}
	
	/**
	 * <pre>
	 * 数据  - 查询发外加工单转代工单的清单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月21日 下午4:41:33, zhengby
	 */
	@RequestMapping(value = "oem_source_list")
	@ResponseBody
	public SearchResult<OutSourceProcessDetail> oemSourceList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceProcessDetail> result = new SearchResult<OutSourceProcessDetail>();
		if (queryParam.getOriginCompanyId() == null)
		{
			return result;
		} else
		{
			result = serviceFactory.getOemZeroService().findTransmitOrderByOutSource(queryParam);
			if (null != result.getResult())
			{
			// 查询所有工单WorkPart2Product，并设置到发外加工明细中
				List<String> companyList = Lists.newArrayList();
				// Map<代工平台公司名称, Customer>
				Map<String, Customer> customerNameMap = Maps.newHashMap();
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
			}
		}
		
		return result;
	}
}
