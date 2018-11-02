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
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.UpdateNameType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 客户信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月29日
 * @since        2.0, 2017年12月29日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/customer")
public class CustomerController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 客户信息列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:00:46, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:customer:list")
	public String list()
	{
		return "basic/customer/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:01:03, zhengby
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<Customer> ajaxList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getCustomerService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 创建客户
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月20日 下午5:04:24, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:customer:create")
	public String create(ModelMap map)
	{
		map.put("code", serviceFactory.getCommonService().getNextCode(BasicType.CUSTOMER));
		return "basic/customer/create";
	}

	/**
	 * <pre>
	 * 功能 - 复制客户信息
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月20日 下午5:05:19, zhengby
	 */
	@RequestMapping(value = "copyCreate/{id}")
	@RequiresPermissions("basic:customer:copyCreate")
	public String copyCreate(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Customer customer = serviceFactory.getCustomerService().get(id);
		map.put("customer", customer);
		map.put("code", serviceFactory.getCommonService().getNextCode(BasicType.CUSTOMER));
		return "basic/customer/copyCreate";
	}

	/**
	 * <pre>
	 * 功能 - 保存客户信息
	 * </pre>
	 * @param customer
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月20日 下午7:36:16, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:customer:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "客户信息", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody Customer customer, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(customer.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Customer _customer = serviceFactory.getCustomerService().getByName(customer.getName());
		if (!Validate.validateObjectsNullOrEmpty(_customer))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		Customer _newCustomer = serviceFactory.getCustomerService().save(customer);
		UserUtils.clearCacheBasic(BasicType.CUSTOMER);
		// 只返回id
		Customer _id = new Customer();
		_id.setId(_newCustomer.getId());
		return returnSuccessBody(_id);
	}

	/**
	 * <pre>
	 * 页面 - 编辑客户信息
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午8:58:46, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:customer:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Customer customer = serviceFactory.getCustomerService().get(id);
		map.put("customer", customer);
		return "basic/customer/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改客户信息
	 * </pre>
	 * @param customer
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午8:59:16, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:customer:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "客户信息", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody Customer customer, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(customer.getId(), customer.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Customer _customer = serviceFactory.getCustomerService().get(customer.getId());
		Customer obj = serviceFactory.getCustomerService().getByName(customer.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_customer.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_customer.setName(customer.getName());
		_customer.setCustomerClassId(customer.getCustomerClassId());
		_customer.setDeliveryClassId(customer.getDeliveryClassId());
		_customer.setSettlementClassId(customer.getSettlementClassId());
		_customer.setCurrencyType(customer.getCurrencyType());
		_customer.setPaymentClassId(customer.getPaymentClassId());
		_customer.setTaxRateId(customer.getTaxRateId());
		_customer.setEmployeeId(customer.getEmployeeId());
		_customer.setIsValid(customer.getIsValid());
		_customer.setUpdateName(UserUtils.getUser().getUserName());
		_customer.setUpdateTime(new Date());
		_customer.setMemo(customer.getMemo());
		_customer.setAddressList(customer.getAddressList());
		_customer.setPayerList(customer.getPayerList());
		serviceFactory.getCustomerService().update(_customer);
		UserUtils.clearCacheBasic(BasicType.CUSTOMER);
		return returnSuccessBody();
	}
	
	/**
	 * <pre>
	 * 功能 - 更新代工平台
	 * </pre>
	 * @param customer
	 * @param map
	 * @return
	 * @since 1.0, 2018年4月2日 上午11:09:41, think
	 */
	@RequestMapping(value = "updateOem")
	@ResponseBody
	@RequiresPermissions("basic:customer:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "客户信息", Operation = Operation.UPDATE)
	public AjaxResponseBody updateOem(@RequestBody Customer customer, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(customer.getId(), customer.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Customer _customer = serviceFactory.getCustomerService().get(customer.getId());
		Customer obj = serviceFactory.getCustomerService().getByName(customer.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_customer.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_customer.setOriginCompanyId(customer.getOriginCompanyId());
		serviceFactory.getCustomerService().update(_customer);
		UserUtils.clearCacheBasic(BasicType.CUSTOMER);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除客户信息 
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午8:59:47, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:customer:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "客户信息", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.CUSTOMER, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <pre>
	 * 功能 - 批量删除客户信息
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:00:24, zhengby
	 */
	@RequestMapping(value = "batchDelete")
	@ResponseBody
	@RequiresPermissions("basic:customer:batchDelete")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "客户信息", Operation = Operation.DELETE)
	public AjaxResponseBody batchDelete(@RequestParam("ids[]") Long[] ids)
	{
		if (ids.length == 0)
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Customer customer = new Customer();
		for (Long id : ids)
		{
			if (serviceFactory.getCommonService().isUsed(BasicType.CUSTOMER, id))
			{
				customer = serviceFactory.getCustomerService().get(id);
				return returnErrorBody(ResourceBundleMessageSource.i18nFormatter(BasicI18nResource.CUSTOMER_VALIDATE_MSG1, customer.getName()));
			}
		}
		serviceFactory.getCustomerService().deleteByIds(ids);
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
	 * @since 1.0, 2018年2月7日 上午9:56:24, zhengby
	 */
	@RequestMapping(value = "updateName/{id}")
	@RequiresPermissions("basic:customer:updateName")
	public String updateName(@PathVariable Long id, ModelMap map)
	{
		Customer customer = serviceFactory.getCustomerService().get(id);
		map.put("id", id);
		map.put("name", customer.getName());
		map.put("code", customer.getCode());
		return "basic/customer/updateName";
	}
	
	/**
	 * <pre>
	 * 功能 - 全局变更客户名称
	 * </pre>
	 * @param id
	 * @param name
	 * @return
	 * @since 1.0, 2018年2月6日 下午5:02:04, zhengby
	 */
	@RequestMapping(value = "updateName")
	@ResponseBody
	@RequiresPermissions("basic:customer:updateName")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "客户信息", Operation = Operation.UPDATE)
	public AjaxResponseBody updateName(@RequestBody Customer customer)
	{
		if (serviceFactory.getCommonService().isExist(customer.getName(), UpdateNameType.CUSTOMER))
		{
			return returnErrorBody("已存在该名称");
		}else
		{
			serviceFactory.getCommonService().updateBasicName(customer.getId(), customer.getName(), UpdateNameType.CUSTOMER);
		}
		return returnSuccessBody();
	}
}
