/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.finance.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.ObjectHelper;
import com.huayin.printmanager.helper.service.CustomerHelper;
import com.huayin.printmanager.helper.service.FinanceSumHelper;
import com.huayin.printmanager.helper.service.SupplierHelper;
import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.persist.entity.begin.SupplierBeginDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.SupplierType;
import com.huayin.printmanager.service.finance.FinanceSumService;
import com.huayin.printmanager.service.finance.vo.FinanceShouldSumVo;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 财务管理 - 财务汇总
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年10月14日
 * @since 	  	 2.0, 2017年12月27日下午3:23:07,zhengby,代码重构
 */
@Service
public class FinanceSumServiceImpl extends BaseServiceImpl implements FinanceSumService
{

	@Override
	public SearchResult<FinanceShouldSumVo> paymentList(QueryParam queryParam)
	{
		// 把供应商名称 转换成 供应商ID
		SupplierHelper.transferToIds(queryParam);
		// 存放所有供应商
		SearchResult<FinanceShouldSumVo> all = new SearchResult<FinanceShouldSumVo>();
		// 计算分页
		Integer pageNumber = queryParam.getPageNumber();
		Integer pageSize = queryParam.getPageSize();
		// 因为手动分页，所以给一个100000肯定够了
		queryParam.setPageNumber(1);
		queryParam.setPageSize(100000);

		/**
		 * 1. 查询所有供应商，放到排序Map中（并查询预付余额）
		 * 2. 查询所有供应商的期初
		 * 3. 查询当前和历史的期初和本期
		 *   3.1 查询当前 期初应付、本期预付、本期实付、本期折扣
		 *   3.2 查询历史 结转
		 *   3.2 合并历史 期初+结转
		 *   3.3 合并所有数据
		 * 4. 汇总计算
		 */

		// 按供应商名称查询不到数据
		if (StringUtils.isNotBlank(queryParam.getSupplierName()) && null == queryParam.getSupplierIdList())
		{
			all.setCount(0);
			all.setResult(new ArrayList<FinanceShouldSumVo>());
			return all;
		}

		// 1. 查询所有供应商，放到排序Map中（并查询预付余额）
		List<Supplier> allSupplierList = serviceFactory.getSupplierService().findAll(queryParam);
		if (allSupplierList.size() == 0)
		{
			all.setCount(0);
			all.setResult(new ArrayList<FinanceShouldSumVo>());
			return all;
		}
		// 2. 查询所有供应商的期初
		List<SupplierBeginDetail> allSupplierBeginList = serviceFactory.getSupplierBeginService().findAll(queryParam);
		Map<Long, BigDecimal> allSupplierBeginMap = Maps.newHashMap();
		for (SupplierBeginDetail supplierBeginDetail : allSupplierBeginList)
		{
			BigDecimal money = allSupplierBeginMap.get(supplierBeginDetail.getSupplierId());
			if (null == money)
			{
				money = new BigDecimal(0);
			}
			money = money.add(supplierBeginDetail.getPaymentMoney());
			allSupplierBeginMap.put(supplierBeginDetail.getSupplierId(), money);
		}
		// 当查询全部账目时，供应商类型设置未综合供应商，原因是queryParam.getSupplierIdList已存有所有供应商
		if (null == queryParam.getSupplierType())
		{
			queryParam.setSupplierType(SupplierType.MATERIAL_AND_PROCESS);
		}
		// 3.1 查询当前 期初应付、本期预付、本期实付、本期折扣
		SearchResult<FinanceShouldSumVo> currentList = _commonSumList(queryParam, allSupplierList);
		// 3.2 查询历史 结转
		if (null == queryParam.getDateMin())
		{
			// 设置一个最小时间
			queryParam.setDateMin(DateUtils.parseDate("2015-01-01"));
		}
		Date dateMax = DateUtils.addDays(queryParam.getDateMin(), -1);
		Date dateMin = DateUtils.addYears(dateMax, -10);
		queryParam.setDateMin(dateMin);
		queryParam.setDateMax(dateMax);
		SearchResult<FinanceShouldSumVo> oldList = _commonSumList(queryParam, allSupplierList);

		// 3.2 合并历史 期初+结转
		Map<Long, FinanceShouldSumVo> oldMap = Maps.newHashMap();
		_commonBeginMoney(oldList, allSupplierBeginMap, oldMap);

		// 4. 汇总计算
		_commonMergeBegin(all, currentList, oldMap);
		// 4_1. 过滤实收/付为0
		if (BoolValue.NO == queryParam.getIsEmptyWare())
		{
			_commonfilterSum(all);
		}
		// 5. 计算分页
		FinanceSumHelper.pageManual(all, pageNumber, pageSize);
		// 6. 汇总
		_commonSum(all);

		return all;
	}

	@Override
	public SearchResult<FinanceShouldSumVo> receiveList(QueryParam queryParam)
	{
		// 把客户名称 转换成 客户ID
		CustomerHelper.transferToIds(queryParam);
		// 存放所有客户
		SearchResult<FinanceShouldSumVo> all = new SearchResult<FinanceShouldSumVo>();
		// 计算分页
		Integer pageNumber = queryParam.getPageNumber();
		Integer pageSize = queryParam.getPageSize();
		// 因为手动分页，所以给一个100000肯定够了
		queryParam.setPageNumber(1);
		queryParam.setPageSize(100000);

		/**
		 * 1. 查询所有客户，放到排序Map中（并查询预收余额）
		 * 2. 查询所有客户的期初
		 * 3. 查询当前和历史的期初和本期
		 *   3.1 查询当前 期初应付、本期预付、本期实付、本期折扣
		 *   3.2 查询历史 结转
		 *   3.2 合并历史 期初+结转
		 *   3.3 合并所有数据
		 * 4. 汇总计算
		 */

		// 按客户名称查询不到数据
		if (StringUtils.isNotBlank(queryParam.getCustomerName()) && null == queryParam.getCustomerIdList())
		{
			all.setCount(0);
			all.setResult(new ArrayList<FinanceShouldSumVo>());
			return all;
		}

		// 1. 查询所有客户，放到排序Map中（并查询预付余额）
		List<Customer> allCustomerList = serviceFactory.getCustomerService().findAll(queryParam);
		if (allCustomerList.size() == 0)
		{
			all.setCount(0);
			all.setResult(new ArrayList<FinanceShouldSumVo>());
			return all;
		}

		// 2. 查询所有客户的期初
		List<CustomerBeginDetail> allCustomerBeginList = serviceFactory.getCustomerBeginService().findAll(queryParam);
		Map<Long, BigDecimal> allCustmoerBeginMap = Maps.newHashMap();
		for (CustomerBeginDetail customerBeginDetail : allCustomerBeginList)
		{
			BigDecimal money = allCustmoerBeginMap.get(customerBeginDetail.getCustomerId());
			if (null == money)
			{
				money = new BigDecimal(0);
			}
			money = money.add(customerBeginDetail.getReceiveMoney());
			allCustmoerBeginMap.put(customerBeginDetail.getCustomerId(), money);
		}
		// 3.1 查询当前 期初应收、本期预收、本期实收、本期折扣
		SearchResult<FinanceShouldSumVo> currentList = _commonSumList(queryParam, allCustomerList);
		// 3.2 查询历史 结转
		if (null == queryParam.getDateMin())
		{
			// 设置一个最小时间
			queryParam.setDateMin(DateUtils.parseDate("2015-01-01"));
		}

		Date dateMax = DateUtils.addDays(queryParam.getDateMin(), -1);
		Date dateMin = DateUtils.addYears(dateMax, -10);
		queryParam.setDateMin(dateMin);
		queryParam.setDateMax(dateMax);
		SearchResult<FinanceShouldSumVo> oldList = _commonSumList(queryParam, allCustomerList);

		// 3.2 合并历史 期初+结转
		Map<Long, FinanceShouldSumVo> oldMap = Maps.newHashMap();
		_commonBeginMoney(oldList, allCustmoerBeginMap, oldMap);

		// 4.汇总计算
		_commonMergeBegin(all, currentList, oldMap);
		// 
		if (BoolValue.NO == queryParam.getIsEmptyWare())
		{
			_commonfilterSum(all);
		}
		// 5. 手动分页
		FinanceSumHelper.pageManual(all, pageNumber, pageSize);
		// 6. 汇总
		_commonSum(all);

		return all;
	}

	/**
	 * <pre>
	 * 公共方法 - 供应商/客户  应付/应收 账款汇总表
	 * </pre>
	 * @param queryParam
	 * @param allBussinerList
	 * @return
	 * @since 1.0, 2017年12月26日 下午7:16:36, zhengby
	 */
	private SearchResult<FinanceShouldSumVo> _commonSumList(QueryParam queryParam, List<? extends BaseBasicTableEntity> allBussinerList)
	{
		// long start = System.currentTimeMillis();

		// 存放所有供应商/客户
		Map<Long, FinanceShouldSumVo> allMap = Maps.newLinkedHashMap();
		SearchResult<FinanceShouldSumVo> all = new SearchResult<FinanceShouldSumVo>();

		/**
		 * 1. 查询供应商/客户预付余额
		 * 2. 查询供应商/客户的本期应付
		 * 3. 查询供应商/客户的本期预付、本期实付（必须有付款明细，否则就是预付余额）、本期折扣
		 * 4. 排序Map转换为List
		 */
		if (allBussinerList.get(0).getClass() == Supplier.class && allBussinerList.size() > 0)
		{
			// 1. 查询供应商预付余额
			for (Object supplier : allBussinerList)
			{
				FinanceShouldSumVo vo = new FinanceShouldSumVo();
				Supplier sup = (Supplier) supplier;
				vo.setSupplierId(sup.getId());
				vo.setType(sup.getType().getText());
				vo.setName(sup.getName());
				vo.setId(sup.getId());
				// 预付余额
				vo.setSurAdvance(sup.getAdvanceMoney());
				allMap.put(sup.getId(), vo);
			}

			if (queryParam.getSupplierType() == SupplierType.MATERIAL_AND_PROCESS)
			{
				// 2. 查询供应商的本期应付
				QueryParam _queryParam = ObjectHelper.byteClone(queryParam);
				_queryParam.setSupplierType(SupplierType.MATERIAL);
				SearchResult<FinanceShouldSumVo> result = serviceFactory.getPaymentService().findCurrentShouldMoney(_queryParam);
				_queryParam.setSupplierType(SupplierType.PROCESS);
				SearchResult<FinanceShouldSumVo> result_1 = serviceFactory.getPaymentService().findCurrentShouldMoney(_queryParam);
				result.getResult().addAll(result_1.getResult());
				result.setCount(result.getResult().size());
				_mergeShouldMoney(allMap, result.getResult());
				// 3. 查询供应商的本期预付、本期实付（必须有付款明细，否则就是预付余额）、本期折扣
				SearchResult<FinanceShouldSumVo> result_2 = serviceFactory.getPaymentService().findCurrentShouldResidue(_queryParam);
				_mergeShouldResidue(allMap, result_2.getResult());
				// 4. 查询供应商的核销单
				SearchResult<FinanceShouldSumVo> result_3 = serviceFactory.getPaymentService().findCurrentWriteoffMoney(_queryParam);
				_mergeWriteoffMoney(allMap, result_3.getResult());
				// 5. 查询供应商的财务调整单
				SearchResult<FinanceShouldSumVo> result_4 = serviceFactory.getPaymentService().findCurrentAdjustMoney(_queryParam);
				_mergeAdjustMoney(allMap, result_4.getResult());
			}
			else
			{
				// 2. 查询供应商的本期应付
				SearchResult<FinanceShouldSumVo> result = serviceFactory.getPaymentService().findCurrentShouldMoney(queryParam);
				_mergeShouldMoney(allMap, result.getResult());
				// 3. 查询供应商的本期预付、本期实付（必须有付款明细，否则就是预付余额）、本期折扣
				SearchResult<FinanceShouldSumVo> result2 = serviceFactory.getPaymentService().findCurrentShouldResidue(queryParam);
				_mergeShouldResidue(allMap, result2.getResult());
				// 4. 查询供应商的核销单
				SearchResult<FinanceShouldSumVo> result3 = serviceFactory.getPaymentService().findCurrentWriteoffMoney(queryParam);
				_mergeWriteoffMoney(allMap, result3.getResult());
				// 5. 查询供应商的财务调整单
				SearchResult<FinanceShouldSumVo> result4 = serviceFactory.getPaymentService().findCurrentAdjustMoney(queryParam);
				_mergeAdjustMoney(allMap, result4.getResult());
			}
		}
		else if (allBussinerList.get(0).getClass() == Customer.class && allBussinerList.size() > 0)
		{
			// 1. 查询客户预收余额
			for (Object customer : allBussinerList)
			{
				FinanceShouldSumVo vo = new FinanceShouldSumVo();
				Customer cus = (Customer) customer;
				vo.setCustomerId(cus.getId());
				vo.setName(cus.getName());
				vo.setId(cus.getId());
				// 预收余额
				vo.setSurAdvance(cus.getAdvanceMoney());
				allMap.put(cus.getId(), vo);
			}

			// 2. 查询客户的本期应收
			// 如果选项时全部，则返回销售单+代工单数据
			QueryParam _queryParam = ObjectHelper.byteClone(queryParam);
			_queryParam.setIsOem(BoolValue.NO); // 销售单
			SearchResult<FinanceShouldSumVo> result = serviceFactory.getReceiveService().findCurrentShouldMoney(_queryParam);

			_queryParam.setIsOem(BoolValue.YES); // 代工单
			SearchResult<FinanceShouldSumVo> result2 = serviceFactory.getReceiveService().findCurrentShouldMoney(_queryParam);
			result.getResult().addAll(result2.getResult());
			result.setCount(result.getResult().size());
			_mergeShouldMoney(allMap, result.getResult());
			// 3. 查询客户的本期预收、本期实收（必须有收款明细，否则就是预收余额）、本期折扣
			SearchResult<FinanceShouldSumVo> result3 = serviceFactory.getReceiveService().findCurrentShouldResidue(queryParam);
			_mergeShouldResidue(allMap, result3.getResult());
			// 4. 查询客户的核销单的金额
			SearchResult<FinanceShouldSumVo> result4 = serviceFactory.getReceiveService().findCurrentWriteoffMoney(queryParam);
			_mergeWriteoffMoney(allMap, result4.getResult());
			// 5. 查询供应商的财务调整单
			SearchResult<FinanceShouldSumVo> result5 = serviceFactory.getReceiveService().findCurrentAdjustMoney(queryParam);
			_mergeAdjustMoney(allMap, result5.getResult());
			// else
			// { // 暂时不用
			// SearchResult<FinanceShouldSumVo> result =
			// serviceFactory.getReceiveService().findCurrentShouldMoney(queryParam);
			// _mergeShouldMoney(allMap, result.getResult());
			// // 3. 查询客户的本期预收、本期实收（必须有收款明细，否则就是预收余额）、本期折扣
			// SearchResult<FinanceShouldSumVo> result2 =
			// serviceFactory.getReceiveService().findCurrentShouldResidue(queryParam);
			// _mergeShouldResidue(allMap, result2.getResult());
			// // 4. 查询客户的核销单的金额
			// SearchResult<FinanceShouldSumVo> result3 =
			// serviceFactory.getReceiveService().findCurrentWriteoffMoney(queryParam);
			// _mergeWriteoffMoney(allMap, result3.getResult());
			// }
		}
		// 4. 排序Map转换为List
		ArrayList<FinanceShouldSumVo> sumVoList = Lists.newArrayList(allMap.values());
		all.setResult(sumVoList);

		return all;
	}

	/**
	 * <pre>
	 * 合并供应商(客户)的本期应付
	 * </pre>
	 * @param allMap
	 * @param sumVoList
	 * @since 1.0, 2017年12月21日 下午6:58:55, think
	 */
	private void _mergeShouldMoney(Map<Long, FinanceShouldSumVo> allMap, List<FinanceShouldSumVo> sumVoList)
	{
		for (FinanceShouldSumVo shouldSumVo : sumVoList)
		{
			FinanceShouldSumVo shouldSumVo2 = allMap.get(shouldSumVo.getId());
			if (null != shouldSumVo2)
			{
				shouldSumVo2.setShouldMoney(shouldSumVo2.getShouldMoney().add(shouldSumVo.getShouldMoney()));
			}
		}
	}

	/**
	 * <pre>
	 * 合并供应商/客户的本期预付、本期实付、本期折扣
	 * </pre>
	 * @param allMap
	 * @param sumVoList
	 * @since 1.0, 2017年12月21日 下午6:59:28, think
	 */
	private void _mergeShouldResidue(Map<Long, FinanceShouldSumVo> allMap, List<FinanceShouldSumVo> sumVoList)
	{
		for (FinanceShouldSumVo shouldSumVo : sumVoList)
		{
			FinanceShouldSumVo shouldSumVo2 = allMap.get(shouldSumVo.getId());
			// if (null == shouldSumVo2)
			// {
			// allMap.put(shouldSumVo.getId(), shouldSumVo);
			// }
			// else
			// {
			// // 本期预付-advance、本期实付（必须有付款明细，否则就是预付余额）-money、本期折扣-discount
			// shouldSumVo2.setAdvance(shouldSumVo.getAdvance().add(shouldSumVo2.getAdvance()));
			// shouldSumVo2.setMoney(shouldSumVo.getMoney().add(shouldSumVo2.getMoney()));
			// shouldSumVo2.setDiscount(shouldSumVo.getDiscount().add(shouldSumVo2.getDiscount()));
			// }

			if (null != shouldSumVo2)
			{
				// 本期预付-advance、本期实付（必须有付款明细，否则就是预付余额）-money、本期折扣-discount
				shouldSumVo2.setAdvance(shouldSumVo.getAdvance().add(shouldSumVo2.getAdvance()));
				shouldSumVo2.setMoney(shouldSumVo.getMoney().add(shouldSumVo2.getMoney()));
				shouldSumVo2.setDiscount(shouldSumVo.getDiscount().add(shouldSumVo2.getDiscount()));
			}
		}
	}

	/**
	 * <pre>
	 * 合并核销单的金额
	 * </pre>
	 * @param allMap
	 * @param sumVoList
	 * @since 1.0, 2018年1月2日 下午2:43:13, zhengby
	 */
	private void _mergeWriteoffMoney(Map<Long, FinanceShouldSumVo> allMap, List<FinanceShouldSumVo> sumVoList)
	{
		for (FinanceShouldSumVo shouldSumVo : sumVoList)
		{
			FinanceShouldSumVo shouldSumVo2 = allMap.get(shouldSumVo.getId());
			// if (null == shouldSumVo2)
			// {
			// allMap.put(shouldSumVo.getId(), shouldSumVo);
			// }
			// else
			// {
			// // 本期预付-advance、本期实付（必须有付款明细，否则就是预付余额）-money、本期折扣-discount
			// shouldSumVo2.setMoney(shouldSumVo.getMoney().add(shouldSumVo2.getMoney()));
			// shouldSumVo2.setDiscount(shouldSumVo.getDiscount().add(shouldSumVo2.getDiscount()));
			// }

			if (null != shouldSumVo2)
			{
				// 本期预付-advance、本期实付（必须有付款明细，否则就是预付余额）-money、本期折扣-discount
				shouldSumVo2.setMoney(shouldSumVo.getMoney().add(shouldSumVo2.getMoney()));
				shouldSumVo2.setDiscount(shouldSumVo.getDiscount().add(shouldSumVo2.getDiscount()));
			}
		}
	}
	
	/**
	 * <pre>
	 * 合并财务调整金额
	 * </pre>
	 * @param allMap
	 * @param sumVoList
	 * @since 1.0, 2018年6月26日 下午6:11:52, zhengby
	 */
	private void _mergeAdjustMoney(Map<Long, FinanceShouldSumVo> allMap, List<FinanceShouldSumVo> sumVoList)
	{
		for (FinanceShouldSumVo shouldSumVo : sumVoList)
		{
			FinanceShouldSumVo shouldSumVo2 = allMap.get(shouldSumVo.getId());
			if (null != shouldSumVo2)
			{
				shouldSumVo2.setAdjustMoney(shouldSumVo.getAdjustMoney().add(shouldSumVo2.getAdjustMoney()));
			}
		}
	}
	/**
	 * <pre>
	 * 公共方法 - 计算期初beginMoney
	 * </pre>
	 * @param oldList
	 * @param allBeginMap
	 * @param oldMap
	 * @since 1.0, 2017年12月27日 上午9:41:41, zhengby
	 */
	private void _commonBeginMoney(SearchResult<FinanceShouldSumVo> oldList, Map<Long, BigDecimal> allBeginMap, Map<Long, FinanceShouldSumVo> oldMap)
	{
		for (FinanceShouldSumVo vo : oldList.getResult())
		{
			// 期初应收=本期应收-本期实收+期初
			BigDecimal beginMoney = allBeginMap.get(vo.getId());
			if (null == beginMoney)
			{
				beginMoney = new BigDecimal(0);
			}
			vo.setBeginMoney(beginMoney.add(vo.getShouldMoney()).subtract(vo.getDiscount()).subtract(vo.getMoney()).add(vo.getAdjustMoney()));
			// 如果结转等于0并且期初等于0，则等于0
			if (vo.getBeginMoney().compareTo(new BigDecimal(0)) == -1 && beginMoney.compareTo(new BigDecimal(0)) == 0)
			{
				vo.setBeginMoney(new BigDecimal(0));
			}
			// 如果结转等于0并且期初不等于0，则等于期初
			else if (vo.getBeginMoney().compareTo(new BigDecimal(0)) == -1 && beginMoney.compareTo(new BigDecimal(0)) == 1)
			{
				vo.setBeginMoney(beginMoney);
			}
			oldMap.put(vo.getId(), vo);
		}
	}

	/**
	 * <pre>
	 * 公共计算方法- 计算结转+期初和
	 * </pre>
	 * @param all
	 * @param currentList
	 * @param oldMap
	 * @since 1.0, 2017年12月27日 上午9:01:58, zhengby
	 */
	private void _commonMergeBegin(SearchResult<FinanceShouldSumVo> all, SearchResult<FinanceShouldSumVo> currentList, Map<Long, FinanceShouldSumVo> oldMap)
	{
		// 3.3 合并所有结转+期初数据
		List<FinanceShouldSumVo> sumVoList = Lists.newArrayList();
		all.setResult(sumVoList);
		for (FinanceShouldSumVo vo : currentList.getResult())
		{
			// vo.id = supplierId （ customerId）
			FinanceShouldSumVo old = oldMap.get(vo.getId());
			if (null != old)
			{
				vo.setBeginMoney(old.getBeginMoney());
			}
			sumVoList.add(vo);
		}
		all.setCount(sumVoList.size());
	}

	/**
	 * <pre>
	 * 公共计算方法 - 结果过滤实际应付收为0 的 
	 * </pre>
	 * @param all
	 * @since 1.0, 2018年7月11日 下午5:54:28, zhengby
	 */
	private void _commonfilterSum(SearchResult<FinanceShouldSumVo> all)
	{
		List<FinanceShouldSumVo> dellist = new ArrayList<>();
		for (FinanceShouldSumVo shouldSumVo : all.getResult())
		{
			BigDecimal _m = shouldSumVo.getBeginMoney().add(shouldSumVo.getShouldMoney()).add(shouldSumVo.getAdjustMoney()).subtract(shouldSumVo.getDiscount()).subtract(shouldSumVo.getMoney()).subtract(shouldSumVo.getSurAdvance());
			if (_m.compareTo(BigDecimal.valueOf(0)) == 0)
			{
				dellist.add(shouldSumVo);
				continue;
			}
		}
		for (FinanceShouldSumVo j : dellist)
		{
			all.getResult().remove(j);
		}
		all.setCount(all.getResult().size());
	}
	
	/**
	 * <pre>
	 * 公共计算方法- 结果合计
	 * </pre>
	 * @param all
	 * @since 1.0, 2018年1月13日 下午4:56:25, think
	 */
	private void _commonSum(SearchResult<FinanceShouldSumVo> all)
	{
		BigDecimal beginMoney = new BigDecimal(0);
		BigDecimal shouldMoney = new BigDecimal(0);
		BigDecimal advance = new BigDecimal(0);
		BigDecimal adjustMoney = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);
		BigDecimal discount = new BigDecimal(0);
		BigDecimal surAdvance = new BigDecimal(0);
//		List<FinanceShouldSumVo> dellist = new ArrayList<>();
		for (FinanceShouldSumVo shouldSumVo : all.getResult())
		{
//			if (BoolValue.NO == isFilter)
//			{
//				
//				BigDecimal _m = shouldSumVo.getBeginMoney().add(shouldSumVo.getShouldMoney()).add(shouldSumVo.getAdjustMoney())
//						.subtract(shouldSumVo.getDiscount()).subtract(shouldSumVo.getMoney()).subtract(shouldSumVo.getSurAdvance());
//				if (_m.compareTo(BigDecimal.valueOf(0)) == 0 )
//				{
//					dellist.add(shouldSumVo);
//					continue;
//				}
//			}
			advance = advance.add(shouldSumVo.getAdvance());
			beginMoney = beginMoney.add(shouldSumVo.getBeginMoney());
			shouldMoney = shouldMoney.add(shouldSumVo.getShouldMoney());
			adjustMoney = adjustMoney.add(shouldSumVo.getAdjustMoney());
			money = money.add(shouldSumVo.getMoney());
			discount = discount.add(shouldSumVo.getDiscount());
			surAdvance = surAdvance.add(shouldSumVo.getSurAdvance());
		}
//		for (FinanceShouldSumVo j : dellist)
//		{
//			all.getResult().remove(j);
//		}
		FinanceShouldSumVo vo = new FinanceShouldSumVo();
		vo.setAdvance(advance);
		vo.setBeginMoney(beginMoney);
		vo.setDiscount(discount);
		vo.setShouldMoney(shouldMoney);
		vo.setAdjustMoney(adjustMoney);
		vo.setMoney(money);
		vo.setSurAdvance(surAdvance);
		all.getResult().add(vo);
	}
}
