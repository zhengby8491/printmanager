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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.SystemConfig;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.SystemConfigService;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 系统模块 - 系统参数
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class SystemConfigServiceImpl extends BaseServiceImpl implements SystemConfigService
{

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemConfigService#get(java.lang.String)
	 */
	@Override
	public SystemConfig get(String id)
	{
		DynamicQuery query = new DynamicQuery(SystemConfig.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SystemConfig.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemConfigService#findByCondition(java.lang.String, java.lang.Integer,
	 * java.lang.Integer)
	 */
	@Override
	public SearchResult<SystemConfig> findByCondition(String systemConfigId, Integer pageIndex, Integer pageSize)
	{
		DynamicQuery query = new DynamicQuery(SystemConfig.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(systemConfigId))
		{
			query.like("id", "%" + systemConfigId + "%");
		}
		query.asc("id");
		query.setPageIndex(pageIndex);
		query.setPageSize(pageSize);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SystemConfig.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemConfigService#save(com.huayin.printmanager.persist.entity.sys.
	 * SystemConfig)
	 */
	@Override
	@Transactional
	public SystemConfig save(SystemConfig systemConfig)
	{
		return daoFactory.getCommonDao().saveEntity(systemConfig);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemConfigService#update(com.huayin.printmanager.persist.entity.sys.
	 * SystemConfig)
	 */
	@Override
	@Transactional
	public SystemConfig update(SystemConfig systemConfig)
	{
		return daoFactory.getCommonDao().updateEntity(systemConfig);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemConfigService#delete(java.lang.String)
	 */
	@Override
	@Transactional
	public void delete(String id)
	{
		daoFactory.getCommonDao().deleteEntity(SystemConfig.class, id);
	}

}
