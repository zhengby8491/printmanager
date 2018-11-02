/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月8日 上午9:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.oem;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

/**
 * <pre>
 * 代加工管理  ： 代工单明细表信息基础类子类
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月8日上午9:34:06, zhengby
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class OemBaseEntityDetailSub extends OemBaseEntityDetail
{
	private static final long serialVersionUID = -7123836243155091385L;

	/**
	 * 代工单id
	 */
	private Long oemOrderBillId;

	/**
	 * 代工单号
	 */
	private String oemOrderBillNo;
	
	public Long getOemOrderBillId()
	{
		return oemOrderBillId;
	}

	public void setOemOrderBillId(Long oemOrderBillId)
	{
		this.oemOrderBillId = oemOrderBillId;
	}

	public String getOemOrderBillNo()
	{
		return oemOrderBillNo;
	}

	public void setOemOrderBillNo(String oemOrderBillNo)
	{
		this.oemOrderBillNo = oemOrderBillNo;
	}
}
