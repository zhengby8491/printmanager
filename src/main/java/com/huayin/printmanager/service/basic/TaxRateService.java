/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月27日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 税率信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月27日
 * @since        2.0, 2017年12月27日 下午17:07:00, think, 规范和国际化
 */
public interface TaxRateService
{
	/**
	 * <pre>
	 * 根据id获取税率信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:20:33, think
	 */
	public TaxRate get(Long id);

	/**
	 * <pre>
	 * 根据税率名称查询税率
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:20:41, think
	 */
	public TaxRate getByName(String name);

	/**
	 * <pre>
	 * 根据税率值查询税率
	 * </pre>
	 * @param percent
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:20:48, think
	 */
	public TaxRate getByPercent(int percent);

	/**
	 * <pre>
	 * 添加税率信息
	 * </pre>
	 * @param taxRate
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:20:55, think
	 */
	public TaxRate save(TaxRate taxRate);

	/**
	 * <pre>
	 * 修改税率信息
	 * </pre>
	 * @param taxRate
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:21:01, think
	 */
	public TaxRate update(TaxRate taxRate);

	/**
	 * <pre>
	 * 得到全部税率信息
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:21:11, think
	 */
	public List<TaxRate> findAll();

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:21:18, think
	 */
	public SearchResult<TaxRate> findByCondition(QueryParam queryParam);
}
