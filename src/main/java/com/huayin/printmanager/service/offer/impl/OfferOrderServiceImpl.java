/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月16日 上午10:04:45
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.offer.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.offer.OfferOrder;
import com.huayin.printmanager.persist.entity.offer.OfferOrderQuoteInner;
import com.huayin.printmanager.persist.entity.offer.OfferOrderQuoteOut;
import com.huayin.printmanager.persist.entity.offer.OfferPart;
import com.huayin.printmanager.persist.entity.offer.OfferPartProcedure;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.offer.OfferOrderService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * 
 * <pre>
 * 报价业务层
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月16日
 */
@Service
public class OfferOrderServiceImpl extends BaseServiceImpl implements OfferOrderService
{
	@Override
	public SearchResult<OfferOrder> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferOrder.class, "o");
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
			query.like("o.productName", "%" + queryParam.getName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("o.customerName", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("o.productName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("o.offerNo", "%" + queryParam.getSearchContent() + "%")));
		}
		// 报价单还未生成销售订单
		if (StringUtils.isNoneBlank(queryParam.getSaleBillNo()))
		{
			query.eq("o.isCheck", BoolValue.YES);
			query.add(Restrictions.or(Restrictions.isNull("o.billNo"), Restrictions.eq("o.billNo", "")));
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("o.isCheck", queryParam.getAuditFlag());
		}
		
		if(queryParam.getCompleteFlag() != null)
		{
			// 未强制完工
			if (queryParam.getCompleteFlag() == BoolValue.NO)
			{
				query.eq("o.isForceComplete", BoolValue.NO);// 工序是否强制完工
			}
			// 已强制完工
			else
			{
				query.eq("o.isForceComplete", BoolValue.YES);
			}
		}
		
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		// query.desc("o.createDateTime");
		query.desc("o.offerNo");
		query.setIsSearchTotalCount(true);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OfferOrder.class);
	}

	@Override
	public OfferOrder get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferOrder.class);
		query.eq("id", id);
		OfferOrder order = daoFactory.getCommonDao().getByDynamicQuery(query, OfferOrder.class);
		if (order.getOfferType() == OfferType.ALBUMBOOK || order.getOfferType() == OfferType.CARTONBOX)
		{
			order.setProductProcedure(_getProductProcedureList(id));
		}
		order.setOfferPartList(_getPartList(order.getId()));
		order.setOfferOrderQuoteOutList(_getQuoteOutList(id));
		order.setOfferOrderQuoteInnerList(_getQuoteInnerList(id));
		return order;
	}

	@Override
	public OfferOrder get(String offerNo)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferOrder.class);
		query.eq("offerNo", offerNo);
		OfferOrder order = daoFactory.getCommonDao().getByDynamicQuery(query, OfferOrder.class);
		if (order.getOfferType() == OfferType.ALBUMBOOK || order.getOfferType() == OfferType.CARTONBOX)
		{
			order.setProductProcedure(_getProductProcedureList(order.getId()));
		}
		order.setOfferPartList(_getPartList(order.getId()));
		order.setOfferOrderQuoteOutList(_getQuoteOutList(order.getId()));
		order.setOfferOrderQuoteInnerList(_getQuoteInnerList(order.getId()));
		return order;
	}

	@Override
	public OfferOrder getOrder(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferOrder.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, OfferOrder.class);
	}

	/**
	 * <pre>
	 * 查找部件
	 * </pre>
	 * @param masterId
	 * @return
	 * @since 1.0, 2017年11月13日 上午11:26:24, zhengby
	 */
	private List<OfferPart> _getPartList(Long masterId)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferPart.class);
		query.eq("masterId", masterId);
		query.asc("id");
		List<OfferPart> partList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferPart.class);
		for (OfferPart op : partList)
		{
			op.setOfferPartProcedureList(_getProcedureList(op.getId()));
		}
		return partList;
	}

	/**
	 * <pre>
	 * 查找部件的后道工序
	 * </pre>
	 * @param partId
	 * @return
	 * @since 1.0, 2017年11月13日 上午11:27:05, zhengby
	 */
	private List<OfferPartProcedure> _getProcedureList(Long partId)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferPartProcedure.class);
		query.eq("partId", partId);
		query.asc("id");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferPartProcedure.class);
	}

	/**
	 * <pre>
	 * 查找书刊类的成品工序
	 * </pre>
	 * @param offerId
	 * @return
	 * @since 1.0, 2017年11月17日 上午11:41:15, zhengby
	 */
	private List<OfferPartProcedure> _getProductProcedureList(Long offerId)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferPartProcedure.class);
		query.eq("orderId", offerId);
		query.asc("id");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferPartProcedure.class);
	}

	/**
	 * <pre>
	 * 查找报价单的对外报价单阶梯数据报表
	 * </pre>
	 * @param offerId
	 * @return
	 * @since 1.0, 2017年11月30日 下午6:26:30, zhengby
	 */
	private List<OfferOrderQuoteOut> _getQuoteOutList(Long offerId)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferOrderQuoteOut.class);
		query.eq("masterId", offerId);
		query.asc("id");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferOrderQuoteOut.class);
	}

	/**
	 * <pre>
	 * 查找报价单的内部核价单阶梯数据报表
	 * </pre>
	 * @param offerId
	 * @return
	 * @since 1.0, 2017年11月30日 下午6:27:10, zhengby
	 */
	private List<OfferOrderQuoteInner> _getQuoteInnerList(Long offerId)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferOrderQuoteInner.class);
		query.eq("masterId", offerId);
		query.asc("id");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferOrderQuoteInner.class);
	}

	@Override
	public List<OfferOrderQuoteInner> findAllQuoteInner()
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferOrderQuoteInner.class);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferOrderQuoteInner.class);
	}

	@Override
	@Transactional
	public Boolean delete(Long id)
	{
		try
		{
			// 删除表：先删除子表再删除主表，从细往粗删
			OfferOrder offerBean = get(id);

			for (OfferPart op : offerBean.getOfferPartList())
			{ // 删除后道工序
				daoFactory.getCommonDao().deleteAllEntity(op.getOfferPartProcedureList());
			}
			// 删除部件
			daoFactory.getCommonDao().deleteAllEntity(offerBean.getOfferPartList());
			// 删除成品工序
			if (CollectionUtils.isNotEmpty(offerBean.getProductProcedure()))
			{
				daoFactory.getCommonDao().deleteAllEntity(offerBean.getProductProcedure());
			}
			// 删除阶梯数据
			if (CollectionUtils.isNotEmpty(offerBean.getOfferOrderQuoteInnerList()))
			{
				daoFactory.getCommonDao().deleteAllEntity(offerBean.getOfferOrderQuoteInnerList());
			}
			// 删除阶梯数据
			if (CollectionUtils.isNotEmpty(offerBean.getOfferOrderQuoteOutList()))
			{
				daoFactory.getCommonDao().deleteAllEntity(offerBean.getOfferOrderQuoteOutList());
			}
			// 删除整个报价单
			daoFactory.getCommonDao().deleteEntity(offerBean);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public OfferOrder updateOrder(OfferOrder offerOrder)
	{
		return daoFactory.getCommonDao().updateEntity(offerOrder);
	}

	@Override
	@Transactional
	public Boolean check(Long id)
	{
		return this._check(id, BoolValue.YES);
	}

	/**
	 * <pre>
	 * 审核、反审核
	 * </pre>
	 * @param id
	 * @param isCheck
	 * @return
	 * @since 1.0, 2018年2月10日 上午9:20:45, think
	 */
	private boolean _check(Long id, BoolValue isCheck)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferOrder.class);
		query.eq("id", id);
		OfferOrder offerOrder = daoFactory.getCommonDao().getByDynamicQuery(query, OfferOrder.class);
		if (isCheck == BoolValue.YES)
		{
			// 判断是否已审核
			if (offerOrder.getIsCheck() == BoolValue.YES)
			{
				throw new BusinessException("已审核");
			}
		}
		else
		{
			// 判断是否已反审核
			if (offerOrder.getIsCheck() == BoolValue.NO)
			{
				throw new BusinessException("已反审核");
			}
		}
		offerOrder.setCheckTime(new Date());
		offerOrder.setCheckUserName(UserUtils.getUserName());
		offerOrder.setIsCheck(isCheck);
		daoFactory.getCommonDao().updateEntity(offerOrder);
		return true;
	}

	@Override
	@Transactional
	public Boolean checkBack(Long id)
	{
		return this._check(id, BoolValue.NO);
	}

	@Override
	public List<OfferOrder> findBySaleId(Long saleId)
	{
		DynamicQuery query = new CompanyDynamicQuery(OfferOrder.class);
		query.eq("saleId", saleId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OfferOrder.class);
	}

	@Override
	@Transactional
	public boolean forceComplete(List<Long> ids, BoolValue flag)
	{
		if (ids != null && ids.size() > 0)
		{
			DynamicQuery query = new CompanyDynamicQuery(OfferOrder.class);
			query.in("id", ids);
			List<OfferOrder> tableList = serviceFactory.getDaoFactory().getCommonDao().lockByDynamicQuery(query, OfferOrder.class, LockType.LOCK_WAIT);
			for (OfferOrder table : tableList)
			{
				table.setIsForceComplete(flag);
			}
			serviceFactory.getDaoFactory().getCommonDao().updateAllEntity(tableList);

			return true;
		}
		else
		{
			return false;
		}

	}
}
