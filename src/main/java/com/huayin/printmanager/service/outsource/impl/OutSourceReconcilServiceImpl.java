/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.outsource.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ConverterUtils;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.helper.ServiceHelper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturnDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.outsource.OutSourceReconcilService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 发外管理 - 发外对账
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午4:52:50, zhengby, 代码规范
 */
@Service
public class OutSourceReconcilServiceImpl extends BaseServiceImpl implements OutSourceReconcilService
{

	@Override
	public OutSourceReconcil get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReconcil.class);
		query.eq("id", id);
		OutSourceReconcil order = daoFactory.getCommonDao().getByDynamicQuery(query, OutSourceReconcil.class);

		// OutSourceReconcil order = daoFactory.getCommonDao().getEntity(OutSourceReconcil.class, id);
		order.setDetailList(this.getDetailList(id));
		return order;
	}

	@Override
	public OutSourceReconcilDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReconcilDetail.class);
		query.eq("id", id);
		OutSourceReconcilDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, OutSourceReconcilDetail.class);

		// OutSourceReconcilDetail detail = daoFactory.getCommonDao().getEntity(OutSourceReconcilDetail.class, id);
		detail.setMaster(daoFactory.getCommonDao().getEntity(OutSourceReconcil.class, detail.getMasterId()));
		return detail;
	}

	@Override
	public OutSourceReconcil lock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReconcil.class);
		query.eq("id", id);
		OutSourceReconcil order = daoFactory.getCommonDao().lockByDynamicQuery(query, OutSourceReconcil.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(OutSourceReconcilDetail.class);
		query_detail.eq("masterId", id);
		List<OutSourceReconcilDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, OutSourceReconcilDetail.class, LockType.LOCK_WAIT);

		/*
		 * OutSourceReconcil order = daoFactory.getCommonDao().lockObject(OutSourceReconcil.class, id);
		 * List<OutSourceReconcilDetail> detailList = this.getDetailList(id); for (OutSourceReconcilDetail detail :
		 * detailList) { detail = daoFactory.getCommonDao().lockObject(OutSourceReconcilDetail.class, detail.getId()); }
		 */
		order.setDetailList(detailList);
		return order;
	}

	public List<OutSourceReconcilDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReconcilDetail.class);
		query.eq("masterId", id);
		List<OutSourceReconcilDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OutSourceReconcilDetail.class);
		return detailList;
	}

	@Override
	@Transactional
	public OutSourceReconcil save(OutSourceReconcil order)
	{
		BoolValue flag = order.getIsCheck();
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.OUTSOURCE_OC);
		order.setBillNo(UserUtils.createBillNo(BillType.OUTSOURCE_OC));
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
		order.setIsCheck(BoolValue.NO);
		order.setIsForceComplete(BoolValue.NO);
		order = daoFactory.getCommonDao().saveEntity(order);
		for (OutSourceReconcilDetail detail : order.getDetailList())
		{
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setMasterId(order.getId());
			detail.setIsForceComplete(BoolValue.NO);
			detail.setUserNo(UserUtils.getUser().getUserNo());
			detail.setIsPaymentOver(BoolValue.NO);
			detail.setPaymentMoney(new BigDecimal(0));

			TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), detail.getTaxRateId());
			detail.setTaxRateName(taxRate.getName());
			detail.setTaxRatePercent(taxRate.getPercent());
			if (detail.getSourceBillType() == BillType.OUTSOURCE_OA)
			{
				// 反写到货单对账数量
				OutSourceArriveDetail sourceArrive = daoFactory.getCommonDao().lockObject(OutSourceArriveDetail.class, detail.getSourceDetailId());
				sourceArrive.setReconcilQty(sourceArrive.getReconcilQty().add(detail.getQty()));
				sourceArrive.setReconcilMoney(sourceArrive.getReconcilMoney().add(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(sourceArrive);
			}
			else if (detail.getSourceBillType() == BillType.OUTSOURCE_OR)
			{
				// 反写退货单对账数量
				OutSourceReturnDetail sourceReturn = daoFactory.getCommonDao().lockObject(OutSourceReturnDetail.class, detail.getSourceDetailId());
				sourceReturn.setReconcilQty((sourceReturn.getReconcilQty().add(new BigDecimal(Math.abs(detail.getQty().doubleValue())))).setScale(2, RoundingMode.HALF_UP));
				sourceReturn.setReconcilMoney(sourceReturn.getReconcilMoney().subtract(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(sourceReturn);
			}
		}
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());
		if (flag == BoolValue.YES)
		{
			serviceFactory.getCommonService().audit(BillType.OUTSOURCE_OC, order.getId(), BoolValue.YES);
		}
		return order;
	}

	@Override
	@Transactional
	public OutSourceReconcil update(OutSourceReconcil order)
	{
		BoolValue flag = order.getIsCheck();
		order.setIsCheck(BoolValue.NO);
		OutSourceReconcil old_order = serviceFactory.getOutSourceReconcilService().lock(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		Map<Long, OutSourceReconcilDetail> old_detail_map = ConverterUtils.list2Map(old_order.getDetailList(), "id");

		for (OutSourceReconcilDetail new_detail : order.getDetailList())
		{
			OutSourceReconcilDetail old_detail = old_detail_map.get(new_detail.getId());
			if (new_detail.getId() == null)
			{// 新增
				new_detail.setCompanyId(UserUtils.getCompanyId());
				new_detail.setMasterId(order.getId());
				new_detail.setIsForceComplete(BoolValue.NO);
				TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), new_detail.getTaxRateId());
				new_detail.setTaxRateName(taxRate.getName());
				new_detail.setTaxRatePercent(taxRate.getPercent());
				new_detail.setUserNo(UserUtils.getUser().getUserNo());
				new_detail.setIsPaymentOver(BoolValue.NO);
				new_detail.setPaymentMoney(new BigDecimal(0));
				if (new_detail.getSourceBillType() == BillType.OUTSOURCE_OA)
				{
					// 反写到货单对账数量
					OutSourceArriveDetail sourceArrive = daoFactory.getCommonDao().lockObject(OutSourceArriveDetail.class, new_detail.getSourceDetailId());
					sourceArrive.setReconcilQty(sourceArrive.getReconcilQty().add(new_detail.getQty()));
					sourceArrive.setReconcilMoney(sourceArrive.getReconcilMoney().add(new_detail.getMoney()));
					daoFactory.getCommonDao().updateEntity(sourceArrive);
				}
				else if (new_detail.getSourceBillType() == BillType.OUTSOURCE_OR)
				{
					// 反写退货单对账数量
					OutSourceReturnDetail sourceReturn = daoFactory.getCommonDao().lockObject(OutSourceReturnDetail.class, new_detail.getSourceDetailId());
					sourceReturn.setReconcilQty(sourceReturn.getReconcilQty().add(new BigDecimal(Math.abs(new_detail.getQty().doubleValue()))));
					sourceReturn.setReconcilMoney(sourceReturn.getReconcilMoney().subtract(new_detail.getMoney()));
					daoFactory.getCommonDao().updateEntity(sourceReturn);
				}
				serviceFactory.getPersistService().save(new_detail);
			}
			else
			{
				// 更新
				if (new_detail.getSourceBillType() == BillType.OUTSOURCE_OA)
				{
					// 反写到货单对账数量
					OutSourceArriveDetail sourceArrive = daoFactory.getCommonDao().lockObject(OutSourceArriveDetail.class, old_detail.getSourceDetailId());
					sourceArrive.setReconcilQty(sourceArrive.getReconcilQty().subtract(old_detail.getQty().subtract(new_detail.getQty())));
					sourceArrive.setReconcilMoney(sourceArrive.getReconcilMoney().subtract(old_detail.getMoney()).add(new_detail.getMoney()));
					daoFactory.getCommonDao().updateEntity(sourceArrive);
				}
				else if (new_detail.getSourceBillType() == BillType.OUTSOURCE_OR)
				{
					// 反写退货单对账数量
					OutSourceReturnDetail sourceReturn = daoFactory.getCommonDao().lockObject(OutSourceReturnDetail.class, old_detail.getSourceDetailId());
					sourceReturn.setReconcilQty(sourceReturn.getReconcilQty().subtract(new BigDecimal(Math.abs(old_detail.getQty().doubleValue()) - Math.abs(new_detail.getQty().doubleValue()))));
					sourceReturn.setReconcilMoney(sourceReturn.getReconcilMoney().add(old_detail.getMoney()).subtract(new_detail.getMoney()));
					daoFactory.getCommonDao().updateEntity(sourceReturn);
				}
				PropertyClone.copyProperties(old_detail, new_detail, false, null, new String[] { "memo" });// 替换成新内容

				daoFactory.getCommonDao().updateEntity(old_detail);// 更新子表
			}
		}
		// 删除记录
		Map<String, List<OutSourceReconcilDetail>> resultMap = ServiceHelper.filterCUD(old_order.getDetailList(), order.getDetailList());
		List<OutSourceReconcilDetail> listDel = resultMap.get(ServiceHelper.Cud.D);
		for (OutSourceReconcilDetail detail : listDel)
		{
			// 反写发外到货/退货数量
			if (detail.getSourceBillType() == BillType.OUTSOURCE_OA)
			{// 反写发外到货对账数量
				OutSourceArriveDetail sourceArrive = daoFactory.getCommonDao().lockObject(OutSourceArriveDetail.class, detail.getSourceDetailId());
				sourceArrive.setReconcilQty(sourceArrive.getReconcilQty().subtract(detail.getQty()));
				sourceArrive.setReconcilMoney(sourceArrive.getReconcilMoney().subtract(detail.getMoney()));
			}
			else
			{
				// 反写发外退货对账数量
				OutSourceReturnDetail sourceReturn = daoFactory.getCommonDao().lockObject(OutSourceReturnDetail.class, detail.getSourceDetailId());
				sourceReturn.setReconcilQty(sourceReturn.getReconcilQty().subtract(new BigDecimal(Math.abs(detail.getQty().doubleValue()))));
				sourceReturn.setReconcilMoney(sourceReturn.getReconcilMoney().add(detail.getMoney()));
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(listDel); // 删除

		PropertyClone.copyProperties(old_order, order, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "supplierAddress", "supplierPayer" });// 替换新内容
		order.setUpdateName(UserUtils.getUserName());
		order.setUpdateTime(new Date());
		// 必须先保存子表，然后再保存主表
		daoFactory.getCommonDao().updateEntity(old_order);// 更新主表
		if (flag == BoolValue.YES)
		{
			serviceFactory.getCommonService().audit(BillType.OUTSOURCE_OC, order.getId(), BoolValue.YES);
		}
		return order;
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		OutSourceReconcil order = this.lock(id);
		List<OutSourceReconcilDetail> detailList = order.getDetailList();
		// daoFactory.getCommonDao().deleteEntity(OutSourceReconcil.class, id);
		// List<OutSourceReconcilDetail> detailList = getDetailList(id);
		for (OutSourceReconcilDetail detail : detailList)
		{
			if (detail.getSourceBillType() == BillType.OUTSOURCE_OA)
			{
				// 反写到货单对账数量
				OutSourceArriveDetail sourceArrive = daoFactory.getCommonDao().lockObject(OutSourceArriveDetail.class, detail.getSourceDetailId());

				// TODO
				sourceArrive.setReconcilQty(sourceArrive.getReconcilQty().subtract(detail.getQty()));
				sourceArrive.setReconcilMoney(sourceArrive.getReconcilMoney().subtract(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(sourceArrive);
			}
			else if (detail.getSourceBillType() == BillType.OUTSOURCE_OR)
			{
				// 反写退货单对账数量
				OutSourceReturnDetail sourceReturn = daoFactory.getCommonDao().lockObject(OutSourceReturnDetail.class, detail.getSourceDetailId());
				sourceReturn.setReconcilQty(sourceReturn.getReconcilQty().subtract(new BigDecimal(Math.abs(detail.getQty().doubleValue()))));
				sourceReturn.setReconcilMoney(sourceReturn.getReconcilMoney().add(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(sourceReturn);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(detailList);
		daoFactory.getCommonDao().deleteEntity(order);
	}

	@Override
	public SearchResult<OutSourceReconcil> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReconcil.class, "o");
		query.addProjection(Projections.property("o"));
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("o.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("o.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("o.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("o.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("o.isCheck", queryParam.getAuditFlag());
		}
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, "s");
			query.eqProperty("s.id", "o.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.eq("o.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("o.createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OutSourceReconcil.class);
	}

	@Override
	public SearchResult<OutSourceReconcilDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReconcilDetail.class, "a");
		query.createAlias(OutSourceReconcil.class, "b");
		query.addProjection(Projections.property("a,b"));
		query.createAlias(Supplier.class, "c");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		query.eqProperty("a.masterId", "b.id");
		query.eqProperty("c.id", "b.supplierId");

		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("a.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
		{
			query.like("a.sourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getOutSourceBillNo()))
		{
			query.like("a.outSourceBillNo", "%" + queryParam.getOutSourceBillNo() + "%");
		}

		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("b.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("a.procedureName", "%" + queryParam.getProcedureName() + "%");
		}

		if (queryParam.getOutSourceType() != null)
		{
			query.eq("a.type", queryParam.getOutSourceType());
		}
		if (queryParam.getSupplierClassId() != null && queryParam.getSupplierClassId() != -1)
		{
			query.eq("c.supplierClassId", queryParam.getSupplierClassId());
		}

		if (queryParam.getProcedureType() != null)
		{
			query.eq("a.procedureType", queryParam.getProcedureType());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("b.isCheck", queryParam.getAuditFlag());
		}
		// query.eq("a.isForceComplete", queryParam.getCompleteFlag() == null ? BoolValue.NO :
		// queryParam.getCompleteFlag());// 工序是否强制完工

		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		;
		SearchResult<OutSourceReconcilDetail> result = new SearchResult<OutSourceReconcilDetail>();
		result.setResult(new ArrayList<OutSourceReconcilDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OutSourceReconcilDetail detail = (OutSourceReconcilDetail) c[0];
			detail.setMaster((OutSourceReconcil) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public boolean forceComplete(TableType tableType, Long[] ids, Long[] arriveIds, Long[] returnIds, BoolValue completeFlag)
	{
		if (tableType == TableType.MASTER)
		{
			serviceFactory.getCommonService().forceComplete(OutSourceReconcil.class, ids, completeFlag);

		}
		else if (tableType == TableType.DETAIL)
		{
			serviceFactory.getCommonService().forceComplete(OutSourceArriveDetail.class, arriveIds, completeFlag);
			serviceFactory.getCommonService().forceComplete(OutSourceReturnDetail.class, returnIds, completeFlag);
		}
		else
		{
			return false;
		}
		return true;

	}

	@Override
	public BigDecimal countReturnPaymentMoney(OutSourceArriveDetail outSourceArriveDetail)
	{
		BigDecimal sum = new BigDecimal(0.00);
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReconcilDetail.class, "a");
		query.createAlias(OutSourceReconcil.class, "b");
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", outSourceArriveDetail.getId());
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<OutSourceReconcilDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OutSourceReconcilDetail.class);
		for (OutSourceReconcilDetail outSourceReconcilDetail : result.getResult())
		{
			sum = sum.add(outSourceReconcilDetail.getPaymentMoney());
		}
		return sum;
	}

}
