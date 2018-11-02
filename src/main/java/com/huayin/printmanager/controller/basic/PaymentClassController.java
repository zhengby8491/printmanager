/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月27日 上午9:30:23
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
import com.huayin.printmanager.persist.entity.basic.PaymentClass;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础模块 - 付款方式
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月21日 上午9:37:56
 * @since        2.0, 2017年12月27日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/paymentClass")
public class PaymentClassController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 付款方式列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:40:48, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:paymentClass:list")
	public String list()
	{
		return "basic/paymentClass/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:41:13, zhengby
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<PaymentClass> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getPaymentClassService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面- 跳转到新增付款方式
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:38:23, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:paymentClass:create")
	public String create()
	{
		return "basic/paymentClass/create";
	}
	
	/**
	 * <pre>
	 * 页面- 快捷创建跳转到新增付款方式
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:38:23, zhengby
	 */
	@RequestMapping(value = "createShotCut")
	@RequiresPermissions("basic:paymentClass:create")
	public String createShotCut()
	{
		return "basic/paymentClass/createShotCut";
	}


	/**
	 * <pre>
	 * 功能 - 新增付款方式
	 * </pre>
	 * @param paymentClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:38:45, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:paymentClass:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "付款方式", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody PaymentClass paymentClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(paymentClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		PaymentClass obj = serviceFactory.getPaymentClassService().getByName(paymentClass.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		PaymentClass _paymentClass = new PaymentClass();
		_paymentClass.setCompanyId(UserUtils.getCompanyId());
		_paymentClass.setName(paymentClass.getName());
		_paymentClass.setType(paymentClass.getType());
		_paymentClass.setDayNum(paymentClass.getDayNum());
		_paymentClass.setSort(paymentClass.getSort());
		_paymentClass.setMemo(paymentClass.getMemo());
		PaymentClass _paymentClassNew = serviceFactory.getPaymentClassService().save(_paymentClass);

		UserUtils.clearCacheBasic(BasicType.PAYMENTCLASS);
		return returnSuccessBody(_paymentClassNew);
	}

	/**
	 * <pre>
	 * 页面 - 跳转到修改付款方式
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:39:03, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:paymentClass:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		PaymentClass paymentClass = serviceFactory.getPaymentClassService().get(id);
		map.put("paymentClass", paymentClass);
		return "basic/paymentClass/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改付款方式
	 * </pre>
	 * @param paymentClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:40:03, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:paymentClass:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "付款方式", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody PaymentClass paymentClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(paymentClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		PaymentClass _paymentClass = serviceFactory.getPaymentClassService().get(paymentClass.getId());
		PaymentClass obj = serviceFactory.getPaymentClassService().getByName(paymentClass.getName());

		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_paymentClass.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_paymentClass.setName(paymentClass.getName());
		_paymentClass.setType(paymentClass.getType());
		_paymentClass.setDayNum(paymentClass.getDayNum());
		_paymentClass.setSort(paymentClass.getSort());
		_paymentClass.setMemo(paymentClass.getMemo());
		serviceFactory.getPaymentClassService().update(_paymentClass);

		UserUtils.clearCacheBasic(BasicType.PAYMENTCLASS);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除付款方式
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:40:31, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:paymentClass:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "付款方式", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.PAYMENTCLASS, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
