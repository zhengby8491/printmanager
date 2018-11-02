/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月22日 上午9:30:23
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
import com.huayin.printmanager.persist.entity.basic.Department;
import com.huayin.printmanager.service.basic.DepartmentService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 基础设置 - 部门信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月22日
 * @since        2.0, 2017年12月22日 下午17:07:00, think, 规范和国际化
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl implements DepartmentService
{
	@Override
	public Department get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Department.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Department.class);
	}

	@Override
	public Department getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(Department.class);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Department.class);
	}

	@Override
	@Transactional
	public Department save(Department department)
	{
		return daoFactory.getCommonDao().saveEntity(department);
	}

	@Override
	@Transactional
	public Department update(Department department)
	{
		return daoFactory.getCommonDao().updateEntity(department);
	}

	@Override
	public List<Department> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(Department.class);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Department.class);
	}

	@Override
	public SearchResult<Department> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Department.class);
		if (StringUtils.isNotEmpty(queryParam.getDepartmentName()))
		{
			query.like("name", "%" + queryParam.getDepartmentName() + "%");
		}
		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Department.class);
	}
}
