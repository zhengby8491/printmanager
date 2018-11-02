/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月29日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.basic;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.i18n.ResourceBundleMessageSource;
import com.huayin.printmanager.i18n.service.BasicI18nResource;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.UpdateNameType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 供应商信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月29日
 * @since        2.0, 2017年12月29日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/supplier")
public class SupplierController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 供应商信息列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:42:42, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:supplier:list")
	public String list()
	{
		return "basic/supplier/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:43:00, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<Supplier> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getSupplierService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 供应商新增
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:43:09, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:supplier:create")
	public String create(ModelMap map)
	{
		map.put("code", serviceFactory.getCommonService().getNextCode(BasicType.SUPPLIER));
		return "basic/supplier/create";
	}

	/**
	 * <pre>
	 * 页面 - 供应商复制
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:43:32, think
	 */
	@RequestMapping(value = "copyCreate/{id}")
	@RequiresPermissions("basic:supplier:copyCreate")
	public String copyCreate(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Supplier supplier = serviceFactory.getSupplierService().get(id);
		map.put("supplier", supplier);
		map.put("code", serviceFactory.getCommonService().getNextCode(BasicType.SUPPLIER));
		return "basic/supplier/copyCreate";
	}

	/**
	 * <pre>
	 * 功能 - 新增供应商
	 * </pre>
	 * @param supplier
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月29日 上午11:40:44, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:supplier:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "供应商信息", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody Supplier supplier, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(supplier.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Supplier obj = serviceFactory.getSupplierService().getByName(supplier.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		// 如果没有币别，设置默认为人民币
		if (supplier.getCurrencyType() == null)
		{
			supplier.setCurrencyType(CurrencyType.RMB);
		}
		serviceFactory.getSupplierService().save(supplier);
		UserUtils.clearCacheBasic(BasicType.SUPPLIER);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 供应商修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月29日 上午11:40:58, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:supplier:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Supplier supplier = serviceFactory.getSupplierService().get(id);
		map.put("supplier", supplier);
		return "basic/supplier/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改供应商
	 * </pre>
	 * @param supplier
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月29日 上午11:41:24, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:supplier:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "供应商信息", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody Supplier supplier, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(supplier.getId(), supplier.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Supplier _supplier = serviceFactory.getSupplierService().get(supplier.getId());
		Supplier obj = serviceFactory.getSupplierService().getByName(supplier.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_supplier.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_supplier.setType(supplier.getType());
		_supplier.setSupplierClassId(supplier.getSupplierClassId());
		_supplier.setName(supplier.getName());
		_supplier.setEmployeeId(supplier.getEmployeeId());
		_supplier.setTaxRateId(supplier.getTaxRateId());
		_supplier.setPaymentClassId(supplier.getPaymentClassId());
		_supplier.setSettlementClassId(supplier.getSettlementClassId());
		_supplier.setDeliveryClassId(supplier.getDeliveryClassId());
		_supplier.setCorporate(supplier.getCorporate());
		_supplier.setUrl(supplier.getUrl());
		_supplier.setRegisteredCapital(supplier.getRegisteredCapital());
		_supplier.setMemo(supplier.getMemo());
		_supplier.setAddressList(supplier.getAddressList());
		_supplier.setPayerList(supplier.getPayerList());
		_supplier.setUpdateName(UserUtils.getUser().getUserName());
		_supplier.setUpdateTime(new Date());
		_supplier.setIsValid(supplier.getIsValid());
		_supplier.setCurrencyType(supplier.getCurrencyType());
		_supplier.setCompanyId(supplier.getCompanyId());
		serviceFactory.getSupplierService().update(_supplier);
		UserUtils.clearCacheBasic(BasicType.SUPPLIER);
		return returnSuccessBody();
	}
	
	/**
	 * <pre>
	 * 功能 - 更新代工平台
	 * </pre>
	 * @param supplier
	 * @param map
	 * @return
	 * @since 1.0, 2018年4月2日 上午10:26:44, think
	 */
	@RequestMapping(value = "updateOem")
	@ResponseBody
	@RequiresPermissions("basic:supplier:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "供应商信息", Operation = Operation.UPDATE)
	public AjaxResponseBody updateOem(@RequestBody Supplier supplier, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(supplier.getId(), supplier.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Supplier _supplier = serviceFactory.getSupplierService().get(supplier.getId());
		Supplier obj = serviceFactory.getSupplierService().getByName(supplier.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_supplier.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		//_supplier.setName(supplier.getName());
		//_supplier.setCompanyId(UserUtils.getCompanyId());
		_supplier.setOriginCompanyId(supplier.getOriginCompanyId());
		serviceFactory.getSupplierService().update(_supplier);
		UserUtils.clearCacheBasic(BasicType.SUPPLIER);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除供应商
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月29日 上午11:41:36, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:supplier:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "供应商信息", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.SUPPLIER, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <pre>
	 * 功能 - 批量删除供应商
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2017年12月29日 上午11:41:47, think
	 */
	@RequestMapping(value = "batchDelete")
	@ResponseBody
	@RequiresPermissions("basic:supplier:batchDelete")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "供应商信息", Description = "批量删除", Operation = Operation.DELETE)
	public AjaxResponseBody batchDelete(@RequestParam("ids[]") Long[] ids)
	{
		if (ids.length == 0)
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Supplier supplier = new Supplier();
		for (Long id : ids)
		{
			if (serviceFactory.getCommonService().isUsed(BasicType.SUPPLIER, id))
			{
				supplier = serviceFactory.getSupplierService().get(id);
				return returnErrorBody(ResourceBundleMessageSource.i18nFormatter(BasicI18nResource.SUPPLIER_VALIDATE_MSG1, supplier.getName()));
			}
		}
		serviceFactory.getSupplierService().deleteByIds(ids);
		return returnSuccessBody();
	}
	
	/**
	 * <pre>
	 * 页面 - 跳转到变更名称页面
	 * </pre>
	 * @param id
	 * @param name
	 * @param code
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月7日 上午9:57:24, zhengby
	 */
	@RequestMapping(value = "updateName/{id}")
	@RequiresPermissions("basic:supplier:updateName")
	public String updateName(@PathVariable Long id, ModelMap map)
	{
		Supplier supplier = serviceFactory.getSupplierService().get(id);
		map.put("id", id);
		map.put("name", supplier.getName());
		map.put("code", supplier.getCode());
		return "basic/supplier/updateName";
	}
	
	/**
	 * <pre>
	 * 功能 - 全局变更供应商名称
	 * </pre>
	 * @param customer
	 * @return
	 * @since 1.0, 2018年2月7日 上午9:57:51, zhengby
	 */
	@RequestMapping(value = "updateName")
	@ResponseBody
	@RequiresPermissions("basic:supplier:updateName")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "供应商信息", Operation = Operation.UPDATE)
	public AjaxResponseBody updateName(@RequestBody Supplier supplier)
	{
		if (serviceFactory.getCommonService().isExist(supplier.getName(), UpdateNameType.SUPPLIER))
		{
			return returnErrorBody("已存在该名称");
		}else
		{
			serviceFactory.getCommonService().updateBasicName(supplier.getId(), supplier.getName(), UpdateNameType.SUPPLIER);
		}
		return returnSuccessBody();
	}
}
