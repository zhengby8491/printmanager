/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.outsource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.OutSourceType;
import com.huayin.printmanager.persist.enumerate.ProgressStatusOutsource;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 发外管理 - 发外加工:发外加工表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月7日, liudong
 * @version 	   2.0, 2018年2月23日下午4:03:02, zhengby, 代码规范
 */
@Entity
@Table(name = "outsource_process")
public class OutSourceProcess extends OutSourceBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 发外类型
	 */
	@Column(length = 30)
	@Enumerated(EnumType.STRING)
	private OutSourceType type;

	/**
	 * 送货方式
	 */
	private Long deliveryClassId;

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

	/**
	 * 进度状态
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private ProgressStatusOutsource progressStatus;

	/**
	 * 加工要求
	 */
	private String requirements;

	@Transient
	private List<OutSourceProcessDetail> detailList = new ArrayList<OutSourceProcessDetail>();
	
	

	public ProgressStatusOutsource getProgressStatus()
	{
		return progressStatus;
	}

	public void setProgressStatus(ProgressStatusOutsource progressStatus)
	{
		this.progressStatus = progressStatus;
	}

	public String getRequirements()
	{
		return requirements;
	}

	public void setRequirements(String requirements)
	{
		this.requirements = requirements;
	}

	public Long getDeliveryClassId()
	{
		return deliveryClassId;
	}

	public void setDeliveryClassId(Long deliveryClassId)
	{
		this.deliveryClassId = deliveryClassId;
	}

	public List<OutSourceProcessDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<OutSourceProcessDetail> detailList)
	{
		this.detailList = detailList;
	}

	public OutSourceType getType()
	{
		return type;
	}

	public void setType(OutSourceType type)
	{
		this.type = type;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}
	
	// ------------------------------------------------

	public String getTypeText()
	{

		if (type != null)
		{
			return type.getText();
		}
		return "-";
	}

	public String getDeliveryClassName()
	{
		if (deliveryClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.DELIVERYCLASS.name(), deliveryClassId, "name");
		}
		return "-";
	}

	public String getProgressStatusText()
	{

		if (progressStatus != null)
		{

			return progressStatus.getText();
		}
		return "-";
	}
}
