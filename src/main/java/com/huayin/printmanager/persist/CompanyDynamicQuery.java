package com.huayin.printmanager.persist;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.utils.UserUtils;

public class CompanyDynamicQuery extends DynamicQuery
{
	public CompanyDynamicQuery(Class<?> clz)
	{
		super(clz);
			try
			{
				if (clz.newInstance() instanceof BaseTableIdEntity)
				{
					this.eq("companyId", UserUtils.getCompanyId());
				}
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
	}

	public CompanyDynamicQuery(DynamicQuery query)
	{
		super(query);
	}

	public CompanyDynamicQuery(String table)
	{
		super(table);
	}

	public CompanyDynamicQuery(Class<?> clz, String alias)
	{
		super(clz, alias);
			try
			{
				if (clz.newInstance() instanceof BaseTableIdEntity)
				{
					this.eq(alias+".companyId", UserUtils.getCompanyId());
				}
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
	}

	public CompanyDynamicQuery(DynamicQuery query, String alias)
	{
		super(query, alias);
	}

	public CompanyDynamicQuery(String table, String alias)
	{
		super(table, alias);
	}

}
