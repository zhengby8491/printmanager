package com.huayin.printmanager.service.basic.impl;

import org.springframework.stereotype.Service;

import com.huayin.printmanager.service.basic.ExchangeRateService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;

@Service
public class ExchangeRateServiceImpl extends BaseServiceImpl implements ExchangeRateService
{

	/*@Override
	public List<ExchangeRate> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(ExchangeRate.class);
		query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, ExchangeRate.class);
	}*/

}
