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
import com.huayin.printmanager.persist.entity.sys.SystemVersionNotice;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.SystemVersionNoticeService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 版本公告
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class SystemVersionNoticeServiceImpl extends BaseServiceImpl implements SystemVersionNoticeService
{

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemVersionNoticeService#get(java.lang.Long)
	 */
	@Override
	public SystemVersionNotice get(Long id)
	{
		DynamicQuery query = new DynamicQuery(SystemVersionNotice.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SystemVersionNotice.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.SystemVersionNoticeService#findByCondition(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<SystemVersionNotice> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(SystemVersionNotice.class);
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SystemVersionNotice.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.SystemVersionNoticeService#findAllPublish(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<SystemVersionNotice> findAllPublish(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(SystemVersionNotice.class);
		query.setIsSearchTotalCount(true);
		query.eq("publish", BoolValue.YES);
		query.desc("noticeTime");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SystemVersionNotice.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemVersionNoticeService#findLastPublish()
	 */
	@Override
	public SystemVersionNotice findLastPublish()
	{
		DynamicQuery query = new DynamicQuery(SystemVersionNotice.class);
		query.le("noticeTime", new Date());
		query.desc("createTime");
		List<SystemVersionNotice> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, SystemVersionNotice.class);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.SystemVersionNoticeService#save(com.huayin.printmanager.persist.entity.sys.
	 * SystemVersionNotice)
	 */
	@Override
	@Transactional
	public SystemVersionNotice save(SystemVersionNotice systemVersionNotice)
	{
		systemVersionNotice.setCreateTime(new Date());
		systemVersionNotice.setUserName(UserUtils.getUserName());
		if (systemVersionNotice.getNoticeTime() == null)
		{
			systemVersionNotice.setNoticeTime(new Date());
		}
		if (systemVersionNotice.getPublish() == null)
		{
			systemVersionNotice.setPublish(BoolValue.YES);
		}
		systemVersionNotice = daoFactory.getCommonDao().saveEntity(systemVersionNotice);

		if (systemVersionNotice.getPublish() == BoolValue.YES)
		{
			_updataAllUserVersionNotify();
		}

		return systemVersionNotice;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.SystemVersionNoticeService#update(com.huayin.printmanager.persist.entity.sys.
	 * SystemVersionNotice)
	 */
	@Override
	@Transactional
	public SystemVersionNotice update(SystemVersionNotice SystemVersionNotice)
	{
		SystemVersionNotice systemVersionNotice = daoFactory.getCommonDao().updateEntity(SystemVersionNotice);

		if (systemVersionNotice.getPublish() == BoolValue.YES)
		{
			_updataAllUserVersionNotify();
		}
		return systemVersionNotice;
	}

	/**
	 * <pre>
	 * 异步更新所有用户versionNotify状态
	 * </pre>
	 * @since 1.0, 2017年10月25日 下午6:01:54, think
	 */
	@Transactional
	private void _updataAllUserVersionNotify()
	{
		daoFactory.getCommonDao().execNamedQuery("user.updateVersionNotify");
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SystemVersionNoticeService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void delete(Long id)
	{
		daoFactory.getCommonDao().deleteByIds(SystemVersionNotice.class, id);
	}
}
