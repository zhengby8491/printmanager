/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年2月23日上午10:55:00
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.produce.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;

/**
 * <pre>
 * 生产管理 - 工单用料分析
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年10月28日, sunxl
 * @version 	   2.0, 2018年2月23日上午10:55:07, zhengby, 代码规范
 */
public class WorkMaterialVo
{

	private BoolValue isCheck;

	private BillType billType;

	private Date createTime;

	private String billNo;

	private String partName;

	/**
	 * 工单ID
	 */
	private Long workId;

	private Integer workPartQty;

	private String workPartStyle;

	private String materialName;

	private Integer splitQty;

	private String materialStyle;

	private BigDecimal materialQty;

	private BigDecimal takeQty;

	private BigDecimal supplementQty;

	private BigDecimal returnQty;

	public BoolValue getIsCheck()
	{
		return isCheck;
	}

	public void setIsCheck(BoolValue isCheck)
	{
		this.isCheck = isCheck;
	}

	public BillType getBillType()
	{
		return billType;
	}

	public void setBillType(BillType billType)
	{
		this.billType = billType;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public String getPartName()
	{
		return partName;
	}

	public void setPartName(String partName)
	{
		this.partName = partName;
	}

	public Long getWorkId()
	{
		return workId;
	}

	public void setWorkId(Long workId)
	{
		this.workId = workId;
	}

	public Integer getWorkPartQty()
	{
		return workPartQty;
	}

	public void setWorkPartQty(Integer workPartQty)
	{
		this.workPartQty = workPartQty;
	}

	public String getWorkPartStyle()
	{
		return workPartStyle;
	}

	public void setWorkPartStyle(String workPartStyle)
	{
		this.workPartStyle = workPartStyle;
	}

	public String getMaterialName()
	{
		return materialName;
	}

	public void setMaterialName(String materialName)
	{
		this.materialName = materialName;
	}

	public Integer getSplitQty()
	{
		return splitQty;
	}

	public void setSplitQty(Integer splitQty)
	{
		this.splitQty = splitQty;
	}

	public String getMaterialStyle()
	{
		return materialStyle;
	}

	public void setMaterialStyle(String materialStyle)
	{
		this.materialStyle = materialStyle;
	}

	public BigDecimal getMaterialQty()
	{
		return materialQty;
	}

	public void setMaterialQty(BigDecimal materialQty)
	{
		this.materialQty = materialQty;
	}

	public BigDecimal getTakeQty()
	{
		return takeQty;
	}

	public void setTakeQty(BigDecimal takeQty)
	{
		this.takeQty = takeQty;
	}

	public BigDecimal getSupplementQty()
	{
		return supplementQty;
	}

	public void setSupplementQty(BigDecimal supplementQty)
	{
		this.supplementQty = supplementQty;
	}

	public BigDecimal getReturnQty()
	{
		return returnQty;
	}

	public void setReturnQty(BigDecimal returnQty)
	{
		this.returnQty = returnQty;
	}

	public String getBillTypeText()
	{

		if (billType != null)
		{
			return billType.getText();
		}
		return "-";
	}

}
