/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月24日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.stock.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ObjectHelper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.persist.enumerate.MaterialType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockMaterialService;
import com.huayin.printmanager.service.stock.vo.StockMaterialVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料库存
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockMaterialServiceImpl extends BaseServiceImpl implements StockMaterialService
{

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialService#findByCondition(java.lang.String,
	 * com.huayin.printmanager.persist.enumerate.MaterialType, java.lang.Long, java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue, java.lang.Integer, java.lang.Integer, java.lang.String,
	 * java.lang.String, java.lang.Integer)
	 */
	@Override
	public SearchResult<StockMaterial> findByCondition(String materialName, MaterialType materialType, Long warehouseId, Long materialClassId, BoolValue isEmptyWare, Integer pageIndex, Integer pageSize, String code, String specifications, Integer weight)
	{
		SearchResult<StockMaterial> result = new SearchResult<StockMaterial>();
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class, "s");
			query.createAlias(Material.class, "m");
			query.addProjection(Projections.property("s, m"));
			query.eqProperty("s.materialId", "m.id");

			if (StringUtils.isNotBlank(code))
			{
				query.like("m.code", "%" + code + "%");
			}
			if (StringUtils.isNotBlank(specifications))
			{
				query.like("s.specifications", "%" + specifications + "%");
			}
			if (weight != null)
			{
				query.eq("s.weight", weight);
			}

			if (materialName != null && !"".equals(materialName))
			{
				query.like("m.name", "%" + materialName + "%");
			}
			if (materialType != null && !"".equals(materialType))
			{
				query.eq("m.materialType", materialType);
			}
			if (warehouseId != null && !"".equals(warehouseId))
			{
				query.eq("s.warehouseId", warehouseId);
			}
			if (materialClassId != null && !"".equals(materialClassId))
			{
				query.eq("m.materialClassId", materialClassId);
			}
			if (isEmptyWare == null || "".equals(isEmptyWare))
			{
				query.gt("qty", 0);
			}
			query.eq("s.companyId", UserUtils.getCompanyId());
			query.setPageIndex(pageIndex);
			query.setPageSize(pageSize);
			query.desc("s.updateTime");
			query.setIsSearchTotalCount(true);

			SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
			result.setResult(new ArrayList<StockMaterial>());
			for (Object[] c : temp_result.getResult())
			{
				StockMaterial stockMaterial = (StockMaterial) c[0];
				stockMaterial.setMaterial((Material) c[1]);
				result.getResult().add(stockMaterial);
			}
			result.setCount(temp_result.getCount());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialService#stock(com.huayin.printmanager.persist.entity.stock.
	 * StockMaterial)
	 */
	@Override
	@Transactional
	public Long stock(StockMaterial stockMaterial, InOutType type)
	{
		StockMaterial stockMaterialOld = null;
		try
		{
			if (type == InOutType.OUT) // 出库时的数量改成负数
			{
				stockMaterial.setQty(BigDecimal.valueOf(0).subtract(stockMaterial.getQty()));	
				stockMaterial.setValuationQty(BigDecimal.valueOf(0).subtract(stockMaterial.getValuationQty()));
			}
			
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
			query.eq("materialId", stockMaterial.getMaterialId());
			if (stockMaterial.getSpecifications() == null || "".equals(stockMaterial.getSpecifications()))
			{
				stockMaterial.setSpecifications(null);
				query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
			}
			else
			{
				query.eq("specifications", stockMaterial.getSpecifications());
			}
			query.eq("warehouseId", stockMaterial.getWarehouseId());
			stockMaterialOld = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);
			// 如果存在库存记录，则需合计旧记录
			if (stockMaterialOld != null)
			{
				stockMaterialOld.setUpdateTime(new Date());
				stockMaterialOld.setQty(stockMaterial.getQty().add(stockMaterialOld.getQty()));
				stockMaterialOld.setValuationQty(stockMaterial.getValuationQty().add(stockMaterialOld.getValuationQty()));
				BigDecimal moneyStock = stockMaterialOld.getMoney();// 入库前库存总金额
				BigDecimal moneyPurch = stockMaterial.getMoney();// 此次入库总金额
				// 判断是不是出库
				if (type == InOutType.IN) // 入库
				{
					// 算实时计价单价 计价数量不为0才计算
					if (stockMaterialOld.getValuationQty().doubleValue() - 0 != 0)
					{
						stockMaterialOld.setValuationPrice(moneyStock.add(moneyPurch).divide(stockMaterialOld.getValuationQty(), 4, BigDecimal.ROUND_HALF_UP));
						// 库存单价
						stockMaterialOld.setPrice(moneyStock.add(moneyPurch).divide(stockMaterialOld.getQty(), 4, BigDecimal.ROUND_HALF_UP));
						// 算金额
						stockMaterialOld.setMoney(moneyStock.add(moneyPurch));
					}
					else
					{
						stockMaterialOld.setMoney(new BigDecimal(0));
					}
				}
				else // 出库
				{
					stockMaterialOld.setMoney(moneyStock.subtract(moneyPurch));
				}

			}
			else // 如果不存在库存记录，则新增一笔记录
			{
				stockMaterial.setCreateTime(new Date());
				stockMaterial.setCompanyId(UserUtils.getCompanyId());
				Material material = daoFactory.getCommonDao().getEntity(Material.class, stockMaterial.getMaterialId());
				stockMaterial.setWeight(material.getWeight());
				stockMaterial.setValuationPrice(stockMaterial.getMoney().divide(stockMaterial.getValuationQty(), 4, RoundingMode.HALF_UP));
				daoFactory.getCommonDao().saveEntity(stockMaterial);
				stockMaterialOld = stockMaterial;
				stockMaterialOld.setUpdateTime(new Date());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new Long(0);
		}

		return stockMaterialOld.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialService#backStock(com.huayin.printmanager.persist.entity.stock.
	 * StockMaterial)
	 */
	public Long backStock(StockMaterial stockMaterial, InOutType type)
	{
		StockMaterial stockMaterialOld = null;

		try
		{
			if (type == InOutType.IN) // 反审核入库时的数量改成负数
			{
				stockMaterial.setQty(BigDecimal.valueOf(0).subtract(stockMaterial.getQty()));	
				stockMaterial.setValuationQty(BigDecimal.valueOf(0).subtract(stockMaterial.getValuationQty()));
			}
			
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
			query.eq("materialId", stockMaterial.getMaterialId());
			if (stockMaterial.getSpecifications() == null || "".equals(stockMaterial.getSpecifications()))
			{
				stockMaterial.setSpecifications(null);
				query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
			}
			else
			{
				query.eq("specifications", stockMaterial.getSpecifications());
			}
			query.eq("warehouseId", stockMaterial.getWarehouseId());
			stockMaterialOld = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);
			// 如果存在库存记录，则需合计旧记录
			if (stockMaterialOld != null)
			{
				stockMaterialOld.setUpdateTime(new Date());
				stockMaterialOld.setQty(stockMaterial.getQty().add(stockMaterialOld.getQty()));
				stockMaterialOld.setValuationQty(stockMaterial.getValuationQty().add(stockMaterialOld.getValuationQty()));
				BigDecimal moneyStock = stockMaterialOld.getMoney();// 入库前库存总金额
				BigDecimal moneyPurch = stockMaterial.getMoney();// 此次入库总金额
				// 判断是不是出库
				if (type == InOutType.IN) // 反审核入库
				{
					// 算实时计价单价 计价数量不为0才计算
					if (stockMaterialOld.getValuationQty().doubleValue() - 0 != 0)
					{
						stockMaterialOld.setValuationPrice(moneyStock.subtract(moneyPurch).divide(stockMaterialOld.getValuationQty(), 4, BigDecimal.ROUND_HALF_UP));
						// 库存单价
						stockMaterialOld.setPrice(moneyStock.subtract(moneyPurch).divide(stockMaterialOld.getQty(), 4, BigDecimal.ROUND_HALF_UP));
						// 算金额
						stockMaterialOld.setMoney(moneyStock.subtract(moneyPurch));
					}
					else
					{
						stockMaterialOld.setMoney(new BigDecimal(0));
					}
				}
				else // 反审核出库
				{
					stockMaterialOld.setMoney(moneyStock.add(moneyPurch));
				}
			}
			else // 如果不存在库存记录，则新增一笔记录
			{
				stockMaterial.setCreateTime(new Date());
				stockMaterial.setCompanyId(UserUtils.getCompanyId());
				Material material = daoFactory.getCommonDao().getEntity(Material.class, stockMaterial.getMaterialId());
				stockMaterial.setWeight(material.getWeight());
				stockMaterial.setValuationPrice(stockMaterial.getMoney().divide(stockMaterial.getValuationQty(), 4, RoundingMode.HALF_UP));
				daoFactory.getCommonDao().saveEntity(stockMaterial);
				stockMaterialOld = stockMaterial;
				stockMaterialOld.setUpdateTime(new Date());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new Long(0);
		}

		return stockMaterialOld.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialService#adjust(com.huayin.printmanager.persist.entity.stock.
	 * StockMaterial)
	 */
	@Override
	public Long adjust(StockMaterial stockMaterial)
	{
		StockMaterial stockMaterial_ = null;
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
			query.eq("id", stockMaterial.getId());
			stockMaterial_ = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);
			if (stockMaterial_ != null)
			{
				stockMaterial_.setMoney(stockMaterial.getMoney());
				stockMaterial_.setValuationQty(stockMaterial.getValuationQty());
				stockMaterial_.setQty(stockMaterial.getQty());
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return (long) 0;
		}
		return stockMaterial_.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialService#changeValuationPrice(com.huayin.printmanager.persist.
	 * entity.stock.StockMaterial)
	 */
	@Override
	@Transactional
	public Long changeValuationPrice(StockMaterial stockMaterial)
	{
		StockMaterial stockMaterial_ = null;
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
			query.eq("materialId", stockMaterial.getMaterialId());
			if (stockMaterial.getSpecifications() == null || "".equals(stockMaterial.getSpecifications()))
			{
				stockMaterial.setSpecifications(null);
				query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
			}
			else
			{
				query.eq("specifications", stockMaterial.getSpecifications());
			}
			query.eq("warehouseId", stockMaterial.getWarehouseId());
			stockMaterial_ = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);

			if (stockMaterial_ != null)
			{
				stockMaterial_.setUpdateTime(new Date());
				// 入库前库存总金额
				BigDecimal moneyStock = stockMaterial_.getMoney();
				// 此次入库总金额
				BigDecimal moneyPurch = stockMaterial.getMoney();
				// 改变库存单价和金额
				// 库存单价
				stockMaterial_.setPrice(moneyStock.add(moneyPurch).divide(stockMaterial_.getQty(), 4, BigDecimal.ROUND_HALF_UP));
				// 计价单价
				stockMaterial_.setValuationPrice(moneyStock.add(moneyPurch).divide(stockMaterial_.getValuationQty(), 4, BigDecimal.ROUND_HALF_UP));
				stockMaterial_.setMoney(moneyStock.add(moneyPurch));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new Long(0);
		}

		return stockMaterial_.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialService#quickFindByCondition(java.lang.Long,
	 * java.lang.String, java.lang.Long, java.lang.Integer, java.lang.Integer,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	public SearchResult<StockMaterial> quickFindByCondition(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class, "s");
			query.createAlias(Material.class, "m");
			query.eqProperty("s.materialId", "m.id");
			if (queryParam.getMaterialClassId() != null)
			{
				query.eq("s.materialClassId", queryParam.getMaterialClassId());
			}
			if (StringUtils.isNoneBlank(queryParam.getMaterialName()))
			{
				query.like("m.name", "%" + queryParam.getMaterialName() + "%");
			}
			if (StringUtils.isNoneBlank(queryParam.getSpecifications()))
			{
				query.like("s.specifications", "%" + queryParam.getSpecifications() + "%");
			}
			if (queryParam.getWarehouseId() != null)
			{
				query.eq("s.warehouseId", queryParam.getWarehouseId());
			}
			// 不显示0库存
			if (BoolValue.YES == queryParam.getIsEmptyWare())
			{
				query.gt("s.qty", 0);
			}
			query.eq("s.companyId", UserUtils.getCompanyId());
			query.setPageIndex(queryParam.getPageNumber());
			query.setPageSize(queryParam.getPageSize());
			query.desc("s.updateTime");
			query.setIsSearchTotalCount(true);
			query.setQueryType(QueryType.JDBC);
			SearchResult<StockMaterial> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockMaterial.class);
			if (result.getResult().size() > 0)
			{
				for (StockMaterial bean : result.getResult())
				{
					bean.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, bean.getMaterialId()));
				}

			}
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
	 * @see com.huayin.printmanager.service.stock.StockMaterialService#getQty(java.lang.Long, java.lang.String)
	 */
	@Override
	public BigDecimal getQty(Long materialId, String style)
	{
		BigDecimal result = new BigDecimal(0);
		DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
		query.eq("materialId", materialId);
		query.eq("specifications", style);
		List<StockMaterial> queryList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockMaterial.class);
		for (StockMaterial stockMaterial : queryList)
		{
			result = result.add(stockMaterial.getQty());
		}
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialService#stockWarn(com.huayin.printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterial> stockWarn(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class, "s");
		query.createAlias(Material.class, JoinType.INNERJOIN, "m", "m.id=s.materialId");
		query.add(Restrictions.ltProperty("s.qty", "m.minStockNum"));
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setQueryType(QueryType.JDBC);
		query.setIsSearchTotalCount(true);
		SearchResult<StockMaterial> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockMaterial.class);
		if (result.getResult().size() > 0)
		{
			for (StockMaterial bean : result.getResult())
			{
				bean.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, bean.getMaterialId()));
			}

		}
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialService#findStockMaterialLogDetailList(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialLog> findStockMaterialLogDetailList(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialLog.class);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getMaterialName()))
		{
			query.like("materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (null != queryParam.getWarehouseId())
		{
			query.eq("warehouseId", queryParam.getWarehouseId());
		}
		if (StringUtils.isNotEmpty(queryParam.getSpecifications()))
		{
			query.like("specifications", "%" + queryParam.getSpecifications() + "%");
		}
		if (null != queryParam.getBillType())
		{
			query.eq("billType", queryParam.getBillType());
		}
		query.eq("companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("createTime");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<StockMaterialLog> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockMaterialLog.class);
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialService#findStockMaterialLogSumList(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<StockMaterialLog> findStockMaterialLogSumList(QueryParam queryParam)
	{
		SearchResult<StockMaterialLog> logResult = new SearchResult<StockMaterialLog>();

		DynamicQuery query = new CompanyDynamicQuery(StockMaterialLog.class, "a");
		query.addProjection(Projections.property("a.warehouseId,a.materialClassId,a.code,a.materialName,a.specifications,a.weight,a.unitId,sum(a.inQty) as inQty,sum(a.inMoney) as inMoney,sum(a.outQty) as outQty,sum(outMoney) as outMoney"));
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getMaterialName()))
		{
			query.like("materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (null != queryParam.getWarehouseId())
		{
			query.eq("warehouseId", queryParam.getWarehouseId());
		}
		if (null != queryParam.getMaterialClassId())
		{
			query.eq("materialClassId", queryParam.getMaterialClassId());
		}
		if (null != queryParam.getSpecifications() && !"".equals(queryParam.getSpecifications()))
		{
			query.like("specifications", "%" + queryParam.getSpecifications() + "%");
		}
		query.eq("a.companyId", UserUtils.getCompanyId());

		query.addGourp("a.materialName");
		query.addGourp("a.specifications");
		query.addGourp("a.warehouseId");
		DynamicQuery page_query = new CompanyDynamicQuery(query, "b");
		page_query.addProjection(Projections.property("b.*"));
		page_query.setIsSearchTotalCount(true);
		page_query.setPageIndex(queryParam.getPageNumber());
		page_query.setPageSize(queryParam.getPageSize());
		page_query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapStock = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(page_query, HashMap.class);
		logResult.setCount(mapStock.getCount());
		List<StockMaterialLog> logList = new ArrayList<StockMaterialLog>();
		for (Map<String, Object> map : mapStock.getResult())
		{
			StockMaterialLog log = new StockMaterialLog();
			log.setWarehouseId(StringUtils.toLong(map.get("warehouseId")));
			log.setMaterialClassId(StringUtils.toLong(map.get("materialClassId")));
			log.setCode(StringUtils.toString(map.get("code")));
			log.setMaterialName(StringUtils.toString(map.get("materialName")));
			log.setSpecifications(StringUtils.toString(map.get("specifications")));
			log.setWeight(StringUtils.toInteger(map.get("weight")));
			log.setUnitId(StringUtils.toLong(map.get("unitId")));
			log.setInQty(new BigDecimal(StringUtils.toDouble(map.get("inQty"))));
			log.setInMoney(new BigDecimal(StringUtils.toDouble(map.get("inMoney"))));
			log.setOutQty(new BigDecimal(StringUtils.toDouble(map.get("outQty"))));
			log.setOutMoney(new BigDecimal(StringUtils.toDouble(map.get("outMoney"))));
			logList.add(log);

		}
		logResult.setResult(logList);
		return logResult;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialService#findByMaterialId(java.lang.Long)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<StockMaterialVo> findByMaterialId(Long materialId)
	{
		List<StockMaterialVo> resultList = new ArrayList<StockMaterialVo>();
		DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class, "s");
		query.addProjection(Projections.property("s.materialId as materialId,s.warehouseId as warehouseId,s.specifications as style,sum(s.qty) as qty,m.valuationUnitId as valuationUnitId,m.stockUnitId as stockUnitId"));
		query.createAlias(Material.class, JoinType.INNERJOIN, "m", "m.id=s.materialId");
		query.eq("s.materialId", materialId);
		query.addGourp("s.specifications,s.warehouseId");
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		for (Map<String, Object> map : mapResult.getResult())
		{
			StockMaterialVo vo = new StockMaterialVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, StockMaterialVo.class);
				vo.setWarehouseName((String) UserUtils.getBasicInfoFiledValue(BasicType.WAREHOUSE.name(), vo.getWarehouseId(), "name"));
				vo.setPurchQty(serviceFactory.getPurOrderService().getPurchQty(vo.getMaterialId(), vo.getStyle()));
				vo.setWorkEmployQty(serviceFactory.getWorkService().getWorkEmployQty(vo.getMaterialId(), vo.getStyle()));
				vo.setStyle((vo.getStyle() == null || vo.getStyle() == "null") ? "" : vo.getStyle());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			resultList.add(vo);
		}
		return resultList;
	}

}
