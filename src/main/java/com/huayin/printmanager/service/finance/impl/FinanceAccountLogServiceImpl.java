/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.finance.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.DateTimeUtil;
import com.huayin.common.util.ObjectHelper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Account;
import com.huayin.printmanager.persist.entity.basic.AccountLog;
import com.huayin.printmanager.persist.entity.begin.AccountBeginDetail;
import com.huayin.printmanager.service.finance.FinanceAccountLogService;
import com.huayin.printmanager.service.finance.vo.FinanceAccountLogSumVo;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 财务管理 - 资金帐户流水信息
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class FinanceAccountLogServiceImpl extends BaseServiceImpl implements FinanceAccountLogService
{
	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.finance.FinanceAccountLogService#findByCondition(com.huayin.printmanager.service.vo
	 * .QueryParam)
	 */
	@Override
	public SearchResult<AccountLog> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(AccountLog.class, "al");
		query.addProjection(Projections.property("al, a"));
		query.createAlias(Account.class, "a");
		query.eqProperty("al.accountId", "a.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("al.transTime", DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(queryParam.getDateMin()) + " 00:00:01", DateTimeUtil.DATE_FORMAT_YMDHMS));
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("al.transTime", DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(queryParam.getDateMax()) + " 23:59:59", DateTimeUtil.DATE_FORMAT_YMDHMS));
		}
		if (queryParam.getTransType() != null)
		{
			query.eq("al.transType", queryParam.getTransType());
		}
		if (queryParam.getTradeMode() != null)
		{
			query.eq("al.tradeMode", queryParam.getTradeMode());
		}
		if (StringUtils.isNotEmpty(queryParam.getBankNo()))
		{
			query.like("a.bankNo", "%" + queryParam.getBankNo() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		// query.eq("al.companyId", UserUtils.getCompanyId());
		query.desc("al.transTime");
		query.setIsSearchTotalCount(true);

		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<AccountLog> result = new SearchResult<AccountLog>();
		result.setResult(new ArrayList<AccountLog>());
		for (Object[] c : temp_result.getResult())
		{
			AccountLog accountLog = (AccountLog) c[0];
			accountLog.setAccount((Account) c[1]);
			result.getResult().add(accountLog);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.finance.FinanceAccountLogService#findAccountLogSum(com.huayin.printmanager.service.
	 * vo.QueryParam)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<FinanceAccountLogSumVo> findAccountLogSum(QueryParam queryParam)
	{
		SearchResult<FinanceAccountLogSumVo> result = new SearchResult<FinanceAccountLogSumVo>();
		DynamicQuery query = new CompanyDynamicQuery(AccountLog.class, "a");
		query.addProjection(Projections.property("ac.bankNo as bankNo,IFNULL(SUM(CASE when a.tradeMode='ADD' THEN a.transMoney END),0) as inTransMoney," + "IFNULL(SUM(CASE when a.tradeMode='SUBTRACT' THEN a.transMoney end),0)  as outTransMoney," + "IFNULL(ab.beginMoney,0) as beginMoney"));
		query.createAlias(AccountBeginDetail.class, JoinType.LEFTJOIN, "ab", "ab.accountId=a.accountId");
		query.createAlias(Account.class, JoinType.LEFTJOIN, "ac", "a.accountId=ac.id");
		if (StringUtils.isNotBlank(queryParam.getBankNo()))
		{
			query.eq("ac.bankNo", queryParam.getBankNo());
		}
		if (queryParam.getDateMin() != null && !"".equals(queryParam.getDateMin()))
		{
			query.ge("a.transTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null && !"".equals(queryParam.getDateMax()))
		{
			query.le("a.transTime", queryParam.getDateMax());
		}
		// query.eq("a.companyId", UserUtils.getCompanyId());

		query.addGourp("a.accountId");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<FinanceAccountLogSumVo> resultList = new ArrayList<FinanceAccountLogSumVo>();

		for (Map<String, Object> map : mapResult.getResult())
		{
			FinanceAccountLogSumVo vo = new FinanceAccountLogSumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, FinanceAccountLogSumVo.class);
				resultList.add(vo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		result.setCount(mapResult.getResult().size());
		result.setResult(resultList);
		return result;
	}

}
