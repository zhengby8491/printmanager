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
 * 库存管理 - 成品调整
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_product_adjust")
public class StockProductAdjust extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 仓库ID
	 */
	private Long warehouseId;

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
	 * 调整时间
	 */
	private Date adjustTime;

	/**
	 * 子表
	 */
	@Transient
	private List<StockProductAdjustDetail> detailList;

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

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

	public List<StockProductAdjustDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<StockProductAdjustDetail> detailList)
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
