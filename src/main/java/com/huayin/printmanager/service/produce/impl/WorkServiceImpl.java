/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年2月23日上午9:47:12
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.produce.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ConverterUtils;
import com.huayin.common.util.ObjectHelper;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.helper.ServiceHelper;
import com.huayin.printmanager.helper.service.CommonHelper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.produce.WorkPack;
import com.huayin.printmanager.persist.entity.produce.WorkPart;
import com.huayin.printmanager.persist.entity.produce.WorkPart2Product;
import com.huayin.printmanager.persist.entity.produce.WorkProcedure;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.produce.WorkReport;
import com.huayin.printmanager.persist.entity.produce.WorkReportDetail;
import com.huayin.printmanager.persist.entity.produce.WorkReportTask;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStock;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturn;
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturnDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplement;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplementDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTake;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTakeDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OutSourceType;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.persist.enumerate.ProductType;
import com.huayin.printmanager.persist.enumerate.ScheduleDataSource;
import com.huayin.printmanager.persist.enumerate.WorkMaterialType;
import com.huayin.printmanager.persist.enumerate.WorkProcedureType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.produce.WorkService;
import com.huayin.printmanager.service.produce.vo.WorkMaterialVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.QRCodeUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 生产管理 - 生产工单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日上午10:12:01, zhengby
 */
@Service
public class WorkServiceImpl extends BaseServiceImpl implements WorkService
{

	@Override
	public Work get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Work.class);
		query.eq("id", id);
		Work bean = daoFactory.getCommonDao().getByDynamicQuery(query, Work.class);

		// Work bean = daoFactory.getCommonDao().getEntity(Work.class, id);
		bean.setProductList(this.getProductListByWorkId(id));
		bean.setPartList(this.getPartListByWorkId(id));
		bean.setPack(this.getPackByWorkId(id));
		return bean;
	}

	@Override
	public Work get(String billNo)
	{
		DynamicQuery query = new CompanyDynamicQuery(Work.class);
		query.eq("billNo", billNo);
		Work bean = daoFactory.getCommonDao().getByDynamicQuery(query, Work.class);
		return bean;
	}

	@Override
	public WorkProduct getProduct(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class);
		query.eq("id", id);
		WorkProduct bean = daoFactory.getCommonDao().getByDynamicQuery(query, WorkProduct.class);

		// WorkProduct bean = daoFactory.getCommonDao().getEntity(WorkProduct.class, id);
		bean.setMaster(daoFactory.getCommonDao().getEntity(Work.class, bean.getMasterId()));
		DynamicQuery imQuery = new CompanyDynamicQuery(Product.class);
		imQuery.eq("id", bean.getProductId());
		Product product = daoFactory.getCommonDao().getByDynamicQuery(imQuery, Product.class);
		bean.setImgUrl(product.getImgUrl());
		return bean;

	}

	@Override
	public WorkPart getPart(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkPart.class);
		query.eq("id", id);
		WorkPart bean = daoFactory.getCommonDao().getByDynamicQuery(query, WorkPart.class);

		// WorkPart bean = daoFactory.getCommonDao().getEntity(WorkPart.class, id);
		bean.setMaster(daoFactory.getCommonDao().getEntity(Work.class, bean.getMasterId()));
		bean.setProductList(this.getPart2ProductByPartId(id));
		bean.setMaterialList(this.getWorkMaterialListByPartId(id));
		bean.setProcedureList(this.getProcedureListByPartId(id));
		return bean;
	}

	@Override
	public WorkPack getPack(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkPack.class);
		query.eq("id", id);
		WorkPack bean = daoFactory.getCommonDao().getByDynamicQuery(query, WorkPack.class);

		// WorkPack bean = daoFactory.getCommonDao().getEntity(WorkPack.class, id);
		bean.setMaster(daoFactory.getCommonDao().getEntity(Work.class, bean.getMasterId()));
		bean.setMaterialList(this.getWorkMaterialListByPartId(id));
		bean.setProcedureList(this.getProcedureListByPartId(id));
		return bean;
	}

	@Override
	public WorkProcedure getProcedure(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProcedure.class);
		query.eq("id", id);
		WorkProcedure bean = daoFactory.getCommonDao().getByDynamicQuery(query, WorkProcedure.class);

		// WorkProcedure bean = daoFactory.getCommonDao().getEntity(WorkProcedure.class, id);

		bean.setWork(daoFactory.getCommonDao().getEntity(Work.class, bean.getWorkId()));
		if (bean.getWorkProcedureType() == WorkProcedureType.PRODUCT)
		{// 产品工序
			bean.setWorkPack(daoFactory.getCommonDao().getEntity(WorkPack.class, bean.getParentId()));
		}
		else if (bean.getWorkProcedureType() == WorkProcedureType.PART)
		{// 部件工序
			WorkPart part = daoFactory.getCommonDao().getEntity(WorkPart.class, bean.getParentId());
			bean.setWorkPart(part);
		}
		return bean;
	}

	@Override
	public WorkMaterial getMaterial(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class);
		query.eq("id", id);
		WorkMaterial bean = daoFactory.getCommonDao().getByDynamicQuery(query, WorkMaterial.class);

		// WorkMaterial bean = daoFactory.getCommonDao().getEntity(WorkMaterial.class, id);

		bean.setWork(daoFactory.getCommonDao().getEntity(Work.class, bean.getWorkId()));
		if (bean.getWorkMaterialType() == WorkMaterialType.PRODUCT)
		{// 成品材料
			bean.setWorkPack(daoFactory.getCommonDao().getEntity(WorkPack.class, bean.getParentId()));
		}
		else if (bean.getWorkMaterialType() == WorkMaterialType.PART)
		{// 部件材料
			WorkPart part = daoFactory.getCommonDao().getEntity(WorkPart.class, bean.getParentId());
			bean.setWorkPart(part);

		}
		return bean;

	}

	@Override
	public List<WorkProduct> getProductListByWorkId(Long workId)
	{
		// DynamicQuery query_product = new CompanyDynamicQuery(WorkProduct.class,"wp");
		//
		// query_product.eq("masterId", workId);
		// return daoFactory.getCommonDao().findEntityByDynamicQuery(query_product, WorkProduct.class);

		DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class, "wp");
		query.addProjection(Projections.property("wp,p"));
		query.createAlias(Product.class, "p");
		query.eq("masterId", workId);
		query.eqProperty("p.id", "wp.productId");
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		List<WorkProduct> detailList = new ArrayList<WorkProduct>();

		for (Object[] c : temp_result.getResult())
		{
			WorkProduct detail = (WorkProduct) c[0];
			Product product = (Product) c[1];
			detail.setImgUrl(product.getImgUrl());
			detailList.add(detail);
		}
		return detailList;
	}

	@Override
	public List<WorkPart> getPartListByWorkId(Long workId)
	{
		DynamicQuery query_product = new CompanyDynamicQuery(WorkPart.class);
		query_product.eq("masterId", workId);
		List<WorkPart> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query_product, WorkPart.class);

		for (WorkPart part : list)
		{
			part.setMaterialList(this.getWorkMaterialListByPartId(part.getId()));
			part.setProcedureList(this.getProcedureListByPartId(part.getId()));
			part.setProductList(this.getPart2ProductByPartId(part.getId()));
		}
		return list;
	}

	@Override
	public WorkPack getPackByWorkId(Long workId)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkPack.class);
		query.eq("masterId", workId);
		WorkPack pack = daoFactory.getCommonDao().getByDynamicQuery(query, WorkPack.class);
		pack.setMaterialList(this.getWorkMaterialListByPackId(pack.getId()));
		pack.setProcedureList(this.getProcedureListByPackId(pack.getId()));
		return pack;

	}

	@Override
	public List<WorkPart2Product> getPart2ProductByPartId(Long partId)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkPart2Product.class);
		query.eq("workPartId", partId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkPart2Product.class);
	}

	@Override
	public List<WorkProcedure> getProcedureListByPartId(Long partId)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProcedure.class);
		query.eq("workProcedureType", WorkProcedureType.PART);
		query.eq("parentId", partId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkProcedure.class);
	}

	@Override
	public List<WorkMaterial> getWorkMaterialListByPartId(Long partId)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class);
		query.eq("workMaterialType", WorkMaterialType.PART);
		query.eq("parentId", partId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkMaterial.class);
	}

	@Override
	public List<WorkProcedure> getProcedureListByPackId(Long packId)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProcedure.class);
		query.eq("workProcedureType", WorkProcedureType.PRODUCT);
		query.eq("parentId", packId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkProcedure.class);
	}

	@Override
	public List<WorkMaterial> getWorkMaterialListByPackId(Long packId)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class);
		query.eq("workMaterialType", WorkMaterialType.PRODUCT);
		query.eq("parentId", packId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkMaterial.class);
	}

	@Override
	public List<WorkProcedure> getProcedureListByWorkId(Long workId)
	{
		DynamicQuery query_product = new CompanyDynamicQuery(WorkProcedure.class);
		query_product.eq("workId", workId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query_product, WorkProcedure.class);

	}

	@Override
	public List<WorkMaterial> getWorkMaterialListByWorkId(Long workId)
	{
		DynamicQuery query_product = new CompanyDynamicQuery(WorkMaterial.class);
		query_product.eq("workId", workId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query_product, WorkMaterial.class);
	}

	@Override
	@Transactional
	public Work save(Work order)
	{
		BoolValue flag = order.getIsCheck();				// 标识是否保存并审核
		order.setCompanyId(UserUtils.getCompanyId());
		order.setUserNo(UserUtils.getUser().getUserNo());
		order.setIsCheck(BoolValue.NO);							// 默认是未审核
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			order.setCreateName(e.getName());
		}
		else
		{
			order.setCreateName(UserUtils.getUserName());
		}
		order.setCreateEmployeeId(UserUtils.getEmployeeId());
		order.setCreateTime(new Date());
		/* order.setIsCheck(BoolValue.NO); */
		order.setIsForceComplete(BoolValue.NO);
		order.setPrintCount(0);
		if (order.getBillType() == BillType.PRODUCE_SUPPLEMENT)
		{
			Work sourceWork = serviceFactory.getDaoFactory().getCommonDao().lockObject(Work.class, order.getSourceWorkId());
			sourceWork.setSupplementCount(sourceWork.getSupplementCount() + 1);
			serviceFactory.getDaoFactory().getCommonDao().updateEntity(sourceWork);
			// order.setBillNo(sourceWork.getBillNo() + "补" + sourceWork.getSupplementCount());
		}
		else if (order.getBillType() == BillType.PRODUCE_TURNING)
		{
			Work sourceWork = serviceFactory.getDaoFactory().getCommonDao().lockObject(Work.class, order.getSourceWorkId());
			sourceWork.setTurningCount(sourceWork.getTurningCount() + 1);
			serviceFactory.getDaoFactory().getCommonDao().updateEntity(sourceWork);
			// order.setBillNo(sourceWork.getBillNo() + "翻" + sourceWork.getTurningCount());
		}
		if (order.getBillNo() == null)
		{
			order.setBillNo(UserUtils.createBillNo(BillType.PRODUCE_MO));
		}

		order = daoFactory.getCommonDao().saveEntity(order);
		// 保存产品列表信息
		for (WorkProduct product : order.getProductList())
		{
			product.setCompanyId(order.getCompanyId());
			product.setUserNo(order.getUserNo());
			product.setMasterId(order.getId());
			product.setIsForceComplete(BoolValue.NO);
			product.setInStockQty(0);
			product.setOutOfQty(0);
			product.setDeliverQty(0);
			product.setDeliverMoney(new BigDecimal(0));
			product.setDeliverSpareedQty(0);

			DynamicQuery query = new CompanyDynamicQuery(Customer.class);
			query.eq("id", product.getCustomerId());
			Customer customer = daoFactory.getCommonDao().getByDynamicQuery(query, Customer.class);
			if (customer != null)
			{
				TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), customer.getTaxRateId());

				// 不含税金额计算 不含税金额=（金额/(1+税率值/100)）
				BigDecimal noTaxMoney = product.getMoney().divide(new BigDecimal(1d + taxRate.getPercent() / 100d), 2);

				// 税额=(金额-不含税金额)
				BigDecimal tax = product.getMoney().subtract(noTaxMoney);

				// 不含税单价=(单价/(1+税率值/100))
				BigDecimal noTaxPrice = product.getPrice().divide(new BigDecimal(1d + taxRate.getPercent() / 100d), 4);
				product.setNoTaxMoney(noTaxMoney);
				product.setNoTaxPrice(noTaxPrice);
				product.setPercent(taxRate.getPercent());
				product.setTax(tax);
				product.setTaxRateId(customer.getTaxRateId());
			}
		
			StringBuilder sbLog = new StringBuilder();
			sbLog.append("SourceDetailId").append(product.getSourceDetailId()).append(",");
			if (product.getSourceDetailId() != null && product.getSourceDetailId() != 0l)
			{// 反写销售数量、备品数量
				SaleOrderDetail source = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, product.getSourceDetailId());
				source.setProduceedQty(source.getProduceedQty() + product.getSaleProduceQty());
				source.setProduceSpareedQty(source.getProduceSpareedQty() + product.getSpareProduceQty());
				daoFactory.getCommonDao().updateEntity(source);
				sbLog.append("ProduceedQty").append(source.getProduceedQty()).append(",");
				sbLog.append("SaleProduceQty").append(product.getSaleProduceQty()).append(",");
			}
			// TODO 这里是为了解决工单反写销售数量BUG，进行日志查看
			logger.info(sbLog.toString());
			
		}
		daoFactory.getCommonDao().saveAllEntity(order.getProductList());

		// 保存部件列表信息
		for (WorkPart part : order.getPartList())
		{
			part.setCompanyId(order.getCompanyId());
			part.setUserNo(order.getUserNo());
			part.setMasterId(order.getId());
			part.setIsForceComplete(BoolValue.NO);
			if (StringUtils.isBlank(part.getMachineName()))
			{
				part.setMachineId(null);
			}
			part = daoFactory.getCommonDao().saveEntity(part);

			for (WorkPart2Product p2p : part.getProductList())
			{
				p2p.setCompanyId(order.getCompanyId());
				p2p.setWorkId(order.getId());
				p2p.setWorkPartId(part.getId());
			}
			daoFactory.getCommonDao().saveAllEntity(part.getProductList());
			for (WorkMaterial material : part.getMaterialList())
			{
				material.setCompanyId(order.getCompanyId());
				material.setWorkId(order.getId());
				material.setWorkBillNo(order.getBillNo());
				material.setWorkMaterialType(WorkMaterialType.PART);
				material.setParentId(part.getId());
				material.setIsForceComplete(BoolValue.NO);
				material.setPurchQty(new BigDecimal(0));
				material.setTakeQty(new BigDecimal(0));
				material.setIsNotTake(BoolValue.NO);
				material.setIsNotPurch(BoolValue.NO);

			}
			daoFactory.getCommonDao().saveAllEntity(part.getMaterialList());

			for (WorkProcedure procedure : part.getProcedureList())
			{
				procedure.setCompanyId(order.getCompanyId());
				procedure.setWorkId(order.getId());
				procedure.setWorkBillNo(order.getBillNo());
				procedure.setWorkProcedureType(WorkProcedureType.PART);
				procedure.setParentId(part.getId());
				procedure.setIsForceComplete(BoolValue.NO);
				procedure.setOutOfQty(new BigDecimal(0));
				procedure.setArriveOfQty(new BigDecimal(0));
			}
			daoFactory.getCommonDao().saveAllEntity(part.getProcedureList());

		}

		// 保存成品信息
		order.getPack().setCompanyId(order.getCompanyId());
		order.getPack().setMasterId(order.getId());
		order.getPack().setUserNo(order.getUserNo());
		order.getPack().setIsForceComplete(BoolValue.NO);
		WorkPack pack = daoFactory.getCommonDao().saveEntity(order.getPack());
		for (WorkMaterial material : order.getPack().getMaterialList())
		{
			material.setCompanyId(order.getCompanyId());
			material.setWorkId(order.getId());
			material.setWorkBillNo(order.getBillNo());
			material.setWorkMaterialType(WorkMaterialType.PRODUCT);
			material.setParentId(pack.getId());
			material.setIsForceComplete(BoolValue.NO);
			material.setPurchQty(new BigDecimal(0));
			material.setTakeQty(new BigDecimal(0));
			material.setIsNotTake(BoolValue.NO);
			material.setIsNotPurch(BoolValue.NO);
		}
		daoFactory.getCommonDao().saveAllEntity(order.getPack().getMaterialList());

		for (WorkProcedure procedure : order.getPack().getProcedureList())
		{
			procedure.setCompanyId(order.getCompanyId());
			procedure.setWorkId(order.getId());
			procedure.setWorkBillNo(order.getBillNo());
			procedure.setWorkProcedureType(WorkProcedureType.PRODUCT);
			procedure.setParentId(pack.getId());
			procedure.setIsForceComplete(BoolValue.NO);
			// procedure.setIsOutSource(BoolValue.NO);
			procedure.setOutOfQty(new BigDecimal(0));
			procedure.setArriveOfQty(new BigDecimal(0));
		}
		daoFactory.getCommonDao().saveAllEntity(order.getPack().getProcedureList());
		// 保存并审核
		if (flag == BoolValue.YES)
		{
			serviceFactory.getCommonService().audit(BillType.PRODUCE_MO, order.getId(), BoolValue.YES);
			this.savaReportTask(order.getId(), BoolValue.YES);
		}
		else
		{
			this.savaReportTask(order.getId(), BoolValue.NO);
		}

		return order;
	}

	/**
	 * 创建生产任务
	 * @param id
	 */
	@Transactional
	public void savaReportTask(Long id, BoolValue boolValue)
	{
		Work work = this.get(id);
		if (work.getIsForceComplete() == BoolValue.NO)
		{
			String sourceBillNo = "";
			String customerName = "";
			String customerMaterialCode = "";
			String productName = "";
			String specifications = "";
			String customerBillNo = "";
			String customerIds = "";
			String productIds = "";
			Integer produceQty = 0;
			for (WorkProduct product : work.getProductList())
			{
				if (StringUtils.isBlank(customerBillNo))
				{
					customerBillNo = product.getCustomerBillNo();
				}
				else
				{
					customerBillNo += "," + product.getCustomerBillNo();
				}
				if (StringUtils.isBlank(sourceBillNo))
				{
					sourceBillNo = product.getSourceBillNo();
				}
				else
				{
					sourceBillNo += "," + product.getSourceBillNo();
				}

				if (StringUtils.isBlank(customerName))
				{
					customerName = product.getCustomerName();
				}
				else
				{
					customerName += "," + product.getCustomerName();
				}

				if (StringUtils.isBlank(customerMaterialCode))
				{
					customerMaterialCode = product.getCustomerMaterialCode();
				}
				else
				{
					customerMaterialCode += "," + product.getCustomerMaterialCode();
				}

				if (StringUtils.isBlank(productName))
				{
					productName = product.getProductName();
				}
				else
				{
					productName += "," + product.getProductName();
				}

				if (StringUtils.isBlank(specifications))
				{
					specifications = product.getStyle();
				}
				else
				{
					specifications += "," + product.getStyle();
				}
				produceQty += product.getProduceQty();
				customerIds += "B" + product.getCustomerId().toString() + "E,";
				productIds += "B" + product.getProductId().toString() + "E,";
			}

			boolean flag = false;
			List<WorkPart> partList = this.getPartListByWorkId(id);
			int sort = 0;
			int partSort = 0;

			// 部件工序
			for (WorkPart part : partList)
			{
				for (WorkProcedure workProcedure : part.getProcedureList())
				{
					DynamicQuery proQuery = new CompanyDynamicQuery(Procedure.class);
					proQuery.eq("id", workProcedure.getProcedureId());
					Procedure procedure = daoFactory.getCommonDao().getByDynamicQuery(proQuery, Procedure.class);

					WorkReportTask task = new WorkReportTask();
					task.setIsPart(BoolValue.YES);// 是否部件
					task.setIsOutSource(workProcedure.getIsOutSource());// 是否发外
					task.setIsSchedule(procedure.getIsSchedule()); // 是否排程
					task.setSort(sort++); // 工序排序
					task.setProcedureRefId(workProcedure.getId());
					task.setProduceQty(part.getQty());
					if (procedure.getScheduleDataSource() == ScheduleDataSource.OUTPUT)
					{
						task.setYieldQty(workProcedure.getOutputQty());
					}
					if (procedure.getScheduleDataSource() == ScheduleDataSource.INPUT)
					{
						task.setYieldQty(workProcedure.getInputQty());
					}
					task.setUnreportQty(task.getYieldQty());
					task.setDeliveryTime(work.getProductList().get(0).getDeliveryTime());
					task.setCreateTime(new Date());
					task.setSourceBillNo(sourceBillNo);
					task.setBillNo(work.getBillNo());
					task.setCustomerName(customerName);
					task.setCustomerMaterialCode(customerMaterialCode);
					task.setProductName(productName);
					task.setSpecifications(specifications);
					task.setProcedureClassId(workProcedure.getProcedureClassId()); // 工序分类 ID
					task.setProcedureId(workProcedure.getProcedureId()); // 工序ID
					task.setProcedureCode(workProcedure.getProcedureCode());// 工序Code
					task.setProcedureName(workProcedure.getProcedureName());// 工序名称
					task.setProcedureType(workProcedure.getProcedureType()); // 工序类型
					task.setPartId(part.getId());
					task.setPartName(part.getPartName());
					task.setStyle(part.getStyle());
					task.setCompanyId(UserUtils.getCompanyId());
					task.setUserNo(UserUtils.getUser().getUserNo());
					task.setProductType(work.getProductType()); // 工单类型
					task.setCustomerId(customerIds); // 客户id集合
					task.setProductId(productIds); // 产品id集合
					if (procedure.getProcedureType() == ProcedureType.PRINT)
					{
						task.setMachineName(part.getMachineName());
					}
					task.setCustomerBillNo(customerBillNo);
					task.setPartSort(partSort++);
					if (boolValue == BoolValue.NO)
					{
						task.setIsShow(BoolValue.NO);
					}

					daoFactory.getCommonDao().saveEntity(task);
					QRCodeUtils.getQRCodeImgFile(task.getId().toString());
					flag = true;
				}
			}

			// 成品工序
			if (work.getPack() != null)
			{
				for (WorkProcedure workProcedure : work.getPack().getProcedureList())
				{
					DynamicQuery proQuery = new CompanyDynamicQuery(Procedure.class);
					proQuery.eq("id", workProcedure.getProcedureId());
					Procedure procedure = daoFactory.getCommonDao().getByDynamicQuery(proQuery, Procedure.class);

					WorkReportTask task = new WorkReportTask();
					task.setIsPart(BoolValue.NO);// 是否部件
					task.setIsOutSource(workProcedure.getIsOutSource());// 是否发外
					task.setIsSchedule(procedure.getIsSchedule()); // 是否排程
					task.setSort(sort++); // 工序排序
					task.setProcedureRefId(workProcedure.getId());
					task.setProduceQty(produceQty.intValue());
					if (procedure.getScheduleDataSource() == ScheduleDataSource.OUTPUT)
					{
						task.setYieldQty(workProcedure.getOutputQty());
					}
					if (procedure.getScheduleDataSource() == ScheduleDataSource.INPUT)
					{
						task.setYieldQty(workProcedure.getInputQty());
					}
					task.setUnreportQty(task.getYieldQty());
					task.setDeliveryTime(work.getProductList().get(0).getDeliveryTime());
					task.setCreateTime(new Date());
					task.setSourceBillNo(sourceBillNo);
					task.setBillNo(work.getBillNo());
					task.setCustomerName(customerName);
					task.setCustomerMaterialCode(customerMaterialCode);
					task.setProductName(productName);
					task.setSpecifications(specifications);
					task.setProcedureClassId(workProcedure.getProcedureClassId()); // 工序分类 ID
					task.setProcedureId(workProcedure.getProcedureId()); // 工序ID
					task.setProcedureCode(workProcedure.getProcedureCode());// 工序Code
					task.setProcedureName(workProcedure.getProcedureName());// 工序名称
					task.setProcedureType(workProcedure.getProcedureType()); // 工序类型
					task.setCompanyId(UserUtils.getCompanyId());
					task.setUserNo(UserUtils.getUser().getUserNo());
					task.setCustomerBillNo(customerBillNo);
					task.setCustomerId(customerIds); // 客户id集合
					task.setProductId(productIds); // 产品id集合
					if (boolValue == BoolValue.NO)
					{
						task.setIsShow(BoolValue.NO);
					}

					daoFactory.getCommonDao().saveEntity(task);
					QRCodeUtils.getQRCodeImgFile(task.getId().toString());
					flag = true;
				}
			}

			// 生成工单条码
			if (flag)
			{
				QRCodeUtils.getQRCodeImgFile(work.getBillNo());
			}
		}
	}

	@Override
	@Transactional
	public Work update(Work order)
	{
		// 需要删除的集合
		List<WorkProduct> del_product_list = new ArrayList<WorkProduct>();
		List<WorkPart> del_part_list = new ArrayList<WorkPart>();
		List<WorkPart2Product> del_part2product_list = new ArrayList<WorkPart2Product>();
		List<WorkMaterial> del_material_list = new ArrayList<WorkMaterial>();

		// 需要新增的集合
		List<WorkProduct> add_product_list = new ArrayList<WorkProduct>();
		List<WorkPart2Product> add_part2product_list = new ArrayList<WorkPart2Product>();
		List<WorkMaterial> add_material_list = new ArrayList<WorkMaterial>();
		List<WorkProcedure> add_procedure_list = new ArrayList<WorkProcedure>();

		// 需要更新的集合
		List<WorkProduct> update_product_list = new ArrayList<WorkProduct>();
		List<WorkPart> update_part_list = new ArrayList<WorkPart>();
		List<WorkPart2Product> update_part2product_list = new ArrayList<WorkPart2Product>();
		List<WorkMaterial> update_material_list = new ArrayList<WorkMaterial>();

		Work old_order = serviceFactory.getWorkService().get(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		// 删除所有工序信息
		daoFactory.getCommonDao().deleteAllEntity(this.getProcedureListByWorkId(order.getId()));
		// ----------------------------产品更新-----------------------------------------------------------
		// 更新删除掉的产品列表
		Map<Long, WorkProduct> old_product_map = ConverterUtils.list2Map(old_order.getProductList(), "id");
		List<Long> old_product_Ids = new ArrayList<Long>();
		List<Long> new_product_Ids = new ArrayList<Long>();
		for (WorkProduct product : old_order.getProductList())
		{
			old_product_Ids.add(product.getId());
		}
		for (WorkProduct new_product : order.getProductList())
		{
			if (new_product.getId() != null)
			{
				new_product_Ids.add(new_product.getId());
			}
		}
		for (Long id : old_product_Ids)
		{
			if (!new_product_Ids.contains(id))
			{
				WorkProduct old_product = old_product_map.get(id);
				if (old_product.getSourceDetailId() != null && old_product.getSourceDetailId() != 0l)
				{// 反写销售数量、备品数量
					SaleOrderDetail source = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, old_product.getSourceDetailId());
					source.setProduceedQty(source.getProduceedQty() - old_product.getSaleProduceQty());
					source.setProduceSpareedQty(source.getProduceSpareedQty() - old_product.getSpareProduceQty());
					daoFactory.getCommonDao().updateEntity(source);
				}

				del_product_list.add(old_product);
			}
		}
		// 更新产品列表信息
		for (WorkProduct product : order.getProductList())
		{
			if (product.getId() == null)
			{// 新增
				product.setCompanyId(old_order.getCompanyId());
				product.setUserNo(old_order.getUserNo());
				product.setMasterId(old_order.getId());
				product.setIsForceComplete(BoolValue.NO);
				product.setInStockQty(0);
				product.setOutOfQty(0);

				DynamicQuery query = new CompanyDynamicQuery(Customer.class);
				query.eq("id", product.getCustomerId());
				Customer customer = daoFactory.getCommonDao().getByDynamicQuery(query, Customer.class);
				if (customer != null)
				{
					TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), customer.getTaxRateId());

					// 不含税金额计算 不含税金额=（金额/(1+税率值/100)）
					BigDecimal noTaxMoney = product.getMoney().divide(new BigDecimal(1d + taxRate.getPercent() / 100d), 2);

					// 税额=(金额-不含税金额)
					BigDecimal tax = product.getMoney().subtract(noTaxMoney);

					// 不含税单价=(单价/(1+税率值/100))
					BigDecimal noTaxPrice = product.getPrice().divide(new BigDecimal(1d + taxRate.getPercent() / 100d), 4);
					product.setNoTaxMoney(noTaxMoney);
					product.setNoTaxPrice(noTaxPrice);
					product.setPercent(taxRate.getPercent());
					product.setTax(tax);
					product.setTaxRateId(customer.getTaxRateId());
				}

				if (product.getSourceDetailId() != null && product.getSourceDetailId() != 0l)
				{// 反写销售数量、备品数量
					SaleOrderDetail source = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, product.getSourceDetailId());
					source.setProduceedQty(source.getProduceedQty() + product.getSaleProduceQty());
					source.setProduceSpareedQty(source.getProduceSpareedQty() + product.getSpareProduceQty());
					daoFactory.getCommonDao().updateEntity(source);
				}
				add_product_list.add(product);
			}
			else
			{
				if (old_product_Ids.contains(product.getId()))
				{// 更新
					WorkProduct old_product = old_product_map.get(product.getId());
					if (product.getSourceDetailId() != null && StringUtils.isNotBlank(String.valueOf(product.getSourceDetailId())))
					{
						SaleOrderDetail source = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, product.getSourceDetailId());
						source.setProduceedQty(source.getProduceedQty() - (old_product.getSaleProduceQty() - product.getSaleProduceQty()));
						source.setProduceSpareedQty(source.getProduceSpareedQty() - (old_product.getSpareProduceQty() - product.getSpareProduceQty()));
						daoFactory.getCommonDao().updateEntity(source);
					}

					DynamicQuery query = new CompanyDynamicQuery(Customer.class);
					query.eq("id", product.getCustomerId());
					Customer customer = daoFactory.getCommonDao().getByDynamicQuery(query, Customer.class);
					if (customer != null)
					{
						TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), customer.getTaxRateId());

						// 不含税金额计算 不含税金额=（金额/(1+税率值/100)）
						BigDecimal noTaxMoney = product.getMoney().divide(new BigDecimal(1d + taxRate.getPercent() / 100d), 2);

						// 税额=(金额-不含税金额)
						BigDecimal tax = product.getMoney().subtract(noTaxMoney);

						// 不含税单价=(单价/(1+税率值/100))
						BigDecimal noTaxPrice = product.getPrice().divide(new BigDecimal(1d + taxRate.getPercent() / 100d), 4);
						product.setNoTaxMoney(noTaxMoney);
						product.setNoTaxPrice(noTaxPrice);
						product.setPercent(taxRate.getPercent());
						product.setTax(tax);
						product.setTaxRateId(customer.getTaxRateId());
					}

					PropertyClone.copyProperties(old_product, product, false);
					update_product_list.add(old_product);
				}
			}
		}
		// ----------------------------部件更新-----------------------------------------------------------
		// 更新删除掉的部件信息
		Map<Long, WorkPart> old_part_map = new HashMap<Long, WorkPart>();
		if (old_order.getPartList() != null)
		{
			for (WorkPart object : old_order.getPartList())
			{
				old_part_map.put(object.getId(), object);
			}
		}

		List<Long> old_part_Ids = new ArrayList<Long>();
		List<Long> new_part_Ids = new ArrayList<Long>();

		for (WorkPart part : old_order.getPartList())
		{
			old_part_Ids.add(part.getId());
		}
		for (WorkPart new_part : order.getPartList())
		{
			if (new_part.getId() != null)
			{
				new_part_Ids.add(new_part.getId());
			}
		}
		for (Long id : old_part_Ids)
		{
			if (!new_part_Ids.contains(id))
			{
				WorkPart old_part = old_part_map.get(id);
				del_part_list.add(old_part);
				del_part2product_list.addAll(old_part.getProductList());
				del_material_list.addAll(old_part.getMaterialList());
			}
		}

		// 保存部件列表信息
		for (WorkPart part : order.getPartList())
		{
			if (part.getId() == null)
			{// 新增
				part.setCompanyId(old_order.getCompanyId());
				part.setUserNo(old_order.getUserNo());
				part.setMasterId(old_order.getId());
				part.setIsForceComplete(BoolValue.NO);
				if (StringUtils.isBlank(part.getMachineName()))
				{
					part.setMachineId(null);
				}
				WorkPart new_part = daoFactory.getCommonDao().saveEntity(part);
				for (WorkPart2Product p2p : part.getProductList())
				{
					p2p.setCompanyId(old_order.getCompanyId());
					p2p.setWorkId(old_order.getId());
					p2p.setWorkPartId(new_part.getId());
					add_part2product_list.add(p2p);
				}

				for (WorkMaterial material : part.getMaterialList())
				{
					material.setCompanyId(old_order.getCompanyId());
					material.setWorkId(old_order.getId());
					material.setWorkBillNo(old_order.getBillNo());
					material.setWorkMaterialType(WorkMaterialType.PART);
					material.setParentId(new_part.getId());
					material.setIsForceComplete(BoolValue.NO);
					material.setPurchQty(new BigDecimal(0));
					material.setTakeQty(new BigDecimal(0));
					material.setIsNotTake(BoolValue.NO);
					material.setIsNotPurch(BoolValue.NO);
					add_material_list.add(material);
				}

				for (WorkProcedure procedure : part.getProcedureList())
				{
					procedure.setId(null);
					procedure.setCompanyId(old_order.getCompanyId());
					procedure.setWorkId(old_order.getId());
					procedure.setWorkBillNo(old_order.getBillNo());
					procedure.setWorkProcedureType(WorkProcedureType.PART);
					procedure.setParentId(new_part.getId());
					procedure.setIsForceComplete(BoolValue.NO);
					procedure.setOutOfQty(new BigDecimal(0));
					procedure.setArriveOfQty(new BigDecimal(0));
					add_procedure_list.add(procedure);
				}

			}
			else
			{
				if (old_part_Ids.contains(part.getId()))
				{// 更新
					WorkPart old_part = old_part_map.get(part.getId());
					PropertyClone.copyProperties(old_part, part, false, new String[] { "materialList", "procedureList", "productList" });// 替换成新内容
					if (StringUtils.isBlank(old_part.getMachineName()))
					{
						old_part.setMachineId(null);
					}
					update_part_list.add(old_part);

					// -----------------------部件产品更新----------------------------------------------
					List<WorkPart2Product> old_p2p_list = old_part_map.get(part.getId()).getProductList();
					Map<Long, WorkPart2Product> old_p2p_map = ConverterUtils.list2Map(old_p2p_list, "id");
					List<Long> old_p2p_Ids = new ArrayList<Long>();
					List<Long> new_p2p_Ids = new ArrayList<Long>();
					for (WorkPart2Product p2p : old_p2p_list)
					{
						old_p2p_Ids.add(p2p.getId());
					}
					for (WorkPart2Product new_p2p : part.getProductList())
					{
						if (new_p2p.getId() != null)
						{
							new_p2p_Ids.add(new_p2p.getId());
						}
					}
					for (Long id : old_p2p_Ids)
					{
						if (!new_p2p_Ids.contains(id))
						{
							WorkPart2Product old_p2p = old_p2p_map.get(id);
							del_part2product_list.add(old_p2p);
						}
					}

					for (WorkPart2Product new_p2p : part.getProductList())
					{
						if (new_p2p.getId() == null)
						{// 新增
							new_p2p.setCompanyId(old_order.getCompanyId());
							new_p2p.setWorkId(old_order.getId());
							new_p2p.setWorkPartId(part.getId());
							add_part2product_list.add(new_p2p);
						}
						else
						{
							if (old_p2p_Ids.contains(new_p2p.getId()))
							{// 更新
								WorkPart2Product old_p2p = old_p2p_map.get(new_p2p.getId());
								PropertyClone.copyProperties(old_p2p, new_p2p, false);
								update_part2product_list.add(old_p2p);
							}
						}
					}
					// -----------------------部件材料----------------------------------------------
					List<WorkMaterial> old_material_list = old_part_map.get(part.getId()).getMaterialList();
					Map<Long, WorkMaterial> old_material_map = ConverterUtils.list2Map(old_material_list, "id");
					List<Long> old_material_Ids = new ArrayList<Long>();
					List<Long> new_material_Ids = new ArrayList<Long>();
					for (WorkMaterial material : old_material_list)
					{
						old_material_Ids.add(material.getId());
					}
					for (WorkMaterial new_material : part.getMaterialList())
					{
						if (new_material.getId() != null)
						{
							new_material_Ids.add(new_material.getId());
						}
					}
					for (Long id : old_material_Ids)
					{
						if (!new_material_Ids.contains(id))
						{
							WorkMaterial old_material = old_material_map.get(id);
							del_material_list.add(old_material);
						}
					}

					for (WorkMaterial new_material : part.getMaterialList())
					{
						if (new_material.getId() == null)
						{// 新增

							new_material.setCompanyId(old_order.getCompanyId());
							new_material.setWorkId(old_order.getId());
							new_material.setWorkBillNo(old_order.getBillNo());
							new_material.setWorkMaterialType(WorkMaterialType.PART);
							new_material.setParentId(part.getId());
							new_material.setIsForceComplete(BoolValue.NO);
							new_material.setPurchQty(new BigDecimal(0));
							new_material.setTakeQty(new BigDecimal(0));
							new_material.setIsNotTake(BoolValue.NO);
							new_material.setIsNotPurch(BoolValue.NO);

							add_material_list.add(new_material);
						}
						else
						{
							if (old_material_Ids.contains(new_material.getId()))
							{// 更新
								WorkMaterial old_material = old_material_map.get(new_material.getId());
								PropertyClone.copyProperties(old_material, new_material, false);
								update_material_list.add(old_material);
							}

						}
					}
					// -----------------------部件工序----------------------------------------------
					for (WorkProcedure procedure : part.getProcedureList())
					{
						procedure.setId(null);
						procedure.setCompanyId(old_order.getCompanyId());
						procedure.setWorkId(old_order.getId());
						procedure.setWorkBillNo(old_order.getBillNo());
						procedure.setWorkProcedureType(WorkProcedureType.PART);
						procedure.setParentId(part.getId());
						procedure.setIsForceComplete(BoolValue.NO);
						procedure.setOutOfQty(new BigDecimal(0));
						procedure.setArriveOfQty(new BigDecimal(0));
						add_procedure_list.add(procedure);
					}
				}
			}
		}

		// ----------------------------成品更新-----------------------------------------------------------
		// 更新成品信息
		{
			List<WorkMaterial> old_material_list = old_order.getPack().getMaterialList();
			Map<Long, WorkMaterial> old_material_map = ConverterUtils.list2Map(old_material_list, "id");
			List<Long> old_material_Ids = new ArrayList<Long>();
			List<Long> new_material_Ids = new ArrayList<Long>();
			for (WorkMaterial material : old_material_list)
			{
				old_material_Ids.add(material.getId());
			}
			for (WorkMaterial new_material : order.getPack().getMaterialList())
			{
				if (new_material.getId() != null)
				{
					new_material_Ids.add(new_material.getId());
				}
			}
			for (Long id : old_material_Ids)
			{
				if (!new_material_Ids.contains(id))
				{
					WorkMaterial old_material = old_material_map.get(id);
					del_material_list.add(old_material);
				}
			}
			for (WorkMaterial new_material : order.getPack().getMaterialList())
			{
				if (new_material.getId() == null)
				{// 新增
					new_material.setCompanyId(old_order.getCompanyId());
					new_material.setWorkId(old_order.getId());
					new_material.setWorkBillNo(old_order.getBillNo());
					new_material.setWorkMaterialType(WorkMaterialType.PRODUCT);
					new_material.setParentId(old_order.getPack().getId());
					new_material.setIsForceComplete(BoolValue.NO);
					new_material.setPurchQty(new BigDecimal(0));
					new_material.setTakeQty(new BigDecimal(0));
					new_material.setIsNotTake(BoolValue.NO);
					new_material.setIsNotPurch(BoolValue.NO);
					add_material_list.add(new_material);
				}
				else
				{
					if (old_material_Ids.contains(new_material.getId()))
					{// 更新
						WorkMaterial old_material = old_material_map.get(new_material.getId());
						PropertyClone.copyProperties(old_material, new_material, false);
						update_material_list.add(old_material);
					}
				}
			}
			for (WorkProcedure procedure : order.getPack().getProcedureList())
			{
				procedure.setId(null);
				procedure.setCompanyId(old_order.getCompanyId());
				procedure.setWorkId(old_order.getId());
				procedure.setWorkBillNo(old_order.getBillNo());
				procedure.setWorkProcedureType(WorkProcedureType.PRODUCT);
				procedure.setParentId(old_order.getPack().getId());
				procedure.setIsForceComplete(BoolValue.NO);
				procedure.setOutOfQty(new BigDecimal(0));
				procedure.setArriveOfQty(new BigDecimal(0));
				add_procedure_list.add(procedure);
			}
		}

		daoFactory.getCommonDao().deleteAllEntity(del_product_list);
		daoFactory.getCommonDao().deleteAllEntity(del_part2product_list);
		daoFactory.getCommonDao().deleteAllEntity(del_material_list);
		daoFactory.getCommonDao().deleteAllEntity(del_part_list);

		daoFactory.getCommonDao().saveAllEntity(add_product_list);
		daoFactory.getCommonDao().saveAllEntity(add_part2product_list);
		daoFactory.getCommonDao().saveAllEntity(add_material_list);
		daoFactory.getCommonDao().saveAllEntity(add_procedure_list);

		daoFactory.getCommonDao().updateAllEntity(update_product_list);
		daoFactory.getCommonDao().updateAllEntity(update_part_list);
		daoFactory.getCommonDao().updateAllEntity(update_part2product_list);
		daoFactory.getCommonDao().updateAllEntity(update_material_list);

		WorkPack old_pack = old_order.getPack();
		PropertyClone.copyProperties(old_pack, order.getPack(), false);
		daoFactory.getCommonDao().updateEntity(old_pack);
		// ----------------------------主表更新-----------------------------------------------------------
		// 更新主表
		PropertyClone.copyProperties(old_order, order, false, new String[] { "productList", "partList", "pack" });// 替换新内容
		old_order.setUpdateName(UserUtils.getUserName());
		old_order.setUpdateTime(new Date());
		old_order.setIsCheck(BoolValue.NO);						// 修改后保存或修改后保存并审核，都是未审核状态
		Work new_order = daoFactory.getCommonDao().updateEntity(old_order);
		// 保存并审核
		if (order.getIsCheck() == BoolValue.YES)
		{
			serviceFactory.getCommonService().audit(BillType.PRODUCE_MO, new_order.getId(), BoolValue.YES);
			// 更新生产任务
			this.updateReportTask(new_order.getId(), BoolValue.YES, update_part_list, del_part_list);
		}
		else
		{
			// 更新生产任务
			this.updateReportTask(new_order.getId(), BoolValue.NO, update_part_list, del_part_list);
		}

		return new_order;
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		Work work = this.get(id);
		serviceFactory.getDaoFactory().getCommonDao().lockObject(Work.class, id);
		if (work.getBillType() == BillType.PRODUCE_SUPPLEMENT)
		{// 补单：减少源工单补单次数
			Work sourceWork = serviceFactory.getDaoFactory().getCommonDao().lockObject(Work.class, work.getSourceWorkId());
			if (sourceWork != null)
			{// 源始单据有可能已被删除
				sourceWork.setSupplementCount(sourceWork.getSupplementCount() - 1);
				serviceFactory.getDaoFactory().getCommonDao().updateEntity(sourceWork);
			}
		}
		else if (work.getBillType() == BillType.PRODUCE_TURNING)
		{// 翻单：减少源工单翻单次数
			Work sourceWork = serviceFactory.getDaoFactory().getCommonDao().lockObject(Work.class, work.getSourceWorkId());
			if (sourceWork != null)
			{// 源始单据有可能已被删除
				sourceWork.setTurningCount(sourceWork.getTurningCount() - 1);
				serviceFactory.getDaoFactory().getCommonDao().updateEntity(sourceWork);
			}
		}

		List<WorkPart> list = this.getPartListByWorkId(id);
		for (WorkPart part : list)
		{
			daoFactory.getCommonDao().deleteAllEntity(this.getPart2ProductByPartId(part.getId()));
		}

		List<WorkProduct> productList = work.getProductList();
		for (WorkProduct product : productList)
		{
			if (product.getSourceDetailId() != null && product.getSourceDetailId() != 0l)
			{// 反写销售数量、备品数量
				SaleOrderDetail source = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, product.getSourceDetailId());
				source.setProduceedQty(source.getProduceedQty() - product.getSaleProduceQty());
				source.setProduceSpareedQty(source.getProduceSpareedQty() - product.getSpareProduceQty());
				daoFactory.getCommonDao().updateEntity(source);
			}
		}
		// 删除生产任务
		deleteReportTask(id);

		daoFactory.getCommonDao().deleteAllEntity(work.getProductList());
		daoFactory.getCommonDao().deleteAllEntity(work.getPartList());
		daoFactory.getCommonDao().deleteAllEntity(this.getProcedureListByWorkId(id));
		daoFactory.getCommonDao().deleteAllEntity(this.getWorkMaterialListByWorkId(id));
		daoFactory.getCommonDao().deleteEntity(work.getPack());
		daoFactory.getCommonDao().deleteEntity(work);

	}

	@Override
	@Transactional
	public boolean checkAll()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(Work.class);
			query.eq("isCheck", BoolValue.NO);
			query.eq("isForceComplete", BoolValue.NO);
			List<Work> workList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Work.class);
			for (Work work : workList)
			{
				work.setIsCheck(BoolValue.YES);
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
	public SearchResult<Work> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Work.class, "w");
		query.setIsSearchTotalCount(true);
		// query.addProjection(Projections.property(" EXISTS (select 1 from produce_work_product p where p.masterId=w.id and
		// p.customerId in(1,2,3) )"));
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();

		if (employes.length > 0)
		{
			DynamicQuery queryPro = new CompanyDynamicQuery(WorkProduct.class, "wp");
			queryPro.createAlias(Customer.class, "c");
			queryPro.addProjection(Projections.property("1"));
			queryPro.eqProperty("wp.masterId", "w.id");
			queryPro.eqProperty("wp.customerId", "c.id");
			queryPro.inArray("c.employeeId", employes);
			query.exists(queryPro);
		}

		if (queryParam.getDateMin() != null)
		{
			query.ge("w.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("w.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("w.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("w.isCheck", queryParam.getAuditFlag());
		}
		// query.eq("w.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("w.createTime");
		query.setQueryType(QueryType.JDBC);
		SearchResult<Work> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Work.class);
		return result;
	}

	@Override
	public SearchResult<WorkProduct> findProductByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class, "a");
		query.createAlias(Work.class, "b");
		query.createAlias(Product.class, "p");
		query.addProjection(Projections.property("a,b,p"));
		query.eqProperty("a.masterId", "b.id");
		query.eqProperty("a.productId", "p.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, "c");
			query.eqProperty("a.customerId", "c.id");
			query.inArray("c.employeeId", employes);
		}

		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("a.style", "%" + queryParam.getProductStyle() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("a.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}

		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getCreateName()))
		{
			query.like("b.createName", "%" + queryParam.getCreateName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("a.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getSaleBillNo()))
		{
			query.like("a.sourceBillNo", "%" + queryParam.getSaleBillNo() + "%");
		}

		if (queryParam.getSourceDetailId() != null)
		{
			query.eq("a.sourceDetailId", queryParam.getSourceDetailId());
		}

		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("a.customerName", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}

		if (queryParam.getDeliverDateMin() != null)
		{
			query.ge("a.deliveryTime", queryParam.getDeliverDateMin());
		}
		if (queryParam.getDeliverDateMax() != null)
		{
			query.le("a.deliveryTime", queryParam.getDeliverDateMax());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("b.isCheck", queryParam.getAuditFlag());
		}
		if (queryParam.getIsOutSource() != null)
		{
			query.eq("b.isOutSource", queryParam.getIsOutSource());
		}
		if (queryParam.getProductType() != null)
		{
			query.eq("b.productType", queryParam.getProductType());
		}
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<WorkProduct> result = new SearchResult<WorkProduct>();
		result.setResult(new ArrayList<WorkProduct>());

		for (Object[] c : temp_result.getResult())
		{
			WorkProduct detail = (WorkProduct) c[0];
			Product product = (Product) c[2];
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((Work) c[1]);
			result.getResult().add(detail);
		}
		for (WorkProduct workProduct : result.getResult())
		{
			workProduct.setCompleteQty(serviceFactory.getOutSourceProcessService().getCompleteQty(workProduct.getMaster().getBillNo(), workProduct.getProductId()));
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<WorkProduct> findSaleProductByCondition(Long saleBillId, String productCode)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class, "a");
		query.createAlias(Work.class, "b");
		query.addProjection(Projections.property("a,b"));
		query.eqProperty("a.masterId", "b.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, "c");
			query.eqProperty("a.customerId", "c.id");
			query.inArray("c.employeeId", employes);
		}
		query.eq("a.sourceId", saleBillId);// 销售id
		query.eq("a.productCode", productCode);

		query.eq("a.companyId", UserUtils.getCompanyId());
		// query.setPageIndex(queryParam.getPageNumber());
		// query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<WorkProduct> result = new SearchResult<WorkProduct>();
		result.setResult(new ArrayList<WorkProduct>());

		for (Object[] c : temp_result.getResult())
		{
			WorkProduct detail = (WorkProduct) c[0];
			detail.setMaster((Work) c[1]);
			result.getResult().add(detail);
		}
		for (WorkProduct workProduct : result.getResult())
		{
			workProduct.setCompleteQty(serviceFactory.getOutSourceProcessService().getCompleteQty(workProduct.getMaster().getBillNo(), workProduct.getProductId()));
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<WorkProcedure> findProcedureByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProcedure.class, "wp");
		query.addProjection(Projections.property("wp,wpt,wpk,w"));
		query.createAlias(WorkPart.class, JoinType.LEFTJOIN, "wpt", "wp.parentId=wpt.id and wp.workProcedureType='PART'");
		query.createAlias(WorkPack.class, JoinType.LEFTJOIN, "wpk", "wp.parentId=wpk.id and wp.workProcedureType='PRODUCT'");
		query.createAlias(Work.class, JoinType.LEFTJOIN, "w", "wp.workId=w.id");

		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("wp.procedureName", "%" + queryParam.getProcedureName() + "%");

		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("w.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("w.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getPartName()))
		{
			query.like("wpt.partName", "%" + queryParam.getPartName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("w.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("w.isCheck", queryParam.getAuditFlag());
		}
		if (queryParam.getBillType() != null)
		{
			query.eq("w.billType", queryParam.getBillType());
		}
		if (queryParam.getProcedureType() != null)
		{
			query.eq("wp.procedureType", queryParam.getProcedureType());
		}
		query.eq("wp.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("w.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		;
		SearchResult<WorkProcedure> result = new SearchResult<WorkProcedure>();
		result.setResult(new ArrayList<WorkProcedure>());
		for (Object[] c : temp_result.getResult())
		{
			WorkProcedure detail = (WorkProcedure) c[0];
			detail.setWorkPart((WorkPart) c[1]);
			detail.setWorkPack((WorkPack) c[2]);
			detail.setWork((Work) c[3]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SearchResult<WorkMaterialVo> findMaterialByCondition(QueryParam queryParam)
	{

		SearchResult<WorkMaterialVo> result = new SearchResult<WorkMaterialVo>();

		// 补料明细左联接主表
		DynamicQuery querySupplement = new CompanyDynamicQuery(StockMaterialSupplement.class, "ss");
		DynamicQuery querySupplementDetail = new CompanyDynamicQuery(StockMaterialSupplementDetail.class, "ssd");
		querySupplementDetail.addProjection(Projections.property("qs.id,qs.workId,ssd.specifications,ssd.materialId,sum(ssd.qty) as sdQty"));
		querySupplementDetail.createAlias(querySupplement, JoinType.LEFTJOIN, "qs", "qs.id=ssd.masterId");
		querySupplementDetail.addGourp("qs.workId");
		// 退料明细左联接主表
		DynamicQuery queryReturn = new CompanyDynamicQuery(StockMaterialReturn.class, "sr");
		DynamicQuery queryReturnDetail = new CompanyDynamicQuery(StockMaterialReturnDetail.class, "srd");
		queryReturnDetail.addProjection(Projections.property("qr.id,qr.workId,srd.specifications,srd.materialId,sum(srd.qty) as rdQty"));
		queryReturnDetail.createAlias(queryReturn, JoinType.LEFTJOIN, "qr", "qr.id=srd.masterId");
		queryReturnDetail.addGourp("qr.workId");

		DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class, "wm");
		query.addProjection(Projections.property("w.id as workId,w.isCheck as isCheck,w.billType as billType,w.createTime as createTime," + "	w.billNo as billNo,wpt.partName as partName,ifnull(wpt.qty,0) as workPartQty,wpt.style as workPartStyle," + "	wm.materialName as materialName,ifnull(wm.splitQty,0) as splitQty,wm.style as materialStyle,ifnull(wm.qty,0) as materialQty," + "	ifnull(wm.takeQty,0) as takeQty,ifnull(qsd.sdQty,0) as supplementQty,ifnull(qrd.rdQty,0) as returnQty"));
		query.createAlias(WorkPart.class, JoinType.LEFTJOIN, "wpt", "wm.parentId=wpt.id and wm.workMaterialType='PART'");
		query.createAlias(WorkPack.class, JoinType.LEFTJOIN, "wpk", "wm.parentId=wpk.id and wm.workMaterialType='PRODUCT'");
		query.createAlias(Work.class, JoinType.LEFTJOIN, "w", "wm.workId=w.id");
		query.createAlias(querySupplementDetail, JoinType.LEFTJOIN, "qsd", "qsd.workId = wm.workId and qsd.specifications = wm.style and qsd.materialId = wm.materialId");
		query.createAlias(queryReturnDetail, JoinType.LEFTJOIN, "qrd", "qrd.workId = wm.workId and qrd.specifications = wm.style and qrd.materialId = wm.materialId");

		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("wm.style", "%" + queryParam.getProductStyle() + "%");
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("w.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("w.createTime", queryParam.getDateMax());
		}

		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("w.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("w.isCheck", queryParam.getAuditFlag());
		}
		if (queryParam.getBillType() != null)
		{
			query.eq("w.billType", queryParam.getBillType());
		}
		if (queryParam.getMaterialName() != null)
		{
			query.like("wm.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		query.eq("wm.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("w.createTime");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);

		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		// SearchResult<Object[]> temp_result=
		// daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query,Object[].class);;
		List<WorkMaterialVo> list = new ArrayList<WorkMaterialVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			WorkMaterialVo vo = new WorkMaterialVo();
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
				}, BoolValue.class);
				vo = ObjectHelper.mapToObject(map, WorkMaterialVo.class);

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

	@Override
	public SearchResult<WorkProcedure> findForTransmitProcedureOut(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProcedure.class, "a");
		query.createAlias(Work.class, "b");
		query.addProjection(Projections.property("a,b"));
		query.eqProperty("a.workId", "b.id");
		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("a.procedureName", "%" + queryParam.getProcedureName() + "%");
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}

		query.eq("a.companyId", UserUtils.getCompanyId());
		query.add(Restrictions.gtProperty("a.inputQty", "a.outOfQty"));// 投入数>发外数
		query.eq("a.isOutSource", BoolValue.YES);// 是否发外
		query.eq("b.isOutSource", BoolValue.NO);// 是否整单发外

		query.eq("b.isCheck", BoolValue.YES);// 工单已审核
		query.eq("a.isForceComplete", queryParam.getCompleteFlag());
		query.eq("b.isForceComplete", BoolValue.NO);
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());

		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<WorkProcedure> result = new SearchResult<WorkProcedure>();
		result.setResult(new ArrayList<WorkProcedure>());

		// TODO 数据多，性能有影响（拆分成【工序未发外】和【整单未发外】得，使用LEFT JOIN WorkPart 和 WorkPack）
		for (Object[] c : temp_result.getResult())
		{
			WorkProcedure detail = (WorkProcedure) c[0];
			detail.setWork((Work) c[1]);
			// 工序发外
			if (detail.getWorkProcedureType() == WorkProcedureType.PART)
			{
				WorkPart part = daoFactory.getCommonDao().getEntity(WorkPart.class, detail.getParentId());
				detail.setWorkPart(part);
			}
			else
			{
				WorkPack pack = daoFactory.getCommonDao().getEntity(WorkPack.class, detail.getParentId());
				List<WorkProduct> productList = getWorkProduct(pack.getMasterId());
				if (productList != null && productList.size() > 0)
				{
					pack.setStyle(productList.get(0).getStyle());
				}
				detail.setWorkPack(pack);
			}
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<WorkProduct> findForTransmitProductOut(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class, "a");
		query.createAlias(Work.class, "b");
		query.addProjection(Projections.property("a,b"));
		query.eqProperty("a.masterId", "b.id");
		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}

		query.eq("a.companyId", UserUtils.getCompanyId());
		query.add(Restrictions.gtProperty("a.produceQty", "a.outOfQty"));// 生产数>已发外数
		query.eq("b.isOutSource", BoolValue.YES);// 是否整单发外
		query.eq("b.isCheck", BoolValue.YES);// 工单已审核
		query.eq("a.isForceComplete", queryParam.getCompleteFlag());
		query.eq("b.isForceComplete", BoolValue.NO);
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());

		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		;
		SearchResult<WorkProduct> result = new SearchResult<WorkProduct>();
		result.setResult(new ArrayList<WorkProduct>());

		for (Object[] c : temp_result.getResult())
		{
			WorkProduct detail = (WorkProduct) c[0];
			Work work = (Work) c[1];
			detail.setMaster(work);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/**查询生产日报表*/
	@Override
	public SearchResult<WorkReportDetail> findWorkReportDeailsByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkReportDetail.class, "a");
		query.createAlias(WorkReportTask.class, "b");
		query.createAlias(WorkReport.class, "c");
		query.addProjection(Projections.property("a, b,c"));
		query.eqProperty("a.taskId", "b.id");
		query.eqProperty("a.masterId", "c.id");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("c.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("c.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNoneBlank(queryParam.getEmployeeName()))
		{
			query.like("c.employeeName", "%" + queryParam.getEmployeeName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("b.procedureName", "%" + queryParam.getProcedureName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("b.productName", "%" + queryParam.getProductName() + "%");
		}
		if (StringUtils.isNoneBlank(queryParam.getCustomerName()))
		{
			query.like("b.customerName", "%" + queryParam.getCustomerName() + "%");
		}
		if (queryParam.getAuditFlag() == BoolValue.YES)
		{
			query.eq("c.isCheck", queryParam.getAuditFlag());
		}
		if (queryParam.getAuditFlag() == BoolValue.NO)
		{
			query.eq("c.isCheck", queryParam.getAuditFlag());
		}
		if (StringUtils.isNoneBlank(queryParam.getMasterBillNo()))
		{
			query.like("c.billNo", "%" + queryParam.getMasterBillNo() + "%");
		}
		if (StringUtils.isNoneBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNoneBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("b.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		query.desc("a.id");

		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<WorkReportDetail> result = new SearchResult<WorkReportDetail>();
		result.setResult(new ArrayList<WorkReportDetail>());
		for (Object[] c : temp_result.getResult())
		{
			WorkReportDetail detail = (WorkReportDetail) c[0];
			WorkReportTask task = (WorkReportTask) c[1];
			WorkReport report = (WorkReport) c[2];
			detail.setMasterBillNo(report.getBillNo());
			detail.setIsCancel(report.getIsCancel());
			detail.setIsCheck(report.getIsCheck());
			detail.setTask(task);
			detail.setReport(report);
			result.getResult().add(detail);
			detail.setEmployeeName(report.getEmployeeName());
		}
		result.setCount(temp_result.getCount());

		return result;

	}

	@Override
	@Transactional
	public int cancelOutSourceProcess(OutSourceType type, Long[] ids)
	{
		if (type == OutSourceType.PROCESS)
		{// 取消工序发外
			serviceFactory.getDaoFactory().getCommonDao().execNamedQuery("work.procedure.canceloutsource", BoolValue.NO, UserUtils.getCompanyId(), Arrays.asList(ids));
		}
		else if (type == OutSourceType.PRODUCT)
		{// 取消整单发外
			serviceFactory.getDaoFactory().getCommonDao().execNamedQuery("work.product.canceloutsource", BoolValue.NO, UserUtils.getCompanyId(), Arrays.asList(ids));
		}
		return 0;
	}

	@Override
	public Map<String, Object> printDataMap(Long id)
	{
		Map<String, Object> map = null;
		try
		{
			Work order = serviceFactory.getWorkService().get(id);

			DynamicQuery query = new CompanyDynamicQuery(WorkReportTask.class);
			query.eq("billNo", order.getBillNo());
			List<WorkReportTask> taskList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkReportTask.class);

			map = ObjectUtils.objectToMap(order);
			if ("YES".equals(order.getIsEmergency().toString()))
			{
				map.put("isEmergency", "<span style=\"color:red;\">(急)</span>");
			}
			else
			{
				map.put("isEmergency", "");
			}

			String partFlow = "";
			List<Map<String, Object>> materialList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> before_procedureList = new ArrayList<Map<String, Object>>();
			// 印刷工序书刊印刷
			List<Map<String, Object>> print_bookList = new ArrayList<Map<String, Object>>();
			// 印刷工序包装印刷
			List<Map<String, Object>> print_packeList = new ArrayList<Map<String, Object>>();
			// 印刷工序轮转印刷
			List<Map<String, Object>> print_rotaryList = new ArrayList<Map<String, Object>>();
			// 印后书刊印刷
			List<Map<String, Object>> after_bookList = new ArrayList<Map<String, Object>>();
			// 印后包装印刷
			List<Map<String, Object>> after_packeList = new ArrayList<Map<String, Object>>();
			for (WorkPart part : order.getPartList())
			{
				partFlow += "<div>";
				partFlow += "	<div class=\"l\" style=\"height:24px;line-height:24px;\">" + part.getPartName() + "&nbsp;：</div>	";
				partFlow += "		<div class=\"cl\" style=\"display:inline;\">";
				int index = 0;
				for (WorkProcedure procedure : part.getProcedureList())
				{
					partFlow += "	<div class='cl pri_item mar_r'>";
					if (index > 0)
					{
						partFlow += "	<i class='arrow_r fa fa-long-arrow-right'></i> ";
					}
					index++;
					partFlow += "	<span class='show_fw'>";
					partFlow += "	<span class='ct'>" + procedure.getProcedureName() + "</span>";
					if ("YES".equals(procedure.getIsOutSource().toString()))
					{
						partFlow += "	<span class='outsource'>(发外)</span>";
					}
					partFlow += "</span>";
					partFlow += "</div>";
					Map<String, Object> procedureMap = ObjectUtils.objectToMap(procedure);
					procedureMap.put("partName", part.getPartName());
					procedureMap.put("printTypeText", part.getPrintTypeText());
					procedureMap.put("lossQty", part.getLossQty());
					procedureMap.put("generalColor", part.getGeneralColor());
					procedureMap.put("spotColor", part.getSpotColor());
					procedureMap.put("pieceNum", part.getPieceNum());
					procedureMap.put("pageNum", part.getPageNum());
					procedureMap.put("stickersNum", part.getStickersNum());
					procedureMap.put("stickersPostedNum", part.getStickersPostedNum());
					procedureMap.put("machineName", part.getMachineName());
					if (order.getProductType() == ProductType.ROTARY)
					{
						procedureMap.put("impressionNum", part.getMaterialNum());// 轮转专用，标准用料（产出数）
					}
					else
					{
						procedureMap.put("impressionNum", part.getImpressionNum());
					}

					String proedureCode = "";
					for (WorkReportTask task : taskList)
					{
						if (task.getProcedureId().equals(procedure.getProcedureId()) && part.getPartName().equals(task.getPartName()))
						{
							proedureCode = "<img src='" + QRCodeUtils.createLocalUrl(task.getId().toString()) + "' width='200px' height='30px'/>";
							break;
						}
					}
					procedureMap.put("proedureCode", proedureCode);
					// 【BUG-2238】 Begin 生产管理－生产工单书刊类印刷打印模板单贴损耗字段打印预览是英文格式，数据未取出来
					procedureMap.put("stickerlossQty", part.getStickerlossQty());
					// 【BUG-2238】 End

					if ("YES".equals(procedure.getIsOutSource().toString()))
					{
						procedureMap.put("isOutSource", "是");
					}
					else
					{
						procedureMap.put("isOutSource", "");
					}

					switch (procedure.getProcedureType())
					{
						case BEFORE:
							before_procedureList.add(procedureMap);
							break;
						case PRINT:
							if ("PACKE".equals(order.getProductType().toString()))
							{
								print_packeList.add(procedureMap);
							}
							if ("BOOK".equals(order.getProductType().toString()))
							{
								print_bookList.add(procedureMap);
							}
							if ("ROTARY".equals(order.getProductType().toString()))
							{
								print_rotaryList.add(procedureMap);
							}
							break;
						case AFTER:
							if ("PACKE".equals(order.getProductType().toString()) || "ROTARY".equals(order.getProductType().toString()))
							{
								after_packeList.add(procedureMap);
							}
							if ("BOOK".equals(order.getProductType().toString()))
							{
								after_bookList.add(procedureMap);
							}
							break;
						default:
							break;
					}

				}
				partFlow += "	</div>";
				partFlow += "</div>";
				// 材料信息
				for (WorkMaterial material : part.getMaterialList())
				{
					if (material.getIsCustPaper() == BoolValue.YES)
					{
						material.setIsCustPaperText("是");
					}
					Map<String, Object> materialMap = ObjectUtils.objectToMap(material);
					materialMap.put("partName", part.getPartName());
					materialMap.put("partStlye", part.getStyle());
					materialMap.put("impressionNum", part.getImpressionNum());
					materialMap.put("lossQty", part.getLossQty());
					materialMap.put("materialNum", part.getMaterialNum());

					if (material.getStockUnitId() != null)
					{
						// 去除小数0
						String qty = StringUtils.removeLastZero(material.getQty());
						Unit unit = (Unit) UserUtils.getBasicInfo(BasicType.UNIT.name(), material.getStockUnitId());
						materialMap.put("qty", qty + unit.getName());
					}
					materialMap.put("memo", part.getMemo());
					materialList.add(materialMap);
				}
			}
			for (WorkMaterial material : order.getPack().getMaterialList())
			{
				if (material.getIsCustPaper() == BoolValue.YES)
				{
					material.setIsCustPaperText("是");
				}
				Map<String, Object> materialMap = ObjectUtils.objectToMap(material);
				materialMap.put("partName", "");
				materialMap.put("partStlye", "");
				materialMap.put("impressionNum", "");
				materialMap.put("lossQty", "");
				materialMap.put("materialNum", material.getQty());
				if (material.getStockUnitId() != null)
				{
					// 去除小数0
					String qty = StringUtils.removeLastZero(material.getQty());
					Unit unit = (Unit) UserUtils.getBasicInfo(BasicType.UNIT.name(), material.getStockUnitId());
					materialMap.put("qty", qty + unit.getName());
				}
				materialMap.put("memo", "");
				materialList.add(materialMap);
			}

			// 成品工序
			for (WorkProcedure workProcedure : order.getPack().getProcedureList())
			{

				if (workProcedure.getIsOutSource() == BoolValue.YES)
				{
					workProcedure.setIsOutSourceTemplate("是");
				}

				for (WorkReportTask task : taskList)
				{
					if (task.getProcedureId().equals(workProcedure.getProcedureId()))
					{
						workProcedure.setProedureBarCode("<img src='" + QRCodeUtils.createLocalUrl(task.getId().toString()) + "' width='200px' height='30px'/>");
						break;
					}
				}

			}
			map.put("partFlow", partFlow);
			if (order.getProductType() == ProductType.ROTARY)
			{
				map.put("material2List", materialList);
				map.put("materialList", Lists.newArrayList());
			}
			else
			{
				map.put("material2List", Lists.newArrayList());
				map.put("materialList", materialList);
			}
			map.put("packMemo", order.getPack().getMemo());
			map.put("beforeProList", before_procedureList);
			map.put("printBookList", print_bookList);
			map.put("printPackeList", print_packeList);
			map.put("printRotaryList", print_rotaryList);
			map.put("afterPackeList", after_packeList);
			map.put("afterBookList", after_bookList);
			map.put("packProList", order.getPack().getProcedureList());
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());

			map.put("barCode", "<img src='" + QRCodeUtils.createLocalUrl(order.getBillNo()) + "' width='200px' height='50px'/>");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public List<WorkProduct> getWorkProduct(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class);
		query.eq("masterId", id);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkProduct.class);
	}

	@Override
	public Boolean hasCheckAll(String saleOrderBillNo, Long productId)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class, "d");
		query.createAlias(Work.class, JoinType.LEFTJOIN, "m", "m.id=d.masterId");
		query.addProjection(Projections.property("count(1)"));
		query.setQueryType(QueryType.JDBC);
		query.eq("d.sourceBillNo", saleOrderBillNo);
		query.eq("d.productId", productId);
		query.eq("m.isCheck", BoolValue.NO);
		@SuppressWarnings("rawtypes")
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt(map.getResult().get(0).get("count(1)").toString()) == 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getWorkEmployQty(Long materialId, String style)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class, "a");
		query.createAlias(Work.class, JoinType.LEFTJOIN, "b", "b.id=a.workId");
		query.addProjection(Projections.property("sum(a.qty-a.takeQty)"));
		query.eq("a.materialId", materialId);
		query.eq("a.style", style);
		query.eq("b.isCheck", BoolValue.YES);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return new BigDecimal((map.getResult().get(0).get("sum(a.qty-a.takeQty)") == null ? 0 : map.getResult().get(0).get("sum(a.qty-a.takeQty)")).toString());
	}

	@Override
	public SearchResult<WorkReportTask> findProductTaskList(QueryParam queryParam)
	{

		DynamicQuery query = new CompanyDynamicQuery(WorkReportTask.class, "w");
		query.eq("w.isOutSource", BoolValue.NO); // 不发外
		query.eq("w.isSchedule", BoolValue.YES); // 要排程
		query.add(Restrictions.gtProperty("w.yieldQty", "w.reportQty"));
		query.eq("w.isShow", BoolValue.YES); // 是否显示
		if (StringUtils.isNotBlank(queryParam.getMachineName()))
		{
			query.like("w.machineName", "%" + queryParam.getMachineName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("w.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("w.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("w.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("w.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("w.procedureName", "%" + queryParam.getProcedureName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("w.customerName", "%" + queryParam.getCustomerName() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("w.isCheck", queryParam.getAuditFlag());
		}

		if (StringUtils.isNotBlank(queryParam.getSaleBillNo()))
		{
			query.like("w.sourceBillNo", "%" + queryParam.getSaleBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("w.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("w.productName", "%" + queryParam.getProductName() + "%");
		}
		if (queryParam.getCompleteFlag() != null)
		{
			query.eq("w.isForceComplete", queryParam.getCompleteFlag());
		}

		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());

		query.setQueryType(QueryType.JDBC);
		SearchResult<WorkReportTask> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, WorkReportTask.class);
		return result;
	}
	
	@Override
	public WorkReportTask getWorkReportTaskById(Long id)
	{
		return daoFactory.getCommonDao().getEntity(WorkReportTask.class, id);
	}
	
	@Override
	@Transactional
	public void saveReport(WorkReport workReport)
	{
		BoolValue flag = workReport.getIsCheck();
		workReport.setIsCheck(BoolValue.NO);
		// 保存产品上报主表
		workReport.setCompanyId(UserUtils.getCompanyId());
		workReport.setBillType(BillType.PRODUCE_REPORT);
		workReport.setUserNo(UserUtils.getUser().getUserNo());
		// 创建人优先取员工姓名
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			workReport.setCreateName(e.getName());
		}
		else
		{
			workReport.setCreateName(UserUtils.getUserName());
		}
		workReport.setCreateTime(new Date());
		workReport.setCreateEmployeeId(UserUtils.getEmployeeId());
		workReport.setIsForceComplete(BoolValue.NO);
		workReport.setBillNo(UserUtils.createBillNo(BillType.PRODUCE_REPORT));
		daoFactory.getCommonDao().saveEntity(workReport);

		// 保存产品上报详细表
		for (WorkReportDetail detail : workReport.getReportList())
		{
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setUserNo(UserUtils.getUser().getUserNo());
			detail.setMasterId(workReport.getId());
			detail.setUnqualified(detail.getReportQty().subtract(detail.getQualifiedQty()));

			// 计算工时
			if (detail.getStartTime() != null && detail.getEndTime() != null)
			{

			}
			DynamicQuery taskQuery = new CompanyDynamicQuery(WorkReportTask.class);
			taskQuery.eq("id", detail.getTaskId());
			WorkReportTask task = daoFactory.getCommonDao().getByDynamicQuery(taskQuery, WorkReportTask.class);

			task.setReportQty(task.getReportQty().add(detail.getReportQty()));
			if (task.getYieldQty().subtract(task.getReportQty()).compareTo(new BigDecimal(0)) != 1)
			{
				task.setUnreportQty(new BigDecimal(0));
			}
			else
			{
				task.setUnreportQty(task.getYieldQty().subtract(task.getReportQty()));
			}
			task.setUpdateTime(new Date(System.currentTimeMillis()));
			daoFactory.getCommonDao().updateEntity(task);
		}
		daoFactory.getCommonDao().saveAllEntity(workReport.getReportList());

		// 审核
		if (flag == BoolValue.YES)
		{
			serviceFactory.getWorkService().auditReport(BillType.PRODUCE_REPORT, workReport.getId(), BoolValue.YES);
		}

	}

	@Override
	public List<WorkReportDetail> findReportInfo(Long[] ids)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkReportDetail.class);
		query.inArray("id", ids);
		List<WorkReportDetail> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkReportDetail.class);
		return list;
	}

	@Override
	public List<WorkReportTask> findReportTaskInfo(Long[] ids)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkReportTask.class);
		query.inArray("id", ids);
		// query.eq("isOutSource", BoolValue.NO); // 不发外
		// query.eq("isSchedule", BoolValue.YES); // 要排程
		// query.eq("isShow", BoolValue.YES); //是否显示
		// query.add(Restrictions.gtProperty("yieldQty", "reportQty"));
		List<WorkReportTask> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkReportTask.class);
		return list;
	}

	@Override
	public List<WorkReportTask> findReportTaskInfo(Long[] ids, String billNo, String companyId)
	{

		DynamicQuery query = new CompanyDynamicQuery(WorkReportTask.class);
		if (StringUtils.isNoneBlank(companyId))
		{
			query.eq("companyId", companyId);
		}
		if (ids != null)
		{
			query.inArray("id", ids);
		}
		else if (StringUtils.isNotBlank(billNo))
		{
			if (billNo.startsWith("MO"))
				query.like("billNo", "%" + billNo + "%");
			if (billNo.startsWith("SO"))
				query.like("sourceBillNo", "%" + billNo + "%");
		}
		query.asc("sort");
		List<WorkReportTask> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkReportTask.class);
		return list;
	}

	@Override
	public List<WorkReportTask> findReportTaskUnreportQtyInfo(Long[] ids, String billNo, String companyId)
	{

		DynamicQuery query = new CompanyDynamicQuery(WorkReportTask.class);
		if (StringUtils.isNoneBlank(companyId))
		{
			query.eq("companyId", companyId);
		}
		if (ids != null)
		{
			query.inArray("id", ids);
		}
		else if (StringUtils.isNotBlank(billNo))
		{
			billNo = billNo.trim();// 去掉空格
			if (billNo.startsWith("MO"))
				query.like("billNo", "%" + billNo + "%");// 要求不可使用不完全匹配查询
			query.eq("isOutSource", BoolValue.NO);// 只能上报不是发外工序
			query.eq("isSchedule", BoolValue.YES);// 只能上报是排成工序
		}
		query.gt("unreportQty", new BigDecimal(0));// 未上报数大于0
		List<WorkReportTask> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkReportTask.class);
		List<WorkReportTask> errorList = new ArrayList<WorkReportTask>();
		for (WorkReportTask task : list)
		{
			// 如果任务强制完工，返回这个任务
			if (BoolValue.YES.equals(task.getIsForceComplete()))
			{
				errorList.add(task);
				return errorList;
			}
			// 如果任务不显示，返回这个任务
			if (BoolValue.NO.equals(task.getIsShow()))
			{
				errorList.add(task);
				return errorList;
			}
		}
		return list;
	}

	@Override
	public WorkReport findWorkReportById(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkReport.class);
		query.eq("id", id);
		WorkReport workReport = daoFactory.getCommonDao().getByDynamicQuery(query, WorkReport.class);

		DynamicQuery detailQuery = new CompanyDynamicQuery(WorkReportDetail.class);
		detailQuery.eq("masterId", id);
		detailQuery.asc("id");
		List<WorkReportDetail> list = daoFactory.getCommonDao().findEntityByDynamicQuery(detailQuery, WorkReportDetail.class);
		for (WorkReportDetail detail : list)
		{
			DynamicQuery taskQuery = new CompanyDynamicQuery(WorkReportTask.class);
			taskQuery.eq("id", detail.getTaskId());
			WorkReportTask task = daoFactory.getCommonDao().getByDynamicQuery(taskQuery, WorkReportTask.class);
			detail.setTask(task);
		}
		workReport.setReportList(list);
		return workReport;
	}

	@Override
	@Transactional
	public void updateReport(WorkReport workReport)
	{
		DynamicQuery queryDetail = new CompanyDynamicQuery(WorkReport.class);
		queryDetail.eq("id", workReport.getId());
		WorkReport report = daoFactory.getCommonDao().getByDynamicQuery(queryDetail, WorkReport.class);
		// 先判断是否已经审核
		if (report.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		
		report.setMemo(workReport.getMemo());
		report.setEmployeeName(workReport.getEmployeeName());
		report.setEmployeeId(workReport.getEmployeeId());
		report.setReportTime(workReport.getReportTime());

		if (workReport.getIsCheck() == BoolValue.YES)
		{
			// 审核
			report.setCheckTime(new Date());
			// 审核人优先去员工姓名
			Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
			if (e != null)
			{
				report.setCheckUserName(e.getName());
			}
			else
			{
				report.setCheckUserName(UserUtils.getUser().getUserName());
			}
			report.setIsCheck(workReport.getIsCheck());
		}
		daoFactory.getCommonDao().updateEntity(report);

		DynamicQuery query = new CompanyDynamicQuery(WorkReportDetail.class);
		query.in("masterId", workReport.getId());

		List<WorkReportDetail> newReportList = workReport.getReportList();
		List<WorkReportDetail> oldReportList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkReportDetail.class);
		Map<Long, WorkReportDetail> map = new HashMap<>();
		for (WorkReportDetail _detail : oldReportList)
		{
			map.put(_detail.getId(), _detail);
		}
		for (int i = 0; i < newReportList.size(); i++)
		{
			WorkReportDetail newDetail = newReportList.get(i);
			WorkReportDetail oldDetail = map.get(newReportList.get(i).getId());
			if (null != oldDetail)
			{
				// 反写生产任务
				DynamicQuery taskQuery = new CompanyDynamicQuery(WorkReportTask.class);
				taskQuery.eq("id", oldDetail.getTaskId());
				WorkReportTask task = daoFactory.getCommonDao().getByDynamicQuery(taskQuery, WorkReportTask.class);
				task.setReportQty(task.getReportQty().add(newDetail.getReportQty().subtract(oldDetail.getReportQty())));
				task.setUnreportQty(task.getYieldQty().subtract(task.getReportQty()));
				daoFactory.getCommonDao().updateEntity(task);

				oldDetail.setReportQty(newDetail.getReportQty());
				oldDetail.setQualifiedQty(newDetail.getQualifiedQty());
				oldDetail.setUnqualified(newDetail.getReportQty().subtract(newDetail.getQualifiedQty()));
				oldDetail.setMemo(newDetail.getMemo());

				oldDetail.setStartTime(newDetail.getStartTime());
				oldDetail.setEndTime(newDetail.getEndTime());
				// 计算时长

			}
		}
		daoFactory.getCommonDao().updateAllEntity(oldReportList);
		// 删除记录
		Map<String,List<WorkReportDetail>> resultMap = ServiceHelper.filterCUD(oldReportList, newReportList);
		List<WorkReportDetail> listDel = resultMap.get(ServiceHelper.Cud.D);
		for (WorkReportDetail detail_ : listDel)
		{
			DynamicQuery taskQuery = new CompanyDynamicQuery(WorkReportTask.class);
			taskQuery.eq("id", detail_.getTaskId());
			WorkReportTask task = daoFactory.getCommonDao().getByDynamicQuery(taskQuery, WorkReportTask.class);
			task.setReportQty(task.getReportQty().subtract(detail_.getReportQty()));
			task.setUnreportQty(task.getYieldQty().subtract(task.getReportQty()));
		}
		daoFactory.getCommonDao().deleteAllEntity(listDel); //删除记录
	}

	@Override
	@Transactional
	public void auditReport(BillType billType, Long id, BoolValue boolValue)
	{
		serviceFactory.getCommonService().audit(billType, id, boolValue);
	}

	@Override
	@Transactional
	public void deleteReport(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkReportDetail.class);
		query.eq("masterId", id);
		List<WorkReportDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkReportDetail.class);

		// 反写生产任务单
		for (WorkReportDetail detail : detailList)
		{
			DynamicQuery taskQuery = new CompanyDynamicQuery(WorkReportTask.class);
			taskQuery.eq("id", detail.getTaskId());
			WorkReportTask task = daoFactory.getCommonDao().getByDynamicQuery(taskQuery, WorkReportTask.class);
			task.setReportQty(task.getReportQty().subtract(detail.getReportQty()));
			task.setUnreportQty(task.getYieldQty().subtract(task.getReportQty()));
			daoFactory.getCommonDao().updateEntity(task);
		}

		// 删除详细表
		daoFactory.getCommonDao().deleteAllEntity(detailList);

		// 删除主表
		daoFactory.getCommonDao().deleteEntity(WorkReport.class, id);

	}

	@Override
	public List<WorkReportDetail> findReportDetailByTaskId(Long taskId)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkReportDetail.class);
		query.eq("taskId", taskId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkReportDetail.class);
	}

	@Override
	public boolean findReportDetailByTask(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Work.class);
		query.eq("id", id);
		Work work = daoFactory.getCommonDao().getByDynamicQuery(query, Work.class);
		if (work != null)
		{
			DynamicQuery taskQuery = new CompanyDynamicQuery(WorkReportTask.class);
			taskQuery.eq("billNo", work.getBillNo());
			List<WorkReportTask> taskList = daoFactory.getCommonDao().findEntityByDynamicQuery(taskQuery, WorkReportTask.class);
			if (CollectionUtils.isNotEmpty(taskList))
			{
				for (WorkReportTask task : taskList)
				{
					if (task.getReportQty().compareTo(new BigDecimal(0)) == 1)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	@Transactional
	public void deleteReportTask(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Work.class);
		query.eq("id", id);
		Work work = daoFactory.getCommonDao().getByDynamicQuery(query, Work.class);
		if (work != null)
		{
			DynamicQuery taskQuery = new CompanyDynamicQuery(WorkReportTask.class);
			taskQuery.eq("billNo", work.getBillNo());
			List<WorkReportTask> taskList = daoFactory.getCommonDao().findEntityByDynamicQuery(taskQuery, WorkReportTask.class);
			daoFactory.getCommonDao().deleteAllEntity(taskList);
		}
	}
	
	private void _deleteReportTaskByList(List<WorkProcedure> delList)
	{
		List<WorkReportTask> _delList = Lists.newArrayList();
		for (WorkProcedure procedure : delList)
		{
			DynamicQuery taskQuery = new CompanyDynamicQuery(WorkReportTask.class);
			taskQuery.eq("billNo", procedure.getWorkBillNo());
			taskQuery.eq("procedureId", procedure.getProcedureId());
			if (procedure.getProcedureType() == ProcedureType.FINISHED)
			{
				taskQuery.isNull("partId");
			} else
			{
				taskQuery.eq("partId", procedure.getParentId());
			}
			WorkReportTask task = daoFactory.getCommonDao().getByDynamicQuery(taskQuery, WorkReportTask.class);
			if (task != null)
			{
				_delList.add(task);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(_delList);
	}
	
	@Override
	public SearchResult<Map<String, Object>> findWorkMaterials(Long workId, String companyId)
	{
		DynamicQuery query_product = new CompanyDynamicQuery(WorkPart.class);
		if (StringUtils.isNoneBlank(companyId))
		{
			query_product.eq("companyId", companyId);
		}
		query_product.eq("masterId", workId);
		List<WorkPart> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query_product, WorkPart.class);

		// 查询部件
		for (WorkPart part : list)
		{
			part.setMaterialList(this.getWorkMaterialListByPartId(part.getId()));
		}

		// 查询成品
		DynamicQuery query = new CompanyDynamicQuery(WorkPack.class);
		query.eq("masterId", workId);
		WorkPack pack = daoFactory.getCommonDao().getByDynamicQuery(query, WorkPack.class);
		pack.setMaterialList(this.getWorkMaterialListByPackId(pack.getId()));

		List<Map<String, Object>> rets = Lists.newArrayList();

		// 查询所有采购得库存数量（最后统计库存）
		List<PurchOrderDetail> purchDetaiList = serviceFactory.getPurOrderService().getDetailByWork(workId);
		Map<String, BigDecimal> purchDetailMap = Maps.newHashMap();// key(工单id+部件或成品id) + value(detail)
		for (PurchOrderDetail detail : purchDetaiList)
		{
			// 先合并
			String key = workId + "" + detail.getParentId();
			BigDecimal oldStockQty = purchDetailMap.get(key);
			if (null != oldStockQty)
			{
				detail.setStorageQty(detail.getStorageQty().add(oldStockQty));
			}
			purchDetailMap.put(workId + "" + detail.getParentId(), detail.getStorageQty());
		}

		// 遍历所有部件得材料
		for (WorkPart part : list)
		{
			for (WorkMaterial material : part.getMaterialList())
			{
				Map<String, Object> ret = Maps.newHashMap();
				ret.put("partName", part.getPartName());
				ret.put("materialName", material.getMaterialName());
				ret.put("style", material.getStyle());
				ret.put("weight", material.getWeight());
				ret.put("qty", material.getQty());
				ret.put("purchQty", material.getPurchQty());
				ret.put("takeQty", material.getTakeQty());
				ret.put("stockQty", 0);// 默认为0
				BigDecimal stockQty = purchDetailMap.get(workId + "" + material.getId());
				if (null != stockQty)
				{
					ret.put("stockQty", stockQty);
				}
				rets.add(ret);
			}
		}
		// 遍历所有成品材料
		for (WorkMaterial material : pack.getMaterialList())
		{
			Map<String, Object> ret = Maps.newHashMap();
			ret.put("partName", "");
			ret.put("materialName", material.getMaterialName());
			ret.put("style", material.getStyle());
			ret.put("weight", material.getWeight());
			ret.put("qty", material.getQty());
			ret.put("purchQty", material.getPurchQty());
			ret.put("takeQty", material.getTakeQty());
			ret.put("stockQty", 0);// 默认为0
			BigDecimal stockQty = purchDetailMap.get(workId + "" + material.getId());
			if (null != stockQty)
			{
				ret.put("stockQty", stockQty);
			}
			rets.add(ret);
		}
		SearchResult<Map<String, Object>> result = new SearchResult<>();
		result.setResult(rets);
		result.setCount(rets.size());
		return result;
	}

	@Override
	public SearchResult<Map<String, Object>> findReportTaskDetail(String billNo, String companyId)
	{
		// 取出该工单号下的所有任务
		List<WorkReportTask> taskArr = this.findReportTaskInfo(null, billNo, companyId);
		// 创建一个map对象以部件名称为key 任务对象为value
		Map<String, List<WorkReportTask>> resultMap = Maps.newLinkedHashMap();
		// 遍历集合分成多个数组
		for (WorkReportTask task : taskArr)
		{
			if (task.getPartName() == null)
			{
				task.setPartName("成品工序");
			}
			if (resultMap.containsKey(task.getPartName()))
			{
				resultMap.get(task.getPartName()).add(task);
			}
			else
			{
				List<WorkReportTask> tmpList = new ArrayList<WorkReportTask>();
				tmpList.add(task);
				resultMap.put(task.getPartName(), tmpList);
			}
		}
		Set<Entry<String, List<WorkReportTask>>> entrySet = resultMap.entrySet();
		Iterator<Entry<String, List<WorkReportTask>>> iterator = entrySet.iterator();
		Map<String, Object> parent = null;
		List<Map<String, Object>> parents = Lists.newArrayList();
		List<Map<String, Object>> childList = null;
		WorkReportTask task = null;
		Map<String, Object> children = null;
		BigDecimal sum_yield = new BigDecimal(0);
		BigDecimal sum_report = new BigDecimal(0);
		while (iterator.hasNext())
		{
			Entry<String, List<WorkReportTask>> entry = iterator.next();
			List<WorkReportTask> value = entry.getValue();

			// 多次上报得情况下，根据工序名称，需要合并
			Set<String> taskSet = Sets.newLinkedHashSet();
			Map<String, WorkReportTask> procedureMap = Maps.newLinkedHashMap();
			for (int i = 0; i < value.size(); i++)
			{
				WorkReportTask newTask = value.get(i);
				String procedureName = newTask.getProcedureName();
				WorkReportTask newTask2 = procedureMap.get(procedureName);

				// 合并不合格数（这里注意一个BUG，理论上detailList一定有值。为了兼容BUG，所以这么写）
				List<WorkReportDetail> detailList = serviceFactory.getWorkService().findReportDetailByTaskId(newTask.getId());
				if (null != detailList && detailList.size() > 0)
				{
					BigDecimal reportQty = new BigDecimal(0);
					BigDecimal qualifiedQty = new BigDecimal(0);
					BigDecimal unqualified = new BigDecimal(0);
					for (WorkReportDetail detail : detailList)
					{
						// 上报数
						reportQty = reportQty.add(detail.getReportQty());
						// 合格数
						qualifiedQty = qualifiedQty.add(detail.getQualifiedQty());
						// 不合格数
						unqualified = unqualified.add(detail.getUnqualified());
					}
					// 上报数
					newTask.setReportQty(reportQty);
					// 合格数
					newTask.setQualifiedQty(qualifiedQty);
					// 不合格数
					newTask.setUnqualified(unqualified);
				}

				if (null == newTask2)
				{
					// 第一个作为默认
					newTask2 = newTask;
					procedureMap.put(procedureName, newTask2);
				}
				else
				{
					// 合并上报数
					BigDecimal reportQty = newTask2.getReportQty().add(newTask.getReportQty());
					newTask2.setReportQty(reportQty);
					// 合并合格数
					BigDecimal qualifiedQty = newTask2.getQualifiedQty().add(newTask.getQualifiedQty());
					newTask2.setQualifiedQty(qualifiedQty);
					// 合并不合格数
					BigDecimal unqualified = newTask2.getUnqualified().add(newTask.getUnqualified());
					newTask2.setUnqualified(unqualified);
				}

				taskSet.add(procedureName);
			}

			// 构造列表
			parent = new HashMap<String, Object>();
			childList = Lists.newArrayList();
			for (Iterator<String> it2 = taskSet.iterator(); it2.hasNext();)
			{
				children = Maps.newLinkedHashMap();
				String procedureName = it2.next();
				task = procedureMap.get(procedureName);

				children.put("isOutSource", task.getIsOutSource().getValue());
				children.put("procedureName", task.getProcedureName());// 部件名称
				children.put("yieldQty", task.getYieldQty());// 应产数
				sum_yield = sum_yield.add(task.getYieldQty());
				children.put("reportQty", task.getReportQty());// 上报数
				sum_report = sum_report.add(task.getReportQty());
				children.put("createTime", task.getCreateTime());
				children.put("updateTime", task.getUpdateTime());
				if (task.getReportQty().compareTo(new BigDecimal(0)) != 0)
				{
					children.put("qualifiedQty", task.getQualifiedQty());// 合格数
					children.put("unqualified", task.getUnqualified());// 不合格数
				}
				else
				{
					children.put("qualifiedQty", 0);// 合格数
					children.put("unqualified", 0);//
				}
				childList.add(children);
			}
			// 增加排序序号
			if (task.getPartSort() != null)
			{
				parent.put("sort", task.getPartSort());
			}
			else
			{
				parent.put("sort", 1000);
			}

			parent.put("partName", entry.getKey());
			parent.put("child", childList);
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMaximumFractionDigits(2);
			String result = numberFormat.format(sum_report.floatValue() / sum_yield.floatValue());
			parent.put("rate", result + "%");
			sum_report = new BigDecimal(0);
			sum_yield = new BigDecimal(0);
			parents.add(parent);
		}
		// 增加排序
		Collections.sort(parents, new Comparator<Map<String, Object>>()
		{

			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2)
			{

				int i = Integer.parseInt(o1.get("sort").toString()) - Integer.parseInt(o2.get("sort").toString());
				return i;
			}

		});
		SearchResult<Map<String, Object>> result = new SearchResult<>();
		result.setResult(parents);
		result.setCount(taskArr.size());
		parents = null;
		taskArr = null;
		parent = null;
		resultMap = null;
		return result;
	}

	@Override
	public void completeTaskByWorkId(Long[] id, BoolValue boolValue)
	{
		DynamicQuery query = new CompanyDynamicQuery(Work.class);
		query.eq("id", id[0]);
		Work work = daoFactory.getCommonDao().getByDynamicQuery(query, Work.class);

		if (work != null)
		{

			DynamicQuery taskQuery = new CompanyDynamicQuery(WorkReportTask.class);
			taskQuery.eq("billNo", work.getBillNo());
			List<WorkReportTask> list = daoFactory.getCommonDao().findEntityByDynamicQuery(taskQuery, WorkReportTask.class);

			List<Long> ids = new ArrayList<Long>();
			for (WorkReportTask task : list)
			{
				ids.add(task.getId());
			}
			serviceFactory.getCommonService().forceComplete2(WorkReportTask.class, ids, boolValue);
		}

	}

	@Override
	@Transactional
	public void updateReportTask(Long id, BoolValue boolValue, List<WorkPart> update_part_list, List<WorkPart> del_part_list)
	{
		// 转换成map<String, partName>
		Map<Long, String> updatePartMap = Maps.newHashMap();
		if (null != update_part_list)
		{
			for (WorkPart workPart : update_part_list)
			{
				updatePartMap.put(workPart.getId(), workPart.getPartName());
			}
		}

		Work work = this.get(id);

		if (work.getIsForceComplete() == BoolValue.NO)
		{

			// 原生产任务信息
			DynamicQuery taskQuery = new CompanyDynamicQuery(WorkReportTask.class);
			taskQuery.eq("billNo", work.getBillNo());
			List<WorkReportTask> taskList = daoFactory.getCommonDao().findEntityByDynamicQuery(taskQuery, WorkReportTask.class);

			String sourceBillNo = "";
			String customerName = "";
			String customerMaterialCode = "";
			String productName = "";
			String specifications = "";
			String customerBillNo = "";
			Integer produceQty = 0;
			String customerIds = "";
			String productIds = "";
			for (WorkProduct product : work.getProductList())
			{
				if (StringUtils.isBlank(customerBillNo))
				{
					customerBillNo = product.getCustomerBillNo();
				}
				else
				{
					customerBillNo += "," + product.getCustomerBillNo();
				}
				if (StringUtils.isBlank(sourceBillNo))
				{
					sourceBillNo = product.getSourceBillNo();
				}
				else
				{
					sourceBillNo += "," + product.getSourceBillNo();
				}

				if (StringUtils.isBlank(customerName))
				{
					customerName = product.getCustomerName();
				}
				else
				{
					customerName += "," + product.getCustomerName();
				}

				if (StringUtils.isBlank(customerMaterialCode))
				{
					customerMaterialCode = product.getCustomerMaterialCode();
				}
				else
				{
					customerMaterialCode += "," + product.getCustomerMaterialCode();
				}

				if (StringUtils.isBlank(productName))
				{
					productName = product.getProductName();
				}
				else
				{
					productName += "," + product.getProductName();
				}

				if (StringUtils.isBlank(specifications))
				{
					specifications = product.getStyle();
				}
				else
				{
					specifications += "," + product.getStyle();
				}
				produceQty += product.getProduceQty().intValue();
				// 为全局变更名称做的特殊处理
				customerIds += "B" + product.getCustomerId().toString() + "E,";
				productIds += "B" + product.getProductId().toString() + "E,";
			}

			// 无工序直接保存所有工序
			if (CollectionUtils.isEmpty(taskList))
			{
				this.savaReportTask(id, boolValue);
			}
			else
			{
				/**
				 * 生成工单保存和审核处理的逻辑：
				 * 1. 保存
				 *   1.1 直接同步工序，并删除多余部件的工序
				 *   1.2 设置isShow=false
				 * 2. 保存并审核
				 *   2.1 直接同步工序，并删除多余部件的工序
				 *   2.1 设置isShow=true
				 * 3. 审核
				 *   3.1 设置isShow=true
				 */

				List<WorkPart> partList = work.getPartList();
				int sort = 0;
				int partSort = 0;

				// 同步部件
				for (WorkPart workPart : partList)
				{
					// 单独审核，不要更新
					if (updatePartMap.size() == 0 && boolValue == BoolValue.YES)
					{
						continue;
					}

					// 判断是不是只是修改了部件名称（有可能只是修改了部件名称）
					boolean isUpdatePart = false;
					String updatedPartName = updatePartMap.get(workPart.getId());
					if (null != updatedPartName && updatedPartName.equals(workPart.getPartName()))
					{
						isUpdatePart = true;
					}

					// 同步工序
					for (WorkProcedure workProcedure : workPart.getProcedureList())
					{
						boolean flag = true;
						for (WorkReportTask task : taskList)
						{
							// 比对部件名称、工序id
							if (isUpdatePart && task.getPartId() != null && task.getPartId().equals(workPart.getId()) && task.getProcedureCode().equals(workProcedure.getProcedureCode()))
							{
								flag = false;

								DynamicQuery proQuery = new CompanyDynamicQuery(Procedure.class);
								proQuery.eq("id", workProcedure.getProcedureId());
								Procedure procedure = daoFactory.getCommonDao().getByDynamicQuery(proQuery, Procedure.class);

								task.setIsOutSource(workProcedure.getIsOutSource());// 是否发外
								task.setIsSchedule(procedure.getIsSchedule()); // 是否排程
								task.setSort(sort++); // 工序排序
								task.setProcedureRefId(workProcedure.getId());
								task.setProduceQty(workPart.getQty());
								if (procedure.getScheduleDataSource() == ScheduleDataSource.OUTPUT)
								{
									task.setYieldQty(workProcedure.getOutputQty());
								}
								if (procedure.getScheduleDataSource() == ScheduleDataSource.INPUT)
								{
									task.setYieldQty(workProcedure.getInputQty());
								}
								task.setUnreportQty(task.getUnreportQty());
								task.setDeliveryTime(work.getProductList().get(0).getDeliveryTime());
								task.setCreateTime(new Date());
								task.setSourceBillNo(sourceBillNo);
								task.setBillNo(work.getBillNo());
								task.setCustomerName(customerName);
								task.setCustomerMaterialCode(customerMaterialCode);
								task.setProductName(productName);
								task.setSpecifications(specifications);
								task.setProcedureClassId(workProcedure.getProcedureClassId()); // 工序分类 ID
								task.setProcedureId(workProcedure.getProcedureId()); // 工序ID
								task.setProcedureCode(workProcedure.getProcedureCode());// 工序Code
								task.setProcedureName(workProcedure.getProcedureName());// 工序名称
								task.setProcedureType(workProcedure.getProcedureType()); // 工序类型
								task.setPartId(workPart.getId());
								task.setPartName(workPart.getPartName());
								task.setStyle(workPart.getStyle());
								task.setCompanyId(UserUtils.getCompanyId());
								task.setUserNo(UserUtils.getUser().getUserNo());
								task.setCustomerId(customerIds); // 客户id集合
								task.setProductId(productIds); // 产品id集合
								if (procedure.getProcedureType() == ProcedureType.PRINT)
								{
									task.setMachineName(workPart.getMachineName());
								}
								task.setCustomerBillNo(customerBillNo);
								task.setPartSort(partSort++);
								task.setIsShow(boolValue);

								daoFactory.getCommonDao().updateEntity(task);
							}
						}

						// 新的工序
						if (flag)
						{
							DynamicQuery proQuery = new CompanyDynamicQuery(Procedure.class);
							proQuery.eq("id", workProcedure.getProcedureId());
							Procedure procedure = daoFactory.getCommonDao().getByDynamicQuery(proQuery, Procedure.class);

							WorkReportTask task = new WorkReportTask();
							task.setIsPart(BoolValue.YES);// 是否部件
							task.setIsOutSource(workProcedure.getIsOutSource());// 是否发外
							task.setIsSchedule(procedure.getIsSchedule()); // 是否排程
							task.setSort(sort++); // 工序排序
							task.setProcedureRefId(workProcedure.getId());
							task.setProduceQty(workPart.getQty());
							if (procedure.getScheduleDataSource() == ScheduleDataSource.OUTPUT)
							{
								task.setYieldQty(workProcedure.getOutputQty());
							}
							if (procedure.getScheduleDataSource() == ScheduleDataSource.INPUT)
							{
								task.setYieldQty(workProcedure.getInputQty());
							}
							task.setUnreportQty(task.getYieldQty());
							task.setDeliveryTime(work.getProductList().get(0).getDeliveryTime());
							task.setCreateTime(new Date());
							task.setSourceBillNo(sourceBillNo);
							task.setBillNo(work.getBillNo());
							task.setCustomerName(customerName);
							task.setCustomerMaterialCode(customerMaterialCode);
							task.setProductName(productName);
							task.setSpecifications(specifications);
							task.setProcedureClassId(workProcedure.getProcedureClassId()); // 工序分类 ID
							task.setProcedureId(workProcedure.getProcedureId()); // 工序ID
							task.setProcedureCode(workProcedure.getProcedureCode());// 工序Code
							task.setProcedureName(workProcedure.getProcedureName());// 工序名称
							task.setProcedureType(workProcedure.getProcedureType()); // 工序类型
							task.setPartId(workPart.getId());
							task.setPartName(workPart.getPartName());
							task.setStyle(workPart.getStyle());
							task.setCompanyId(UserUtils.getCompanyId());
							task.setUserNo(UserUtils.getUser().getUserNo());
							task.setCustomerId(customerIds); // 客户id集合
							task.setProductId(productIds); // 产品id集合
							if (procedure.getProcedureType() == ProcedureType.PRINT)
							{
								task.setMachineName(workPart.getMachineName());
							}
							task.setCustomerBillNo(customerBillNo);
							task.setPartSort(partSort++);
							task.setIsShow(boolValue);

							daoFactory.getCommonDao().saveEntity(task);
							QRCodeUtils.getQRCodeImgFile(task.getId().toString());

						}
					}
				}

				// 同步成品工序
				if (work.getPack() != null)
				{
					for (WorkProcedure workProcedure : work.getPack().getProcedureList())
					{
						boolean flag = true;

						DynamicQuery proQuery = new CompanyDynamicQuery(Procedure.class);
						proQuery.eq("id", workProcedure.getProcedureId());
						Procedure procedure = daoFactory.getCommonDao().getByDynamicQuery(proQuery, Procedure.class);

						for (WorkReportTask task : taskList)
						{
							if (task.getProcedureCode().equals(workProcedure.getProcedureCode()))
							{
								flag = false;

								task.setIsPart(BoolValue.NO);// 是否部件
								task.setIsOutSource(workProcedure.getIsOutSource());// 是否发外
								task.setIsSchedule(procedure.getIsSchedule()); // 是否排程
								task.setSort(sort++); // 工序排序
								task.setProcedureRefId(workProcedure.getId());
								task.setProduceQty(produceQty);
								if (procedure.getScheduleDataSource() == ScheduleDataSource.OUTPUT)
								{
									task.setYieldQty(workProcedure.getOutputQty());
								}
								if (procedure.getScheduleDataSource() == ScheduleDataSource.INPUT)
								{
									task.setYieldQty(workProcedure.getInputQty());
								}
								task.setUnreportQty(task.getUnreportQty());
								task.setDeliveryTime(work.getProductList().get(0).getDeliveryTime());
								task.setCreateTime(new Date());
								task.setSourceBillNo(sourceBillNo);
								task.setBillNo(work.getBillNo());
								task.setCustomerName(customerName);
								task.setCustomerMaterialCode(customerMaterialCode);
								task.setProductName(productName);
								task.setSpecifications(specifications);
								task.setProcedureClassId(workProcedure.getProcedureClassId()); // 工序分类 ID
								task.setProcedureId(workProcedure.getProcedureId()); // 工序ID
								task.setProcedureCode(workProcedure.getProcedureCode());// 工序Code
								task.setProcedureName(workProcedure.getProcedureName());// 工序名称
								task.setProcedureType(workProcedure.getProcedureType()); // 工序类型
								task.setCompanyId(UserUtils.getCompanyId());
								task.setUserNo(UserUtils.getUser().getUserNo());
								task.setCustomerBillNo(customerBillNo);
								task.setIsShow(BoolValue.YES);
								task.setCustomerId(customerIds); // 客户id集合
								task.setProductId(productIds); // 产品id集合
								daoFactory.getCommonDao().updateEntity(task);

							}
						}

						if (flag)
						{

							WorkReportTask task = new WorkReportTask();
							task.setIsPart(BoolValue.NO);// 是否部件
							task.setIsOutSource(workProcedure.getIsOutSource());// 是否发外
							task.setIsSchedule(procedure.getIsSchedule()); // 是否排程
							task.setSort(sort++); // 工序排序
							task.setProcedureRefId(workProcedure.getId());
							task.setProduceQty(produceQty);
							if (procedure.getScheduleDataSource() == ScheduleDataSource.OUTPUT)
							{
								task.setYieldQty(workProcedure.getOutputQty());
							}
							if (procedure.getScheduleDataSource() == ScheduleDataSource.INPUT)
							{
								task.setYieldQty(workProcedure.getInputQty());
							}
							task.setUnreportQty(task.getYieldQty());
							task.setDeliveryTime(work.getProductList().get(0).getDeliveryTime());
							task.setCreateTime(new Date());
							task.setSourceBillNo(sourceBillNo);
							task.setBillNo(work.getBillNo());
							task.setCustomerName(customerName);
							task.setCustomerMaterialCode(customerMaterialCode);
							task.setProductName(productName);
							task.setSpecifications(specifications);
							task.setProcedureClassId(workProcedure.getProcedureClassId()); // 工序分类 ID
							task.setProcedureId(workProcedure.getProcedureId()); // 工序ID
							task.setProcedureCode(workProcedure.getProcedureCode());// 工序Code
							task.setProcedureName(workProcedure.getProcedureName());// 工序名称
							task.setProcedureType(workProcedure.getProcedureType()); // 工序类型
							task.setCompanyId(UserUtils.getCompanyId());
							task.setUserNo(UserUtils.getUser().getUserNo());
							task.setCustomerBillNo(customerBillNo);
							task.setCustomerId(customerIds); // 客户id集合
							task.setProductId(productIds); // 产品id集合
							daoFactory.getCommonDao().saveEntity(task);
							QRCodeUtils.getQRCodeImgFile(task.getId().toString());
						}
					}
				}

				// 审核后需要更新为yes
				if (boolValue == BoolValue.YES)
				{
					for (WorkReportTask task : taskList)
					{
						task.setIsShow(BoolValue.YES);
						daoFactory.getCommonDao().updateEntity(task);
					}
				}

				// 清除多余部件
				if (del_part_list != null && del_part_list.size() > 0)
				{
					List<WorkReportTask> deleteTaskList = Lists.newArrayList();
					for (WorkPart workPart : del_part_list)
					{
						String partName = workPart.getPartName();
						Long partId = workPart.getId();
						for (WorkReportTask task : taskList)
						{
							if (task.getPartId() != null && task.getPartId().equals(partId))
							{
								deleteTaskList.add(task);
							}
							else if (partName.equals(task.getPartName()))
							{
								deleteTaskList.add(task);
							}
						}
					}

					daoFactory.getCommonDao().deleteAllEntity(deleteTaskList);
				}
			}
		}

	}

	@Override
	@Transactional
	public void updateReportTaskState(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Work.class);
		query.eq("id", id);
		Work work = daoFactory.getCommonDao().getByDynamicQuery(query, Work.class);
		if (work != null)
		{
			DynamicQuery taskQuery = new CompanyDynamicQuery(WorkReportTask.class);
			taskQuery.eq("billNo", work.getBillNo());
			List<WorkReportTask> taskList = daoFactory.getCommonDao().findEntityByDynamicQuery(taskQuery, WorkReportTask.class);
			for (WorkReportTask task : taskList)
			{
				task.setIsShow(BoolValue.NO);
			}
			daoFactory.getCommonDao().updateAllEntity(taskList);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> findMateriaState(Long id)
	{
		// 0未被任何订单引用 1被采购订单引用 2被生产领料引用 3被采购订单和生产领料同时引用
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", "0");

		DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class);
		query.eq("sourceDetailId", id);
		List<PurchOrderDetail> purchOrderDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchOrderDetail.class);

		DynamicQuery takeQuery = new CompanyDynamicQuery(StockMaterialTakeDetail.class);
		takeQuery.eq("sourceDetailId", id);
		List<StockMaterialTakeDetail> stockMaterialTakeDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(takeQuery, StockMaterialTakeDetail.class);

		if (CollectionUtils.isNotEmpty(purchOrderDetailList))
		{
			List list = new ArrayList();
			for (PurchOrderDetail detail : purchOrderDetailList)
			{
				PurchOrder purchOrder = daoFactory.getCommonDao().getEntity(PurchOrder.class, detail.getMasterId());
				list.add(purchOrder);
			}
			map.put("POBillNo", list);
		}

		if (CollectionUtils.isNotEmpty(stockMaterialTakeDetailList))
		{
			List list = new ArrayList();
			for (StockMaterialTakeDetail detail : stockMaterialTakeDetailList)
			{
				StockMaterialTake stockMaterialTake = daoFactory.getCommonDao().getEntity(StockMaterialTake.class, detail.getMasterId());
				list.add(stockMaterialTake);
			}
			map.put("MRBillNo", list);
		}

		if (CollectionUtils.isNotEmpty(purchOrderDetailList) && CollectionUtils.isEmpty(stockMaterialTakeDetailList))
		{
			map.put("state", "1");
		}
		if (CollectionUtils.isEmpty(purchOrderDetailList) && CollectionUtils.isNotEmpty(stockMaterialTakeDetailList))
		{
			map.put("state", "2");
		}
		if (CollectionUtils.isNotEmpty(purchOrderDetailList) && CollectionUtils.isNotEmpty(stockMaterialTakeDetailList))
		{
			map.put("state", "3");
		}
		return map;
	}

	@Override
	@Transactional
	public void updateMateria(Work order) throws Exception
	{
		// 更新工单的部件材料列表信息
		for (WorkPart part : order.getPartList())
		{
			// 旧数据
			List<WorkMaterial> oldList = this.getWorkMaterialListByPartId(part.getId());
			// 新数据
			List<WorkMaterial> newList = part.getMaterialList();
			_updateMaterial(newList, oldList, part.getId(), order.getId(), order.getBillNo(),WorkMaterialType.PART);
			
		}
		// 更新工单的成品材料列表信息
		// 旧数据
		List<WorkMaterial> oldList = this.getWorkMaterialListByPackId(order.getPack().getId());
		// 新数据
		List<WorkMaterial> newList = order.getPack().getMaterialList();
		_updateMaterial(newList, oldList, order.getPack().getId(), order.getId(), order.getBillNo(),WorkMaterialType.PRODUCT);
		updateReportTask(order.getId(), BoolValue.YES, order.getPartList(), null);
		// 更新部件工序材料(下游数据的变更) 采购订单-采购入库-采购退货-采购对账-材料库存管理-生产领料的材料规格、计价数量、金额、税额都需要修改
		for (WorkPart part : order.getPartList())
		{
			for (WorkMaterial workMaterial : part.getMaterialList())
			{
				updateWorkMaterial(workMaterial);
			}

		}

		// 更新成品工序材料(下游数据的变更)
		List<WorkMaterial> list = order.getPack().getMaterialList();
		for (WorkMaterial workMaterial : list)
		{
			updateWorkMaterial(workMaterial);
		}

	}
	
	public void _updateMaterial(List<WorkMaterial> newList, List<WorkMaterial> oldList, Long parentId,Long workId, String workBillNo,WorkMaterialType workMaterialType)
	{	
		Map<Long,WorkMaterial> oldMap = new HashMap<>();
    for (WorkMaterial pr : oldList)
    {
      oldMap.put(pr.getMaterialId(), pr);
    }
    // 补充来自旧数据的字段值，新数据的字段值从页面传来的不完整
    for (WorkMaterial pr : newList)
    {
      if (oldMap.get(pr.getMaterialId()) != null)
      {
      	WorkMaterial _material = oldMap.get(pr.getMaterialId());
        pr.setCompanyId(UserUtils.getCompanyId());
        pr.setPurchQty(_material.getPurchQty());
        pr.setParentId(_material.getParentId());
        pr.setWorkBillNo(_material.getWorkBillNo());
        pr.setWorkId(_material.getWorkId());
        pr.setIsNotPurch(_material.getIsNotPurch());
        pr.setIsNotTake(_material.getIsNotTake());
        pr.setTakeQty(_material.getTakeQty());
        pr.setWorkMaterialType(workMaterialType);
      }
    }
    Map<String,List<WorkMaterial>> resultMap = ServiceHelper.filterCUD(oldList, newList);
    List<WorkMaterial> listAdd = resultMap.get(ServiceHelper.Cud.C);
    List<WorkMaterial> listUpd = resultMap.get(ServiceHelper.Cud.U);
    List<WorkMaterial> listDel = resultMap.get(ServiceHelper.Cud.D);
    // 新增的数据需填写的字段
    for (WorkMaterial material : listAdd)
    {
    	material.setCompanyId(UserUtils.getCompanyId());
    	material.setPurchQty(BigDecimal.valueOf(0));
    	material.setParentId(parentId);
    	material.setWorkBillNo(workBillNo);
    	material.setWorkId(workId);
    	material.setIsNotPurch(BoolValue.NO);
    	material.setIsNotTake(BoolValue.NO);
    	material.setTakeQty(BigDecimal.valueOf(0));
    	material.setWorkMaterialType(workMaterialType);
    }
  
    daoFactory.getCommonDao().saveAllEntity(listAdd);
    daoFactory.getCommonDao().updateAllEntity(listUpd);
    daoFactory.getCommonDao().deleteAllEntity(listDel);
	}
	
	@Override
	@Transactional
	public void updateProcedure(Work order) throws Exception
	{
		// 更新部件工序
		for (WorkPart part : order.getPartList())
		{
			// 旧数据
			List<WorkProcedure> oldList = this.getProcedureListByPartId(part.getId());
			// 新数据
			List<WorkProcedure> newList = part.getProcedureList();
			_updateProcedure(newList, oldList, part.getId(), order.getId(), order.getBillNo(), WorkProcedureType.PART);
		}
		// 更新成品工序
		// 旧数据
		List<WorkProcedure> oldList = this.getProcedureListByPackId(order.getPack().getId());
		// 新数据
		List<WorkProcedure> newList = order.getPack().getProcedureList();
		_updateProcedure(newList, oldList, order.getPack().getId(), order.getId(), order.getBillNo(), WorkProcedureType.PRODUCT);
		updateReportTask(order.getId(), BoolValue.YES, order.getPartList(), null);
	}
 
	private void _updateProcedure(List<WorkProcedure> newList, List<WorkProcedure> oldList, Long parentId, Long workId, String workBillNo,WorkProcedureType procedureType)
  {
    Map<Long,WorkProcedure> oldMap = new HashMap<>();
    for (WorkProcedure pr : oldList)
    {
      oldMap.put(pr.getProcedureId(), pr);
    }
    // 补充来自旧数据的字段值，新数据的字段值从页面传来的不完整
    for (WorkProcedure pr : newList)
    {
      if (oldMap.get(pr.getProcedureId()) != null)
      {
        WorkProcedure _procedure = oldMap.get(pr.getProcedureId());
        pr.setCompanyId(UserUtils.getCompanyId());
        pr.setOutOfQty(_procedure.getOutOfQty());
        pr.setArriveOfQty(_procedure.getArriveOfQty());
        pr.setParentId(_procedure.getParentId());
        pr.setWorkBillNo(_procedure.getWorkBillNo());
        pr.setWorkId(_procedure.getWorkId());
        pr.setIsOutSourceTemplate(_procedure.getIsOutSourceTemplate());
        pr.setWorkProcedureType(_procedure.getWorkProcedureType());
      }
    }
    Map<String,List<WorkProcedure>> resultMap = ServiceHelper.filterCUD(oldList, newList);
    List<WorkProcedure> listAdd = resultMap.get(ServiceHelper.Cud.C);
    List<WorkProcedure> listUpd = resultMap.get(ServiceHelper.Cud.U);
    List<WorkProcedure> listDel = resultMap.get(ServiceHelper.Cud.D);
    // 新增的数据需填写的字段
    for (WorkProcedure procedure : listAdd)
    {
      procedure.setCompanyId(UserUtils.getCompanyId());
      procedure.setOutOfQty(BigDecimal.valueOf(0));
      procedure.setArriveOfQty(BigDecimal.valueOf(0));
      procedure.setParentId(parentId);
      procedure.setWorkBillNo(workBillNo);
      procedure.setWorkId(workId);
      procedure.setWorkProcedureType(procedureType);
    }
  
    daoFactory.getCommonDao().saveAllEntity(listAdd);
    daoFactory.getCommonDao().updateAllEntity(listUpd);
    daoFactory.getCommonDao().deleteAllEntity(listDel);
    // 删除工序的同时删除任务列表
    if (listDel.size() > 0)
    {
    	_deleteReportTaskByList(listDel);
    }
  }
	
//	private void updateProcedureInfo(Work order)
//	{
//		// 找到发外加工单修改sourceDetailId
//		List<WorkProcedure> procedureList = this.getProcedureListByWorkId(order.getId());
//		for (WorkProcedure workProcedure : procedureList)
//		{
//			DynamicQuery processQuery = new CompanyDynamicQuery(OutSourceProcessDetail.class);
//			processQuery.eq("sourceId", order.getId());
//			processQuery.eq("procedureId", workProcedure.getProcedureId());
//			// 必须是同一个工序
//			// processQuery.eq("sourceDetailId", workProcedure.getId());
//			List<OutSourceProcessDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(processQuery, OutSourceProcessDetail.class);
//			if (CollectionUtils.isNotEmpty(detailList))
//			{
//				BigDecimal qty = new BigDecimal(0);
//				for (OutSourceProcessDetail detail : detailList)
//				{
//					detail.setSourceDetailId(workProcedure.getId());
//					daoFactory.getCommonDao().updateEntity(detail);
//					qty = qty.add(detail.getQty());
//				}
//				workProcedure.setOutOfQty(qty);
//				daoFactory.getCommonDao().updateEntity(workProcedure);
//			}
//		}
//	}

	private void updateWorkMaterial(WorkMaterial workMaterial) throws Exception
	{

		// 1.采购订单
		DynamicQuery orderDetailQuery = new CompanyDynamicQuery(PurchOrderDetail.class);
		orderDetailQuery.eq("sourceDetailId", workMaterial.getId());
		List<PurchOrderDetail> purchOrderDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(orderDetailQuery, PurchOrderDetail.class);

		if (CollectionUtils.isNotEmpty(purchOrderDetailList))
		{
			for (PurchOrderDetail purchOrderDetail : purchOrderDetailList)
			{
				PurchOrderDetail purchOrderDetail_tmp = (PurchOrderDetail) BeanUtils.cloneBean(purchOrderDetail);

				if (purchOrderDetail.getPurchUnitName().equals(purchOrderDetail.getValuationUnitName()))
				{
					purchOrderDetail.setSpecifications(workMaterial.getStyle());
					daoFactory.getCommonDao().updateEntity(purchOrderDetail);
				}
				else
				{
					purchOrderDetail.setSpecifications(workMaterial.getStyle());

					BigDecimal valuationQty = CommonHelper.getValuationQty(purchOrderDetail.getSpecifications(), purchOrderDetail.getUnitId(), purchOrderDetail.getValuationUnitId(), purchOrderDetail.getValuationUnitAccuracy(), purchOrderDetail.getWeight(), purchOrderDetail.getQty());
					purchOrderDetail.setValuationQty(valuationQty);

					BigDecimal money = valuationQty.multiply(purchOrderDetail.getValuationPrice());
					purchOrderDetail.setMoney(money);

					BigDecimal notTaxMoney = money.divide(new BigDecimal(1d + purchOrderDetail.getPercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
					BigDecimal tax = purchOrderDetail.getMoney().subtract(notTaxMoney);
					purchOrderDetail.setTax(tax);
					purchOrderDetail.setNoTaxMoney(notTaxMoney);
					purchOrderDetail.setNoTaxPrice(notTaxMoney.divide(purchOrderDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP));
					purchOrderDetail.setPrice(money.divide(purchOrderDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP));
					daoFactory.getCommonDao().updateEntity(purchOrderDetail);

					// 1.1更新采购订单主表
					PurchOrder purchOrder = daoFactory.getCommonDao().getEntity(PurchOrder.class, purchOrderDetail.getMasterId());
					if (purchOrder != null)
					{
						purchOrder.setTotalMoney(purchOrder.getTotalMoney().subtract(purchOrderDetail_tmp.getMoney()).add(purchOrderDetail.getMoney()));
						purchOrder.setNoTaxTotalMoney(purchOrder.getNoTaxTotalMoney().subtract(purchOrderDetail_tmp.getNoTaxMoney()).add(purchOrderDetail.getNoTaxMoney()));
						purchOrder.setTotalTax(purchOrder.getTotalMoney().subtract(purchOrder.getNoTaxTotalMoney()));
						daoFactory.getCommonDao().updateEntity(purchOrder);
					}
				}

				// 2.采购入库
				// DynamicQuery purchStockQuery = new CompanyDynamicQuery(PurchStockDetail.class);
				// purchStockQuery.eq("sourceDetailId", purchOrderDetail.getId());
				// List<PurchStockDetail> purchStockDetailList =
				// daoFactory.getCommonDao().findEntityByDynamicQuery(purchStockQuery, PurchStockDetail.class);
				// if(CollectionUtils.isNotEmpty(purchStockDetailList)){
				// for(PurchStockDetail purchStockDetail:purchStockDetailList){
				// PurchStockDetail purchStockDetail_tmp = (PurchStockDetail) BeanUtils.cloneBean(purchStockDetail);
				//
				// purchStockDetail.setSpecifications(workMaterial.getStyle());
				//
				// BigDecimal valuationQty2 = MaterialConversionUtils.getValuationQty(purchStockDetail.getSpecifications(),
				// purchStockDetail.getPurchUnitName(),purchStockDetail.getValuationUnitName(),
				// purchStockDetail.getValuationUnitAccuracy(), purchStockDetail.getWeight(), purchStockDetail.getQty());
				// purchStockDetail.setValuationQty(valuationQty2);
				//
				// BigDecimal money_purch = valuationQty2.multiply(purchStockDetail.getValuationPrice());
				// purchStockDetail.setMoney(money_purch);
				//
				// BigDecimal noTaxMoney = money_purch.divide(new BigDecimal(1d+purchOrderDetail.getPercent()/100d),2);
				// BigDecimal tax_purch = noTaxMoney.multiply(new BigDecimal(purchStockDetail.getPercent()/100d));
				// purchStockDetail.setTax(tax_purch);
				// purchStockDetail.setNoTaxMoney(noTaxMoney);
				// purchStockDetail.setPrice(money_purch.divide(purchStockDetail.getQty(), 4));
				// purchStockDetail.setNoTaxPrice(noTaxMoney.divide(purchStockDetail.getQty(), 4));
				// daoFactory.getCommonDao().updateEntity(purchStockDetail);
				//
				// //2.1更新采购入库主表
				// PurchStock purchStock_tmp = daoFactory.getCommonDao().getEntity(PurchStock.class,
				// purchStockDetail.getMasterId());
				// if(purchStock_tmp != null){
				// purchStock_tmp.setTotalMoney(purchStock_tmp.getTotalMoney().subtract(purchStockDetail_tmp.getMoney()).add(purchStockDetail.getMoney()));
				// purchStock_tmp.setNoTaxTotalMoney(purchStock_tmp.getNoTaxTotalMoney().subtract(purchStockDetail_tmp.getNoTaxMoney()).add(purchStockDetail.getNoTaxMoney()));
				// purchStock_tmp.setTotalTax(purchStock_tmp.getTotalTax().subtract(purchStockDetail_tmp.getTax()).add(purchStockDetail.getTax()));
				// daoFactory.getCommonDao().updateEntity(purchStock_tmp);
				// }
				//
				// //3.采购退货
				// DynamicQuery purchRefundQuery = new CompanyDynamicQuery(PurchRefundDetail.class);
				// purchRefundQuery.eq("sourceDetailId", purchStockDetail.getId());
				// List<PurchRefundDetail> purchRefundDetailList =
				// daoFactory.getCommonDao().findEntityByDynamicQuery(purchRefundQuery,PurchRefundDetail.class);
				//
				// BigDecimal refund_qty = new BigDecimal(0);
				// BigDecimal refund_valuationQty = new BigDecimal(0);
				// if(CollectionUtils.isNotEmpty(purchRefundDetailList))
				// {
				// for(PurchRefundDetail purchRefundDetail:purchRefundDetailList){
				// PurchRefundDetail purchRefundDetail_tmp = (PurchRefundDetail) BeanUtils.cloneBean(purchRefundDetail);
				//
				//
				// purchRefundDetail.setSpecifications(workMaterial.getStyle());
				//
				// BigDecimal valuationQty3 = MaterialConversionUtils.getValuationQty(purchRefundDetail.getSpecifications(),
				// purchRefundDetail.getPurchUnitName(),purchRefundDetail.getValuationUnitName(),
				// purchRefundDetail.getValuationUnitAccuracy(), purchRefundDetail.getWeight(), purchRefundDetail.getQty());
				// purchRefundDetail.setValuationQty(valuationQty3);
				//
				// BigDecimal money3 = valuationQty3.multiply(purchRefundDetail.getValuationPrice());
				// purchRefundDetail.setMoney(money3);
				//
				// BigDecimal noTaxMoney3 = money3.divide(new BigDecimal(1d+purchRefundDetail.getPercent()/100d),2);
				// BigDecimal tax3 = noTaxMoney3.multiply(new BigDecimal(purchRefundDetail.getPercent()/100d));
				// purchRefundDetail.setTax(tax3);
				// purchRefundDetail.setNoTaxMoney(noTaxMoney3);
				// purchRefundDetail.setPrice(money3.divide(purchRefundDetail.getQty(), 4));
				// purchRefundDetail.setNoTaxPrice(noTaxMoney3.divide(purchRefundDetail.getQty(), 4));
				// daoFactory.getCommonDao().updateEntity(purchRefundDetail);
				//
				// //3.1更新采购退货主表
				// PurchRefund purchRefund = daoFactory.getCommonDao().getEntity(PurchRefund.class,
				// purchRefundDetail.getMasterId());
				// if(purchRefund != null){
				//
				// purchRefund.setTotalMoney(purchRefund.getTotalMoney().subtract(purchRefundDetail_tmp.getMoney()).add(purchRefundDetail.getMoney()));
				// purchRefund.setNoTaxTotalMoney(purchRefund.getNoTaxTotalMoney().subtract(purchRefundDetail_tmp.getNoTaxMoney()).add(purchRefundDetail.getNoTaxMoney()));
				// purchRefund.setTotalTax(purchRefund.getTotalTax().subtract(purchRefundDetail_tmp.getTax()).add(purchRefundDetail.getTax()));
				// daoFactory.getCommonDao().updateEntity(purchRefund);
				//
				// if(purchRefund.getIsCheck() == BoolValue.YES){
				// refund_qty = refund_qty.add(purchRefundDetail_tmp.getQty());
				// refund_valuationQty = refund_valuationQty.add(purchRefundDetail_tmp.getValuationQty());
				// }
				// }
				//
				// //采购对账-退货
				// DynamicQuery purchReconcilQuery2 = new CompanyDynamicQuery(PurchReconcilDetail.class);
				// purchReconcilQuery2.eq("sourceDetailId", purchRefundDetail.getId());
				// PurchReconcilDetail purchReconcilDetail2 =
				// daoFactory.getCommonDao().getByDynamicQuery(purchReconcilQuery2,PurchReconcilDetail.class);
				// if(purchReconcilDetail2 != null)
				// {
				// PurchReconcilDetail purchReconcilDetail_tmp = (PurchReconcilDetail)
				// BeanUtils.cloneBean(purchReconcilDetail2);
				// purchReconcilDetail2.setSpecifications(workMaterial.getStyle());
				//
				// BigDecimal valuationQty4 = MaterialConversionUtils.getValuationQty(purchReconcilDetail2.getSpecifications(),
				// purchReconcilDetail2.getPurchUnitName(),purchReconcilDetail2.getValuationUnitName(),
				// purchReconcilDetail2.getValuationUnitAccuracy(), purchReconcilDetail2.getWeight(),
				// purchReconcilDetail2.getQty());
				// purchReconcilDetail2.setValuationQty(valuationQty4);
				//
				// BigDecimal money4 = valuationQty4.multiply(purchReconcilDetail2.getValuationPrice());
				// purchReconcilDetail2.setMoney(money4);
				//
				// BigDecimal noTaxMoney4 = money4.divide(new BigDecimal(1d+purchReconcilDetail2.getPercent()/100d),2);
				// BigDecimal tax4 = noTaxMoney4.multiply(new BigDecimal(purchReconcilDetail2.getPercent()/100d));
				// purchReconcilDetail2.setTax(tax4);
				// purchReconcilDetail2.setNoTaxMoney(noTaxMoney4);
				// purchReconcilDetail2.setPrice(money4.divide(purchReconcilDetail2.getQty(), 4));
				// purchReconcilDetail2.setNoTaxPrice(noTaxMoney4.divide(purchReconcilDetail2.getQty(), 4));
				// daoFactory.getCommonDao().updateEntity(purchReconcilDetail2);
				//
				// //4.1更新采购对账主表
				// PurchReconcil purchReconcil = daoFactory.getCommonDao().getEntity(PurchReconcil.class,
				// purchReconcilDetail2.getMasterId());
				// if(purchReconcil != null){
				// purchReconcil.setTotalMoney(purchReconcil.getTotalMoney().subtract(purchReconcilDetail_tmp.getMoney()).add(purchReconcilDetail2.getMoney()));
				// purchReconcil.setNoTaxTotalMoney(purchReconcil.getNoTaxTotalMoney().subtract(purchReconcilDetail_tmp.getNoTaxMoney()).add(purchReconcilDetail2.getNoTaxMoney()));
				// purchReconcil.setTotalTax(purchReconcil.getTotalTax().subtract(purchReconcilDetail_tmp.getTax()).add(purchReconcilDetail2.getTax()));
				// daoFactory.getCommonDao().updateEntity(purchReconcil);
				// }
				// }
				//
				// }
				//
				// //4.采购对账
				// DynamicQuery purchReconcilQuery = new CompanyDynamicQuery(PurchReconcilDetail.class);
				// purchReconcilQuery.eq("sourceDetailId", purchStockDetail.getId());
				// List<PurchReconcilDetail> purchReconcilDetailList =
				// daoFactory.getCommonDao().findEntityByDynamicQuery(purchReconcilQuery,PurchReconcilDetail.class);
				// if(CollectionUtils.isNotEmpty(purchReconcilDetailList))
				// {
				// for(PurchReconcilDetail purchReconcilDetail:purchReconcilDetailList){
				// PurchReconcilDetail purchReconcilDetail_tmp = new PurchReconcilDetail();
				// purchReconcilDetail_tmp.setMoney(purchReconcilDetail.getMoney());
				// purchReconcilDetail_tmp.setNoTaxMoney(purchReconcilDetail.getNoTaxMoney());
				// purchReconcilDetail_tmp.setTax(purchReconcilDetail.getTax());
				//
				// purchReconcilDetail.setSpecifications(workMaterial.getStyle());
				//
				// BigDecimal valuationQty4 = MaterialConversionUtils.getValuationQty(purchReconcilDetail.getSpecifications(),
				// purchReconcilDetail.getPurchUnitName(),purchReconcilDetail.getValuationUnitName(),
				// purchReconcilDetail.getValuationUnitAccuracy(), purchReconcilDetail.getWeight(),
				// purchReconcilDetail.getQty());
				// purchReconcilDetail.setValuationQty(valuationQty4);
				//
				// BigDecimal money4 = valuationQty4.multiply(purchReconcilDetail.getValuationPrice());
				// purchReconcilDetail.setMoney(money4);
				//
				// BigDecimal noTaxMoney4 = money4.divide(new BigDecimal(1d+purchReconcilDetail.getPercent()/100d),2);
				// BigDecimal tax4 = noTaxMoney4.multiply(new BigDecimal(purchReconcilDetail.getPercent()/100d));
				// purchReconcilDetail.setTax(tax4);
				// purchReconcilDetail.setNoTaxMoney(noTaxMoney4);
				// purchReconcilDetail.setPrice(money4.divide(purchReconcilDetail.getQty(), 4));
				// purchReconcilDetail.setNoTaxPrice(noTaxMoney4.divide(purchReconcilDetail.getQty(), 4));
				// daoFactory.getCommonDao().updateEntity(purchReconcilDetail);
				//
				// //4.1更新采购对账主表
				// PurchReconcil purchReconcil = daoFactory.getCommonDao().getEntity(PurchReconcil.class,
				// purchReconcilDetail.getMasterId());
				// if(purchReconcil != null){
				// purchReconcil.setTotalMoney(purchReconcil.getTotalMoney().subtract(purchReconcilDetail_tmp.getMoney()).add(purchReconcilDetail.getMoney()));
				// purchReconcil.setNoTaxTotalMoney(purchReconcil.getNoTaxTotalMoney().subtract(purchReconcilDetail_tmp.getNoTaxMoney()).add(purchReconcilDetail.getNoTaxMoney()));
				// purchReconcil.setTotalTax(purchReconcil.getTotalTax().subtract(purchReconcilDetail_tmp.getTax()).add(purchReconcilDetail.getTax()));
				// daoFactory.getCommonDao().updateEntity(purchReconcil);
				// }
				// }
				// }
				// }
				//
				// //5.材料库存管理 先删除之前的入库记录 在增加新的入库记录
				// if(UserUtils.hasCompanyPermission("stock:material:list")){
				// //1.先删除之前的入库记录
				// DynamicQuery smlq = new CompanyDynamicQuery(StockMaterialLog.class);
				// smlq.eq("billId", purchStockDetail.getMasterId());
				// smlq.eq("code",purchStockDetail.getCode());
				// StockMaterialLog stockMaterialLog = daoFactory.getCommonDao().getByDynamicQuery(smlq,
				// StockMaterialLog.class);
				// daoFactory.getCommonDao().deleteEntity(stockMaterialLog);
				//
				// //先减去之前的材料库存
				// DynamicQuery smQuery = new CompanyDynamicQuery(StockMaterial.class);
				// smQuery.eq("materialId", purchStockDetail.getMaterialId());
				// smQuery.eq("specifications", purchStockDetail_tmp.getSpecifications());
				// smQuery.eq("warehouseId", purchStockDetail_tmp.getWarehouseId());
				// StockMaterial oldStockMaterial = daoFactory.getCommonDao().getByDynamicQuery(smQuery, StockMaterial.class);
				// if(oldStockMaterial != null){
				// oldStockMaterial.setUpdateTime(new Date());
				// oldStockMaterial.setQty(oldStockMaterial.getQty().subtract(purchStockDetail_tmp.getQty()).add(refund_qty));
				// oldStockMaterial.setValuationQty(oldStockMaterial.getValuationQty().subtract(purchStockDetail_tmp.getValuationQty()).add(refund_valuationQty));
				// BigDecimal moneyStock = oldStockMaterial.getMoney();// 入库前库存总金额
				// BigDecimal moneyPurch = purchStockDetail_tmp.getMoney();// 上一次入库总金额
				//
				// // 算实时计价单价 计价数量不为0才计算
				// if (oldStockMaterial.getValuationQty().doubleValue() > 0)
				// {
				// oldStockMaterial.setValuationPrice(moneyStock.subtract(moneyPurch)
				// .divide(oldStockMaterial.getValuationQty(), 4, BigDecimal.ROUND_HALF_UP));
				// // 库存单价
				// oldStockMaterial.setPrice(moneyStock.subtract(moneyPurch).divide(oldStockMaterial.getQty(), 4,
				// BigDecimal.ROUND_HALF_UP));
				// // 算金额
				// oldStockMaterial.setMoney(
				// oldStockMaterial.getValuationPrice().multiply(oldStockMaterial.getValuationQty()));
				// }
				// else
				// {
				// oldStockMaterial.setMoney(new BigDecimal(0));
				// }
				// daoFactory.getCommonDao().updateEntity(oldStockMaterial);
				// }
				//
				//
				// //在增加新规格的库存
				// DynamicQuery query = new CompanyDynamicQuery(PurchStock.class);
				// query.eq("id", purchStockDetail.getMasterId());
				// PurchStock purchStock = daoFactory.getCommonDao().getByDynamicQuery(query, PurchStock.class);
				//
				// StockMaterial stockMaterial = new StockMaterial();
				// // 库存操作
				// stockMaterial.setQty(purchStockDetail.getQty().subtract(refund_qty));
				// stockMaterial.setValuationQty(purchStockDetail.getValuationQty().subtract(refund_valuationQty));
				//
				// if(stockMaterial.getValuationQty().doubleValue() >0){
				// stockMaterial.setMoney(purchStockDetail.getMoney());
				// stockMaterial.setPrice(purchStockDetail.getPrice());
				// stockMaterial.setValuationPrice(purchStockDetail.getValuationPrice());
				// }else{
				// stockMaterial.setMoney(new BigDecimal(0));
				// }
				//
				//
				// stockMaterial.setMaterialId(purchStockDetail.getMaterialId());
				// stockMaterial.setSpecifications(purchStockDetail.getSpecifications());
				// stockMaterial.setMaterialClassId(purchStockDetail.getMaterialClassId());
				// stockMaterial.setWarehouseId(purchStockDetail.getWarehouseId());
				// serviceFactory.getMaterialStockService().stock(stockMaterial);
				//
				// // 材料最近采购价操作
				// Material material = serviceFactory.getMaterialService().get(purchStockDetail.getMaterialId());
				// material.setLastPurchPrice(purchStockDetail.getValuationPrice());
				// daoFactory.getCommonDao().updateEntity(material);
				//
				// // 入库记录
				// StockMaterialLog log = new StockMaterialLog();
				// log.setBillId(purchStockDetail.getMasterId());
				// log.setBillType(purchStock.getBillType());
				// log.setBillNo(purchStock.getBillNo());
				// log.setCreateTime(new Date());
				// log.setCompanyId(UserUtils.getCompanyId());
				// log.setCode(purchStockDetail.getCode());
				// log.setMaterialClassId(purchStockDetail.getMaterialClassId());
				// log.setMaterialName(purchStockDetail.getMaterialName());
				// log.setSpecifications(purchStockDetail.getSpecifications());
				// log.setWarehouseId(purchStockDetail.getWarehouseId());
				// log.setUnitId(purchStockDetail.getUnitId());
				// log.setWeight(purchStockDetail.getWeight());
				// log.setPrice(purchStockDetail.getPrice());
				// log.setInQty(purchStockDetail.getQty());
				// log.setInMoney(purchStockDetail.getMoney());
				// daoFactory.getCommonDao().saveEntity(log);
				// }
				// }
				//
				//
				//
				//
				// }
			}
		}

		// 生产领料修改
		DynamicQuery takeDetailQuery = new CompanyDynamicQuery(StockMaterialTakeDetail.class);
		takeDetailQuery.eq("sourceDetailId", workMaterial.getId());
		List<StockMaterialTakeDetail> takeDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(takeDetailQuery, StockMaterialTakeDetail.class);
		if (CollectionUtils.isNotEmpty(takeDetailList))
		{
			for (StockMaterialTakeDetail takeDetail : takeDetailList)
			{
				takeDetail.setSpecifications(workMaterial.getStyle());

				BigDecimal valuationQty = CommonHelper.getValuationQty(takeDetail.getSpecifications(), takeDetail.getStockUnitId(), takeDetail.getValuationUnitId(), takeDetail.getValuationUnitAccuracy(), takeDetail.getWeight(), takeDetail.getQty());
				takeDetail.setValuationQty(valuationQty);

				BigDecimal money = valuationQty.multiply(takeDetail.getValuationPrice());
				takeDetail.setMoney(money);
				daoFactory.getCommonDao().updateEntity(takeDetail);
			}

		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> findProcedureState(Long procedureId, String billNo, Long procedureRefId, String partName)
	{
		// 0未被任何订单引用 1被发外加工单引用 2被产量上报引用
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", "0");

		DynamicQuery processQuery = new CompanyDynamicQuery(OutSourceProcessDetail.class);
		processQuery.eq("procedureId", procedureId);
		processQuery.eq("workBillNo", billNo);
		if (StringUtils.isNotBlank(partName))
		{
			processQuery.eq("partName", partName);
		}

		List<OutSourceProcessDetail> outSourceProcessDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(processQuery, OutSourceProcessDetail.class);
		if (CollectionUtils.isNotEmpty(outSourceProcessDetailList))
		{
			List list = new ArrayList();
			for (OutSourceProcessDetail detatil : outSourceProcessDetailList)
			{
				OutSourceProcess outSourceProcess = daoFactory.getCommonDao().getEntity(OutSourceProcess.class, detatil.getMasterId());
				list.add(outSourceProcess);
			}
			map.put("state", "1");
			map.put("OPBillNo", list);
		}

		// 产量日报是否被引用
		DynamicQuery taskQuery = new CompanyDynamicQuery(WorkReportTask.class);
		taskQuery.eq("billNo", billNo);
		if (StringUtils.isNotBlank(partName))
		{
			taskQuery.eq("procedureRefId", procedureRefId);
		}
		else
		{
			taskQuery.eq("procedureId", procedureId);
		}
		taskQuery.eq("isShow", BoolValue.YES);
		WorkReportTask workReportTask = daoFactory.getCommonDao().getByDynamicQuery(taskQuery, WorkReportTask.class);
		if (workReportTask != null)
		{
			DynamicQuery reportQuery = new DynamicQuery(WorkReport.class, "rp");
			reportQuery.addProjection(Projections.property("rp"));
			reportQuery.createAlias(WorkReportDetail.class, JoinType.LEFTJOIN, "dt", "rp.id=dt.masterId");
			reportQuery.eq("dt.companyId", UserUtils.getCompanyId());
			reportQuery.eq("dt.taskId", workReportTask.getId());

			List<WorkReport> list = daoFactory.getCommonDao().findEntityByDynamicQuery(reportQuery, WorkReport.class);
			if (CollectionUtils.isNotEmpty(list))
			{
				map.put("state", "2");
				map.put("DYBillNo", list);
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, List> findProductTask(Long id)

	{
		Map<String, List> map = new HashMap<String, List>();
		// 根据工单id查找产量上报任务表的taskId
		DynamicQuery WorkQuery = new CompanyDynamicQuery(Work.class);
		WorkQuery.eq("id", id);
		Work work = daoFactory.getCommonDao().getByDynamicQuery(WorkQuery, Work.class);

		DynamicQuery query = new CompanyDynamicQuery(WorkReportDetail.class, "a");
		query.createAlias(WorkReportTask.class, "b");
		query.createAlias(WorkReport.class, "c");
		query.addProjection(Projections.property("a, b,c"));
		query.eqProperty("a.taskId", "b.id");
		query.eqProperty("a.masterId", "c.id");
		query.eq("b.billNo", work.getBillNo());

		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		List<WorkReport> newList = new ArrayList<WorkReport>();
		for (Object[] c : temp_result.getResult())
		{
			List<WorkReport> list = new ArrayList<WorkReport>();
			WorkReport report = (WorkReport) c[2];
			list.add(report);
			// 去重复单号
			for (Object obj : list)
			{
				List<Boolean> repetList = new ArrayList<>();
				WorkReport n = (WorkReport) obj;
				for (WorkReport _n : newList)
				{
					if (_n.getBillNo().equals(n.getBillNo()))
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
			map.put(BillType.PRODUCE_REPORT.getCode(), newList);
		}

		/*
		 * DynamicQuery WorkReportTaskQuery = new CompanyDynamicQuery(WorkReportTask.class);
		 * WorkReportTaskQuery.eq("billNo", work.getBillNo()); WorkReportTaskQuery.eq("companyId", work.getCompanyId());
		 * List<WorkReportTask> taskList = daoFactory.getCommonDao().findEntityByDynamicQuery(WorkReportTaskQuery,
		 * WorkReportTask.class); // 连表查询 WorkReportDetail + WorkReport List<WorkReport> list = new ArrayList<WorkReport>();
		 * for(WorkReportTask wode : taskList){ // 查询出每个上报任务的taskID DynamicQuery WorkDetail = new
		 * CompanyDynamicQuery(WorkReportDetail.class,"detail"); WorkDetail.addProjection(Projections.property("master"));
		 * WorkDetail.createAlias(WorkReport.class, "master"); WorkDetail.eqProperty("detail.masterId", "master.id");
		 * WorkDetail.eq("detail.taskId", wode.getId()); WorkReport workReport =
		 * daoFactory.getCommonDao().getByDynamicQuery(WorkDetail, WorkReport.class); if(workReport!=null){ // 去除重复单号
		 * List<Boolean> booleanList = new ArrayList<>(); for(WorkReport wr : list){
		 * if(wr.getBillNo().equals(workReport.getBillNo())){ booleanList.add(true); } } if(!booleanList.contains(true)){
		 * list.add(workReport); } } } if(CollectionUtils.isNotEmpty(list)){ map.put(BillType.PRODUCE_REPORT.getCode(),
		 * list); }
		 */
		return map;
	}

	@Override
	public Map<String, Object> findPurchStockLog(String workBillNo)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		DynamicQuery query = new CompanyDynamicQuery(PurchStockDetail.class, "detail");
		query.eq("detail.workBillNo", workBillNo);
		query.addProjection(Projections.property("master"));
		query.createAlias(PurchStock.class, JoinType.LEFTJOIN, "master", "master.id=detail.masterId");
		query.addGourp("master.billNo");
		List<PurchStock> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchStock.class);
		if (CollectionUtils.isNotEmpty(list))
		{
			map.put("PN", list);
		}

		DynamicQuery takeQuery = new CompanyDynamicQuery(StockMaterialTakeDetail.class, "detail");
		takeQuery.eq("detail.sourceBillNo", workBillNo);
		takeQuery.addProjection(Projections.property("master"));
		takeQuery.createAlias(StockMaterialTake.class, JoinType.LEFTJOIN, "master", "master.id=detail.masterId");
		takeQuery.addGourp("master.billNo");
		List<StockMaterialTake> takeList = daoFactory.getCommonDao().findEntityByDynamicQuery(takeQuery, StockMaterialTake.class);
		if (CollectionUtils.isNotEmpty(takeList))
		{
			map.put("MR", takeList);
		}
		return map;
	}

	// ==================== 新规范 - 代码重构 ====================

	@Override
	public Integer countInStockQty(long sourceDetailId)
	{
		Integer sum = 0;
		DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class, "a");
		query.createAlias(Work.class, "b");
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", sourceDetailId);
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<WorkProduct> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, WorkProduct.class);
		for (WorkProduct workProduct : result.getResult())
		{
			sum += workProduct.getInStockQty();
		}
		return sum;
	}

	@Override
	public SearchResult<WorkProduct> findAllProduct(BoolValue isCheck)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class, "a");
		query.createAlias(Work.class, "b");
		query.eqProperty("a.masterId", "b.id");
		query.eq("b.isCheck", isCheck);
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, WorkProduct.class);
	}

	@Override
	public SearchResult<WorkProduct> findAllProduct()
	{
		return findAllProduct(BoolValue.YES);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<Long, List<WorkProduct>> findAllProductForMap()
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class, "a");
		query.createAlias(Work.class, "b");
		query.eqProperty("a.masterId", "b.id");
		query.addProjection(Projections.property("a.masterId, a.productName,a.productId, b.id"));
		query.eq("b.isCheck", BoolValue.YES);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		
		Map<Long, List<WorkProduct>> map = Maps.newHashMap();
		
		for(HashMap p : result.getResult())
		{
			try
			{
				WorkProduct workProduct = ObjectHelper.mapToObject(p, WorkProduct.class);
				long workId = workProduct.getMasterId();
				List<WorkProduct> list = map.get(workId);
				if (null == list)
				{
					list = Lists.newArrayList();
					map.put(workId, list);
				}
				list.add(workProduct);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return map;
	}

	@Override
	public void setProceduresMaterials(OutSourceProcessDetail detail)
	{
		StringBuilder workProcedures = new StringBuilder();	// 工艺信息
		StringBuilder workMaterials = new StringBuilder();	// 材料信息
		Long id = detail.getWorkId();
		
		DynamicQuery query_part = new CompanyDynamicQuery(WorkPart.class);
		query_part.eq("masterId", id);
		List<WorkPart> partList = daoFactory.getCommonDao().findEntityByDynamicQuery(query_part, WorkPart.class);
		for (WorkPart part : partList)
		{
			DynamicQuery query_procedure = new CompanyDynamicQuery(WorkProcedure.class);
			query_procedure.eq("workProcedureType", WorkProcedureType.PART);
			query_procedure.eq("parentId", part.getId());
			List<WorkProcedure> procedure_list = daoFactory.getCommonDao().findEntityByDynamicQuery(query_procedure, WorkProcedure.class);
			if (procedure_list.size() > 0)
			{
				workProcedures.append(part.getPartName()).append("：");
				int i = 1, len = procedure_list.size();
				for (WorkProcedure workProcedure : procedure_list)
				{
					workProcedures.append(workProcedure.getProcedureName());
					if (i != len)
					{
						workProcedures.append("，");
					}
					i++;
				}

				workProcedures.append("；");
			}
			
			DynamicQuery query_material = new CompanyDynamicQuery(WorkMaterial.class);
			query_material.eq("workMaterialType", WorkMaterialType.PART);
			query_material.eq("parentId", part.getId());
			List<WorkMaterial> material_list = daoFactory.getCommonDao().findEntityByDynamicQuery(query_material, WorkMaterial.class);
			if (material_list.size() > 0)
			{
				workMaterials.append(part.getPartName()).append("：");
				int i = 1, len = material_list.size();
				for (WorkMaterial workMaterial : material_list)
				{
					workMaterials.append(workMaterial.getMaterialName());
					if (i != len)
					{
						workMaterials.append("，");
					}
					i++;
				}

				workMaterials.append("；");
			}
		}
		
		
		DynamicQuery query_pack = new CompanyDynamicQuery(WorkPack.class);
		query_pack.eq("masterId", id);
		WorkPack pack = daoFactory.getCommonDao().getByDynamicQuery(query_pack, WorkPack.class);
		
		// 材料
		DynamicQuery query_material = new CompanyDynamicQuery(WorkMaterial.class);
		query_material.eq("workMaterialType", WorkMaterialType.PRODUCT);
		query_material.eq("parentId", pack.getId());
		List<WorkMaterial> material_list = daoFactory.getCommonDao().findEntityByDynamicQuery(query_material, WorkMaterial.class);
		
		// 产品
		DynamicQuery query_procedure = new CompanyDynamicQuery(WorkProcedure.class);
		query_procedure.eq("workProcedureType", WorkProcedureType.PRODUCT);
		query_procedure.eq("parentId", pack.getId());
		List<WorkProcedure> procedure_list = daoFactory.getCommonDao().findEntityByDynamicQuery(query_procedure, WorkProcedure.class);
		
		if (null != pack && (procedure_list.size() > 0 || material_list.size() > 0))
		{
			if (procedure_list.size() > 0)
			{
				workProcedures.append("成品").append("：");
				int i = 1, len = procedure_list.size();
				for (WorkProcedure workProcedure : procedure_list)
				{
					workProcedures.append(workProcedure.getProcedureName());
					if (i != len)
					{
						workProcedures.append("，");
					}
					i++;
				}

				workProcedures.append("；");
			}

			if (material_list.size() > 0)
			{
				workMaterials.append("成品").append("：");
				int i = 1, len = material_list.size();
				for (WorkMaterial workMaterial : material_list)
				{
					workMaterials.append(workMaterial.getMaterialName());
					if (i != len)
					{
						workMaterials.append("，");
					}
					i++;
				}

				workMaterials.append("；");
			}
		}
		detail.setWorkProcedures(StringUtils.removeEnd(workProcedures.toString(), "；"));
		detail.setWorkMaterials(StringUtils.removeEnd(workMaterials.toString(), "；"));
	}
}
