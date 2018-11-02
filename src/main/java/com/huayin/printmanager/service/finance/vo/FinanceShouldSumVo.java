/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.finance.vo;

import java.math.BigDecimal;

/**
 * <pre>
 * 财务管理 - 财务汇总VO
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年10月14日
 * @since 	  	 2.0, 2017年12月27日下午3:23:07,zhengby,代码重构
 */
public class FinanceShouldSumVo
{
	private Long id;

	private String name;

	private String type;

	/**
	 * 期初应付/收
	 */
	private BigDecimal beginMoney = new BigDecimal(0);

	/**
	 * 本期应付/收
	 */
	private BigDecimal shouldMoney = new BigDecimal(0);

	/**
	 * 本期预付/收
	 */
	private BigDecimal advance = new BigDecimal(0);

	/**
	 * 本期实付/收
	 */
	private BigDecimal money = new BigDecimal(0);
	
	/**
	 * 调整金额
	 */
	private BigDecimal adjustMoney = new BigDecimal(0);
	
	/**
	 * 本期折扣 
	 */
	private BigDecimal discount = new BigDecimal(0);

	/**
	 * 预付/收余额
	 */
	private BigDecimal surAdvance = new BigDecimal(0);
	
	/**
	 * 供应商id
	 */
	private Long supplierId;

	/**
	 * 客户id
	 */
	private Long customerId;

	/**
	 * 已付款金额
	 */
	private BigDecimal paymentMoney;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public BigDecimal getBeginMoney()
	{
		return beginMoney;
	}

	public void setBeginMoney(BigDecimal beginMoney)
	{
		this.beginMoney = beginMoney;
	}

	public BigDecimal getShouldMoney()
	{
		return shouldMoney;
	}

	public void setShouldMoney(BigDecimal shouldMoney)
	{
		this.shouldMoney = shouldMoney;
	}

	public BigDecimal getAdvance()
	{
		return advance;
	}

	public void setAdvance(BigDecimal advance)
	{
		this.advance = advance;
	}

	public BigDecimal getAdjustMoney()
	{
		return adjustMoney;
	}

	public void setAdjustMoney(BigDecimal adjustMoney)
	{
		this.adjustMoney = adjustMoney;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public BigDecimal getDiscount()
	{
		return discount;
	}

	public void setDiscount(BigDecimal discount)
	{
		this.discount = discount;
	}

	public BigDecimal getSurAdvance()
	{
		return surAdvance;
	}

	public void setSurAdvance(BigDecimal surAdvance)
	{
		this.surAdvance = surAdvance;
	}

	public FinanceShouldSumVo(Long id, String name, BigDecimal beginMoney, BigDecimal shouldMoney, BigDecimal advance, BigDecimal adjustMoney, BigDecimal money, BigDecimal discount, BigDecimal surAdvance)
	{
		super();
		this.id = id;
		this.name = name;
		this.beginMoney = beginMoney;
		this.shouldMoney = shouldMoney;
		this.advance = advance;
		this.adjustMoney = adjustMoney;
		this.money = money;
		this.discount = discount;
		this.surAdvance = surAdvance;
	}

	public FinanceShouldSumVo()
	{
		super();
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getSupplierId()
	{
		return supplierId;
	}

	public void setSupplierId(Long supplierId)
	{
		this.supplierId = supplierId;
	}

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public BigDecimal getPaymentMoney()
	{
		return paymentMoney;
	}

	public void setPaymentMoney(BigDecimal paymentMoney)
	{
		this.paymentMoney = paymentMoney;
	}

}
