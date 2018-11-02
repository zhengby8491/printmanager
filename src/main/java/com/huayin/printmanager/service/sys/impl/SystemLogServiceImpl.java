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
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.util.DateTimeUtil;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.SystemLog;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.SystemLogService;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 系统日志
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class SystemLogServiceImpl extends BaseServiceImpl implements SystemLogService
{
	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemLogService#findByCondition(java.util.Date, java.util.Date,
	 * com.huayin.printmanager.persist.enumerate.SystemLogType, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public SearchResult<SystemLog> findByCondition(Date dateMin, Date dateMax, SystemLogType type, String companyName, String userName, String ip, Integer pageIndex, Integer pageSize)
	{
		DynamicQuery query = new DynamicQuery(SystemLog.class, "sl");
		query.createAlias(User.class, JoinType.LEFTJOIN, "u", "sl.userId=u.id");
		query.createAlias(Company.class, JoinType.LEFTJOIN, "c", "sl.companyId=c.id");
		query.addProjection(Projections.property("sl, u, c"));

		query.setIsSearchTotalCount(true);
		if (dateMin != null)
		{
			query.ge("sl.createTime", DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(dateMin) + " 00:00:01", DateTimeUtil.DATE_FORMAT_YMDHMS));
		}
		if (dateMax != null)
		{
			query.le("sl.createTime", DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(dateMax) + " 23:59:59", DateTimeUtil.DATE_FORMAT_YMDHMS));
		}
		if (type != null)
		{
			query.eq("sl.type", type);
		}
		if (StringUtils.isNotEmpty(companyName))
		{
			query.like("c.name", "%" + companyName + "%");
		}
		if (StringUtils.isNotEmpty(userName))
		{
			query.like("u.userName", "%" + userName + "%");
		}
		if (StringUtils.isNotEmpty(ip))
		{
			query.like("sl.operatorIp", "%" + ip + "%");
		}
		if (!UserUtils.isSystemCompany())
		{
			query.eq("sl.companyId", UserUtils.getCompanyId());
		}
		query.desc("sl.createTime");
		query.setPageIndex(pageIndex);
		query.setPageSize(pageSize);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<SystemLog> result = new SearchResult<SystemLog>();
		result.setResult(new ArrayList<SystemLog>());

		for (Object[] c : temp_result.getResult())
		{
			SystemLog systemLog = (SystemLog) c[0];
			systemLog.setUser((User) c[1]);
			systemLog.setCompany((Company) c[2]);
			result.getResult().add(systemLog);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.SystemLogService#addLog(com.huayin.printmanager.persist.entity.sys.SystemLog)
	 */
	@Override
	@Transactional
	public void addLog(SystemLog log)
	{
		daoFactory.getCommonDao().saveEntity(log);
	}

}
