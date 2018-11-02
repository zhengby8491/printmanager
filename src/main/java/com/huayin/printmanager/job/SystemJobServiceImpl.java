package com.huayin.printmanager.job;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.CommonDao;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.util.NetworkUtil;
import com.huayin.printmanager.persist.entity.sys.SystemJob;
import com.huayin.printmanager.persist.entity.sys.SystemJob.SystemJobState;
import com.huayin.printmanager.persist.enumerate.BoolValue;

/**
 * <pre>
 * 系统作业服务实现
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-4-19
 */
@Transactional
@Component
@Lazy
public class SystemJobServiceImpl implements ApplicationContextAware, SystemJobService
{
	private CommonDao commondao;

	@Transactional(propagation = Propagation.REQUIRED)
	public SystemJob checkJob(String className, String taskName, String taskDescription)
	{
		SystemJob systemJob = this.findByClassName(className);
		if (systemJob == null)
		{
			systemJob = new SystemJob();
			systemJob.setCanRun(BoolValue.YES);
			systemJob.setClassName(className);
			systemJob.setMemo(taskDescription);
			systemJob.setLastCompleteTime(new Date());
			systemJob.setLastRunHostName(NetworkUtil.getHostName());
			systemJob.setLastStartTime(new Date());
			systemJob.setRunCount(0);
			systemJob.setRunState(SystemJobState.WAITING);
			systemJob.setSuccessCount(0);
			systemJob.setFailureCount(0);
			systemJob.setTaskName(taskName);
			systemJob.setTimeoutCount(0);
			systemJob = this.save(systemJob).getReturnValue();
		}
		return systemJob;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void execOverSystemJob(String className, boolean isSuccess, int runCount)
	{
		SystemJob __systemJob = lock(className);
		__systemJob.setLastRunHostName(NetworkUtil.getHostName());
		if (__systemJob.getRunCount().equals(runCount))
		{
			__systemJob.setRunState(SystemJobState.WAITING);
			__systemJob.setLastCompleteTime(new Date());
		}
		if (isSuccess)
		{
			__systemJob.setSuccessCount(__systemJob.getSuccessCount() + 1);
		}
		else
		{
			__systemJob.setFailureCount(__systemJob.getFailureCount() + 1);
		}
		update(__systemJob);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResult<SystemJob> execSystemJob(String className, long timeout)
	{
		SystemJob _systemJob = lock(className);
		boolean isExec = false;// 是否可以执行
		boolean isTimeout = false;// 定时任务是否超时

		if (_systemJob.getRunState() == SystemJobState.WAITING)
		{
			isExec = true;
		}
		else if (_systemJob.getRunState() == SystemJobState.RUNING && timeout != -1)
		{
			long runtimeNum = new Date().getTime() - _systemJob.getLastStartTime().getTime();
			if (runtimeNum > timeout)
			{
				isTimeout = true;
			}
		}
		if (isExec || isTimeout)
		{
			_systemJob.setLastStartTime(new Date());
			_systemJob.setRunCount(_systemJob.getRunCount() + 1);
			if (isTimeout)
			{
				_systemJob.setTimeoutCount(_systemJob.getTimeoutCount() + 1);
			}
			_systemJob.setRunState(SystemJobState.RUNING);
			update(_systemJob);
			return new ServiceResult<SystemJob>(_systemJob);
		}
		return new ServiceResult<SystemJob>();
	}

	public SystemJob findByClassName(String className)
	{
		DynamicQuery query = new DynamicQuery(SystemJob.class);
		query.eq("className", className);
		List<SystemJob> list = commondao.findEntityByDynamicQuery(query, SystemJob.class);
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	public ServiceResult<SystemJob> get(String className)
	{
		SystemJob sysjob = commondao.getEntity(SystemJob.class, className);
		return new ServiceResult<SystemJob>(sysjob);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public SystemJob lock(String className)
	{
		return commondao.lockObject(SystemJob.class, className);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int resetAllSystemJob()
	{
		DynamicQuery query = new DynamicQuery(SystemJob.class);
		query.eq("runState", SystemJobState.RUNING);
		List<SystemJob> lockByDynamicQuery = commondao.lockByDynamicQuery(query, SystemJob.class, LockType.LOCK_PASS);
		for (SystemJob systemJob : lockByDynamicQuery)
		{
			systemJob.setRunState(SystemJobState.WAITING);
		}
		commondao.updateAllEntity(lockByDynamicQuery);
		return lockByDynamicQuery.size();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResult<SystemJob> save(SystemJob sysJob)
	{
		commondao.saveEntity(sysJob);
		return new ServiceResult<SystemJob>(sysJob);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResult<SystemJob> update(SystemJob sysJob)
	{
		SystemJob systemJob = commondao.updateEntity(sysJob);
		return new ServiceResult<SystemJob>(systemJob);
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException
	{
		this.commondao = context.getBean(CommonDao.class);
	}
}
