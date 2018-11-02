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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.printmanager.helper.service.CustomerHelper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.Product_Customer;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockProductService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品库存
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockProductServiceImpl extends BaseServiceImpl implements StockProductService
{

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductService#findByCondition(java.lang.String, java.lang.Long,
	 * java.lang.Long, com.huayin.printmanager.persist.enumerate.BoolValue, java.lang.Integer, java.lang.Integer,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public SearchResult<StockProduct> findByCondition(QueryParam queryParam)
	{
		SearchResult<StockProduct> result = new SearchResult<StockProduct>();
		try
		{
			CustomerHelper.transferToIds(queryParam);
			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class, "s");
			query.createAlias(Product.class,JoinType.LEFTJOIN, "p", "s.productId =p.id");
			query.addProjection(Projections.property("s, p"));
			if (null != queryParam.getCustomerIdList() && queryParam.getCustomerIdList().size() != 0)
			{
				query.createAlias(Product_Customer.class, JoinType.LEFTJOIN, "pc", "pc.productId=s.productId");
				query.in("pc.customerId", queryParam.getCustomerIdList());
			}
			if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
			{
				query.like("p.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
			}
			if (StringUtils.isNotBlank(queryParam.getCode()))
			{
				query.like("p.code", "%" + queryParam.getCode() + "%");
			}
			if (StringUtils.isNotBlank(queryParam.getSpecifications()))
			{
				query.like("p.specifications", "%" + queryParam.getSpecifications() + "%");
			}

			if (StringUtils.isNotBlank(queryParam.getProductName()))
			{
				query.like("p.name", "%" + queryParam.getProductName() + "%");
			}
			if (queryParam.getWarehouseId() != null && !"".equals(queryParam.getWarehouseId()))
			{
				query.eq("warehouseId", queryParam.getWarehouseId());
			}
			if (queryParam.getProductClassId() != null && !"".equals(queryParam.getProductClassId()))
			{
				query.eq("p.productClassId", queryParam.getProductClassId());
			}
			if (queryParam.getIsEmptyWare() == null || "".equals(queryParam.getIsEmptyWare()))
			{
				query.gt("qty", 0);
			}
			query.eq("p.companyId", UserUtils.getCompanyId());
			query.setPageIndex(queryParam.getPageNumber());
			query.setPageSize(queryParam.getPageSize());
			query.desc("s.updateTime");
			query.setIsSearchTotalCount(true);
			SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
			result.setResult(new ArrayList<StockProduct>());
			for (Object[] c : temp_result.getResult())
			{
				StockProduct stockProduct = (StockProduct) c[0];
				stockProduct.setProduct((Product) c[1]);
				result.getResult().add(stockProduct);
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
	 * @see com.huayin.printmanager.service.stock.StockProductService#stock(com.huayin.printmanager.persist.entity.stock.
	 * StockProduct)
	 */
	@Override
	public Long stock(StockProduct stockProduct, InOutType type)
	{
		StockProduct stockProductOld = null;
		try
		{
			if (type == InOutType.OUT) // 出库时的数量改成负数
			{
				// 出库数量
				stockProduct.setQty(0 - stockProduct.getQty());
				// 出库金额
				stockProduct.setMoney(BigDecimal.valueOf(0).subtract(stockProduct.getMoney()));
			}
			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
			query.eq("productId", stockProduct.getProductId());
			query.eq("warehouseId", stockProduct.getWarehouseId());
			stockProductOld = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);
			// 如果存在库存记录，则需合计旧记录
			if (stockProductOld != null) 
			{
				stockProductOld.setUpdateTime(new Date());
				stockProductOld.setQty(stockProductOld.getQty() + stockProduct.getQty());

				BigDecimal moneyStock = stockProductOld.getMoney();// 入库前库存总金额
				BigDecimal moneyPurch = stockProduct.getMoney();// 此次入库总金额
				if (type == InOutType.IN) // 入库
				{
					if (stockProductOld.getQty() != 0) // 入库前的库存数量不为0
					{
						// 库存单价
						stockProductOld.setPrice(moneyStock.add(moneyPurch).divide(new BigDecimal(stockProductOld.getQty()), 4, BigDecimal.ROUND_HALF_UP));
						// 算金额（跟材料统一算法）
						// stockProduct_.setMoney(stockProduct_.getPrice().multiply(new BigDecimal(stockProduct_.getQty())));
						stockProductOld.setMoney(moneyStock.add(moneyPurch));
					}
					else  // 入库前的库存数量为0
					{
						stockProductOld.setMoney(new BigDecimal(0));
					}
				}
				else  // 出库
				{
					stockProductOld.setMoney(stockProductOld.getPrice().multiply(new BigDecimal(stockProductOld.getQty())));
				}
			}
		  // 如果不存在库存记录，则新增一笔记录
			else
			{
				stockProduct.setCompanyId(UserUtils.getCompanyId());
				daoFactory.getCommonDao().saveEntity(stockProduct);
				stockProductOld = stockProduct;
				stockProductOld.setUpdateTime(new Date());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new Long(0);
		}
		return stockProductOld.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductService#backStock(com.huayin.printmanager.persist.entity.stock.
	 * StockProduct)
	 */
	@Override
	public Long backStock(StockProduct stockProduct, InOutType type)
	{
		/**
		 * -------------------------------------
		 *        | 重新计算单价 | 库存  |
		 * --------------------------------------
		 * 出库           |  NO      | 减少  |
		 * --------------------------------------
		 * 入库           |  YES     | 增加  |
		 * --------------------------------------
		 * 反审核出库|  NO      | 增加 |
		 * --------------------------------------
		 * 反审核入库|  YES     | 减少 |
		 * --------------------------------------
		 */
		StockProduct stockProductOld = null;
		try
		{
			// 表示反审核入库单
			if (type == InOutType.IN)
			{
				stockProduct.setQty(0 - stockProduct.getQty());
			}
			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
			query.eq("productId", stockProduct.getProductId());
			query.eq("warehouseId", stockProduct.getWarehouseId());
			stockProductOld = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);

			// 如果存在库存记录，则需合计旧记录
			if (stockProductOld != null)
			{
				stockProductOld.setUpdateTime(new Date());
				stockProductOld.setQty(stockProductOld.getQty() + stockProduct.getQty());

				BigDecimal moneyStock = stockProductOld.getMoney();// 入库前库存总金额
				BigDecimal moneyPurch = stockProduct.getMoney();// 此次入库总金额
				if (type == InOutType.IN) // 反审核入库
				{
					if (stockProductOld.getQty() != 0) // 入库前的库存数量不为0
					{
						// 库存单价
						stockProductOld.setPrice(moneyStock.subtract(moneyPurch).divide(new BigDecimal(stockProductOld.getQty()), 4, BigDecimal.ROUND_HALF_UP));
						// 算金额（跟材料统一算法）
						// stockProduct_.setMoney(stockProduct_.getPrice().multiply(new BigDecimal(stockProduct_.getQty())));
						stockProductOld.setMoney(moneyStock.subtract(moneyPurch));
					}
					else // 入库前的库存数量为0
					{
						stockProductOld.setMoney(new BigDecimal(0));
					}
				}
				else // 反审核出库
				{
					stockProductOld.setMoney(stockProductOld.getPrice().multiply(new BigDecimal(stockProductOld.getQty())));
				}
			}
			else
			{
				stockProduct.setCompanyId(UserUtils.getCompanyId());
				daoFactory.getCommonDao().saveEntity(stockProduct);
				stockProductOld = stockProduct;
				stockProductOld.setUpdateTime(new Date());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new Long(0);
		}
		return stockProductOld.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductService#adjust(com.huayin.printmanager.persist.entity.stock.
	 * StockProduct)
	 */
	@Override
	public Long adjust(StockProduct stockProduct)
	{
		StockProduct stockProduct_ = null;
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
			query.eq("id", stockProduct.getId());
			stockProduct_ = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);
			if (stockProduct_ != null)
			{
				stockProduct_.setMoney(stockProduct.getMoney());
				stockProduct_.setQty(stockProduct.getQty());
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return (long) 0;
		}
		return stockProduct_.getId();
	}

	@Override
	public Long changePrice(StockProduct stockProduct)
	{
		StockProduct stockProduct_ = null;
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
			query.eq("productId", stockProduct.getProductId());
			query.eq("warehouseId", stockProduct.getWarehouseId());
			stockProduct_ = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);
			if (stockProduct_ != null)
			{
				stockProduct_.setUpdateTime(new Date());
				BigDecimal moneyStock = stockProduct_.getMoney();// 入库前库存总金额
				BigDecimal moneyPurch = stockProduct.getMoney();// 此次入库总金额
				if (stockProduct_.getQty() != 0)
				{
					// 库存单价
					stockProduct_.setPrice(moneyStock.add(moneyPurch).divide(new BigDecimal(stockProduct_.getQty()), 4, BigDecimal.ROUND_HALF_UP));
					// 算金额
					stockProduct_.setMoney(stockProduct_.getPrice().multiply(new BigDecimal(stockProduct_.getQty())));
				}
				else
				{
					// 金额直接为零
					stockProduct_.setMoney(new BigDecimal(0));
					// 单价直接修改
					stockProduct_.setPrice(stockProduct.getPrice());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new Long(0);
		}
		return stockProduct_.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductService#quickFindByCondition(java.lang.Long,
	 * java.lang.String, java.lang.Long, java.lang.Integer, java.lang.Integer,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	public SearchResult<StockProduct> quickFindByCondition(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class, "s");
			query.createAlias(Product.class, "p");
			query.eqProperty("s.productId", "p.id");
			if (queryParam.getProductClassId() != null)
			{
				query.eq("p.productClassId", queryParam.getProductClassId());
			}
			if (StringUtils.isNotBlank(queryParam.getProductName()))
			{
				query.like("p.name", "%" + queryParam.getProductName() + "%");
			}
			if (StringUtils.isNotBlank(queryParam.getProductStyle()))
			{
				query.like("p.specifications", "%" + queryParam.getProductStyle() + "%");
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
			SearchResult<StockProduct> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockProduct.class);
			if (result.getResult().size() > 0)
			{
				for (StockProduct bean : result.getResult())
				{
					Product product = daoFactory.getCommonDao().getEntity(Product.class, bean.getProductId());
					bean.setProduct(product);
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
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductService#findStockProductLogDetailList(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockProductLog> findStockProductLogDetailList(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductLog.class);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getProductName()))
		{
			query.like("productName", "%" + queryParam.getProductName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerMaterialCode()))
		{
			query.like("customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (null != queryParam.getWarehouseId())
		{
			query.eq("warehouseId", queryParam.getWarehouseId());
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
		SearchResult<StockProductLog> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockProductLog.class);
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductService#findStockProductLogSumList(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<StockProductLog> findStockProductLogSumList(QueryParam queryParam)
	{
		SearchResult<StockProductLog> logResult = new SearchResult<StockProductLog>();
		DynamicQuery query = new CompanyDynamicQuery(StockProductLog.class, "a");
		query.addProjection(Projections.property("a.warehouseId,a.productClassId,a.code,a.productName,a.specifications,a.customerMaterialCode,a.unitId,sum(a.inQty) as inQty,sum(a.inMoney) as inMoney,sum(a.outQty) as outQty,sum(outMoney) as outMoney"));
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getProductName()))
		{
			query.like("productName", "%" + queryParam.getProductName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerMaterialCode()))
		{
			query.like("customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (null != queryParam.getWarehouseId())
		{
			query.eq("warehouseId", queryParam.getWarehouseId());
		}

		query.eq("a.companyId", UserUtils.getCompanyId());
		query.addGourp("a.productName");
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
		List<StockProductLog> logList = new ArrayList<StockProductLog>();
		for (Map<String, Object> map : mapStock.getResult())
		{
			StockProductLog log = new StockProductLog();
			log.setWarehouseId(StringUtils.toLong(map.get("warehouseId")));
			log.setProductClassId(StringUtils.toLong(map.get("productClassId")));
			log.setCode(StringUtils.toString(map.get("code")));
			log.setProductName(StringUtils.toString(map.get("productName")));
			log.setSpecifications(StringUtils.toString(map.get("specifications")));
			log.setCustomerMaterialCode(StringUtils.toString(map.get("customerMaterialCode")));
			log.setUnitId(StringUtils.toLong(map.get("unitId")));
			log.setInQty(StringUtils.toInteger(map.get("inQty")));
			log.setInMoney(new BigDecimal(StringUtils.toDouble(map.get("inMoney"))));
			log.setOutQty(StringUtils.toInteger(map.get("outQty")));
			log.setOutMoney(new BigDecimal(StringUtils.toDouble(map.get("outMoney"))));
			logList.add(log);

		}
		logResult.setResult(logList);
		return logResult;
	}

	@Override
	public Map<Long, Integer> findStockQty(List<Long> list)
	{
		Map<Long, Integer> map = new HashMap<>();
		DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
		query.addProjection(Projections.property("productId,SUM(qty) AS qty"));
		query.in("productId", list);
		query.addGourp("productId");
		SearchResult<Object[]> result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		for (Object[] c : result.getResult())
		{
			Long id = (Long) c[0];
			Integer qty = Integer.valueOf(c[1].toString());
			map.put(id, qty);
		}
		return map;
	}
	
	@Override
	public Integer getStockQty(Long productId, Long warehouseId)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
		query.eq("productId", productId);
		query.eq("warehouseId", warehouseId);
		StockProduct _stockProduct = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);
		return _stockProduct.getQty();
	}
}
