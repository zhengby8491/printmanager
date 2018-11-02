/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月28日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.basic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.SupplierClass;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 供应商类型
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月28日
 * @since        2.0, 2017年12月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/supplierClass")
public class SupplierClassController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 供应商分类列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:04:24, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:supplierClass:list")
	public String list()
	{
		return "basic/supplierClass/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:04:35, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<SupplierClass> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getSupplierClassService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 供应商分类新增
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:04:56, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:supplierClass:create")
	public String create()
	{
		return "basic/supplierClass/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增供应商分类
	 * </pre>
	 * @param supplierClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:05:07, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:supplierClass:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "供应商分类", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody SupplierClass supplierClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(supplierClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		SupplierClass obj = serviceFactory.getSupplierClassService().getByName(supplierClass.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		SupplierClass _supplierClass = new SupplierClass();
		_supplierClass.setCompanyId(UserUtils.getCompanyId());
		_supplierClass.setName(supplierClass.getName());
		_supplierClass.setSort(supplierClass.getSort());
		_supplierClass.setMemo(supplierClass.getMemo());

		serviceFactory.getSupplierClassService().save(_supplierClass);
		UserUtils.clearCacheBasic(BasicType.SUPPLIERCLASS);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 供应商分类修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:05:18, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:supplierClass:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		SupplierClass supplierClass = serviceFactory.getSupplierClassService().get(id);
		map.put("supplierClass", supplierClass);
		return "basic/supplierClass/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改供应商分类
	 * </pre>
	 * @param supplierClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:05:29, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:supplierClass:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "供应商分类", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody SupplierClass supplierClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(supplierClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		SupplierClass obj = serviceFactory.getSupplierClassService().getByName(supplierClass.getName());
		SupplierClass _supplierClass = serviceFactory.getSupplierClassService().get(supplierClass.getId());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_supplierClass.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_supplierClass.setName(supplierClass.getName());
		_supplierClass.setSort(supplierClass.getSort());
		_supplierClass.setMemo(supplierClass.getMemo());
		serviceFactory.getSupplierClassService().update(_supplierClass);
		UserUtils.clearCacheBasic(BasicType.SUPPLIERCLASS);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除供应商分类
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:05:42, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:supplierClass:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "供应商分类", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.SUPPLIERCLASS, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
