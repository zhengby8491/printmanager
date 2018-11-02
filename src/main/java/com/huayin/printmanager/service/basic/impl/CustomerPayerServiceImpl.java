package com.huayin.printmanager.service.basic.impl;

import org.springframework.stereotype.Service;

import com.huayin.printmanager.service.basic.CustomerPayerService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;

@Service
public class CustomerPayerServiceImpl extends BaseServiceImpl implements CustomerPayerService
{

	/*@Override
	public CustomerPayer getByCompanyId(String companyId)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerPayer.class);
		query.eq("companyId", companyId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, CustomerPayer.class);
	}*/

}
