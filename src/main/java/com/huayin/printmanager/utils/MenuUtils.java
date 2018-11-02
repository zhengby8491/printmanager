/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huayin.printmanager.persist.entity.sys.Menu;

/**
 * <pre>
 * 公共 - 菜单（业务）
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class MenuUtils
{
	public static final Long TREE_ROOT_ID = 0l;

	/**
	 * <pre>
	 * 将数据库菜单数组转化成树形结构数组
	 * </pre>
	 * @param nodes
	 * @param parentId
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:37:09, think
	 */
	public static List<Menu> buildTree(List<Menu> nodes, Long parentId)
	{
		List<Menu> treeNodes = new ArrayList<Menu>();
		for (Menu treeNode : nodes)
		{
			Menu node = new Menu();
			node.setId(treeNode.getId());
			node.setName(treeNode.getName());
			node.setUrl(treeNode.getUrl());
			node.setIcon(treeNode.getIcon());
			node.setType(treeNode.getType());
			node.setSort(treeNode.getSort());
			node.setIdentifier(treeNode.getIdentifier());
			node.setRefresh(treeNode.getRefresh());
			node.setIsBase(treeNode.getIsBase());
			// node.setParentId(treeNode.getParentId());
			if (parentId.equals(treeNode.getParentId()))
			{
				node.setChildrens(buildTree(nodes, node.getId()));
				treeNodes.add(node);
			}
		}
		return treeNodes;
	}

	/**
	 * <pre>
	 * 将树形结构数组转化成数据库菜单数组(进行排序)
	 * </pre>
	 * @param newMenu
	 * @param sourcelist
	 * @param rootId
	 * @since 1.0, 2017年10月26日 上午10:37:18, think
	 */
	@JsonIgnore
	public static void sortList(List<Menu> newMenu, List<Menu> sourcelist, Long rootId)
	{
		for (Menu m : sourcelist)
		{
			if (m.getParentId() != null && m.getParentId().equals(rootId))
			{
				newMenu.add(m);
				// 判断是否还有子节点, 有则继续获取子节点
				for (Menu child : sourcelist)
				{
					if (child.getParentId().equals(m.getId()))
					{
						sortList(newMenu, sourcelist, m.getId());
						break;
					}
				}
			}
		}
	}

	/**
	 * <pre>
	 * 包装父类（给菜单的父类赋值）
	 * </pre>
	 * @param list
	 * @since 1.0, 2017年10月26日 上午10:37:24, think
	 */
	public static void wrapParent(List<Menu> list)
	{
		// 通过两次循环，设置父类实体
		Map<Long, Menu> menuMap = new HashMap<Long, Menu>();
		for (Menu m : list)
		{
			menuMap.put(m.getId(), m);
		}
		for (Menu m : list)
		{
			if (m.getParentId() != null && menuMap.containsKey(m.getParentId()))
			{
				m.setParent(menuMap.get(m.getParentId()));
			}
		}
	}
}
