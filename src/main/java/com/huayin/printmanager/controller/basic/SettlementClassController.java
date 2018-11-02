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
import com.huayin.printmanager.persist.entity.basic.SettlementClass;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础模块 - 结算方式
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月27日
 * @since        2.0, 2017年12月27日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/settlementClass")
public class SettlementClassController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 结算方式列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月27日 下午2:11:45, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:settlementClass:list")
	public String list()
	{
		return "basic/settlementClass/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月27日 下午2:12:28, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<SettlementClass> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getSettlementClassService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 结算方式新增
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月27日 下午2:12:53, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:settlementClass:create")
	public String create()
	{
		return "basic/settlementClass/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增结算方式
	 * </pre>
	 * @param settlementClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月27日 下午2:12:50, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:settlementClass:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "结算方式", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody SettlementClass settlementClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(settlementClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		SettlementClass obj = serviceFactory.getSettlementClassService().getByName(settlementClass.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		SettlementClass _settlementClass_ = new SettlementClass();
		_settlementClass_.setCompanyId(UserUtils.getCompanyId());
		_settlementClass_.setName(settlementClass.getName());
		_settlementClass_.setSort(settlementClass.getSort());
		_settlementClass_.setMemo(settlementClass.getMemo());

		SettlementClass _settlementClassNew = serviceFactory.getSettlementClassService().save(_settlementClass_);
		UserUtils.clearCacheBasic(BasicType.SETTLEMENTCLASS);
		return returnSuccessBody(_settlementClassNew);
	}

	/**
	 * <pre>
	 * 页面 - 结算方式修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @throws Exception
	 * @since 1.0, 2017年12月27日 下午2:13:57, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:settlementClass:edit")
	public String edit(@PathVariable Long id, ModelMap map) throws Exception
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		SettlementClass settlementClass = serviceFactory.getSettlementClassService().get(id);
		map.put("settlementClass", settlementClass);
		return "basic/settlementClass/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改结算方式
	 * </pre>
	 * @param settlementClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月27日 下午2:14:10, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:settlementClass:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "结算方式", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody SettlementClass settlementClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(settlementClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		SettlementClass _settlementClass_ = serviceFactory.getSettlementClassService().get(settlementClass.getId());
		SettlementClass obj = serviceFactory.getSettlementClassService().getByName(settlementClass.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_settlementClass_.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_settlementClass_.setName(settlementClass.getName());
		_settlementClass_.setSort(settlementClass.getSort());
		_settlementClass_.setMemo(settlementClass.getMemo());

		serviceFactory.getSettlementClassService().update(_settlementClass_);
		UserUtils.clearCacheBasic(BasicType.SETTLEMENTCLASS);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除结算方式
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月27日 下午2:14:23, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:settlementClass:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "结算方式", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.SETTLEMENTCLASS, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
