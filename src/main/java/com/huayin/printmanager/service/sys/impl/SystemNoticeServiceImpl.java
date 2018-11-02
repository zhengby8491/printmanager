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

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.SystemNotice;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.SystemNoticeService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 系统公告
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class SystemNoticeServiceImpl extends BaseServiceImpl implements SystemNoticeService
{

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemNoticeService#get(java.lang.Long)
	 */
	@Override
	public SystemNotice get(Long id)
	{
		DynamicQuery query = new DynamicQuery(SystemNotice.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SystemNotice.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemNoticeService#findByCondition(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<SystemNotice> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(SystemNotice.class);
		query.setIsSearchTotalCount(true);

		if (queryParam.getDateMin() != null)
		{
			query.ge("noticeTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("noticeTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getNoticeTitle()))
		{
			query.like("title", "%" + queryParam.getNoticeTitle() + "%");
		}
		if (queryParam.getPublish() != null)
		{
			query.eq("publish", queryParam.getPublish());
		}
		query.desc("createTime");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SystemNotice.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemNoticeService#findAllPublish(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<SystemNotice> findAllPublish(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(SystemNotice.class);
		query.setIsSearchTotalCount(true);
		query.eq("publish", BoolValue.YES);
		query.desc("noticeTime");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SystemNotice.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemNoticeService#findLastPublish()
	 */
	@Override
	public SystemNotice findLastPublish()
	{
		DynamicQuery query = new DynamicQuery(SystemNotice.class);
		query.le("noticeTime", new Date());
		query.desc("createTime");
		List<SystemNotice> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, SystemNotice.class);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemNoticeService#save(com.huayin.printmanager.persist.entity.sys.
	 * SystemNotice)
	 */
	@Override
	@Transactional
	public SystemNotice save(SystemNotice systemNotice)
	{
		systemNotice.setCreateTime(new Date());
		systemNotice.setUserName(UserUtils.getUserName());
		if (systemNotice.getNoticeTime() == null)
		{
			systemNotice.setNoticeTime(new Date());
		}
		if (systemNotice.getPublish() == null)
		{
			systemNotice.setPublish(BoolValue.YES);
		}
		systemNotice = daoFactory.getCommonDao().saveEntity(systemNotice);
		return systemNotice;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemNoticeService#update(com.huayin.printmanager.persist.entity.sys.
	 * SystemNotice)
	 */
	@Override
	@Transactional
	public SystemNotice update(SystemNotice systemNotice)
	{
		return daoFactory.getCommonDao().updateEntity(systemNotice);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemNoticeService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void delete(Long id)
	{
		daoFactory.getCommonDao().deleteByIds(SystemNotice.class, id);
	}

}
