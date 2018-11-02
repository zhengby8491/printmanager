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
import com.huayin.printmanager.persist.entity.basic.CustomerClass;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 客户分类
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月28日
 * @since        2.0, 2017年12月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/customerClass")
public class CustomerClassController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 客户分类列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:38:00, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:customerClass:list")
	public String list()
	{
		return "basic/customerClass/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:38:18, zhengby
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<CustomerClass> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getCustomerClassService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 客户分类新增
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:32:19, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:customerClass:create")
	public String create()
	{
		return "basic/customerClass/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增客户分类
	 * </pre>
	 * @param customerClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:32:45, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:customerClass:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "客户分类", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody CustomerClass customerClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(customerClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		CustomerClass obj = serviceFactory.getCustomerClassService().getByName(customerClass.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		CustomerClass _customerClass = new CustomerClass();
		_customerClass.setCompanyId(UserUtils.getCompanyId());
		_customerClass.setName(customerClass.getName());
		_customerClass.setSort(customerClass.getSort());
		_customerClass.setMemo(customerClass.getMemo());
		serviceFactory.getCustomerClassService().save(_customerClass);

		UserUtils.clearCacheBasic(BasicType.CUSTOMERCLASS);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 客户分类修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:33:15, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:customerClass:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		CustomerClass customerClass = serviceFactory.getCustomerClassService().get(id);
		map.put("customerClass", customerClass);
		return "basic/customerClass/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改客户分类
	 * </pre>
	 * @param customerClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:36:33, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:customerClass:edit")
	public AjaxResponseBody update(@RequestBody CustomerClass customerClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(customerClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		CustomerClass _customerClass = serviceFactory.getCustomerClassService().get(customerClass.getId());
		CustomerClass obj = serviceFactory.getCustomerClassService().getByName(customerClass.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_customerClass.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_customerClass.setName(customerClass.getName());
		_customerClass.setSort(customerClass.getSort());
		_customerClass.setMemo(customerClass.getMemo());
		serviceFactory.getCustomerClassService().update(_customerClass);

		UserUtils.clearCacheBasic(BasicType.CUSTOMERCLASS);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除客户分类
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年11月2日 下午4:37:41, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:customerClass:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "客户分类", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.CUSTOMERCLASS, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
