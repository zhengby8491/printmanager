/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年02月22日 下午17:53:23
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.sale;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;

/**
 * <pre>
 * 销售管理 - 销售订单：(成品)销售装订、打包、粘合
 * </pre>
 * @author think
 * @version 1.0, 2017年9月18日
 */
@Entity
@Table(name = "sale_order_pack")
public class SaleOrderPack extends BaseBillDetailTableEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 工单对象
	 */
	@Transient
	private SaleOrderDetail master;

	/**
	 * 材料列表
	 */
	@Transient
	List<SaleOrderMaterial> materialList = new ArrayList<SaleOrderMaterial>();

	/**
	 * 成品工序列表
	 */
	@Transient
	List<SaleOrderProcedure> procedureList = new ArrayList<SaleOrderProcedure>();

	@Transient
	private String style;

	public SaleOrderDetail getMaster()
	{
		return master;
	}

	public void setMaster(SaleOrderDetail master)
	{
		this.master = master;
	}

	public List<SaleOrderMaterial> getMaterialList()
	{
		return materialList;
	}

	public void setMaterialList(List<SaleOrderMaterial> materialList)
	{
		this.materialList = materialList;
	}

	public List<SaleOrderProcedure> getProcedureList()
	{
		return procedureList;
	}

	public void setProcedureList(List<SaleOrderProcedure> procedureList)
	{
		this.procedureList = procedureList;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

}
