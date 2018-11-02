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
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.huayin.common.exception.ServiceException;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.helper.service.CustomerHelper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.ProductClass;
import com.huayin.printmanager.persist.entity.basic.Product_Customer;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.begin.ProductBegin;
import com.huayin.printmanager.persist.entity.begin.ProductBeginDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.ProductType;
import com.huayin.printmanager.service.basic.ProductService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ExcelUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 产品信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月2日
 * @since        2.0, 2018年1月2日 下午17:07:00, think, 规范和国际化
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl implements ProductService
{

	@Override
	public Product get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Product.class);
		query.eq("id", id);
		Product product = daoFactory.getCommonDao().getByDynamicQuery(query, Product.class);
		List<Customer> customerList = new ArrayList<Customer>();
		if (null != product)
		{
			DynamicQuery customerQuery = new CompanyDynamicQuery(Product_Customer.class);
			customerQuery.eq("productId", product.getId());
			List<Product_Customer> list = daoFactory.getCommonDao().findEntityByDynamicQuery(customerQuery, Product_Customer.class);
			for (Product_Customer pc : list)
			{
				Customer customer = serviceFactory.getCustomerService().get(pc.getCustomerId());
				customerList.add(customer);
			}
		}
		if (null != customerList && customerList.size() > 0)
		{
			product.setCustomerList(customerList);
		}
		return product;
	}

	@Override
	public Product getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(Product.class);
		query.eq("name", name);
		query.desc("createTime");
		// query.eq("companyId", UserUtils.getCompanyId());
		return daoFactory.getCommonDao().getByDynamicQuery(query, Product.class);
	}

	@Override
	public Product getByCustomerMaterialCode(String customerMaterialCode)
	{
		DynamicQuery query = new CompanyDynamicQuery(Product.class);
		query.eq("customerMaterialCode", customerMaterialCode);
		// query.eq("companyId", UserUtils.getCompanyId());
		return daoFactory.getCommonDao().getByDynamicQuery(query, Product.class);
	}

	@Override
	@Transactional
	public Product save(Product product)
	{
		product.setCompanyId(UserUtils.getCompanyId());
		if (product.getCode() == null)
		{
			product.setCode(serviceFactory.getCommonService().getNextCode(BasicType.PRODUCT));
		}
		if (product.getIsPublic() == null)
		{
			product.setIsPublic(BoolValue.NO);
		}
		if (product.getIsValid() == null)
		{
			product.setIsValid(BoolValue.NO);
		}
		if (product.getSort() == null)
		{
			product.setSort(serviceFactory.getCommonService().getNextSort(BasicType.PRODUCT));
		}
		product.setCreateName(UserUtils.getUserName());
		product.setCreateTime(new Date());

		Product p = daoFactory.getCommonDao().saveEntity(product);
		List<Product_Customer> list = new ArrayList<Product_Customer>();
		if (null != product.getCustomerList() && product.getCustomerList().size() > 0)
		{
			for (Customer customer : product.getCustomerList())
			{
				if (customer.getId() != null)
				{
					Product_Customer pc = new Product_Customer();
					pc.setCustomerId(customer.getId());
					pc.setProductId(product.getId());
					pc.setCompanyId(product.getCompanyId());
					list.add(pc);
				}
			}
		}

		if (null != list && list.size() > 0)
		{
			daoFactory.getCommonDao().saveAllEntity(list);
		}
		return p;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Product saveQuick(Long customerId, String name, String customerMaterialCode, String specifications)
	{
		// 产品分类（默认选第一个）
		List<ProductClass> productClassList = (List<ProductClass>) UserUtils.getBasicList("PRODUCTCLASS");
		if (null == productClassList || productClassList.size() == 0)
		{
			return null;
		}
		return this.saveQuick(productClassList.get(0).getId(), customerId, name, customerMaterialCode, specifications);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Product saveQuick(Long productClassId, Long customerId, String name, String customerMaterialCode, String specifications)
	{
		Product product = new Product();
		// 客户信息
		List<Customer> customerList = Lists.newArrayList();
		Customer customer = new Customer();
		customer.setId(customerId);
		customerList.add(customer);
		product.setCustomerList(customerList);
		// 产品分类
		product.setProductClassId(productClassId);
		// 产品名称
		product.setName(name);
		// 客户料号
		product.setCustomerMaterialCode(customerMaterialCode);
		// 产品规格
		product.setSpecifications(specifications);
		// 单位
		List<Unit> unitList = (List<Unit>) UserUtils.getBasicList("UNIT");
		if (null == unitList || unitList.size() == 0)
		{
			return null;
		}
		product.setUnitId(unitList.get(0).getId());

		return this.save(product);
	}

	@Override
	@Transactional
	public Product_Customer appendProductCustomer(Long productId, Long customerId)
	{
		Product_Customer pc = new Product_Customer();
		pc.setCustomerId(customerId);
		pc.setProductId(productId);
		pc.setCompanyId(UserUtils.getCompanyId());

		return daoFactory.getCommonDao().saveEntity(pc);
	}

	@Override
	@Transactional
	public Product update(Product product)
	{
		Product old_product = this.get(product.getId());

		List<Long> old_customerIds = new ArrayList<Long>();
		List<Long> new_customerIds = new ArrayList<Long>();

		List<Product_Customer> del_pc = new ArrayList<Product_Customer>();
		List<Product_Customer> add_pc = new ArrayList<Product_Customer>();

		if (null != old_product.getCustomerList() && old_product.getCustomerList().size() > 0)
		{
			for (Customer customer : old_product.getCustomerList())
			{
				old_customerIds.add(customer.getId());
			}
		}
		if (null != product.getCustomerList() && product.getCustomerList().size() > 0)
		{
			for (Customer new_customer : product.getCustomerList())
			{
				if (new_customer.getId() != null)
				{
					new_customerIds.add(new_customer.getId());
				}
			}
		}
		// 删除
		for (Long id : old_customerIds)
		{
			if (!new_customerIds.contains(id))
			{
				DynamicQuery customerQuery = new CompanyDynamicQuery(Product_Customer.class);
				customerQuery.eq("customerId", id);
				customerQuery.eq("productId", product.getId());
				Product_Customer old_pc = daoFactory.getCommonDao().getByDynamicQuery(customerQuery, Product_Customer.class);
				if (null != old_pc)
				{
					del_pc.add(old_pc);
				}
			}
		}
		if (null != del_pc)
		{
			daoFactory.getCommonDao().deleteAllEntity(del_pc);
		}
		// 新增
		for (Long id : new_customerIds)
		{
			if (!old_customerIds.contains(id))
			{
				Product_Customer new_pc = new Product_Customer();
				new_pc.setCustomerId(id);
				new_pc.setProductId(product.getId());
				new_pc.setCompanyId(product.getCompanyId());
				add_pc.add(new_pc);
			}
		}
		if (null != add_pc)
		{
			daoFactory.getCommonDao().saveAllEntity(add_pc);
		}
		return daoFactory.getCommonDao().updateEntity(product);
	}

	/*
	 * @Override
	 * @Transactional public void delete(Long id) { daoFactory.getCommonDao().deleteEntity(Product.class, id); }
	 */

	@Override
	@Transactional
	public void deleteByIds(Long[] ids)
	{
		DynamicQuery query = new CompanyDynamicQuery(Product.class);
		query.in("id", Arrays.asList(ids));
		List<Product> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Product.class);
		serviceFactory.getDaoFactory().getCommonDao().deleteAllEntity(list);
	}

	@Override
	@Transactional
	public void deleteProductCustomerById(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Product_Customer.class);
		query.eq("productId", id);
		List<Product_Customer> pcList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Product_Customer.class);
		if (pcList.size() > 0)
			daoFactory.getCommonDao().deleteAllEntity(pcList);
	}

	@Override
	@Transactional
	public void deleteProductCustomerByIds(Long[] ids)
	{
		DynamicQuery query = new CompanyDynamicQuery(Product_Customer.class);
		query.in("productId", Arrays.asList(ids));
		List<Product_Customer> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Product_Customer.class);
		if (list.size() > 0)
			serviceFactory.getDaoFactory().getCommonDao().deleteAllEntity(list);
	}

	@Override
	public List<Product> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(Product.class);
		// query.eq("companyId", UserUtils.getCompanyId());
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Product.class);
	}

	@Override
	public SearchResult<Product> findByCondition(QueryParam queryParam)
	{
		boolean hasCustomer = false;
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			hasCustomer = true;
		}
		DynamicQuery query = new CompanyDynamicQuery(Product.class,"p");
		query.setIsSearchTotalCount(true);
		if (hasCustomer)
		{
			query.createAlias(Product_Customer.class, "pc");
			query.eqProperty("p.id", "pc.productId");
			CustomerHelper.transferToIds(queryParam);// 把客户名称转为id
			query.in("pc.customerId",queryParam.getCustomerIdList());
		}
		if (StringUtils.isNotEmpty(queryParam.getProductName()))
		{
			query.like("p.name", "%" + queryParam.getProductName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getProductCode()))
		{
			query.like("p.code", "%" + queryParam.getProductCode() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getProductStyle()))
		{
			query.like("p.specifications", "%" + queryParam.getProductStyle() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("p.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (null != queryParam.getIds() && queryParam.getIds().size() > 0)
		{
			query.in("p.id", queryParam.getIds());
		}
		
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		// query.eq("companyId", UserUtils.getCompanyId());
		query.desc("p.createTime");
		if (hasCustomer)
		{
			SearchResult<Product> result = new SearchResult<Product>();
			List<Product> productList = new ArrayList<Product>();
			result.setResult(productList);
			SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
			for (Object[] c : temp_result.getResult())
			{
				Product product = (Product) c[0];
				productList.add(product);
			}
			result.setCount(temp_result.getCount());
			return result;
		} else
		{
			return daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Product.class);
		}
	}

	@Override
	public SearchResult<Product> quickFindByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Product.class, "p");
		query.addProjection(Projections.property("p, pcla"));
		query.createAlias(ProductClass.class, JoinType.LEFTJOIN, "pcla", "pcla.id=p.productClassId");
		if (queryParam.getIsBegin() == BoolValue.YES)
		{
			DynamicQuery begin_query = new CompanyDynamicQuery(ProductBeginDetail.class, "b");
			begin_query.createAlias(ProductBegin.class, JoinType.INNERJOIN, "c", "c.id=b.masterId");
			begin_query.eq("c.warehouseId", queryParam.getWarehouseId());
			begin_query.eqProperty("p.id", "b.productId");
			query.notExists(begin_query);
		}
		query.eq("p.isValid", BoolValue.YES);
		if (queryParam.getProductType() != null && ProductType.ROTARY != queryParam.getProductType())
		{
			query.eq("pcla.productType", queryParam.getProductType());
		}
		if (queryParam.getProductClassId() != null && queryParam.getProductClassId() != -1l)
		{
			query.eq("p.productClassId", queryParam.getProductClassId());
		}
		if (queryParam.getCustomerMaterialCode() != null && queryParam.getCustomerMaterialCode() != "")
		{
			query.like("p.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (queryParam.getProductStyle() != null && queryParam.getProductStyle() != "")
		{
			query.like("p.specifications", "%" + queryParam.getProductStyle() + "%");
		}
		if (queryParam.getCustomerId() != null && queryParam.getCustomerId() != 0)
		{
			DynamicQuery sub_query = new CompanyDynamicQuery(Product_Customer.class, "pcus");
			sub_query.eq("pcus.customerId", queryParam.getCustomerId());
			sub_query.eqProperty("pcus.productId", "p.id");
			query.add(Restrictions.or(Restrictions.exists(sub_query), Restrictions.eq("p.isPublic", BoolValue.YES)));
		}
		if (StringUtils.isNotEmpty(queryParam.getProductName()))
		{
			query.like("p.name", "%" + queryParam.getProductName() + "%");
		}

		// query.eq("p.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());

		query.asc("p.sort");
		query.desc("p.createTime");
		query.setIsSearchTotalCount(true);
		// query.setQueryType(QueryType.JDBC);
		// return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Product.class);

		SearchResult<Product> result = new SearchResult<Product>();
		List<Product> productList = new ArrayList<Product>();
		result.setResult(productList);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		for (Object[] c : temp_result.getResult())
		{
			Product product = (Product) c[0];
			product.setProductClass((ProductClass) c[1]);
			productList.add(product);
		}
		result.setCount(temp_result.getCount());
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
			List<ArrayList<String>> customerSheet = ExcelUtils.readXlsx(book, 1);
			String code = serviceFactory.getCommonService().getNextCode(BasicType.PRODUCT).replace(BasicType.PRODUCT.getCodePrefix(), "");
			Integer codeIndex = Integer.parseInt(code);
			Integer sortIndex = serviceFactory.getCommonService().getNextSort(BasicType.PRODUCT);
			for (ArrayList<String> row : sheet)
			{
				Product obj = serviceFactory.getProductService().getByName(row.get(0));
				if (!Validate.validateObjectsNullOrEmpty(obj))
				{
					continue;
				}
				// 成品名称为空
				if ("".equals(row.get(0).trim()))
				{
					continue;
				}
				/*
				 * QueryParam queryParam=new QueryParam (); queryParam.setProductName(row.get(0)); SearchResult<Product>
				 * result=serviceFactory.getProductService().findByCondition(queryParam); if(result.getCount()>0){ continue; }
				 */
				Product product = new Product();

				product.setName(row.get(0));
				product.setSpecifications(row.get(1));
				ProductClass productClass = serviceFactory.getProductClassService().getByName(row.get(2));
				if (productClass != null)
				{
					product.setProductClassId(productClass.getId());
				}
				product.setCustomerMaterialCode(row.get(3));
				Unit unit = serviceFactory.getUnitService().getByName(row.get(4));
				if (unit == null)
				{
					throw new ServiceException("产品\"" + product.getName() + "\"的单位" + row.get(4) + "不存在！");
				}
				product.setUnitId(unit.getId());
				product.setSalePrice(new BigDecimal(row.get(5)));
				product.setMemo(row.get(8));
				product.setCompanyId(UserUtils.getCompanyId());
				product.setCode(BasicType.PRODUCT.getCodePrefix() + new DecimalFormat("000000").format(codeIndex++));
				product.setIsPublic(ObjectUtils.getBoolValue(row.get(6)));
				product.setIsValid(ObjectUtils.getBoolValue(row.get(7)));
				product.setSort(sortIndex++);
				product.setCreateName(UserUtils.getUserName());
				product.setCreateTime(new Date());
				daoFactory.getCommonDao().saveEntity(product);
				for (ArrayList<String> customerList : customerSheet)
				{
					if (product.getName().equals(customerList.get(0)))
					{
						Customer customer = serviceFactory.getCustomerService().getByName(customerList.get(2));
						if (customer != null)
						{
							Product_Customer pc = new Product_Customer();
							pc.setCustomerId(customer.getId());
							pc.setProductId(product.getId());
							pc.setCompanyId(product.getCompanyId());
							daoFactory.getCommonDao().saveEntity(pc);
						}
					}
				}
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
			UserUtils.clearCacheBasic(BasicType.PRODUCT);
		}
		return count;
	}

	@Override
	public Product_Customer findCustomerById(Long productId, Long customerId)
	{
		DynamicQuery query = new CompanyDynamicQuery(Product_Customer.class);
		query.eq("productId", productId);
		query.eq("customerId", customerId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Product_Customer.class);
	}

	@Override
	@Transactional
	public void updatePrice(Map<Long, BigDecimal> map)
	{
		List<Long> idList = Lists.newArrayList();
		idList.addAll(map.keySet());
		// 设参
		QueryParam queryParam = new QueryParam();
	  queryParam.setIds(idList);
	  // 根据产品id查找出所有产品
	  SearchResult<Product> result = findByCondition(queryParam);
	  
	  List<Product> list = Lists.newArrayList();
	  for (Product product : result.getResult())
	  {
	  	BigDecimal price = map.get(product.getId());// 根据产品id获取map里面的值
	  	// 优化性能，价格有变化的产品才修改数据库
	  	if (null == product.getSalePrice() || product.getSalePrice().compareTo(price) != 0)
	  	{
	  		product.setSalePrice(price);
	  		list.add(product);
	  	}
	  }
	  // 修改
	  daoFactory.getCommonDao().updateAllEntity(list);
	}
}
