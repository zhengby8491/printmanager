package com.huayin.printmanager.persist.entity.workbench;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;

/**
 * <pre>
 * 公告
 * </pre>
 * @author raintear
 * @version 1.0, 2017年2月15日
 */
@Entity
@Table(name="workbench_notice")
public class WorkbenchNotice extends BaseTableIdEntity
{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 内容
	 */
	private String content;
	
	/**
	 * 创建人
	 */
	private String createName;
	
	/**
	 * 日期
	 */
	private Date createTime;


	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	
}
