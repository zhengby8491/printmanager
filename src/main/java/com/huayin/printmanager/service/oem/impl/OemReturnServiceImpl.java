/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月14日 下午6:48:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.oem.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.huayin.printmanager.persist.entity.basic.ExchangeRate;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.entity.oem.OemDeliverDetail;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.persist.entity.oem.OemReturn;
import com.huayin.printmanager.persist.entity.oem.OemReturnDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.ReturnGoodsType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.oem.OemReturnService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工管理  - 代工退货单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月14日下午6:48:02, zhengby
 */
@Service
public class OemReturnServiceImpl extends BaseServiceImpl implements OemReturnService
{
	@Override
	public OemReturn get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReturn.class);
		query.eq("id", id);
		OemReturn order = daoFactory.getCommonDao().getByDynamicQuery(query, OemReturn.class);
		order.setDetailList(getDetailList(id));
		return order;
	}

	@Override
	public List<OemReturnDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReturnDetail.class);
		query.eq("masterId", id);
		query.asc("id");
		List<OemReturnDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OemReturnDetail.class);
		return detailList;
	}

	@Override
	public OemReturnDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReturnDetail.class);
		query.eq("id", id);
		OemReturnDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, OemReturnDetail.class);
		return detail;
	}

	@Override
	public OemReturn getMaster(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReturn.class);
		query.eq("id", id);
		OemReturn order = daoFactory.getCommonDao().getByDynamicQuery(query, OemReturn.class);
		return order;
	}

	@Override
	public OemReturnDetail getDetailHasMaster(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReturnDetail.class);
		query.eq("id", id);
		OemReturnDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, OemReturnDetail.class);
		detail.setMaster(this.getMaster(detail.getMasterId()));
		return detail;
	}

	@Override
	@Transactional
	public void save(OemReturn order)
	{
		BoolValue flag = order.getIsCheck();
		order.setBillType(BillType.OEM_ER);
		order.setBillNo(UserUtils.createBillNo(BillType.OEM_ER));
		order.setUserNo(UserUtils.getUser().getUserNo());
		order.setCompanyId(UserUtils.getCompanyId());
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
		// 先保存主表，得到返回的order
		order = daoFactory.getCommonDao().saveEntity(order);
		// 给明细表的masterId设值
		for (OemReturnDetail detail : order.getDetailList())
		{
			// 反写送货单退货数量
			OemDeliverDetail deliverDetail = daoFactory.getCommonDao().lockObject(OemDeliverDetail.class, detail.getSourceDetailId());
			deliverDetail.setReturnQty(deliverDetail.getReturnQty().add(detail.getQty()));
			deliverDetail.setReturnMoney(deliverDetail.getReturnMoney().add(detail.getMoney()));
			daoFactory.getCommonDao().updateEntity(deliverDetail);

			// 反写代工单送货量
			if (order.getReturnType() == ReturnGoodsType.EXCHANGE)
			{
				OemOrderDetail orderDetail = daoFactory.getCommonDao().lockObject(OemOrderDetail.class, deliverDetail.getSourceDetailId());
				orderDetail.setDeliverQty(orderDetail.getDeliverQty().subtract( detail.getQty()));
				orderDetail.setDeliverMoney(orderDetail.getDeliverMoney().subtract(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(orderDetail);
			}

			detail.setMasterId(order.getId());
			detail.setUserNo(UserUtils.getUser().getUserNo());
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setOemOrderBillId(deliverDetail.getSourceId());
			detail.setOemOrderBillNo(deliverDetail.getSourceBillNo());
			detail.setSourceBillType(BillType.OEM_ED);
		}
		// 再保存订单明细表
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());

		// 保存并审核按钮执行审核
		if (flag == BoolValue.YES)
		{
			serviceFactory.getCommonService().audit(BillType.OEM_ER, order.getId(), BoolValue.YES);
		}
	}

	@Override
	@Transactional
	public void update(OemReturn order)
	{
		if (order.getIsCheck() == BoolValue.YES)
		{
			order.setCheckTime(new Date());
			Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
			if (e != null)
			{
				order.setCheckUserName(e.getName());
			}
			else
			{
				order.setCheckUserName(UserUtils.getUserName());
			}
		}
		OemReturn old_order = this.lockHasChildren(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		Map<Long, OemReturnDetail> old_detail_map = ConverterUtils.list2Map(old_order.getDetailList(), "id");
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		List<OemReturnDetail> delList = new ArrayList<>();

		// ID集合
		for (OemReturnDetail newItem : order.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (OemReturnDetail oldItem : old_order.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}

		for (OemReturnDetail newOrderDetail : order.getDetailList())
		{
			// 送货单明细
			OemDeliverDetail deliverDetail = serviceFactory.getOemDeliverService().getDetail(newOrderDetail.getSourceDetailId());
			if (newOrderDetail.getId() != null)
			{ // 更新
				OemReturnDetail oldOrderDetail = this.getDetail(newOrderDetail.getId());

				// 反写代工送货单的退货数量
				deliverDetail.setReturnQty(deliverDetail.getReturnQty().subtract(oldOrderDetail.getQty().subtract(newOrderDetail.getQty())));
				deliverDetail.setReturnMoney(deliverDetail.getReturnMoney().subtract(oldOrderDetail.getMoney().subtract(newOrderDetail.getMoney())));
				daoFactory.getCommonDao().updateEntity(deliverDetail);

				if (old_order.getReturnType() == ReturnGoodsType.RETURN)
				{// 老类型=退货
					if (order.getReturnType() == ReturnGoodsType.EXCHANGE)
					{
						OemOrderDetail process = daoFactory.getCommonDao().lockObject(OemOrderDetail.class, deliverDetail.getSourceDetailId());
						process.setDeliverQty(process.getDeliverQty().subtract(newOrderDetail.getQty()));
						process.setDeliverMoney(process.getDeliverMoney().subtract(newOrderDetail.getMoney()));
						daoFactory.getCommonDao().updateEntity(process);
					}
				}
				else if (old_order.getReturnType() == ReturnGoodsType.EXCHANGE)
				{// 老类型=换货
					if (order.getReturnType() == ReturnGoodsType.EXCHANGE)
					{// 新类型=换货：更新换货逻辑
						OemOrderDetail process = daoFactory.getCommonDao().lockObject(OemOrderDetail.class, deliverDetail.getSourceDetailId());
						process.setDeliverQty(process.getDeliverQty().add(oldOrderDetail.getQty().subtract(newOrderDetail.getQty())));
						process.setDeliverMoney(process.getDeliverMoney().add(oldOrderDetail.getMoney()).subtract(newOrderDetail.getMoney()));
						daoFactory.getCommonDao().updateEntity(process);
					}
					else
					{// 新类型=退货:删除换货记录逻辑
						OemOrderDetail process = daoFactory.getCommonDao().lockObject(OemOrderDetail.class, deliverDetail.getSourceDetailId());
						process.setDeliverQty(process.getDeliverQty().add(oldOrderDetail.getQty()));
						process.setDeliverMoney(process.getDeliverMoney().add(oldOrderDetail.getMoney()));
						daoFactory.getCommonDao().updateEntity(process);
					}
				}
				oldOrderDetail.setMemo(newOrderDetail.getMemo());
				PropertyClone.copyProperties(oldOrderDetail, newOrderDetail, false, null, new String[] { "memo","processRequire" });// 替换成新内容
			}
			else
			{// 新增
				newOrderDetail.setMasterId(old_order.getId());
				newOrderDetail.setReconcilQty(new BigDecimal(0));
				newOrderDetail.setCompanyId(UserUtils.getCompanyId());
				newOrderDetail.setIsForceComplete(BoolValue.NO);
				newOrderDetail.setUserNo(UserUtils.getUser().getUserNo());
				TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), newOrderDetail.getTaxRateId());
				newOrderDetail.setPercent(taxRate.getPercent());
				// 反写代工送货单送货数量
				deliverDetail.setReturnQty(deliverDetail.getReturnQty().add(newOrderDetail.getQty()));
				deliverDetail.setReturnMoney(deliverDetail.getReturnMoney().add(newOrderDetail.getMoney()));
				daoFactory.getCommonDao().updateEntity(deliverDetail);
				if (order.getReturnType() == ReturnGoodsType.EXCHANGE)
				{// 换货：送货数-换货数
					OemOrderDetail process = daoFactory.getCommonDao().lockObject(OemOrderDetail.class, deliverDetail.getSourceDetailId());
					// 反写代工订单送货数量
					process.setDeliverQty(process.getDeliverQty().subtract(newOrderDetail.getQty()));
					process.setDeliverMoney(process.getDeliverMoney().subtract(newOrderDetail.getMoney()));
					daoFactory.getCommonDao().updateEntity(process);
				}
				daoFactory.getCommonDao().saveEntity(newOrderDetail);
			}

		}
		// 删除操作
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				OemReturnDetail returnDetail = old_detail_map.get(id);
				// 反写送货单退货数量
				OemDeliverDetail source = daoFactory.getCommonDao().lockObject(OemDeliverDetail.class, returnDetail.getSourceDetailId());
				source.setReturnQty(source.getReturnQty().subtract(returnDetail.getQty()));
				source.setReturnMoney(source.getReturnMoney().subtract(returnDetail.getMoney()));
				daoFactory.getCommonDao().updateEntity(source);
				if (old_order.getReturnType() == ReturnGoodsType.EXCHANGE)
				{// 换货：送货数+换货数
					OemOrderDetail process = daoFactory.getCommonDao().lockObject(OemOrderDetail.class, source.getSourceDetailId());
					process.setDeliverQty(process.getDeliverQty().add(returnDetail.getQty()));
					process.setDeliverMoney(process.getDeliverMoney().add(returnDetail.getMoney()));
					daoFactory.getCommonDao().updateEntity(process);
				}
				delList.add(returnDetail);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(delList);

		PropertyClone.copyProperties(old_order, order, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "supplierAddress" });// 替换成新内容
		ExchangeRate exchangeRate = serviceFactory.getCompanyService().getExchangeRate(order.getCurrencyType());
		old_order.setRateId(exchangeRate.getId());
		old_order.setUpdateName(UserUtils.getUserName());
		old_order.setUpdateTime(new Date());
		daoFactory.getCommonDao().updateEntity(old_order);
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		OemReturn order = this.get(id);
		List<OemReturnDetail> detailList = order.getDetailList();
		for (OemReturnDetail detail : detailList)
		{
			// 反写代工送货的送货量
			OemDeliverDetail source = daoFactory.getCommonDao().lockObject(OemDeliverDetail.class, detail.getSourceDetailId());
			source.setReturnQty(source.getReturnQty().subtract(detail.getQty()));
			source.setReturnMoney(source.getReturnMoney().subtract(detail.getMoney()));
			daoFactory.getCommonDao().updateEntity(source);

			if (order.getReturnType() == ReturnGoodsType.EXCHANGE)
			{// 换货：送货数+换货数
				OemOrderDetail process = daoFactory.getCommonDao().lockObject(OemOrderDetail.class, source.getSourceDetailId());
				process.setDeliverQty(process.getDeliverQty().add(detail.getQty()));
				process.setDeliverMoney(process.getDeliverMoney().add(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(process);
			}
		}

		daoFactory.getCommonDao().deleteAllEntity(order.getDetailList());
		daoFactory.getCommonDao().deleteEntity(order);
	}

	@Override
	public SearchResult<OemReturn> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReturn.class, "o");
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
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("o.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("o.isCheck", queryParam.getAuditFlag());
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		query.eq("o.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("o.createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OemReturn.class);
	}

	@Override
	public SearchResult<OemReturnDetail> findDetailBycondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReturnDetail.class, "od");
		query.createAlias(OemReturn.class, "o");
		query.createAlias(Customer.class, "c");
		query.eqProperty("od.masterId", "o.id");
		query.eqProperty("c.id", "o.customerId");
		query.addProjection(Projections.property("od, o"));
		query.eq("o.isCancel", BoolValue.NO);

		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("od.style", "%" + queryParam.getProductStyle() + "%");
		}

		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
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
		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("o.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getProductName()))
		{
			query.like("od.productName", "%" + queryParam.getProductName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getProcedureName()))
		{
			query.like("od.procedureName", "%" + queryParam.getProcedureName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getOemOrderBillNo()))
		{
			query.like("od.oemOrderBillNo", "%" + queryParam.getOemOrderBillNo() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getSourceBillNo()))
		{
			query.like("od.sourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
		}
		if (queryParam.getDeliverDateMin() != null)
		{
			query.ge("od.deliveryTime", queryParam.getDeliverDateMin());
		}
		if (queryParam.getDeliverDateMax() != null)
		{
			query.le("od.deliveryTime", queryParam.getDeliverDateMax());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("o.isCheck", queryParam.getAuditFlag());
		}
		query.eq("o.companyId", UserUtils.getCompanyId());
		query.eq("c.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("o.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<OemReturnDetail> result = new SearchResult<OemReturnDetail>();
		result.setResult(new ArrayList<OemReturnDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OemReturn oemOrder = (OemReturn) c[1];
			OemReturnDetail detail = (OemReturnDetail) c[0];
			detail.setMaster(oemOrder);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public OemReturn lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReturn.class);
		query.eq("id", id);
		OemReturn order = daoFactory.getCommonDao().lockByDynamicQuery(query, OemReturn.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(OemReturnDetail.class);
		query_detail.eq("masterId", id);
		List<OemReturnDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, OemReturnDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}
	/* （非 Javadoc）
	 * @see com.huayin.printmanager.service.oem.OemReturnService#findAll(com.huayin.printmanager.persist.enumerate.ReturnGoodsType)
	 */
	@Override
	public SearchResult<OemReturnDetail> findAll(BoolValue isCheck, ReturnGoodsType type)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReturnDetail.class, "a");
		query.createAlias(OemReturn.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.eqProperty("a.masterId", "b.id");
		query.eq("b.isCheck", isCheck);
		query.eq("b.returnType", type);// 换货类型
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OemReturnDetail.class);
	}
	
	@Override
	public SearchResult<OemReturnDetail> findAll(ReturnGoodsType type)
	{
		return findAll(BoolValue.YES, type);
	}
}
