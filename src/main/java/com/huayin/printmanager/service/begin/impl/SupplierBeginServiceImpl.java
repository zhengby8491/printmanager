/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月8日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.begin.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.begin.SupplierBegin;
import com.huayin.printmanager.persist.entity.begin.SupplierBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentAdvanceLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.FinanceTradeMode;
import com.huayin.printmanager.service.begin.SupplierBeginService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 供应商期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月8日
 * @since        2.0, 2018年1月8日 下午17:07:00, think, 规范和国际化
 */
@Service
public class SupplierBeginServiceImpl extends BaseServiceImpl implements SupplierBeginService
{
	@Override
	public SupplierBegin get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierBegin.class);
		query.eq("id", id);
		SupplierBegin supplierBegin = daoFactory.getCommonDao().getByDynamicQuery(query, SupplierBegin.class);

		if (supplierBegin != null)
		{
			supplierBegin.setDetailList(this.getDetail(id));
		}
		return supplierBegin;
	}

	@Override
	public List<SupplierBeginDetail> getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierBeginDetail.class);
		query.eq("masterId", id);
		List<SupplierBeginDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, SupplierBeginDetail.class);
		return detailList;

	}

	@Override
	@Transactional
	public Long save(SupplierBegin supplierBegin)
	{
		BoolValue flag = supplierBegin.getIsCheck();					// 标识是否保存并审核
		supplierBegin.setIsCheck(BoolValue.NO);								// 默认未审核
		supplierBegin.setBillType(BillType.BEGIN_SUPPLIER);
		supplierBegin.setBillNo(UserUtils.createBillNo(BillType.BEGIN_SUPPLIER));
		supplierBegin.setCompanyId(UserUtils.getCompanyId());
		supplierBegin.setCreateTime(new Date());
		// 创建人优先取员工姓名
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			supplierBegin.setCreateName(e.getName());
		}
		else
		{
			supplierBegin.setCreateName(UserUtils.getUserName());
		}
		supplierBegin.setCreateEmployeeId(UserUtils.getEmployeeId());
		supplierBegin.setMemo(supplierBegin.getMemo());
		daoFactory.getCommonDao().saveEntity(supplierBegin);
		for (SupplierBeginDetail detail : supplierBegin.getDetailList())
		{
			detail.setMasterId(supplierBegin.getId());
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setPaymentedMoney(new BigDecimal(0));
			if (detail.getAdvanceMoney() == null)
			{
				detail.setAdvanceMoney(new BigDecimal(0));
			}
			daoFactory.getCommonDao().getEntity(Supplier.class, detail.getSupplierId()).setIsBegin(BoolValue.YES);
			detail.setMemo(detail.getMemo());
			daoFactory.getCommonDao().saveEntity(detail);
		}
		if (flag == BoolValue.YES)
		{
			this.audit(supplierBegin.getId());
		}
		return supplierBegin.getId();

	}

	@Override
	@Transactional
	public Long update(SupplierBegin supplierBegin)
	{
		SupplierBegin supplierBegin_ = this.lockHasChildren(supplierBegin.getId());
		// 先判断是否已经审核
		if (supplierBegin_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		// 删除记录
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (SupplierBeginDetail newItem : supplierBegin.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (SupplierBeginDetail oldItem : supplierBegin_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				SupplierBeginDetail supplierBeginDetail = daoFactory.getCommonDao().getEntity(SupplierBeginDetail.class, id);
				daoFactory.getCommonDao().getEntity(Supplier.class, supplierBeginDetail.getSupplierId()).setIsBegin(BoolValue.NO);
				daoFactory.getCommonDao().deleteEntity(SupplierBeginDetail.class, id);
			}
		}
		supplierBegin_.setBeginTime(supplierBegin.getBeginTime());
		supplierBegin_.setMemo(supplierBegin.getMemo());
		for (SupplierBeginDetail detail : supplierBegin.getDetailList())
		{
			if (detail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(SupplierBeginDetail.class);
				querySt.eq("id", detail.getId());
				SupplierBeginDetail supplierBeginDetail = daoFactory.getCommonDao().getByDynamicQuery(querySt, SupplierBeginDetail.class);
				supplierBeginDetail.setSupplierId(detail.getSupplierId());
				supplierBeginDetail.setSupplierCode(detail.getSupplierCode());
				supplierBeginDetail.setSupplierName(detail.getSupplierName());
				if (detail.getAdvanceMoney() == null)
				{
					detail.setAdvanceMoney(new BigDecimal(0));
				}
				supplierBeginDetail.setAdvanceMoney(detail.getAdvanceMoney());
				supplierBeginDetail.setPaymentMoney(detail.getPaymentMoney());
				supplierBeginDetail.setReceiveTime(detail.getReceiveTime());
				supplierBeginDetail.setMemo(detail.getMemo());
				daoFactory.getCommonDao().getEntity(Supplier.class, supplierBeginDetail.getSupplierId()).setIsBegin(BoolValue.YES);
				daoFactory.getCommonDao().updateEntity(supplierBeginDetail);
			}
			else
			{
				detail.setMasterId(supplierBegin.getId());
				detail.setCompanyId(UserUtils.getCompanyId());
				daoFactory.getCommonDao().getEntity(Supplier.class, detail.getSupplierId()).setIsBegin(BoolValue.YES);
				daoFactory.getCommonDao().saveEntity(detail);
			}
		}
		if (supplierBegin.getIsCheck() == BoolValue.YES)
		{
			this.audit(supplierBegin.getId());
		}
		return supplierBegin.getId();

	}

	@Override
	@Transactional
	public boolean delete(Long id)
	{
		try
		{
			SupplierBegin master = this.lockHasChildren(id);
			for (SupplierBeginDetail detail : master.getDetailList())
			{
				daoFactory.getCommonDao().getEntity(Supplier.class, detail.getSupplierId()).setIsBegin(BoolValue.NO);
			}
			daoFactory.getCommonDao().deleteAllEntity(master.getDetailList());
			daoFactory.getCommonDao().deleteEntity(master);

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
	public boolean audit(Long id)
	{
		SupplierBegin order = this.get(id);
		// 先判断是否已经审核
		if (order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核");
		}
		for (SupplierBeginDetail detail : order.getDetailList())
		{
			// 创建预付款日志
			FinancePaymentAdvanceLog log = new FinancePaymentAdvanceLog();
			log.setTradeMode(FinanceTradeMode.ADD);
			log.setBillType(order.getBillType());
			log.setBillId(detail.getId());
			log.setCompanyId(detail.getCompanyId());
			log.setMoney(detail.getAdvanceMoney());
			log.setSupplierId(detail.getSupplierId());
			log.setSupplierName(detail.getSupplierName());
			log.setCreateTime(new Date());
			daoFactory.getCommonDao().saveEntity(log);
			// 增加客户预收款
			Supplier supplier = daoFactory.getCommonDao().lockObject(Supplier.class, detail.getSupplierId());
			if (supplier.getAdvanceMoney() == null)
			{
				supplier.setAdvanceMoney(new BigDecimal(0));
			}
			supplier.setAdvanceMoney(supplier.getAdvanceMoney().add(detail.getAdvanceMoney()));
			daoFactory.getCommonDao().updateEntity(supplier);
			UserUtils.clearCacheBasic(BasicType.CUSTOMER);// 清空缓存
		}
		serviceFactory.getCommonService().audit(BillType.BEGIN_SUPPLIER, id, BoolValue.YES);
		return true;
	}
	
	@Override
	@Transactional
	public boolean auditCancel(Long id)
	{
		SupplierBegin order = this.get(id);
		// 先判断是否已经反审核
		if (order.getIsCheck() == BoolValue.NO)
		{
			throw new BusinessException("已反审核");
		}
		for (SupplierBeginDetail detail : order.getDetailList())
		{
			// 创建预付款日志
			FinancePaymentAdvanceLog log = new FinancePaymentAdvanceLog();
			log.setTradeMode(FinanceTradeMode.SUBTRACT);
			log.setBillType(order.getBillType());
			log.setBillId(detail.getId());
			log.setCompanyId(detail.getCompanyId());
			log.setMoney(detail.getAdvanceMoney());
			log.setSupplierId(detail.getSupplierId());
			log.setSupplierName(detail.getSupplierName());
			log.setCreateTime(new Date());
			daoFactory.getCommonDao().saveEntity(log);
			// 减少客户预收款
			Supplier supplier = daoFactory.getCommonDao().lockObject(Supplier.class, detail.getSupplierId());
			if (supplier.getAdvanceMoney() == null)
			{
				supplier.setAdvanceMoney(new BigDecimal(0));
			}
			supplier.setAdvanceMoney(supplier.getAdvanceMoney().subtract(detail.getAdvanceMoney()));
			daoFactory.getCommonDao().updateEntity(supplier);
			UserUtils.clearCacheBasic(BasicType.CUSTOMER);// 清空缓存
		}
		serviceFactory.getCommonService().audit(BillType.BEGIN_SUPPLIER, id, BoolValue.NO);
		return true;

	}

	@Override
	public SearchResult<SupplierBegin> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierBegin.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}

		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("isCheck", queryParam.getAuditFlag());
		}
		query.eq("companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SupplierBegin.class);
	}

	@Override
	public SupplierBegin lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierBegin.class);
		query.eq("id", id);
		SupplierBegin order = daoFactory.getCommonDao().lockByDynamicQuery(query, SupplierBegin.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(SupplierBeginDetail.class);
		query_detail.eq("masterId", id);
		List<SupplierBeginDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, SupplierBeginDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

	@Override
	public SupplierBeginDetail findSupplierBeginDetail(Long supplierId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierBeginDetail.class, "a");
		query.addProjection(Projections.property("a"));
		query.createAlias(SupplierBegin.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.eq("a.supplierId", supplierId);
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.eq("a.isPaymentOver", BoolValue.NO);// 是否已完成付款
		query.eq("a.companyId", UserUtils.getCompanyId());
		SupplierBeginDetail s = daoFactory.getCommonDao().getByDynamicQuery(query, SupplierBeginDetail.class);
		return s;
	}

	@Override
	public List<SupplierBeginDetail> findAll(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierBeginDetail.class, "a");
		query.addProjection(Projections.property("a"));
		query.createAlias(SupplierBegin.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.createAlias(Supplier.class,JoinType.LEFTJOIN,"s","s.id=a.supplierId");
		if (null != queryParam.getSupplierIdList())
		{
			query.in("a.supplierId", queryParam.getSupplierIdList());
		}
		if (null != queryParam.getSupplierType())
		{
			query.eq("s.type", queryParam.getSupplierType());
		}
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		// query.eq("a.isPaymentOver", BoolValue.NO);// 是否已完成付款
		query.eq("a.companyId", UserUtils.getCompanyId());
		SearchResult<SupplierBeginDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SupplierBeginDetail.class);
		return result.getResult();
	}
}
