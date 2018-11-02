/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月22日 下午5:46:00
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.vo;

import java.math.BigDecimal;

/**
 * <pre>
 * 统计应收款金额
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月22日下午5:46:00, zhengby
 */
public class SumBeginMoneyVo
{
	// 订单总金额
	BigDecimal money = new BigDecimal(0);
	
	// 已收金额
	BigDecimal receiveMoney = new BigDecimal(0);

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public BigDecimal getReceiveMoney()
	{
		return receiveMoney;
	}

	public void setReceiveMoney(BigDecimal receiveMoney)
	{
		this.receiveMoney = receiveMoney;
	}
}
