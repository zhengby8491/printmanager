/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.stock;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品调拨单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_product_transfer")
public class StockProductTransfer extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 调拨人id
	 */
	private Long employeeId;

	/**
	 * 调拨人姓名
	 */
	private String employeeName;

	/**
	 * 调拨日期
	 */
	private Date transferTime;

	/**
	 * 调出仓库ID
	 */
	private Long outWarehouseId;

	/**
	 * 调入仓库id
	 */
	private Long inWarehouseId;

	/**
	 * 子表
	 */
	@Transient
	private List<StockProductTransferDetail> detailList;

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	public String getEmployeeName()
	{
		return employeeName;
	}

	public void setEmployeeName(String employeeName)
	{
		this.employeeName = employeeName;
	}

	public Date getTransferTime()
	{
		return transferTime;
	}

	public void setTransferTime(Date transferTime)
	{
		this.transferTime = transferTime;
	}

	public Long getOutWarehouseId()
	{
		return outWarehouseId;
	}

	public void setOutWarehouseId(Long outWarehouseId)
	{
		this.outWarehouseId = outWarehouseId;
	}

	public Long getInWarehouseId()
	{
		return inWarehouseId;
	}

	public void setInWarehouseId(Long inWarehouseId)
	{
		this.inWarehouseId = inWarehouseId;
	}

	public List<StockProductTransferDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<StockProductTransferDetail> detailList)
	{
		this.detailList = detailList;
	}

	public String getOutWarehouseName()
	{
		if (outWarehouseId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.WAREHOUSE.name(), outWarehouseId, "name");
		}
		return "-";
	}

	public String getInWarehouseName()
	{
		if (inWarehouseId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.WAREHOUSE.name(), inWarehouseId, "name");
		}
		return "-";
	}

}
