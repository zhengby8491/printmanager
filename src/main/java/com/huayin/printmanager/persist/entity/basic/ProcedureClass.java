/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月28日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.ProcedureType;

/**
 * <pre>
 * 基础设置 - 工序分类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月28日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_procedure_class")
public class ProcedureClass extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 工序分类名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 工序分类类型
	 */
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private ProcedureType procedureType;

	/**
	 * 产品类型 1通用 2包装 3书刊
	 */
	private Integer productType;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ProcedureType getProcedureType()
	{
		return procedureType;
	}

	public void setProcedureType(ProcedureType procedureType)
	{
		this.procedureType = procedureType;
	}

	public String getProcedureTypeText()
	{
		if (procedureType != null)
		{
			return procedureType.getText();
		}
		return "-";
	}

	public Integer getProductType()
	{
		return productType;
	}

	public void setProductType(Integer productType)
	{
		this.productType = productType;
	}
}
