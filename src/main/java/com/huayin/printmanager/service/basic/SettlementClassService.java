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
import com.huayin.printmanager.persist.entity.basic.SettlementClass;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础模块 - 结算方式
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月27日
 * @since        2.0, 2017年12月27日 下午17:07:00, think, 规范和国际化
 */
public interface SettlementClassService
{
	/**
	 * <pre>
	 * 根据id获取结算方式
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月27日 下午2:02:32, think
	 */
	public SettlementClass get(Long id);

	/**
	 * <pre>
	 * 根据结算方式名称查询结算方式
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月27日 下午2:02:40, think
	 */
	public SettlementClass getByName(String name);

	/**
	 * <pre>
	 * 添加结算方式
	 * </pre>
	 * @param settlementClass
	 * @return
	 * @since 1.0, 2017年12月27日 下午2:02:47, think
	 */
	public SettlementClass save(SettlementClass settlementClass);

	/**
	 * <pre>
	 * 修改结算方式
	 * </pre>
	 * @param settlementClass
	 * @return
	 * @since 1.0, 2017年12月27日 下午2:02:54, think
	 */
	public SettlementClass update(SettlementClass settlementClass);

	/**
	 * <pre>
	 * 得到全部销售结算方式
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月27日 下午2:03:00, think
	 */
	public List<SettlementClass> findAll();

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月27日 下午2:03:10, think
	 */
	public SearchResult<SettlementClass> findByCondition(QueryParam queryParam);
}
