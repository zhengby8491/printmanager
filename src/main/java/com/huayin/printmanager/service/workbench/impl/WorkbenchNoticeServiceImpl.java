/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.workbench.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.workbench.WorkbenchNotice;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.service.workbench.WorkbenchNoticeService;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统  - 公告
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年2月16日, raintear
 * @version 	   2.0, 2018年2月27日下午2:19:14, zhengby, 代码规范
 */
@Service
public class WorkbenchNoticeServiceImpl extends BaseServiceImpl implements WorkbenchNoticeService
{

	@Override
	public WorkbenchNotice get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkbenchNotice.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, WorkbenchNotice.class);
	}

	@Override
	public String getNotice()
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkbenchNotice.class);
		query.desc("createTime");
		List<WorkbenchNotice> list=daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkbenchNotice.class);
		if(list!=null&&list.size()>0){
			return list.get(0).getContent();
		}else{
			return "";
		}
		
	}
	
	@Override
	@Transactional
	public WorkbenchNotice save(String content)
	{
		try
		{
			WorkbenchNotice notice=new WorkbenchNotice();
			notice.setCompanyId(UserUtils.getCompanyId());
			notice.setCreateTime(new Date());
			notice.setCreateName(UserUtils.getUserName());
			notice.setContent(content);
			daoFactory.getCommonDao().saveEntity(notice);
			return notice;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	@Transactional
	public WorkbenchNotice update(WorkbenchNotice notice)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkbenchNotice.class);
		query.eq("id", notice.getId());
		WorkbenchNotice notice_=daoFactory.getCommonDao().getByDynamicQuery(query, WorkbenchNotice.class);
		notice_.setContent(notice.getContent());
		daoFactory.getCommonDao().updateEntity(notice_);
		return notice_;
	}

	@Override
	public SearchResult<WorkbenchNotice> findNotice(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkbenchNotice.class);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, WorkbenchNotice.class);
	}

	@Override
	@Transactional
	public Boolean del(Long id)
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(WorkbenchNotice.class);
			query.eq("id", id);
			WorkbenchNotice notice=daoFactory.getCommonDao().getByDynamicQuery(query, WorkbenchNotice.class);
			daoFactory.getCommonDao().deleteEntity(notice);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}


	
}
