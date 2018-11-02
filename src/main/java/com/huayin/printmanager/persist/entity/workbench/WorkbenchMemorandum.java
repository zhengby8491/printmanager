package com.huayin.printmanager.persist.entity.workbench;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;

/**
 * <pre>
 * 备忘录
 * </pre>
 * @author raintear
 * @version 1.0, 2017年2月15日
 */
@Entity
@Table(name="workbench_memorandum")
public class WorkbenchMemorandum extends BaseTableIdEntity
{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	private Long userId;
	
	/**
	 * 备忘日期
	 */
	private Date date;
	
	/**
	 * 备忘内容
	 */
	private String content;
	
	/**
	 * 备忘录状态（框架上读取的时候，此字段为true，则显示有事件）
	 */
	private Boolean badge=true;

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date =date;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Boolean getBadge()
	{
		return badge;
	}

	public void setBadge(Boolean badge)
	{
		this.badge = badge;
	}
	
	
}
