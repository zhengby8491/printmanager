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
import com.huayin.printmanager.persist.entity.sys.SystemVersionNotice;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统模块 - 版本公告
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface SystemVersionNoticeService
{
	/**
	 * <pre>
	 * 根据id获取公告信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:59:45, think
	 */
	public SystemVersionNotice get(Long id);

	/**
	 * <pre>
	 * 多条件查询公告信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:59:53, think
	 */
	public SearchResult<SystemVersionNotice> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询所有已发布公告
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:00:00, think
	 */
	public SearchResult<SystemVersionNotice> findAllPublish(QueryParam queryParam);

	/**
	 * <pre>
	 * 获取最新已发布的公告信息
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:00:06, think
	 */
	public SystemVersionNotice findLastPublish();

	/**
	 * <pre>
	 * 新增系统公告
	 * </pre>
	 * @param SystemVersionNotice
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:00:13, think
	 */
	public SystemVersionNotice save(SystemVersionNotice SystemVersionNotice);

	/**
	 * <pre>
	 * 修改系统公告
	 * </pre>
	 * @param SystemVersionNotice
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:00:20, think
	 */
	public SystemVersionNotice update(SystemVersionNotice SystemVersionNotice);

	/**
	 * <pre>
	 * 删除系统公告
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年10月25日 下午6:00:32, think
	 */
	public void delete(Long id);
}
