/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月2日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.Product_Customer;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 产品信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月2日
 * @since        2.0, 2018年1月2日 下午17:07:00, think, 规范和国际化
 */
public interface ProductService
{
	/**
	 * <pre>
	 * 根据id获取产品信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:30:56, think
	 */
	public Product get(Long id);

	/**
	 * <pre>
	 * 根据产品名获取产品信息
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:31:07, think
	 */
	public Product getByName(String name);

	/**
	 * <pre>
	 * 根据客户料号获取产品信息
	 * </pre>
	 * @param customerMaterialCode
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:31:15, think
	 */
	public Product getByCustomerMaterialCode(String customerMaterialCode);

	/**
	 * <pre>
	 * 保存产品信息
	 * </pre>
	 * @param product
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:31:21, think
	 */
	public Product save(Product product);

	/**
	 * <pre>
	 * 快速保存产品信息（快速添加）
	 * </pre>
	 * @param customerId
	 * @param name
	 * @param customerMaterialCode
	 * @param specifications
	 * @return
	 * @since 1.0, 2018年2月7日 上午10:58:02, think
	 */
	public Product saveQuick(Long customerId, String name, String customerMaterialCode, String specifications);

	/**
	 * <pre>
	 * 快速保存产品信息（快速添加）
	 * </pre>
	 * @param productClassId
	 * @param customerId
	 * @param name
	 * @param customerMaterialCode
	 * @param specifications
	 * @return
	 * @since 1.0, 2018年2月28日 下午5:25:33, think
	 */
	public Product saveQuick(Long productClassId, Long customerId, String name, String customerMaterialCode, String specifications);

	/**
	 * <pre>
	 * 追加保存产品客户信息
	 * </pre>
	 * @param productId
	 * @param customerId
	 * @return
	 * @since 1.0, 2018年3月5日 下午2:19:11, think
	 */
	public Product_Customer appendProductCustomer(Long productId, Long customerId);

	/**
	 * <pre>
	 * 修改产品信息
	 * </pre>
	 * @param product
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:31:27, think
	 */
	public Product update(Product product);

	/**
	 * <pre>
	 * 批量删除产品信息
	 * </pre>
	 * @param ids
	 * @since 1.0, 2018年1月2日 上午9:31:37, think
	 */
	public void deleteByIds(Long[] ids);

	/**
	 * <pre>
	 * 删除客户产品关联表
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年1月2日 上午9:31:43, think
	 */
	public void deleteProductCustomerById(Long id);

	/**
	 * <pre>
	 * 批量删除客户产品关联表
	 * </pre>
	 * @param ids
	 * @since 1.0, 2018年1月2日 上午9:31:50, think
	 */
	public void deleteProductCustomerByIds(Long[] ids);

	/**
	 * <pre>
	 * 得到全部产品信息
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:32:03, think
	 */
	public List<Product> findAll();

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:32:10, think
	 */
	public SearchResult<Product> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 快捷查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月2日 上午9:32:17, think
	 */
	public SearchResult<Product> quickFindByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * excel导入
	 * </pre>
	 * @param inputStream
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年1月2日 上午9:32:23, think
	 */
	public Integer importFromExcel(InputStream inputStream) throws Exception;

	/**
	 * <pre>
	 * 根据产品id 和 客户id查询客户信息
	 * </pre>
	 * @param productId
	 * @param customerId
	 * @return
	 * @since 1.0, 2018年3月5日 下午2:15:46, think
	 */
	public Product_Customer findCustomerById(Long productId, Long customerId);

	/**
	 * <pre>
	 * 批量修改产品单价
	 * </pre>
	 * @param map
	 * @since 1.0, 2018年4月13日 下午5:04:14, zhengby
	 */
	public void updatePrice(Map<Long, BigDecimal> map);

}
