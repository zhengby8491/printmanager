package com.huayin.printmanager.service.basic.impl;

import org.springframework.stereotype.Service;

import com.huayin.printmanager.service.basic.MaterialBeginDetailService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;

@Service
public class MaterialBeginDetailServiceImpl extends BaseServiceImpl implements MaterialBeginDetailService
{

	/*@Override
	public MaterialBeginDetail getByCompanyId(String companyId)
	{
		DynamicQuery query = new CompanyDynamicQuery(MaterialBeginDetail.class);
		query.eq("companyId", companyId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, MaterialBeginDetail.class);
	}*/

}
