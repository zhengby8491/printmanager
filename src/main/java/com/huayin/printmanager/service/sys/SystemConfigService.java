/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.SystemConfig;

/**
 * <pre>
 * 系统模块 - 系统参数
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface SystemConfigService
{
	/**
	 * <pre>
	 * 通过id标识获取系统参数
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:46:56, think
	 */
	public SystemConfig get(String id);

	/**
	 * <pre>
	 * 多条件查询系统参数
	 * </pre>
	 * @param systemConfigId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:47:02, think
	 */
	public SearchResult<SystemConfig> findByCondition(String systemConfigId, Integer pageIndex, Integer pageSize);

	/**
	 * <pre>
	 * 新增系统新参数
	 * </pre>
	 * @param systemConfig
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:47:08, think
	 */
	public SystemConfig save(SystemConfig systemConfig);

	/**
	 * <pre>
	 * 更新系统参数
	 * </pre>
	 * @param systemConfig
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:47:13, think
	 */
	public SystemConfig update(SystemConfig systemConfig);

	/**
	 * <pre>
	 * 删除系统参数
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年10月25日 下午5:47:29, think
	 */
	public void delete(String id);
}
