/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年1月11日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.entity.AbstractTableIdEntity;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.util.ObjectHelper;
import com.huayin.common.util.Reflections;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.UnitConvert;
import com.huayin.printmanager.persist.entity.finance.FinancePayment;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceiveDetail;
import com.huayin.printmanager.persist.entity.oem.OemDeliver;
import com.huayin.printmanager.persist.entity.oem.OemDeliverDetail;
import com.huayin.printmanager.persist.entity.oem.OemReconcil;
import com.huayin.printmanager.persist.entity.oem.OemReconcilDetail;
import com.huayin.printmanager.persist.entity.oem.OemReturn;
import com.huayin.printmanager.persist.entity.oem.OemReturnDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArrive;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturn;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturnDetail;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.produce.WorkReportTask;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcil;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchRefund;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStock;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.entity.sale.SaleDeliver;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcil;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReturn;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherOut;
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturn;
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturnDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplement;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplementDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTake;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTakeDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTransfer;
import com.huayin.printmanager.persist.entity.stock.StockProductIn;
import com.huayin.printmanager.persist.entity.stock.StockProductInDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductTransfer;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BeginBillType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InitStep;
import com.huayin.printmanager.persist.enumerate.OfferSettingType;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.UpdateNameType;
import com.huayin.printmanager.service.CommonService;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架 - 缓存常用功能
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月11日
 */
@Service
@Lazy
public class CommonServiceImpl extends BaseServiceImpl implements CommonService
{
	@Override
	@Transactional
	public boolean clearAllData(String companyId)
	{
		for (BasicType type : BasicType.values())
		{
			DynamicQuery query = new DynamicQuery(type.getCla());
			query.eq("companyId", companyId);
			List<? extends BaseBasicTableEntity> allList = serviceFactory.getDaoFactory().getCommonDao().lockByDynamicQuery(query, type.getCla(), LockType.LOCK_WAIT);
			serviceFactory.getDaoFactory().getCommonDao().deleteAllEntity(allList);
			UserUtils.clearCacheBasic(type,companyId);
		}

		// 清除所有基础资料关联数据
		List<String> namedQueryList = new ArrayList<String>();
		namedQueryList.add("clear.customerAddress");
		namedQueryList.add("clear.customerPayer");
		namedQueryList.add("clear.supplierAddress");
		namedQueryList.add("clear.supplierPayer");
		namedQueryList.add("clear.product_customer");
		// 清除所有报价单设置数据
		namedQueryList.add("clear.offer.offerBflute");
		namedQueryList.add("clear.offer.offerMachine");
		namedQueryList.add("clear.offer.offerFormula");
		namedQueryList.add("clear.offer.offerStartPrint");
		namedQueryList.add("clear.offer.offerPaper");
		namedQueryList.add("clear.offer.offerPreprint");
		namedQueryList.add("clear.offer.offerProcedure");
		namedQueryList.add("clear.offer.offerProfit");
		namedQueryList.add("clear.offer.offerWaste");

		// 清除所有工单部件
		namedQueryList.add("clear.produce.workPart");
		namedQueryList.add("clear.produce.workPack");
		// 清除所有销售部件
		namedQueryList.add("clear.sale.saleOrderPart");
		namedQueryList.add("clear.sale.saleOrderPack");

		for (String nameQuery : namedQueryList)
		{
			serviceFactory.getDaoFactory().getCommonDao().execNamedQuery(nameQuery, companyId);
		}
		// 清空所有单据数据
		clearAllBillData(companyId);
		return true;
	}

	@Override
	@Transactional
	public boolean clearAllBillData(String companyId)
	{
		if (StringUtils.isBlank(companyId))
		{
			return false;
		}
		for (BillType billType : BillType.values())
		{
			DynamicQuery query = new DynamicQuery(billType.getCla());
			query.eq("companyId", companyId);
			List<? extends BaseBillMasterTableEntity> allList = serviceFactory.getDaoFactory().getCommonDao().lockByDynamicQuery(query, billType.getCla(), LockType.LOCK_WAIT);
			serviceFactory.getDaoFactory().getCommonDao().deleteAllEntity(allList);

			if (billType == BillType.PRODUCE_MO || billType == BillType.PRODUCE_PROOFING || billType == BillType.PRODUCE_SUPPLEMENT || billType == BillType.PRODUCE_TURNING)
			{// 生产工单明细，需要特殊处理
				continue;
			}
			else
			{
				DynamicQuery detail_query = new DynamicQuery(billType.getDetailCla());
				detail_query.eq("companyId", companyId);
				List<? extends BaseBillDetailTableEntity> detailAllList = serviceFactory.getDaoFactory().getCommonDao().lockByDynamicQuery(detail_query, billType.getDetailCla(), LockType.LOCK_WAIT);
				serviceFactory.getDaoFactory().getCommonDao().deleteAllEntity(detailAllList);

			}
		}

		// 清除所有单据关联数据(成品库存表，材料库存表，成品库存流水表，材料库存流水表，账号流水表)
		List<String> namedQueryList = new ArrayList<String>();
		namedQueryList.add("clear.stockProduct");
		namedQueryList.add("clear.stockProductLog");
		namedQueryList.add("clear.stockMaterial");
		namedQueryList.add("clear.stockMaterialLog");
		namedQueryList.add("clear.paymentAdvanceLog");
		namedQueryList.add("clear.receiveAdvanceLog");
		namedQueryList.add("clear.accountLog");
		namedQueryList.add("clear.account.reset");
		namedQueryList.add("clear.supplier.reset");
		namedQueryList.add("clear.customer.reset");
		namedQueryList.add("clear.warehouse.reset");

		// 加入清除工单数据
		namedQueryList.add("clear.produce.work");
		namedQueryList.add("clear.produce.workMaterial");
		namedQueryList.add("clear.produce.workProcedure");
		namedQueryList.add("clear.produce.workPart");
		namedQueryList.add("clear.produce.workProduct");
		namedQueryList.add("clear.produce.workPack");
		namedQueryList.add("clear.produce.workPart2Product");

		namedQueryList.add("clear.produce.reportTask");
		namedQueryList.add("clear.produce.report");
		namedQueryList.add("clear.produce.reportDetail");

		// 加入清除报价单数据
		namedQueryList.add("clear.offer.offerOrder");
		namedQueryList.add("clear.offer.offerPart");
		namedQueryList.add("clear.offer.offerPartProcedure");
		namedQueryList.add("clear.offer.offerOrderQuoteInner");
		namedQueryList.add("clear.offer.offerOrderQuoteOut");

		// 加入清楚销售订单工序材料数据
		namedQueryList.add("clear.sale.saleOrderMaterial");
		namedQueryList.add("clear.sale.saleOrderPack");
		namedQueryList.add("clear.sale.saleOrderPart");
		namedQueryList.add("clear.sale.saleOrderPart2Product");
		namedQueryList.add("clear.sale.saleOrderProcedure");

		for (String nameQuery : namedQueryList)
		{
			serviceFactory.getDaoFactory().getCommonDao().execNamedQuery(nameQuery, companyId);
		}
		return true;
	}

	@Override
	@Transactional
	public boolean resetCompanyState(InitStep initType, String companyId)
	{
		if (StringUtils.isBlank(companyId))
		{
			return false;
		}
		
		for (BasicType type : BasicType.values())
		{
			DynamicQuery query = new DynamicQuery(type.getCla());
			query.eq("companyId", companyId);
			List<? extends BaseBasicTableEntity> allList = serviceFactory.getDaoFactory().getCommonDao().lockByDynamicQuery(query, type.getCla(), LockType.LOCK_WAIT);
			if (allList.size() > 0)
			{
				throw new BusinessException("单据未清除");
			}
		}
	
		Company company = daoFactory.getCommonDao().lockObject(Company.class, companyId);
		company.setInitStep(initType);
		return true;
	}
	
	@Override
	@Transactional
	public boolean delete(BasicType basicType, Long id)
	{
		if (!isUsed(basicType, id))
		{
			DynamicQuery query = new CompanyDynamicQuery(basicType.getCla());
			query.eq("id", id);
			BaseBasicTableEntity master = serviceFactory.getDaoFactory().getCommonDao().lockByDynamicQuery(query, basicType.getCla(), LockType.LOCK_WAIT).get(0);
			serviceFactory.getDaoFactory().getCommonDao().deleteEntity(master);
			UserUtils.clearCacheBasic(basicType);
		}
		else
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean isUsed(BasicType basicType, Long id)
	{
		Class<? extends AbstractTableIdEntity>[] refrenceClaArray = basicType.getRefrenceClaArray();
		for (Class<? extends AbstractTableIdEntity> cla : refrenceClaArray)
		{
			DynamicQuery query = new CompanyDynamicQuery(cla);
			query.addProjection(Projections.count());
			if (basicType == BasicType.UNIT && cla == Material.class)
			{
				query.add(Restrictions.or(Restrictions.eq("stockUnitId", id), Restrictions.eq("valuationUnitId", id)));
			}
			else if (basicType == BasicType.UNIT && cla == UnitConvert.class)
			{
				query.add(Restrictions.or(Restrictions.eq("sourceUnitId", id), Restrictions.eq("conversionUnitId", id)));
			}
			else if (basicType == BasicType.EMPLOYEE && (cla == StockMaterialTake.class || cla == StockMaterialOtherOut.class || cla == StockMaterialSupplement.class))
			{
				query.add(Restrictions.or(Restrictions.eq("sendEmployeeId", id), Restrictions.eq("receiveEmployeeId", id)));
			}
			else if (basicType == BasicType.EMPLOYEE && cla == StockMaterialReturn.class)
			{
				query.add(Restrictions.or(Restrictions.eq("returnEmployeeId", id), Restrictions.eq("receiveEmployeeId", id)));
			}
			else if (basicType == BasicType.WAREHOUSE && (cla == StockMaterialTransfer.class || cla == StockProductTransfer.class))
			{
				query.add(Restrictions.or(Restrictions.eq("outWarehouseId", id), Restrictions.eq("inWarehouseId", id)));
			}
			else
			{
				query.eq(basicType.getRefrenceColumnName(), id);
			}
			int count = serviceFactory.getDaoFactory().getCommonDao().countByDynamicQuery(query, cla);
			if (count > 0)
			{
				return true;
			}
			else
			{
				continue;
			}
		}
		return false;
	}

	@Override
	public boolean isUsed(BeginBillType basicType, List<Long> ids)
	{
		Class<? extends AbstractTableIdEntity>[] refrenceClaArray = basicType.getRefrenceClaArray();
		for (Class<? extends AbstractTableIdEntity> cla : refrenceClaArray)
		{
			DynamicQuery query = new CompanyDynamicQuery(cla);
			query.addProjection(Projections.count());
			query.in(basicType.getRefrenceColumnName(), ids);
			if (cla == FinancePayment.class || cla == FinanceReceive.class)
			{
				query.eq("isCancel", BoolValue.NO);
			}
			int count = serviceFactory.getDaoFactory().getCommonDao().countByDynamicQuery(query, cla);
			if (count > 0)
			{
				return true;
			}
			else
			{
				continue;
			}
		}
		return false;
	}

	@Override
	@Transactional
	public boolean audit(BillType billType, Long id, BoolValue flag)
	{
		DynamicQuery query = new CompanyDynamicQuery(billType.getCla());
		query.eq("id", id);
		BaseBillMasterTableEntity order = daoFactory.getCommonDao().getByDynamicQuery(query, billType.getCla());

		// BaseBillMasterTableEntity order = serviceFactory.getDaoFactory().getCommonDao().getEntity(billType.getCla(),id);
		if (flag == BoolValue.YES)
		{// 审核
			// 判断是否已审核
			if (order.getIsCheck() == BoolValue.YES)
			{
				throw new BusinessException("已审核");
			}
			order.setCheckTime(new Date());
			// 审核人优先去员工姓名
			Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
			if (e != null)
			{
				order.setCheckUserName(e.getName());
			}
			else
			{
				order.setCheckUserName(UserUtils.getUser().getUserName());
			}
		}
		else
		{// 反审核
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核状态的进行反审核
				if (isUsed(billType, id))
				{// 反审核失败，已被下下游单据引用,
					return false;
				}
				order.setCheckTime(null);
				order.setCheckUserName(null);
			}
			else
			{
				throw new BusinessException("已反审核");
			}
		}
		order.setIsCheck(flag);

		serviceFactory.getDaoFactory().getCommonDao().updateEntity(order);
		return true;
	}

	@Override
	public boolean isUsed(BillType billType, Long id)
	{
		Class<? extends BaseBillDetailTableEntity>[] refrenceClaArray = billType.getRefrenceClaArray();
		for (Class<? extends BaseBillDetailTableEntity> subCla : refrenceClaArray)
		{
			DynamicQuery query = new CompanyDynamicQuery(subCla, "detail");
			if (subCla == FinancePaymentDetail.class)
			{
				query.createAlias(FinancePayment.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));
			}
			else if (subCla == FinanceReceiveDetail.class)
			{
				query.createAlias(FinanceReceive.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

			}
			else if (subCla == FinanceWriteoffPaymentDetail.class)
			{
				query.createAlias(FinanceWriteoffPayment.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

			}
			else if (subCla == FinanceWriteoffReceiveDetail.class)
			{
				query.createAlias(FinanceWriteoffReceive.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));
			}

			query.addProjection(Projections.count());
			query.eq("detail.sourceBillType", billType);
			query.eq("detail.sourceId", id);
			int count = 0;
			if (subCla != StockMaterialReturnDetail.class && subCla != StockMaterialSupplementDetail.class)
			{
				count = serviceFactory.getDaoFactory().getCommonDao().countByDynamicQuery(query, subCla);
			}
			if (count > 0)
			{
				return true;
			}
			else
			{
				continue;
			}
		}
		// 如果是工单，则需要另外校验生产退料表
		if (billType == BillType.PRODUCE_MO || billType == BillType.PRODUCE_PROOFING || billType == BillType.PRODUCE_SUPPLEMENT || billType == BillType.PRODUCE_TURNING)
		{
			DynamicQuery queryPurch = new DynamicQuery(PurchOrderDetail.class);
			queryPurch.addProjection(Projections.count());
			queryPurch.eq("sourceBillType", billType);
			queryPurch.eq("sourceId", id);
			int count = serviceFactory.getDaoFactory().getCommonDao().countByDynamicQuery(queryPurch, PurchOrderDetail.class);
			if (count > 0)
			{
				return true;
			}
			// 检查退料单
			DynamicQuery queryReturn = new DynamicQuery(StockMaterialReturn.class);
			queryReturn.addProjection(Projections.count());
			queryReturn.eq("workBillType", billType);
			queryReturn.eq("workId", id);
			count = serviceFactory.getDaoFactory().getCommonDao().countByDynamicQuery(queryReturn, StockMaterialReturn.class);
			if (count > 0)
			{
				return true;
			}
			// 检查补料单
			DynamicQuery querySupplement = new DynamicQuery(StockMaterialSupplement.class);
			querySupplement.addProjection(Projections.count());
			querySupplement.eq("workBillType", billType);
			querySupplement.eq("workId", id);
			count = serviceFactory.getDaoFactory().getCommonDao().countByDynamicQuery(querySupplement, StockMaterialSupplement.class);
			if (count > 0)
			{
				return true;
			}
			// 检查成品入库列表
			DynamicQuery queryStockProductIn = new DynamicQuery(StockProductIn.class);
			queryStockProductIn.addProjection(Projections.count());
			queryStockProductIn.eq("workBillType", billType);
			queryStockProductIn.eq("workId", id);
			count = serviceFactory.getDaoFactory().getCommonDao().countByDynamicQuery(queryStockProductIn, StockProductIn.class);
			if (count > 0)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional
	public boolean forceDetailComplete(Class<? extends BaseBillDetailTableEntity> cla, Long[] ids, BoolValue flag)
	{
		DynamicQuery query = new CompanyDynamicQuery(cla);
		query.in("id", Arrays.asList(ids));
		List<? extends BaseBillDetailTableEntity> detailList = serviceFactory.getDaoFactory().getCommonDao().lockByDynamicQuery(query, cla, LockType.LOCK_WAIT);
		for (BaseBillDetailTableEntity detail : detailList)
		{
			detail.setIsForceComplete(flag);
		}
		serviceFactory.getDaoFactory().getCommonDao().updateAllEntity(detailList);
		return true;
	}

	@Override
	@Transactional
	public boolean forceMasterComplete(Class<? extends BaseBillMasterTableEntity> cla, Long[] ids, BoolValue flag)
	{
		DynamicQuery query = new CompanyDynamicQuery(cla);
		query.in("id", Arrays.asList(ids));
		List<? extends BaseBillMasterTableEntity> masterList = serviceFactory.getDaoFactory().getCommonDao().lockByDynamicQuery(query, cla, LockType.LOCK_WAIT);
		for (BaseBillMasterTableEntity master : masterList)
		{
			master.setIsForceComplete(flag);
		}
		serviceFactory.getDaoFactory().getCommonDao().updateAllEntity(masterList);

		return true;
	}

	@Override
	@Transactional
	public boolean forceComplete(String companyId, Class<? extends BaseBillTableEntity> cla, Long[] ids, BoolValue flag)
	{
		if (ids != null && ids.length > 0)
		{
			DynamicQuery query = new DynamicQuery(cla);
			query.eq("companyId", companyId);
			query.in("id", Arrays.asList(ids));
			List<? extends BaseBillTableEntity> tableList = serviceFactory.getDaoFactory().getCommonDao().lockByDynamicQuery(query, cla, LockType.LOCK_WAIT);
			for (BaseBillTableEntity table : tableList)
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

	@Override
	@Transactional
	public boolean forceComplete(Class<? extends BaseBillTableEntity> cla, Long[] ids, BoolValue flag)
	{
		return this.forceComplete(UserUtils.getCompanyId(), cla, ids, flag);
	}

	@Override
	@Transactional
	public boolean forceComplete2(Class<? extends BaseBillTableEntity> cla, List<Long> ids, BoolValue flag)
	{
		if (ids != null && ids.size() > 0)
		{
			DynamicQuery query = new CompanyDynamicQuery(cla);
			query.in("id", ids);
			List<? extends BaseBillTableEntity> tableList = serviceFactory.getDaoFactory().getCommonDao().lockByDynamicQuery(query, cla, LockType.LOCK_WAIT);
			for (BaseBillTableEntity table : tableList)
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

	@Override
	public int getNextSort(BasicType type)
	{
		DynamicQuery query = new CompanyDynamicQuery(type.getCla());
		query.addProjection(Projections.max("sort"));
		int sort = daoFactory.getCommonDao().countByDynamicQuery(query, BaseBasicTableEntity.class);
		return sort + 1;
	}

	@Override
	public String getNextCode(BasicType type)
	{
		int codeMax = 0;
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(type.getCla());
			query.setPageIndex(1);
			query.setPageSize(1);
			query.desc("code");
			SearchResult<? extends BaseBasicTableEntity> result = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQueryPage(query, type.getCla());
			if (result != null && result.getResult() != null && result.getResult().size() > 0)
			{

				codeMax = Integer.parseInt(String.valueOf(ObjectHelper.getPropertyValue(result.getResult().get(0), "code")).replaceAll("\\D", ""));

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return type.getCodePrefix() + new DecimalFormat("000000").format(codeMax + 1);
	}

	@Override
	public List<BaseBasicTableEntity> getBasicInfoList(BasicType type)
	{
		DynamicQuery query = new CompanyDynamicQuery(type.getCla());
		query.asc("sort");
		return serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, BaseBasicTableEntity.class);
	}

	@Override
	public List<BaseBasicTableEntity> getCommBasicInfoList(BasicType type)
	{
		DynamicQuery query = new DynamicQuery(type.getCla());
		query.eq("companyId", SystemConfigUtil.getInitCompanyId());
		query.asc("sort");
		return serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, BaseBasicTableEntity.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.CommonService#getCommBasicOfferList(com.huayin.printmanager.persist.enumerate.
	 * OfferType, com.huayin.printmanager.persist.enumerate.OfferSettingType)
	 */
	@Override
	public List<BaseBasicTableEntity> getCommBasicOfferList(OfferType type, OfferSettingType settingType)
	{
		DynamicQuery query = new CompanyDynamicQuery(settingType.getCla());
		query.eq("offerTpye", type);
		query.asc("sort");
		return serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, BaseBasicTableEntity.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, List> findRefBillNo(BillType billType, Long id)
	{
		Class<? extends BaseBillDetailTableEntity>[] refrenceClaArray = billType.getRefrenceClaArray();
		Map<String, List> map = new HashMap<String, List>();
		for (Class<? extends BaseBillDetailTableEntity> subCla : refrenceClaArray)
		{
			DynamicQuery query = new CompanyDynamicQuery(subCla, "detail");
			query.addProjection(Projections.property("master"));
			if (subCla != StockMaterialSupplementDetail.class && subCla != StockMaterialReturnDetail.class)
			{
				query.eq("detail.sourceBillType", billType);
				query.eq("detail.sourceId", id);
			}

			// 工单
			if (subCla == WorkProduct.class)
			{
				query.createAlias(Work.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, Work.class);

				List<Work> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{
					// 去重复单号
					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						Work n = (Work) obj;
						for (Work _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}
						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}

				if (CollectionUtils.isNotEmpty(newList))
				{

					map.put(BillType.PRODUCE_MO.getCode(), newList);
				}
			}

			// 送货单
			if (subCla == SaleDeliverDetail.class)
			{
				query.createAlias(SaleDeliver.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, SaleDeliver.class);
				List<SaleDeliver> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{
					// 去重复单号
					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						SaleDeliver n = (SaleDeliver) obj;
						for (SaleDeliver _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}
						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.SALE_IV.getCode(), newList);
				}
			}

			// 销售退货单
			if (subCla == SaleReturnDetail.class)
			{
				query.createAlias(SaleReturn.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, SaleReturn.class);

				List<SaleReturn> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{
					// 去重复单号
					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						SaleReturn n = (SaleReturn) obj;
						for (SaleReturn _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}
						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.SALE_IR.getCode(), newList);
				}
			}

			// 销售对账单
			if (subCla == SaleReconcilDetail.class)
			{
				query.createAlias(SaleReconcil.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, SaleReconcil.class);
				List<SaleReconcil> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{
					// 去重复单号
					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						SaleReconcil n = (SaleReconcil) obj;
						for (SaleReconcil _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}
						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.SALE_SK.getCode(), newList);
				}
			}

			// 收款单
			if (subCla == FinanceReceiveDetail.class)
			{
				query.createAlias(FinanceReceive.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, FinanceReceive.class);
				List<FinanceReceive> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{
					// 去重复单号
					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						FinanceReceive n = (FinanceReceive) obj;
						for (FinanceReceive _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}
						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.FINANCE_REC.getCode(), newList);
				}
			}

			// 收款核销单
			if (subCla == FinanceWriteoffReceiveDetail.class)
			{
				query.createAlias(FinanceWriteoffReceive.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, FinanceWriteoffReceive.class);
				// 去重复单号
				List<FinanceWriteoffReceive> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{
					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						FinanceWriteoffReceive n = (FinanceWriteoffReceive) obj;
						for (FinanceWriteoffReceive _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.FINANCE_WRITEOFF_RC.getCode(), newList);
				}
			}

			// 采购入库单
			if (subCla == PurchStockDetail.class)
			{
				query.createAlias(PurchStock.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, PurchStock.class);
				// 去重复单号
				List<PurchStock> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						PurchStock n = (PurchStock) obj;
						for (PurchStock _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}

				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.PURCH_PN.getCode(), newList);
				}
			}

			// 采购退货单
			if (subCla == PurchRefundDetail.class)
			{
				query.createAlias(PurchRefund.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, PurchRefund.class);
				// 去重复单号
				List<PurchRefund> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						PurchRefund n = (PurchRefund) obj;
						for (PurchRefund _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.PURCH_PR.getCode(), newList);
				}
			}

			// 采购对账单
			if (subCla == PurchReconcilDetail.class)
			{
				query.createAlias(PurchReconcil.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, PurchReconcil.class);
				// 去重复单号
				List<PurchReconcil> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						PurchReconcil n = (PurchReconcil) obj;
						for (PurchReconcil _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}

				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.PURCH_PK.getCode(), newList);
				}
			}

			// 付款单
			if (subCla == FinancePaymentDetail.class)
			{
				query.createAlias(FinancePayment.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, FinancePayment.class);
				// 去重复单号
				List<FinancePayment> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						FinancePayment n = (FinancePayment) obj;
						for (FinancePayment _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.FINANCE_PAY.getCode(), newList);
				}
			}

			// 付款核销单
			if (subCla == FinanceWriteoffPaymentDetail.class)
			{
				query.createAlias(FinanceWriteoffPayment.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, FinanceWriteoffPayment.class);
				// 去重复单号
				List<FinanceWriteoffPayment> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						FinanceWriteoffPayment n = (FinanceWriteoffPayment) obj;
						for (FinanceWriteoffPayment _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.FINANCE_WRITEOFF_PAY.getCode(), newList);
				}
			}

			// 发外到货单
			if (subCla == OutSourceArriveDetail.class)
			{
				query.createAlias(OutSourceArrive.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, OutSourceArrive.class);
				// 去重复单号
				List<OutSourceArrive> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						OutSourceArrive n = (OutSourceArrive) obj;
						for (OutSourceArrive _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.OUTSOURCE_OA.getCode(), newList);
				}
			}

			// 发外退货单
			if (subCla == OutSourceReturnDetail.class)
			{
				query.createAlias(OutSourceReturn.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, OutSourceReturn.class);
				// 去重复单号
				List<OutSourceReturn> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						OutSourceReturn n = (OutSourceReturn) obj;
						for (OutSourceReturn _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.OUTSOURCE_OR.getCode(), newList);
				}
			}

			// 发外对账单
			if (subCla == OutSourceReconcilDetail.class)
			{
				query.createAlias(OutSourceReconcil.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, OutSourceReconcil.class);
				// 去重复单号
				List<OutSourceReconcil> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						OutSourceReconcil n = (OutSourceReconcil) obj;
						for (OutSourceReconcil _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.OUTSOURCE_OC.getCode(), newList);
				}
			}

			// 采购订单
			if (subCla == PurchOrderDetail.class)
			{
				query.createAlias(PurchOrder.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, PurchOrder.class);
				// 去重复单号
				List<PurchOrder> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						PurchOrder n = (PurchOrder) obj;
						for (PurchOrder _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.PURCH_PO.getCode(), newList);
				}
			}

			// 成品入库单
			if (subCla == StockProductInDetail.class)
			{
				query.createAlias(StockProductIn.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, StockProductIn.class);
				// 去重复单号
				List<StockProductIn> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						StockProductIn n = (StockProductIn) obj;
						for (StockProductIn _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.STOCK_SPIN.getCode(), newList);
				}
			}

			// 生产领料单
			if (subCla == StockMaterialTakeDetail.class)
			{
				query.createAlias(StockMaterialTake.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, StockMaterialTake.class);
				// 去重复单号
				List<StockMaterialTake> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						StockMaterialTake n = (StockMaterialTake) obj;
						for (StockMaterialTake _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.STOCK_MR.getCode(), newList);
				}
			}

			// 退料单
			if (subCla == StockMaterialReturnDetail.class)
			{
				query.createAlias(StockMaterialReturn.class, "master");
				query.eq("master.workBillType", billType);
				query.eq("master.workId", id);
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, StockMaterialReturn.class);
				// 去重复单号
				List<StockMaterialReturn> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						StockMaterialReturn n = (StockMaterialReturn) obj;
						for (StockMaterialReturn _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.STOCK_RM.getCode(), newList);
				}
			}

			// 补料单
			if (subCla == StockMaterialSupplementDetail.class)
			{
				query.createAlias(StockMaterialSupplement.class, "master");
				query.eq("master.workBillType", billType);
				query.eq("master.workId", id);
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, StockMaterialSupplement.class);
				// 去重复单号
				List<StockMaterialSupplement> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						StockMaterialSupplement n = (StockMaterialSupplement) obj;
						for (StockMaterialSupplement _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.STOCK_SM.getCode(), newList);
				}
			}

			// 发外加工单
			if (subCla == OutSourceProcessDetail.class)
			{
				query.createAlias(OutSourceProcess.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, OutSourceProcess.class);
				// 去重复单号
				List<OutSourceProcess> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{

					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						OutSourceProcess n = (OutSourceProcess) obj;
						for (OutSourceProcess _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}

						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.OUTSOURCE_OP.getCode(), newList);
				}
			}
			// 代工送货单
			if (subCla == OemDeliverDetail.class)
			{
				query.createAlias(OemDeliver.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, OemDeliver.class);
				List<OemDeliver> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{
					// 去重复单号
					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						OemDeliver n = (OemDeliver) obj;
						for (OemDeliver _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}
						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.OEM_ED.getCode(), newList);
				}
			}
			// 代工退货单
			if (subCla == OemReturnDetail.class)
			{
				query.createAlias(OemReturn.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, OemReturn.class);
				List<OemReturn> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{
					// 去重复单号
					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						OemReturn n = (OemReturn) obj;
						for (OemReturn _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}
						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.OEM_ER.getCode(), newList);
				}
			}
			// 代工对账单
			if (subCla == OemReconcilDetail.class)
			{
				query.createAlias(OemReconcil.class, "master");
				query.eqProperty("detail.masterId", "master.id");
				query.add(Restrictions.or(Restrictions.and(Restrictions.eq("master.isCheck", BoolValue.YES), Restrictions.eq("master.isCancel", BoolValue.NO)), Restrictions.eq("master.isCheck", BoolValue.NO)));

				List list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, OemReconcil.class);
				List<OemReconcil> newList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(list))
				{
					// 去重复单号
					for (Object obj : list)
					{
						List<Boolean> repetList = new ArrayList<>();
						OemReconcil n = (OemReconcil) obj;
						for (OemReconcil _n : newList)
						{
							if ((_n.getBillNo()).equals(n.getBillNo()))
							{
								repetList.add(true);
							}
						}
						if (!repetList.contains(true))
						{
							newList.add(n);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(newList))
				{
					map.put(BillType.OEM_EC.getCode(), newList);
				}
			}
		}

		return map;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.CommonService#updataName(java.lang.Long, java.lang.String,
	 * com.huayin.printmanager.persist.enumerate.UpdateNameType)
	 */
	@Override
	@Transactional
	public void updateBasicName(Long id, String newName, UpdateNameType type)
	{
		/**
		 * v6.9 变更客户名称、供应商名称、材料名称、工序名称、产品名称
		 * 1.首先根据id查询基础资料中的对象
		 * 2.更新对象的name字段
		 * 3.清除缓存
		 * 4.根据id查询引用表中所有引用过的对象
		 * 5.更新对象的name字段
		 * 6.更新对象
		 */
		DynamicQuery query = new CompanyDynamicQuery(type.getBasicCla());
		query.eq("id", id);
		/* 1.首先根据id查询基础资料中的对象 */
		Object obj = serviceFactory.getDaoFactory().getCommonDao().lockObject(type.getBasicCla(), id);
		/* 2.更新对象的name字段 */
		Reflections.invokeSetter(obj, "name", newName);
		if (type.getBasicCla() == Customer.class)
		{
			Customer reObj = (Customer) obj;
			daoFactory.getCommonDao().updateEntity(reObj);
		}
		else if (type.getBasicCla() == Supplier.class)
		{
			Supplier reObj = (Supplier) obj;
			daoFactory.getCommonDao().updateEntity(reObj);
		}
		else if (type.getBasicCla() == Material.class)
		{
			Material reObj = (Material) obj;
			daoFactory.getCommonDao().updateEntity(reObj);
		}
		else if (type.getBasicCla() == Procedure.class)
		{
			Procedure reObj = (Procedure) obj;
			daoFactory.getCommonDao().updateEntity(reObj);
		}
		else if (type.getBasicCla() == Product.class)
		{
			Product reObj = (Product) obj;
			daoFactory.getCommonDao().updateEntity(reObj);
		}
		/* 3.更新之后清除一下缓存 */
		UserUtils.clearCompanyCache(UserUtils.getCompanyId(), type.getCacheName());
		UserUtils.clearCompanyCache(UserUtils.getCompanyId(), type.getCacheName() + "_map");
		// 更新完基础资料后再更新引用表
		for (Class<? extends BaseTableIdEntity> clz : type.getRefrenceClaArray())
		{
			/* 4.根据id查询引用表中所有引用过的对象 */
			List<? extends BaseTableIdEntity> list = null;
			DynamicQuery _query = new DynamicQuery(clz);
			query.eq("companyId", UserUtils.getCompanyId());
			if ((clz == WorkReportTask.class || clz == StockMaterialTakeDetail.class) && (type.getBasicCla() == Product.class || type.getBasicCla() == Customer.class))
			{
				_query.like(type.getRefrenceColumnName(), "%B" + id + "E%");
			}
			else
			{
				_query.eq(type.getRefrenceColumnName(), id);
			}
			list = serviceFactory.getDaoFactory().getCommonDao().lockByDynamicQuery(_query, clz, LockType.LOCK_WAIT);

			// 生产任务列表的特殊处理(本次版本之前的生产任务列表不能变更客户名称及产品名称，原因之前没有id字段)
			// 生产任务的客户id/产品id是多个客户/产品id合并一起的，所以利用定位id去定位名称
			if ((clz == WorkReportTask.class || clz == StockMaterialTakeDetail.class) && (type.getBasicCla() == Product.class || type.getBasicCla() == Customer.class))
			{
				for (Object _obj : list)
				{
					// 获取id
					String ids = (String) Reflections.getFieldValue(_obj, type.getRefrenceColumnName());
					String[] str = ids.split(",");
					// 获取需变更客户/产品id的下标index
					int i = 0;
					int index = 0;
					for (String s : str)
					{
						if (s.equals("B" + id + "E"))
						{
							index = i;
						}
						i++;
					}
					// 获取客户/产品名称
					String names = (String) Reflections.getFieldValue(_obj, type.getReplaceColunmName());
					String[] str2 = names.split(",");
					// 根据客户/产品id的下标可获取到对应的客户/产品名称
					String searchChars = str2[index];
					// 替换新名称
					String replaceChars = newName;
					String replacedNames = StringUtils.replace(names, searchChars, replaceChars);
					/* 5.更新对象的name字段 */
					Reflections.invokeSetter(_obj, type.getReplaceColunmName(), replacedNames);
				}
			}
			else
			{
				/* 5.更新对象的name字段 */
				for (Object _obj : list)
				{
					Reflections.invokeSetter(_obj, type.getReplaceColunmName(), newName);
				}
			}
			/* 6.更新对象 */
			if (!list.isEmpty())
			{
				daoFactory.getCommonDao().updateAllEntity(list);
			}
		}
	}

	/**
	 * <pre>
	 * 判断是否存在相同的名称
	 * </pre>
	 * @param name
	 * @param spec
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月8日 上午9:48:07, zhengby
	 */
	@Override
	public Boolean isExist(String name, UpdateNameType type)
	{
		List<?> _list = new ArrayList<>();

		DynamicQuery query = new CompanyDynamicQuery(type.getBasicCla());
		query.eq("name", name);
		_list = serviceFactory.getDaoFactory().getCommonDao().findEntityByDynamicQuery(query, type.getBasicCla());

		if (_list.isEmpty())
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
