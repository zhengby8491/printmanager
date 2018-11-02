/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月22日 上午9:30:23
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
import com.huayin.printmanager.persist.entity.basic.Department;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 部门信息
 * </pre>
 * @author       zhengby
 * @since        1.0, 2017年12月22日
 * @since        2.0, 2017年12月22日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/department")
public class DepartmentController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 部门信息列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:10:19, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:department:list")
	public String list()
	{
		return "basic/department/list";
	}

	/**
	 * <pre>
	 * 功能 - 部门信息列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:10:57, zhengby
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<Department> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getDepartmentService().findByCondition(queryParam);
	}
	
	/**
	 * <pre>
	 * 页面 - 部门信息新增
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:07:52, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:department:create")
	public String createView()
	{
		return "basic/department/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增部门信息
	 * </pre>
	 * @param department
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:08:26,  zhengby
	 * @since 2.0, 2017年12月22日 下午17:07:00, think, 规范和国际化
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:department:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "部门信息", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody Department department, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(department.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Department obj = serviceFactory.getDepartmentService().getByName(department.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		Department _department = new Department();
		_department.setCompanyId(UserUtils.getCompanyId());
		_department.setName(department.getName());
		_department.setSort(department.getSort());
		_department.setMemo(department.getMemo());
		serviceFactory.getDepartmentService().save(_department);

		UserUtils.clearCacheBasic(BasicType.DEPARTMENT);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 部门信息修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:08:58, zhengby
	 * @since 2.0, 2017年12月22日 下午17:07:00, think, 规范和国际化
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:department:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Department department = serviceFactory.getDepartmentService().get(id);
		map.put("department", department);
		return "basic/department/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改部门信息
	 * </pre>
	 * @param department
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月22日 下午5:21:27, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:department:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "部门信息", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody Department department, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(department.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Department _department = serviceFactory.getDepartmentService().get(department.getId());
		Department obj = serviceFactory.getDepartmentService().getByName(department.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_department.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_department.setName(department.getName());
		_department.setSort(department.getSort());
		_department.setMemo(department.getMemo());
		serviceFactory.getDepartmentService().update(_department);

		UserUtils.clearCacheBasic(BasicType.DEPARTMENT);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除部门信息
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:09:53, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:department:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "部门信息", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.DEPARTMENT, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
