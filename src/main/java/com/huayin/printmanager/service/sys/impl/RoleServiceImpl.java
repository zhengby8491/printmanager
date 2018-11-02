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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.exception.OperatorException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.Role;
import com.huayin.printmanager.persist.entity.sys.Role_Menu;
import com.huayin.printmanager.persist.entity.sys.User_Role;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.RoleService;
import com.huayin.printmanager.service.sys.vo.RoleVo;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 角色管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
@Lazy
public class RoleServiceImpl extends BaseServiceImpl implements RoleService
{
	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.RoleService#getByName(java.lang.String, java.lang.String)
	 */
	@Override
	public Role getByName(String name, String companyId)
	{
		DynamicQuery query = new DynamicQuery(Role.class);
		query.like("name", "%" + name + "%");
		query.eq("companyId", companyId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Role.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.RoleService#lock(java.lang.Long, java.lang.String)
	 */
	@Override
	public Role lock(Long id, String companyId)
	{
		DynamicQuery query = new DynamicQuery(Role.class);
		query.eq("id", id);
		query.eq("companyId", companyId);
		Role role = daoFactory.getCommonDao().lockByDynamicQuery(query, Role.class, LockType.LOCK_WAIT).get(0);
		return role;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.RoleService#findByUserId(java.lang.Long)
	 */
	@Override
	public List<Role> findByUserId(Long userId)
	{
		DynamicQuery query = new CompanyDynamicQuery(Role.class, "r");
		query.createAlias(User_Role.class, "ur");
		query.add(Restrictions.eqProperty("ur.roleId", "r.id"));
		query.eq("ur.userId", userId);
		query.addProjection(Projections.property("r"));
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Role.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.RoleService#findAll(java.lang.String)
	 */
	@Override
	public List<Role> findAll(String companyId)
	{
		DynamicQuery query = new DynamicQuery(Role.class);
		query.eq("companyId", companyId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Role.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.RoleService#findByCondition(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public SearchResult<RoleVo> findByCondition(String companyName, String companyId, String name, Integer pageIndex, Integer pageSize)
	{
		DynamicQuery query = new DynamicQuery(Role.class, "r");
		query.createAlias(Company.class, JoinType.LEFTJOIN, "c", "r.companyId=c.id");
		query.addProjection(Projections.property("r, c"));

		if (StringUtils.isNotEmpty(companyName))
		{
			query.like("c.name", "%" + companyName + "%");
		}
		if (StringUtils.isNotEmpty(companyId))
		{
			query.like("r.companyId", "%" + companyId + "%");
		}
		if (StringUtils.isNotEmpty(name))
		{
			query.like("r.name", "%" + name + "%");
		}
		// query.eq("companyId", UserUtils.getCompanyId());
		query.desc("r.companyId");
		query.setPageIndex(pageIndex);
		query.setPageSize(pageSize);
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<RoleVo> result = new SearchResult<RoleVo>();
		result.setResult(new ArrayList<RoleVo>());
		for (Object[] objs : temp_result.getResult())
		{
			Role r = (Role) objs[0];
			Company c = (Company) objs[1];

			RoleVo vo = new RoleVo();
			vo.setRole(r);
			vo.setCompany(c);
			result.getResult().add(vo);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.RoleService#save(com.huayin.printmanager.persist.entity.sys.Role)
	 */
	@Override
	@Transactional
	public void save(Role role) throws OperatorException
	{
		role.setCompanyId(UserUtils.getUser().getCompanyId());
		role.setCreateTime(new Date());
		role.setCreateName(UserUtils.getUserName());
		serviceFactory.getPersistService().save(role);
		// 更新所有角色权限
		serviceFactory.getMenuService().updateRoleMenu(role.getId(), role.getMenuIdList());
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.RoleService#update(com.huayin.printmanager.persist.entity.sys.Role)
	 */
	@Override
	@Transactional
	public void update(Role role) throws OperatorException
	{

		Role _role = this.lock(role.getId(), role.getCompanyId());
		_role.setName(role.getName());
		_role.setMemo(role.getMemo());
		daoFactory.getCommonDao().updateEntity(_role);
		// 取消所有权限
		// serviceFactory.getMenuService().clearAllRoleMenu(role.getId());
		// 更新所有角色权限
		serviceFactory.getMenuService().updateRoleMenu(role.getId(), role.getMenuIdList());
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.RoleService#updateUR(java.lang.Long, java.lang.Long[])
	 */
	@Override
	@Transactional
	public void updateUR(Long userId, Long[] roleIds)
	{
		if (roleIds != null && roleIds.length > 0)
		{
			DynamicQuery query = new CompanyDynamicQuery(User_Role.class);
			query.eq("userId", userId);
			List<User_Role> urList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, User_Role.class);
			List<Long> oldRoleIdArray = new ArrayList<Long>();
			List<Long> newRoleIdArray = Arrays.asList(roleIds);
			List<User_Role> delList = new ArrayList<User_Role>();
			List<User_Role> addList = new ArrayList<User_Role>();
			for (User_Role ur : urList)
			{
				if (!newRoleIdArray.contains(ur.getRoleId()))
				{
					delList.add(ur);
				}
				oldRoleIdArray.add(ur.getRoleId());
			}
			for (Long roleId : newRoleIdArray)
			{
				if (!oldRoleIdArray.contains(roleId))
				{
					User_Role newUR = new User_Role();
					newUR.setUserId(userId);
					newUR.setRoleId(roleId);
					newUR.setCompanyId(UserUtils.getCompanyId());
					addList.add(newUR);
				}
			}

			daoFactory.getCommonDao().deleteAllEntity(delList);
			daoFactory.getCommonDao().saveAllEntity(addList);
		}
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.RoleService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void delete(Long id)
	{
		Role _role = this.lock(id, UserUtils.getCompanyId());
		DynamicQuery urQuery = new CompanyDynamicQuery(User_Role.class);
		urQuery.eq("roleId", id);
		List<User_Role> urList = daoFactory.getCommonDao().findEntityByDynamicQuery(urQuery, User_Role.class);

		DynamicQuery rmQuery = new CompanyDynamicQuery(Role_Menu.class);
		rmQuery.eq("roleId", id);
		List<Role_Menu> rmList = daoFactory.getCommonDao().findEntityByDynamicQuery(rmQuery, Role_Menu.class);

		daoFactory.getCommonDao().deleteAllEntity(urList);
		daoFactory.getCommonDao().deleteAllEntity(rmList);
		daoFactory.getCommonDao().deleteEntity(_role);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.RoleService#count(java.lang.String)
	 */
	@Override
	public Integer count(String companyId)
	{
		DynamicQuery query = new DynamicQuery(Role.class);
		query.addProjection(Projections.count());
		query.eq("companyId", companyId);
		return daoFactory.getCommonDao().countByDynamicQuery(query, Role.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.RoleService#checkRoleCount(java.lang.String)
	 */
	@Override
	public Boolean checkRoleCount(String companyId)
	{
		Integer company_role_count_max = serviceFactory.getCompanyService().get(companyId).getRoleCountMax();
		Integer company_role_count_max_sys = Integer.parseInt(SystemConfigUtil.getConfig(SysConstants.COMPANY_ROLE_COUNT_MAX));
		Integer company_role_count_curr = serviceFactory.getRoleService().count(companyId);
		if (company_role_count_curr < (company_role_count_max != null ? company_role_count_max : company_role_count_max_sys))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
