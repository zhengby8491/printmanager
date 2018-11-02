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

import org.springframework.stereotype.Service;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.wx.service.WXEmployeeSerice;
import com.huayin.printmanager.wx.vo.QueryParam;

/**
 * <pre>
 * 微信 - 员工信息
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class WXEmployeeServiceImpl extends BaseServiceImpl implements WXEmployeeSerice
{
	@Override
	public SearchResult<Employee> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(Employee.class, "e");
		query.addProjection(Projections.property("e"));

		query.eq("e.companyId", queryParam.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());

		query.setIsSearchTotalCount(true);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Employee.class);
	}

}
