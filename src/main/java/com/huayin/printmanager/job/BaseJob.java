package com.huayin.printmanager.job;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.huayin.common.exception.ServiceResult;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.entity.sys.SystemJob;
import com.huayin.printmanager.persist.entity.sys.SystemJob.SystemJobState;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.ServiceFactory;

public abstract class BaseJob
{
	public static Log log = LogFactory.getLog(BaseJob.class);

	@Autowired
	protected ServiceFactory serviceFactory;

	// 首次启动要检查任务状态
	private static Boolean flag = false;

	/**
	 * 任务名
	 */
	public String taskName;

	/**
	 * 任务描述
	 */
	public String taskDescription;

	/**
	 * 超时时间,单位毫秒 一般根据业务设定一下最高值(必需大于两次任务的间隔时间,不然不起作用) -1为忽略超时(当上次任务没有执行完之前,不能执行下次任务)
	 */
	public int timeout = -1;

	/**
	 * <pre>
	 * 具体业务执行 
	 * </pre>
	 * @param isTimeout 上次任务是否超时
	 */
	public abstract void exec(boolean isTimeout);

	/**
	 * <pre>
	 * 任务调度开始
	 * </pre>
	 */
	protected void start()
	{
		try
		{
			// 初次启动时，把定时作业的状态全部改成 等待运行
			checkSystemJob();
			// 检查系统是否正在正常运行
			String systemStatus = SystemConfigUtil.getConfig(SysConstants.SYSTEM_JOB_STATUS);
			if (systemStatus != null && !systemStatus.equals(SysConstants.YES))
			{
				return;
			}
			boolean isTimeout = false;
			SystemJob systemJob = serviceFactory.getSystemJobService().checkJob(this.getClass().getName(), taskName,
					taskDescription);
			if (systemJob.getCanRun() == BoolValue.NO)
			{
				log.info(systemJob.getTaskName() + "作业的状态为{" + systemJob.getCanRun() + "},不可调度");
				return;
			}
			if (systemJob.getRunState() == SystemJobState.RUNING && timeout != -1)
			{
				// 如果任务正在运行,且有加入超时处理
				long runtimeNum = new Date().getTime() - systemJob.getLastStartTime().getTime();
				if (runtimeNum > timeout)
				{
					// 工作超时
					isTimeout = true;
				}
			}
			if (systemJob.getRunState() == SystemJobState.WAITING || isTimeout)
			{
				boolean isSuccess = true;
				ServiceResult<SystemJob> result = serviceFactory.getSystemJobService().execSystemJob(
						systemJob.getClassName(), timeout);
				if (result.getReturnValue() != null)
				{
					// long start = new Date().getTime();
					try
					{
						exec(isTimeout);
					}
					catch (Exception e)
					{
						log.error("执行定时任务{" + systemJob.getTaskName() + "}出现异常.", e);
						isSuccess = false;
					}
					// long end = new Date().getTime();
					// log.info(this.taskName + " 完成，耗时:" + (end - start));
					try
					{
						serviceFactory.getSystemJobService().execOverSystemJob(systemJob.getClassName(), isSuccess,
								result.getReturnValue().getRunCount());
					}
					catch (Exception e)
					{
						// 如果有异常重做一次
						serviceFactory.getSystemJobService().execOverSystemJob(systemJob.getClassName(), isSuccess,
								result.getReturnValue().getRunCount());
					}
				}
				else
				{
					log.info("任务[" + systemJob.getTaskName() + "]正在运行中...");
				}
			}
			else
			{
				log.info("任务[" + systemJob.getTaskName() + "]上次任务还没有完成,等下次执行");
				return;
			}
		}
		catch (Throwable e)
		{
			log.error("执行定时任务{" + this.getClass().getName() + "}出现异常.", e);
		}
	}

	private void checkSystemJob()
	{
		if (!flag)
		{
			synchronized (flag)
			{
				if (!flag)
				{
					int num = serviceFactory.getSystemJobService().resetAllSystemJob();
					log.info("共有[" + num + "]个任务状态被重置");
					flag = true;
				}
			}
		}
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	public void setTaskDescription(String taskDescription)
	{
		this.taskDescription = taskDescription;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

}
