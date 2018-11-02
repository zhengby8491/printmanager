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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;

/**
 * <pre>
 * 库存管理 - 生产领料
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_material_take")
public class StockMaterialTake extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 仓库ID
	 */
	private Long warehouseId;

	/**
	 * 仓库名称
	 */
	private String warehouseName;

	/**
	 * 发料人ID
	 */
	private Long sendEmployeeId;

	/**
	 * 发料人姓名
	 */
	private String sendEmployeeName;

	/**
	 * 收料人ID
	 */
	private Long receiveEmployeeId;

	/**
	 * 收料人姓名
	 */
	private String receiveEmployeeName;

	/**
	 * 领料日期
	 */
	private Date takeTime;

	/**
	 * 子表
	 */
	@Transient
	private List<StockMaterialTakeDetail> detailList;

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
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName)
	{
		this.warehouseName = warehouseName;
	}

	public Long getSendEmployeeId()
	{
		return sendEmployeeId;
	}

	public void setSendEmployeeId(Long sendEmployeeId)
	{
		this.sendEmployeeId = sendEmployeeId;
	}

	public Long getReceiveEmployeeId()
	{
		return receiveEmployeeId;
	}

	public void setReceiveEmployeeId(Long receiveEmployeeId)
	{
		this.receiveEmployeeId = receiveEmployeeId;
	}

	public Date getTakeTime()
	{
		return takeTime;
	}

	public void setTakeTime(Date takeTime)
	{
		this.takeTime = takeTime;
	}

	public String getSendEmployeeName()
	{
		return sendEmployeeName;
	}

	public void setSendEmployeeName(String sendEmployeeName)
	{
		this.sendEmployeeName = sendEmployeeName;
	}

	public String getReceiveEmployeeName()
	{
		return receiveEmployeeName;
	}

	public void setReceiveEmployeeName(String receiveEmployeeName)
	{
		this.receiveEmployeeName = receiveEmployeeName;
	}

	public List<StockMaterialTakeDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<StockMaterialTakeDetail> detailList)
	{
		this.detailList = detailList;
	}

}
