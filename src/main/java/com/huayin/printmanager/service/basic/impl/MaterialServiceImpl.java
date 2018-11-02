/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月2日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceException;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.MaterialClass;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.MaterialType;
import com.huayin.printmanager.service.basic.MaterialService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ExcelUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 材料信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月2日
 * @since        2.0, 2018年1月2日 下午17:07:00, think, 规范和国际化
 */
@Service
public class MaterialServiceImpl extends BaseServiceImpl implements MaterialService
{
	@Override
	public Material get(Long id)
	{
		if (id == null)
			return null;
		DynamicQuery query = new CompanyDynamicQuery(Material.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Material.class);
	}

	@Override
	public Material get(String companyId, Long id)
	{
		if (id == null || companyId == null)
			return null;
		DynamicQuery query = new DynamicQuery(Material.class);
		query.eq("id", id);
		query.eq("companyId", companyId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Material.class);
	}

	@Override
	public Material getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(Material.class);
		query.eq("name", name);
		// query.eq("companyId", UserUtils.getCompanyId());
		return daoFactory.getCommonDao().getByDynamicQuery(query, Material.class);
	}

	@Override
	@Transactional
	public Material save(Material material)
	{
		if (material.getWeight() == null)
		{
			material.setWeight(0);
		}
		material.setCode(serviceFactory.getCommonService().getNextCode(BasicType.MATERIAL));
		material.setCompanyId(UserUtils.getCompanyId());
		material.setIsValid(BoolValue.YES);
		material.setCreateName(UserUtils.getUserName());
		material.setCreateTime(new Date());

		return daoFactory.getCommonDao().saveEntity(material);
	}
	
	@Override
	@Transactional
	public Material saveQuick(Long materialClassId, String name, Integer weight, Long valuationUnitId, Long stockUnitId, BigDecimal lastPurchPrice)
	{
		Material material = new Material();
		
		// 材料分类
		material.setMaterialClassId(materialClassId);
		// 材料类别
		material.setMaterialType(MaterialType.INGREDIENTS);
		// 材料编号
		material.setCode(serviceFactory.getCommonService().getNextCode(BasicType.MATERIAL));
		// 材料名称
		material.setName(name);
		// 计价单位
		material.setValuationUnitId(valuationUnitId);
		// 库存单位
		material.setStockUnitId(stockUnitId);
		// 克重
		material.setWeight(weight);
		// 最低采购单价
		material.setLastPurchPrice(lastPurchPrice);
		// 是否有效
		material.setIsValid(BoolValue.YES);
		
		return this.save(material);
	}

	@Override
	@Transactional
	public Material update(Material material)
	{
		return daoFactory.getCommonDao().updateEntity(material);
	}

	/*
	 * @Override
	 * @Transactional public void delete(Long id) { daoFactory.getCommonDao().deleteEntity(Material.class, id); }
	 */

	@Override
	@Transactional
	public void deleteByIds(Long[] ids)
	{
		DynamicQuery query = new CompanyDynamicQuery(Material.class);
		query.in("id", Arrays.asList(ids));
		List<Material> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Material.class);
		serviceFactory.getDaoFactory().getCommonDao().deleteAllEntity(list);
		// serviceFactory.getDaoFactory().getCommonDao().deleteByIds(Material.class,
		// (Object[])ids);
	}

	@Override
	public List<Material> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(Material.class);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Material.class);
	}

	public List<Material> findByMaterialClassId(Long materialClassId)
	{
		DynamicQuery query = new CompanyDynamicQuery(Material.class);
		query.eq("materialClassId", materialClassId);
		query.eq("isValid", BoolValue.YES);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Material.class);
	}

	@Override
	public SearchResult<Material> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Material.class);
		query.setIsSearchTotalCount(true);
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
			query.like("name", "%" + queryParam.getMaterialName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCode()))
		{
			query.like("code", "%" + queryParam.getCode() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		// query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Material.class);
	}

	@Override
	public SearchResult<Material> quickFindByCondition(QueryParam queryParam)
	{
		SearchResult<Material> result = new SearchResult<Material>();
		DynamicQuery query = new CompanyDynamicQuery(Material.class);
		if (queryParam.getMaterialClassId() != null && queryParam.getMaterialClassId() != -1)
		{
			query.eq("materialClassId", queryParam.getMaterialClassId());
		}
		if (StringUtils.isNotEmpty(queryParam.getMaterialName()))
		{
			query.like("name", "%" + queryParam.getMaterialName() + "%");
		}
		query.eq("isValid", BoolValue.YES);
		// query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		query.desc("createTime");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Material.class);
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer importFromExcel(InputStream inputStream) throws Exception
	{
		Integer count = 0;
		try
		{
			Workbook book = ExcelUtils.initBook(inputStream);
			List<ArrayList<String>> sheet = ExcelUtils.readXlsx(book, 0);
			String code = serviceFactory.getCommonService().getNextCode(BasicType.MATERIAL).replace(BasicType.MATERIAL.getCodePrefix(), "");
			Integer codeIndex = Integer.parseInt(code);
			for (ArrayList<String> row : sheet)
			{
				Material obj = serviceFactory.getMaterialService().getByName(row.get(0));
				if (!Validate.validateObjectsNullOrEmpty(obj))
				{
					continue;
				}
				/*
				 * QueryParam queryParam = new QueryParam(); queryParam.setMaterialName(row.get(0)); SearchResult<Material>
				 * result = serviceFactory.getMaterialService ().findByCondition(queryParam); if (result.getCount() > 0) {
				 * continue; }
				 */
				Material material = new Material();
				material.setName(row.get(0));
				MaterialClass materialClass = serviceFactory.getMaterialClassService().getByName(row.get(1));
				if (materialClass == null)
				{
					throw new ServiceException("材料\"" + material.getName() + "\"的分类" + row.get(1) + "不存在！");
				}
				material.setMaterialClassId(materialClass.getId());
				material.setMaterialType(ObjectUtils.getMaterialType(row.get(2)));
				Unit valuationUnit = serviceFactory.getUnitService().getByName(row.get(3));
				if (valuationUnit == null)
				{
					throw new ServiceException("材料\"" + material.getName() + "\"的采购单位" + row.get(3) + "不存在！");
				}
				material.setValuationUnitId(valuationUnit.getId());
				Unit stockUnit = serviceFactory.getUnitService().getByName(row.get(4));
				if (stockUnit == null)
				{
					throw new ServiceException("材料\"" + material.getName() + "\"的库存单位" + row.get(4) + "不存在！");
				}
				material.setStockUnitId(stockUnit.getId());
				String weight = row.get(5);
				if (weight == null || "".equals(weight))
				{
					throw new ServiceException("材料\"" + material.getName() + "\"的克重值不是有效数字！");
				}
				material.setWeight(Integer.parseInt(weight));
				material.setLastPurchPrice(new BigDecimal(row.get(6)));
				material.setMinStockNum(new BigDecimal(row.get(7)));
				BoolValue boolValue = ObjectUtils.getBoolValue(row.get(8));
				if (boolValue == null)
				{
					throw new ServiceException("材料\"" + material.getName() + "\"的是否有效值异常");
				}
				material.setIsValid(boolValue);
				material.setCode(BasicType.MATERIAL.getCodePrefix() + new DecimalFormat("000000").format(codeIndex++));
				material.setCompanyId(UserUtils.getCompanyId());
				material.setIsValid(BoolValue.YES);
				material.setCreateName(UserUtils.getUser().getUserName());
				material.setCreateTime(new Date());
				daoFactory.getCommonDao().saveEntity(material);
				count++;
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			UserUtils.clearCacheBasic(BasicType.MATERIAL);
		}
		return count;
	}

	public double caculUnitPrice(Material material, int width, int length)
	{
		// 纸张面积平方米
		double area = (width / 1000d) * (length / 1000d);
		// 纸张重量克=纸张大小*每平方米克重
		double unit = area * material.getWeight();
		if ("吨".equals(material.getValuationUnitName()))
		{
			// 每克单价
			BigDecimal price = material.getLastPurchPrice().divide(new BigDecimal(1000000));
			return new BigDecimal(unit).multiply(price).doubleValue();
		}
		if ("千克".equals(material.getValuationUnitName()))
		{
			// 每克单价
			BigDecimal price = material.getLastPurchPrice().divide(new BigDecimal(1000));
			return new BigDecimal(unit).multiply(price).doubleValue();
		}
		if ("KG".equals(material.getValuationUnitName()))
		{
			// 每克单价
			BigDecimal price = material.getLastPurchPrice().divide(new BigDecimal(1000));
			return new BigDecimal(unit).multiply(price).doubleValue();
		}
		if ("令".equals(material.getValuationUnitName()))
		{
			BigDecimal price = material.getLastPurchPrice().divide(new BigDecimal(500));
			return price.doubleValue();
		}
		if ("张".equals(material.getValuationUnitName()))
		{
			return material.getLastPurchPrice().doubleValue();
		}
		if ("平方英寸".equals(material.getValuationUnitName()))
		{
			BigDecimal price = material.getLastPurchPrice().divide(new BigDecimal(0.0006452));
			return new BigDecimal(area).multiply(price).doubleValue();
		}
		if ("千平方英寸".equals(material.getValuationUnitName()))
		{
			BigDecimal price = material.getLastPurchPrice().divide(new BigDecimal(0.0006452)).divide(new BigDecimal(1000));
			return new BigDecimal(unit).multiply(price).doubleValue();
		}
		if ("平方米".equals(material.getValuationUnitName()))
		{
			BigDecimal price = material.getLastPurchPrice();
			return new BigDecimal(area).multiply(price).doubleValue();
		}
		return 0;
	}

	public static void main(String[] args)
	{
		double unit = (155 / 1000d) * (665 / 1000d) * 80;
		BigDecimal price = new BigDecimal(4000).divide(new BigDecimal(1000000));
		System.out.println(unit);
		System.out.println(price);
		System.out.println(new BigDecimal(unit).multiply(price).doubleValue());
	}
}
