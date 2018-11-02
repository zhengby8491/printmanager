package com.huayin.printmanager.persist.entity.workbench;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;

/**
 * <pre>
 * 常用功能
 * </pre>
 * @author raintear
 * @version 1.0, 2017年2月15日
 */
@Entity
@Table(name="workbench_often")
public class WorkbenchOften extends BaseTableIdEntity
{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 链接
	 */
	@Column(length = 255)
	private String url;
	
	private Long userId;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
	
	
}
