package com.huayin.printmanager.persist.entity.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.huayin.common.persist.entity.AbstractEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;

@Entity
@Table(name="sys_job")
public class SystemJob extends AbstractEntity
{
	/**
	 * <pre>
	 * 系统作业状态
	 * </pre>
	 * @author zhaojitao
	 * @version 1.0, 2016-4-17
	 */
	public enum SystemJobState
	{
		/**
		 * 运行中
		 */
		RUNING("正在运行"),
		/**
		 * 等待运行
		 */
		WAITING("等待运行");

		private String text;

		SystemJobState(String text)
		{
			this.text = text;
		}

		public String getText()
		{
			return text;
		}

		public void setText(String text)
		{
			this.text = text;
		}
	}

	private static final long serialVersionUID = -2397735988036657801L;

	/**
	 * 是否可调度
	 */
	@Column(length = 30)
	@Enumerated(javax.persistence.EnumType.STRING)
	private BoolValue canRun;

	/**
	 * 作业类名
	 */
	@Id
	@Column(length = 200)
	private String className;

	/**
	 * 描述
	 */
	@Column(length = 255)
	private String memo;

	private Integer failureCount;

	private Long id = 0L;

	/**
	 * 最后一次结束时间
	 */
	private Date lastCompleteTime;

	/**
	 * 最后运行主机名
	 */
	@Column(length = 1000)
	private String lastRunHostName;

	/**
	 * 最后一次开始时间
	 */
	private Date lastStartTime;

	/**
	 * 运行次数
	 */
	private Integer runCount;

	/**
	 * 运行状态
	 */
	@Column(length = 20)
	@Enumerated(javax.persistence.EnumType.STRING)
	private SystemJobState runState;

	/**
	 * 运行成功次数
	 */
	private Integer successCount;

	/**
	 * 作业名称
	 */
	@Column(length = 50)
	private String taskName;

	/**
	 * 超时次数
	 */
	private Integer timeoutCount;

	public BoolValue getCanRun()
	{
		return canRun;
	}

	public String getClassName()
	{
		return className;
	}


	public Integer getFailureCount()
	{
		return failureCount;
	}

	public Long getId()
	{
		return id;
	}

	public Date getLastCompleteTime()
	{
		return lastCompleteTime;
	}

	public String getLastRunHostName()
	{
		return lastRunHostName;
	}

	public Date getLastStartTime()
	{
		return lastStartTime;
	}

	public Integer getRunCount()
	{
		return runCount;
	}

	public SystemJobState getRunState()
	{
		return runState;
	}

	public Integer getSuccessCount()
	{
		return successCount;
	}

	public String getTaskName()
	{
		return taskName;
	}

	public Integer getTimeoutCount()
	{
		return timeoutCount;
	}

	public void setCanRun(BoolValue canRun)
	{
		this.canRun = canRun;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}
	public void setFailureCount(Integer failureCount)
	{
		this.failureCount = failureCount;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setLastCompleteTime(Date lastCompleteTime)
	{
		this.lastCompleteTime = lastCompleteTime;
	}

	public void setLastRunHostName(String lastRunHostName)
	{
		this.lastRunHostName = lastRunHostName;
	}

	public void setLastStartTime(Date lastStartTime)
	{
		this.lastStartTime = lastStartTime;
	}

	public void setRunCount(Integer runCount)
	{
		if (runCount.intValue() >= 1000000000)
		{// 重置
			this.runCount = 0;
		}
		else
		{
			this.runCount = runCount;
		}
	}

	public void setRunState(SystemJobState runState)
	{
		this.runState = runState;
	}

	public void setSuccessCount(Integer successCount)
	{
		if (successCount.intValue() >= 1000000000)
		{// 重置
			this.successCount = 0;
		}
		else
		{
			this.successCount = successCount;
		}
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	public void setTimeoutCount(Integer timeoutCount)
	{
		if (timeoutCount.intValue() >= 1000000000)
		{// 重置
			this.timeoutCount = 0;
		}
		else
		{
			this.timeoutCount = timeoutCount;
		}
	}

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}
	
}
