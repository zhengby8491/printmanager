/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.printmanager.exception.OperatorException;
import com.huayin.printmanager.persist.entity.sys.Company_Menu;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.entity.sys.Product_Menu;
import com.huayin.printmanager.persist.entity.sys.Role;
import com.huayin.printmanager.persist.entity.sys.Role_Menu;
import com.huayin.printmanager.persist.entity.sys.User_Menu;
import com.huayin.printmanager.persist.entity.sys.User_Role;
import com.huayin.printmanager.persist.enumerate.PermissionType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.MenuService;
import com.huayin.printmanager.utils.MenuUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 菜单管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl implements MenuService
{
	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#getChildrenMaxId(java.lang.Long)
	 */
	@Override
	public int getChildrenMaxId(Long parentId)
	{

		DynamicQuery query = new DynamicQuery(Menu.class);
		query.eq("parentId", parentId);
		query.addProjection(Projections.max("id"));
		int maxId = daoFactory.getCommonDao().countByDynamicQuery(query, Menu.class);

		return maxId;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#getChildrenMaxSort(java.lang.Long)
	 */
	@Override
	public int getChildrenMaxSort(Long parentId)
	{
		DynamicQuery query = new DynamicQuery(Menu.class);
		query.eq("parentId", parentId);
		query.addProjection(Projections.max("sort"));
		int maxSort = daoFactory.getCommonDao().countByDynamicQuery(query, Menu.class);
		return maxSort;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#findByUserId(java.lang.Long)
	 */
	@Override
	public List<Menu> findByUserId(Long userId)
	{
		DynamicQuery query = new DynamicQuery(Menu.class, "m");
		query.createAlias(User_Menu.class, "um");
		query.add(Restrictions.eqProperty("um.menuId", "m.id"));
		query.eq("um.userId", userId);
		query.addProjection(Projections.property("m"));
		List<Menu> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Menu.class);

		MenuUtils.wrapParent(list);
		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#findByRoleId(java.lang.Long)
	 */
	@Override
	public List<Menu> findByRoleId(Long roleId)
	{
		DynamicQuery query = new DynamicQuery(Menu.class, "m");
		query.createAlias(Role_Menu.class, "rm");
		query.add(Restrictions.eqProperty("rm.menuId", "m.id"));
		query.eq("rm.roleId", roleId);
		query.addProjection(Projections.property("m"));
		List<Menu> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Menu.class);

		MenuUtils.wrapParent(list);
		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#findMenuIdByRoleId(java.lang.Long)
	 */
	public List<Role_Menu> findMenuIdByRoleId(Long roleId)
	{
		DynamicQuery query = new DynamicQuery(Role_Menu.class);
		query.eq("roleId", roleId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Role_Menu.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#findAll()
	 */
	@Override
	public List<Menu> findAll()
	{
		DynamicQuery query = new DynamicQuery(Menu.class);
		query.asc("sort");
		List<Menu> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Menu.class);

		MenuUtils.wrapParent(list);
		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#findAll(java.lang.String)
	 */
	@Override
	public List<Menu> findAll(String companyId)
	{
		DynamicQuery query = new DynamicQuery(Menu.class, "m");
		query.createAlias(Company_Menu.class, "cm");
		query.add(Restrictions.eqProperty("cm.menuId", "m.id"));
		query.eq("cm.companyId", companyId);
		query.asc("sort");
		query.addProjection(Projections.property("m"));
		List<Menu> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Menu.class);

		MenuUtils.wrapParent(list);
		return list;

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#findMenuByProductId(java.lang.Long)
	 */
	@Override
	public List<Product_Menu> findMenuByProductId(Long productId)
	{
		DynamicQuery query = new DynamicQuery(Product_Menu.class);
		query.eq("productId", productId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Product_Menu.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#findMenuByProduct()
	 */
	public List<Product_Menu> findMenuByProduct()
	{
		DynamicQuery query = new DynamicQuery(Product_Menu.class);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Product_Menu.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#findAllPermissionByCompanyId(java.lang.String)
	 */
	@Override
	public List<Menu> findAllPermissionByCompanyId(String companyId)
	{
		DynamicQuery query = new DynamicQuery(Menu.class, "m");
		query.createAlias(Company_Menu.class, "cm");
		query.add(Restrictions.eqProperty("cm.menuId", "m.id"));
		query.eq("cm.companyId", companyId);
		query.addProjection(Projections.property("m"));
		query.asc("m.sort");
		List<Menu> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Menu.class);

		MenuUtils.wrapParent(list);
		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#findAllNavigationMenu(java.lang.String)
	 */
	@Override
	public List<Menu> findAllNavigationMenu(String companyId)
	{
		if (StringUtils.isBlank(companyId))
		{
			return null;
		}
		DynamicQuery query = new DynamicQuery(Menu.class, "m");
		query.createAlias(Company_Menu.class, "cm");
		query.add(Restrictions.eqProperty("cm.menuId", "m.id"));
		query.eq("cm.companyId", companyId);
		query.eq("m.type", PermissionType.MENU);
		query.addProjection(Projections.property("m"));
		query.asc("m.sort");
		List<Menu> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Menu.class);

		list = MenuUtils.buildTree(list, MenuUtils.TREE_ROOT_ID);
		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#findAllNavigationMenu()
	 */
	@Override
	public List<Menu> findAllNavigationMenu()
	{
		DynamicQuery query = new DynamicQuery(Menu.class);
		query.eq("type", PermissionType.MENU);
		query.asc("sort");
		List<Menu> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Menu.class);

		list = MenuUtils.buildTree(list, MenuUtils.TREE_ROOT_ID);
		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#findAllPermissionByUserId(java.lang.Long)
	 */
	@Override
	public List<Menu> findAllPermissionByUserId(Long userId)
	{
		List<Menu> list = _findMenuByUserId(userId, null);
		MenuUtils.wrapParent(list);
		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#findNavigationMenuByUserId(java.lang.Long)
	 */
	@Override
	public List<Menu> findNavigationMenuByUserId(Long userId)
	{
		List<Menu> list = _findMenuByUserId(userId, PermissionType.MENU);

		list = MenuUtils.buildTree(list, MenuUtils.TREE_ROOT_ID);
		return list;
	}

	/**
	 * <pre>
	 * 获取用户权限
	 * SELECT DISTINCT m.id FROM sys_menu m INNER JOIN ( select DISTINCT mId from ( SELECT um.menuId as mId FROM
	 * sys_user_menu um WHERE um.userId = 1115 UNION SELECT rm.menuId as mId FROM sys_user_role ur, sys_role_menu rm
	 * WHERE ur.roleId = rm.roleId AND ur.userId = 1115 ) as um2rm ) B on m.id=B.id WHERE m.type = 'MENU'
	 * </pre>
	 * @param userId
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:21:13, think
	 */
	private List<Menu> _findMenuByUserId(Long userId, PermissionType type)
	{
		// 查找用户关联菜单
		DynamicQuery queryUM = new DynamicQuery(User_Menu.class, "um");
		queryUM.eq("um.userId", userId);
		queryUM.addProjection(Projections.property("um.menuId", "mId"));
		// 查找角色关联菜单
		DynamicQuery queryUR = new DynamicQuery(User_Role.class, "ur");
		queryUR.createAlias(Role_Menu.class, "rm");
		queryUR.add(Restrictions.eqProperty("ur.roleId", "rm.roleId"));
		queryUR.eq("ur.userId", userId);
		queryUR.addProjection(Projections.property("rm.menuId", "mId"));
		// 菜单ID合并
		queryUM.union(queryUR);

		DynamicQuery queryUM2RM = new DynamicQuery(queryUM, "um2rm");
		queryUM2RM.addProjection(Projections.property("distinct mId"));

		DynamicQuery query = new DynamicQuery(Menu.class, "m");
		query.createAlias(queryUM2RM, JoinType.INNERJOIN, "um2rm", "m.id=um2rm.mId");

		query.eq("m.type", type);
		// 过滤菜单ID

		query.asc("m.sort");

		query.addProjection(Projections.property("*"));
		query.setQueryType(QueryType.JDBC);
		List<Menu> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Menu.class);

		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#updateRoleMenu(java.lang.Long, java.util.List)
	 */
	@Override
	@Transactional
	public void updateRoleMenu(Long roleId, List<Long> menuIdList) throws OperatorException
	{
		if (!UserUtils.isSystemCompany())
		{// 非管理员公司必须校验公司权限
			if (!serviceFactory.getMenuService().checkMenu(UserUtils.getCompanyId(), menuIdList))
			{// 该公司菜单权限未通过校验
				throw new OperatorException("非法操作");
			}
		}
		List<Role_Menu> addMenuList = new ArrayList<Role_Menu>();// 需要新增的菜单
		List<Role_Menu> delMenuList = new ArrayList<Role_Menu>();// 需要删除的菜单
		DynamicQuery query = new DynamicQuery(Role_Menu.class, "rm");
		query.eq("roleId", roleId);
		List<Role_Menu> old_rmList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Role_Menu.class);
		Collection<Long> hasMenuIdArray = new ArrayList<Long>();

		// 提取需要删除的菜单ID
		for (Role_Menu rm : old_rmList)
		{
			if (!menuIdList.contains(rm.getMenuId()))
			{
				delMenuList.add(rm);
				continue;
			}
			hasMenuIdArray.add(rm.getMenuId());
		}
		// 提取需要新增的菜单
		for (Long menuId : menuIdList)
		{
			if (!hasMenuIdArray.contains(menuId))
			{
				Role_Menu new_rm = new Role_Menu();
				new_rm.setRoleId(roleId);
				new_rm.setMenuId(menuId);
				new_rm.setCompanyId(UserUtils.getCompanyId());
				addMenuList.add(new_rm);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(delMenuList);// 删除权限
		daoFactory.getCommonDao().saveAllEntity(addMenuList);// 新增权限
	}

	@Override
	@Transactional
	public void updateRoleMenuByNotify(Long roleId, List<Long> menuIdList, String companyId) throws OperatorException
	{
		List<Role_Menu> addMenuList = new ArrayList<Role_Menu>();// 需要新增的菜单
		List<Role_Menu> delMenuList = new ArrayList<Role_Menu>();// 需要删除的菜单
		DynamicQuery query = new DynamicQuery(Role_Menu.class, "rm");
		query.eq("roleId", roleId);
		List<Role_Menu> old_rmList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Role_Menu.class);
		Collection<Long> hasMenuIdArray = new ArrayList<Long>();

		// 提取需要删除的菜单ID
		for (Role_Menu rm : old_rmList)
		{
			if (!menuIdList.contains(rm.getMenuId()))
			{
				delMenuList.add(rm);
				continue;
			}
			hasMenuIdArray.add(rm.getMenuId());
		}
		// 提取需要新增的菜单
		for (Long menuId : menuIdList)
		{
			if (!hasMenuIdArray.contains(menuId))
			{
				Role_Menu new_rm = new Role_Menu();
				new_rm.setRoleId(roleId);
				new_rm.setMenuId(menuId);
				new_rm.setCompanyId(companyId);
				addMenuList.add(new_rm);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(delMenuList);// 删除权限
		daoFactory.getCommonDao().saveAllEntity(addMenuList);// 新增权限
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#updateCompanyMenu(java.lang.String, java.util.List)
	 */
	@Override
	@Transactional
	public void updateCompanyMenu(String companyId, List<Long> menuIdList) throws OperatorException
	{
		// 取消所有权限
		// serviceFactory.getMenuService().clearAllCompanyMenu(companyId);

		List<Company_Menu> addMenuList = new ArrayList<Company_Menu>();// 需要新增的菜单
		List<Company_Menu> delMenuList = new ArrayList<Company_Menu>();// 需要删除的菜单
		DynamicQuery query = new DynamicQuery(Company_Menu.class, "cm");
		query.eq("companyId", companyId);
		List<Company_Menu> old_cmList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Company_Menu.class);
		Collection<Long> hasMenuIdArray = new ArrayList<Long>();

		// 提取需要删除的菜单ID
		for (Company_Menu cm : old_cmList)
		{
			if (!menuIdList.contains(cm.getMenuId()))
			{
				delMenuList.add(cm);
				continue;
			}
			hasMenuIdArray.add(cm.getMenuId());
		}
		// 提取需要新增的菜单
		for (Long menuId : menuIdList)
		{
			if (!hasMenuIdArray.contains(menuId))
			{
				Company_Menu new_cm = new Company_Menu();
				new_cm.setCompanyId(companyId);
				new_cm.setMenuId(menuId);
				addMenuList.add(new_cm);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(delMenuList);// 删除权限
		daoFactory.getCommonDao().saveAllEntity(addMenuList);// 新增权限

		// 更新公司下所有角色权限
		// 更新管理员角色权限
		List<Role> roleList = serviceFactory.getRoleService().findAll(companyId);
		for (Role role : roleList)
		{
			if (role.getName().equals("管理员"))
			{
				serviceFactory.getMenuService().updateRoleMenu(role.getId(), menuIdList);
			}
			else
			{// 去除公司内的所有角色的多余权限
				List<Long> menuIds = new ArrayList<Long>();
				for (Company_Menu delMenu : delMenuList)
				{
					menuIds.add(delMenu.getId());
				}
				if (menuIds.size() > 0)
				{
					serviceFactory.getMenuService().delRoleMenu(companyId, role.getId(), menuIds);
				}
				// serviceFactory.getMenuService().updateRoleMenu(roleId, menuIdList);
			}
		}
		// 更新其它角色权限
		UserUtils.clearCachePermission(companyId);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#updateCompanyAdminMenu(java.lang.String, java.util.List)
	 */
	@Override
	@Transactional
	public void updateCompanyAdminMenu(String companyId, List<Long> menuIdList) throws OperatorException
	{
		// 取消所有权限
		// serviceFactory.getMenuService().clearAllCompanyMenu(companyId);

		List<Company_Menu> addMenuList = new ArrayList<Company_Menu>();// 需要新增的菜单
		List<Company_Menu> delMenuList = new ArrayList<Company_Menu>();// 需要删除的菜单
		DynamicQuery query = new DynamicQuery(Company_Menu.class, "cm");
		query.eq("companyId", companyId);
		List<Company_Menu> old_cmList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Company_Menu.class);
		Collection<Long> hasMenuIdArray = new ArrayList<Long>();

		// 提取需要删除的菜单ID
		for (Company_Menu cm : old_cmList)
		{
			if (!menuIdList.contains(cm.getMenuId()))
			{
				delMenuList.add(cm);
				continue;
			}
			hasMenuIdArray.add(cm.getMenuId());
		}
		// 提取需要新增的菜单
		for (Long menuId : menuIdList)
		{
			if (!hasMenuIdArray.contains(menuId))
			{
				Company_Menu new_cm = new Company_Menu();
				new_cm.setCompanyId(companyId);
				new_cm.setMenuId(menuId);
				addMenuList.add(new_cm);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(delMenuList);// 删除权限
		daoFactory.getCommonDao().saveAllEntity(addMenuList);// 新增权限

		// 更新公司下所有角色权限
		// 更新管理员角色权限
		List<Role> roleList = serviceFactory.getRoleService().findAll(companyId);
		for (Role role : roleList)
		{
			if (role.getName().equals("管理员"))
			{
				serviceFactory.getMenuService().updateRoleMenuByNotify(role.getId(), menuIdList, companyId);
			}
			else
			{// 去除公司内的所有角色的多余权限
				List<Long> menuIds = new ArrayList<Long>();
				for (Company_Menu delMenu : delMenuList)
				{
					menuIds.add(delMenu.getId());
				}
				if (menuIds.size() > 0)
				{
					serviceFactory.getMenuService().delRoleMenu(companyId, role.getId(), menuIds);
				}
				// serviceFactory.getMenuService().updateRoleMenu(roleId, menuIdList);
			}
		}

		// 更新其它角色权限
		UserUtils.clearCachePermission(companyId);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void delete(Long id)
	{
		// 删除Company_Menu、User_Menu、Role_Menu
		DynamicQuery cmQuery = new DynamicQuery(Company_Menu.class);
		cmQuery.eq("menuId", id);
		List<Company_Menu> cmList = daoFactory.getCommonDao().findEntityByDynamicQuery(cmQuery, Company_Menu.class);
		daoFactory.getCommonDao().deleteAllEntity(cmList);

		DynamicQuery umQuery = new DynamicQuery(User_Menu.class);
		umQuery.eq("menuId", id);
		List<User_Menu> umList = daoFactory.getCommonDao().findEntityByDynamicQuery(umQuery, User_Menu.class);
		daoFactory.getCommonDao().deleteAllEntity(umList);

		DynamicQuery rmQuery = new DynamicQuery(Role_Menu.class);
		rmQuery.eq("menuId", id);
		List<Role_Menu> rmList = daoFactory.getCommonDao().findEntityByDynamicQuery(rmQuery, Role_Menu.class);
		daoFactory.getCommonDao().deleteAllEntity(rmList);

		daoFactory.getCommonDao().deleteEntity(Menu.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#delRoleMenu(java.lang.String, java.lang.Long, java.util.List)
	 */
	@Override
	@Transactional
	public void delRoleMenu(String companyId, Long roleId, List<Long> menuIds)
	{
		/*
		 * DynamicQuery query = new DynamicQuery(Role_Menu.class, "rm"); query.in("menuId", Arrays.asList(menuIds));
		 * List<Role_Menu> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Role_Menu.class);
		 * if(list!=null&&list.size()>0) { daoFactory.getCommonDao().deleteAllEntity(list); }
		 */

		daoFactory.getCommonDao().execNamedQuery("del.role_menu", companyId, roleId, menuIds);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#checkMenu(java.lang.String, java.util.List)
	 */
	@Override
	public boolean checkMenu(String companyId, List<Long> menuIdList)
	{
		DynamicQuery query = new DynamicQuery(Company_Menu.class);
		query.addProjection(Projections.property("menuId"));
		query.eq("companyId", companyId);
		query.setQueryType(QueryType.JDBC);
		List<?> company_menuId_list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, null);
		/*
		 * List<Long> company_menuId_list=new ArrayList<Long>(); for(Company_Menu cm:company_menu_list) {
		 * company_menuId_list.add(cm.getMenuId()); }
		 */
		List<BigInteger> new_menuIdList = new ArrayList<BigInteger>();
		for (Long menuId : menuIdList)
		{
			new_menuIdList.add(BigInteger.valueOf(menuId));
		}
		// new_menuIdList.add(BigInteger.valueOf(1004));
		if (company_menuId_list.containsAll(new_menuIdList))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#appendCompanyMenu(java.lang.String, java.util.List)
	 */
	@Override
	@Transactional
	public void appendCompanyMenu(String companyId, List<Long> menuIdList) throws OperatorException
	{
		List<Company_Menu> addMenuList = new ArrayList<Company_Menu>();// 需要新增的菜单
		DynamicQuery query = new DynamicQuery(Company_Menu.class, "cm");
		query.eq("companyId", companyId);
		List<Company_Menu> old_cmList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Company_Menu.class);
		Collection<Long> hasMenuIdArray = new ArrayList<Long>();
		Collection<Long> old_cmIdList = new ArrayList<Long>();

		for (Company_Menu cm : old_cmList)
		{
			old_cmIdList.add(cm.getMenuId());
		}

		// 过滤已经存在的菜单ID
		for (long menuId : menuIdList)
		{
			if (old_cmIdList.contains(menuId))
			{
				continue;
			}
			hasMenuIdArray.add(menuId);
		}

		// 提取需要新增的菜单
		for (Long menuId : hasMenuIdArray)
		{
			Company_Menu new_cm = new Company_Menu();
			new_cm.setCompanyId(companyId);
			new_cm.setMenuId(menuId);
			addMenuList.add(new_cm);
		}

		if (addMenuList.size() > 0)
		{
			daoFactory.getCommonDao().saveAllEntity(addMenuList);// 新增权限
		}

		// 更新公司下所有角色权限
		List<Role> roleList = serviceFactory.getRoleService().findAll(companyId);
		for (Role role : roleList)
		{
			// 管理员（TODO 这里要考虑国际化）
			if("管理员".equals(role.getName()))
			{
				serviceFactory.getMenuService().appendRoleMenu(role.getId(), menuIdList);
			}
		}
		// 更新其它角色权限
		UserUtils.clearCachePermission(companyId);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#appendRoleMenu(java.lang.Long, java.util.List)
	 */
	@Override
	@Transactional
	public void appendRoleMenu(Long roleId, List<Long> menuIdList) throws OperatorException
	{
		if (!UserUtils.isSystemCompany())
		{// 非管理员公司必须校验公司权限
			if (!serviceFactory.getMenuService().checkMenu(UserUtils.getCompanyId(), menuIdList))
			{// 该公司菜单权限未通过校验
				throw new OperatorException("非法操作");
			}
		}
		List<Role_Menu> addMenuList = new ArrayList<Role_Menu>();// 需要新增的菜单
		DynamicQuery query = new DynamicQuery(Role_Menu.class, "rm");
		query.eq("roleId", roleId);
		List<Role_Menu> old_rmList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Role_Menu.class);
		Collection<Long> hasMenuIdArray = new ArrayList<Long>();
		Collection<Long> old_rmIdList = new ArrayList<Long>();

		for (Role_Menu cm : old_rmList)
		{
			old_rmIdList.add(cm.getMenuId());
		}

		// 过滤已经存在的菜单ID
		for (long menuId : menuIdList)
		{
			if (old_rmIdList.contains(menuId))
			{
				continue;
			}
			hasMenuIdArray.add(menuId);
		}
		// 提取需要新增的菜单
		for (Long menuId : hasMenuIdArray)
		{
			Role_Menu new_rm = new Role_Menu();
			new_rm.setRoleId(roleId);
			new_rm.setMenuId(menuId);
			new_rm.setCompanyId(UserUtils.getCompanyId());
			addMenuList.add(new_rm);
		}

		if (addMenuList.size() > 0)
		{
			daoFactory.getCommonDao().saveAllEntity(addMenuList);// 新增权限
		}
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#clearAllRoleMenu(java.lang.Long)
	 */
	@Override
	@Transactional
	public void clearAllRoleMenu(Long roleId)
	{
		serviceFactory.getDaoFactory().getCommonDao().execNamedQuery("menu.clearRoleMenu", roleId);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.MenuService#clearAllCompanyMenu(java.lang.String)
	 */
	@Override
	@Transactional
	public void clearAllCompanyMenu(String companyId)
	{
		serviceFactory.getDaoFactory().getCommonDao().execNamedQuery("menu.clearCompanyMenu", companyId);
	}
}
