/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年1月11日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.job;

import com.huayin.common.exception.ServiceResult;
import com.huayin.printmanager.persist.entity.sys.SystemJob;

/**
 * <pre>
 * 框架 - 系统任务
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月11日
 */
public interface SystemJobService
{
	/**
	 * <pre>
	 * 检查任务 如果不存在，则创建
	 * </pre>
	 * @param className
	 * @param taskName
	 * @param taskDescription
	 * @return
	 */
	public SystemJob checkJob(String className, String taskName, String taskDescription);

	public void execOverSystemJob(String className, boolean isSuccess, int runCount);

	public ServiceResult<SystemJob> execSystemJob(String className, long timeout);

	/**
	 * <pre>
	 * 通过类名查找对应的JOB
	 * </pre>
	 * @param className 类名
	 * @return
	 */
	SystemJob findByClassName(String className);

	/**
	 * <pre>
	 *  根据系统作业ID查询系统作业
	 * </pre>
	 * @param id 系统作业ID
	 * @return
	 */
	ServiceResult<SystemJob> get(String className);

	/**
	 * <pre>
	 *  根据系统作业ID查询系统作业
	 * </pre>
	 * @param id 系统作业ID
	 * @return
	 */
	SystemJob lock(String className);

	/**
	 * <pre>
	 * 还原所有系统任务状态
	 * </pre>
	 * @return 被还原状态的任务数
	 */
	public int resetAllSystemJob();

	/**
	 * <pre>
	 * 新增系统作业
	 * </pre>
	 * @param sysJob
	 */
	ServiceResult<SystemJob> save(SystemJob sysJob);

	/**
	 * <pre>
	 *  修改系统作业
	 * </pre>
	 * @param sysJob 系统作业
	 * @return
	 */
	ServiceResult<SystemJob> update(SystemJob sysJob);
}
