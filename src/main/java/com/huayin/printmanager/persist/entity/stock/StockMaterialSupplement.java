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
 * 库存管理 - 生产补料
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_material_supplement")
public class StockMaterialSupplement extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 仓库ID
	 */
	private Long warehouseId;

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
	 * 补料日期
	 */
	private Date supplementTime;

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
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private BillType workBillType;

	/**
	 * 子表
	 */
	@Transient
	private List<StockMaterialSupplementDetail> detailList;

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public Long getSendEmployeeId()
	{
		return sendEmployeeId;
	}

	public BillType getWorkBillType()
	{
		return workBillType;
	}

	public void setWorkBillType(BillType workBillType)
	{
		this.workBillType = workBillType;
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

	public Date getSupplementTime()
	{
		return supplementTime;
	}

	public void setSupplementTime(Date supplementTime)
	{
		this.supplementTime = supplementTime;
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

	public List<StockMaterialSupplementDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<StockMaterialSupplementDetail> detailList)
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

	public String getWorkBillTypeText()
	{
		if (workBillType != null)
		{
			return workBillType.getText();
		}
		return "-";
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
