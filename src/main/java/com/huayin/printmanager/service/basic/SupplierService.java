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
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.SupplierAddress;
import com.huayin.printmanager.persist.entity.basic.SupplierPayer;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 供应商信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月29日
 * @since        2.0, 2017年12月29日 下午17:07:00, think, 规范和国际化
 */
public interface SupplierService
{

	/**
	 * <pre>
	 * 根据id获取供应商信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:39:22, think
	 */
	public Supplier get(Long id);

	/**
	 * <pre>
	 * 根据名称获取供应商信息
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:39:28, think
	 */
	public Supplier getByName(String name);

	/**
	 * <pre>
	 * 保存供应商信息
	 * </pre>
	 * @param supplier
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:39:45, think
	 */
	public Supplier save(Supplier supplier);

	/**
	 * <pre>
	 * 修改供应商信息
	 * </pre>
	 * @param supplier
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:39:51, think
	 */
	public Supplier update(Supplier supplier);

	/**
	 * <pre>
	 * 批量删除供应商信息
	 * </pre>
	 * @param ids
	 * @since 1.0, 2017年12月29日 上午9:40:00, think
	 */
	public void deleteByIds(Long[] ids);

	/**
	 * <pre>
	 * 获取供应商地址
	 * </pre>
	 * @param supplierId
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:40:06, think
	 */
	public SupplierAddress getDefaultAddress(Long supplierId);

	/**
	 * <pre>
	 *  获得供应商地址
	 * </pre>
	 * @param supplierId
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:40:25, think
	 */
	public List<SupplierAddress> getAddressList(Long supplierId);

	/**
	 * <pre>
	 * 获取付款单位信息
	 * </pre>
	 * @param supplierId
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:40:36, think
	 */
	public SupplierPayer getDefaultPayer(Long supplierId);

	/**
	 * 
	 * <pre>
	 * 获取付款单位信息
	 * </pre>
	 * @param supplierId
	 * @return
	 */
	public List<SupplierPayer> getPayerList(Long supplierId);

	/**
	 * <pre>
	 * 得到所有供应商信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:41:09, think
	 */
	public List<Supplier> findAll(QueryParam queryParam);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:41:15, think
	 */
	public SearchResult<Supplier> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 快捷查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:41:23, think
	 */
	public SearchResult<Supplier> quickFindByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * excel文件导入
	 * </pre>
	 * @param inputStream
	 * @return
	 * @throws Exception
	 * @since 1.0, 2017年12月29日 上午9:41:34, think
	 */
	public Integer importFromExcel(InputStream inputStream) throws Exception;

	/**
	 * <pre>
	 * 获得所有员工id
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:41:45, think
	 */
	public List<Long> findAllSaleEmployeeIds();

	/**
	 * <pre>
	 * 根据名称查询ID
	 * </pre>
	 * @param supplierName
	 * @return
	 * @since 1.0, 2017年12月21日 下午6:08:53, think
	 */
	public Long findIdByName(String supplierName);

	/**
	 * <pre>
	 * 根据名称查询IDs
	 * </pre>
	 * @param supplierName
	 * @return
	 * @since 1.0, 2017年12月28日 上午10:13:37, zhengby
	 */
	List<Long> findIdsByName(String supplierName);

}
