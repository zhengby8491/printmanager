/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月3日 上午9:30:23
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
import com.huayin.printmanager.persist.entity.basic.DeliveryClass;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 送货方式
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月3日
 * @since        2.0, 2018年1月3日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/deliveryClass")
public class DeliveryClassController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 送货方式列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:05:43, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:deliveryClass:list")
	public String list()
	{
		return "basic/deliveryClass/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:06:03, zhengby
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<DeliveryClass> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getDeliveryClassService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 跳转到新增送货方式
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:02:33, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:deliveryClass:create")
	public String create()
	{
		return "basic/deliveryClass/create";
	}

	/**
	 * <pre>
	 * 页面 - 快捷创建跳转到新增送货方式
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:02:33, zhengby
	 */
	@RequestMapping(value = "createShotCut")
	@RequiresPermissions("basic:deliveryClass:create")
	public String createShotCut()
	{
		return "basic/deliveryClass/createShotCut";
	}
	/**
	 * <pre>
	 * 功能 - 新增送货方式
	 * </pre>
	 * @param deliveryClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:02:56, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:deliveryClass:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "送货方式", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody DeliveryClass deliveryClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(deliveryClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		DeliveryClass obj = serviceFactory.getDeliveryClassService().getByName(deliveryClass.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		DeliveryClass _deliveryClass = new DeliveryClass();
		_deliveryClass.setCompanyId(UserUtils.getCompanyId());
		_deliveryClass.setName(deliveryClass.getName());
		_deliveryClass.setSort(deliveryClass.getSort());
		_deliveryClass.setMemo(deliveryClass.getMemo());
		DeliveryClass	_deliveryClassNew = serviceFactory.getDeliveryClassService().save(_deliveryClass);
		UserUtils.clearCacheBasic(BasicType.DELIVERYCLASS);
		return returnSuccessBody(_deliveryClassNew);
	}

	/**
	 * <pre>
	 * 页面 - 跳转到修改送货方式
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:04:25, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:deliveryClass:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		DeliveryClass deliveryClass = serviceFactory.getDeliveryClassService().get(id);
		map.put("deliveryClass", deliveryClass);
		return "basic/deliveryClass/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改送货方式
	 * </pre>
	 * @param deliveryClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:04:53, zhengby
	 */
	@RequestMapping(value = "update")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "送货方式", Operation = Operation.UPDATE)
	@ResponseBody
	@RequiresPermissions("basic:deliveryClass:edit")
	public AjaxResponseBody update(@RequestBody DeliveryClass deliveryClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(deliveryClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		DeliveryClass _deliveryClass = serviceFactory.getDeliveryClassService().get(deliveryClass.getId());
		DeliveryClass obj = serviceFactory.getDeliveryClassService().getByName(deliveryClass.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_deliveryClass.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_deliveryClass.setName(deliveryClass.getName());
		_deliveryClass.setSort(deliveryClass.getSort());
		_deliveryClass.setMemo(deliveryClass.getMemo());
		serviceFactory.getDeliveryClassService().update(_deliveryClass);
		UserUtils.clearCacheBasic(BasicType.DELIVERYCLASS);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除送货方式
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:05:23, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:deliveryClass:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "送货方式", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.DELIVERYCLASS, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
