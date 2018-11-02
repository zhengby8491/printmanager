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

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.exception.OperatorException;
import com.huayin.printmanager.persist.entity.sys.Role;
import com.huayin.printmanager.service.sys.vo.RoleVo;

/**
 * <pre>
 * 系统模块 - 角色管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface RoleService
{
	/**
	 * <pre>
	 * 通过角色名获取所拥有的角色
	 * </pre>
	 * @param name
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:25:39, think
	 */
	public Role getByName(String name, String companyId);

	/**
	 * <pre>
	 * 根据id和公司id获取角色
	 * </pre>
	 * @param id
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:25:46, think
	 */
	public Role lock(Long id, String companyId);

	/**
	 * <pre>
	 * 通过用户ID获取所拥有的角色
	 * </pre>
	 * @param userId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:26:02, think
	 */
	public List<Role> findByUserId(Long userId);

	/**
	 * <pre>
	 * 获取所有角色
	 * </pre>
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:26:20, think
	 */
	public List<Role> findAll(String companyId);

	/**
	 * <pre>
	 * 多条件查询角色信息
	 * </pre>
	 * @param companyName
	 * @param companyId
	 * @param name
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:26:34, think
	 */
	public SearchResult<RoleVo> findByCondition(String companyName, String companyId, String name, Integer pageIndex, Integer pageSize);

	/**
	 * <pre>
	 * 新增角色
	 * </pre>
	 * @param role
	 * @throws OperatorException
	 * @since 1.0, 2017年10月25日 下午5:27:32, think
	 */
	public void save(Role role) throws OperatorException;

	/**
	 * <pre>
	 * 修改角色
	 * </pre>
	 * @param role
	 * @throws OperatorException
	 * @since 1.0, 2017年10月25日 下午5:28:22, think
	 */
	public void update(Role role) throws OperatorException;

	/**
	 * <pre>
	 * 更新用户所属角色
	 * </pre>
	 * @param userId
	 * @param roleIds
	 * @since 1.0, 2017年10月25日 下午5:28:30, think
	 */
	public void updateUR(Long userId, Long[] roleIds);

	/**
	 * <pre>
	 * 删除角色，需要删除 角色权限(Role_menu)，用户角色(User_Role)
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年10月25日 下午5:28:36, think
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 获取公司角色数量
	 * </pre>
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:28:44, think
	 */
	public Integer count(String companyId);

	/**
	 * <pre>
	 * 检查是否超出最大角色数量
	 * </pre>
	 * @param companyId
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:28:52, think
	 */
	public Boolean checkRoleCount(String companyId);

}
