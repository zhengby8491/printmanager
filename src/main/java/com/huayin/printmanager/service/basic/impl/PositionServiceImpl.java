/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月26日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Position;
import com.huayin.printmanager.service.basic.PositionService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 基础设置 - 职位设置
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月26日
 * @since        2.0, 2017年12月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class PositionServiceImpl extends BaseServiceImpl implements PositionService
{
	@Override
	public Position get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Position.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Position.class);
	}

	@Override
	public Position getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(Position.class);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Position.class);
	}

	@Override
	@Transactional
	public Position save(Position position)
	{
		return daoFactory.getCommonDao().saveEntity(position);
	}

	@Override
	@Transactional
	public Position update(Position position)
	{
		return daoFactory.getCommonDao().updateEntity(position);
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		daoFactory.getCommonDao().deleteByIds(Position.class, id);
	}

	@Override
	public List<Position> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(Position.class);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Position.class);
	}

	@Override
	public SearchResult<Position> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Position.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getPositionName()))
		{
			query.like("name", "%" + queryParam.getPositionName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Position.class);
	}
}
