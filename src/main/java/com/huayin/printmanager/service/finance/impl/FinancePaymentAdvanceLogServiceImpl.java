package com.huayin.printmanager.service.finance.impl;

import org.springframework.stereotype.Service;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.DateTimeUtil;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentAdvanceLog;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

@Service
public class FinancePaymentAdvanceLogServiceImpl extends BaseServiceImpl implements com.huayin.printmanager.service.finance.FinancePaymentAdvanceLogService
{
	@Override
	public SearchResult<FinancePaymentAdvanceLog> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinancePaymentAdvanceLog.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(queryParam.getDateMin()) + " 00:00:01", DateTimeUtil.DATE_FORMAT_YMDHMS));
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(queryParam.getDateMax()) + " 23:59:59", DateTimeUtil.DATE_FORMAT_YMDHMS));
		}
		if (StringUtils.isNotEmpty(queryParam.getSupplierName()))
		{
			query.like("supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (queryParam.getSupplierId() != null)
		{
			query.eq("supplierId", queryParam.getSupplierId());
		}
		query.desc("createTime");
		// query.eq("companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);

		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, FinancePaymentAdvanceLog.class);
	}

}
