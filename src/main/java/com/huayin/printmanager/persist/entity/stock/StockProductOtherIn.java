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
 * 库存管理 - 成品其它入库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_product_otherin")
public class StockProductOtherIn extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 入库仓库ID
	 */
	private Long warehouseId;

	/**
	 * 入库人ID
	 */
	@Column(length = 50)
	private Long employeeId;

	/**
	 * 入库人姓名
	 */
	private String employeeName;

	/**
	 * 入库时间
	 */
	private Date inTime;

	/**
	 * 子表
	 */
	@Transient
	private List<StockProductOtherInDetail> detailList;

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

	public Date getInTime()
	{
		return inTime;
	}

	public void setInTime(Date inTime)
	{
		this.inTime = inTime;
	}

	public List<StockProductOtherInDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<StockProductOtherInDetail> detailList)
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
