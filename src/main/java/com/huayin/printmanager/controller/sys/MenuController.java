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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.AdminAuth;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.utils.MenuUtils;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 系统模块 - 菜单管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/menu")
public class MenuController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 菜单列表
	 * </pre>
	 * @param isShowFunction 是否显示按钮菜单
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:23:03, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:menu:list")
	@AdminAuth
	public String list(Boolean isShowFunction, HttpServletRequest request, ModelMap map)
	{
		List<Menu> list = serviceFactory.getMenuService().findAll();

		List<Menu> newList = new ArrayList<Menu>();
		MenuUtils.sortList(newList, list, MenuUtils.TREE_ROOT_ID);
		// list=MenuUtils.buildtree(list, 0l);
		map.put("isShowFunction", isShowFunction == null ? false : isShowFunction);
		map.put("list", newList);
		return "sys/menu/list";
	}

	/**
	 * <pre>
	 * 页面 - 菜单新增
	 * </pre>
	 * @param map
	 * @param parentId
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:23:35, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("sys:menu:create")
	@AdminAuth
	public String create(ModelMap map, Long parentId)
	{
		Menu parent = new Menu();
		if (parentId != null)
		{

			parent = serviceFactory.getDaoFactory().getCommonDao().getEntity(Menu.class, parentId);
			int _newId = serviceFactory.getMenuService().getChildrenMaxId(parentId);
			if (_newId == 0)
			{
				_newId = Integer.parseInt(String.valueOf(parent.getId()) + "01");
			}
			else
			{
				_newId++;
			}
			map.put("newId", _newId);

			int _newSort = serviceFactory.getMenuService().getChildrenMaxSort(parentId);
			_newSort++;
			map.put("newSort", _newSort);
		}
		map.put("parent", parent);

		return "sys/menu/create";
	}

	/**
	 * <pre>
	 * 功能 - 菜单新增
	 * </pre>
	 * @param menu
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:23:55, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("sys:menu:create")
	@AdminAuth
	public AjaxResponseBody save(Menu menu, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(menu.getName(), menu.getType()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}

		if (menu.getParentId() == null)
		{// 上级菜单为空时，设置为根目录：0
			menu.setParentId(0l);
		}
		serviceFactory.getPersistService().save(menu);
		// UserUtils.clearPermission();
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 菜单修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:24:06, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("sys:menu:edit")
	@AdminAuth
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		Menu menu = serviceFactory.getDaoFactory().getCommonDao().getEntity(Menu.class, id);
		Menu menuParent = serviceFactory.getDaoFactory().getCommonDao().getEntity(Menu.class, menu.getParentId());
		menu.setParent(menuParent);
		map.put("menu", menu);
		return "sys/menu/edit";
	}

	/**
	 * <pre>
	 * 功能 - 菜单修改
	 * </pre>
	 * @param menu
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:24:21, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sys:menu:edit")
	@AdminAuth
	public AjaxResponseBody update(Menu menu, ModelMap map)
	{
		if (menu.getParentId() == null)
		{// 上级菜单为空时，设置为根目录：0
			menu.setParentId(0l);
		}
		Menu _menu = serviceFactory.getPersistService().lockObject(Menu.class, menu.getId());
		_menu.setName(menu.getName());
		_menu.setIcon(menu.getIcon());
		_menu.setIdentifier(menu.getIdentifier());
		_menu.setParentId(menu.getParentId());
		_menu.setSort(menu.getSort());
		_menu.setType(menu.getType());
		_menu.setUrl(menu.getUrl());
		_menu.setRefresh(menu.getRefresh());
		_menu.setIsBase(menu.getIsBase());
		serviceFactory.getPersistService().update(_menu);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 菜单删除
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:24:34, think
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("sys:menu:del")
	@AdminAuth
	public boolean delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			serviceFactory.getMenuService().delete(id);
			// UserUtils.clearPermission();
			return true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * <pre>
	 * Ajax列表 - 菜单列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:25:13, think
	 */
	@RequestMapping(value = "treeData")
	@ResponseBody
	@RequiresPermissions("sys:menu:list")
	@AdminAuth
	public List<Map<String, Object>> treeData()
	{
		List<Map<String, Object>> mapList = Lists.newArrayList();
		// 及时更新，不缓存
		List<Menu> list = serviceFactory.getMenuService().findAll();
		for (Menu m : list)
		{

			Map<String, Object> map = Maps.newHashMap();
			map.put("id", m.getId());
			map.put("pId", m.getParentId());
			map.put("name", StringUtils.replace(m.getName(), " ", ""));
			mapList.add(map);
		}
		return mapList;

	}
}
