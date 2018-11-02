/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.Machine;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 机台信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
public interface MachineService
{
	/**
	 * <pre>
	 * 根据id获取机台信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月4日 上午10:00:21, think
	 */
	public Machine get(Long id);

	/**
	 * <pre>
	 * 根据id获取机台信息
	 * </pre>
	 * @param companyId
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月4日 上午10:00:27, think
	 */
	public Machine get(String companyId, Long id);

	/**
	 * <pre>
	 * 根据name获取机台信息
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2018年1月4日 上午10:00:33, think
	 */
	public Machine getByName(String name);

	/**
	 * <pre>
	 * 保存机台信息
	 * </pre>
	 * @param machine
	 * @return
	 * @since 1.0, 2018年1月4日 上午10:00:42, think
	 */
	public Machine save(Machine machine);

	/**
	 * <pre>
	 * 修改机台信息
	 * </pre>
	 * @param machine
	 * @return
	 * @since 1.0, 2018年1月4日 上午10:00:48, think
	 */
	public Machine update(Machine machine);

	/**
	 * <pre>
	 * 批量删除机台信息
	 * </pre>
	 * @param ids
	 * @since 1.0, 2018年1月4日 上午10:00:56, think
	 */
	public void deleteByIds(Long[] ids);

	/**
	 * <pre>
	 * 得到全部机台信息
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月4日 上午10:01:02, think
	 */
	public List<Machine> findAll();

	/**
	 * <pre>
	 * 按色数获取机台信息
	 * </pre>
	 * @param colorQty
	 * @return
	 * @since 1.0, 2018年1月4日 上午10:01:09, think
	 */
	public Machine findMachineBycolorQty(Integer colorQty);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月4日 上午10:01:16, think
	 */
	public SearchResult<Machine> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 快捷查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月4日 上午10:01:24, think
	 */
	public SearchResult<Machine> quickFindByCondition(QueryParam queryParam);
}
