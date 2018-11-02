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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ConverterUtils;
import com.huayin.common.util.ObjectHelper;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.helper.ServiceHelper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.basic.ProcedureClass;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.SupplierClass;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArrive;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturn;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturnDetail;
import com.huayin.printmanager.persist.entity.produce.WorkProcedure;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.sale.SaleOrder;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OutSourceType;
import com.huayin.printmanager.persist.enumerate.ProgressStatusOutsource;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.outsource.OutSourceProcessService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.service.vo.SumVo;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 发外管理 - 发外加工
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午4:51:51, zhengby, 代码规范
 */
@Service
public class OutSourceProcessServiceImpl extends BaseServiceImpl implements OutSourceProcessService
{

	@Override
	public OutSourceProcess get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceProcess.class);
		query.eq("id", id);
		OutSourceProcess order = daoFactory.getCommonDao().getByDynamicQuery(query, OutSourceProcess.class);

		// OutSourceProcess order = daoFactory.getCommonDao().getEntity(OutSourceProcess.class, id);
		order.setDetailList(this.getDetailList(id));
		return order;
	}

	@Override
	public OutSourceProcess get(String billNo)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceProcess.class);
		query.eq("billNo", billNo);
		OutSourceProcess order = daoFactory.getCommonDao().getByDynamicQuery(query, OutSourceProcess.class);
		return order;
	}

	@Override
	public OutSourceProcessDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceProcessDetail.class);
		query.eq("id", id);
		OutSourceProcessDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, OutSourceProcessDetail.class);

		// OutSourceProcessDetail detail = daoFactory.getCommonDao().getEntity(OutSourceProcessDetail.class, id);
		detail.setMaster(daoFactory.getCommonDao().getEntity(OutSourceProcess.class, detail.getMasterId()));
		return detail;
	}

	@Override
	public OutSourceProcess lock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceProcess.class);
		query.eq("id", id);
		OutSourceProcess order = daoFactory.getCommonDao().lockByDynamicQuery(query, OutSourceProcess.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(OutSourceProcessDetail.class);
		query_detail.eq("masterId", id);
		List<OutSourceProcessDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, OutSourceProcessDetail.class, LockType.LOCK_WAIT);
		/*
		 * OutSourceProcess order = daoFactory.getCommonDao().lockObject(OutSourceProcess.class, id);
		 * List<OutSourceProcessDetail> detailList = this.getDetailList(id); for (OutSourceProcessDetail detail :
		 * detailList) { detail = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class, detail.getId()); }
		 */
		order.setDetailList(detailList);
		return order;
	}

	public List<OutSourceProcessDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceProcessDetail.class);
		query.eq("masterId", id);
		List<OutSourceProcessDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OutSourceProcessDetail.class);
		return detailList;
	}

	@Override
	@Transactional
	public OutSourceProcess save(OutSourceProcess order)
	{
		// 代工平台的公司id
		String originCompanyId = order.getOriginCompanyId();
		BoolValue flag = order.getIsCheck();
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.OUTSOURCE_OP);
		order.setBillNo(UserUtils.createBillNo(BillType.OUTSOURCE_OP));
		order.setProgressStatus(ProgressStatusOutsource.NO_ARRIVING);
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
		for (OutSourceProcessDetail detail : order.getDetailList())
		{
			// 来自代工平台
			detail.setOriginCompanyId(originCompanyId);
			detail.setArriveQty(new BigDecimal(0));
			detail.setArriveMoney(new BigDecimal(0));
			detail.setIsForceComplete(BoolValue.NO);
			if (StringUtils.isBlank(detail.getWorkBillNo()))
			{
				detail.setWorkBillNo(detail.getSourceBillNo());// 工单单据编号
				detail.setWorkId(detail.getSourceId());
			}

			detail.setMasterId(order.getId());
			detail.setCompanyId(UserUtils.getCompanyId());
			TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), detail.getTaxRateId());
			detail.setTaxRateName(taxRate.getName());
			detail.setTaxRatePercent(taxRate.getPercent());
			detail.setUserNo(UserUtils.getUser().getUserNo());
			// 反写加工单数量
			if (detail.getType() == OutSourceType.PROCESS)
			{// 工序发外

				WorkProcedure source = daoFactory.getCommonDao().lockObject(WorkProcedure.class, detail.getSourceDetailId());
				source.setOutOfQty(source.getOutOfQty().add(detail.getQty()));
				daoFactory.getCommonDao().updateEntity(source);
			}
			else
			{// 整单发外
				WorkProduct source = daoFactory.getCommonDao().lockObject(WorkProduct.class, detail.getSourceDetailId());
				source.setOutOfQty(source.getOutOfQty() + detail.getQty().intValue());
				daoFactory.getCommonDao().updateEntity(source);
			}
		}
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());
		if (flag == BoolValue.YES)
		{
			serviceFactory.getCommonService().audit(BillType.OUTSOURCE_OP, order.getId(), BoolValue.YES);
		}
		return order;
	}

	@Override
	@Transactional
	public OutSourceProcess update(OutSourceProcess order)
	{
		// 代工平台的公司id
		String originCompanyId = order.getOriginCompanyId();
		BoolValue flag = order.getIsCheck();
		order.setIsCheck(BoolValue.NO);
		OutSourceProcess old_order = serviceFactory.getOutSourceProcessService().lock(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		Map<Long, OutSourceProcessDetail> old_detail_map = ConverterUtils.list2Map(old_order.getDetailList(), "id");
		
		for (OutSourceProcessDetail new_detail : order.getDetailList())
		{
			OutSourceProcessDetail old_detail = old_detail_map.get(new_detail.getId());

			if (new_detail.getId() == null)
			{// 新增
				new_detail.setMasterId(order.getId());
				// 来自代工平台
				new_detail.setOriginCompanyId(originCompanyId);

				daoFactory.getCommonDao().saveEntity(new_detail);
			}
			else
			{
				if (old_detail.getType() == OutSourceType.PROCESS)
				{// 工序发外
					WorkProcedure source = daoFactory.getCommonDao().lockObject(WorkProcedure.class, old_detail.getSourceDetailId());
					source.setOutOfQty(source.getOutOfQty().subtract(old_detail.getQty()).add(new_detail.getQty()));
					daoFactory.getCommonDao().updateEntity(source);
				}
				else
				{// 整单发外
					WorkProduct source = daoFactory.getCommonDao().lockObject(WorkProduct.class, old_detail.getSourceDetailId());
					source.setOutOfQty(source.getOutOfQty() - ((old_detail.getQty().subtract(new_detail.getQty()))).intValue());
					daoFactory.getCommonDao().updateEntity(source);
				}
				PropertyClone.copyProperties(old_detail, new_detail, false, null, new String[] { "memo", "style", "processRequire" });// 替换成新内容
				// 来自代工平台
				old_detail.setOriginCompanyId(originCompanyId);
				daoFactory.getCommonDao().updateEntity(old_detail);// 更新子表
			}
		}
		// 删除记录
		Map<String,List<OutSourceProcessDetail>> resultMap = ServiceHelper.filterCUD(old_order.getDetailList(), order.getDetailList());
		List<OutSourceProcessDetail> listDel = resultMap.get(ServiceHelper.Cud.D);
		for (OutSourceProcessDetail detail : listDel)
		{
			// 反写发外订单的发外数量
			if (detail.getType() == OutSourceType.PROCESS)
			{// 工序发外
				WorkProcedure source = daoFactory.getCommonDao().lockObject(WorkProcedure.class, detail.getSourceDetailId());
				source.setOutOfQty(source.getOutOfQty().subtract(detail.getQty()));
			} else
			{// 整单发外
				WorkProduct source = daoFactory.getCommonDao().lockObject(WorkProduct.class, detail.getSourceDetailId());
				source.setOutOfQty(source.getOutOfQty() - detail.getQty().intValue());
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(listDel);
		
		PropertyClone.copyProperties(old_order, order, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "supplierAddress" });// 替换新内容
		order.setUpdateName(UserUtils.getUserName());
		order.setUpdateTime(new Date());
		// 来自代工平台
		old_order.setOriginCompanyId(originCompanyId);
		// 必须先保存子表，然后再保存主表
		daoFactory.getCommonDao().updateEntity(old_order);// 更新主表
		if (flag == BoolValue.YES)
		{
			serviceFactory.getCommonService().audit(BillType.OUTSOURCE_OP, order.getId(), BoolValue.YES);
		}
		return order;
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		OutSourceProcess order = this.lock(id);
		List<OutSourceProcessDetail> detailList = order.getDetailList();

		// daoFactory.getCommonDao().deleteEntity(OutSourceProcess.class, id);
		// List<OutSourceProcessDetail> detailList = getDetailList(id);
		for (OutSourceProcessDetail detail : detailList)
		{
			if (detail.getType() == OutSourceType.PROCESS)
			{// 工序发外
				WorkProcedure source = daoFactory.getCommonDao().lockObject(WorkProcedure.class, detail.getSourceDetailId());
				if (source != null)
				{
					source.setOutOfQty(source.getOutOfQty().subtract(detail.getQty()));
					daoFactory.getCommonDao().updateEntity(source);
				}
			}
			else
			{// 整单发外
				WorkProduct source = daoFactory.getCommonDao().lockObject(WorkProduct.class, detail.getSourceDetailId());
				if (source != null)
				{
					source.setOutOfQty(source.getOutOfQty() - detail.getQty().intValue());
					daoFactory.getCommonDao().updateEntity(source);
				}
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(detailList);
		daoFactory.getCommonDao().deleteEntity(order);
	}

	@Override
	@Transactional
	public boolean checkAll()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(OutSourceProcess.class);
			query.eq("isCheck", BoolValue.NO);
			query.eq("isForceComplete", BoolValue.NO);
			Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
			if (employes.length > 0)
			{
				query.inArray("supplierId", employes);
			}
			List<OutSourceProcess> orderlList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OutSourceProcess.class);
			for (OutSourceProcess order : orderlList)
			{
				order.setIsCheck(BoolValue.YES);
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public SearchResult<OutSourceProcess> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceProcess.class, "o");
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
		// 代工平台的公司id
		if (StringUtils.isNotBlank(queryParam.getOriginCompanyId()))
		{
			query.eq("o.originCompanyId", queryParam.getOriginCompanyId());
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OutSourceProcess.class);
	}

	@Override
	public SearchResult<OutSourceProcessDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceProcessDetail.class, "a");
		query.createAlias(OutSourceProcess.class, "b");
		query.addProjection(Projections.property("a,b"));
		query.createAlias(Supplier.class, "c");
		query.eqProperty("a.masterId", "b.id");
		query.eqProperty("c.id", "b.supplierId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}

		if (queryParam.getDeliverDateMin() != null && queryParam.getDeliverDateMax() != null)
		{
			query.between("b.deliveryTime", queryParam.getDeliverDateMin(), queryParam.getDeliverDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("a.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
		{
			query.like("a.sourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
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
		if (StringUtils.isNotBlank(queryParam.getPartName()))
		{
			query.like("a.partName", "%" + queryParam.getPartName() + "%");
		}

		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<OutSourceProcessDetail> result = new SearchResult<OutSourceProcessDetail>();
		result.setResult(new ArrayList<OutSourceProcessDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OutSourceProcessDetail detail = (OutSourceProcessDetail) c[0];
			// 查询返写对账数量
			serviceFactory.getOutSourceArriveService().coutReturnReconcil(detail);
			detail.setMaster((OutSourceProcess) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<OutSourceProcessDetail> findForTransmitArrive(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceProcessDetail.class, "a");
		query.createAlias(OutSourceProcess.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, "s");
			query.eqProperty("s.id", "b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.addProjection(Projections.property("a,b"));
		query.eqProperty("a.masterId", "b.id");

		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("a.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("a.procedureName", "%" + queryParam.getProcedureName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("b.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (queryParam.getDeliverDateMin() != null)
		{
			query.ge("b.deliveryTime", queryParam.getDeliverDateMin());
		}
		if (queryParam.getDeliverDateMax() != null)
		{
			query.le("b.deliveryTime", queryParam.getDeliverDateMax());
		}
		if (queryParam.getCompleteFlag() == BoolValue.NO)
		{// 搜索非强制完工
			query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
			query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		}
		else
		{// 搜索已强制完工
			query.eq("a.isForceComplete", BoolValue.YES);

		}
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.add(Restrictions.gtProperty("a.qty", "a.arriveQty"));// 加工数>到货数

		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<OutSourceProcessDetail> result = new SearchResult<OutSourceProcessDetail>();
		result.setResult(new ArrayList<OutSourceProcessDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OutSourceProcessDetail detail = (OutSourceProcessDetail) c[0];
			detail.setMaster((OutSourceProcess) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public List<OutSourceProcess> getYearsFromOutSourceProcess()
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrder.class);
		query.addProjection(Projections.property("distinct DATE_FORMAT(createTime,'%Y') as name,DATE_FORMAT(createTime,'%Y') as value"));
		query.eq("isCheck", BoolValue.YES);
		query.eq("companyId", UserUtils.getCompanyId());
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OutSourceProcess.class);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SumVo> sumOutsourceBySupplier(QueryParam queryParam, String type)
	{
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(OutSourceProcessDetail.class, "d");
		query.addProjection(Projections.property("d.id," + (type.equals("name") ? "a.supplierName as name," : type.equals("class") ? "cc.name as name," : "") + "SUM(d.money) as 'sumMoney' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-01' THEN d.money ELSE 0 END ) as 'january' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-02' THEN d.money ELSE 0 END ) as 'february' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-03' THEN d.money ELSE 0 END ) as 'march' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-04' THEN d.money ELSE 0 END ) as 'april' ,"
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-05' THEN d.money ELSE 0 END ) as 'may' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-06' THEN d.money ELSE 0 END ) as 'june' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-07' THEN d.money ELSE 0 END ) as 'july' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-08' THEN d.money ELSE 0 END ) as 'august' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-09' THEN d.money ELSE 0 END ) as 'september' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear()
				+ "-10' THEN d.money ELSE 0 END ) as 'october' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-11' THEN d.money ELSE 0 END ) as 'november' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-12' THEN d.money ELSE 0 END ) as 'december'"));
		query.createAlias(OutSourceProcess.class, JoinType.LEFTJOIN, "a", "a.id=d.masterId");
		if (type.equals("class"))
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "c", "c.id=a.supplierId");
			Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
			if (employes.length > 0)
			{
				query.inArray("c.employeeId", employes);
			}
			query.createAlias(SupplierClass.class, JoinType.LEFTJOIN, "cc", "cc.id=c.supplierClassId");
		}
		else
		{
			Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
			if (employes.length > 0)
			{
				query.createAlias(Supplier.class, "s");
				query.eqProperty("s.id", "a.supplierId");
				query.inArray("s.employeeId", employes);
			}
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("a.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (queryParam.getSupplierClassId() != null)
		{
			query.eq("cc.id", queryParam.getSupplierClassId());
		}
		if (type.equals("name"))
		{
			query.isNotNull("a.supplierName");
		}
		else if (type.equals("class"))
		{
			query.isNotNull("a.supplierId");
		}
		query.eq("a.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("DATE_FORMAT(a.createTime,'%Y')", queryParam.getYear());
		if (type.equals("name"))
		{
			// 按加工商名称
			query.addGourp("a.supplierId");
		}
		else if (type.equals("class"))
		{
			// 按加工商分类
			query.addGourp("cc.id");
		}
		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);

		List<SumVo> list = new ArrayList<SumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SumVo vo = new SumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SumVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			list.add(vo);
		}
		result.setResult(list);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SearchResult<SumVo> sumOutsourceByProcedure(QueryParam queryParam, String type)
	{
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(OutSourceProcessDetail.class, "d");
		query.addProjection(Projections.property("d.id," + (type.equals("name") ? "d.procedureName as name," : type.equals("class") ? "cc.name as name," : "") + "SUM(d.money) as 'sumMoney' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-01' THEN d.money ELSE 0 END ) as 'january' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-02' THEN d.money ELSE 0 END ) as 'february' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-03' THEN d.money ELSE 0 END ) as 'march' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-04' THEN d.money ELSE 0 END ) as 'april' ,"
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-05' THEN d.money ELSE 0 END ) as 'may' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-06' THEN d.money ELSE 0 END ) as 'june' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-07' THEN d.money ELSE 0 END ) as 'july' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-08' THEN d.money ELSE 0 END ) as 'august' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-09' THEN d.money ELSE 0 END ) as 'september' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear()
				+ "-10' THEN d.money ELSE 0 END ) as 'october' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-11' THEN d.money ELSE 0 END ) as 'november' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-12' THEN d.money ELSE 0 END ) as 'december'"));
		query.createAlias(OutSourceProcess.class, JoinType.LEFTJOIN, "a", "a.id=d.masterId");
		if (type.equals("class"))
		{
			query.createAlias(Procedure.class, JoinType.LEFTJOIN, "c", "c.id=d.procedureId");
			Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
			if (employes.length > 0)
			{
				query.inArray("c.employeeId", employes);
			}
			query.createAlias(ProcedureClass.class, JoinType.LEFTJOIN, "cc", "cc.id=c.procedureClassId");
		}
		else
		{
			Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
			if (employes.length > 0)
			{
				query.createAlias(Supplier.class, "s");
				query.eqProperty("s.id", "a.supplierId");
				query.inArray("s.employeeId", employes);
			}
		}
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("d.procedureName", "%" + queryParam.getProcedureName() + "%");
		}
		if (queryParam.getProcedureClassId() != null)
		{
			query.eq("cc.id", queryParam.getProcedureClassId());
		}
		if (type.equals("name"))
		{
			query.isNotNull("d.procedureName");
		}
		else if (type.equals("class"))
		{
			query.isNotNull("d.procedureId");
		}
		query.eq("a.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("DATE_FORMAT(a.createTime,'%Y')", queryParam.getYear());
		if (type.equals("name"))
		{
			// 按工序名称
			query.addGourp("d.procedureId");
		}
		else if (type.equals("class"))
		{
			// 按工序分类
			query.addGourp("cc.id");
		}
		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);

		List<SumVo> list = new ArrayList<SumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SumVo vo = new SumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SumVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			list.add(vo);
		}
		result.setResult(list);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SearchResult<SumVo> sumOutsourceByType(QueryParam queryParam)
	{
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(OutSourceProcessDetail.class, "d");
		query.addProjection(Projections.property("d.id,d.type as name," + "SUM(d.money) as 'sumMoney' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-01' THEN d.money ELSE 0 END ) as 'january' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-02' THEN d.money ELSE 0 END ) as 'february' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-03' THEN d.money ELSE 0 END ) as 'march' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-04' THEN d.money ELSE 0 END ) as 'april' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear()
				+ "-05' THEN d.money ELSE 0 END ) as 'may' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-06' THEN d.money ELSE 0 END ) as 'june' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-07' THEN d.money ELSE 0 END ) as 'july' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-08' THEN d.money ELSE 0 END ) as 'august' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-09' THEN d.money ELSE 0 END ) as 'september' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-10' THEN d.money ELSE 0 END ) as 'october' ,"
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-11' THEN d.money ELSE 0 END ) as 'november' ," + "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-12' THEN d.money ELSE 0 END ) as 'december'"));
		query.createAlias(OutSourceProcess.class, JoinType.LEFTJOIN, "a", "a.id=d.masterId");
		if (queryParam.getOutSourceType() != null)
		{
			query.eq("d.type", queryParam.getOutSourceType());
		}
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, "s");
			query.eqProperty("s.id", "a.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.isNotNull("d.type");
		query.eq("a.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("DATE_FORMAT(a.createTime,'%Y')", queryParam.getYear());
		query.addGourp("d.type");
		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);

		List<SumVo> list = new ArrayList<SumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SumVo vo = new SumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SumVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			list.add(vo);
		}
		result.setResult(list);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getCompleteQty(String billNo, Long productId)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceProcessDetail.class);
		query.addProjection(Projections.property("ifnull(sum(qty),0)"));
		query.eq("workBillNo", billNo);
		query.eq("productId", productId);
		query.eq("isForceComplete", BoolValue.YES);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return new BigDecimal(mapResult.getResult().get(0).get("ifnull(sum(qty),0)").toString()).intValue();
	}

	@Override
	@Transactional
	public void changePrice(OutSourceProcessDetail outSourceProcessDetail) throws Exception
	{
		/**
		 * 1. 修改发外加工明细的单价、金额
		 * 2. 修改发外加工主表的单价、金额、交期日期
		 * 3. 修改发外明细的下游单据的单价，金额，税额，不含税金额，不含税单价也需要重新计算并保存（发外到货，发外退货，发外对账）
		 *    3.1 修改发外到货
		 *      3.1.1 线路1 - 修改发外对账
		 *      3.1.2 线路2 - 修改发外退货、发外退货对账
		 *      3.1.3 修改库存金额、成品出库日志（成品发外才有）
		 */
		OutSourceProcessDetail detail = daoFactory.getCommonDao().getEntity(OutSourceProcessDetail.class, outSourceProcessDetail.getId());
		if (detail != null)
		{
			// 1. 修改发外加工明细的单价、金额、交期日期
			OutSourceProcessDetail detailTmp = (OutSourceProcessDetail) BeanUtils.cloneBean(detail);
			// 单价(含税)
			detail.setPrice(outSourceProcessDetail.getPrice());
			// 金额（含税）
			detail.setMoney(outSourceProcessDetail.getMoney());
			// 不含税金额
			BigDecimal noTaxMoney = outSourceProcessDetail.getMoney().divide(new BigDecimal(1d + detail.getTaxRatePercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
			// 税额
			BigDecimal tax = detail.getMoney().subtract(noTaxMoney);
			// 不含税单价
			BigDecimal noTaxPrice = noTaxMoney.divide(detail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
			detail.setNoTaxMoney(noTaxMoney);
			detail.setNoTaxPrice(noTaxPrice);
			detail.setTax(tax);

			// 2. 修改发外主表的单价、金额、交期日期
			OutSourceProcess outSourceProcess = daoFactory.getCommonDao().getEntity(OutSourceProcess.class, detail.getMasterId());
			/**
			 * 总金额算法：
			 *   算法1： 新价格-原价格+总价格（【使用中】）
			 *   算法2： 总价格-原价格+新价格
			 */
			BigDecimal totalMoney = detail.getMoney().subtract(detailTmp.getMoney()).add(outSourceProcess.getTotalMoney());
			BigDecimal totalTax = detail.getTax().subtract(detailTmp.getTax()).add(outSourceProcess.getTotalTax());
			BigDecimal noTaxTotalMoney = detail.getNoTaxMoney().subtract(detailTmp.getNoTaxMoney()).add(outSourceProcess.getNoTaxTotalMoney());
			outSourceProcess.setTotalMoney(totalMoney);
			outSourceProcess.setNoTaxTotalMoney(noTaxTotalMoney);
			outSourceProcess.setTotalTax(totalTax);
			// 修改交期日期
			if (null != outSourceProcessDetail.getMaster())
			{
				outSourceProcess.setDeliveryTime(outSourceProcessDetail.getMaster().getDeliveryTime());
			}
			daoFactory.getCommonDao().updateEntity(outSourceProcess);

			// 3. 修改发外明细的下游单据的单价，金额，税额，不含税金额，不含税单价也需要重新计算并保存（发外入库，发外退货，发外对账）
			// 3.1 修改发外到货
			// 发外明细到货金额
			BigDecimal detailArriveMoney = new BigDecimal(0);
			DynamicQuery queryArrive = new CompanyDynamicQuery(OutSourceArriveDetail.class);
			// queryArrive.eq("sourceBillType", BillType.OUTSOURCE_OP);
			queryArrive.eq("sourceDetailId", outSourceProcessDetail.getId());
			List<OutSourceArriveDetail> outSourceArriveDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryArrive, OutSourceArriveDetail.class);
			if (CollectionUtils.isNotEmpty(outSourceArriveDetailList))
			{
				for (OutSourceArriveDetail outSourceArriveDetail : outSourceArriveDetailList)
				{
					OutSourceArriveDetail outSourceArriveDetailTmp = (OutSourceArriveDetail) BeanUtils.cloneBean(outSourceArriveDetail);

					// 修改发外到货明细
					outSourceArriveDetail.setPrice(outSourceProcessDetail.getPrice());
					outSourceArriveDetail.setMoney(outSourceProcessDetail.getPrice().multiply(outSourceArriveDetail.getQty()));
					// 不含税金额
					BigDecimal noTaxMoney_work = outSourceArriveDetail.getMoney().divide(new BigDecimal(1d + outSourceArriveDetail.getTaxRatePercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
					// 不含税单价
					BigDecimal noTaxPrice_work = noTaxMoney_work.divide(outSourceArriveDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
					// 税额
					BigDecimal tax_work = outSourceArriveDetail.getMoney().subtract(noTaxMoney_work);
					outSourceArriveDetail.setNoTaxMoney(noTaxMoney_work);
					outSourceArriveDetail.setNoTaxPrice(noTaxPrice_work);
					outSourceArriveDetail.setTax(tax_work);

					detailArriveMoney = detailArriveMoney.add(outSourceArriveDetail.getMoney());

					// 修改发外到货主表
					OutSourceArrive outSourceArrive = daoFactory.getCommonDao().getEntity(OutSourceArrive.class, outSourceArriveDetail.getMasterId());
					BigDecimal _totalMoney = outSourceArriveDetail.getMoney().subtract(outSourceArriveDetailTmp.getMoney()).add(outSourceArrive.getTotalMoney());
					BigDecimal _totalTax = outSourceArriveDetail.getTax().subtract(outSourceArriveDetailTmp.getTax()).add(outSourceArrive.getTotalTax());
					BigDecimal _noTaxTotalMoney = outSourceArriveDetail.getNoTaxMoney().subtract(outSourceArriveDetailTmp.getNoTaxMoney()).add(outSourceArrive.getNoTaxTotalMoney());
					outSourceArrive.setTotalMoney(_totalMoney);
					outSourceArrive.setNoTaxTotalMoney(_noTaxTotalMoney);
					outSourceArrive.setTotalTax(_totalTax);
					daoFactory.getCommonDao().updateEntity(outSourceArrive);

					// 3.1.1 线路1 - 修改发外对账
					// 发外到货已对账金额
					BigDecimal arriveReconcilMoney = new BigDecimal(0);
					DynamicQuery queryReconcil2 = new CompanyDynamicQuery(OutSourceReconcilDetail.class);
					// queryReconcil.eq("sourceBillType", BillType.OUTSOURCE_OC);
					queryReconcil2.eq("sourceDetailId", outSourceArriveDetail.getId());
					List<OutSourceReconcilDetail> outSourceReconcilDetailList2 = daoFactory.getCommonDao().findEntityByDynamicQuery(queryReconcil2, OutSourceReconcilDetail.class);
					if (CollectionUtils.isNotEmpty(outSourceReconcilDetailList2))
					{
						for (OutSourceReconcilDetail reconcilDetail : outSourceReconcilDetailList2)
						{
							OutSourceReconcilDetail reconcilDetailTmp = (OutSourceReconcilDetail) BeanUtils.cloneBean(reconcilDetail);

							reconcilDetail.setPrice(outSourceProcessDetail.getPrice());
							reconcilDetail.setMoney(outSourceProcessDetail.getPrice().multiply(reconcilDetail.getQty()));
							// 不含税金额
							BigDecimal noTaxMoney_reconcil = reconcilDetail.getMoney().divide(new BigDecimal(1d + detail.getTaxRatePercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
							// 不含税单价
							BigDecimal noTaxPrice_reconcil = noTaxMoney_reconcil.divide(reconcilDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
							// 税额
							BigDecimal tax_reconcil = reconcilDetail.getMoney().subtract(noTaxMoney_reconcil);
							reconcilDetail.setNoTaxMoney(noTaxMoney_reconcil);
							reconcilDetail.setNoTaxPrice(noTaxPrice_reconcil);
							reconcilDetail.setTax(tax_reconcil);
							daoFactory.getCommonDao().updateEntity(reconcilDetail);

							// 发外到货已对账金额累加
							arriveReconcilMoney = arriveReconcilMoney.add(reconcilDetail.getMoney());

							// 修改发外对账主表数据
							OutSourceReconcil outSourceReconcil = daoFactory.getCommonDao().getEntity(OutSourceReconcil.class, reconcilDetail.getMasterId());
							BigDecimal totalMoney_reconcil = reconcilDetail.getMoney().subtract(reconcilDetailTmp.getMoney()).add(outSourceReconcil.getTotalMoney());
							BigDecimal totalTax_reconcil = reconcilDetail.getTax().subtract(reconcilDetailTmp.getTax()).add(outSourceReconcil.getTotalTax());
							BigDecimal noTaxTotalMoney_reconcil = reconcilDetail.getNoTaxMoney().subtract(reconcilDetailTmp.getNoTaxMoney()).add(outSourceReconcil.getNoTaxTotalMoney());
							outSourceReconcil.setTotalMoney(totalMoney_reconcil);
							outSourceReconcil.setNoTaxTotalMoney(noTaxTotalMoney_reconcil);
							outSourceReconcil.setTotalTax(totalTax_reconcil);
							daoFactory.getCommonDao().updateEntity(outSourceReconcil);
						}
					}

					// 3.1.2 线路2 - 修改发外退货、发外退货对账
					// 发外到货已退货金额
					BigDecimal arriveRefundMoney = new BigDecimal(0); 
					BigDecimal arriveRefundMoney2 = new BigDecimal(0);
					DynamicQuery queryRefund = new CompanyDynamicQuery(OutSourceReturnDetail.class);
					// queryRefund.eq("sourceBillType", BillType.OUTSOURCE_OR);
					queryRefund.eq("sourceDetailId", outSourceArriveDetail.getId());
					List<OutSourceReturnDetail> outSourceReturnDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryRefund, OutSourceReturnDetail.class);
					if (CollectionUtils.isNotEmpty(outSourceReturnDetailList))
					{
						for (OutSourceReturnDetail refundDetail : outSourceReturnDetailList)
						{
							// 修改发外退货明细
							OutSourceReturnDetail refundDetailTmp = (OutSourceReturnDetail) BeanUtils.cloneBean(refundDetail);

							refundDetail.setPrice(outSourceProcessDetail.getPrice());
							refundDetail.setMoney(outSourceProcessDetail.getPrice().multiply(refundDetail.getQty()));
							// 不含税金额
							BigDecimal noTaxMoney_return = refundDetail.getMoney().divide(new BigDecimal(1d + detail.getTaxRatePercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
							// 不含税单价
							BigDecimal noTaxPrice_return = noTaxMoney_return.divide(refundDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
							// 税额
							BigDecimal tax_return = refundDetail.getMoney().subtract(noTaxMoney_return);
							refundDetail.setNoTaxMoney(noTaxMoney_return);
							refundDetail.setNoTaxPrice(noTaxPrice_return);
							refundDetail.setTax(tax_return);
							
							// 相加：退货金额=当前金额-历史金额
							if (refundDetail.getPrice().compareTo(refundDetailTmp.getPrice()) > 0)
							{
								arriveRefundMoney2 = arriveRefundMoney2.add(refundDetail.getMoney().subtract(refundDetailTmp.getMoney()));
							}
							// 相减：退货金额=历史金额-当前金额
							else if (refundDetail.getPrice().compareTo(refundDetailTmp.getPrice()) < 0)
							{
								arriveRefundMoney2 = arriveRefundMoney2.add(refundDetailTmp.getMoney().subtract(refundDetail.getMoney()));
							}
							// 用于发外到货明细的 到货金额
							arriveRefundMoney = arriveRefundMoney.add(refundDetail.getMoney());
							// 用法订单明细已到货金额 = 发外到货金额 - 发外拖货金额
							detailArriveMoney = detailArriveMoney.subtract(refundDetail.getMoney());

							// 修改发外退货主表数据
							OutSourceReturn outSourceReturn = daoFactory.getCommonDao().getEntity(OutSourceReturn.class, refundDetail.getMasterId());
							BigDecimal totalMoney_return = refundDetail.getMoney().subtract(refundDetailTmp.getMoney()).add(outSourceReturn.getTotalMoney());
							BigDecimal totalTax_return = refundDetail.getTax().subtract(refundDetailTmp.getTax()).add(outSourceReturn.getTotalTax());
							BigDecimal noTaxTotalMoney_return = refundDetail.getNoTaxMoney().subtract(refundDetailTmp.getNoTaxMoney()).add(outSourceReturn.getNoTaxTotalMoney());
							outSourceReturn.setTotalMoney(totalMoney_return);
							outSourceReturn.setNoTaxTotalMoney(noTaxTotalMoney_return);
							outSourceReturn.setTotalTax(totalTax_return);
							daoFactory.getCommonDao().updateEntity(outSourceReturn);

							// 发外对账-退货
							// 发外退货已对账金额
							BigDecimal reconcilMoney = new BigDecimal(0);
							DynamicQuery queryReconcil = new CompanyDynamicQuery(OutSourceReconcilDetail.class);
							// queryReconcil.eq("sourceBillType", BillType.OUTSOURCE_OC);
							queryReconcil.eq("sourceDetailId", refundDetail.getId());
							List<OutSourceReconcilDetail> outSourceReconcilDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryReconcil, OutSourceReconcilDetail.class);
							if (CollectionUtils.isNotEmpty(outSourceReconcilDetailList))
							{
								for (OutSourceReconcilDetail reconcilDetail : outSourceReconcilDetailList)
								{
									OutSourceReconcilDetail outSourceReconcilDetailTmp = (OutSourceReconcilDetail) BeanUtils.cloneBean(reconcilDetail);

									reconcilDetail.setPrice(outSourceProcessDetail.getPrice());
									reconcilDetail.setMoney(outSourceProcessDetail.getPrice().multiply(reconcilDetail.getQty()));
									// 不含税金额
									BigDecimal noTaxMoney_reconcil = reconcilDetail.getMoney().divide(new BigDecimal(1d + detail.getTaxRatePercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
									// 不含税单价
									BigDecimal noTaxPrice_reconcil = noTaxMoney_reconcil.divide(reconcilDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
									// 税额
									BigDecimal tax_reconcil = reconcilDetail.getMoney().subtract(noTaxMoney_reconcil);
									reconcilDetail.setNoTaxMoney(noTaxMoney_reconcil);
									reconcilDetail.setNoTaxPrice(noTaxPrice_reconcil);
									reconcilDetail.setTax(tax_reconcil);
									daoFactory.getCommonDao().updateEntity(reconcilDetail);

									// 发外退货已对账金额累加
									reconcilMoney = reconcilMoney.add(reconcilDetail.getMoney());

									// 修改发外对账主表数据
									OutSourceReconcil outSourceReconcil = daoFactory.getCommonDao().getEntity(OutSourceReconcil.class, reconcilDetail.getMasterId());
									BigDecimal totalMoney_reconcil = reconcilDetail.getMoney().subtract(outSourceReconcilDetailTmp.getMoney()).add(outSourceReconcil.getTotalMoney());
									BigDecimal totalTax_reconcil = reconcilDetail.getTax().subtract(outSourceReconcilDetailTmp.getTax()).add(outSourceReconcil.getTotalTax());
									BigDecimal noTaxTotalMoney_reconcil = reconcilDetail.getNoTaxMoney().subtract(outSourceReconcilDetailTmp.getNoTaxMoney()).add(outSourceReconcil.getNoTaxTotalMoney());
									outSourceReconcil.setTotalMoney(totalMoney_reconcil);
									outSourceReconcil.setNoTaxTotalMoney(noTaxTotalMoney_reconcil);
									outSourceReconcil.setTotalTax(totalTax_reconcil);
									daoFactory.getCommonDao().updateEntity(outSourceReconcil);
								}
							}

							// 发外退货已对账金额
							refundDetail.setReconcilMoney(reconcilMoney.abs());
							daoFactory.getCommonDao().updateEntity(refundDetail);
							// 修改成品出库日志
							DynamicQuery query = new CompanyDynamicQuery(StockProductLog.class);
							query.eq("billNo", outSourceReturn.getBillNo());
							query.eq("sourceId", refundDetail.getId());
							query.eq("productId", refundDetail.getProductId());
							query.eq("warehouseId", refundDetail.getWarehouseId());
							if (refundDetail.getStyle() == null || "".equals(refundDetail.getStyle()))
							{
								query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
							}
							else
							{
								query.eq("specifications", refundDetail.getStyle());
							}
							// getByDynamicQuery自动更新数据
							StockProductLog stockProductLog_ = daoFactory.getCommonDao().getByDynamicQuery(query, StockProductLog.class);
							if (null != stockProductLog_)
							{
								// 金额
								stockProductLog_.setOutMoney(refundDetail.getMoney());
								// 单价
								stockProductLog_.setPrice(refundDetail.getPrice());
							}
						}
					}

					// 发外到货已对账金额
					outSourceArriveDetail.setReconcilMoney(arriveReconcilMoney);
					// 发外到货已退货金额
					outSourceArriveDetail.setReturnMoney(arriveRefundMoney);
					daoFactory.getCommonDao().updateEntity(outSourceArriveDetail);

					// 3.1.3 修改库存金额、成品出库日志（成品发外才有）
					if (outSourceArriveDetail.getType() == OutSourceType.PRODUCT)
					{
						// 库存操作
						StockProduct stockProduct = new StockProduct();
						stockProduct.setProductId(outSourceArriveDetail.getProductId());
						stockProduct.setWarehouseId(outSourceArriveDetail.getWarehouseId());
						stockProduct.setPrice(outSourceArriveDetail.getPrice());
						stockProduct.setUpdateTime(new Date());
						// 单价增加
						if (outSourceArriveDetail.getPrice().compareTo(outSourceArriveDetailTmp.getPrice()) > 0)
						{
							stockProduct.setMoney(outSourceArriveDetail.getMoney().subtract(outSourceArriveDetailTmp.getMoney()).subtract(arriveRefundMoney2));
						}
						// 单价相减
						else if (outSourceArriveDetail.getPrice().compareTo(outSourceArriveDetailTmp.getPrice()) < 0)
						{
							stockProduct.setMoney(outSourceArriveDetail.getMoney().subtract(outSourceArriveDetailTmp.getMoney()).add(arriveRefundMoney2));
						}
						else
						{
							 stockProduct.setMoney(new BigDecimal(0));
						}
						
						serviceFactory.getStockProductService().changePrice(stockProduct);
						// 修改成品出库日志
						DynamicQuery query = new CompanyDynamicQuery(StockProductLog.class);
						query.eq("billNo", outSourceArrive.getBillNo());
						query.eq("sourceId", outSourceArriveDetail.getId());
						query.eq("productId", stockProduct.getProductId());
						query.eq("warehouseId", stockProduct.getWarehouseId());
						if (outSourceArriveDetail.getStyle() == null || "".equals(outSourceArriveDetail.getStyle()))
						{
							query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
						}
						else
						{
							query.eq("specifications", outSourceArriveDetail.getStyle());
						}
						// getByDynamicQuery自动更新数据
						StockProductLog stockProductLog_ = daoFactory.getCommonDao().getByDynamicQuery(query, StockProductLog.class);
						if (null != stockProductLog_)
						{
							// 金额
							stockProductLog_.setInMoney(outSourceArriveDetail.getMoney());
							// 单价
							stockProductLog_.setPrice(outSourceArriveDetail.getPrice());
						}
						// daoFactory.getCommonDao().updateEntity(stockProductLog_);
					}
				}
			}

			// 发外明细到货金额
			detail.setArriveMoney(detailArriveMoney);
			daoFactory.getCommonDao().updateEntity(detail);
		}
	}
}
