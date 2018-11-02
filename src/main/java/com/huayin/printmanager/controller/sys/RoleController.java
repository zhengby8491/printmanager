/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.huayin.printmanager.controller.vo.DeleteVo;
import com.huayin.printmanager.domain.annotation.AdminAuth;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.exception.OperatorException;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.entity.sys.Role;
import com.huayin.printmanager.plugin.PageSupport;
import com.huayin.printmanager.service.sys.vo.RoleVo;
import com.huayin.printmanager.utils.MenuUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 角色管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/role")
public class RoleController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 角色列表
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:52:52, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:role:list")
	public String list(ModelMap map)
	{
		List<Role> list = serviceFactory.getRoleService().findAll(UserUtils.getCompanyId());
		map.put("list", list);
		return "sys/role/list";
	}

	/**
	 * <pre>
	 * 页面 - 所有公司角色列表
	 * </pre>
	 * @param companyName
	 * @param companyId
	 * @param name
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:53:17, think
	 */
	@RequestMapping(value = "allList")
	@RequiresPermissions("sys:role:all_list")
	@AdminAuth
	public String allList(String companyName, String companyId, String name, HttpServletRequest request, ModelMap map)
	{
		PageSupport<RoleVo> page = new PageSupport<RoleVo>(request);
		SearchResult<RoleVo> result = serviceFactory.getRoleService().findByCondition(companyName, companyId, name, page.getPageNo(), page.getPageSize());
		page.setSearchResult(result);
		map.put("page", page);
		map.put("companyName", companyName);
		map.put("companyId", companyId);
		map.put("name", name);
		return "sys/role/list_all";
	}

	/**
	 * <pre>
	 * 页面 - 角色新增
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:53:42, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("sys:role:create")
	public String create(ModelMap map)
	{
		if (serviceFactory.getRoleService().checkRoleCount(UserUtils.getCompanyId()))
		{
			List<Menu> allMenuList = UserUtils.getMenuListByCompanyId(UserUtils.getCompanyId());
			allMenuList = MenuUtils.buildTree(allMenuList, MenuUtils.TREE_ROOT_ID);
			map.put("allMenuList", allMenuList);
			return "sys/role/create";
		}
		else
		{
			return returnErrorPage(map, "最大角色数量受限");
		}

	}

	/**
	 * <pre>
	 * 功能 - 角色新增
	 * </pre>
	 * @param role
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:53:58, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("sys:role:create")
	public AjaxResponseBody save(@RequestBody Role role, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(role, role.getName()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		if (!serviceFactory.getRoleService().checkRoleCount(UserUtils.getCompanyId()))
		{
			return returnErrorBody("最大角色数量受限");
		}
		try
		{
			Role old_role = serviceFactory.getRoleService().getByName(role.getName(), UserUtils.getCompanyId());
			if (old_role != null)
			{
				return returnErrorBody("角色名已存在");
			}

			serviceFactory.getRoleService().save(role);
			return returnSuccessBody();
		}
		catch (OperatorException ex)
		{
			ex.printStackTrace();
			return returnErrorBody(ex);
		}
	}

	/**
	 * <pre>
	 * 页面 - 角色修改
	 * </pre>
	 * @param id
	 * @param roleType
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:54:18, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("sys:role:edit")
	public String edit(@PathVariable Long id, @RequestParam(name = "roleType", required = false) String roleType, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		Role role = serviceFactory.getDaoFactory().getCommonDao().getEntity(Role.class, id);
		String role_companyId = role.getCompanyId();
		if (UserUtils.isSystemCompany() || role_companyId.equals(UserUtils.getCompanyId()))
		{
			List<Menu> allMenuList = UserUtils.getMenuListByCompanyId(role_companyId);
			allMenuList = MenuUtils.buildTree(allMenuList, MenuUtils.TREE_ROOT_ID);
			Map<Long, Long> hasMenuIdMap = new HashMap<Long, Long>();
			List<Menu> hasMenuList = serviceFactory.getMenuService().findByRoleId(id);
			for (Menu m : hasMenuList)
			{
				hasMenuIdMap.put(m.getId(), m.getId());
			}
			map.put("allMenuList", allMenuList);
			map.put("hasMenuIdMap", hasMenuIdMap);
			map.put("role", role);
			map.put("roleType", roleType);
			return "sys/role/edit";
		}
		else
		{// 只有系统公司才能编辑其他公司的角色权限
			return returnErrorPage(map, "权限非法！");
		}
	}

	/**
	 * <pre>
	 * 功能 - 角色修改
	 * </pre>
	 * @param role
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:54:50, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sys:role:edit")
	public AjaxResponseBody update(@RequestBody Role role, ModelMap map)
	{
		try
		{
			Role db_role = serviceFactory.getDaoFactory().getRoleDao().get(role.getId());
			String role_companyId = db_role.getCompanyId();
			if (UserUtils.isSystemCompany() || role_companyId.equals(UserUtils.getCompanyId()))
			{
				Role old_role = serviceFactory.getRoleService().getByName(role.getName(), role_companyId);
				if (old_role != null && old_role.getId().longValue() != role.getId().longValue())
				{
					return returnErrorBody("角色名已存在");
				}
				role.setCompanyId(role_companyId);
				serviceFactory.getRoleService().update(role);
				return returnSuccessBody();
			}
			else
			{
				return returnErrorBody("权限非法！");
			}

		}
		catch (OperatorException ex)
		{
			ex.printStackTrace();
			return returnErrorBody(ex);
		}
	}

	/**
	 * <pre>
	 * 功能 - 角色删除
	 * </pre>
	 * @param delVo
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:55:05, think
	 */
	@RequestMapping(value = "del")
	@ResponseBody
	@RequiresPermissions("sys:role:del")
	public AjaxResponseBody delete(@RequestBody DeleteVo delVo, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(delVo, delVo.getId()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		try
		{
			Role db_role = serviceFactory.getDaoFactory().getRoleDao().get(Long.parseLong(delVo.getId().toString()));
			if (!db_role.getCompanyId().equals(UserUtils.getCompanyId()))
			{
				return returnErrorBody("权限非法！");
			}
			serviceFactory.getRoleService().delete(Long.parseLong(delVo.getId().toString()));
			return returnSuccessBody();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return returnErrorBody("操作异常！");
		}
	}
}
