/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.sys;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.AdvertisementType;
import com.huayin.printmanager.persist.enumerate.BoolValue;

/**
 * <pre>
 * 系统模块 - 系统广告
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_advertisement")
public class Advertisement extends BaseTableIdEntity
{
	private static final long serialVersionUID = -2397735988036657802L;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 图片
	 */
	private String photoUrl;

	/**
	 * 链接
	 */
	private String linkedUrl;

	/**
	 * 点击次数
	 */
	private Long clickCount = 0L;

	/**
	 * 是否发布
	 */
	@Enumerated(javax.persistence.EnumType.STRING)
	private BoolValue publish;

	@Enumerated(javax.persistence.EnumType.STRING)
	private AdvertisementType advertisementType;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 发布人
	 */
	private String createName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getPhotoUrl()
	{
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl)
	{
		this.photoUrl = photoUrl;
	}

	public BoolValue getPublish()
	{
		return publish;
	}

	public void setPublish(BoolValue publish)
	{
		this.publish = publish;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public String getPublishText()
	{
		if (publish != null)
		{

			return publish.getText();
		}
		return "-";
	}

	public Long getClickCount()
	{
		return clickCount;
	}

	public void setClickCount(Long clickCount)
	{
		this.clickCount = clickCount;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public String getLinkedUrl()
	{
		return linkedUrl;
	}

	public void setLinkedUrl(String linkedUrl)
	{
		this.linkedUrl = linkedUrl;
	}

	public AdvertisementType getAdvertisementType()
	{
		return advertisementType;
	}

	public void setAdvertisementType(AdvertisementType advertisementType)
	{
		this.advertisementType = advertisementType;
	}

	public Integer getSort()
	{
		return sort;
	}

	public void setSort(Integer sort)
	{
		this.sort = sort;
	}
	
	public String getAdvertisementTypeText()
	{
		if (advertisementType != null)
		{
			return advertisementType.getText();
		}
		return null;
	}
}