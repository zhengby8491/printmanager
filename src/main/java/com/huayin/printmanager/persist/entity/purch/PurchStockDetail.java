/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.purch;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
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
 * 采购管理 - 采购入库 ：采购入库明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月8日, liudong
 * @version 	   2.0, 2018年2月23日上午11:40:23, zhengby, 代码规范
 */
@Entity
@Table(name = "purch_stock_detail")
public class PurchStockDetail extends PurchDetailBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 已对账数量
	 */
	private BigDecimal reconcilQty = new BigDecimal(0);

	/**
	 * 已退货数量
	 */
	private BigDecimal refundQty = new BigDecimal(0);

	/**
	 * 赠送数量
	 */
	@Column(columnDefinition = "decimal(19,2) default '0'")
	private BigDecimal freeQty = new BigDecimal(0);

	/**
	 * 赠送计价数量
	 */
	@Column(columnDefinition = "decimal(19,4) default '0.0000'")
	private BigDecimal freeValuationQty = new BigDecimal(0);

	/**
	 * 仓库ID
	 */
	private Long warehouseId;

	/**
	 * 生产工单单号
	 */
	private String workBillNo;
	
	/**
	 * 生产工单id
	 */
	private Long workId;

	/**
	 * 采购入库单主表
	 */
	@Transient
	private PurchStock master;

	/**
	 * 产品名称列表
	 */
	@Transient
	private List<WorkProduct> productList = Lists.newArrayList();
	
	/**
	 * 产品名称字符串（因为页面实现不一样，需要单独加上字段显示）
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

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public String getWorkBillNo()
	{
		return workBillNo;
	}

	public void setWorkBillNo(String workBillNo)
	{
		this.workBillNo = workBillNo;
	}

	public Long getWorkId()
	{
		return workId;
	}

	public void setWorkId(Long workId)
	{
		this.workId = workId;
	}

	public PurchStock getMaster()
	{
		return master;
	}

	public void setMaster(PurchStock master)
	{
		this.master = master;
	}

	public BigDecimal getRefundQty()
	{
		return refundQty;
	}

	public void setRefundQty(BigDecimal refundQty)
	{
		this.refundQty = refundQty;
	}

	public BigDecimal getFreeQty()
	{
		return freeQty;
	}

	public void setFreeQty(BigDecimal freeQty)
	{
		this.freeQty = freeQty;
	}

	public BigDecimal getFreeValuationQty()
	{
		return freeValuationQty;
	}

	public void setFreeValuationQty(BigDecimal freeValuationQty)
	{
		this.freeValuationQty = freeValuationQty;
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
