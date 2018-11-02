/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.outsource;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.collect.Lists;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 发外管理 - 发外到货:发外到货明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月7日 下午16:11:38, liudong
 * @version 	   2.0, 2018年2月23日下午3:58:36, zhengby, 代码规范
 */
@Entity
@Table(name = "outsource_arrive_detail")
public class OutSourceArriveDetail extends OutSourceDetailBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 已对账数量
	 */
	private BigDecimal reconcilQty = new BigDecimal(0);

	/**
	 * 已退货数量
	 */
	private BigDecimal returnQty = new BigDecimal(0);

	// /**
	// * 已对账数量
	// */
	// @Transient
	// private BigDecimal reconcilQty2=new BigDecimal(0);

	// /**
	// * 已退货数量
	// */
	// @Transient
	// private BigDecimal returnQty2=new BigDecimal(0);
	/**
	 *已退货金额 
	 */
	private BigDecimal returnMoney = new BigDecimal(0);

	/**
	 * 已对账金额
	 */
	private BigDecimal reconcilMoney = new BigDecimal(0);

	/**
	 * 仓库ID
	 */
	private Long warehouseId;

	/**
	 * 加工单号
	 */
	private String outSourceBillNo;

	/**
	 * 主表对象
	 */
	@Transient
	private OutSourceArrive master;

	/**
	 * 产品名称列表
	 */
	@Transient
	private List<WorkProduct> productList = Lists.newArrayList();

	/**
	 * 产品名称字符串
	 */
	@Transient
	private String productNames;

	public BigDecimal getReconcilQty()
	{
		return reconcilQty;
	}

	public void setReconcilQty(BigDecimal reconcilQty)
	{
		this.reconcilQty = reconcilQty;
	}

	public BigDecimal getReconcilMoney()
	{
		return reconcilMoney;
	}

	public void setReconcilMoney(BigDecimal reconcilMoney)
	{
		this.reconcilMoney = reconcilMoney;
	}

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public OutSourceArrive getMaster()
	{
		return master;
	}

	public void setMaster(OutSourceArrive master)
	{
		this.master = master;
	}

	public BigDecimal getReturnQty()
	{
		return returnQty;
	}

	public void setReturnQty(BigDecimal returnQty)
	{
		this.returnQty = returnQty;
	}

	public BigDecimal getReturnMoney()
	{
		return returnMoney;
	}

	public String getOutSourceBillNo()
	{
		return outSourceBillNo;
	}

	public void setOutSourceBillNo(String outSourceBillNo)
	{
		this.outSourceBillNo = outSourceBillNo;
	}

	public void setReturnMoney(BigDecimal returnMoney)
	{
		this.returnMoney = returnMoney;
	}

	public String getWarehouseName()
	{
		if (warehouseId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.WAREHOUSE.name(), warehouseId, "name");
		}
		return "-";
	}

	public List<WorkProduct> getProductList()
	{
		return productList;
	}

	public void setProductList(List<WorkProduct> productList)
	{
		this.productList = productList;
	}

	public void setProductNames(String productNames)
	{
		this.productNames = productNames;
	}

	public String getProductNames()
	{
		StringBuilder sb = new StringBuilder();
		if (this.productList != null && this.productList.size() > 0)
		{
			for (WorkProduct p : this.productList)
			{
				sb.append(p.getProductName()).append(",");
			}
		}
		else
		{
			sb.append("-");
		}
		return StringUtils.removeEnd(sb.toString(), ",");
	}
}
