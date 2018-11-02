/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年6月21日 上午9:13:39
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.finance.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.helper.ServiceHelper;
import com.huayin.printmanager.helper.ServiceHelper.Cud;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjust;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjustDetail;
import com.huayin.printmanager.persist.enumerate.AccountTransType;
import com.huayin.printmanager.persist.enumerate.AdjustType;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.finance.FinanceAdjustService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 - 财务调整单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年6月21日上午9:13:39, zhengby
 */
@Service
public class FinanceAdjustServiceImpl extends BaseServiceImpl implements FinanceAdjustService
{
	@Override
	public FinanceAdjust getMaster(Long id)
	{
		DynamicQuery query = new DynamicQuery(FinanceAdjust.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, FinanceAdjust.class);
	}

	@Override
	public List<FinanceAdjustDetail> getDetailList(Long id)
	{
		DynamicQuery query = new DynamicQuery(FinanceAdjustDetail.class);
		query.eq("masterId", id);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, FinanceAdjustDetail.class);
	}

	@Override
	public FinanceAdjust get(Long id)
	{
		FinanceAdjust master = this.getMaster(id);
		master.setDetailList(this.getDetailList(id));
		return master;
	}

	@Override
	public FinanceAdjustDetail getDetail(Long id)
	{
		DynamicQuery query = new DynamicQuery(FinanceAdjustDetail.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, FinanceAdjustDetail.class);
	}

	@Override
	public FinanceAdjustDetail getDetailHasMaster(Long id)
	{
		FinanceAdjustDetail detail = this.getDetail(id);
		detail.setMaster(this.get(detail.getMasterId()));
		return detail;
	}

	@Override
	@Transactional
	public void save(FinanceAdjust order)
	{
		BoolValue flag = order.getIsCheck();
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.FINANCE_ADJUST);
		order.setBillNo(UserUtils.createBillNo(BillType.FINANCE_ADJUST));
		order.setUserNo(UserUtils.getUser().getUserNo());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			order.setCreateName(e.getName());
		}
		else
		{
			order.setCreateName(UserUtils.getUserName());
		}
		order.setCreateTime(new Date());
		order.setCreateEmployeeId(UserUtils.getEmployeeId());
		order.setIsForceComplete(BoolValue.NO);
		order.setIsCancel(BoolValue.NO);
		order.setIsCheck(BoolValue.NO);
		order = daoFactory.getCommonDao().saveEntity(order);
		if (order.getDetailList() != null && order.getDetailList().size() != 0)
		{
			for (FinanceAdjustDetail detail : order.getDetailList())
			{
				detail.setMasterId(order.getId());
				detail.setIsForceComplete(BoolValue.NO);
				detail.setUserNo(UserUtils.getUser().getUserNo());
				detail.setCompanyId(UserUtils.getCompanyId());
				detail.setIsReceiveOrPayOver(BoolValue.NO);
			}
		}
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());

		// 保存并审核按钮执行审核
		if (flag == BoolValue.YES)
		{
			this.audit(order);
		}
	}

	@Override
	@Transactional
	public void update(FinanceAdjust order)
	{
		BoolValue flag = order.getIsCheck();					// 标识是否保存并审核
		order.setIsCheck(BoolValue.NO);								// 默认未审核
		FinanceAdjust old_order = this.lockHasChildren(order.getId());
		// 先判断是否已经审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		Map<String, List<FinanceAdjustDetail>> map = ServiceHelper.filterCUD(old_order.getDetailList(), order.getDetailList());
		// 区分出增删改记录
		List<FinanceAdjustDetail> delList = map.get(Cud.D);
		List<FinanceAdjustDetail> newList = map.get(Cud.C);
		List<FinanceAdjustDetail> updList = map.get(Cud.U);

		// 增 删 改
		daoFactory.getCommonDao().deleteAllEntity(delList);
		for (FinanceAdjustDetail detail : updList)
		{
			detail.setMasterId(order.getId());
			detail.setIsForceComplete(BoolValue.NO);
			detail.setUserNo(UserUtils.getUser().getUserNo());
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setIsReceiveOrPayOver(BoolValue.NO);
		}
		daoFactory.getCommonDao().updateAllEntity(updList);

		for (FinanceAdjustDetail detail : newList)
		{
			detail.setMasterId(order.getId());
			detail.setIsForceComplete(BoolValue.NO);
			detail.setUserNo(UserUtils.getUser().getUserNo());
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setIsReceiveOrPayOver(BoolValue.NO);
		}
		daoFactory.getCommonDao().saveAllEntity(newList);
		// 更新主表
		PropertyClone.copyProperties(old_order, order, false, new String[] { "detailList" }, new String[] { "memo" });// 替换新内容
		order.setUpdateName(UserUtils.getUserName());
		order.setUpdateTime(new Date());
		// 审核
		if (flag == BoolValue.YES)
		{
			this.audit(order);
		}
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		FinanceAdjust order = this.lockHasChildren(id);
		daoFactory.getCommonDao().deleteAllEntity(order.getDetailList());
		daoFactory.getCommonDao().deleteEntity(FinanceAdjust.class, id);
	}

	@Override
	public SearchResult<FinanceAdjust> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceAdjust.class);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (null != queryParam.getEmployeeId())
		{
			query.eq("employeeId", queryParam.getEmployeeId());
		}
		if (null != queryParam.getAuditFlag())
		{
			query.eq("isCheck", queryParam.getAuditFlag());
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("createTime");
		query.setIsSearchTotalCount(true);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, FinanceAdjust.class);
	}

	@Override
	public SearchResult<FinanceAdjustDetail> findDetailBycondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceAdjustDetail.class, "d");
		query.createAlias(FinanceAdjust.class, JoinType.LEFTJOIN, "m", "d.masterId=m.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("m.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("m.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("m.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (null != queryParam.getEmployeeId())
		{
			query.eq("m.employeeId", queryParam.getEmployeeId());
		}
		if (null != queryParam.getAdjustType())
		{
			query.eq("m.adjustType", queryParam.getAdjustType());
		}
		if (null != queryParam.getAuditFlag())
		{
			query.eq("m.isCheck", queryParam.getAuditFlag());
		}
		if (null != queryParam.getBusinessName())
		{
			query.like("d.businessName", "%"+queryParam.getBusinessName()+"%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("m.createTime");
		query.setIsSearchTotalCount(true);

		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<FinanceAdjustDetail> result = new SearchResult<FinanceAdjustDetail>();
		result.setResult(new ArrayList<FinanceAdjustDetail>());
		result.setCount(temp_result.getCount());
		for (Object[] obj : temp_result.getResult())
		{
			FinanceAdjustDetail detail = (FinanceAdjustDetail) obj[0];
			FinanceAdjust master = (FinanceAdjust) obj[1];
			detail.setMaster(master);
			result.getResult().add(detail);
		}
		return result;
	}

	@Override
	public FinanceAdjust lockHasChildren(Long id)
	{
		FinanceAdjust order = daoFactory.getCommonDao().lockObject(FinanceAdjust.class, id);
		DynamicQuery query_detail = new CompanyDynamicQuery(FinanceAdjustDetail.class);
		query_detail.eq("masterId", id);
		List<FinanceAdjustDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, FinanceAdjustDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

	@Override
	@Transactional
	public boolean audit(FinanceAdjust order)
	{
		boolean bl = serviceFactory.getCommonService().audit(BillType.FINANCE_ADJUST, order.getId(), BoolValue.YES);
		// 反写账户资金
		if (order.getAdjustType() == AdjustType.ACCOUNT)
		{
			for (FinanceAdjustDetail new_detail : order.getDetailList())
			{
				if (new_detail.getAdjustMoney().compareTo(BigDecimal.ZERO) >= 0)
				{
					// 当调整金额为正数时，账户资金增加，账户流水记录增加一条；
					serviceFactory.getAccountService().addMoney(new_detail.getBusinessId(), new_detail.getAdjustMoney(), AccountTransType.FINANCE_ADJUST, order.getId().toString(), "财务调整单编号[" + order.getBillNo() + "]");
				}
				else
				{
					// 当调整金额为负数时，账户资金减少，账户流水记录增加一条；
					serviceFactory.getAccountService().subtractMoney(new_detail.getBusinessId(), new_detail.getAdjustMoney().abs(), AccountTransType.FINANCE_ADJUST, order.getId().toString(), "财务调整单编号[" + order.getBillNo() + "]");
				}
			}
		}
		return bl;
	}

}
