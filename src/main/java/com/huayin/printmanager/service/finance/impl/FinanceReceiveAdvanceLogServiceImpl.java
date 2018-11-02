package com.huayin.printmanager.service.finance.impl;

import org.springframework.stereotype.Service;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.DateTimeUtil;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveAdvanceLog;
import com.huayin.printmanager.service.finance.FinanceReceiveAdvanceLogService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

@Service
public class FinanceReceiveAdvanceLogServiceImpl extends BaseServiceImpl implements FinanceReceiveAdvanceLogService
{

	@Override
	public SearchResult<FinanceReceiveAdvanceLog> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceReceiveAdvanceLog.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(queryParam.getDateMin()) + " 00:00:01", DateTimeUtil.DATE_FORMAT_YMDHMS));
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(queryParam.getDateMax()) + " 23:59:59", DateTimeUtil.DATE_FORMAT_YMDHMS));
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerName()))
		{
			query.like("customerName", "%" + queryParam.getCustomerName() + "%");
		}
		if (queryParam.getCustomerId() != null)
		{
			query.eq("customerId", queryParam.getCustomerId());
		}
		query.desc("createTime");
		// query.eq("companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);

		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, FinanceReceiveAdvanceLog.class);
	}

}
