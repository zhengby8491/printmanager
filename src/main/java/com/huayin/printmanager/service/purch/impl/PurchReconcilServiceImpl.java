/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.purch.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
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
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.ExchangeRate;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.purch.PurchReconcil;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchRefund;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStock;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.purch.PurchReconcilService;
import com.huayin.printmanager.service.purch.vo.NotReconcilDetailVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 采购管理 - 采购对账
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午2:20:01, zhengby, 代码规范
 */
@Service
public class PurchReconcilServiceImpl extends BaseServiceImpl implements PurchReconcilService
{

	@Override
	@Transactional
	public PurchReconcil save(PurchReconcil purchReconcil)
	{
		purchReconcil.setCompanyId(UserUtils.getCompanyId());
		purchReconcil.setBillType(BillType.PURCH_PK);
		purchReconcil.setBillNo(UserUtils.createBillNo(BillType.PURCH_PK));

		purchReconcil.setIsForceComplete(BoolValue.NO);
		purchReconcil.setCreateTime(new Date());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			purchReconcil.setCreateName(e.getName());
		}
		else
		{
			purchReconcil.setCreateName(UserUtils.getUserName());
		}
		purchReconcil.setCreateEmployeeId(UserUtils.getEmployeeId());
		if (purchReconcil.getIsCheck() == BoolValue.YES)
		{
			purchReconcil.setCheckTime(new Date());
			if (e != null)
			{
				purchReconcil.setCheckUserName(e.getName());
			}
			else
			{
				purchReconcil.setCheckUserName(UserUtils.getUserName());
			}
		}
		daoFactory.getCommonDao().saveEntity(purchReconcil);

		for (PurchReconcilDetail purchReconcilDetail : purchReconcil.getDetailList())
		{
			if ("".equals(purchReconcilDetail.getCode()) || purchReconcilDetail.getCode() == null)
			{
				continue;
			}
			purchReconcilDetail.setIsPaymentOver(BoolValue.NO);
			purchReconcilDetail.setPaymentMoney(new BigDecimal(0));
			// 源单为入库单时
			if (purchReconcilDetail.getSourceBillType().getCode() == "PN")
			{
				PurchStockDetail purchStockDetail = serviceFactory.getPurStockService().getDetail(purchReconcilDetail.getSourceDetailId());

				purchReconcilDetail.setMasterId(purchReconcil.getId());
				purchReconcilDetail.setCompanyId(UserUtils.getCompanyId());

				// 新增 入库单已对账数量变为 入库单已对账数量+此次对账数量
				purchStockDetail.setReconcilQty(purchReconcilDetail.getQty().add(purchStockDetail.getReconcilQty()));
				purchReconcilDetail.setPaymentMoney(new BigDecimal(0));
				purchReconcilDetail.setIsPaymentOver(BoolValue.NO);
				daoFactory.getCommonDao().saveEntity(purchReconcilDetail);
				daoFactory.getCommonDao().updateEntity(purchStockDetail);

			}
			else
			{// 源单为退货单时
				// 退货记录
				PurchRefundDetail purchRefundDetail = serviceFactory.getPurReturnService().getDetail(purchReconcilDetail.getSourceDetailId());

				purchReconcilDetail.setMasterId(purchReconcil.getId());
				purchReconcilDetail.setCompanyId(UserUtils.getCompanyId());

				purchReconcilDetail.setPaymentMoney(new BigDecimal(0));
				purchReconcilDetail.setIsPaymentOver(BoolValue.NO);
				// 退货在对账单数据库保存为负数 计算逻辑要反过来
				purchRefundDetail.setReconcilQty(purchRefundDetail.getReconcilQty().subtract(purchReconcilDetail.getQty()));
				daoFactory.getCommonDao().saveEntity(purchReconcilDetail);
				daoFactory.getCommonDao().saveEntity(purchRefundDetail);
			}
		}
		return purchReconcil;
	}

	@Override
	@Transactional
	public PurchReconcil update(PurchReconcil purchReconcil)
	{
		purchReconcil.setCompanyId(UserUtils.getCompanyId());
		purchReconcil.setBillType(BillType.PURCH_PK);
		// 存汇率
		ExchangeRate exchangeRate = serviceFactory.getCompanyService().getExchangeRate(purchReconcil.getCurrencyType());
		purchReconcil.setRate(exchangeRate.getRate());
		purchReconcil.setRateId(exchangeRate.getId());
		if (purchReconcil.getIsCheck() == BoolValue.YES)
		{
			purchReconcil.setCheckTime(new Date());
			Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
			if (e != null)
			{
				purchReconcil.setCheckUserName(e.getName());
			}
			else
			{
				purchReconcil.setCheckUserName(UserUtils.getUserName());
			}
		}
		PurchReconcil purchReconcil_ = this.lockHasChildren(purchReconcil.getId());
		// 判断是否已审核
		if (purchReconcil_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		
		PropertyClone.copyProperties(purchReconcil_, purchReconcil, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "supplierAddress" });// 替换成新内容
		purchReconcil_.setUpdateName(UserUtils.getUserName());
		purchReconcil_.setUpdateTime(new Date());
		daoFactory.getCommonDao().updateEntity(purchReconcil_);

		for (PurchReconcilDetail purchReconcilDetail : purchReconcil.getDetailList())
		{

			if ("".equals(purchReconcilDetail.getCode()) || purchReconcilDetail.getCode() == null)
			{
				continue;
			}
			// 源单为入库单时
			if (purchReconcilDetail.getSourceBillType().getCode() == "PN")
			{

				PurchStockDetail purchStockDetail = serviceFactory.getPurStockService().getDetail(purchReconcilDetail.getSourceDetailId());

				purchReconcilDetail.setMasterId(purchReconcil.getId());
				purchReconcilDetail.setCompanyId(UserUtils.getCompanyId());
				PurchReconcilDetail purchRecDetail = this.getDetail(purchReconcilDetail.getId());
				purchStockDetail.setReconcilQty(purchStockDetail.getReconcilQty().subtract(purchRecDetail.getQty()).add(purchReconcilDetail.getQty()));
				purchRecDetail.setMemo(purchReconcilDetail.getMemo());
				PropertyClone.copyProperties(purchRecDetail, purchReconcilDetail, false, null, new String[] { "memo" });// 替换成新内容
				daoFactory.getCommonDao().updateEntity(purchRecDetail);

				daoFactory.getCommonDao().updateEntity(purchStockDetail);

			}
			else
			{// 源单为退货单时

				// 退货记录
				PurchRefundDetail purchRefundDetail = serviceFactory.getPurReturnService().getDetail(purchReconcilDetail.getSourceDetailId());

				purchReconcilDetail.setMasterId(purchReconcil.getId());
				purchReconcilDetail.setCompanyId(UserUtils.getCompanyId());

				// 查之前的对账记录的对账数量
				PurchReconcilDetail purchRecDetail = this.getDetail(purchReconcilDetail.getId());
				// 已对账数量 变为 修改前对账数量-之前对账数量 + 此次对账数量
				// 退货在对账单数据库保存为负数 计算逻辑要反过来
				purchRefundDetail.setReconcilQty(purchRefundDetail.getReconcilQty().add(purchRecDetail.getQty()).subtract(purchReconcilDetail.getQty()));
				PropertyClone.copyProperties(purchRecDetail, purchReconcilDetail, false, null, new String[] { "memo" });
				daoFactory.getCommonDao().updateEntity(purchRecDetail);

				daoFactory.getCommonDao().updateEntity(purchRefundDetail);

			}

		}
		Map<Long, PurchReconcilDetail> old_detail_map = ConverterUtils.list2Map(purchReconcil_.getDetailList(), "id");
		// 删除记录
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (PurchReconcilDetail newItem : purchReconcil.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (PurchReconcilDetail oldItem : purchReconcil_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(PurchReconcilDetail.class, id);
				PurchReconcilDetail reConcilDetail_ = old_detail_map.get(id);
				if (reConcilDetail_.getSourceBillType() == BillType.PURCH_PN)
				{
					// 反写采购入库单的对账数量
					PurchStockDetail source = daoFactory.getCommonDao().getEntity(PurchStockDetail.class, reConcilDetail_.getSourceDetailId());
					source.setReconcilQty(source.getReconcilQty().subtract(reConcilDetail_.getQty()));
				} else
				{
					// 反写采购退货单的对账数量
					PurchRefundDetail source = daoFactory.getCommonDao().getEntity(PurchRefundDetail.class, reConcilDetail_.getSourceDetailId());
					source.setReconcilQty(source.getReconcilQty().subtract(reConcilDetail_.getQty().abs()));
				}
			}
		}
		return purchReconcil;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<NotReconcilDetailVo> transmitPurchReconcilList(QueryParam queryParam)
	{
		List<NotReconcilDetailVo> notReconcilDetailVoList = new ArrayList<NotReconcilDetailVo>();
		/**
		 * 查入库单与退货单详情 条件都相同
		 */
		DynamicQuery queryStock = new CompanyDynamicQuery(PurchStockDetail.class, "ps");
		queryStock.addProjection(Projections.property("ps.*,s.name as supplierName,p.billType,p.billNo,p.createTime"));
		queryStock.createAlias(PurchStock.class, JoinType.LEFTJOIN, "p", "p.id=ps.masterId");
		queryStock.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=p.supplierId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			queryStock.inArray("s.employeeId", employes);
		}

		DynamicQuery queryRefund = new CompanyDynamicQuery(PurchRefundDetail.class, "pr");
		queryRefund.addProjection(Projections.property("pr.*,s.name as supplierName,p.billType,p.billNo,p.createTime"));
		queryRefund.createAlias(PurchRefund.class, JoinType.LEFTJOIN, "p", "p.id=pr.masterId");
		queryRefund.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=p.supplierId");
		if (employes.length > 0)
		{
			queryRefund.inArray("s.employeeId", employes);
		}
		queryStock.eq("p.isCheck", BoolValue.YES);
		queryRefund.eq("p.isCheck", BoolValue.YES);
		if (queryParam.getCompleteFlag() == BoolValue.YES)
		{
			queryStock.eq("ps.isForceComplete", BoolValue.YES);
			queryRefund.eq("pr.isForceComplete", BoolValue.YES);
		}
		else
		{
			/**
			 * 已入库数量大于已对账数量
			 */
			queryStock.add(Restrictions.gtProperty("qty", "reconcilQty"));
			queryStock.eq("ps.isForceComplete", BoolValue.NO);
			queryStock.eq("p.isForceComplete", BoolValue.NO);

			/**
			 * 退货数量大于已对账数量
			 */
			queryRefund.add(Restrictions.gtProperty("qty", "reconcilQty"));
			queryRefund.eq("pr.isForceComplete", BoolValue.NO);
			queryRefund.eq("p.isForceComplete", BoolValue.NO);
		}

		if (StringUtils.isNotBlank(queryParam.getSpecifications()))
		{
			queryStock.like("ps.specifications", "%" + queryParam.getSpecifications() + "%");
			queryRefund.like("pr.specifications", "%" + queryParam.getSpecifications() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			queryStock.like("ps.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
			queryRefund.like("pr.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getOrderBillNo()))
		{
			queryStock.like("ps.sourceBillNo", "%" + queryParam.getOrderBillNo() + "%");
			queryRefund.like("pr.orderBillNo", "%" + queryParam.getOrderBillNo() + "%");
		}

		if (queryParam.getBillNo() != null && !"".equals(queryParam.getBillNo()))
		{
			queryStock.like("p.billNo", "%" + queryParam.getBillNo() + "%");
			queryRefund.like("p.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getSupplierName() != null && !"".equals(queryParam.getSupplierName()))
		{
			queryStock.like("p.supplierName", "%" + queryParam.getSupplierName() + "%");
			queryRefund.like("p.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (queryParam.getMaterialName() != null && !"".equals(queryParam.getMaterialName()))
		{
			queryStock.like("ps.materialName", "%" + queryParam.getMaterialName() + "%");
			queryRefund.like("pr.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (queryParam.getDateMin() != null && !"".equals(queryParam.getDateMin()))
		{
			queryStock.ge("p.createTime", queryParam.getDateMin());
			queryRefund.ge("p.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null && !"".equals(queryParam.getDateMax()))
		{
			queryStock.le("p.createTime", queryParam.getDateMax());
			queryRefund.le("p.createTime", queryParam.getDateMax());
		}

		queryStock.eq("ps.companyId", UserUtils.getCompanyId());
		queryRefund.eq("pr.companyId", UserUtils.getCompanyId());
		queryStock.setQueryType(QueryType.JDBC);
		queryRefund.setQueryType(QueryType.JDBC);

		SearchResult<HashMap> mapStock = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryStock, HashMap.class);
		SearchResult<HashMap> mapRefund = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryRefund, HashMap.class);
		for (Map<String, Object> map : mapStock.getResult())
		{
			NotReconcilDetailVo ReconcilVo = new NotReconcilDetailVo();
			try
			{
				// 注册枚举
				ConvertUtils.register(new Converter()
				{
					public Object convert(Class type, Object value)
					{
						if (!StringUtils.isEmpty((String) value))
						{
							return BillType.valueOf(type, (String) value);
						}
						else
						{
							return null;
						}
					}
				}, BillType.class);

				ReconcilVo = ObjectHelper.mapToObject(map, NotReconcilDetailVo.class);
				notReconcilDetailVoList.add(ReconcilVo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		for (Map<String, Object> map : mapRefund.getResult())
		{
			NotReconcilDetailVo ReconcilVo = new NotReconcilDetailVo();
			try
			{
				// 注册枚举
				ConvertUtils.register(new Converter()
				{
					public Object convert(Class type, Object value)
					{
						if (!StringUtils.isEmpty((String) value))
						{
							return BillType.valueOf((String) value);
						}
						else
						{
							return null;
						}
					}
				}, BillType.class);

				ReconcilVo = ObjectHelper.mapToObject(map, NotReconcilDetailVo.class);
				notReconcilDetailVoList.add(ReconcilVo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return notReconcilDetailVoList;
	}

	@Override
	public PurchReconcil get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchReconcil.class);
		query.eq("id", id);
		PurchReconcil purchReconcil = daoFactory.getCommonDao().getByDynamicQuery(query, PurchReconcil.class);

		DynamicQuery queryD = new CompanyDynamicQuery(PurchReconcilDetail.class);
		queryD.eq("masterId", id);
		List<PurchReconcilDetail> purchReconcilDetail = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, PurchReconcilDetail.class);
		if (purchReconcil != null)
		{
			purchReconcil.setDetailList(purchReconcilDetail);
		}

		return purchReconcil;
	}

	@Override
	public PurchReconcilDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchReconcilDetail.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, PurchReconcilDetail.class);

	}

	@Override
	public PurchReconcil lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchReconcil.class);
		query.eq("id", id);
		PurchReconcil order = daoFactory.getCommonDao().lockByDynamicQuery(query, PurchReconcil.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(PurchReconcilDetail.class);
		query_detail.eq("masterId", id);
		List<PurchReconcilDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, PurchReconcilDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

	@Override
	public PurchReconcil findByCheckbox(String[] checkbox)
	{
		PurchReconcil purchReconcil = new PurchReconcil();

		List<Long> pnListIds = new ArrayList<Long>(); // 入库id集合
		List<Long> prListIds = new ArrayList<Long>(); // 退货id集合
		// 参数例子 单据类型_id
		for (String string : checkbox)
		{
			if (!"".equals(string))
			{
				String[] n = string.split("_");
				if (n[0].equals("PN"))
				{
					pnListIds.add(Long.parseLong(n[1]));
				}
				else
				{
					prListIds.add(Long.parseLong(n[1]));
				}
			}
		}
		if (pnListIds.size() != 0)
		{
			// 源单类型为入库单 数据转对账
			DynamicQuery queryStockDetail = new CompanyDynamicQuery(PurchStockDetail.class);
			queryStockDetail.in("id", pnListIds);
			List<PurchStockDetail> purchStockDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryStockDetail, PurchStockDetail.class);

			DynamicQuery query = new CompanyDynamicQuery(PurchStock.class);
			query.eq("id", purchStockDetailList.get(0).getMasterId());
			PurchStock purchStock = daoFactory.getCommonDao().getByDynamicQuery(query, PurchStock.class);

			PropertyClone.copyProperties(purchReconcil, purchStock, false);

			// 明细表转入
			for (PurchStockDetail purchStockDetail : purchStockDetailList)
			{
				// 采购对账 >【入库数量 大于 已对账数量】
				if (purchStockDetail.getQty().compareTo(purchStockDetail.getReconcilQty()) != 1)
					continue;

				DynamicQuery queryStock = new CompanyDynamicQuery(PurchStock.class);
				queryStock.eq("id", purchStockDetail.getMasterId());
				PurchStock purchStock_ = daoFactory.getCommonDao().getByDynamicQuery(queryStock, PurchStock.class);

				PurchReconcilDetail purchReconcilDetail = new PurchReconcilDetail();
				PropertyClone.copyProperties(purchReconcilDetail, purchStockDetail, false);
				purchReconcilDetail.setSourceBillNo(purchStock_.getBillNo());
				purchReconcilDetail.setSourceBillType(purchStock_.getBillType());
				purchReconcilDetail.setSourceId(purchStock_.getId());
				purchReconcilDetail.setValuationUnitId(purchStockDetail.getValuationUnitId());
				purchReconcilDetail.setQty(purchStockDetail.getQty().subtract(purchStockDetail.getReconcilQty()));
				purchReconcilDetail.setSourceQty(purchStockDetail.getQty());
				purchReconcilDetail.setSourceDetailId(purchStockDetail.getId());
				purchReconcilDetail.setId(null);
				purchReconcilDetail.setWorkBillNo(purchStockDetail.getWorkBillNo());
				purchReconcilDetail.setWorkId(purchStockDetail.getWorkId());
				purchReconcilDetail.setOrderBillNo(purchStockDetail.getSourceBillNo());
				purchReconcilDetail.setDeliveryTime(purchStock.getStorageTime());
				purchReconcil.getDetailList().add(purchReconcilDetail);
			}
		}

		if (prListIds.size() != 0)
		{
			DynamicQuery queryRefundDet = new CompanyDynamicQuery(PurchRefundDetail.class);
			queryRefundDet.in("id", prListIds);
			List<PurchRefundDetail> purchRefundDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryRefundDet, PurchRefundDetail.class);
			// 入库单无选中记录时
			if (pnListIds.size() == 0)
			{
				DynamicQuery query = new CompanyDynamicQuery(PurchRefund.class);
				query.eq("id", purchRefundDetailList.get(0).getMasterId());
				PurchRefund purchRefund = daoFactory.getCommonDao().getByDynamicQuery(query, PurchRefund.class);
				PropertyClone.copyProperties(purchReconcil, purchRefund, false);
			}

			for (PurchRefundDetail purchRefundDetail : purchRefundDetailList)
			{
				DynamicQuery queryRefund = new CompanyDynamicQuery(PurchRefund.class);
				queryRefund.eq("id", purchRefundDetail.getMasterId());
				PurchRefund purchRefund = daoFactory.getCommonDao().getByDynamicQuery(queryRefund, PurchRefund.class);
				// 采购对账 >【入库数量 大于 已对账数量】
				if (purchRefundDetail.getQty().compareTo(purchRefundDetail.getReconcilQty()) != 1)
					continue;
				PurchReconcilDetail purchReconcilDetail = new PurchReconcilDetail();
				PropertyClone.copyProperties(purchReconcilDetail, purchRefundDetail, false);
				purchReconcilDetail.setSourceBillNo(purchRefund.getBillNo());
				purchReconcilDetail.setSourceBillType(purchRefund.getBillType());
				purchReconcilDetail.setSourceId(purchRefund.getId());
				purchReconcilDetail.setQty(purchRefundDetail.getQty().subtract(purchRefundDetail.getReconcilQty()));
				purchReconcilDetail.setSourceQty(purchRefundDetail.getQty());
				purchReconcilDetail.setSourceDetailId(purchRefundDetail.getId());
				purchReconcilDetail.setId(null);
				purchReconcilDetail.setOrderBillNo(purchRefundDetail.getOrderBillNo());
				purchReconcilDetail.setWorkBillNo(purchRefundDetail.getWorkBillNo());
				purchReconcilDetail.setWorkId(purchRefundDetail.getWorkId());
				purchReconcilDetail.setDeliveryTime(purchRefund.getCreateTime());
				purchReconcil.getDetailList().add(purchReconcilDetail);
			}
		}
		purchReconcil.setCompanyId(UserUtils.getCompanyId());
		purchReconcil.setCreateName(null);
		purchReconcil.setCreateEmployeeId(null);
		purchReconcil.setBillType(BillType.PURCH_PK);
		purchReconcil.setPrintCount(0);
		purchReconcil.setId(null);
		purchReconcil.setBillNo(null);
		purchReconcil.setReconcilTime(new Date());
		purchReconcil.setCreateTime(null);
		purchReconcil.setIsCheck(BoolValue.NO);
		purchReconcil.setIsCancel(BoolValue.NO);
		purchReconcil.setCheckTime(null);
		purchReconcil.setCheckUserName(null);
		return purchReconcil;
	}

	@Override
	public boolean forceComplete(TableType tableType, Long[] ids, Long[] stockIds, Long[] refundIds, BoolValue completeFlag)
	{
		if (tableType == TableType.MASTER)
		{
			serviceFactory.getCommonService().forceComplete(PurchReconcil.class, ids, completeFlag);

		}
		else if (tableType == TableType.DETAIL)
		{
			serviceFactory.getCommonService().forceComplete(PurchStockDetail.class, stockIds, completeFlag);
			serviceFactory.getCommonService().forceComplete(PurchRefundDetail.class, refundIds, completeFlag);
		}
		else
		{
			return false;
		}
		return true;

	}

	@Override
	@Transactional
	public Boolean delete(Long id)
	{
		try
		{
			PurchReconcil master = this.lockHasChildren(id);
			for (PurchReconcilDetail purchReconcilDetail : master.getDetailList())
			{
				if (purchReconcilDetail.getSourceBillType().getCode() == "PN")
				{
					DynamicQuery querySt = new CompanyDynamicQuery(PurchStockDetail.class);
					querySt.eq("id", purchReconcilDetail.getSourceDetailId());
					PurchStockDetail purchStockDetail = daoFactory.getCommonDao().getByDynamicQuery(querySt, PurchStockDetail.class);
					purchStockDetail.setReconcilQty(purchStockDetail.getReconcilQty().subtract(purchReconcilDetail.getQty()));
					daoFactory.getCommonDao().updateEntity(purchStockDetail);
				}
				if (purchReconcilDetail.getSourceBillType().getCode() == "PR")
				{
					DynamicQuery querySR = new CompanyDynamicQuery(PurchRefundDetail.class);
					querySR.eq("id", purchReconcilDetail.getSourceDetailId());
					PurchRefundDetail purchRefundDetail = daoFactory.getCommonDao().getByDynamicQuery(querySR, PurchRefundDetail.class);
					purchRefundDetail.setReconcilQty(purchRefundDetail.getReconcilQty().add(purchReconcilDetail.getQty()));
					daoFactory.getCommonDao().updateEntity(purchRefundDetail);
				}
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
	public SearchResult<PurchReconcil> findByCondition(QueryParam queryParam)
	{
		SearchResult<PurchReconcil> purchReconcilList = new SearchResult<PurchReconcil>();
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(PurchReconcil.class, "p");
			if (queryParam.getDateMin() != null)
			{
				query.ge("p.createTime", queryParam.getDateMin());
			}
			if (queryParam.getDateMax() != null)
			{
				query.le("p.createTime", queryParam.getDateMax());
			}
			if (StringUtils.isNotBlank(queryParam.getBillNo()))
			{
				query.like("p.billNo", "%" + queryParam.getBillNo() + "%");
			}
			if (StringUtils.isNotBlank(queryParam.getSupplierName()))
			{
				query.like("p.supplierName", "%" + queryParam.getSupplierName() + "%");
			}
			if (queryParam.getAuditFlag() != null)
			{
				query.eq("p.isCheck", queryParam.getAuditFlag());
			}
			Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
			if (employes.length > 0)
			{
				query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=p.supplierId");
				query.inArray("s.employeeId", employes);
			}
			query.eq("p.companyId", UserUtils.getCompanyId());
			query.desc("p.createTime");
			query.setPageIndex(queryParam.getPageNumber());
			query.setPageSize(queryParam.getPageSize());
			query.setIsSearchTotalCount(true);
			query.setQueryType(QueryType.JDBC);
			purchReconcilList = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, PurchReconcil.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return purchReconcilList;
	}

	@Override
	public SearchResult<PurchReconcilDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchReconcilDetail.class, "po");
		query.createAlias(PurchReconcil.class, "p");
		query.addProjection(Projections.property("po, p"));
		query.createAlias(Supplier.class, "s");
		query.eqProperty("po.masterId", "p.id");
		query.eqProperty("s.id", "p.supplierId");

		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("po.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSpecifications()))
		{
			query.like("po.specifications", "%" + queryParam.getSpecifications() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
		{
			query.like("po.sourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getOrderBillNo()))
		{
			query.like("po.orderBillNo", "%" + queryParam.getOrderBillNo() + "%");
		}

		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("s.employeeId", employes);
		}
		if (null != queryParam.getDateMin())
		{
			query.ge("p.createTime", queryParam.getDateMin());
		}
		if (null != queryParam.getDateMax())
		{
			query.le("p.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("p.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getMaterialName()))
		{
			query.like("po.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (null != queryParam.getEmployeeId())
		{
			query.eq("p.employeeId", queryParam.getEmployeeId());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("p.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (null != queryParam.getSupplierClassId())
		{
			query.eq("s.supplierClassId", queryParam.getSupplierClassId());
		}
		if (null != queryParam.getMaterialClassId())
		{
			query.eq("po.materialClassId", queryParam.getMaterialClassId());
		}
		if (null != queryParam.getAuditFlag())
		{
			query.eq("p.isCheck", queryParam.getAuditFlag());
		}

		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("p.createTime");
		query.setIsSearchTotalCount(true);
		query.eq("po.companyId", UserUtils.getCompanyId());
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<PurchReconcilDetail> result = new SearchResult<PurchReconcilDetail>();
		result.setResult(new ArrayList<PurchReconcilDetail>());
		for (Object[] c : temp_result.getResult())
		{
			PurchReconcilDetail detail = (PurchReconcilDetail) c[0];
			detail.setMaster((PurchReconcil) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/**
	 * <pre>
	 * 查询库存量大于对账量的记录
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:19:53, zhengby
	 */
	@Override
	public List<PurchStockDetail> findStockQtyReconcil(List<Long> ids)
	{
		List<PurchStockDetail> list = Lists.newArrayList();
		DynamicQuery queryStock = new CompanyDynamicQuery(PurchStockDetail.class, "ps");
		queryStock.createAlias(PurchStock.class, JoinType.LEFTJOIN, "p", "p.id=ps.masterId");
		/**
		 * 已入库数量大于已对账数量
		 */
		if (ids.size() > 0)
		{
			queryStock.add(Restrictions.gtProperty("qty", "reconcilQty"));
			queryStock.eq("ps.isForceComplete", BoolValue.NO);
			queryStock.eq("p.isForceComplete", BoolValue.NO);
			queryStock.in("ps.id", ids);
			SearchResult<PurchStockDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(queryStock, PurchStockDetail.class);
			list.addAll(result.getResult());
		}
		return list;
	}

	/**
	 * <pre>
	 * 查询退货量大于已对账量的记录
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:20:48, zhengby
	 */
	@Override
	public List<PurchRefundDetail> findRefundQtyReconcil(List<Long> ids)
	{
		List<PurchRefundDetail> list = Lists.newArrayList();
		DynamicQuery queryRefund = new CompanyDynamicQuery(PurchRefundDetail.class, "pr");
		queryRefund.createAlias(PurchRefund.class, JoinType.LEFTJOIN, "p", "p.id=pr.masterId");
		/**
		 * 退货数量大于已对账数量
		 */
		if (ids.size() > 0)
		{
			queryRefund.add(Restrictions.gtProperty("qty", "reconcilQty"));
			queryRefund.eq("pr.isForceComplete", BoolValue.NO);
			queryRefund.eq("p.isForceComplete", BoolValue.NO);
			queryRefund.in("pr.id", ids);
			SearchResult<PurchRefundDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(queryRefund, PurchRefundDetail.class);
			list.addAll(result.getResult());
		}
		return list;
	}
}
