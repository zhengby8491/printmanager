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
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSplit;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSplitDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockMaterialSplitService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料分切
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockMaterialSplitServiceImpl extends BaseServiceImpl implements StockMaterialSplitService
{

	/* （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSplitService#findByCondition(com.huayin.printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialSplit> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialSplit.class);
		if (queryParam.getDateMin() != null)
		{
			query.ge("splitTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("splitTime", queryParam.getDateMax());
		}
		if (null != queryParam.getWarehouseId())
		{
			query.eq("warehouseId", queryParam.getWarehouseId());
		}
		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getSplitType() != null)
		{
			query.eq("splitType", queryParam.getSplitType());
		}
		if (StringUtils.isNotEmpty(queryParam.getSpecifications()))
		{
			query.like("specifications", "%" + queryParam.getSpecifications() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getMaterialName()))
		{
			query.like("materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("isCheck", queryParam.getAuditFlag());
		}
		query.eq("companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockMaterialSplit.class);
	}

	/* （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSplitService#findDetailByCondition(com.huayin.printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialSplitDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialSplitDetail.class, "ad");
		query.createAlias(StockMaterialSplit.class, "a");
		query.addProjection(Projections.property("ad, a"));
		query.eqProperty("ad.masterId", "a.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.splitTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.splitTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getMaterialName()))
		{
			query.like("ad.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("a.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (null != queryParam.getInWarehouseId())
		{
			query.eq("ad.warehouseId", queryParam.getInWarehouseId());
		}
		if (null != queryParam.getOutWarehouseId())
		{
			query.eq("a.warehouseId", queryParam.getOutWarehouseId());
		}
		if (queryParam.getSplitType() != null)
		{
			query.eq("a.splitType", queryParam.getSplitType());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("a.isCheck", queryParam.getAuditFlag());
		}
		if (StringUtils.isNotEmpty(queryParam.getSpecifications()))
		{
			query.like("a.specifications", "%" + queryParam.getSpecifications() + "%");
		}
		query.eq("ad.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("a.createTime");
		query.setIsSearchTotalCount(true);

		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<StockMaterialSplitDetail> result = new SearchResult<StockMaterialSplitDetail>();
		result.setResult(new ArrayList<StockMaterialSplitDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockMaterialSplitDetail detail = (StockMaterialSplitDetail) c[0];
			detail.setMaster((StockMaterialSplit) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/* （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSplitService#save(com.huayin.printmanager.persist.entity.stock.StockMaterialSplit)
	 */
	@Override
	@Transactional
	public ServiceResult<StockMaterialSplit> save(StockMaterialSplit stockMaterialSplit)
	{
		// 是否要审核标识
		BoolValue flag = stockMaterialSplit.getIsCheck();
		stockMaterialSplit.setBillType(BillType.STOCK_SPL);
		stockMaterialSplit.setBillNo(UserUtils.createBillNo(BillType.STOCK_SPL));
		stockMaterialSplit.setCompanyId(UserUtils.getCompanyId());
		stockMaterialSplit.setCreateTime(new Date());
		stockMaterialSplit.setIsCheck(BoolValue.NO);
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			stockMaterialSplit.setCreateName(e.getName());
		}
		else
		{
			stockMaterialSplit.setCreateName(UserUtils.getUserName());
		}
		stockMaterialSplit.setCreateEmployeeId(UserUtils.getEmployeeId());
		daoFactory.getCommonDao().saveEntity(stockMaterialSplit);
		for (StockMaterialSplitDetail stockMaterialSplitDetail : stockMaterialSplit.getDetailList())
		{
			stockMaterialSplitDetail.setMasterId(stockMaterialSplit.getId());
			stockMaterialSplitDetail.setCompanyId(UserUtils.getCompanyId());

			daoFactory.getCommonDao().saveEntity(stockMaterialSplitDetail);
		}
		ServiceResult<StockMaterialSplit> serviceResult = new ServiceResult<StockMaterialSplit>();
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(stockMaterialSplit.getId(), stockMaterialSplit.getForceCheck()));
		}
		serviceResult.setReturnValue(stockMaterialSplit);
		return serviceResult;

	}

	/* （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSplitService#update(com.huayin.printmanager.persist.entity.stock.StockMaterialSplit)
	 */
	@Override
	@Transactional
	public ServiceResult<StockMaterialSplit> update(StockMaterialSplit stockMaterialSplit)
	{
		// 是否要审核标识
		BoolValue flag = stockMaterialSplit.getIsCheck();

		ServiceResult<StockMaterialSplit> serviceResult = new ServiceResult<StockMaterialSplit>();

		// 再更新数据
		StockMaterialSplit stockMaterialSplit_ = this.lockHasChildren(stockMaterialSplit.getId());
		// 先判断是否已经审核
		if (stockMaterialSplit_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockMaterialSplitDetail newItem : stockMaterialSplit.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockMaterialSplitDetail oldItem : stockMaterialSplit_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockMaterialSplitDetail.class, id);
			}
		}
		stockMaterialSplit_.setQty(stockMaterialSplit.getQty());
		stockMaterialSplit_.setMoney(stockMaterialSplit.getMoney());
		stockMaterialSplit_.setValuationQty(stockMaterialSplit.getValuationQty());
		stockMaterialSplit_.setWarehouseId(stockMaterialSplit.getWarehouseId());
		stockMaterialSplit_.setSplitTime(stockMaterialSplit.getSplitTime());
		stockMaterialSplit_.setMemo(stockMaterialSplit.getMemo());
		daoFactory.getCommonDao().updateEntity(stockMaterialSplit_);
		for (StockMaterialSplitDetail stockMaterialSplitDetail : stockMaterialSplit.getDetailList())
		{
			if (stockMaterialSplitDetail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(StockMaterialSplitDetail.class);
				querySt.eq("id", stockMaterialSplitDetail.getId());
				StockMaterialSplitDetail stockMaterialSplitDetail_ = daoFactory.getCommonDao().getByDynamicQuery(querySt, StockMaterialSplitDetail.class);
				stockMaterialSplitDetail_.setSpecifications(stockMaterialSplitDetail.getSpecifications());
				stockMaterialSplitDetail_.setQty(stockMaterialSplitDetail.getQty());
				stockMaterialSplitDetail_.setPrice(stockMaterialSplitDetail.getPrice());
				stockMaterialSplitDetail_.setValuationQty(stockMaterialSplitDetail.getValuationQty());
				stockMaterialSplitDetail_.setValuationPrice(stockMaterialSplitDetail.getValuationPrice());
				stockMaterialSplitDetail_.setMoney(stockMaterialSplitDetail.getMoney());
				stockMaterialSplitDetail_.setMemo(stockMaterialSplitDetail.getMemo());
				stockMaterialSplitDetail_.setWarehouseId(stockMaterialSplitDetail.getWarehouseId());
				daoFactory.getCommonDao().updateEntity(stockMaterialSplitDetail_);
			}
			else
			{
				stockMaterialSplitDetail.setMasterId(stockMaterialSplit.getId());
				stockMaterialSplitDetail.setCompanyId(UserUtils.getCompanyId());

				daoFactory.getCommonDao().saveEntity(stockMaterialSplitDetail);
			}
		}
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(stockMaterialSplit.getId(), stockMaterialSplit.getForceCheck()));
		}
		serviceResult.setReturnValue(stockMaterialSplit_);
		return serviceResult;
	}

	/* （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSplitService#get(java.lang.Long)
	 */
	@Override
	public StockMaterialSplit get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialSplit.class);
		query.eq("id", id);
		StockMaterialSplit stockMaterialSplit = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialSplit.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockMaterialSplitDetail.class);
		queryD.eq("masterId", id);
		List<StockMaterialSplitDetail> stockMaterialSplitDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, StockMaterialSplitDetail.class);
		if (stockMaterialSplit != null)
		{
			stockMaterialSplit.setDetailList(stockMaterialSplitDetailList);
		}
		return stockMaterialSplit;
	}

	/* （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSplitService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			StockMaterialSplit master = this.lockHasChildren(id);
			daoFactory.getCommonDao().deleteAllEntity(master.getDetailList());
			daoFactory.getCommonDao().deleteEntity(master);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	/* （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSplitService#check(java.lang.Long, com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockMaterial> check(Long id, BoolValue forceCheck)
	{
		List<StockMaterial> list = new ArrayList<StockMaterial>();
		StockMaterialSplit master = this.lockHasChildren(id);
		// 先判断是否已经审核
		if (master.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核");
		}

		// 判断库存是否充足
		StockMaterial stock = stockIsEnough(master.getMaterialId(), master.getSpecifications(), master.getWarehouseId(), master.getQty());
		if (null != stock)
		{
			list.add(stock);
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			list.clear();

			if (!serviceFactory.getCommonService().audit(BillType.STOCK_SPL, id, BoolValue.YES))
			{
				return null;
			}

			// 出库记录
			StockMaterialLog logOut = new StockMaterialLog();
			logOut.setBillId(id);
			logOut.setBillType(master.getBillType());
			logOut.setBillNo(master.getBillNo());
			logOut.setSourceId(master.getId());
			logOut.setCreateTime(new Date());
			logOut.setCompanyId(UserUtils.getCompanyId());
			logOut.setCode(master.getMaterial().getCode());
			logOut.setMaterialClassId(master.getMaterial().getMaterialClassId());
			logOut.setMaterialName(master.getMaterialName());
			logOut.setMaterialId(master.getMaterialId());
			logOut.setSpecifications(master.getSpecifications());
			logOut.setWarehouseId(master.getWarehouseId());
			logOut.setWeight(master.getMaterial().getWeight());
			logOut.setUnitId(master.getStockUnitId());
			logOut.setPrice(master.getPrice());
			logOut.setOutQty(master.getQty());
			logOut.setOutMoney(master.getMoney());
			daoFactory.getCommonDao().saveEntity(logOut);

			// 出库库存操作
			StockMaterial stockMaterial = new StockMaterial();
			stockMaterial.setMaterialId(master.getMaterialId());
			stockMaterial.setSpecifications(master.getSpecifications());
			stockMaterial.setQty(master.getQty());
			stockMaterial.setValuationQty(master.getValuationQty());
			stockMaterial.setWarehouseId(master.getWarehouseId());
			stockMaterial.setMoney(master.getMoney());
			serviceFactory.getMaterialStockService().stock(stockMaterial, InOutType.OUT);

			for (StockMaterialSplitDetail stockMaterialSplitDetail : master.getDetailList())
			{
				// 入库记录
				StockMaterialLog log = new StockMaterialLog();
				log.setBillId(master.getId());
				log.setBillType(master.getBillType());
				log.setBillNo(master.getBillNo());
				log.setSourceId(stockMaterialSplitDetail.getId());
				log.setCreateTime(new Date());
				log.setCompanyId(UserUtils.getCompanyId());
				log.setCode(stockMaterialSplitDetail.getCode());
				log.setMaterialClassId(stockMaterialSplitDetail.getMaterialClassId());
				log.setMaterialId(stockMaterialSplitDetail.getMaterialId());
				log.setMaterialName(stockMaterialSplitDetail.getMaterialName());
				log.setSpecifications(stockMaterialSplitDetail.getSpecifications());
				log.setWeight(stockMaterialSplitDetail.getWeight());
				log.setWarehouseId(stockMaterialSplitDetail.getWarehouseId());
				log.setUnitId(stockMaterialSplitDetail.getStockUnitId());
				log.setPrice(stockMaterialSplitDetail.getPrice());

				log.setInQty(stockMaterialSplitDetail.getQty());
				log.setInMoney(stockMaterialSplitDetail.getMoney());

				daoFactory.getCommonDao().saveEntity(log);

				// 库存操作
				StockMaterial stockMaterialDetail = new StockMaterial();
				stockMaterialDetail.setMaterialId(stockMaterialSplitDetail.getMaterialId());
				stockMaterialDetail.setSpecifications(stockMaterialSplitDetail.getSpecifications());
				stockMaterialDetail.setQty(stockMaterialSplitDetail.getQty());
				stockMaterialDetail.setValuationQty(stockMaterialSplitDetail.getValuationQty());
				stockMaterialDetail.setWarehouseId(stockMaterialSplitDetail.getWarehouseId());
				stockMaterialDetail.setPrice(stockMaterialSplitDetail.getPrice());
				stockMaterialDetail.setValuationPrice(stockMaterialSplitDetail.getValuationPrice());
				stockMaterialDetail.setMoney(stockMaterialSplitDetail.getMoney());
				stockMaterialDetail.setMaterialClassId(stockMaterialSplitDetail.getMaterialClassId());
				stockMaterialDetail.setValuationPrice(stockMaterialSplitDetail.getValuationPrice());
				stockMaterialDetail.setUpdateTime(new Date());
				serviceFactory.getMaterialStockService().stock(stockMaterialDetail,InOutType.IN);
			}
		}

		return list;
	}

	/* （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSplitService#checkBack(java.lang.Long, com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockMaterial> checkBack(Long id, BoolValue forceCheck)
	{
		List<StockMaterial> resultList = new ArrayList<StockMaterial>();
		// 审核操作
		StockMaterialSplit master = this.lockHasChildren(id);
		// 先判断是否已经反审核
		if (master.getIsCheck() == BoolValue.NO)
		{
			throw new BusinessException("已反审核");
		}
		// 库存操作
		StockMaterial stockMaterial = new StockMaterial();
		stockMaterial.setMaterialId(master.getMaterialId());
		stockMaterial.setSpecifications(master.getSpecifications());
		stockMaterial.setQty(master.getQty());
		stockMaterial.setValuationQty(master.getValuationQty());
		stockMaterial.setWarehouseId(master.getWarehouseId());
		stockMaterial.setPrice(master.getPrice());
		stockMaterial.setValuationPrice(master.getValuationPrice());
		stockMaterial.setMoney(master.getMoney());
		stockMaterial.setMaterialClassId(master.getMaterial().getMaterialClassId());
		stockMaterial.setUpdateTime(new Date());

		for (StockMaterialSplitDetail detail : master.getDetailList())
		{
			// 判断库存是否充足
			StockMaterial stock = stockIsEnough(detail.getMaterialId(), detail.getSpecifications(), detail.getWarehouseId(), detail.getQty());
			if (stock != null)
			{
				stock.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, detail.getMaterialId()));
				resultList.add(stock);
			}
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (resultList.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			if (!serviceFactory.getCommonService().audit(BillType.STOCK_SPL, id, BoolValue.NO))
			{
				return null;
			}
			serviceFactory.getMaterialStockService().backStock(stockMaterial, InOutType.OUT);
			for (StockMaterialSplitDetail detail : master.getDetailList())
			{
				// 库存操作
				StockMaterial stockMaterialDetail = new StockMaterial();
				stockMaterialDetail.setMaterialId(detail.getMaterialId());
				stockMaterialDetail.setSpecifications(detail.getSpecifications());
				stockMaterialDetail.setQty(detail.getQty());
				stockMaterialDetail.setValuationQty(detail.getValuationQty());
				stockMaterialDetail.setWarehouseId(detail.getWarehouseId());
				stockMaterialDetail.setPrice(detail.getPrice());
				stockMaterialDetail.setValuationPrice(detail.getValuationPrice());
				stockMaterialDetail.setMaterialClassId(detail.getMaterialClassId());
				stockMaterialDetail.setMoney(detail.getMoney());
				stockMaterialDetail.setValuationPrice(detail.getValuationPrice());
				stockMaterialDetail.setUpdateTime(new Date());
				stockMaterial.setIsIn(BoolValue.YES);
				serviceFactory.getMaterialStockService().backStock(stockMaterialDetail, InOutType.IN);
			}
		}
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialLog.class);
		query.eq("billId", id);
		List<StockMaterialLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockMaterialLog.class);
		daoFactory.getCommonDao().deleteAllEntity(logs);

		return resultList;
	}

	/* （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSplitService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockMaterialSplit lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialSplit.class);
		query.eq("id", id);
		StockMaterialSplit order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockMaterialSplit.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockMaterialSplitDetail.class);
		query_detail.eq("masterId", id);
		List<StockMaterialSplitDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockMaterialSplitDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

	/**
	 * <pre>
	 * 判断库存是否充足 充足返回null 不足返回库存对象
	 * </pre>
	 * @param materialId
	 * @param specifications
	 * @param warehouseId
	 * @param qty
	 * @return
	 * @since 1.0, 2018年2月24日 下午3:04:56, think
	 */
	private StockMaterial stockIsEnough(Long materialId, String specifications, Long warehouseId, BigDecimal qty)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
		query.eq("materialId", materialId);
		if (specifications == null || "".equals(specifications))
		{
			query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
		}
		else
		{
			query.eq("specifications", specifications);
		}
		query.eq("warehouseId", warehouseId);
		StockMaterial stockMaterial = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);

		if (stockMaterial == null)
		{
			stockMaterial = new StockMaterial();
			stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, materialId));
			stockMaterial.setQty(new BigDecimal(0));
		}
		else
		{
			stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterial.getMaterialId()));
		}

		if (qty.compareTo(stockMaterial.getQty()) > 0)
		{
			return stockMaterial;
		}

		return null;
	}
}
