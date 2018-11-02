/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ObjectHelper;
import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.entity.basic.Department;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.DataShare;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.entity.sys.UserShare;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.State;
import com.huayin.printmanager.persist.enumerate.UserShareType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.utils.ServletUtils;
import com.huayin.printmanager.utils.UserUtils;
import com.huayin.printmanager.wx.service.WXBasicService;
import com.huayin.printmanager.wx.vo.UserVo;

/**
 * <pre>
 * 微信 - 基础信息
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class WXBasicServiceImpl extends BaseServiceImpl implements WXBasicService
{

	@Override
	public Long[] findSharedEmployeeIds(Long userId, String companyId)
	{
		try
		{
			User user = daoFactory.getCommonDao().getEntity(User.class, userId);
			List<Long> employeeIds = new ArrayList<Long>();
			DynamicQuery query = new DynamicQuery(DataShare.class);
			query.eq("sharedUserId", userId);
			query.eq("companyId", companyId);
			List<DataShare> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, DataShare.class);
			for (DataShare d : list)
			{
				if (employeeIds.contains(d.getEmployeeId()))
				{
					continue;
				}
				employeeIds.add(d.getEmployeeId());
			}
			if (employeeIds.size() > 0 && !employeeIds.contains(user.getEmployeeId()))
			{
				// 添加自己
				if (user.getEmployeeId() != null)
				{
					employeeIds.add(user.getEmployeeId());
				}
			}
			int size = employeeIds.size();
			return (Long[]) employeeIds.toArray(new Long[size]);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public UserShare getUserShareByOpenId(String openid)
	{
		if (openid == null)
		{
			return null;
		}
		DynamicQuery query = new DynamicQuery(UserShare.class);
		query.eq("identifier", openid);
		UserShare userShare = daoFactory.getCommonDao().getByDynamicQuery(query, UserShare.class);
		return userShare;
	}

	@Override
	public User getUser(String userName, String passWord)
	{
		DynamicQuery query = new DynamicQuery(User.class);
		query.eq("state", State.NORMAL);
		query.add(Restrictions.or(Restrictions.eq("userName", userName), Restrictions.eq("mobile", userName)));
		User user = daoFactory.getCommonDao().getByDynamicQuery(query, User.class);
		if (user == null)
		{
			return null;
		}
		if (UserUtils.validatePassword(passWord, user.getPassword()))
		{
			return user;
		}
		return null;
	}

	@Override
	public UserShare getUserShare(Long userId)
	{
		DynamicQuery query = new DynamicQuery(UserShare.class);
		query.eq("userId", userId);
		query.eq("userType", UserShareType.WEIXIN);
		return daoFactory.getCommonDao().getByDynamicQuery(query, UserShare.class);
	}

	@Override
	@Transactional
	public Boolean bind(User user, String openid)
	{
		try
		{
			UserShare userShare = new UserShare();
			userShare.setUserId(user.getId());
			userShare.setCompanyId(user.getCompanyId());
			userShare.setIdentifier(openid);
			userShare.setUserType(UserShareType.WEIXIN);
			daoFactory.getCommonDao().saveEntity(userShare);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Boolean delBind(UserShare userShare)
	{
		try
		{
			daoFactory.getCommonDao().deleteEntity(userShare);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public UserVo getUser(Long userId, String companyId)
	{
		UserVo result=new UserVo();
		DynamicQuery query = new DynamicQuery(User.class, "a");
		query.createAlias(Company.class, JoinType.LEFTJOIN, "b", "b.id=a.companyId");
		query.createAlias(Employee.class, JoinType.LEFTJOIN, "c", "c.id=a.employeeId");
		query.createAlias(Department.class, JoinType.LEFTJOIN, "d", "d.id=c.departmentId");
		query.addProjection(Projections.property(
				"b.name as companyName,ifnull(a.realName,a.userName) as userName,d.name as name,b.linkName as linkName,b.tel as mobile,b.email as email,d.name as departmentName"));
		query.eq("a.id", userId);
		query.eq("a.companyId", companyId);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query,
				HashMap.class);
		for (Map<String, Object> map : mapResult.getResult())
		{
			try
			{
				result = ObjectHelper.mapToObject(map, UserVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public User getUser(Long userId)
	{
		return daoFactory.getCommonDao().getEntity(User.class, userId);
	}

	@Override
	public List<BaseBasicTableEntity> getBasicInfoList(BasicType type)
	{
		DynamicQuery query = new DynamicQuery(type.getCla());
		query.asc("sort");
		query.eq("companyId", ServletUtils.getRequest().getSession().getAttribute("companyId").toString());
		return serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query,
				BaseBasicTableEntity.class);
	}
	
}
