/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月24日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.stock;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料盘点单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_material_inventory")
public class StockMaterialInventory extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 盘点人ID
	 */
	@Column(length = 50)
	private Long employeeId;

	/**
	 * 盘点人姓名
	 */
	private String employeeName;

	/**
	 * 盘点仓库ID
	 */
	private Long warehouseId;

	/**
	 * 盘点日期
	 */
	private Date inventoryTime;

	/**
	 * 子表
	 */
	@Transient
	private List<StockMaterialInventoryDetail> detailList;

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

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public Date getInventoryTime()
	{
		return inventoryTime;
	}

	public void setInventoryTime(Date inventoryTime)
	{
		this.inventoryTime = inventoryTime;
	}

	public List<StockMaterialInventoryDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<StockMaterialInventoryDetail> detailList)
	{
		this.detailList = detailList;
	}

	public String getWarehouseName()
	{
		if (warehouseId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.WAREHOUSE.name(), warehouseId, "name");
		}
		return "-";
	}
}
