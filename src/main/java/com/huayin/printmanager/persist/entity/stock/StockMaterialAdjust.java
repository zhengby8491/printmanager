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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料库存调整单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_material_adjust")
public class StockMaterialAdjust extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 调整人ID
	 */
	@Column(length = 50)
	private Long employeeId;

	/**
	 * 调整人姓名
	 */
	private String employeeName;

	/**
	 * 调整日期
	 */
	private Date adjustTime;

	/**
	 * 调整仓库ID
	 */
	private Long warehouseId;

	/**
	 * 子表实体
	 */
	@Transient
	private List<StockMaterialAdjustDetail> detailList;

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

	public Date getAdjustTime()
	{
		return adjustTime;
	}

	public void setAdjustTime(Date adjustTime)
	{
		this.adjustTime = adjustTime;
	}

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public List<StockMaterialAdjustDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<StockMaterialAdjustDetail> detailList)
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
