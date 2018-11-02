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
 * 采购管理 - 采购退货：采购退货明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年5月24日 下午3:51:27, zhong
 * @version 	   1.0, 2018年2月23日上午11:38:51, zhengby, 代码规范
 */
@Entity
@Table(name = "purch_refund_detail")
public class PurchRefundDetail extends PurchDetailBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 生产工单单号
	 */
	private String workBillNo;

	/**
	 * 生产工单id
	 */
	private Long workId;

	/**
	 * 仓库ID
	 */
	private Long warehouseId;

	/**
	 * 已对账数量
	 */
	private BigDecimal reconcilQty = new BigDecimal(0);

	/**
	 * 采购单号
	 */
	private String orderBillNo;

	@Transient
	private PurchRefund master;

	/**
	 * 产品名称列表
	 */
	@Transient
	private List<WorkProduct> productList = Lists.newArrayList();

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

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public PurchRefund getMaster()
	{
		return master;
	}

	public void setMaster(PurchRefund master)
	{
		this.master = master;
	}

	public BigDecimal getReconcilQty()
	{
		return reconcilQty;
	}

	public void setReconcilQty(BigDecimal reconcilQty)
	{
		this.reconcilQty = reconcilQty;
	}

	public String getOrderBillNo()
	{
		return orderBillNo;
	}

	public void setOrderBillNo(String orderBillNo)
	{
		this.orderBillNo = orderBillNo;
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
