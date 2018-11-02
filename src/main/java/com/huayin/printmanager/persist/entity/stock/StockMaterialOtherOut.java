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
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料其他出库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_material_otherout")
public class StockMaterialOtherOut extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

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
	 * 出库时间
	 */
	private Date outTime;

	/**
	 * 出库仓库ID
	 */
	private Long warehouseId;

	@Transient
	private List<StockMaterialOtherOutDetail> detailList;

	public Long getSendEmployeeId()
	{
		return sendEmployeeId;
	}

	public void setSendEmployeeId(Long sendEmployeeId)
	{
		this.sendEmployeeId = sendEmployeeId;
	}

	public String getSendEmployeeName()
	{
		return sendEmployeeName;
	}

	public void setSendEmployeeName(String sendEmployeeName)
	{
		this.sendEmployeeName = sendEmployeeName;
	}

	public Long getReceiveEmployeeId()
	{
		return receiveEmployeeId;
	}

	public void setReceiveEmployeeId(Long receiveEmployeeId)
	{
		this.receiveEmployeeId = receiveEmployeeId;
	}

	public String getReceiveEmployeeName()
	{
		return receiveEmployeeName;
	}

	public void setReceiveEmployeeName(String receiveEmployeeName)
	{
		this.receiveEmployeeName = receiveEmployeeName;
	}

	public Date getOutTime()
	{
		return outTime;
	}

	public void setOutTime(Date outTime)
	{
		this.outTime = outTime;
	}

	public List<StockMaterialOtherOutDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<StockMaterialOtherOutDetail> detailList)
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
