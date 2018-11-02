/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月29日 上午9:30:23
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.huayin.common.exception.ServiceException;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.ConverterUtils;
import com.huayin.common.util.PropertyClone;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.i18n.ResourceBundleMessageSource;
import com.huayin.printmanager.i18n.service.BasicI18nResource;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.CustomerAddress;
import com.huayin.printmanager.persist.entity.basic.CustomerClass;
import com.huayin.printmanager.persist.entity.basic.CustomerPayer;
import com.huayin.printmanager.persist.entity.basic.DeliveryClass;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.PaymentClass;
import com.huayin.printmanager.persist.entity.basic.SettlementClass;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.service.basic.CustomerService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ExcelUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 客户信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月29日
 * @since        2.0, 2017年12月29日 下午17:07:00, think, 规范和国际化
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl implements CustomerService
{
	@Override
	public Customer get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Customer.class);
		query.eq("id", id);
		Customer c = daoFactory.getCommonDao().getByDynamicQuery(query, Customer.class);
		if (c != null)
		{
			List<CustomerAddress> addressList = this.getAddressList(c.getId());
			for (CustomerAddress address : addressList)
			{
				if (address.getIsDefault().equals(BoolValue.YES))
				{
					c.setDefaultAddress(address);
				}
			}
			List<CustomerPayer> payerList = this.getPayerList(c.getId());
			for (CustomerPayer payer : payerList)
			{
				if (payer.getIsDefault().equals(BoolValue.YES))
				{
					c.setDefaultPayer(payer);
				}
			}
			c.setAddressList(addressList);
			c.setPayerList(payerList);
		}
		return c;
	}

	@Override
	public Customer getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(Customer.class);
		query.eq("name", name);
		// query.eq("companyId", UserUtils.getCompanyId());
		return daoFactory.getCommonDao().getByDynamicQuery(query, Customer.class);
	}

	@Override
	public CustomerAddress getDefaultAddress(Long customerId)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerAddress.class);
		query.eq("customerId", customerId);
		query.eq("isDefault", BoolValue.YES);
		return daoFactory.getCommonDao().getByDynamicQuery(query, CustomerAddress.class);
	}

	@Override
	public CustomerPayer getDefaultPayer(Long customerId)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerPayer.class);
		query.eq("customerId", customerId);
		query.eq("isDefault", BoolValue.YES);
		return daoFactory.getCommonDao().getByDynamicQuery(query, CustomerPayer.class);
	}

	@Override
	public List<CustomerAddress> getAddressList(Long customerId)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerAddress.class);
		query.eq("customerId", customerId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, CustomerAddress.class);
	}

	@Override
	public List<CustomerPayer> getPayerList(Long customerId)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerPayer.class);
		query.eq("customerId", customerId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, CustomerPayer.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Customer save(Customer customer)
	{
		customer.setCompanyId(UserUtils.getCompanyId());
		if (customer.getCode() == null)
		{
			customer.setCode(serviceFactory.getCommonService().getNextCode(BasicType.CUSTOMER));
		}
		if (customer.getCurrencyType() == null)
		{
			customer.setCurrencyType(CurrencyType.RMB);
		}
		if (customer.getSettlementClassId() == null)
		{
			List<SettlementClass> settlementClassList = (List<SettlementClass>) (UserUtils.getBasicList(BasicType.SETTLEMENTCLASS.toString()));
			if (settlementClassList != null && settlementClassList.size() > 0)
			{
				customer.setSettlementClassId(settlementClassList.get(0).getId());
			}
		}
		if (customer.getTaxRateId() == null)
		{
			List<TaxRate> taxRateList = (List<TaxRate>) (UserUtils.getBasicList(BasicType.TAXRATE.toString()));
			if (taxRateList != null && taxRateList.size() > 0)
			{
				customer.setTaxRateId(taxRateList.get(0).getId());
			}
		}
		if (customer.getCustomerClassId() == null)
		{
			List<CustomerClass> customerClassList = (List<CustomerClass>) (UserUtils.getBasicList(BasicType.CUSTOMERCLASS.toString()));
			if (customerClassList != null && customerClassList.size() > 0)
			{
				customer.setCustomerClassId(customerClassList.get(0).getId());
			}
		}
		if (customer.getDeliveryClassId() == null)
		{
			List<DeliveryClass> deliveryClassList = (List<DeliveryClass>) (UserUtils.getBasicList(BasicType.DELIVERYCLASS.toString()));
			if (deliveryClassList != null && deliveryClassList.size() > 0)
			{
				customer.setDeliveryClassId(deliveryClassList.get(0).getId());
			}
		}
		if (customer.getPaymentClassId() == null)
		{
			List<PaymentClass> paymentClassList = (List<PaymentClass>) (UserUtils.getBasicList(BasicType.PAYMENTCLASS.toString()));
			if (paymentClassList != null && paymentClassList.size() > 0)
			{
				customer.setPaymentClassId(paymentClassList.get(0).getId());
			}
		}

		// 自动创建客户编号
		// customer.setCode(UserUtils.createBasicCode(BasicType.CUSTOMER));
		customer.setCreateName(UserUtils.getUserName());
		customer.setCreateTime(new Date());
		customer.setAdvanceMoney(new BigDecimal(0));
		daoFactory.getCommonDao().saveEntity(customer);
		// 保存地址
		if (null != customer.getAddressList() && customer.getAddressList().size() > 0)
		{
			boolean hasDefault = false;// 是否有默认值
			for (CustomerAddress address : customer.getAddressList())
			{
				address.setCompanyId(customer.getCompanyId());
				address.setCustomerId(customer.getId());
				if (hasDefault)
				{// 只允许一个默认值
					address.setIsDefault(BoolValue.NO);
				}
				if (address.getIsDefault() == BoolValue.YES)
				{
					hasDefault = true;
				}
			}
			if (!hasDefault)
			{// 如果没有默认值,则取第一条为默认值
				customer.getAddressList().get(0).setIsDefault(BoolValue.YES);
			}
			daoFactory.getCommonDao().saveAllEntity(customer.getAddressList());
		}
		// 保存付款单位
		if (null != customer.getPayerList() && customer.getPayerList().size() > 0)
		{
			boolean hasDefault = false;// 是否有默认值
			for (CustomerPayer payer : customer.getPayerList())
			{
				payer.setCompanyId(customer.getCompanyId());
				payer.setCustomerId(customer.getId());
				if (hasDefault)
				{// 只允许一个默认值
					payer.setIsDefault(BoolValue.NO);
				}
				if (payer.getIsDefault() == BoolValue.YES)
				{
					hasDefault = true;
				}
			}
			if (!hasDefault)
			{// 如果没有默认值,则取第一条为默认值
				customer.getPayerList().get(0).setIsDefault(BoolValue.YES);
			}
			daoFactory.getCommonDao().saveAllEntity(customer.getPayerList());
		}
		return customer;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Customer saveQuick(String name, String address, String userName, String mobile)
	{
		Customer customer = new Customer();
		// 客户分类
		List<CustomerClass> customerClassList = (List<CustomerClass>) UserUtils.getBasicList("CUSTOMERCLASS");
		if (null == customerClassList || customerClassList.size() == 0)
		{
			return null;
		}
		customer.setCustomerClassId(customerClassList.get(0).getId());
		// 销售员
		List<Employee> employeeList = (List<Employee>) UserUtils.getBasicList("EMPLOYEE");
		if (null == employeeList || employeeList.size() == 0)
		{
			return null;
		}
		customer.setEmployeeId(employeeList.get(0).getId());
		// 客户名称
		customer.setName(name);
		// 客户地址
		List<CustomerAddress> addressList = Lists.newArrayList();
		CustomerAddress customerAddress = new CustomerAddress();
		// 地址
		customerAddress.setAddress(address);
		// 用户名称
		customerAddress.setUserName(userName);
		// 手机号码
		customerAddress.setMobile(mobile);
		addressList.add(customerAddress);
		customer.setAddressList(addressList);

		Customer save = this.save(customer);

		// 清除缓存
		UserUtils.clearCacheBasic(BasicType.CUSTOMER);

		return save;
	}

	/*
	 * @Override
	 * @Transactional public void delete(Long id) { Customer customer = this.get(id); List<CustomerAddress> addressList =
	 * this.getAddressList(customer.getId()); List<CustomerPayer> payerList = this.getPayerList(customer.getId());
	 * daoFactory.getCommonDao().deleteEntity(customer); daoFactory.getCommonDao().deleteAllEntity(addressList);
	 * daoFactory.getCommonDao().deleteAllEntity(payerList); }
	 */

	@Override
	@Transactional
	public void deleteByIds(Long[] ids)
	{
		DynamicQuery query = new CompanyDynamicQuery(Customer.class);
		query.in("id", Arrays.asList(ids));
		List<Customer> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Customer.class);
		serviceFactory.getDaoFactory().getCommonDao().deleteAllEntity(list);
		// serviceFactory.getDaoFactory().getCommonDao().deleteByIds(Customer.class, (Object[])ids);
	}

	@Override
	@Transactional
	public Customer update(Customer customer)
	{
		Customer old_customer = this.get(customer.getId());
		Map<Long, CustomerAddress> old_address_map = ConverterUtils.list2Map(old_customer.getAddressList(), "id");

		List<Long> old_addressIds = new ArrayList<Long>();
		List<Long> new_addressIds = new ArrayList<Long>();

		List<CustomerAddress> add_address = new ArrayList<CustomerAddress>();
		List<CustomerAddress> update_address = new ArrayList<CustomerAddress>();
		List<CustomerAddress> del_address = new ArrayList<CustomerAddress>();

		for (CustomerAddress address : old_customer.getAddressList())
		{
			old_addressIds.add(address.getId());
		}
		for (CustomerAddress new_address : customer.getAddressList())
		{
			if (new_address.getId() != null)
			{
				new_addressIds.add(new_address.getId());
			}
		}

		if (null != customer.getAddressList() && customer.getAddressList().size() > 0)
		{
			boolean hasDefault = false;// 是否有默认值
			for (CustomerAddress address : customer.getAddressList())
			{
				address.setCompanyId(customer.getCompanyId());
				if (address.getId() == null)
				{
					if (hasDefault)
					{// 只允许一个默认值
						address.setIsDefault(BoolValue.NO);
					}
					if (address.getIsDefault() == BoolValue.YES)
					{
						hasDefault = true;
					}

					// 新增
					address.setCustomerId(customer.getId());
					add_address.add(address);
				}
				else
				{
					if (hasDefault)
					{// 只允许一个默认值
						address.setIsDefault(BoolValue.NO);
					}
					if (address.getIsDefault() == BoolValue.YES)
					{
						hasDefault = true;
					}
					// 更新
					if (old_addressIds.contains(address.getId()))
					{
						CustomerAddress old_address = old_address_map.get(address.getId());
						PropertyClone.copyProperties(old_address, address, false);// 替换成新内容
						update_address.add(old_address);
					}
				}
			}
			if (!hasDefault)
			{
				customer.getAddressList().get(0).setIsDefault(BoolValue.YES);
			}
		}

		// 删除操作
		for (Long id : old_addressIds)
		{
			if (!new_addressIds.contains(id))
			{
				CustomerAddress old_address = old_address_map.get(id);
				if (null != old_address)
				{
					del_address.add(old_address);
				}
			}
		}

		Map<Long, CustomerPayer> old_payer_map = ConverterUtils.list2Map(old_customer.getPayerList(), "id");

		List<Long> old_payerIds = new ArrayList<Long>();
		List<Long> new_payerIds = new ArrayList<Long>();

		List<CustomerPayer> add_payer = new ArrayList<CustomerPayer>();
		List<CustomerPayer> update_payer = new ArrayList<CustomerPayer>();
		List<CustomerPayer> del_payer = new ArrayList<CustomerPayer>();

		for (CustomerPayer payer : old_customer.getPayerList())
		{
			old_payerIds.add(payer.getId());
		}
		for (CustomerPayer new_payer : customer.getPayerList())
		{
			if (new_payer.getId() != null)
			{
				new_payerIds.add(new_payer.getId());
			}
		}

		if (null != customer.getPayerList() && customer.getPayerList().size() > 0)
		{
			boolean hasDefault = false;// 是否有默认值
			for (CustomerPayer payer : customer.getPayerList())
			{
				if (payer.getId() == null)
				{
					payer.setCompanyId(customer.getCompanyId());
					if (hasDefault)
					{// 只允许一个默认值
						payer.setIsDefault(BoolValue.NO);
					}
					if (payer.getIsDefault() == BoolValue.YES)
					{
						hasDefault = true;
					}
					// 新增
					payer.setCustomerId(customer.getId());
					add_payer.add(payer);
				}
				else
				{
					if (hasDefault)
					{// 只允许一个默认值
						payer.setIsDefault(BoolValue.NO);
					}
					if (payer.getIsDefault() == BoolValue.YES)
					{
						hasDefault = true;
					}
					// 更新
					if (old_payerIds.contains(payer.getId()))
					{
						CustomerPayer old_payer = old_payer_map.get(payer.getId());
						PropertyClone.copyProperties(old_payer, payer, false);// 替换成新内容
						update_payer.add(old_payer);
					}
				}
			}
			if (!hasDefault)
			{
				customer.getPayerList().get(0).setIsDefault(BoolValue.YES);
			}
		}

		// 删除操作
		for (Long id : old_payerIds)
		{
			if (!new_payerIds.contains(id))
			{
				CustomerPayer old_payer = old_payer_map.get(id);
				if (null != old_payer)
				{
					del_payer.add(old_payer);
				}
			}
		}
		daoFactory.getCommonDao().saveAllEntity(add_address);
		daoFactory.getCommonDao().saveAllEntity(add_payer);
		daoFactory.getCommonDao().deleteAllEntity(del_address);
		daoFactory.getCommonDao().deleteAllEntity(del_payer);
		daoFactory.getCommonDao().updateAllEntity(update_address);
		daoFactory.getCommonDao().updateAllEntity(update_payer);
		Customer c = daoFactory.getCommonDao().updateEntity(customer);
		return c;
	}

	@Override
	public List<Customer> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(Customer.class);
		// query.eq("companyId", UserUtils.getCompanyId());
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("employeeId", employes);
		}
		return daoFactory.getCommonDao().listAllEntity(Customer.class);
	}

	@Override
	public SearchResult<Customer> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Customer.class);
		query.setIsSearchTotalCount(true);
		// 业务权限
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("employeeId", employes);
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerName()))
		{
			query.like("name", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCode()))
		{
			query.like("code", "%" + queryParam.getCode() + "%");
		}
		if (queryParam.getId() != null)
		{
			query.eq("id", queryParam.getId());
		}

		// 代工平台
		if (queryParam.getIsOem() != null && BoolValue.YES == queryParam.getIsOem())
		{
			query.isNotNull("originCompanyId");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.eq("companyId", UserUtils.getCompanyId());

		query.desc("createTime");
		SearchResult<Customer> result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Customer.class);
		/*
		 * if (result.getResult().size() > 0) { for (Customer c : result.getResult()) { List<CustomerAddress> addressList =
		 * this.getAddressList(c.getId()); for (CustomerAddress address : addressList) { if
		 * (address.getIsDefault().equals(BoolValue.YES)) { c.setDefaultAddress(address); } } c.setAddressList(addressList);
		 * List<CustomerPayer> payerList = this.getPayerList(c.getId()); for (CustomerPayer payer : payerList) { if
		 * (payer.getIsDefault().equals(BoolValue.YES)) { c.setDefaultPayer(payer); } } c.setPayerList(payerList); } }
		 */
		return result;
	}

	@Override
	public SearchResult<Customer> quickFindByCondition(QueryParam queryParam)
	{
		/**
		 * 原方法由于性能问题（原因客户地址表where后面未加索引，但是又不能直接加索引，因为客户的地址可以为空的，加了索引则返回的结果集会少了），
		 * 不能合成一个sql查询，分开成两个sql，先查询客户表，再查询客户地址表
		 */
		SearchResult<Customer> result = new SearchResult<Customer>();
		List<Customer> customerList = new ArrayList<Customer>();
		result.setResult(customerList);

		DynamicQuery query = new CompanyDynamicQuery(Customer.class, "a");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("a.employeeId", employes);
		}
		//query.addProjection(Projections.property("a, b"));
		//query.createAlias(CustomerAddress.class, JoinType.LEFTJOIN, "b", "a.id=b.customerId and b.isDefault='YES' ");
		if (queryParam.getId() != null)
		{
			query.eq("a.id", queryParam.getId());
		}
		if (queryParam.getIsBegin() != null)
		{
			query.ne("a.isBegin", queryParam.getIsBegin());
		}
		query.eq("a.isValid", BoolValue.YES);

		query.desc("a.createTime");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getCustomerName()))
		{// 按客户名称查
			query.like("a.name", "%" + queryParam.getCustomerName() + "%");
		}
		else
		{// 按分类获取客户信息
			if (queryParam.getCustomerClassId() != null && queryParam.getCustomerClassId() != 0l)
			{
				query.eq("a.customerClassId", queryParam.getCustomerClassId());
			}
		}
		result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Customer.class);
		// 查询客户地址表
		DynamicQuery query2 = new CompanyDynamicQuery(CustomerAddress.class);
		query2.eq("companyId", UserUtils.getCompanyId());
		query2.eq("isDefault", BoolValue.YES); // 客户默认住址标识
		List<CustomerAddress> customerAddressList = daoFactory.getCommonDao().findEntityByDynamicQuery(query2, CustomerAddress.class);
		// 填充客户的默认地址信息
		for (Customer customer : result.getResult())
		{
			for (CustomerAddress cusd : customerAddressList)
			{
				if (customer.getId().longValue() == cusd.getCustomerId().longValue())
				{
					customer.setDefaultAddress(cusd);
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public Integer importFromExcel(InputStream inputStream) throws Exception
	{
		Integer count = 0;
		try
		{
			Workbook book = ExcelUtils.initBook(inputStream);
			List<ArrayList<String>> customerSheet = ExcelUtils.readXlsx(book, 0);
			List<ArrayList<String>> addressSheet = ExcelUtils.readXlsx(book, 1);
			List<ArrayList<String>> paymentSheet = ExcelUtils.readXlsx(book, 2);
			String code = serviceFactory.getCommonService().getNextCode(BasicType.CUSTOMER).replace(BasicType.CUSTOMER.getCodePrefix(), "");
			Integer codeIndex = Integer.parseInt(code);
			for (ArrayList<String> row : customerSheet)
			{
				Customer obj = serviceFactory.getCustomerService().getByName(row.get(0));
				if (!Validate.validateObjectsNullOrEmpty(obj))
				{
					continue;
				}
				/*
				 * QueryParam queryParam=new QueryParam (); queryParam.setCustomerName(row.get(0)); SearchResult<Customer>
				 * result=serviceFactory.getCustomerService().findByCondition(queryParam); if(result.getCount()>0){ continue; }
				 */
				Customer customer = new Customer();
				customer.setCreateName(UserUtils.getUserName());
				customer.setCreateTime(new Date());
				customer.setAdvanceMoney(new BigDecimal(0));
				customer.setMemo(row.get(9));
				customer.setName(row.get(0));
				BoolValue boolValue = ObjectUtils.getBoolValue(row.get(8));
				if (boolValue == null)
				{
					throw new ServiceException(ResourceBundleMessageSource.i18nFormatter(BasicI18nResource.CUSTOMER_VALIDATE_MSG2, customer.getName()));
				}
				customer.setIsValid(boolValue);
				Employee employee = serviceFactory.getEmployeeService().getByName(row.get(2));
				if (employee == null)
				{
					throw new ServiceException(ResourceBundleMessageSource.i18nFormatter(BasicI18nResource.CUSTOMER_VALIDATE_MSG3, customer.getName(), row.get(2)));
				}
				customer.setEmployeeId(employee.getId());
				customer.setCode(BasicType.CUSTOMER.getCodePrefix() + new DecimalFormat("000000").format(codeIndex++));
				customer.setCompanyId(UserUtils.getCompanyId());
				customer.setCurrencyType(ObjectUtils.getCurrencyType(row.get(3)));

				SettlementClass settlementClass = serviceFactory.getSettlementClassService().getByName(row.get(6));
				if (settlementClass == null)
				{
					List<SettlementClass> settlementClassList = (List<SettlementClass>) (UserUtils.getBasicList(BasicType.SETTLEMENTCLASS.toString()));
					if (settlementClassList != null && settlementClassList.size() > 0)
					{
						customer.setSettlementClassId(settlementClassList.get(0).getId());
					}
				}
				customer.setSettlementClassId(settlementClass.getId());

				TaxRate taxRate = serviceFactory.getTaxRateService().getByName(row.get(4));
				if (taxRate == null)
				{
					List<TaxRate> taxRateList = (List<TaxRate>) (UserUtils.getBasicList(BasicType.TAXRATE.toString()));
					if (taxRateList != null && taxRateList.size() > 0)
					{
						customer.setTaxRateId(taxRateList.get(0).getId());
					}
				}
				customer.setTaxRateId(taxRate.getId());

				CustomerClass customerClass = serviceFactory.getCustomerClassService().getByName(row.get(1));
				if (customerClass == null)
				{
					List<CustomerClass> customerClassList = (List<CustomerClass>) (UserUtils.getBasicList(BasicType.CUSTOMERCLASS.toString()));
					if (customerClassList != null && customerClassList.size() > 0)
					{
						customer.setCustomerClassId(customerClassList.get(0).getId());
					}
				}
				customer.setCustomerClassId(customerClass.getId());

				DeliveryClass deliveryClass = serviceFactory.getDeliveryClassService().getByName(row.get(7));
				if (deliveryClass == null)
				{
					List<DeliveryClass> deliveryClassList = (List<DeliveryClass>) (UserUtils.getBasicList(BasicType.DELIVERYCLASS.toString()));
					if (deliveryClassList != null && deliveryClassList.size() > 0)
					{
						customer.setDeliveryClassId(deliveryClassList.get(0).getId());
					}
				}
				customer.setDeliveryClassId(deliveryClass.getId());

				PaymentClass paymentClass = serviceFactory.getPaymentClassService().getByName(row.get(5));
				if (paymentClass == null)
				{
					List<PaymentClass> paymentClassList = (List<PaymentClass>) (UserUtils.getBasicList(BasicType.PAYMENTCLASS.toString()));
					if (paymentClassList != null && paymentClassList.size() > 0)
					{
						customer.setPaymentClassId(paymentClassList.get(0).getId());
					}
				}

				customer.setPaymentClassId(paymentClass.getId());
				daoFactory.getCommonDao().saveEntity(customer);
				// 保存地址
				for (ArrayList<String> addressList : addressSheet)
				{
					if (customer.getName().equals(addressList.get(0)))
					{
						CustomerAddress customerAddress = new CustomerAddress();
						customerAddress.setCompanyId(customer.getCompanyId());
						customerAddress.setCustomerId(customer.getId());
						customerAddress.setUserName(addressList.get(1));
						customerAddress.setAddress(addressList.get(2));
						customerAddress.setMobile(addressList.get(3));
						customerAddress.setEmail(addressList.get(4));
						customerAddress.setQq(addressList.get(5));
						customerAddress.setIsDefault(ObjectUtils.getBoolValue(addressList.get(6)));
						daoFactory.getCommonDao().saveEntity(customerAddress);
					}
				}
				// 保存付款单位
				for (ArrayList<String> paymentList : paymentSheet)
				{
					if (customer.getName().equals(paymentList.get(0)))
					{
						CustomerPayer customerPayer = new CustomerPayer();
						customerPayer.setCompanyId(customer.getCompanyId());
						customerPayer.setCustomerId(customer.getId());
						customerPayer.setIsDefault(ObjectUtils.getBoolValue(paymentList.get(2)));
						customerPayer.setName(paymentList.get(1));
						daoFactory.getCommonDao().saveEntity(customerPayer);
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
			UserUtils.clearCacheBasic(BasicType.CUSTOMER);
		}

		return count;
	}

	@Override
	public List<Long> findAllSaleEmployeeIds()
	{
		List<Long> ids = new ArrayList<Long>();
		DynamicQuery query = new CompanyDynamicQuery(Customer.class);
		// query.eq("companyId", UserUtils.getCompanyId());
		List<Customer> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Customer.class);
		for (Customer c : list)
		{
			if (ids.contains(c.getEmployeeId()))
			{
				continue;
			}
			if (c.getEmployeeId() == null)
			{
				continue;
			}
			ids.add(c.getEmployeeId());
		}
		return ids;
	}

	@Override
	public Long findIdByName(String customerName)
	{
		DynamicQuery query = new CompanyDynamicQuery(Customer.class);
		query.eq("name", customerName);
		List<Customer> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Customer.class);
		if (CollectionUtils.isNotEmpty(list))
		{
			return list.get(0).getId();
		}
		else
		{
			return null;
		}
	}

	@Override
	public List<Long> findIdsByName(String customerName)
	{
		List<Long> idList = Lists.newArrayList();
		if (StringUtils.isNotEmpty(customerName))
		{
			QueryParam queryParam = new QueryParam();
			queryParam.setCustomerName(customerName);
			SearchResult<Customer> customerList = serviceFactory.getCustomerService().findByCondition(queryParam);
			for (Customer c : customerList.getResult())
			{
				idList.add(c.getId());
			}
		}
		return idList;
	}
	
	@Override
	public List<Long> findIdsByName(String customerName, BoolValue isOem)
	{
		List<Long> idList = Lists.newArrayList();
		QueryParam queryParam = new QueryParam();
		queryParam.setCustomerName(customerName);
		queryParam.setIsOem(isOem);
		SearchResult<Customer> customerList = serviceFactory.getCustomerService().findByCondition(queryParam);
		for (Customer c : customerList.getResult())
		{
			idList.add(c.getId());
		}
		return idList;
	}

	@Override
	public List<Customer> findAll(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Customer.class);
		if (null != queryParam.getCustomerIdList())
		{
			query.in("id", queryParam.getCustomerIdList());
		}
		if (BoolValue.YES.equals(queryParam.getIsOem()))
		{
			query.add(Restrictions.isNotNull("originCompanyId"));
		}
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("employeeId", employes);
		}
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Customer.class);
	}
}
