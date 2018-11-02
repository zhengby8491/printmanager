/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月29日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.io.InputStream;
import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.CustomerAddress;
import com.huayin.printmanager.persist.entity.basic.CustomerPayer;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 客户信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月29日
 * @since        2.0, 2017年12月29日 下午17:07:00, think, 规范和国际化
 */
public interface CustomerService
{
	/**
	 * <pre>
	 * 根据id获取客户信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月29日 下午2:08:47, think
	 */
	public Customer get(Long id);

	/**
	 * <pre>
	 * 根据name获取客户信息
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月29日 下午2:08:54, think
	 */
	public Customer getByName(String name);

	/**
	 * <pre>
	 * 获取客户默认地址信息
	 * </pre>
	 * @param customerId
	 * @return
	 * @since 1.0, 2017年12月29日 下午2:09:00, think
	 */
	public CustomerAddress getDefaultAddress(Long customerId);

	/**
	 * <pre>
	 * 获取客户默认付款单位信息
	 * </pre>
	 * @param customerId
	 * @return
	 * @since 1.0, 2017年12月29日 下午2:09:07, think
	 */
	public CustomerPayer getDefaultPayer(Long customerId);

	/**
	 * <pre>
	 * 获得地址信息
	 * </pre>
	 * @param customerId
	 * @return
	 * @since 1.0, 2017年12月29日 下午2:09:30, think
	 */
	public List<CustomerAddress> getAddressList(Long customerId);

	/**
	 * <pre>
	 * 获得付款单位信息
	 * </pre>
	 * @param customerId
	 * @return
	 * @since 1.0, 2017年12月29日 下午2:09:36, think
	 */
	public List<CustomerPayer> getPayerList(Long customerId);

	/**
	 * <pre>
	 * 保存客户信息
	 * </pre>
	 * @param customer
	 * @return
	 * @since 1.0, 2017年12月29日 下午2:09:43, think
	 */
	public Customer save(Customer customer);
	
	/**
	 * <pre>
	 * 快速保存客户信息（快速添加）
	 * </pre>
	 * @param name
	 * @param address
	 * @param userName
	 * @param mobile
	 * @return
	 * @since 1.0, 2018年2月7日 上午10:39:04, think
	 */
	public Customer saveQuick(String name, String address, String userName, String mobile);

	/**
	 * <pre>
	 * 修改客户信息
	 * </pre>
	 * @param customer
	 * @return
	 * @since 1.0, 2017年12月29日 下午2:09:49, think
	 */
	public Customer update(Customer customer);

	/**
	 * <pre>
	 * 批量删除客户信息
	 * </pre>
	 * @param ids
	 * @since 1.0, 2017年12月29日 下午2:09:58, think
	 */
	public void deleteByIds(Long[] ids);

	/**
	 * <pre>
	 * 查找本公司的所有客户信息
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月29日 下午2:10:05, think
	 */
	public List<Customer> findAll();

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月29日 下午2:10:11, think
	 */
	public SearchResult<Customer> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 快捷查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月29日 下午2:10:28, think
	 */
	public SearchResult<Customer> quickFindByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * excel文件导入
	 * </pre>
	 * @param inputStream
	 * @return
	 * @throws Exception
	 * @since 1.0, 2017年12月29日 下午2:10:34, think
	 */
	public Integer importFromExcel(InputStream inputStream) throws Exception;

	/**
	 * <pre>
	 * 查询所有销售员工
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月29日 下午2:10:45, think
	 */
	public List<Long> findAllSaleEmployeeIds();

	/**
	 * <pre>
	 * 根据客户名称查找id
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月26日 上午10:11:21, zhengby
	 */
	public Long findIdByName(String customerName);

	/**
	 * <pre>
	 * 查询全部客户
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月26日 下午3:41:56, zhengby
	 */
	public List<Customer> findAll(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据客户名称查找ids
	 * </pre>
	 * @param customerName
	 * @return
	 * @since 1.0, 2017年12月28日 上午10:19:51, zhengby
	 */
	public List<Long> findIdsByName(String customerName);
	
	/**
	 * <pre>
	 * 根据客户名称查找ids(代工平台客户)
	 * </pre>
	 * @param customerName
	 * @return
	 * @since 1.0, 2018年3月20日 上午9:34:08, zhengby
	 */
	public List<Long> findIdsByName(String customerName, BoolValue isOem);
}
