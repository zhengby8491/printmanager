package com.huayin.printmanager.service.basic.impl;

import org.springframework.stereotype.Service;

import com.huayin.printmanager.service.basic.ProductBeginDetailService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;

@Service
public class ProductBeginDetailServiceImpl extends BaseServiceImpl implements ProductBeginDetailService
{

/*	@Override
	public ProductBeginDetail getByCompanyId(String companyId)
	{
		DynamicQuery query = new CompanyDynamicQuery(ProductBeginDetail.class);
		query.eq("companyId", companyId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, ProductBeginDetail.class);
	}*/

}
