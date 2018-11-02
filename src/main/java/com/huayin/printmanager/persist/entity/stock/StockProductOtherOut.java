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
 * 库存管理 - 成品其它出库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_product_otherout")
public class StockProductOtherOut extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 出库人ID
	 */
	@Column(length = 50)
	private Long employeeId;

	/**
	 * 出库人姓名
	 */
	private String employeeName;

	/**
	 * 出库时间
	 */
	private Date outTime;

	/**
	 * 仓库ID
	 */
	private Long warehouseId;

	/**
	 * 子表
	 */
	@Transient
	private List<StockProductOtherOutDetail> detailList;

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

	public Date getOutTime()
	{
		return outTime;
	}

	public void setOutTime(Date outTime)
	{
		this.outTime = outTime;
	}

	public List<StockProductOtherOutDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<StockProductOtherOutDetail> detailList)
	{
		this.detailList = detailList;
	}

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
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
