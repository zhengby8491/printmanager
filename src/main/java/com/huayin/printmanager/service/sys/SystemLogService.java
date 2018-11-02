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

import java.util.Date;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.SystemLog;
import com.huayin.printmanager.persist.enumerate.SystemLogType;

/**
 * <pre>
 * 系统模块 - 系统日志
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface SystemLogService
{
	/**
	 * <pre>
	 * 多条件查询 短信记录
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param type
	 * @param companyName
	 * @param userName
	 * @param ip
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @since 1.0, 2017年10月25日 下午5:50:21, think
	 */
	public SearchResult<SystemLog> findByCondition(Date dateMin, Date dateMax, SystemLogType type, String companyName, String userName, String ip, Integer pageIndex, Integer pageSize);

	/**
	 * <pre>
	 * 新增系统日志
	 * </pre>
	 * @param log
	 * @since 1.0, 2017年10月25日 下午5:50:07, think
	 */
	public void addLog(SystemLog log);
}
