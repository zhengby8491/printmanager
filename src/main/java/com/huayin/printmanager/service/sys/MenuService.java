/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys;

import java.util.List;

import com.huayin.printmanager.exception.OperatorException;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.entity.sys.Product_Menu;
import com.huayin.printmanager.persist.entity.sys.Role_Menu;

/**
 * <pre>
 * 系统模块 - 菜单管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface MenuService
{
	/**
	 * <pre>
	 * 根据父ID，获取子菜单的最大id值 
	 * </pre>
	 * @param parentId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:09:07, think
	 */
	public int getChildrenMaxId(Long parentId);

	/**
	 * <pre>
	 * 根据父ID，获取子菜单的最大sort值 
	 * </pre>
	 * @param parentId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:09:14, think
	 */
	public int getChildrenMaxSort(Long parentId);

	/**
	 * <pre>
	 * 通过用户ID查找对应菜单权限（此权限不包括角色权限）
	 * </pre>
	 * @param userId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:09:20, think
	 */
	public List<Menu> findByUserId(Long userId);

	/**
	 * <pre>
	 * 通过公司ID查找对应权限
	 * </pre>
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:09:29, think
	 */
	public List<Menu> findAllPermissionByCompanyId(String companyId);

	/**
	 * <pre>
	 * 通过角色查找对应的菜单权限
	 * </pre>
	 * @param roleId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:09:35, think
	 */
	public List<Menu> findByRoleId(Long roleId);

	/**
	 * <pre>
	 * 通过角色查找对应的角色菜单列表
	 * </pre>
	 * @param roleId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:09:43, think
	 */
	public List<Role_Menu> findMenuIdByRoleId(Long roleId);

	/**
	 * <pre>
	 * 获取公司下的所有菜单
	 * </pre>
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:09:50, think
	 */
	public List<Menu> findAll(String companyId);

	/**
	 * <pre>
	 * 获取销售产品对应的所有菜单
	 * </pre>
	 * @param productId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:14:05, think
	 */
	public List<Product_Menu> findMenuByProductId(Long productId);

	/**
	 * <pre>
	 * 获取所有模块产品对应的所有菜单
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:14:11, think
	 */
	List<Product_Menu> findMenuByProduct();

	/**
	 * <pre>
	 * 获取所有权限
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:14:17, think
	 */
	public List<Menu> findAll();

	/**
	 * <pre>
	 * 获取公司的导航菜单
	 * </pre>
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:14:24, think
	 */
	public List<Menu> findAllNavigationMenu(String companyId);

	/**
	 * <pre>
	 * 获取所有导航菜单
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:14:29, think
	 */
	public List<Menu> findAllNavigationMenu();

	/**
	 * <pre>
	 * 获取用户权限（包括功能）
	 * </pre>
	 * @param userId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:14:37, think
	 */
	public List<Menu> findAllPermissionByUserId(Long userId);

	/**
	 * <pre>
	 * 获取用户导航菜单
	 * </pre>
	 * @param userId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:14:45, think
	 */
	public List<Menu> findNavigationMenuByUserId(Long userId);

	/**
	 * <pre>
	 * 更新角色对应的菜单权限
	 * </pre>
	 * @param roleId
	 * @param menuIdList
	 * @throws OperatorException
	 * @since 1.0, 2017年10月25日 下午5:14:52, think
	 */
	public void updateRoleMenu(Long roleId, List<Long> menuIdList) throws OperatorException;

	/**
	 * <pre>
	 * 更新角色对应的菜单权限
	 * </pre>
	 * @param roleId
	 * @param menuIdList
	 * @param companyId
	 * @throws OperatorException
	 * @since 1.0, 2017年10月25日 下午5:14:58, think
	 */
	public void updateRoleMenuByNotify(Long roleId, List<Long> menuIdList, String companyId) throws OperatorException;

	/**
	 * <pre>
	 * 更新公司对应的菜单权限
	 * </pre>
	 * @param companyId
	 * @param menuIdList
	 * @throws OperatorException
	 * @since 1.0, 2017年10月25日 下午5:15:05, think
	 */
	public void updateCompanyMenu(String companyId, List<Long> menuIdList) throws OperatorException;

	/**
	 * <pre>
	 * 更新公司管理员权限
	 * </pre>
	 * @param companyId
	 * @param menuIdList
	 * @throws OperatorException
	 * @since 1.0, 2017年10月25日 下午5:15:11, think
	 */
	public void updateCompanyAdminMenu(String companyId, List<Long> menuIdList) throws OperatorException;

	/**
	 * <pre>
	 * 删除菜单
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年10月25日 下午5:15:18, think
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 根据权限ID删除角色权限
	 * </pre>
	 * @param companyId
	 * @param roleId
	 * @param menuIds
	 * @since 1.0, 2017年10月25日 下午5:15:25, think
	 */
	public void delRoleMenu(String companyId, Long roleId, List<Long> menuIds);

	/**
	 * <pre>
	 * 校验权限是否存在自己公司
	 * </pre>
	 * @param companyId
	 * @param menuIdList
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:15:31, think
	 */
	public boolean checkMenu(String companyId, List<Long> menuIdList);

	/**
	 * <pre>
	 * 批量追加公司菜单权限
	 * </pre>
	 * @param companyId
	 * @param menuIdList
	 * @throws OperatorException
	 * @since 1.0, 2017年10月25日 下午5:15:38, think
	 */
	public void appendCompanyMenu(String companyId, List<Long> menuIdList) throws OperatorException;

	/**
	 * <pre>
	 * 批量追加角色菜单权限
	 * </pre>
	 * @param roleId
	 * @param menuIdList
	 * @throws OperatorException
	 * @since 1.0, 2017年10月25日 下午5:16:01, think
	 */
	public void appendRoleMenu(Long roleId, List<Long> menuIdList) throws OperatorException;

	/**
	 * <pre>
	 * 清空所有权限
	 * </pre>
	 * @param roleId
	 * @since 1.0, 2017年10月25日 下午5:17:06, think
	 */
	public void clearAllRoleMenu(Long roleId);

	/**
	 * <pre>
	 * 清空所有公司权限
	 * </pre>
	 * @param companyId
	 * @since 1.0, 2017年10月25日 下午5:17:12, think
	 */
	public void clearAllCompanyMenu(String companyId);
}
