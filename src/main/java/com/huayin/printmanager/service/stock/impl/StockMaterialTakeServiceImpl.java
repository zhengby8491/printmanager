/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.stock.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTake;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTakeDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockMaterialTakeService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 生产领料
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockMaterialTakeServiceImpl extends BaseServiceImpl implements StockMaterialTakeService
{

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialTakeService#findByCondition(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialTake> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialTake.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("takeTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("takeTime", queryParam.getDateMax());
		}
		if (null != queryParam.getSendEmployeeId())
		{
			query.eq("sendEmployeeId", queryParam.getSendEmployeeId());
		}
		if (null != queryParam.getWarehouseId())
		{
			query.eq("warehouseId", queryParam.getWarehouseId());
		}
		if (null != queryParam.getReceiveEmployeeId())
		{
			query.eq("receiveEmployeeId", queryParam.getReceiveEmployeeId());
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockMaterialTake.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialTakeService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialTakeDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialTakeDetail.class, "ad");
		query.createAlias(StockMaterialTake.class, "a");
		query.addProjection(Projections.property("ad, a"));
		query.eqProperty("ad.masterId", "a.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.takeTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.takeTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getMaterialName()))
		{
			query.like("ad.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("ad.customerName", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSpecifications()))
		{
			query.like("ad.specifications", "%" + queryParam.getSpecifications() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("a.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getWarehouseId() != null && queryParam.getWarehouseId() != -1)
		{
			query.eq("a.warehouseId", queryParam.getWarehouseId());
		}
		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("ad.sourceBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("ad.productName", "%" + queryParam.getProductName() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("a.isCheck", queryParam.getAuditFlag());
		}
		query.eq("ad.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("a.createTime");
		query.setIsSearchTotalCount(true);

		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<StockMaterialTakeDetail> result = new SearchResult<StockMaterialTakeDetail>();
		result.setResult(new ArrayList<StockMaterialTakeDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockMaterialTakeDetail detail = (StockMaterialTakeDetail) c[0];
			detail.setMaster((StockMaterialTake) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialTakeService#findForTransmitTake(com.huayin.printmanager.service.
	 * vo.QueryParam)
	 */
	@Override
	public SearchResult<WorkMaterial> findForTransmitTake(QueryParam queryParam)
	{
		try
		{
			SearchResult<WorkMaterial> result = new SearchResult<WorkMaterial>();
			DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class, "wm");
			query.addProjection(Projections.property("wm, w"));
			query.createAlias(Work.class, "w");
			query.eqProperty("wm.workId", "w.id");

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
			if (StringUtils.isNotBlank(queryParam.getMaterialName()))
			{
				query.like("wm.materialName", "%" + queryParam.getMaterialName() + "%");
			}

			if (queryParam.getCompleteFlag() == BoolValue.NO)
			{// 搜索非强制完工
				query.eq("wm.isNotTake", BoolValue.NO);
				query.eq("w.isForceComplete", BoolValue.NO);
			}
			else
			{// 搜索已强制完工
				query.eq("wm.isNotTake", BoolValue.YES);
			}

			query.add(Restrictions.gtProperty("wm.qty", "wm.takeQty"));
			query.eq("wm.isCustPaper", BoolValue.NO);
			query.eq("w.isCheck", BoolValue.YES);
			query.setPageIndex(queryParam.getPageNumber());
			query.setPageSize(queryParam.getPageSize());
			query.eq("wm.companyId", UserUtils.getCompanyId());
			query.desc("w.createTime");
			query.setIsSearchTotalCount(true);

			SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
			result.setResult(new ArrayList<WorkMaterial>());
			for (Object[] c : temp_result.getResult())
			{
				WorkMaterial workMaterial = (WorkMaterial) c[0];
				workMaterial.setWork((Work) c[1]);
				result.getResult().add(workMaterial);
			}
			result.setCount(temp_result.getCount());
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialTakeService#toTake(java.lang.Long[])
	 */
	@Override
	public List<StockMaterialTakeDetail> toTake(Long[] ids)
	{
		List<StockMaterialTakeDetail> result = new ArrayList<StockMaterialTakeDetail>();
		DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class);
		query.in("id", Arrays.asList(ids));
		List<WorkMaterial> workMaterialList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkMaterial.class);
		// 查询所有工单WorkProduct，并设置到材料中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (WorkMaterial workMaterial : workMaterialList)
		{
			// System.out.println("需要量：" + workMaterial.getQty() + ",已领料：" + workMaterial.getTakeQty());
			if (!(workMaterial.getQty().compareTo(workMaterial.getTakeQty()) == 1))
			{
				continue;
			}
			long workId = workMaterial.getWorkId();
			List<WorkProduct> list = productMap.get(workId);
			if (null != list)
			{
				workMaterial.setProductList(list);
			}
			StockMaterialTakeDetail detail = new StockMaterialTakeDetail();
			// 获取材料对象
			Material material = serviceFactory.getMaterialService().get(workMaterial.getMaterialId());
			// 获取工单对象
			Work work = daoFactory.getCommonDao().getEntity(Work.class, workMaterial.getWorkId());
			detail.setCode(material.getCode());
			detail.setMaterialName(material.getName());
			detail.setWeight(material.getWeight());
			detail.setMaterialClassId(material.getMaterialClassId());
			detail.setSpecifications(workMaterial.getStyle());
			detail.setMaterialId(workMaterial.getMaterialId());
			detail.setStockUnitId(material.getStockUnitId());
			detail.setStockUnitName(((Unit) UserUtils.getBasicInfo("UNIT", material.getStockUnitId())).getName());
			detail.setValuationUnitId(material.getValuationUnitId());
			detail.setValuationUnitName(((Unit) UserUtils.getBasicInfo("UNIT", material.getValuationUnitId())).getName());
			detail.setSourceDetailId(workMaterial.getId());
			detail.setSourceId(work.getId());
			detail.setSourceQty(workMaterial.getQty());
			detail.setQty(workMaterial.getQty().subtract(workMaterial.getTakeQty()));
			detail.setSourceBillNo(work.getBillNo());
			detail.setSourceBillType(work.getBillType());
			detail.setPrice(getStockPrice(material.getId(), workMaterial.getStyle()));
			detail.setProductName(workMaterial.getProductNames());
			if (StringUtils.isNotBlank(workMaterial.getProductIds()))
			{
				String[] idList = workMaterial.getProductIds().split(",");
				StringBuilder sb = new StringBuilder();
				// 为全局变更名称做的特殊处理 
				for (String id : idList)
				{
					sb.append("B" + id + "E,");
				}
				detail.setProductId(sb.toString());
			}
			result.add(detail);
		}
		return result;

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialTakeService#get(java.lang.Long)
	 */
	@Override
	public StockMaterialTake get(Long id)
	{
		StockMaterialTake stockMaterialTake = daoFactory.getCommonDao().getEntity(StockMaterialTake.class, id);

		DynamicQuery queryD = new CompanyDynamicQuery(StockMaterialTakeDetail.class);
		queryD.eq("masterId", id);
		List<StockMaterialTakeDetail> stockMaterialTakeDetail = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, StockMaterialTakeDetail.class);
		if (stockMaterialTakeDetail != null)
		{
			stockMaterialTake.setDetailList(stockMaterialTakeDetail);
		}
		return stockMaterialTake;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialTakeService#getDetail(java.lang.Long)
	 */
	public StockMaterialTakeDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialTakeDetail.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialTakeDetail.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialTakeService#save(com.huayin.printmanager.persist.entity.stock.
	 * StockMaterialTake)
	 */
	@Override
	@Transactional
	public ServiceResult<StockMaterialTake> save(StockMaterialTake stockMaterialTake)
	{
		// 是否要审核标识
		BoolValue flag = stockMaterialTake.getIsCheck();
		stockMaterialTake.setBillType(BillType.STOCK_MR);
		stockMaterialTake.setBillNo(UserUtils.createBillNo(BillType.STOCK_MR));
		stockMaterialTake.setCompanyId(UserUtils.getCompanyId());
		stockMaterialTake.setCreateTime(new Date());
		stockMaterialTake.setIsCheck(BoolValue.NO);
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			stockMaterialTake.setCreateName(e.getName());
		}
		else
		{
			stockMaterialTake.setCreateName(UserUtils.getUserName());
		}
		stockMaterialTake.setCreateEmployeeId(UserUtils.getEmployeeId());
		if (stockMaterialTake.getReceiveEmployeeId() != null)
		{
			stockMaterialTake.setReceiveEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialTake.getReceiveEmployeeId())).getName());
		}
		if (stockMaterialTake.getSendEmployeeId() != null)
		{
			stockMaterialTake.setSendEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialTake.getSendEmployeeId())).getName());
		}
		stockMaterialTake.setWarehouseName(((Warehouse) UserUtils.getBasicInfo("WAREHOUSE", stockMaterialTake.getWarehouseId())).getName());

		daoFactory.getCommonDao().saveEntity(stockMaterialTake);
		for (StockMaterialTakeDetail detail : stockMaterialTake.getDetailList())
		{
			detail.setMasterId(stockMaterialTake.getId());
			detail.setCompanyId(UserUtils.getCompanyId());
			if (detail.getSourceDetailId() != null)
			{
				DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class);
				query.eq("id", detail.getSourceDetailId());
				WorkMaterial workMaterial = daoFactory.getCommonDao().getByDynamicQuery(query, WorkMaterial.class);
				workMaterial.setTakeQty(workMaterial.getTakeQty().add(detail.getQty()));
			}
			daoFactory.getCommonDao().saveEntity(detail);
		}
		ServiceResult<StockMaterialTake> serviceResult = new ServiceResult<StockMaterialTake>();
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(stockMaterialTake.getId(), stockMaterialTake.getForceCheck()));
		}
		serviceResult.setReturnValue(stockMaterialTake);
		return serviceResult;

	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialTakeService#update(com.huayin.printmanager.persist.entity.stock.
	 * StockMaterialTake)
	 */
	@Override
	@Transactional
	public ServiceResult<StockMaterialTake> update(StockMaterialTake stockMaterialTake)
	{
		// 是否要审核标识
		BoolValue flag = stockMaterialTake.getIsCheck();

		// 先判断库存
		ServiceResult<StockMaterialTake> serviceResult = new ServiceResult<StockMaterialTake>();

		// 再更新数据
		StockMaterialTake stockMaterialTake_ = this.lockHasChildren(stockMaterialTake.getId());
		// 判断是否已审核
		if (stockMaterialTake_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockMaterialTakeDetail newItem : stockMaterialTake.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockMaterialTakeDetail oldItem : stockMaterialTake_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				StockMaterialTakeDetail detail = this.getDetail(id);

				if (detail.getSourceDetailId() != null && !"".equals(detail.getSourceDetailId()))
				{
					DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class);
					query.eq("id", detail.getSourceDetailId());
					WorkMaterial workMaterial = daoFactory.getCommonDao().getByDynamicQuery(query, WorkMaterial.class);
					workMaterial.setTakeQty(workMaterial.getTakeQty().subtract(detail.getQty()));
				}
				daoFactory.getCommonDao().deleteEntity(StockMaterialTakeDetail.class, id);
			}
		}
		if (stockMaterialTake.getReceiveEmployeeId() != null)
		{
			stockMaterialTake.setReceiveEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialTake.getReceiveEmployeeId())).getName());
		}

		if (stockMaterialTake.getSendEmployeeId() != null)
		{
			stockMaterialTake.setSendEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialTake.getSendEmployeeId())).getName());
		}
		stockMaterialTake.setWarehouseName(((Warehouse) UserUtils.getBasicInfo("WAREHOUSE", stockMaterialTake.getWarehouseId())).getName());
		stockMaterialTake.setIsCheck(BoolValue.NO);
		PropertyClone.copyProperties(stockMaterialTake_, stockMaterialTake, false, new String[] { "detailList" }, new String[] { "memo" });
		for (StockMaterialTakeDetail detail : stockMaterialTake.getDetailList())
		{
			if (detail.getId() != null)
			{
				StockMaterialTakeDetail stockMaterialTakeDetail_ = this.getDetail(detail.getId());

				if (detail.getSourceDetailId() != null)
				{
					DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class);
					query.eq("id", detail.getSourceDetailId());
					WorkMaterial workMaterial = daoFactory.getCommonDao().getByDynamicQuery(query, WorkMaterial.class);
					workMaterial.setTakeQty(workMaterial.getTakeQty().subtract(stockMaterialTakeDetail_.getQty()).add(detail.getQty()));
				}
				PropertyClone.copyProperties(stockMaterialTakeDetail_, detail, false, null, new String[] { "memo" });

			}
			else
			{
				detail.setMasterId(stockMaterialTake.getId());
				detail.setCompanyId(UserUtils.getCompanyId());

				daoFactory.getCommonDao().saveEntity(detail);
			}
		}
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(stockMaterialTake.getId(), stockMaterialTake.getForceCheck()));
		}
		serviceResult.setReturnValue(stockMaterialTake);
		return serviceResult;

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialTakeService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Boolean delete(Long id)
	{
		try
		{
			StockMaterialTake stockMaterialTake = this.get(id);
			for (StockMaterialTakeDetail detail : stockMaterialTake.getDetailList())
			{
				if (detail.getSourceId() != null)
				{
					WorkMaterial workMaterial = daoFactory.getCommonDao().getEntity(WorkMaterial.class, detail.getSourceDetailId());
					workMaterial.setTakeQty(workMaterial.getTakeQty().subtract(detail.getQty()));
				}
			}

			daoFactory.getCommonDao().deleteAllEntity(stockMaterialTake.getDetailList());
			daoFactory.getCommonDao().deleteEntity(stockMaterialTake);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialTakeService#check(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockMaterial> check(Long id, BoolValue forceCheck)
	{
		List<StockMaterial> list = new ArrayList<StockMaterial>();
		StockMaterialTake master = this.get(id);
		// 先判断是否已经审核
		if (master.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核");
		}
		
		for (StockMaterialTakeDetail stockMaterialTakeDetail : master.getDetailList())
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
			query.eq("materialId", stockMaterialTakeDetail.getMaterialId());
			if (stockMaterialTakeDetail.getSpecifications() == null || "".equals(stockMaterialTakeDetail.getSpecifications()))
			{
				stockMaterialTakeDetail.setSpecifications(null);
				query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
			}
			else
			{
				query.eq("specifications", stockMaterialTakeDetail.getSpecifications());
			}
			query.eq("warehouseId", master.getWarehouseId());
			StockMaterial stockMaterial = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);
			if (stockMaterial == null)
			{
				stockMaterial = new StockMaterial();
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterialTakeDetail.getMaterialId()));
				stockMaterial.setQty(new BigDecimal(0));
			}
			else
			{
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterial.getMaterialId()));
			}

			if (stockMaterialTakeDetail.getQty().compareTo(stockMaterial.getQty()) > 0)
			{
				list.add(stockMaterial);
			}
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			list.clear();

			if (!serviceFactory.getCommonService().audit(BillType.STOCK_MR, id, BoolValue.YES))
			{
				return null;
			}
			// 库存操作
			for (StockMaterialTakeDetail stockMaterialTakeDetail : master.getDetailList())
			{
				StockMaterial stockMaterial = new StockMaterial();
				stockMaterial.setMaterialId(stockMaterialTakeDetail.getMaterialId());
				stockMaterial.setSpecifications(stockMaterialTakeDetail.getSpecifications());
				stockMaterial.setQty(stockMaterialTakeDetail.getQty());
				stockMaterial.setValuationQty(stockMaterialTakeDetail.getValuationQty());
				stockMaterial.setWarehouseId(master.getWarehouseId());
				stockMaterial.setMoney(stockMaterialTakeDetail.getMoney());
				stockMaterial.setMaterialClassId(stockMaterialTakeDetail.getMaterialClassId());
				serviceFactory.getMaterialStockService().stock(stockMaterial, InOutType.OUT);

				// 出库记录
				StockMaterialLog log = new StockMaterialLog();
				log.setBillId(id);
				log.setBillType(master.getBillType());
				log.setBillNo(master.getBillNo());
				log.setSourceId(stockMaterialTakeDetail.getId());
				log.setCreateTime(new Date());
				log.setCompanyId(UserUtils.getCompanyId());
				log.setCode(stockMaterialTakeDetail.getCode());
				log.setMaterialClassId(stockMaterialTakeDetail.getMaterialClassId());
				log.setMaterialName(stockMaterialTakeDetail.getMaterialName());
				log.setMaterialId(stockMaterialTakeDetail.getMaterialId());
				log.setWeight(stockMaterialTakeDetail.getWeight());
				log.setSpecifications(stockMaterialTakeDetail.getSpecifications());
				log.setWarehouseId(master.getWarehouseId());
				log.setUnitId(stockMaterialTakeDetail.getStockUnitId());
				log.setPrice(stockMaterialTakeDetail.getPrice());
				log.setOutQty(stockMaterialTakeDetail.getQty());
				log.setOutMoney(stockMaterialTakeDetail.getMoney());
				daoFactory.getCommonDao().saveEntity(log);
			}

		}

		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialTakeService#checkBack(java.lang.Long)
	 */
	@Override
	@Transactional
	public Boolean checkBack(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.STOCK_MR, id, BoolValue.NO))
		{
			return false;
		}
		StockMaterialTake stockMaterialTake = this.get(id);
		for (StockMaterialTakeDetail detail : stockMaterialTake.getDetailList())
		{
			StockMaterial stockMaterial = new StockMaterial();
			stockMaterial.setMaterialId(detail.getMaterialId());
			stockMaterial.setSpecifications(detail.getSpecifications());
			stockMaterial.setQty(detail.getQty());
			stockMaterial.setValuationQty(detail.getValuationQty());
			stockMaterial.setWarehouseId(stockMaterialTake.getWarehouseId());
			stockMaterial.setPrice(detail.getPrice());
			stockMaterial.setMoney(detail.getMoney());
			stockMaterial.setMaterialClassId(detail.getMaterialClassId());
			serviceFactory.getMaterialStockService().backStock(stockMaterial,InOutType.OUT);

			DynamicQuery query = new CompanyDynamicQuery(StockMaterialLog.class);
			query.eq("billId", id);
			List<StockMaterialLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockMaterialLog.class);
			daoFactory.getCommonDao().deleteAllEntity(logs);
		}

		return true;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialTakeService#forceComplete(java.lang.Long[],
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public Boolean forceComplete(Long[] ids, BoolValue flag)
	{
		try
		{
			for (Long id : ids)
			{
				WorkMaterial workMaterial = daoFactory.getCommonDao().getEntity(WorkMaterial.class, id);
				workMaterial.setIsNotTake(flag);
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}

	}

	/**
	 * <pre>
	 * 获取库存价格
	 * </pre>
	 * @param materialId
	 * @param specifications
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:32:04, think
	 */
	private BigDecimal getStockPrice(Long materialId, String specifications)
	{
		DynamicQuery queryStock = new CompanyDynamicQuery(StockMaterial.class);
		queryStock.eq("materialId", materialId);
		if (specifications == null || "".equals(specifications))
		{
			queryStock.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
		}
		else
		{
			queryStock.eq("specifications", specifications);
		}
		queryStock.desc("price");
		queryStock.setPageIndex(1);
		queryStock.setPageSize(1);
		queryStock.setQueryType(QueryType.JDBC);
		queryStock.eq("companyId", UserUtils.getCompanyId());
		SearchResult<StockMaterial> stockResult = daoFactory.getCommonDao().findEntityByDynamicQueryPage(queryStock, StockMaterial.class);
		if (stockResult.getResult() != null && stockResult.getResult().size() > 0)
		{
			return stockResult.getResult().get(0).getPrice();
		}
		else
		{
			return new BigDecimal(0);
		}
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialTakeService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockMaterialTake lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialTake.class);
		query.eq("id", id);
		StockMaterialTake order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockMaterialTake.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockMaterialTakeDetail.class);
		query_detail.eq("masterId", id);
		List<StockMaterialTakeDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockMaterialTakeDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}
}
