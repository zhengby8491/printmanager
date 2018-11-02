/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年02月22日 下午18:24:23
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.sale.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ConverterUtils;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcil;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sale.SaleReconcilService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 - 销售对账
 * </pre>
 * @author       zhengby
 * @version 		 1.0, 2016年7月1日
 * @version 	   2.0, 2018年2月22日下午6:15:45, zhengby, 代码规范
 */
@Service
public class SaleReconcilServiceImpl extends BaseServiceImpl implements SaleReconcilService
{

	@Override
	public SaleReconcil get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReconcil.class);
		query.eq("id", id);
		SaleReconcil order = daoFactory.getCommonDao().getByDynamicQuery(query, SaleReconcil.class);

		// SaleReconcil order = daoFactory.getCommonDao().getEntity(SaleReconcil.class, id);
		order.setDetailList(this.getDetailList(id));
		return order;
	}

	@Override
	public SaleReconcilDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReconcilDetail.class);
		query.eq("id", id);
		SaleReconcilDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, SaleReconcilDetail.class);

		// SaleReconcilDetail detail = daoFactory.getCommonDao().getEntity(SaleReconcilDetail.class, id);
		detail.setMaster(daoFactory.getCommonDao().getEntity(SaleReconcil.class, detail.getMasterId()));
		return detail;
	}

	@Override
	public SaleReconcil lock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReconcil.class);
		query.eq("id", id);
		SaleReconcil order = daoFactory.getCommonDao().lockByDynamicQuery(query, SaleReconcil.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(SaleReconcilDetail.class);
		query_detail.eq("masterId", id);
		List<SaleReconcilDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, SaleReconcilDetail.class, LockType.LOCK_WAIT);

		/*
		 * SaleReconcil order = daoFactory.getCommonDao().lockObject(SaleReconcil.class, id); List<SaleReconcilDetail>
		 * detailList = this.getDetailList(id); for (SaleReconcilDetail detail : detailList) { detail =
		 * daoFactory.getCommonDao().lockObject(SaleReconcilDetail.class, detail.getId()); }
		 */
		order.setDetailList(detailList);
		return order;
	}

	@Override
	public List<SaleReconcilDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReconcilDetail.class, "s");
		query.addProjection(Projections.property("s,p"));
		query.createAlias(Product.class, "p");
		query.eq("masterId", id);
		query.eqProperty("p.id", "s.productId");
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		List<SaleReconcilDetail> detailList = new ArrayList<SaleReconcilDetail>();

		for (Object[] c : temp_result.getResult())
		{
			SaleReconcilDetail detail = (SaleReconcilDetail) c[0];
			Product product = (Product) c[1];
			detail.setImgUrl(product.getImgUrl());
			detailList.add(detail);
		}
		return detailList;
	}

	@Override
	@Transactional
	public SaleReconcil save(SaleReconcil order)
	{
		BoolValue flag = order.getIsCheck();
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.SALE_SK);
		order.setBillNo(UserUtils.createBillNo(BillType.SALE_SK));
		order.setUserNo(UserUtils.getUser().getUserNo());
		// 创建人优先取员工姓名
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
		for (SaleReconcilDetail detail : order.getDetailList())
		{
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setMasterId(order.getId());
			detail.setIsForceComplete(BoolValue.NO);
			detail.setIsReceiveOver(BoolValue.NO);
			detail.setReceiveMoney(new BigDecimal(0));
			detail.setSourceBillType(detail.getSourceBillType());
			detail.setSourceBillNo(detail.getSourceBillNo());
			TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), detail.getTaxRateId());
			detail.setPercent(taxRate.getPercent());
			detail.setUserNo(UserUtils.getUser().getUserNo());
			if (detail.getSourceBillType() == BillType.SALE_IV)
			{
				// 反写送货单对账数量
				SaleDeliverDetail sourceArrive = daoFactory.getCommonDao().lockObject(SaleDeliverDetail.class, detail.getSourceDetailId());
				sourceArrive.setReconcilQty(sourceArrive.getReconcilQty() + detail.getQty());
				sourceArrive.setReconcilMoney(sourceArrive.getReconcilMoney().add(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(sourceArrive);
			}
			else if (detail.getSourceBillType() == BillType.SALE_IR)
			{
				// 反写退货单对账数量
				SaleReturnDetail sourceReturn = daoFactory.getCommonDao().lockObject(SaleReturnDetail.class, detail.getSourceDetailId());
				sourceReturn.setReconcilQty(sourceReturn.getReconcilQty() + Math.abs(detail.getQty()));
				sourceReturn.setReconcilMoney(sourceReturn.getReconcilMoney().subtract(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(sourceReturn);
			}
		}
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());
		// 保存并审核按钮执行审核
		if (flag == BoolValue.YES)
		{
			serviceFactory.getCommonService().audit(BillType.SALE_SK, order.getId(), BoolValue.YES);
		}
		return order;
	}

	@Override
	@Transactional
	public SaleReconcil update(SaleReconcil order)
	{
		BoolValue flag = order.getIsCheck();
		order.setIsCheck(BoolValue.NO);
		SaleReconcil old_order = serviceFactory.getSaleReconcilService().lock(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		Map<Long, SaleReconcilDetail> old_detail_map = ConverterUtils.list2Map(old_order.getDetailList(), "id");

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// 要删除的数据
		List<SaleReconcilDetail> del_detail = new ArrayList<SaleReconcilDetail>();

		for (SaleReconcilDetail detail : old_order.getDetailList())
		{
			old_detailIds.add(detail.getId());
		}
		for (SaleReconcilDetail new_detail : order.getDetailList())
		{
			if (new_detail.getId() != null)
			{
				new_detailIds.add(new_detail.getId());
			}
		}

		for (SaleReconcilDetail new_detail : order.getDetailList())
		{
			SaleReconcilDetail old_detail = old_detail_map.get(new_detail.getId());

			if (new_detail.getSourceBillType() == BillType.SALE_IV)
			{
				// 反写送货单对账数量
				SaleDeliverDetail sourceArrive = daoFactory.getCommonDao().lockObject(SaleDeliverDetail.class, old_detail.getSourceDetailId());
				sourceArrive.setReconcilQty(sourceArrive.getReconcilQty() - (old_detail.getQty() - new_detail.getQty()));
				sourceArrive.setReconcilMoney(sourceArrive.getReconcilMoney().subtract(old_detail.getMoney()).add(new_detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(sourceArrive);
			}
			else if (new_detail.getSourceBillType() == BillType.SALE_IR)
			{
				// 反写退货单对账数量
				SaleReturnDetail sourceReturn = daoFactory.getCommonDao().lockObject(SaleReturnDetail.class, old_detail.getSourceDetailId());
				sourceReturn.setReconcilQty(sourceReturn.getReconcilQty() - (Math.abs(old_detail.getQty()) - Math.abs(new_detail.getQty())));
				sourceReturn.setReconcilMoney(sourceReturn.getReconcilMoney().add(old_detail.getMoney()).subtract(new_detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(sourceReturn);
			}
			PropertyClone.copyProperties(old_detail, new_detail, false, null, new String[] { "memo" });// 替换成新内容

			daoFactory.getCommonDao().updateEntity(old_detail);// 更新子表

		}
		// 删除操作
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))
			{
				SaleReconcilDetail saleReconcilDetail = old_detail_map.get(id);
				if (saleReconcilDetail.getSourceBillType() == BillType.SALE_IV)
				{
					// 反写送货单对账数量
					SaleDeliverDetail sourceArrive = daoFactory.getCommonDao().lockObject(SaleDeliverDetail.class, saleReconcilDetail.getSourceDetailId());
					sourceArrive.setReconcilQty(sourceArrive.getReconcilQty() - saleReconcilDetail.getQty());
					sourceArrive.setReconcilMoney(sourceArrive.getReconcilMoney().subtract(saleReconcilDetail.getMoney()));
					daoFactory.getCommonDao().updateEntity(sourceArrive);
				}
				else if (saleReconcilDetail.getSourceBillType() == BillType.SALE_IR)
				{
					// 反写退货单对账数量
					SaleReturnDetail sourceReturn = daoFactory.getCommonDao().lockObject(SaleReturnDetail.class, saleReconcilDetail.getSourceDetailId());
					sourceReturn.setReconcilQty(sourceReturn.getReconcilQty() - Math.abs(saleReconcilDetail.getQty()));
					sourceReturn.setReconcilMoney(sourceReturn.getReconcilMoney().add(saleReconcilDetail.getMoney()));
					daoFactory.getCommonDao().updateEntity(sourceReturn);
				}
				del_detail.add(saleReconcilDetail);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(del_detail);

		PropertyClone.copyProperties(old_order, order, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "deliveryAddress" });// 替换新内容
		order.setUpdateName(UserUtils.getUserName());
		order.setUpdateTime(new Date());
		// 必须先保存子表，然后再保存主表
		daoFactory.getCommonDao().updateEntity(old_order);// 更新主表
		// 保存并审核按钮执行审核
		if (flag == BoolValue.YES)
		{
			serviceFactory.getCommonService().audit(BillType.SALE_SK, order.getId(), BoolValue.YES);
		}
		return order;
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		SaleReconcil order = this.lock(id);
		List<SaleReconcilDetail> detailList = order.getDetailList();

		// daoFactory.getCommonDao().deleteEntity(SaleReconcil.class, id);
		// List<SaleReconcilDetail> detailList = getDetailList(id);
		for (SaleReconcilDetail detail : detailList)
		{
			if (detail.getSourceBillType() == BillType.SALE_IV)
			{
				// 反写送货单对账数量
				SaleDeliverDetail sourceArrive = daoFactory.getCommonDao().lockObject(SaleDeliverDetail.class, detail.getSourceDetailId());
				sourceArrive.setReconcilQty(sourceArrive.getReconcilQty() - detail.getQty());
				sourceArrive.setReconcilMoney(sourceArrive.getReconcilMoney().subtract(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(sourceArrive);
			}
			else if (detail.getSourceBillType() == BillType.SALE_IR)
			{
				// 反写退货单对账数量
				SaleReturnDetail sourceReturn = daoFactory.getCommonDao().lockObject(SaleReturnDetail.class, detail.getSourceDetailId());
				sourceReturn.setReconcilQty(sourceReturn.getReconcilQty() - Math.abs(detail.getQty()));
				sourceReturn.setReconcilMoney(sourceReturn.getReconcilMoney().add(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(sourceReturn);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(detailList);
		daoFactory.getCommonDao().deleteEntity(order);
	}

	@Override
	public SearchResult<SaleReconcil> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReconcil.class, "o");
		query.addProjection(Projections.property("o"));
		query.createAlias(Customer.class, "c");
		query.eqProperty("c.id", "o.customerId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("o.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("o.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerBillNo()))
		{
			query.like("o.custBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("o.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("o.isCheck", queryParam.getAuditFlag());
		}
		query.eq("o.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("o.createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SaleReconcil.class);
	}

	@Override
	public SearchResult<SaleReconcilDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReconcilDetail.class, "srd");
		query.createAlias(SaleReconcil.class, "sr");
		query.addProjection(Projections.property("srd,sr,p.fileName"));
		query.createAlias(Customer.class, "c");
		query.createAlias(Product.class, "p");
		query.eqProperty("srd.masterId", "sr.id");
		query.eqProperty("c.id", "sr.customerId");
		query.eqProperty("p.id", "srd.productId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		query.eq("sr.isCancel", BoolValue.NO);

		if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
		{
			query.like("srd.sourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSaleOrderBillNo()))
		{
			query.like("srd.saleOrderBillNo", "%" + queryParam.getSaleOrderBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("srd.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("srd.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("srd.style", "%" + queryParam.getProductStyle() + "%");
		}

		if (queryParam.getDateMin() != null)
		{
			query.ge("sr.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("sr.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("sr.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getProductName()))
		{
			query.like("srd.productName", "%" + queryParam.getProductName() + "%");
		}
		if (queryParam.getWarehouseId() != null && queryParam.getWarehouseId() != -1)
		{
			query.eq("srd.warehouseId", queryParam.getWarehouseId());
		}
		if (queryParam.getEmployeeId() != null && queryParam.getEmployeeId() != -1)
		{
			query.eq("sr.employeeId", queryParam.getEmployeeId());
		}
		if (queryParam.getCustomerClassId() != null && queryParam.getCustomerClassId() != -1)
		{
			query.eq("c.customerClassId", queryParam.getCustomerClassId());
		}
		if (queryParam.getProductClassId() != null && queryParam.getProductClassId() != -1)
		{
			query.eq("p.productClassId", queryParam.getProductClassId());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("sr.isCheck", queryParam.getAuditFlag());
		}
		query.eq("sr.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("sr.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<SaleReconcilDetail> result = new SearchResult<SaleReconcilDetail>();
		result.setResult(new ArrayList<SaleReconcilDetail>());

		Product product = new Product();
		for (Object[] c : temp_result.getResult())
		{
			SaleReconcilDetail detail = (SaleReconcilDetail) c[0];
			product.setFileName((String)c[2]);
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((SaleReconcil) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public boolean forceComplete(TableType tableType, Long[] ids, Long[] deliverIds, Long[] returnIds, BoolValue completeFlag)
	{
		if (tableType == TableType.MASTER)
		{
			serviceFactory.getCommonService().forceComplete(SaleReconcil.class, ids, completeFlag);

		}
		else if (tableType == TableType.DETAIL)
		{
			serviceFactory.getCommonService().forceComplete(SaleDeliverDetail.class, deliverIds, completeFlag);
			serviceFactory.getCommonService().forceComplete(SaleReturnDetail.class, returnIds, completeFlag);
		}
		else
		{
			return false;
		}
		return true;
	}

	@Override
	public BigDecimal countReturnReceiveMoney(SaleDeliverDetail saleDeliverDetail)
	{
		BigDecimal sum = new BigDecimal(0.00);
		DynamicQuery query = new CompanyDynamicQuery(SaleReconcilDetail.class, "a");
		query.createAlias(SaleReconcil.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", saleDeliverDetail.getId());
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<SaleReconcilDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SaleReconcilDetail.class);
		for (SaleReconcilDetail saleReconcilDetail : result.getResult())
		{
			sum = sum.add(saleReconcilDetail.getReceiveMoney());
		}
		return sum;
	}

	@Override
	public Boolean hasCheckAll(String saleOrderBillNo, Long productId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReconcilDetail.class, "d");
		query.createAlias(SaleReconcil.class, JoinType.LEFTJOIN, "m", "m.id=d.masterId");
		query.addProjection(Projections.property("count(1)"));
		query.setQueryType(QueryType.JDBC);
		query.eq("d.saleOrderBillNo", saleOrderBillNo);
		query.eq("d.productId", productId);
		query.eq("m.isCheck", BoolValue.NO);
		@SuppressWarnings("rawtypes")
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt(map.getResult().get(0).get("count(1)").toString()) == 0;

	}

	// ==================== 新规范 - 代码重构 ====================

	@Override
	public SearchResult<SaleReconcilDetail> findAll(BoolValue isCheck)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReconcilDetail.class, "a");
		query.createAlias(SaleReconcil.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.eqProperty("a.masterId", "b.id");
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SaleReconcilDetail.class);
	}

	@Override
	public SearchResult<SaleReconcilDetail> findAll()
	{
		return findAll(BoolValue.YES);
	}
}
