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
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ConverterUtils;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.helper.ServiceHelper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArrive;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkProcedure;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.produce.WorkReportTask;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.persist.enumerate.OutSourceType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.outsource.OutSourceArriveService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 发外管理 - 发外到货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午4:51:20, zhengby, 代码规范
 */
@Service
public class OutSourceArriveServiceImpl extends BaseServiceImpl implements OutSourceArriveService
{

	@Override
	public OutSourceArrive get(Long id)
	{

		DynamicQuery query = new CompanyDynamicQuery(OutSourceArrive.class);
		query.eq("id", id);
		OutSourceArrive order = daoFactory.getCommonDao().getByDynamicQuery(query, OutSourceArrive.class);

		// OutSourceArrive order = daoFactory.getCommonDao().getEntity(OutSourceArrive.class, id);
		order.setDetailList(this.getDetailList(id));
		return order;
	}

	@Override
	public OutSourceArriveDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceArriveDetail.class);
		query.eq("id", id);
		OutSourceArriveDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, OutSourceArriveDetail.class);

		// OutSourceArriveDetail detail = daoFactory.getCommonDao().getEntity(OutSourceArriveDetail.class, id);
		detail.setMaster(daoFactory.getCommonDao().getEntity(OutSourceArrive.class, detail.getMasterId()));
		return detail;
	}

	@Override
	public OutSourceArrive lock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceArrive.class);
		query.eq("id", id);
		OutSourceArrive order = daoFactory.getCommonDao().lockByDynamicQuery(query, OutSourceArrive.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(OutSourceArriveDetail.class);
		query_detail.eq("masterId", id);
		List<OutSourceArriveDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, OutSourceArriveDetail.class, LockType.LOCK_WAIT);

		/*OutSourceArrive order = daoFactory.getCommonDao().lockObject(OutSourceArrive.class, id);
		List<OutSourceArriveDetail> detailList = this.getDetailList(id);
		for (OutSourceArriveDetail detail : detailList)
		{
			detail = daoFactory.getCommonDao().lockObject(OutSourceArriveDetail.class, detail.getId());
		}*/
		order.setDetailList(detailList);
		return order;
	}

	public List<OutSourceArriveDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceArriveDetail.class);
		query.eq("masterId", id);
		List<OutSourceArriveDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OutSourceArriveDetail.class);
		return detailList;
	}

	@Override
	@Transactional
	public OutSourceArrive save(OutSourceArrive order)
	{
		BoolValue flag = order.getIsCheck();
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.OUTSOURCE_OA);
		order.setBillNo(UserUtils.createBillNo(BillType.OUTSOURCE_OA));
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
		for (OutSourceArriveDetail detail : order.getDetailList())
		{
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setMasterId(order.getId());
			detail.setReconcilQty(new BigDecimal(0));
			detail.setReconcilMoney(new BigDecimal(0));
			detail.setReturnQty(new BigDecimal(0));
			detail.setReturnMoney(new BigDecimal(0));
			detail.setIsForceComplete(BoolValue.NO);

			TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), detail.getTaxRateId());
			detail.setTaxRateName(taxRate.getName());
			detail.setTaxRatePercent(taxRate.getPercent());
			detail.setUserNo(UserUtils.getUser().getUserNo());
			// 反写加工单到货数量
			OutSourceProcessDetail source = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class, detail.getSourceDetailId());
			source.setArriveQty(source.getArriveQty().add(detail.getQty()));
			source.setArriveMoney(source.getArriveMoney().add(detail.getMoney()));
			daoFactory.getCommonDao().updateEntity(source);
			if (source.getType() == OutSourceType.PROCESS)
			{// 工序发外
				WorkProcedure sourceProcedure = daoFactory.getCommonDao().lockObject(WorkProcedure.class, source.getSourceDetailId());
				sourceProcedure.setArriveOfQty(source.getArriveQty());
				// 【FIX BUG 生成到货单的时候进行生产进度任务对象反写】
				BigDecimal sum = detail.getQty();
				updateWorkReportTask(detail.getWorkBillNo(), detail.getProcedureCode(), detail.getPartName(), sum);
				daoFactory.getCommonDao().updateEntity(sourceProcedure);

			}
			else
			{
				DynamicQuery queryWork = new CompanyDynamicQuery(Work.class);
				queryWork.eq("billNo", detail.getWorkBillNo());
				Work work = daoFactory.getCommonDao().getByDynamicQuery(queryWork, Work.class);
				DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class);
				query.eq("productId", detail.getProductId());
				query.eq("masterId", work.getId());
				WorkProduct workProduct = daoFactory.getCommonDao().getByDynamicQuery(query, WorkProduct.class);
				workProduct.setInStockQty(workProduct.getInStockQty() +  detail.getQty().intValue());
			}

		}
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());
		if (flag == BoolValue.YES)
		{
			this.check(order.getId(), BoolValue.YES);
		}
		return order;
	}

	@Override
	@Transactional
	public OutSourceArrive update(OutSourceArrive order)
	{
		BoolValue flag = order.getIsCheck();
		order.setIsCheck(BoolValue.NO);
		OutSourceArrive old_order = serviceFactory.getOutSourceArriveService().lock(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		Map<Long, OutSourceArriveDetail> old_detail_map = ConverterUtils.list2Map(old_order.getDetailList(), "id");

		for (OutSourceArriveDetail new_detail : order.getDetailList())
		{
			OutSourceArriveDetail old_detail = old_detail_map.get(new_detail.getId());
			if (new_detail.getId() == null)
			{// 新增
				new_detail.setCompanyId(UserUtils.getCompanyId());
				new_detail.setMasterId(order.getId());
				new_detail.setReconcilQty(new BigDecimal(0));
				new_detail.setReturnQty(new BigDecimal(0));
				new_detail.setIsForceComplete(BoolValue.NO);
				TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), new_detail.getTaxRateId());
				new_detail.setTaxRateName(taxRate.getName());
				new_detail.setTaxRatePercent(taxRate.getPercent());
				new_detail.setUserNo(UserUtils.getUser().getUserNo());
				// 反写加工单到货数量
				OutSourceProcessDetail source = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class, new_detail.getSourceDetailId());
				source.setArriveQty(source.getArriveQty().add(new_detail.getQty()));
				source.setArriveMoney(source.getArriveMoney().add(new_detail.getMoney()));
				serviceFactory.getPersistService().save(new_detail);
				if (source.getType() == OutSourceType.PROCESS)
				{// 工序发外
					WorkProcedure sourceProcedure = daoFactory.getCommonDao().lockObject(WorkProcedure.class, source.getSourceDetailId());
					sourceProcedure.setArriveOfQty(source.getArriveQty());
					daoFactory.getCommonDao().updateEntity(sourceProcedure);
				}
				else
				{
					DynamicQuery queryWork = new CompanyDynamicQuery(Work.class);
					queryWork.eq("billNo", new_detail.getWorkBillNo());
					Work work = daoFactory.getCommonDao().getByDynamicQuery(queryWork, Work.class);
					DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class);
					query.eq("productId", new_detail.getProductId());
					query.eq("masterId", work.getId());
					WorkProduct workProduct = daoFactory.getCommonDao().getByDynamicQuery(query, WorkProduct.class);
					workProduct.setInStockQty(workProduct.getInStockQty() + new_detail.getQty().intValue());
				}
			}
			else
			{

				/*if (new_detail.getDelFlag() == BoolValue.YES)
				{// 删除
				
					OutSourceProcessDetail source = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class,
							old_detail.getSourceDetailId());
					source.setArriveQty(source.getArriveQty() - old_detail.getQty());
					daoFactory.getCommonDao().updateEntity(source);
				
					serviceFactory.getPersistService().delete(old_detail);
				}
				else
				{*/// 更新
				OutSourceProcessDetail source = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class, old_detail.getSourceDetailId());
				if (source.getType() == OutSourceType.PROCESS)
				{
					BigDecimal old = old_detail.getQty();
					BigDecimal n_ew = new_detail.getQty();
					BigDecimal sum = n_ew.subtract(old) ;
					updateWorkReportTask(new_detail.getWorkBillNo(), new_detail.getProcedureCode(), new_detail.getPartName(), sum);
				}
				source.setArriveQty(source.getArriveQty().subtract((old_detail.getQty().subtract(new_detail.getQty()))));
				source.setArriveMoney(source.getArriveMoney().subtract(old_detail.getMoney()).add(new_detail.getMoney()));

				if (source.getType() == OutSourceType.PROCESS)
				{
					WorkProcedure sourceProcedure = daoFactory.getCommonDao().lockObject(WorkProcedure.class, source.getSourceDetailId());
					sourceProcedure.setArriveOfQty(source.getArriveQty());
					daoFactory.getCommonDao().updateEntity(sourceProcedure);
				}
				else
				{
					// 反写工单产品表已入库数量
					DynamicQuery queryWork = new CompanyDynamicQuery(Work.class);
					queryWork.eq("billNo", new_detail.getWorkBillNo());
					Work work = daoFactory.getCommonDao().getByDynamicQuery(queryWork, Work.class);
					DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class);
					query.eq("productId", new_detail.getProductId());
					query.eq("masterId", work.getId());
					WorkProduct workProduct = daoFactory.getCommonDao().getByDynamicQuery(query, WorkProduct.class);
					workProduct.setInStockQty(workProduct.getInStockQty() - old_detail.getQty().intValue() + new_detail.getQty().intValue());
				}

				daoFactory.getCommonDao().updateEntity(source);

				PropertyClone.copyProperties(old_detail, new_detail, false, null, new String[] { "memo" });// 替换成新内容

				daoFactory.getCommonDao().updateEntity(old_detail);// 更新子表

			}
			// }

		}
		// 删除记录
		Map<String,List<OutSourceArriveDetail>> resultMap = ServiceHelper.filterCUD(old_order.getDetailList(), order.getDetailList());
		List<OutSourceArriveDetail> listDel = resultMap.get(ServiceHelper.Cud.D);
		for (OutSourceArriveDetail detail : listDel)
		{
			OutSourceProcessDetail processDetail = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class, detail.getSourceDetailId());
			processDetail.setArriveQty(processDetail.getArriveQty().subtract(detail.getQty()));
			processDetail.setArriveMoney(processDetail.getArriveMoney().subtract(detail.getMoney()));
			// 反写工单发外到货数量
			if (processDetail.getType() == OutSourceType.PROCESS)
			{// 工序发外
				WorkProcedure source = daoFactory.getCommonDao().lockObject(WorkProcedure.class, processDetail.getSourceDetailId());
				source.setArriveOfQty(source.getArriveOfQty().subtract(processDetail.getQty()));
				daoFactory.getCommonDao().updateEntity(source);
				BigDecimal sum =  new BigDecimal(0).subtract(processDetail.getQty());
				updateWorkReportTask(processDetail.getWorkBillNo(), processDetail.getProcedureCode(), processDetail.getPartName(), sum);
			} else
			{// 整单发外
				// 反写工单产品表已入库数量
				DynamicQuery queryWork = new CompanyDynamicQuery(Work.class);
				queryWork.eq("billNo", processDetail.getWorkBillNo());
				Work work = daoFactory.getCommonDao().getByDynamicQuery(queryWork, Work.class);
				DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class);
				query.eq("productId", processDetail.getProductId());
				query.eq("masterId", work.getId());
				WorkProduct workProduct = daoFactory.getCommonDao().getByDynamicQuery(query, WorkProduct.class);
				workProduct.setInStockQty(workProduct.getInStockQty() - processDetail.getQty().intValue());
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(listDel);
		
		PropertyClone.copyProperties(old_order, order, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "supplierAddress" });// 替换新内容
		order.setUpdateName(UserUtils.getUserName());
		order.setUpdateTime(new Date());
		// 必须先保存子表，然后再保存主表
		daoFactory.getCommonDao().updateEntity(old_order);// 更新主表
		if (flag == BoolValue.YES)
		{
			this.check(order.getId(), BoolValue.YES);
		}
		return order;
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		OutSourceArrive order = this.lock(id);
		List<OutSourceArriveDetail> detailList = order.getDetailList();

		// daoFactory.getCommonDao().deleteEntity(OutSourceArrive.class, id);
		// List<OutSourceArriveDetail> detailList = getDetailList(id);
		for (OutSourceArriveDetail detail : detailList)
		{
			OutSourceProcessDetail source = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class, detail.getSourceDetailId());
			source.setArriveQty(source.getArriveQty().subtract(detail.getQty()));
			source.setArriveMoney(source.getArriveMoney().subtract(detail.getMoney()));
			daoFactory.getCommonDao().updateEntity(source);
			if (source.getType() == OutSourceType.PROCESS)
			{// 工序发外
				WorkProcedure sourceProcedure = daoFactory.getCommonDao().lockObject(WorkProcedure.class, source.getSourceDetailId());
				sourceProcedure.setArriveOfQty(source.getArriveQty());
				daoFactory.getCommonDao().updateEntity(sourceProcedure);
				BigDecimal sum =  new BigDecimal(0).subtract(detail.getQty());
				updateWorkReportTask(detail.getWorkBillNo(), detail.getProcedureCode(), detail.getPartName(), sum);

			}
			else
			{
				DynamicQuery queryWork = new CompanyDynamicQuery(Work.class);
				queryWork.eq("billNo", detail.getWorkBillNo());
				Work work = daoFactory.getCommonDao().getByDynamicQuery(queryWork, Work.class);
				DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class);
				query.eq("productId", detail.getProductId());
				query.eq("masterId", work.getId());
				WorkProduct workProduct = daoFactory.getCommonDao().getByDynamicQuery(query, WorkProduct.class);
				workProduct.setInStockQty(workProduct.getInStockQty() - detail.getQty().intValue());
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(detailList);
		daoFactory.getCommonDao().deleteEntity(order);
	}

	@Override
	public SearchResult<OutSourceArrive> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceArrive.class, "o");
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OutSourceArrive.class);
	}

	@Override
	public SearchResult<OutSourceArriveDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceArriveDetail.class, "a");
		query.createAlias(OutSourceArrive.class, "b");
		query.addProjection(Projections.property("a,b"));
		query.createAlias(Supplier.class, "c");
		query.eqProperty("a.masterId", "b.id");
		query.eqProperty("c.id", "b.supplierId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}

		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
		{
			query.like("a.sourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("a.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
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
		SearchResult<Object[]> temp_result = null;
		try
		{
			temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		SearchResult<OutSourceArriveDetail> result = new SearchResult<OutSourceArriveDetail>();
		result.setResult(new ArrayList<OutSourceArriveDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OutSourceArriveDetail detail = (OutSourceArriveDetail) c[0];
			detail.setMaster((OutSourceArrive) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<OutSourceArriveDetail> findForTransmitReconcil(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceArriveDetail.class, "a");
		query.createAlias(OutSourceArrive.class, "b");
		query.addProjection(Projections.property("a,b"));
		query.eqProperty("a.masterId", "b.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, "s");
			query.eqProperty("s.id", "b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNoneBlank(queryParam.getWorkBillNo()))
		{
			query.like("a.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
		{
			query.like("a.outSourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
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
		query.desc("b.createTime");
		query.add(Restrictions.gtProperty("a.qty", "a.reconcilQty"));// 到货数>对账数

		query.eq("b.isCheck", BoolValue.YES);// 已审核

		query.setIsSearchTotalCount(true);

		query.setPageIndex(queryParam.getPageNumber() == null ? 1 : queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize() == null ? Integer.parseInt(SystemConfigUtil.getConfig(SysConstants.TRANSMIT_ARRIVE_PAGE_SIZE_MAX)) : queryParam.getPageSize());
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		;
		SearchResult<OutSourceArriveDetail> result = new SearchResult<OutSourceArriveDetail>();
		result.setResult(new ArrayList<OutSourceArriveDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OutSourceArriveDetail detail = (OutSourceArriveDetail) c[0];
			detail.setMaster((OutSourceArrive) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	@Transactional
	public boolean check(Long id, BoolValue flag)
	{
		if (serviceFactory.getCommonService().audit(BillType.OUTSOURCE_OA, id, flag))
		{
			// 有库存模块才进行操作
			if (UserUtils.hasCompanyPermission("stock:product:list"))
			{
				OutSourceArrive order = serviceFactory.getOutSourceArriveService().get(id);
				for (OutSourceArriveDetail detail : order.getDetailList())
				{
					if (detail.getType() == OutSourceType.PRODUCT)
					{
						// 库存操作
						StockProduct stockProduct = new StockProduct();
						stockProduct.setProductId(detail.getProductId());
						stockProduct.setWarehouseId(detail.getWarehouseId());
						stockProduct.setPrice(detail.getPrice());
						stockProduct.setQty(detail.getQty().intValue());
						stockProduct.setMoney(detail.getMoney());
						stockProduct.setUpdateTime(new Date());
						if (flag == BoolValue.YES)
						{
							serviceFactory.getStockProductService().stock(stockProduct, InOutType.IN);
						} else
						{
							serviceFactory.getStockProductService().backStock(stockProduct, InOutType.IN);
						}

						if (flag == BoolValue.YES)
						{
							Product product = daoFactory.getCommonDao().getEntity(Product.class, detail.getProductId());
							// 入库记录
							StockProductLog log = new StockProductLog();
							log.setBillId(order.getId());
							log.setBillType(order.getBillType());
							log.setBillNo(order.getBillNo());
							log.setSourceId(detail.getId());
							log.setCreateTime(new Date());
							log.setCompanyId(UserUtils.getCompanyId());
							log.setCode(product.getCode());
							log.setProductClassId(product.getProductClassId());
							log.setCustomerMaterialCode(product.getCustomerMaterialCode());
							log.setProductName(detail.getProductName());
							log.setProductId(detail.getProductId());
							log.setSpecifications(detail.getStyle());
							log.setWarehouseId(detail.getWarehouseId());
							log.setUnitId(product.getUnitId());
							log.setPrice(detail.getPrice());
							log.setInQty(detail.getQty().intValue());
							log.setInMoney(detail.getMoney());
							// 增加成品库存字段
							Integer storgeQty = serviceFactory.getStockProductService().getStockQty(stockProduct.getProductId(), stockProduct.getWarehouseId());
							log.setStorgeQty(storgeQty);
							daoFactory.getCommonDao().saveEntity(log);
						}
					}
				}
			}

			if (flag == BoolValue.NO)
			{
				DynamicQuery query = new CompanyDynamicQuery(StockProductLog.class);
				query.eq("billId", id);
				List<StockProductLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockProductLog.class);
				daoFactory.getCommonDao().deleteAllEntity(logs);
			}
			return true;

		}
		else
		{
			return false;
		}

	}
	
	@Override
	@Transactional
	public List<StockProduct> checkBack(Long id)
	{
		// TODO 该方法与所有的判断不足有区别，需要重构
		
		OutSourceArrive order = serviceFactory.getOutSourceArriveService().get(id);
		// order 是订单下的所有信息，order.getDetailList()是订单下所有产品详情
		List<OutSourceArriveDetail> detileList = order.getDetailList();
		List<StockProduct> list = new ArrayList<StockProduct>();
		// 查找出该订单下的所有产品的库存量
		for (int i = 0; i < detileList.size(); i++)
		{
			// 只验证成品
			if (OutSourceType.PRODUCT != detileList.get(i).getType())
			{
				continue;
			}

			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
			query.eq("productId", detileList.get(i).getProductId());
			query.eq("warehouseId", detileList.get(i).getWarehouseId());
			StockProduct stockProduct = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);
			// 若库存量 < 反审核的订单下的产品数量 ,则反审核失败
			if (stockProduct == null) // 当库存为空的时候
			{
				stockProduct = new StockProduct();
				stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, detileList.get(i).getProductId()));
				stockProduct.setQty(0);
			}
			else
			{
				stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, stockProduct.getProductId()));
			}
			BigDecimal oqty = order.getDetailList().get(i).getQty(); // 当前反审核的产品的数量
			if (stockProduct.getQty() < oqty.intValue())
			{ // 若库存量 < 反审核的订单下的产品数量 ,则反审核失败
				list.add(stockProduct);
			}
		}
		return list;

	}

	@Override
	public SearchResult<OutSourceArriveDetail> findArriveSource(Date dateMin, Date dateMax, Long supplierId, String billNo, Integer pageSize, Integer pageNumber)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceArriveDetail.class, "po");
		query.createAlias(OutSourceArrive.class, "p");
		query.addProjection(Projections.property("po,p"));
		query.createAlias(Supplier.class, "s");
		query.eqProperty("po.masterId", "p.id");
		query.eqProperty("s.id", "p.supplierId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("s.employeeId", employes);
		}
		if (dateMin != null && !"".equals(dateMin))
		{
			query.ge("p.createTime", dateMin);
		}
		if (dateMax != null && !"".equals(dateMax))
		{
			query.le("p.createTime", dateMax);
		}
		if (supplierId != null && !"".equals(supplierId))
		{
			query.eq("p.supplierId", supplierId);
		}
		if (billNo != null && !"".equals(billNo))
		{
			query.like("p.billNo", "%" + billNo + "%");
		}
		query.eq("p.isCheck", BoolValue.YES);
		query.gt("po.qty-po.returnQty", new BigDecimal(0));// 未退货数量大于0
		query.gt("po.qty-po.reconcilQty", new BigDecimal(0));// 未对账数量大于0
		query.setPageIndex(pageNumber);
		query.setPageSize(pageSize);
		query.desc("p.createTime");
		query.setIsSearchTotalCount(true);
		query.eq("po.companyId", UserUtils.getCompanyId());
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<OutSourceArriveDetail> result = new SearchResult<OutSourceArriveDetail>();
		result.setResult(new ArrayList<OutSourceArriveDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OutSourceArriveDetail detail = (OutSourceArriveDetail) c[0];
			detail.setMaster((OutSourceArrive) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;

	}

	@Override
	public void coutReturnReconcil(OutSourceProcessDetail outSourceProcessDetail)
	{
		BigDecimal sum = new BigDecimal(0);// 对账数量
		BigDecimal sumMony = new BigDecimal(0.00);// 已收款
		DynamicQuery query = new CompanyDynamicQuery(OutSourceArriveDetail.class, "a");
		query.createAlias(OutSourceArrive.class, "b");
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", outSourceProcessDetail.getId());
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<OutSourceArriveDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OutSourceArriveDetail.class);
		for (OutSourceArriveDetail outSourceArriveDetail : result.getResult())
		{
			// 对账数量 = 到货对账数量-退货对账数量
			// 根据到货ID查询退货对账数量
			BigDecimal reconcil = serviceFactory.getOutSourceReturnService().coutReturnReconcil(outSourceArriveDetail.getId());
			sum = sum.add(outSourceArriveDetail.getReconcilQty().subtract(reconcil));
			
			BigDecimal paymentMoney = serviceFactory.getOutSourceReconcilService().countReturnPaymentMoney(outSourceArriveDetail);
			sumMony = sumMony.add(paymentMoney);
		}
		outSourceProcessDetail.setReconcilQty(sum);
		outSourceProcessDetail.setPayment(sumMony);
	}

	/** 反写生产任务
	 * @param flag
	 * @param detail
	 */
	private void updateWorkReportTask(String workBillNo, String procedureCode, String partName, BigDecimal reportQty)
	{
		String reString = (reportQty != null && reportQty.compareTo(new BigDecimal(0)) ==1) ? "增加" : "减少";
		logger.info("反写生产任务：生产单号" + workBillNo + "、部件名称：" + partName + "、工序单号:" + procedureCode + "、数量：[" + reString + "]" + reportQty + ",");
		DynamicQuery query = new CompanyDynamicQuery(WorkReportTask.class, "a");
		query.eq("a.billNo", workBillNo);
		query.eq("a.isOutSource", BoolValue.YES);
		query.eq("a.procedureCode", procedureCode);
		query.eq("a.partName", partName);
		WorkReportTask task = daoFactory.getCommonDao().getByDynamicQuery(query, WorkReportTask.class);

		if (task != null)
		{
			task.setReportQty(reportQty.add(task.getReportQty()));
			task.setUnreportQty(task.getYieldQty().subtract(task.getReportQty()));
			task.setUpdateTime(new Date());
			daoFactory.getCommonDao().saveEntity(task);
		}

	}
}
