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
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.util.ConverterUtils;
import com.huayin.common.util.PropertyClone;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.i18n.ResourceBundleMessageSource;
import com.huayin.printmanager.i18n.service.BasicI18nResource;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.DeliveryClass;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.ExchangeRate;
import com.huayin.printmanager.persist.entity.basic.PaymentClass;
import com.huayin.printmanager.persist.entity.basic.SettlementClass;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.SupplierAddress;
import com.huayin.printmanager.persist.entity.basic.SupplierClass;
import com.huayin.printmanager.persist.entity.basic.SupplierPayer;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.SupplierType;
import com.huayin.printmanager.service.basic.SupplierService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ExcelUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 供应商信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月29日
 * @since        2.0, 2017年12月29日 下午17:07:00, think, 规范和国际化
 */
@Service
public class SupplierServiceImpl extends BaseServiceImpl implements SupplierService
{
	@Override
	public Supplier get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Supplier.class);
		query.eq("id", id);
		Supplier s = daoFactory.getCommonDao().getByDynamicQuery(query, Supplier.class);
		if (s != null)
		{
			List<SupplierAddress> addressList = this.getAddressList(s.getId());
			s.setAddressList(addressList);
			for (SupplierAddress address : addressList)
			{
				if (address.getIsDefault() == BoolValue.YES)
				{
					s.setDefaultAddress(address);
				}
			}

			List<SupplierPayer> payerList = this.getPayerList(s.getId());
			s.setPayerList(payerList);
			for (SupplierPayer payer : payerList)
			{
				if (payer.getIsDefault() == BoolValue.YES)
				{
					s.setDefaultPayer(payer);
				}
			}
		}
		return s;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Supplier save(Supplier supplier)
	{
		supplier.setCompanyId(UserUtils.getCompanyId());
		if (supplier.getCode() == null)
		{
			supplier.setCode(serviceFactory.getCommonService().getNextCode(BasicType.SUPPLIER));
		}
		supplier.setAdvanceMoney(new BigDecimal(0));

		if (supplier.getType() == null)
		{
			supplier.setType(SupplierType.MATERIAL);
		}
		if (supplier.getSupplierClassId() == null)
		{
			List<SupplierClass> supplierClassList = (List<SupplierClass>) (UserUtils.getBasicList(BasicType.SUPPLIERCLASS.toString()));
			if (supplierClassList != null && supplierClassList.size() > 0)
			{
				supplier.setSupplierClassId(supplierClassList.get(0).getId());
			}
		}
		if (supplier.getDeliveryClassId() == null)
		{
			List<DeliveryClass> deliveryClassList = (List<DeliveryClass>) (UserUtils.getBasicList(BasicType.DELIVERYCLASS.toString()));
			if (deliveryClassList != null && deliveryClassList.size() > 0)
			{
				supplier.setDeliveryClassId(deliveryClassList.get(0).getId());
			}
		}
		if (supplier.getPaymentClassId() == null)
		{
			List<PaymentClass> paymentClassList = (List<PaymentClass>) (UserUtils.getBasicList(BasicType.PAYMENTCLASS.toString()));
			if (paymentClassList != null && paymentClassList.size() > 0)
			{
				supplier.setPaymentClassId(paymentClassList.get(0).getId());
			}
		}
		if (supplier.getSettlementClassId() == null)
		{
			List<SettlementClass> settlementClassList = (List<SettlementClass>) (UserUtils.getBasicList(BasicType.SETTLEMENTCLASS.toString()));
			if (settlementClassList != null && settlementClassList.size() > 0)
			{
				supplier.setSettlementClassId(settlementClassList.get(0).getId());
			}
		}
		if (supplier.getTaxRateId() == null)
		{
			List<TaxRate> taxRateList = (List<TaxRate>) (UserUtils.getBasicList(BasicType.TAXRATE.toString()));
			if (taxRateList != null && taxRateList.size() > 0)
			{
				supplier.setTaxRateId(taxRateList.get(0).getId());
			}
		}
		if (supplier.getExchangeRateId() == null)
		{
			List<ExchangeRate> exchangeRateList = (List<ExchangeRate>) (UserUtils.getBasicList(BasicType.EXCHANGERATE.toString()));
			if (exchangeRateList != null && exchangeRateList.size() > 0)
			{
				supplier.setExchangeRateId(exchangeRateList.get(0).getId());
			}
		}
		if (supplier.getRegisteredCapital() == null)
		{
			supplier.setRegisteredCapital(0);
		}
		// 自动创建客户编号
		// supplier.setCode(UserUtils.createBasicCode(BasicType.SUPPLIER));
		supplier.setCreateName(UserUtils.getUserName());
		supplier.setCreateTime(new Date());

		daoFactory.getCommonDao().saveEntity(supplier);

		// 保存地址
		if (null != supplier.getAddressList() && supplier.getAddressList().size() > 0)
		{
			boolean hasDefault = false;// 是否有默认值
			for (SupplierAddress address : supplier.getAddressList())
			{
				address.setCompanyId(supplier.getCompanyId());
				address.setSupplierId(supplier.getId());
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
				supplier.getAddressList().get(0).setIsDefault(BoolValue.YES);
			}
			daoFactory.getCommonDao().saveAllEntity(supplier.getAddressList());
		}

		// 保存付款单位
		if (null != supplier.getPayerList() && supplier.getPayerList().size() > 0)
		{
			boolean hasDefault = false;// 是否有默认值
			for (SupplierPayer payer : supplier.getPayerList())
			{
				payer.setCompanyId(supplier.getCompanyId());
				payer.setSupplierId(supplier.getId());
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
				supplier.getPayerList().get(0).setIsDefault(BoolValue.YES);
			}
			daoFactory.getCommonDao().saveAllEntity(supplier.getPayerList());
		}
		return supplier;
	}

	@Override
	@Transactional
	public Supplier update(Supplier supplier)
	{
		Supplier old_supplier = this.get(supplier.getId());
		// 操作地址
		Map<Long, SupplierAddress> old_address_map = ConverterUtils.list2Map(old_supplier.getAddressList(), "id");

		List<Long> old_addressIds = new ArrayList<Long>();
		List<Long> new_addressIds = new ArrayList<Long>();

		List<SupplierAddress> del_address = new ArrayList<SupplierAddress>();

		for (SupplierAddress address : old_supplier.getAddressList())
		{
			old_addressIds.add(address.getId());
		}
		for (SupplierAddress new_address : supplier.getAddressList())
		{
			if (new_address.getId() != null)
			{
				new_addressIds.add(new_address.getId());
			}
		}
		// 更新地址
		if (null != supplier.getAddressList() && supplier.getAddressList().size() > 0)
		{
			boolean hasDefault = false;// 是否有默认值
			for (SupplierAddress address : supplier.getAddressList())
			{
				if (address.getId() == null)
				{
					address.setCompanyId(supplier.getCompanyId());
					address.setSupplierId(supplier.getId());
					if (hasDefault)
					{// 只允许一个默认值
						address.setIsDefault(BoolValue.NO);
					}
					if (address.getIsDefault() == BoolValue.YES)
					{
						hasDefault = true;
					}
					// 新增
					daoFactory.getCommonDao().saveEntity(address);
				}
				else
				{
					// 更新
					if (hasDefault)
					{// 只允许一个默认值
						address.setIsDefault(BoolValue.NO);
					}
					if (address.getIsDefault() == BoolValue.YES)
					{
						hasDefault = true;
					}
					if (old_addressIds.contains(address.getId()))
					{
						SupplierAddress old_address = old_address_map.get(address.getId());

						PropertyClone.copyProperties(old_address, address, false);// 替换成新内容
						daoFactory.getCommonDao().updateEntity(old_address);
					}
				}
			}
		}

		// 删除操作
		for (Long id : old_addressIds)
		{
			if (!new_addressIds.contains(id))
			{
				SupplierAddress old_address = old_address_map.get(id);
				if (null != old_address)
				{
					del_address.add(old_address);
				}
			}
		}
		if (del_address.size() > 0)
		{
			daoFactory.getCommonDao().deleteAllEntity(del_address);
		}

		// 操作付款单位
		Map<Long, SupplierPayer> old_payer_map = ConverterUtils.list2Map(old_supplier.getPayerList(), "id");

		List<Long> old_payerIds = new ArrayList<Long>();
		List<Long> new_payerIds = new ArrayList<Long>();

		List<SupplierPayer> del_payer = new ArrayList<SupplierPayer>();

		for (SupplierPayer payer : old_supplier.getPayerList())
		{
			old_payerIds.add(payer.getId());
		}
		for (SupplierPayer new_payer : supplier.getPayerList())
		{
			if (new_payer.getId() != null)
			{
				new_payerIds.add(new_payer.getId());
			}
		}

		// 更新付款单位
		if (null != supplier.getPayerList() && supplier.getPayerList().size() > 0)
		{
			boolean hasDefault = false;// 是否有默认值
			for (SupplierPayer payer : supplier.getPayerList())
			{
				payer.setCompanyId(supplier.getCompanyId());
				payer.setSupplierId(supplier.getId());
				if (payer.getId() == null)
				{
					if (hasDefault)
					{// 只允许一个默认值
						payer.setIsDefault(BoolValue.NO);
					}
					if (payer.getIsDefault() == BoolValue.YES)
					{
						hasDefault = true;
					}
					daoFactory.getCommonDao().saveEntity(payer);
				}
				else
				{
					// 更新
					if (hasDefault)
					{// 只允许一个默认值
						payer.setIsDefault(BoolValue.NO);
					}
					if (payer.getIsDefault() == BoolValue.YES)
					{
						hasDefault = true;
					}
					if (old_payerIds.contains(payer.getId()))
					{
						SupplierPayer old_payer = old_payer_map.get(payer.getId());

						PropertyClone.copyProperties(old_payer, payer, false);// 替换成新内容
						daoFactory.getCommonDao().updateEntity(payer);
					}
				}
			}
		}
		// 删除操作
		for (Long id : old_payerIds)
		{
			if (!new_payerIds.contains(id))
			{
				SupplierPayer old_payer = old_payer_map.get(id);
				if (null != old_payer)
				{
					del_payer.add(old_payer);
				}
			}
		}
		if (null != del_payer)
		{
			daoFactory.getCommonDao().deleteAllEntity(del_payer);
		}
		Supplier s = daoFactory.getCommonDao().updateEntity(supplier);
		return s;

	}

	/*
	 * @Override
	 * @Transactional public void delete(Long id) { daoFactory.getCommonDao().deleteEntity(Supplier.class, id); }
	 */

	@Override
	@Transactional
	public void deleteByIds(Long[] ids)
	{
		DynamicQuery query = new CompanyDynamicQuery(Supplier.class);
		query.in("id", Arrays.asList(ids));
		List<Supplier> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Supplier.class);
		serviceFactory.getDaoFactory().getCommonDao().deleteAllEntity(list);
		// serviceFactory.getDaoFactory().getCommonDao().deleteByIds(Supplier.class, (Object[])ids);
	}

	@Override
	public List<SupplierAddress> getAddressList(Long supplierId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierAddress.class);
		query.eq("supplierId", supplierId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, SupplierAddress.class);
	}

	@Override
	public SupplierAddress getDefaultAddress(Long supplierId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierAddress.class);
		query.eq("supplierId", supplierId);
		query.eq("isDefault", BoolValue.YES);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SupplierAddress.class);
	}

	@Override
	public SupplierPayer getDefaultPayer(Long supplierId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierPayer.class);
		query.eq("supplierId", supplierId);
		query.eq("isDefault", BoolValue.YES);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SupplierPayer.class);
	}

	@Override
	public List<SupplierPayer> getPayerList(Long supplierId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierPayer.class);
		query.eq("supplierId", supplierId);
		query.asc("id");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, SupplierPayer.class);
	}

	@Override
	public Supplier getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(Supplier.class);
		query.eq("name", name);
		// query.eq("companyId", UserUtils.getCompanyId());
		return daoFactory.getCommonDao().getByDynamicQuery(query, Supplier.class);
	}

	@Override
	public List<Supplier> findAll(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Supplier.class);
		if (null != queryParam.getSupplierIdList())
		{
			query.in("id", queryParam.getSupplierIdList());
		}
		if (null != queryParam.getSupplierType())
		{
			query.eq("type", queryParam.getSupplierType());
		}
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("employeeId", employes);
		}
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Supplier.class);
	}

	@Override
	public SearchResult<Supplier> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Supplier.class);
		query.setIsSearchTotalCount(true);
		// 业务权限
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("employeeId", employes);
		}
		// 供应商名称
		if (StringUtils.isNotEmpty(queryParam.getSupplierName()))
		{
			query.like("name", "%" + queryParam.getSupplierName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCode()))
		{
			query.like("code", "%" + queryParam.getCode() + "%");
		}
		// 代工平台
		if (queryParam.getIsOem() != null && BoolValue.YES == queryParam.getIsOem())
		{
			query.isNotNull("originCompanyId");
		}
		if (queryParam.getId() != null)
		{
			query.eq("id", queryParam.getId());
		}
		if (null != queryParam.getSupplierType())
		{
			query.eq("type", queryParam.getSupplierType());
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.eq("companyId", UserUtils.getCompanyId());
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Supplier.class);
	}

	@Override
	public SearchResult<Supplier> quickFindByCondition(QueryParam queryParam)
	{
		SearchResult<Supplier> result = new SearchResult<Supplier>();
		List<Supplier> supplierList = new ArrayList<Supplier>();
		result.setResult(supplierList);
		SearchResult<Object[]> temp_result = new SearchResult<Object[]>();

		DynamicQuery query = new CompanyDynamicQuery(Supplier.class, "a");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("a.employeeId", employes);
		}
		query.addProjection(Projections.property("a, b"));

		query.createAlias(SupplierAddress.class, JoinType.LEFTJOIN, "b", "a.id=b.supplierId and b.isDefault='YES'");

		if (queryParam.getId() != null)
		{
			query.eq("a.id", queryParam.getId());
		}
		if (queryParam.getSupplierType() != null)
		{
			if (queryParam.getSupplierType() == SupplierType.MATERIAL)
			{// 材料和综合
				query.in("a.type", SupplierType.MATERIAL, SupplierType.MATERIAL_AND_PROCESS);
			}
			else if (queryParam.getSupplierType() == SupplierType.PROCESS)
			{// 加工和综合
				query.in("a.type", SupplierType.PROCESS, SupplierType.MATERIAL_AND_PROCESS);
			}
			else if (queryParam.getSupplierType() == SupplierType.MATERIAL_AND_PROCESS)
			{// 综合
				query.eq("a.type", SupplierType.MATERIAL_AND_PROCESS);
			}
		}
		if (queryParam.getIsBegin() != null)
		{
			query.ne("a.isBegin", queryParam.getIsBegin());
		}
		if (queryParam.getSupplierId() != null)
		{
			query.eq("b.supplierId", queryParam.getSupplierId());
		}

		query.eq("a.isValid", BoolValue.YES);
		// query.eq("a.companyId", UserUtils.getCompanyId());
		query.desc("a.createTime");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);

		if (StringUtils.isNotEmpty(queryParam.getSupplierName()))
		{// 按名称查
			query.like("a.name", "%" + queryParam.getSupplierName() + "%");
		}
		else
		{// 按分类获取信息
			if (queryParam.getSupplierClassId() != null && queryParam.getSupplierClassId() != 0l)
			{
				query.eq("a.supplierClassId", queryParam.getSupplierClassId());
			}
		}
		temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		for (Object[] c : temp_result.getResult())
		{
			Supplier supplier = (Supplier) c[0];
			supplier.setDefaultAddress((SupplierAddress) c[1]);
			supplierList.add(supplier);
		}
		result.setCount(temp_result.getCount());
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
			List<ArrayList<String>> supplierSheet = ExcelUtils.readXlsx(book, 0);
			List<ArrayList<String>> addressSheet = ExcelUtils.readXlsx(book, 1);
			List<ArrayList<String>> paymentSheet = ExcelUtils.readXlsx(book, 2);
			String code = serviceFactory.getCommonService().getNextCode(BasicType.SUPPLIER).replace(BasicType.SUPPLIER.getCodePrefix(), "");
			Integer codeIndex = Integer.parseInt(code);
			for (ArrayList<String> row : supplierSheet)
			{
				Supplier obj = serviceFactory.getSupplierService().getByName(row.get(0));
				if (!Validate.validateObjectsNullOrEmpty(obj))
				{
					continue;
				}
				/*
				 * QueryParam queryParam=new QueryParam (); queryParam.setSupplierName(row.get(0)); SearchResult<Supplier>
				 * result=serviceFactory.getSupplierService().findByCondition(queryParam); if(result.getCount()>0){ continue; }
				 */
				Supplier supplier = new Supplier();
				supplier.setCompanyId(UserUtils.getCompanyId());
				supplier.setCode(BasicType.SUPPLIER.getCodePrefix() + new DecimalFormat("000000").format(codeIndex++));
				supplier.setAdvanceMoney(new BigDecimal(0));
				supplier.setType(ObjectUtils.getSupplierType(row.get(1)));

				SupplierClass supplierClass = serviceFactory.getSupplierClassService().getByName(row.get(2));

				if (supplierClass == null)
				{
					List<SupplierClass> supplierClassList = (List<SupplierClass>) (UserUtils.getBasicList(BasicType.SUPPLIERCLASS.toString()));
					if (supplierClassList != null && supplierClassList.size() > 0)
					{
						supplier.setSupplierClassId(supplierClassList.get(0).getId());
					}
				}
				supplier.setSupplierClassId(supplierClass.getId());

				DeliveryClass deliveryClass = serviceFactory.getDeliveryClassService().getByName(row.get(8));
				if (deliveryClass == null)
				{
					List<DeliveryClass> deliveryClassList = (List<DeliveryClass>) (UserUtils.getBasicList(BasicType.DELIVERYCLASS.toString()));
					if (deliveryClassList != null && deliveryClassList.size() > 0)
					{
						supplier.setDeliveryClassId(deliveryClassList.get(0).getId());
					}
				}
				supplier.setDeliveryClassId(deliveryClass.getId());

				PaymentClass paymentClass = serviceFactory.getPaymentClassService().getByName(row.get(6));
				if (paymentClass == null)
				{
					List<PaymentClass> paymentClassList = (List<PaymentClass>) (UserUtils.getBasicList(BasicType.PAYMENTCLASS.toString()));
					if (paymentClassList != null && paymentClassList.size() > 0)
					{
						supplier.setPaymentClassId(paymentClassList.get(0).getId());
					}
				}
				supplier.setPaymentClassId(paymentClass.getId());

				SettlementClass settlementClass = serviceFactory.getSettlementClassService().getByName(row.get(7));
				if (settlementClass == null)
				{
					List<SettlementClass> settlementClassList = (List<SettlementClass>) (UserUtils.getBasicList(BasicType.SETTLEMENTCLASS.toString()));
					if (settlementClassList != null && settlementClassList.size() > 0)
					{
						supplier.setSettlementClassId(settlementClassList.get(0).getId());
					}
				}
				supplier.setSettlementClassId(settlementClass.getId());

				TaxRate taxRate = serviceFactory.getTaxRateService().getByName(row.get(5));
				if (taxRate == null)
				{
					List<TaxRate> taxRateList = (List<TaxRate>) (UserUtils.getBasicList(BasicType.TAXRATE.toString()));
					if (taxRateList != null && taxRateList.size() > 0)
					{
						supplier.setTaxRateId(taxRateList.get(0).getId());
					}
				}
				supplier.setTaxRateId(taxRate.getId());

				/*
				 * if (supplier.getExchangeRateId() == null) { List<ExchangeRate> exchangeRateList = (List<ExchangeRate>)
				 * (UserUtils .getBasicList(BasicType.EXCHANGERATE.toString())); if (exchangeRateList != null &&
				 * exchangeRateList.size() > 0) { supplier.setExchangeRateId(exchangeRateList.get(0).getId()); } }
				 */
				supplier.setRegisteredCapital(0);

				supplier.setCreateName(UserUtils.getUserName());
				supplier.setCreateTime(new Date());
				supplier.setName(row.get(0));
				Employee employee = serviceFactory.getEmployeeService().getByName(row.get(3));
				if (employee == null)
				{
					throw new ServiceException(ResourceBundleMessageSource.i18nFormatter(BasicI18nResource.SUPPLIER_VALIDATE_MSG2, supplier.getName(), row.get(3)));
				}
				supplier.setEmployeeId(employee.getId());
				supplier.setCurrencyType(ObjectUtils.getCurrencyType(row.get(4)));
				supplier.setIsValid(ObjectUtils.getBoolValue(row.get(9)));
				supplier.setMemo(row.get(10));
				daoFactory.getCommonDao().saveEntity(supplier);

				// 保存地址
				for (ArrayList<String> addressList : addressSheet)
				{
					if (supplier.getName().equals(addressList.get(0)))
					{
						SupplierAddress supplierAddress = new SupplierAddress();
						supplierAddress.setCompanyId(supplier.getCompanyId());
						supplierAddress.setSupplierId(supplier.getId());
						supplierAddress.setUserName(addressList.get(1));
						supplierAddress.setAddress(addressList.get(2));
						supplierAddress.setMobile(addressList.get(3));
						supplierAddress.setEmail(addressList.get(4));
						supplierAddress.setQq(addressList.get(5));
						supplierAddress.setIsDefault(ObjectUtils.getBoolValue(addressList.get(6)));
						daoFactory.getCommonDao().saveEntity(supplierAddress);
					}
				}

				// 保存付款单位
				for (ArrayList<String> paymentList : paymentSheet)
				{
					if (supplier.getName().equals(paymentList.get(0)))
					{
						SupplierPayer supplierPayer = new SupplierPayer();
						supplierPayer.setCompanyId(supplier.getCompanyId());
						supplierPayer.setSupplierId(supplier.getId());
						supplierPayer.setIsDefault(ObjectUtils.getBoolValue(paymentList.get(2)));
						supplierPayer.setName(paymentList.get(1));
						daoFactory.getCommonDao().saveEntity(supplierPayer);
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
			UserUtils.clearCacheBasic(BasicType.SUPPLIER);
		}
		return count;
	}

	@Override
	public List<Long> findAllSaleEmployeeIds()
	{
		List<Long> ids = new ArrayList<Long>();
		DynamicQuery query = new CompanyDynamicQuery(Supplier.class);
		// query.eq("companyId", UserUtils.getCompanyId());
		List<Supplier> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Supplier.class);
		for (Supplier s : list)
		{
			if (ids.contains(s.getEmployeeId()))
			{
				continue;
			}
			if (s.getEmployeeId() == null)
			{
				continue;
			}
			ids.add(s.getEmployeeId());
		}
		return ids;
	}

	@Override
	public Long findIdByName(String supplierName)
	{
		DynamicQuery query = new CompanyDynamicQuery(Supplier.class);
		query.eq("name", supplierName);
		List<Supplier> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Supplier.class);
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
	public List<Long> findIdsByName(String supplierName)
	{
		List<Long> idList = Lists.newArrayList();
		QueryParam queryParam = new QueryParam();
		queryParam.setSupplierName(supplierName);
		SearchResult<Supplier> superlierList = serviceFactory.getSupplierService().findByCondition(queryParam);
		if (null != superlierList.getResult() && superlierList.getResult().size() > 0)
		{
			for (Supplier s : superlierList.getResult())
			{
				idList.add(s.getId());
			}
		}
		return idList;
	}

}
