/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Machine;
import com.huayin.printmanager.service.basic.MachineService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 机台信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
@Service
public class MachineServiceImpl extends BaseServiceImpl implements MachineService
{
	@Override
	public Machine get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Machine.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Machine.class);
	}

	@Override
	public Machine get(String companyId, Long id)
	{
		DynamicQuery query = new DynamicQuery(Machine.class);
		query.eq("id", id);
		query.eq("companyId", companyId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Machine.class);
	}

	@Override
	public Machine getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(Machine.class);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Machine.class);
	}

	@Override
	@Transactional
	public Machine save(Machine machine)
	{
		machine.setCompanyId(UserUtils.getCompanyId());
		machine.setCreateName(UserUtils.getUserName());
		machine.setCreateTime(new Date());
		machine.setSort(machine.getColorQty());
		return daoFactory.getCommonDao().saveEntity(machine);
	}

	@Override
	@Transactional
	public Machine update(Machine machine)
	{
		machine.setUpdateTime(new Date());
		machine.setUpdateName(UserUtils.getUserName());
		machine.setSort(machine.getColorQty());
		return daoFactory.getCommonDao().updateEntity(machine);
	}

	@Override
	@Transactional
	public void deleteByIds(Long[] ids)
	{
		DynamicQuery query = new CompanyDynamicQuery(Machine.class);
		query.in("id", Arrays.asList(ids));
		List<Machine> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Machine.class);
		serviceFactory.getDaoFactory().getCommonDao().deleteAllEntity(list);
	}

	@Override
	public List<Machine> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(Machine.class);
		// query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Machine.class);
	}

	public Machine findMachineBycolorQty(Integer colorQty)
	{
		DynamicQuery query = new CompanyDynamicQuery(Machine.class);
		query.eq("colorQty", colorQty);
		List<Machine> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Machine.class);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	@Override
	public SearchResult<Machine> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Machine.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getMachineName()))
		{
			query.like("name", "%" + queryParam.getMachineName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		// query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Machine.class);
	}

	@Override
	public SearchResult<Machine> quickFindByCondition(QueryParam queryParam)
	{
		SearchResult<Machine> result = new SearchResult<Machine>();
		DynamicQuery query = new CompanyDynamicQuery(Machine.class);
		if (StringUtils.isNotEmpty(queryParam.getMachineName()))
		{
			query.like("name", "%" + queryParam.getMachineName() + "%");
		}
		query.asc("sort");
		query.desc("createTime");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Machine.class);
		return result;
	}

}
