/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.finance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 财务管理 - 预收核销单明细
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "finance_writeoffreceive_detail")
public class FinanceWriteoffReceiveDetail extends FinanceDetailBaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 产品Id
	 */
	@Column(length = 50)
	private Long productId;

	/**
	 * 成品名称
	 */
	@Column(length = 50)
	private String productName;

	/**
	 * 规格(材料规格/加工规格)
	 */
	@Column(length = 50)
	private String style;
	
	/**
	 * 工序名称
	 */
	private String procedureName;
	
	/**
	 * 工序id
	 */
	private Long procedureId;

	@Transient
	private FinanceWriteoffReceive master;

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public String getProcedureName()
	{
		return procedureName;
	}

	public void setProcedureName(String procedureName)
	{
		this.procedureName = procedureName;
	}

	public Long getProcedureId()
	{
		return procedureId;
	}

	public void setProcedureId(Long procedureId)
	{
		this.procedureId = procedureId;
	}

	public FinanceWriteoffReceive getMaster()
	{
		return master;
	}

	public void setMaster(FinanceWriteoffReceive master)
	{
		this.master = master;
	}

}
