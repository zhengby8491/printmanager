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
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.offer.OfferOrder;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.wx.service.WXOfferOrderService;
import com.huayin.printmanager.wx.vo.QueryParam;

/**
 * <pre>
 * 微信 - 报价系统
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class WXOfferOrderServiceImpl extends BaseServiceImpl implements WXOfferOrderService
{
	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXOfferOrderService#findByCondition(com.huayin.printmanager.wx.vo.QueryParam)
	 */
	@Override
	public SearchResult<OfferOrder> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(OfferOrder.class, "o");
		query.addProjection(Projections.property("o"));

		if (queryParam.getDateMin() != null)
		{
			query.ge("o.createDateTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("o.createDateTime", queryParam.getDateMax());
		}
		if (queryParam.getOfferType() != null)
		{
			query.eq("o.offerType", queryParam.getOfferType());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("o.offerNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getName()))
		{
			query.like("o.name", "%" + queryParam.getName() + "%");
		}
		query.eq("o.companyId", queryParam.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("o.createDateTime");
		query.setIsSearchTotalCount(true);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OfferOrder.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXOfferOrderService#get(java.lang.Long)
	 */
	@Override
	public OfferOrder get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferOrder.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, OfferOrder.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXOfferOrderService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Boolean delete(Long id)
	{
		try
		{
			OfferOrder offerBean = get(id);
			daoFactory.getCommonDao().deleteEntity(offerBean);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
