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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 生产退料
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_material_return")
public class StockMaterialReturn extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 仓库ID
	 */
	private Long warehouseId;

	/**
	 * 退料人ID
	 */
	private Long returnEmployeeId;

	/**
	 * 退料人姓名
	 */
	private String returnEmployeeName;

	/**
	 * 收料人ID
	 */
	private Long receiveEmployeeId;

	/**
	 * 收料人姓名
	 */
	private String receiveEmployeeName;

	/**
	 * 退料日期
	 */
	private Date returnTime;

	/**
	 * 工单编号
	 */
	private String workBillNo;

	/**
	 * 工单ID
	 */
	private Long workId;

	/**
	 * 工单类型
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private BillType workBillType;

	/**
	 * 子表
	 */
	@Transient
	private List<StockMaterialReturnDetail> detailList;

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public Long getReturnEmployeeId()
	{
		return returnEmployeeId;
	}

	public void setReturnEmployeeId(Long returnEmployeeId)
	{
		this.returnEmployeeId = returnEmployeeId;
	}

	public Long getReceiveEmployeeId()
	{
		return receiveEmployeeId;
	}

	public void setReceiveEmployeeId(Long receiveEmployeeId)
	{
		this.receiveEmployeeId = receiveEmployeeId;
	}

	public Date getReturnTime()
	{
		return returnTime;
	}

	public void setReturnTime(Date returnTime)
	{
		this.returnTime = returnTime;
	}

	public String getReturnEmployeeName()
	{
		return returnEmployeeName;
	}

	public void setReturnEmployeeName(String returnEmployeeName)
	{
		this.returnEmployeeName = returnEmployeeName;
	}

	public String getReceiveEmployeeName()
	{
		return receiveEmployeeName;
	}

	public void setReceiveEmployeeName(String receiveEmployeeName)
	{
		this.receiveEmployeeName = receiveEmployeeName;
	}

	public List<StockMaterialReturnDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<StockMaterialReturnDetail> detailList)
	{
		this.detailList = detailList;
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

	public BillType getWorkBillType()
	{
		return workBillType;
	}

	public void setWorkBillType(BillType workBillType)
	{
		this.workBillType = workBillType;
	}

	public String getWarehouseName()
	{
		if (warehouseId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.WAREHOUSE.name(), warehouseId, "name");
		}
		return "-";
	}

	public String getWorkBillTypeText()
	{
		if (workBillType != null)
		{
			return workBillType.getText();
		}
		return "-";
	}

}
