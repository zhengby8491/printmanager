/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
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
import com.huayin.printmanager.persist.entity.basic.Machine;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 机台信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/machine")
public class MachineController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 机台信息列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:24:40, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:machine:list")
	public String list()
	{
		return "basic/machine/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:24:57, zhengby
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<Machine> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getMachineService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 跳转到新增机台信息
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:20:39, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:machine:create")
	public String create(ModelMap map)
	{
		return "basic/machine/create";
	}

	/**
	 * <pre>
	 * 页面 - 跳转到复制新增机台信息
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:21:12, zhengby
	 */
	@RequestMapping(value = "copyCreate/{id}")
	@RequiresPermissions("basic:machine:copyCreate")
	public String copyCreate(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Machine machine = serviceFactory.getMachineService().get(id);
		map.put("machine", machine);
		return "basic/machine/copyCreate";
	}

	/**
	 * <pre>
	 * 功能 - 新增机台信息
	 * </pre>
	 * @param machine
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:22:04, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:machine:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "机台信息", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody Machine machine, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(machine.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Machine obj = serviceFactory.getMachineService().getByName(machine.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		serviceFactory.getMachineService().save(machine);

		UserUtils.clearCacheBasic(BasicType.MACHINE);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 跳转到修改机台信息
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:23:15, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:machine:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Machine machine = serviceFactory.getMachineService().get(id);
		map.put("machine", machine);
		return "basic/machine/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改机台信息
	 * </pre>
	 * @param machine
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:23:36, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:machine:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "机台信息", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody Machine machine, ModelMap map)
	{
		Machine machine_ = serviceFactory.getMachineService().get(machine.getId());
		machine_.setName(machine.getName());
		machine_.setCapacity(machine.getCapacity());
		machine_.setCode(machine.getCode());
		machine_.setColorQty(machine.getColorQty());
		machine_.setMachineType(machine.getMachineType());
		machine_.setManufacturer(machine.getManufacturer());
		machine_.setMaxStyle(machine.getMaxStyle());
		machine_.setMinStyle(machine.getMinStyle());
		machine_.setMoney(machine.getMoney());
		machine_.setMemo(machine.getMemo());
		serviceFactory.getMachineService().update(machine_);
		UserUtils.clearCacheBasic(BasicType.MACHINE);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除机台信息
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:24:06, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:machine:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "机台信息", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.MACHINE, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <pre>
	 * 功能 - 批量删除机台信息
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:24:21, zhengby
	 */
	@RequestMapping(value = "batchDelete")
	@ResponseBody
	@RequiresPermissions("basic:machine:batchDelete")
	public AjaxResponseBody batchDelete(@RequestParam("ids[]") Long[] ids)
	{
		if (ids.length == 0)
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Machine machine = new Machine();
		for (Long id : ids)
		{
			if (serviceFactory.getCommonService().isUsed(BasicType.MACHINE, id))
			{
				machine = serviceFactory.getMachineService().get(id);
				return returnErrorBody(ResourceBundleMessageSource.i18nFormatter(BasicI18nResource.MACHINE_VALIDATE_MSG1, machine.getName()));
			}
		}
		serviceFactory.getMachineService().deleteByIds(ids);
		return returnSuccessBody();
	}
}
