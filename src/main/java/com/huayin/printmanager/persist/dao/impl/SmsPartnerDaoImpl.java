package com.huayin.printmanager.persist.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.huayin.common.persist.AbstractDao;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.printmanager.persist.dao.SmsPartnerDao;
import com.huayin.printmanager.persist.entity.sys.SmsPartner;
import com.huayin.printmanager.persist.enumerate.SmsPartnerState;
import com.huayin.printmanager.persist.enumerate.SmsSendType;

/**
 * 短信合作商持久实现
 * @author zhaojitao
 * @version 1.0, 2016-5-5
 */

@Repository
public class SmsPartnerDaoImpl extends AbstractDao<SmsPartner> implements SmsPartnerDao
{

	@Override
	public List<SmsPartner> findSmsPartnerBySmsSendType(SmsSendType smsSendType)
	{
		DynamicQuery query = new DynamicQuery(SmsPartner.class);
		query.eq("smsSendType", smsSendType);
		query.eq("status", SmsPartnerState.OPEN);
		return super.findAllByDynamicQuery(query);
	}

	@Override
	public Long findSmsPartnerLogWithCount(Long id, String name, SmsPartnerState status)
	{
		DynamicQuery query = new DynamicQuery(SmsPartner.class);
		if (id != null && id.toString().trim().length() > 0)
		{
			query.eq("id", id);
		}
		if (name != null && name.trim().length() > 0)
		{
			query.like("name", name);
		}
		if (status != null && status.getText().trim().length() > 0)
		{
			query.eq("status", status);
		}
		query.addProjection(Projections.count());
		return super.countByDynamicQuery(query);
	}

	@Override
	public List<SmsPartner> findSmsPartnerLogWithPage(Long id, String name, SmsPartnerState status, int pageIndex,
			int pageSize)
	{
		DynamicQuery query = new DynamicQuery(SmsPartner.class);
		if (id != null && id.toString().trim().length() > 0)
		{
			query.eq("id", id);
		}
		if (name != null && name.trim().length() > 0)
		{
			query.like("name", name);
		}
		if (status != null && status.getText().trim().length() > 0)
		{
			query.eq("status", status);
		}
		query.setPageIndex(pageIndex);
		query.setPageSize(pageSize);
		return super.findAllByDynamicQuery(query);
	}
}
