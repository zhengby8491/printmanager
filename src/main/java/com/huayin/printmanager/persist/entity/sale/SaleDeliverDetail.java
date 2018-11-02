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
 * 销售管理 - 销售送货：销售送货明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年5月24日 上午9:25:17, zhong
 * @version 	   2.0, 2018年2月22日下午6:01:51, zhengby, 代码规范
 */
@Entity
@Table(name = "sale_deliver_detail")
public class SaleDeliverDetail extends SaleDetailBaseEntity
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
	 * 退货数量
	 */
	private Integer returnQty=0;
	
	/**
	 * 未退货数量，临时返回属性，不要生成字段
	 */
	@Transient
	private Integer returnQty2=0;
	
	/**
	 * 退货金额
	 */
	private BigDecimal returnMoney=new BigDecimal(0);

	/**
	 * 对帐数量
	 */
	private Integer reconcilQty=0;
	
	/**
	 * 未对帐数量，临时返回属性，不要生成字段
	 */
	@Transient
	private Integer reconcilQty2=0;
	
	/**
	 * 对账金额
	 */
	private BigDecimal reconcilMoney=new BigDecimal(0);

	/**
	 * 销售订单信息
	 */
	@Transient
	private SaleDeliver master;

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public String getCustomerBillNo()
	{
		return customerBillNo;
	}

	public void setCustomerBillNo(String customerBillNo)
	{
		this.customerBillNo = customerBillNo;
	}

	public Integer getReturnQty()
	{
		return returnQty;
	}

	public void setReturnQty(Integer returnQty)
	{
		this.returnQty = returnQty;
	}

	public Integer getReconcilQty()
	{
		return reconcilQty;
	}

	public void setReconcilQty(Integer reconcilQty)
	{
		this.reconcilQty = reconcilQty;
	}

	public SaleDeliver getMaster()
	{
		return master;
	}

	public void setMaster(SaleDeliver master)
	{
		this.master = master;
	}

	public BigDecimal getReturnMoney()
	{
		return returnMoney;
	}

	public void setReturnMoney(BigDecimal returnMoney)
	{
		this.returnMoney = returnMoney;
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
		if(warehouseId!=null){
			return (String)UserUtils.getBasicInfoFiledValue(BasicType.WAREHOUSE.name(), warehouseId,"name");
		}
		return "-";
	}

	public Integer getReturnQty2()
	{
		return returnQty2;
	}

	public void setReturnQty2(Integer returnQty2)
	{
		this.returnQty2 = returnQty2;
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
