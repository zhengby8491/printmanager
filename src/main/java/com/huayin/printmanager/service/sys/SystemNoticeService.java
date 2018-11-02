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
import com.huayin.printmanager.persist.entity.sys.SystemNotice;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统模块 - 系统公告
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface SystemNoticeService
{
	/**
	 * <pre>
	 * 根据id获取公告信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:54:39, think
	 */
	public SystemNotice get(Long id);

	/**
	 * <pre>
	 * 多条件查询公告信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:54:45, think
	 */
	public SearchResult<SystemNotice> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询所有已发布公告
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:54:53, think
	 */
	public SearchResult<SystemNotice> findAllPublish(QueryParam queryParam);

	/**
	 * <pre>
	 * 获取最新已发布的公告信息
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:54:59, think
	 */
	public SystemNotice findLastPublish();

	/**
	 * <pre>
	 * 新增系统公告
	 * </pre>
	 * @param systemNotice
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:55:05, think
	 */
	public SystemNotice save(SystemNotice systemNotice);

	/**
	 * <pre>
	 * 修改系统公告
	 * </pre>
	 * @param systemNotice
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:55:12, think
	 */
	public SystemNotice update(SystemNotice systemNotice);

	/**
	 * <pre>
	 * 删除系统公告
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年10月25日 下午5:55:22, think
	 */
	public void delete(Long id);
}
