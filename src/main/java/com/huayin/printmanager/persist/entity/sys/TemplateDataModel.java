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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.huayin.common.persist.entity.AbstractEntity;

/**
 * <pre>
 * 系统模块 - 自定义模板
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_template_data_model")
public class TemplateDataModel extends AbstractEntity
{
	private static final long serialVersionUID = -4969975632609308675L;

	@Id
	private Long id;
	/**
	 * 所属订单类型
	 */
	@Column(length = 50)
	private String billType;
	/**
	 * 属性名称
	 */
	@Column(length = 50)
	private String name;
	/**
	 * 属性值
	 */
	@Column(length = 50)
	private String code;
	 

	public String getBillType()
	{
		return billType;
	}


	public void setBillType(String billType)
	{
		this.billType = billType;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public String getCode()
	{
		return code;
	}


	public void setCode(String code)
	{
		this.code = code;
	}


	public void setId(Long id)
	{
		this.id = id;
	}


	@Override
	public Object getId()
	{
		return id;
	}
 
}
