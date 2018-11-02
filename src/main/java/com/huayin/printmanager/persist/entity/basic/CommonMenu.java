package com.huayin.printmanager.persist.entity.basic;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;

/**
 * 常用菜单
 * @ClassName: Customer
 * @author zhong
 * @date 2016年5月19日 上午11:21:37
 */
@Entity
@Table(name = "basic_commonmenu")
public class CommonMenu extends BaseBasicTableEntity
{

	private static final long serialVersionUID = 1L;

	private Long userId;
	
	private String menuName;
	
	private String url;

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getMenuName()
	{
		return menuName;
	}

	public void setMenuName(String menuName)
	{
		this.menuName = menuName;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
	
	
}
