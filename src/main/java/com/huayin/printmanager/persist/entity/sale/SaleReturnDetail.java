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

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 - 销售退货：销售退货明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年5月24日 上午9:55:56	
 * @version 	   2.0, 2018年2月22日下午6:45:39, zhengby, 代码规范
 */
@Entity
@Table(name = "sale_return_detail")
public class SaleReturnDetail extends SaleDetailBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 仓库号
	 */
	private Long warehouseId;

	/**
	 * 客户订单单据编号
	 */
	@Column(length = 50)
	private String customerBillNo;

	/**
	 * 已对帐数量
	 */
	private Integer reconcilQty=0;
	
	/**
	 * 未对帐数量，临时返回属性，不要生成字段
	 */
	@Transient
	private Integer reconcilQty2=0;
	
	/**
	 * 已对账金额
	 */
	private BigDecimal reconcilMoney=new BigDecimal(0);

	/**
	 * 退货单
	 */
	@Transient
	private SaleReturn master;

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public Integer getReconcilQty()
	{
		return reconcilQty;
	}

	public String getCustomerBillNo()
	{
		return customerBillNo;
	}

	public void setCustomerBillNo(String customerBillNo)
	{
		this.customerBillNo = customerBillNo;
	}

	public void setReconcilQty(Integer reconcilQty)
	{
		this.reconcilQty = reconcilQty;
	}

	public SaleReturn getMaster()
	{
		return master;
	}

	public void setMaster(SaleReturn master)
	{
		this.master = master;
	}

	public BigDecimal getReconcilMoney()
	{
		return reconcilMoney;
	}

	public void setReconcilMoney(BigDecimal reconcilMoney)
	{
		this.reconcilMoney = reconcilMoney;
	}

	public String getWarehouseName()
	{
		if (warehouseId != null)
		{
			return (String)UserUtils.getBasicInfoFiledValue(BasicType.WAREHOUSE.name(), warehouseId,"name");
		}
		return "-";
	}

	public Integer getReconcilQty2()
	{
		return reconcilQty2;
	}

	public void setReconcilQty2(Integer reconcilQty2)
	{
		this.reconcilQty2 = reconcilQty2;
	}
}
